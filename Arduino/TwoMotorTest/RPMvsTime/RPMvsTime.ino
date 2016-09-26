#include "DualVNH5019MotorShield.h"
volatile float pwm_value = 0;
volatile float prev_time = 0;
volatile float befDec = 0;
volatile float initialTime = 0;
volatile float finalTime = 0;
  
DualVNH5019MotorShield md;
float encValuesfor300[9];
float encValues[5];

float rpm;
struct encoder_data{
  float timeValue[100];
  float rpmValue[100];
  float initialTime;
  float changeTime;
  float finalTime;
};

void setup() {
  Serial.begin(115200);
  // when pin D2 goes high, call the rising function
  attachInterrupt(1, rising, RISING);
   md.init();
}
 
void loop() {
  struct encoder_data encoder_data;
  Serial.println("New Loop Started with speed 300");
  md.setM1Speed(292);
  md.setM2Speed(300);
  delay(500);
  int i = 0;
  //initialTime=micros();
  //Serial.print("Initial Time= ");
  //Serial.println(initialTime);
  encoder_data.initialTime=micros();
  Serial.println(i);
  while((micros()-encoder_data.initialTime)<2400)
  {
    encoder_data.rpmValue[i] = (60*pow(10,6))/(pwm_value*562.25*2);
    encoder_data.timeValue[i] = micros()-encoder_data.initialTime;
    //Serial.print("At Time= ");
    //Serial.print((micros()-initialTime));
    //Serial.print(", ");
    //Serial.println(rpm);
    i++;
  }
  Serial.println(i);
  Serial.println("Speed Changed to 250");
  md.setM1Speed(244);
  md.setM2Speed(250);
  encoder_data.changeTime=micros();
  while((micros()-encoder_data.changeTime)<2400)
  {
    encoder_data.rpmValue[i] = (60*pow(10,6))/(pwm_value*562.25*2);
    encoder_data.timeValue[i] = micros()-encoder_data.initialTime;
    //Serial.print("At Time= ");
    //Serial.print((micros()-initialTime));
    //Serial.print(", ");
    //Serial.println(rpm);
    i++;
  }
  Serial.println(i);
  encoder_data.finalTime=micros();
  for(int j=0;j<i;j++){
    Serial.print(j);
    Serial.print("= ");
    Serial.print(encoder_data.timeValue[j]);
    Serial.print(", ");
    Serial.println(encoder_data.rpmValue[j]);
  }
  Serial.print("Change Time= ");
  Serial.println(encoder_data.changeTime);
  Serial.print("Final Time= ");
  Serial.println(encoder_data.finalTime);
  Serial.print("Final Time-Initial Time= ");
  Serial.println(encoder_data.finalTime-encoder_data.initialTime);
  delay(500);
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

//Sorting the PWM Values
void isort(float *a, int n)
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
  for(int i=0; i < 5 ; i++)
      encValues[i]= pwm_value;
    isort(encValues, 5);
    return encValues[5/2];
}


