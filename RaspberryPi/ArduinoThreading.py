import serial
import threading

class ArduinoThreading:

	def __init__(self):
		self.ser = None
		self.isConnected = False
		self.isAck = False

	def connect(self):
		try:
			self.ser = serial.Serial('/dev/ttyACM0', 115200 , timeout=1)
			self.isConnected = True
		except: 
			pass

	def close(self):
		try:
			self.ser.close()
			self.isConnected = False
			print "Arduino Disconnected"
		except: 
			pass

	def connected(self):
		try:
			return self.isConnected
		except:
			pass

	def receive(self):
		try:
			data = ""
			data = self.ser.readline().strip()
			if(data != "" and data is not None):
				print "Sensor data received from Arduino: " + data
				return data
		except: 
			pass
	
	def send(self,data):
		try:
			self.ser.write(data.encode('utf-8'))
			print "Command sent to Arduino: " + str(data)
		except: 
			print "Error sending command to Arduino"
			pass
