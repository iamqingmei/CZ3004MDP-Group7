from bluetooth import *
import threading
import time

class BluetoothThreading:
	def __init__(self):
		self.UUID = "00001101-0000-1000-8000-00805F9B34FB"
		self.client_sock = BluetoothSocket ( RFCOMM )
		#self.btAdd = "08:60:6E:A4:E4:D4"
		self.btAdd = "C8:F2:30:C7:1C:02"
		#self.mutex = threading.Lock()
		self.isConnected = False

	def connect(self):
		#self.mutex.acquire()
		while(1):
			try:
				if self.isConnected == True:
					return
				try:
					service_match = find_service(uuid=self.UUID, address=self.btAdd)
					first_match = service_match[0]
					host = first_match["host"]
					port = first_match["port"]
					self.client_sock.setblocking(1)
					self.client_sock.connect((host,port))
					print "Bluetooth device connected"
					self.isConnected = True
				except BluetoothError as ErrorMsg:
					pass
			except:
				pass
			#finally:
				#self.mutex.release()

	def close(self):
		#self.mutex.acquire()
		try:
			self.client_sock.setblocking(1)
			self.client_sock.close()
			self.isConnected = False
			print "Bluetooth disconnected"
		except:
			pass
		#finally:
			#self.mutex.release()

	def send(self, data):
		#self.mutex.acquire()
		try:
			if self.isConnected == False:
				print "Bluetooth not connected. No data sent."
				return None
			self.client_sock.setblocking(1)
			self.client_sock.send(data.encode('utf-8'))
		except:
			pass
		#finally:
			#self.mutex.release()

	def receive(self):
		#self.mutex.acquire()
		try:
			if self.isConnected == False:
				print "Bluetooth not connected. No data received."
				return ""
			self.client_sock.setblocking(0)
			result = ""
			data = self.client_sock.recv(4096)
			if(data != ""):
				result += data.decode('utf-8')
				print "Message received from Bluetooth: " + result[5:]
			return result
		except IOError:
			return ""
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
