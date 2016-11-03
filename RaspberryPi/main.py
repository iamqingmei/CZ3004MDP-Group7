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
command = ""
moves = 0

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
						if len(receiveData) == 6:
							global moves
							moves += 1
						incomingMessageQueue.put(receiveData)
				else:
					time.sleep(0.001)

class timerThread(threading.Thread):
	def __init__(self):
		threading.Thread.__init__(self)

	def run(self):
		while True:
			if(timer.checkTimer()):
				timer.stopTimer()
				incomingMessageQueue.put("PC2ARS")
				print "Timeout. Requesting for Sensor Data"
			

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
						a = receiveData.split('|')
						fast = re.search(r'Fastest',a[1])
						correctSensor = re.search(r'-?[0-9]+:-?[0-9]+:-?[0-9]+:-?[0-9]+:-?[0-9]+',a[1])

						if correctSensor:
							if moves != int(a[2]):
								timer.stopTimer()
								print "Total moves don't tally"
								repeatMove = "PC2AR" + command
								print "repeatMove: " + repeatMove
								incomingMessageQueue.put(repeatMove)
							else:
								incomingMessageQueue.put(receiveData)
								timer.stopTimer()
						elif fast:
							print "Fastest path received"
							incomingMessageQueue.put(receiveData)
							timer.stopTimer()
						else:
							print "Wrong format received. Requesting new sensor data"
							incomingMessageQueue.put("PC2ARS")
							timer.stopTimer()
							
					else:
						time.sleep(0.001)
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
				b = outgoingMessageQueue.get(True)
				if b[:2] != "AR":
					sender = b[:2]
					receiver = b[3:5]
					data = b[5:]
				else:
					outgoingMessage = b.split("|")
					comm = outgoingMessage[0]
					sender = comm[:2]
					receiver = comm[3:5]
					data = outgoingMessage[1]

				if (sender == "PC" and data != "S"):
					global command
					command = data

				if receiver == "PC" :
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
