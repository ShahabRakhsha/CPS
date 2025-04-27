#include <Wire.h>          
#include <Servo.h>         

#define SOIL_SENSOR_PIN A0
#define WATER_MOTOR_PIN 9
#define SERVO_PIN 10

#define EDGE_NODE_ADDRESS 0x10  

Servo potServo; 

void setup() {
  Wire.begin(EDGE_NODE_ADDRESS);
  Wire.onReceive(receiveCommand);  

  pinMode(SOIL_SENSOR_PIN, INPUT);
  pinMode(WATER_MOTOR_PIN, OUTPUT);

  potServo.attach(SERVO_PIN);

  digitalWrite(WATER_MOTOR_PIN, LOW);  
  potServo.write(90);                  

  Serial.begin(9600);  
}

void loop() {
  int soilMoistureValue = analogRead(SOIL_SENSOR_PIN);

  Serial.print("Soil Moisture: ");
  Serial.println(soilMoistureValue);

  delay(1000);  
}

void receiveCommand(int howMany) {
  while (Wire.available()) {
    char command = Wire.read(); 

    if (command == 'W') {
      digitalWrite(WATER_MOTOR_PIN, HIGH);  
      delay(5000);                           
      digitalWrite(WATER_MOTOR_PIN, LOW);    
    } 
    else if (command == 'R') {
      potServo.write(150);  
      delay(2000);          
      potServo.write(90);   
    }
  }
}
