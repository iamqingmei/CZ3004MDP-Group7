import socket
import sys
import time
import threading

class WifiThreading:
	def __init__(self, host, port):
		# check if inputs are valid
		assert host is not None, "Invalid Host Input"
		assert type(port) is int, "Port is not an integer: %r" % id
		# initialize inputs
		self.host = host
		self.port = port
		self.socket = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
		self.socket.setsockopt(socket.SOL_SOCKET, socket.SO_REUSEADDR, 1)
		self.isConnected = False
		self.conn = None
		self.address = None
		#self.mutex = threading.Lock()

	def connected(self):
		#self.mutex.acquire()
		try:
			return self.isConnected
		except: 
			pass
		#finally:
			#self.mutex.release()

	def connect(self):
		#self.mutex.acquire()
		try:
			if self.isConnected is False:
				try:
					self.socket.bind((self.host, self.port))
					print "Socket Bind complete"
				except socket.error as ErrorMsg:
					print "Bind failed, Error: " + ErrorMsg[1]
					raise socket.error()

				self.socket.listen(10)
				print "Socket Now Listening"
				self.socket.setblocking(1)
				(self.conn, self.address) = self.socket.accept()
				print "Connected with:" + self.address[0] + ":" + str(self.address[1])
				self.isConnected = True
		except: 
			pass
		#finally:
			#self.mutex.release()

	def close(self):
		#self.mutex.acquire()
		try:
			if self.isConnected == False:
				return
			self.conn.close()
			self.socket.close()
			self.isConnected = False
			print "PC Disconnected"
		except: 
			pass
		#finally:
			#self.mutex.release()

	def send(self, data):
		#print "WIFI Acquiring mutex"
		#self.mutex.acquire()
		#print "WIFI Acquired mutex. Yay!"
		try:
			if self.isConnected==False:
				logging.warning('Not connected to any Wifi. Unable to send.')
				return None
			self.conn.setblocking(1)
			data += "\n"
			self.conn.send(data.encode('utf-8'))
		except socket.error as ErrorMsg:
			print ErrorMsg
		#finally:
			#self.mutex.release()

	def receive(self):
		#self.mutex.acquire()
		try:
			if self.isConnected==False:
				print "Not connected to any Wifi. Unable to receive."
				return ""
			self.conn.setblocking(0)
			result = ""
			data = self.conn.recv(4096)
			if(data != ""):
				result += data.decode('utf-8')
				print "Message received from PC: " + result[5:]
			return result
		except Exception as ErrorMsg:
			return ""
		#finally:
			#self.mutex.release()

