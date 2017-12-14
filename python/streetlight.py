import RPi.GPIO as GPIO 
import smbus
import time 
import threading
import thread
import lcd
import client_socket

GPIO.setmode(GPIO.BCM)
GPIO.setwarnings(False)
#set GPIO Pins 
GPIO_TRIGGER = 0 
GPIO_ECHO = 1
led_pin1 = 14
GPIO.setup(GPIO_TRIGGER, GPIO.OUT) 
GPIO.setup(GPIO_ECHO, GPIO.IN, pull_up_down=GPIO.PUD_UP)
GPIO.setup(led_pin1,GPIO.OUT)

#light sensor configuration
bus = smbus.SMBus(1)
addr = 0x23
reset = 0x07
con_hr_mode = 0x10

#piezo setting
piezo_pin = 13
scale = [261,294,329,392,440,493,523]
#jog stick setting
jog_gpio = 21
data = [0,0]
lux=0
val=0

def distance(): 
        print 'in distance'
	# set Trigger to HIGH 
	GPIO.output(GPIO_TRIGGER, True) 
	# set Trigger after 0.01ms to LOW 
	time.sleep(0.00001) 
	GPIO.output(GPIO_TRIGGER, False) 
	StartTime= time.time() 
	StopTime= time.time()
	#save StartTime 
	while GPIO.input(GPIO_ECHO) == 1:
		StartTime= time.time() 
	# save time of arrival 
	while GPIO.input(GPIO_ECHO) == 0: 
		StopTime= time.time() 
	# time difference between start and arrival 
	TimeElapsed= StopTime-StartTime 
	# multiply with the sonic speed (34300 cm/s) 
	# and divide by 2, because there and back 
        distance = (TimeElapsed* 34300) / 2 
	return distance

def led_on():
    	GPIO.output(led_pin1,True)
def led_off():
    	GPIO.output(led_pin1,False)

class LuxUltraThread(threading.Thread):
    def __init__(self,addr,con_hr_mode):
        bus=smbus.SMBus(1)
        threading.Thread.__init__(self)
        self.addr = addr
        self.con_hr_mode = con_hr_mode
        bus.write_byte(addr,reset)
        time.sleep(0.05)
    def run(self):
        while True:
                bus.write_byte(addr,con_hr_mode)
                time.sleep(0.26)
                data[0]=bus.read_byte(addr)
                data[1]=bus.read_byte(addr)
                val = (data[0]<<8)|data[1]
                lux=val/1.2
                dist = distance()
                print ('measured distance =%.2f'%dist)
                print ('lux=%.2f'%lux)
                if lux < 300 or dist <= 10:
                    led_on()
                else:
                    led_off()
                time.sleep(1)

class LcdThread(threading.Thread):
    def __init__(self):
        threading.Thread.__init__(self)
        lcd.setup()
    def run(self):
        print 'lcd print.............'
        
class AlarmThread(threading.Thread):
    def __init__(self):
        threading.Thread.__init__(self)
        GPIO.setup(jog_gpio,GPIO.IN)
    def run(self):
        stat = 0
        count = -1
        isStart = False
        while True:
            cur_stat = GPIO.input(jog_gpio)
            if cur_stat != stat:
                stat = cur_stat
                print 'center'
            if stat == 1 :
                if isStart == False :
                    count = 1
                elif isStart == True:
                     count = 0
            if count == 1:
                    GPIO.setup(piezo_pin,GPIO.OUT)
                    p=GPIO.PWM(piezo_pin,100)
                    count = 2
                    lcd.start()
            elif count ==2:
                    p.start(50)
                    p.ChangeFrequency(scale[5])
                    time.sleep(0.4)
                    count = 3
            elif count ==3:
                    p.ChangeFrequency(scale[2])
                    time.sleep(0.4)
                    count = 2
                    if isStart == False:
                        isStart = True
                        client_socket.sendHelp()
            elif count == 0:
                    p.stop()
                    GPIO.cleanup(13)
                    time.sleep(1)
                    count = -1
                    isStart = False
                    client_socket.sendOK()
                    lcd.stop()
if __name__ == '__main__':
    try:
	t1 = LuxUltraThread(addr,con_hr_mode)
	t1.daemon = True
        t2 = AlarmThread()
        t2.daemon = True
        t1.start()
        t2.start()

        while True:
           time.sleep(0.0001) 
    except KeyboardInterrupt:
	    print("stop")
	    GPIO.cleanup()
