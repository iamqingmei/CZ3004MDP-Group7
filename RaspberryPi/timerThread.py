import time
import threading

class timerThread:

	def __init__(self):
		self.timer = False
		self.timeout = 18

		self.start = 0

	def startTimer(self):
		self.start = time.time()
		self.timer = True

	def checkTimer(self):
		if self.timer:
			if((time.time() - self.start) > self.timeout):	
				return self.timer
			else:
				return False
		else:
			return False

	def stopTimer(self):
		self.timer = False
