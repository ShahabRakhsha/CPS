# Intelligent Plant Management System

## System Overview
This project implements an automated plant care solution that monitors environmental conditions and adjusts watering and positioning for optimal plant health. The architecture features a central control unit with distributed monitoring nodes, all communicating via I2C protocol.

## Technical Architecture
### System Components
1. **Central Control Unit**
   - Arduino Uno board
   - Temperature monitoring (LM35)
   - Dual light detection system (LDRs)
   - I2C primary communication controller
2. **Monitoring and Action Units (x2)**
   - Arduino Uno boards
   - Soil hydration sensors (analog)
   - Water distribution mechanisms (DC)
   - Positioning actuators for light optimization
   - I2C secondary communication modules

### Communication Framework
The system utilizes I2C protocol for data exchange between the central control unit and monitoring nodes. Each monitoring unit maintains a unique address identifier (0x10 and 0x11) for proper bus communication.

### Core Functionalities
#### Hydration Monitoring
- Continuous soil moisture assessment with centralized reporting
- Analog-to-percentage conversion for intuitive measurement (0-100%)

#### Advanced Irrigation Control
The system determines watering requirements based on:
1. Current soil moisture levels
2. Ambient temperature conditions

Irrigation decision matrix:
- High moisture (>80%): Irrigation suspended
- Low moisture (<50%): Standard irrigation rate (15 cc/min)
- Moderate moisture (50-80%):
  - High temperature (>25°C): Moderate irrigation rate (10 cc/min)
  - Low temperature (<25°C): Minimal irrigation rate (5 cc/min)

#### Sunlight Exposure Optimization
- Differential light intensity analysis
- Automated positioning adjustment when light differential exceeds threshold (100)
- Dual-position system with 60-degree differential for optimal exposure

## Implementation Framework
### Central Control Unit Software
The primary control module performs:
1. I2C communication initialization as controller
2. Environmental data acquisition (temperature, light)
3. Remote sensor data collection
4. Irrigation decision processing
5. Position optimization commands
6. System status reporting

### Monitoring Unit Software
Each monitoring unit handles:
1. I2C communication as addressable endpoint
2. Local sensor data collection
3. Command execution:
   - Irrigation activation
   - Position adjustment
4. Data transmission to central unit
5. Status monitoring and reporting

## Technical Specifications
### Hardware Requirements
- 3 Arduino Uno microcontrollers
- 2 Soil moisture detection sensors
- 1 Temperature measurement sensor (LM35)
- 2 Light detection sensors (with voltage division)
- 2 Water control actuators
- 2 Position adjustment servomechanisms

### Communication Protocol Implementation
- Centralized control architecture via I2C
- Unique addressing scheme for monitoring units
- Command structure:
  - Irrigation command ('W')
  - Position adjustment command ('R')
  - Data request protocol (2-byte moisture data)

### Physical Configuration
1. Central unit connections:
   - Temperature sensor: A0
   - Light sensors: A1, A2
   - I2C communication lines
2. Monitoring unit connections:
   - Moisture sensor: A0
   - Irrigation control: pin 9
   - Position control: pin 10
   - I2C communication lines

## Verification Procedures
1. Simulate environmental conditions via adjustable inputs
2. Monitor system response through diagnostic interface:
   - Moisture level reporting
   - Temperature data
   - Irrigation decisions
   - Position adjustment commands