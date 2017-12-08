import socket
import sys
import RPi.GPIO as GPIO
import threading
import time
import pymysql 

HOST = '192.168.1.10'
PORT = 8888

GPIO.setwarnings(False)
GPIO.setmode(GPIO.BCM)
piezo_pin = 13
jog_gpio = 21
scale = 261
isAlarm = False

class Alarm(threading.Thread):
    def run(self):
        print 'run'
        isStart = False
        GPIO.setup(jog_gpio,GPIO.IN)
        stat = 0
        cur_stat = 0
        while True:
            time.sleep(0.5)
            global isAlarm
            if (isAlarm ==True) and (isStart == False):
                #set piezo and start
                GPIO.setup(piezo_pin,GPIO.OUT)
                p=GPIO.PWM(piezo_pin,100)
                p.ChangeFrequency(scale)
                p.start(50)
                isStart = True
                print 'start'
            #check jog
            while isStart == True:
                cur_stat = GPIO.input(jog_gpio)
                if cur_stat != jog_gpio:
                    stat = cur_stat
                if(stat==1):
                    isStart = False
                    isAlarm = False
                    print 'stop'
                    p.stop()
                    GPIO.cleanup(piezo_pin)

def updateDB(id):
    con = pymysql.connect(host='127.0.0.1',user='root',passwd='qweop01',port=3306,db='term') 
    curs = con.cursor()
    select_sql = "select * from streetlight where id = %s"
    update_sql = "update streetlight set code = code+1, alram=1 where id = %s"
    curs.execute(select_sql,(id,))
    rows = curs.fetchall()
    if rows[0]!=None:
        print rows[0]
        curs.execute(update_sql,(id,))
        con.commit()
    con.close()
    print 'update success'

def backDB(id):
    con = pymysql.connect(host='127.0.0.1',user='root',passwd='qweop01',port=3306,db='term') 
    curs = con.cursor()
    update_sql = "update streetlight set alram=0 where id = %s"
    curs.execute(update_sql,(id,))
    con.commit()
    con.close()
    print 'alarm back success'

if __name__=="__main__":

    #1.open socket
    global isAlarm
    isAlarm = False
    s = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
    print('socket created')

    #2.bind to a address and port
    try:
        s.bind((HOST, PORT))
    except socket.error as msg:
        print('Bind Failed. Error code :'+str(msg[0]) + ' Message: ' +msg[1])
        sys.exit()
    print('Socket bind complete')
    #3. Listen for incoming connections
    s.listen(10)
    print('Socket now listening')
    thread = Alarm()
    thread.start()
    
    #keep talking with the client
    while 1:
        #4. Accept connect+ion
        conn, addr = s.accept()
        print('Connected with ' +addr[0] + ':' + str(addr[1]))
        #5. read/send
        data = conn.recv(1024)
        if not data:
            print 'data does not exist'
            break
        else:
            conn.sendall("OK")
            print(data.decode())
            l=['','']
            l = data.split(',')
	    print l[0]
	    print l[1]
	    if (l[1] == '0'):
		updateDB(l[0])
		global isAlarm
            	isAlarm = True
	    elif(l[1] == '1'):
		isAlarm = False				
		backDB(l[0])
    thread.join()
    conn.close()
    s.close()




