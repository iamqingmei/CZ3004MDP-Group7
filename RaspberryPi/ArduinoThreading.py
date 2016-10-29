import serial
import threading
import time
import re

class ArduinoThreading:

	def __init__(self):
		self.ser = None
		self.isConnected = False
		self.isAck = False
		#self.lastCmd = "F"
		#self.mutex = threading.Lock()

	def connect(self):
        	#self.mutex.acquire()
		try:
			self.ser = serial.Serial('/dev/ttyACM0', 115200 , timeout=1)
			self.isConnected = True
		except: 
			pass
		#finally:
			#self.mutex.release()

	def close(self):
		#self.mutex.acquire()
		try:
			self.ser.close()
			self.isConnected = False
			print "Arduino Disconnected"
		except: 
			pass
		#finally:
			#self.mutex.release()

	def connected(self):
		#self.mutex.acquire()
		try:
			return self.isConnected
		except:
			pass
		#finally:
			#self.mutex.release()

	def receive(self):
		#self.mutex.acquire()
		try:
			data = ""
#			data = self.ser.readline().strip()
			data = self.ser.readline()
			if(data != "" and data is not None):
			 
				correctSensor = re.search(r'\|-?[0-9]+:-?[0-9]+:-?[0-9]+:-?[0-9]+:-?[0-9]+',data)
				fast = re.search(r'\(Fastest)',data)
				if correctSensor:
					print "Sensor data received from Arduino: " + data[5:]
					return data
				elif fast:
					print"Received Message from Arduino: "+ data
					return data
		except: 
			pass
		else:
			self.ser.flush()
#			print "Arduino serial flushed."
		#finally:
			#self.mutex.release()
	
	def send(self,data):
		#self.mutex.acquire()
		try:
			#print "lastCmd:" + self.lastCmd
			#print "data:" + data
			#self.lastCmd = data
			self.ser.write(data.encode('utf-8'))
			print "Command sent to Arduino: " + str(data)
		except: 
			print "Error sending command to Arduino"
			#self.send(self,data)
			pass
		#finally:
			#self.mutex.release()

	def receiveAck(self):
		print "Received Data!"
		self.timer.wait()
