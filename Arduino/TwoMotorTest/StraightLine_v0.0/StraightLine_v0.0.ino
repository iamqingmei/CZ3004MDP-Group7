#include "DualVNH5019MotorShield.h"
DualVNH5019MotorShield md;

void stopIfFault(){
  if (md.getM1Fault()){
    Serial.println("M1 fault");
    while(1);
   }
  if (md.getM2Fault()){
    Serial.println("M2 fault");
    while(1);
  }
}
void setup()
{
  Serial.begin(9600);
  Serial.println("Dual VNH5019 Motor Shield");
  md.init();
}
void loop()
{
  md.setM1Speed(300);
  md.setM2Speed(306);
}

