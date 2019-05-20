import RPi.GPIO as GPIO
GPIO.setmode(GPIO.BOARD)
GPIO.setup(29 ,GPIO.OUT)#blue

GPIO.setup(13,GPIO.OUT)#red

GPIO.setup(15, GPIO.OUT)#green

GPIO.setup(36 ,GPIO.OUT)#blue button

GPIO.setup(12,GPIO.OUT)#red buttom

GPIO.setup(33, GPIO.OUT)# magenta button

GPIO.setup(7, GPIO.OUT)#green button
GPIO.cleanup()