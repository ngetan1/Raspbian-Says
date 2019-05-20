import random
import RPi.GPIO as GPIO
import time
# Importing the Bluetooth Socket library
import bluetooth


score = 0
clicked = ""
correct = ""
gameOver = False
p = []
client = None


host = ""
port = 1	# Raspberry Pi uses port 1 for Bluetooth Communication

GPIO.setmode(GPIO.BOARD)

GPIO.setup(29,GPIO.OUT)#blue

GPIO.setup(13,GPIO.OUT)#red

GPIO.setup(15,GPIO.OUT)#green

GPIO.setup(36,GPIO.OUT)#blue button

GPIO.setup(12,GPIO.OUT)#red buttom

GPIO.setup(33,GPIO.OUT)# magenta button

GPIO.setup(7,GPIO.OUT)#green button


def reset():
	GPIO.output((36,12,33,13,29,15,7),GPIO.LOW) 
 

def passed():
        GPIO.output(15, GPIO.HIGH)
	print("sleep")
	time.sleep(0.5)
	GPIO.output(15, GPIO.LOW)
	reset()

def fail():
	
	GPIO.output(29, GPIO.HIGH)
	print("sleep")
	time.sleep(0.5)
	#GPIO.output(29, GPIO.LOW)
	reset()

def createSocket():
	global client
	# Creaitng Socket Bluetooth RFCOMM communication
	server = bluetooth.BluetoothSocket(bluetooth.RFCOMM)
	print('Bluetooth Socket Created')
	try:
		server.bind((host, port))
		print("Bluetooth Binding Completed")
	except:
		print("Bluetooth Binding Failed")
	server.listen(2) # One connection at a time
	# Server accepts the clients request and assigns a mac address. 
	client, address = server.accept()
	print("Connected To", address)
	print("Client:", client)
	return client, server


def receive():
	global client
	
	
	
	data = client.recv(1024)
				
	return data


def check(client) :
	
	
	
	global score
	global p
	global clicked
	global correct
	for x in range(len(p)):
		#receiving data
	
		color = receive()
		clicked = color
		show(int(clicked))
		correct=p[x]
		print(color)
		if (str(p[x]) != color):
			fail()
			return True
	score = score+1	

	return False




				
	

def show(argument):
    switcher = {
        1: showRed,
        2: showBlue,
        3: showMagenta,
        4: showGreen,
        
    }
    # Get the function from switcher dictionary
    func = switcher.get(argument)
    # Execute the function
    func()	

def showGreen():
	print ("Green")
 	GPIO.output((36,12,33),GPIO.HIGH) 
	GPIO.output(15,GPIO.HIGH) 
	print("sleep")
	time.sleep(0.5)
 	#GPIO.output((36,15,13,15),GPIO.LOW) 
	reset()
 	

def showMagenta():
	print ("Magenta")
	GPIO.output((36,12,7),GPIO.HIGH) 
	GPIO.output((13,29),GPIO.HIGH) 
	print("sleep")
	time.sleep(0.5)
 	#GPIO.output((36,12,7,13,29),GPIO.LOW)
	reset()
 
def showRed():
	print ("Red")
	GPIO.output((7,36,33),GPIO.HIGH) 
	GPIO.output(13,GPIO.HIGH) 
	print("sleep")
	time.sleep(0.5)
 	#GPIO.output((7,36,33,13),GPIO.LOW)
	reset() 
def showBlue():
	print ("Blue")
	GPIO.output((7,12,33),GPIO.HIGH)  
	GPIO.output(29,GPIO.HIGH) 
	print("sleep")
	time.sleep(0.5)
 	#GPIO.output((7,12,33,29),GPIO.LOW)
	reset()

def startGame(client):
	global gameOver
	global p
	global clicked
	global score
	global correct
	
	gameOver=False
	p = []
	score = 0
	
	while(gameOver == False):
		client.send("allow false")
		
		
		
		p.append(random.randint(1,4))
		print ("My turn:")
		passed()
		for x in range(len(p)):
			
			time.sleep(0.5)
	    		show(p[x])
		client.send("allow true")
		
		
		print ("Your turn")
		gameOver = check(client)
	client.send("score "+(str(score))+" "+clicked+" "+(str(correct)))



while True:	
	
	global client
	client, server = createSocket()
	
	
	
	user = 	receive()
	while ((user=="start") or (user=="again")):
		reset()
		startGame(client)
		
		user = receive()
	client.close()
	server.close()

GPIO.cleanup()





	
