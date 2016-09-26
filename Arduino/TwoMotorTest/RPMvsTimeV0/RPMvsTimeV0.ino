#include "DualVNH5019MotorShield.h"
volatile float pwm_value = 0;
volatile float prev_time = 0;
volatile float befDec = 0;
volatile float initialTime = 0;
volatile float finalTime = 0;
  
DualVNH5019MotorShield md;
float encValuesfor300[9];
float encValues[9];

float rpm;
 
void setup() {
  Serial.begin(115200);
  // when pin D2 goes high, call the rising function
  attachInterrupt(1, rising, RISING);
   md.init();
}
 
void loop() {
  Serial.println("New Loop Started with speed 300");
  md.setM1Speed(255);
  md.setM2Speed(250);
  int i = 0;
  initialTime=micros();
  Serial.print("Initial Time= ");
  Serial.println(initialTime/pow(10,3));
  befDec=micros();
  while((micros()-befDec)<500000)
  {
    rpm= (60*pow(10,6))/(getEncoderFeedback()*562.25*2);
    //Serial.print("At Time= ");
    Serial.print((micros()-initialTime)/pow(10,3));
    Serial.print(", ");
    Serial.println(rpm);
  }
  Serial.println("Speed M2 Changed to 250");
  md.setM1Speed(306);
  md.setM2Speed(300);
  i=0;
  befDec=micros();
  while((micros()-befDec)<500000)
  {
    rpm= (60*pow(10,6))/(getEncoderFeedback()*562.25*2);
    //Serial.print("At Time= ");
    Serial.print((micros()-initialTime)/pow(10,3));
    Serial.print(", ");
    Serial.println(rpm);
  }
  delay(500);
  finalTime=micros();
  Serial.print("Final Time= ");
  Serial.println(finalTime/pow(10,3));
}
 
void rising() {
  attachInterrupt(1, falling, FALLING);
  prev_time = micros();
}
 
void falling() {
  attachInterrupt(1, rising, RISING);
  pwm_value = micros()-prev_time;
  //Serial.println(pwm_value);
}

//Sorting the PWM Values using Insertion Sort
void insertionSort(float *a, int n)
{
 for (int i = 1; i < n; ++i)
 {
   float j = a[i];
   int k;
   for (k = i - 1; (k >= 0) && (j < a[k]); k--)
   {
     a[k + 1] = a[k];
   }
   a[k + 1] = j;
 }
}

float getEncoderFeedback() {
  for(int i=0; i < 9 ; i++)
      encValues[i]= pwm_value;
    insertionSort(encValues, 9);
    return encValues[9/2];
}


