#include <Wire.h>          
#include <Servo.h>         

#define SOIL_SENSOR_PIN A0
#define WATER_MOTOR_PIN 9
#define SERVO_PIN 10

#define EDGE_NODE_ADDRESS 0x11  

Servo potServo;

int currentSoilMoisture = 0;  

void setup() {
  Wire.begin(EDGE_NODE_ADDRESS);
  Wire.onReceive(receiveCommand);   
  Wire.onRequest(sendSoilMoisture); 

  pinMode(SOIL_SENSOR_PIN, INPUT);
  pinMode(WATER_MOTOR_PIN, OUTPUT);

  potServo.attach(SERVO_PIN);
  potServo.write(90);                  

  digitalWrite(WATER_MOTOR_PIN, LOW);  

  Serial.begin(9600);  
}

void loop() {
  currentSoilMoisture = analogRead(SOIL_SENSOR_PIN);

  Serial.print("Soil Moisture: ");
  Serial.println(currentSoilMoisture);

  delay(1000);
}

void receiveCommand(int howMany) {
  while (Wire.available()) {
    char command = Wire.read(); 

    if (command == 'W') {
      Serial.println("Watering: HIGH");
      digitalWrite(WATER_MOTOR_PIN, HIGH);  
      delay(5000);                           
      digitalWrite(WATER_MOTOR_PIN, LOW);    
    } 
    else if (command == 'R') {
      Serial.println("Rotating pot...");
      potServo.write(150);  
      delay(2000);          
      potServo.write(90);   
    }
  }
}

void sendSoilMoisture() {
  Wire.write((currentSoilMoisture >> 8) & 0xFF);  
  Wire.write(currentSoilMoisture & 0xFF);         
}
