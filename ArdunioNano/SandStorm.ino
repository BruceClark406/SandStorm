#include <Servo.h>

Servo right1;
Servo right2;
Servo right3;
Servo left1;
Servo left2;
Servo left3;
int roation_speed = 0;
int idle = 90;


void setup()                    // run once, when the sketch starts
{
 Serial.begin(9600);            // set the baud rate to 9600, same should be of your Serial Monitor
 pinMode(13, OUTPUT);

 //set up servos
 right1.attach(11); 
 right2.attach(5); 
 right3.attach(9); 
 
 left1.attach(10);
 left2.attach(3);
 left3.attach(6);
 
}

void loop()
{
  
  if(Serial.available()){
    int inputInt;
    inputInt=Serial.read();
    Serial.print(inputInt);
    Serial.print(" ");
  
    if(inputInt == 1){   
      forward();
    }
    else if(inputInt == 2){ 
      right();
    }
    else if(inputInt == 3){   
      back();
    }
    else if(inputInt == 4){
      left();
    }
    
    else if(inputInt == 5){   
      halt();
    }

    else{
      //if 6 is first digit, speed it the next digits from 0-100
      //scale 0-100 to 0-90
      roation_speed = (inputInt % 600)*.9
    }
  }
  
}

void left(){
  right1.write(idle + roation_speed);
  right2.write(idle + roation_speed);
  right3.write(idle + roation_speed);
  
  left1.write(idle + roation_speed);
  left2.write(idle + roation_speed);
  left3.write(idle + roation_speed);
}
void right(){
  right1.write(idle - roation_speed);
  right2.write(idle - roation_speed);
  right3.write(idle - roation_speed);
  
  left1.write(idle - roation_speed);
  left2.write(idle - roation_speed);
  left3.write(idle - roation_speed);
}
void forward(){
  right1.write(idle + roation_speed);
  right2.write(idle + roation_speed);
  right3.write(idle + roation_speed);
  
  left1.write(idle - roation_speed);
  left2.write(idle - roation_speed);
  left3.write(idle - roation_speed);
}
void back(){
  right1.write(idle - roation_speed);
  right2.write(idle - roation_speed);
  right3.write(idle - roation_speed);
  
  left1.write(idle + roation_speed);
  left2.write(idle + roation_speed);
  left3.write(idle + roation_speed);
}
void halt(){
  right1.write(idle);
  right2.write(idle);
  right3.write(idle);
  
  left1.write(idle);
  left2.write(idle);
  left3.write(idle);
}
  
