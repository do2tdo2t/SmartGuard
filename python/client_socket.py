import socket
import RPi.GPIO as GPIO
import time

HOST = '192.168.0.8'
PORT = 8888

id = 's1'
class SendMessageThread(threading.Thread):
    def __init__(self,msg):
        threading.Thread.__init__(self)
    def run(self):
	    s = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
            s.connect((HOST,PORT))
            s.send(self.msg.encode(encoding='utf_8', errors='strict'))
	    data =s.recv(1024)
	    print('result : ' +data.decode())
	    s.close()
def sendOK():
    try:
	t = SendMessageThread('s1,0')
        t.start()
    except keyboardInterrupt:
        pass
    finally:
        t.join()
		
def sendHelp():
    try:
	t = SendMessageThread('s1,1')
	t.start()
    except keyboardInterrupt:
        pass
    finally:
        t.join()
