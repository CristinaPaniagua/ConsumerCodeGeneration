



cd ..
cd TesterSpring

mvn clean install 
PAUSE
cd target
 pscp -C -pw raspberry TesterSpring-4.1.3.1.jar pi@130.240.172.253:
PAUSE
