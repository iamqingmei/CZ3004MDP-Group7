import serial
import threading

class ArduinoThreading:

	def __init__(self):
		self.ser = None
		self.isConnected = False
		self.mutex = threading.Lock()

	def connect(self):
        	self.mutex.acquire()
		try:
			self.ser = serial.Serial('/dev/ttyACM0', 115200 , timeout=1)
			self.isConnected = True
		finally:
			self.mutex.release()

	def close(self):
		self.mutex.acquire()
		try:
			self.ser.close()
			self.isConnected = False
			print "Arduino Disconnected"
		finally:
			self.mutex.release()

	def connected(self):
		self.mutex.acquire()
		try:
			return self.isConnected
		finally:
			self.mutex.release()

	def receive(self):
		self.mutex.acquire()
		try:
			data = ""
			data = self.ser.readline().strip()
			if(data != ""):
				print "Sensor data received from Arduino: " + data[5:]
			return data
		finally:
			self.mutex.release()
	
	def send(self,data):
		self.mutex.acquire()
		try:
			self.ser.write(data.encode('utf-8'))
			print "Command sent to Arduino: " + str(data)
		finally:
			self.mutex.release()