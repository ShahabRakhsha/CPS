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
  int tempRaw = analogRead(TEMP_SENSOR_PIN);
  float temperature = (tempRaw * 5.0 * 100.0) / 1023.0;  // LM35: 10mV/°C

  int light1 = analogRead(LIGHT_SENSOR1_PIN);
  int light2 = analogRead(LIGHT_SENSOR2_PIN);

  int moisture1 = readMoisture(EDGE_NODE1_ADDRESS);
  int moisture2 = readMoisture(EDGE_NODE2_ADDRESS);

  int percent1 = map(moisture1, 0, 1023, 100, 0);
  int percent2 = map(moisture2, 0, 1023, 100, 0);

  Serial.print("Temp: "); Serial.println(temperature);
  Serial.print("Soil1: "); Serial.print(percent1); Serial.println("%");
  Serial.print("Soil2: "); Serial.print(percent2); Serial.println("%");

  if (abs(light1 - light2) > 100) {
    sendCommand(EDGE_NODE1_ADDRESS, 'R');
    sendCommand(EDGE_NODE2_ADDRESS, 'R');
  }

  checkAndWater(EDGE_NODE1_ADDRESS, percent1, temperature);
  checkAndWater(EDGE_NODE2_ADDRESS, percent2, temperature);

  delay(5000); 
}

int readMoisture(int address) {
  Wire.requestFrom(address, 2);  
  int value = 0;
  if (Wire.available() == 2) {
    int msb = Wire.read();
    int lsb = Wire.read();
    value = (msb << 8) | lsb;
  }
  return value;
}

void sendCommand(int address, char command) {
  Wire.beginTransmission(address);
  Wire.write(command);
  Wire.endTransmission();
}

void checkAndWater(int address, int percent, float temp) {
  if (percent > 80) {
    Serial.print("No watering needed for node "); Serial.println(address);
    return;
  }

  Serial.print("Watering node "); Serial.print(address); Serial.print(" at ");
  if (percent < 50) {
    Serial.println("15 cc/min");
    sendCommand(address, 'W');
  } else {
    if (temp > 25.0) {
      Serial.println("10 cc/min");
      sendCommand(address, 'W');
    } else {
      Serial.println("5 cc/min");
      sendCommand(address, 'W');
    }
  }
}
