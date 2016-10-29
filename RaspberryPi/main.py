import os
import Queue
import threading
import WifiThreading
import ArduinoThreading
import BluetoothThreading
import timerThread
import socket
import serial
import bluetooth
import time
import re
import sched

#Message Queue
incomingMessageQueue = Queue.Queue()
outgoingMessageQueue = Queue.Queue()

#Initialize Threads
wifi = WifiThreading.WifiThreading("192.168.7.1", 8080)
arduino = ArduinoThreading.ArduinoThreading()
android = BluetoothThreading.BluetoothThreading()
timer = timerThread.timerThread()
global command
command = ""
global message
message = ""

class wifiThread (threading.Thread):
	def __init__(self):
		threading.Thread.__init__(self)

	def run(self):
		while True:
			wifi.connect()
			if (wifi.connected()):
				receiveData = wifi.receive()
				if(receiveData != ""):
					if("closed" in receiveData):
						wifi.close()
						wifi.__init__("192.168.7.1", 8080)
					else:
						incomingMessageQueue.put(receiveData)
				else:
					time.sleep(0.001)

class timerThread(threading.Thread):
	def __init__(self):
		threading.Thread.__init__(self)

	def run(self):
		while True:
			if(timer.checkTimer()):
				if message = "":
					incomingMessageQueue.put("PC2ARS")
					print"Request for Sensor Data"
					timer.stopTimer()
			

class arduinoThread(threading.Thread):
	def __init__(self):
		threading.Thread.__init__(self)

	def run(self):
		while True:
			try:
				arduino.connect()
				if(arduino.connected()):
					receiveData = arduino.receive()
				
					if(receiveData != "" and receiveData is not None):
#						fast = re.search(r'\[Fastest]',receiveData)
#						correctSensor = re.search(r'\|-?[0-9]+:-?[0-9]+:-?[0-9]+:-?[0-9]+:-?[0-9]+',receiveData)
						incomingMessageQueue.put(receiveData)
						print"Robot sent: " + receiveData
						time.stopTimer()						
#						if correctSensor: 
#							incomingMessageQueue.put(receiveData)
#							print"message sent from robot: "+receiveData
#							timer.stopTimer()
#						elif fast:
#							incomingMessageQueue.put(receiveData)
#							timer.stopTimer()
							
					else:
#						time.sleep(0.001)
						time.sleep(1)
			except serial.SerialException:
				time.sleep(0.001)
			except Exception as ErrorMsg:
				time.sleep(0.001)

class androidThread (threading.Thread):
	def __init__(self):
		threading.Thread.__init__(self)

	def run(self):		
		while True:
			try:
				android.connect()
				if(android.connected()):
					receiveData = android.receive()
					if(receiveData != ""):
						incomingMessageQueue.put(receiveData)
					else:
						time.sleep(0.001)
			except bluetooth.BluetoothError:
				print "Connecting to Nexus 7 failed, retrying"
			except ValueError as ErrorMsg:
				pass
			except Exception as ErrorMsg:
				time.sleep(0.001)

class incomingMessageThread(threading.Thread):
	def __init__(self):
		threading.Thread.__init__(self)

	def run(self):
		while True:
			try:
				incomingMessage = incomingMessageQueue.get(True)
				outgoingMessageQueue.put(incomingMessage)
				incomingMessageQueue.task_done()
			except Exception as ErrorMsg:
				pass

class outgoingMessageThread(threading.Thread):
	def __init__(self):
		threading.Thread.__init__(self)

	
	def run(self):
		while True:
			try:
				outgoingMessage = outgoingMessageQueue.get(True)
				sender = outgoingMessage[:2]
				receiver = outgoingMessage[3:5]
				data = outgoingMessage[5:]

				if sender != "AR":
					global command
					global message
					command = outgoingMessage
					command = message

				if receiver == "PC" :
#					if sender == "AN"
#						print "Sending Ack to AN"
#						wifi.send("A")
#					elif 
					print "Sending message from " + sender + " to PC: " + data
					wifi.send(data)
					print "Message sent to PC: " + data
					if ("explore" in data):
						print "Sending explore to Arduino"
						arduino.send("E")
						print "Message sent to Arduino"
				elif receiver == "AN" :
					print "Sending message from " + sender + " to Android: " + data
					android.send(data)
					print "Message sent to Android"
				elif receiver == "AR" :
					print "Sending message from " + sender + " to Arduino: " + data
					arduino.send(data)
					print "Message sent to Arduino: "+ data
				
					timer.startTimer()
					
										

				outgoingMessageQueue.task_done()
				

			except BaseException as ErrorMsg:
				pass
			except Exception as ErrorMsg:
				time.sleep(0.001)

wifiThread = wifiThread()
arduinoThread = arduinoThread()
androidThread = androidThread()
timerThread = timerThread()

incomingMessageThread = incomingMessageThread()
outgoingMessageThread = outgoingMessageThread()

wifiThread.start()
arduinoThread.start()
androidThread.start()
timerThread.start()

incomingMessageThread.start()
outgoingMessageThread.start()

wifiThread.join()
arduinoThread.join()
androidThread.join()
timerThread.join()

incomingMessageThread.join()
outgoingMessageThread.join()
