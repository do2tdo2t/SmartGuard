import RPi.GPIO as GPIO 
import smbus
import time 
import threading
import thread
import lcd
import client_socket


bus = smbus.SMBus(1)
GPIO.setmode(GPIO.BCM)
GPIO.setwarnings(False)
#set GPIO Pins 
GPIO_TRIGGER = 0 
GPIO_ECHO = 1
led_pin1 = 14
led_pin2 = 15
pir_pin = 24
GPIO.setup(pir_pin,GPIO.IN)
GPIO.setup(GPIO_TRIGGER, GPIO.OUT) 
GPIO.setup(GPIO_ECHO, GPIO.IN, pull_up_down=GPIO.PUD_UP)
GPIO.setup(led_pin1,GPIO.OUT)
GPIO.setup(led_pin2,GPIO.OUT)
#piezo setting
piezo_pin = 13
scale = [261,294,329,392,440,493,523]
#jog stick setting
jog_gpio = 21
distance = 12.0
isClose = False

class PirThread(threading.Thread):
    def run(self):
        global isClose
        while True:
            if (GPIO.input(pir_pin)==True):
                time.sleep(0.1)
                print 'close !'
                global isClose
                isClose == True
            else:
                time.sleep(0.1)
                isClose == False
            time.sleep(0.5)

def led_on():
        GPIO.output(led_pin2,True)
    	GPIO.output(led_pin1,True)
def led_off():
        GPIO.output(led_pin2,False)
    	GPIO.output(led_pin1,False)

class LuxPIRThread(threading.Thread):
    def run(self):
        print 'run lux, ultra thread'
        addr = 0x23
        reset=0x07
        con_hr_mode=0x10
        val=0
        light_val=0
        count = 0
        bus.write_byte(addr,reset)
        time.sleep(0.3)
        while True:
                isClose = GPIO.input(pir_pin)
                if isClose == True:
                    led_on()
                data = bus.read_i2c_block_data(addr,0x20)
                time.sleep(0.2)
                light_val = ((data[1]+(256*data[0]))/1.2)
                time.sleep(0.2)
                print('light_val = %.2f'%light_val)
                if light_val < 150 or isClose == True:
                    led_on()
                else:
                    led_off()

class AlarmThread(threading.Thread):
    def __init__(self):
        threading.Thread.__init__(self)
        GPIO.setup(jog_gpio,GPIO.IN)
    def run(self):
        print 'run alarm thread'
        stat = 0
        count = -1
        isStart = False
        while True:
            cur_stat = GPIO.input(jog_gpio)
            time.sleep(0.00005)
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
                    #lcd.start()
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
                    #lcd.stop()
if __name__ == '__main__':
    try:
	#t1 = LuxPIRThread()
	#t1.daemon = True
        t2 = AlarmThread()
        t2.daemon = True
        #t1.start()
        t2.start()
        while True:
           time.sleep(0.0001) 
    except KeyboardInterrupt:
	    print("stop")
    finally:
	    GPIO.cleanup()
