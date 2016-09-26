#include "DualVNH5019MotorShield.h"

int M1E1 = 0;
int M1E2 = 1;
int M2E1 = 3;
int M2E2 = 5;

int M1INA = 0;
int M1INB = 3;
int M2INA = 1;
int M2INB = 5;

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
  pinMode(M1INA, INPUT);
  pinMode(M1INB, INPUT);
  pinMode(M2INA, INPUT);
  pinMode(M2INB, INPUT);
  pinMode(M1E1, INPUT);
  pinMode(M1E2, INPUT);
  pinMode(M2E1, INPUT);
  pinMode(M2E2, INPUT);
  
  Serial.begin(9600);
  Serial.println("Dual VNH5019 Motor Shield");
  md.init();
}
void loop()
{
  for (int i = 0; i <= 400; i++)
  {
    md.setM1Speed(-i);
    md.setM2Speed(i);
    Serial.println(digitalRead(M1INA));
    Serial.println();
    Serial.println(digitalRead(M2INA));
    Serial.println();
    delay(2);
  }
  for (int i = 400; i >=0; i--)
  {
    md.setM1Speed(-i);
    md.setM2Speed(i);
    Serial.println(digitalRead(M1INA));
    Serial.println();
    Serial.println(digitalRead(M2INA));
    Serial.println();
    delay(2);
  }
  
//  for (int i = 0; i <= 400; i++)
//  {
//    md.setM1Speed(i);
//    md.setM2Speed(-i);
//    //Serial.println(digitalRead(M1E1));
//    delay(2);
//  }
//  for (int i = 400; i >=0; i--)
//  {
//    md.setM1Speed(i);
//    md.setM2Speed(-i);
//    //Serial.println(digitalRead(M1E1));
//    delay(2);
//  }
// 
//  for (int i = 0; i <= 400; i++)
//  {
//    md.setM1Speed(i);
//    stopIfFault();
//    if (i%200 == 100)
//    {
//      //Serial.print("M1 current: ");
//      //Serial.println(md.getM1CurrentMilliamps());
//    }
//    Serial.println(digitalRead(M1E1));
//    delay(2);
//  }
//  for (int i = 400; i >= -400; i--)
//  {
//    md.setM1Speed(i);
//    stopIfFault();
//    if (i%200 == 100)
//    {
//      //Serial.print("M1 current: ");
//      //Serial.println(md.getM1CurrentMilliamps());
//    }
//    delay(2);
//  }
//  for (int i = -400; i <= 0; i++)
//  {
//    md.setM1Speed(i);
//    stopIfFault();
//    if (i%200 == 100)
//    {
//     // Serial.print("M1 current: ");
//     // Serial.println(md.getM1CurrentMilliamps());
//    }
//    delay(2);
//  }
//  for (int i = 0; i <= 400; i++)
//  {
//    md.setM2Speed(i);
//    stopIfFault();
//    if (i%200 == 100)
//    {
//   // Serial.print("M2 current: ");
//   // Serial.println(md.getM2CurrentMilliamps());
//    }
//    delay(2);
//  }
//  for (int i = 400; i >= -400; i--)
//  {
//    md.setM2Speed(i);
//    stopIfFault();
//    if (i%200 == 100)
//    {
//    //  Serial.print("M2 current: ");
//    //  Serial.println(md.getM2CurrentMilliamps());
//    }
//    delay(2);}
//    for (int i = -400; i <= 0; i++)
//    {
//      md.setM2Speed(i);
//      stopIfFault();
//      if (i%200 == 100)
//      {
//      //  Serial.print("M2 current: ");
//       // Serial.println(md.getM2CurrentMilliamps());
//      }
//      delay(2);
//   }
}

