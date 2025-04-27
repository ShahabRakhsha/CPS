#include <Wire.h> 


#define TEMP_SENSOR_PIN A0
#define LIGHT_SENSOR1_PIN A1
#define LIGHT_SENSOR2_PIN A2

#define EDGE_NODE1_ADDRESS 0x10
#define EDGE_NODE2_ADDRESS 0x11

void setup() {
  Serial.begin(9600);
  Wire.begin();  
}

void loop() {
  int tempValue = analogRead(TEMP_SENSOR_PIN); 
  float temperature = (tempValue * 5.0 * 100.0) / 1023.0; 

  int light1 = analogRead(LIGHT_SENSOR1_PIN);
  int light2 = analogRead(LIGHT_SENSOR2_PIN);

  if (abs(light1 - light2) > 100) {  
    if (light1 > light2) {
      sendCommand(EDGE_NODE1_ADDRESS, 'R');
      sendCommand(EDGE_NODE2_ADDRESS, 'R');
    }
  }

  int fakeSoilMoisture = 60; 

  if (fakeSoilMoisture < 50) {
    sendCommand(EDGE_NODE1_ADDRESS, 'W');
    sendCommand(EDGE_NODE2_ADDRESS, 'W');
  }

  delay(3000); 
}

void sendCommand(int address, char command) {
  Wire.beginTransmission(address);
  Wire.write(command);  
  Wire.endTransmission();
}
