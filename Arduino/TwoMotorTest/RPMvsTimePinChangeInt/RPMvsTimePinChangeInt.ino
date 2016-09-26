#include <PinChangeInt.h>
#include "DualVNH5019MotorShield.h"
#define PIN_LEFT_MOTOR 3
#define PIN_RIGHT_MOTOR 5

DualVNH5019MotorShield md;
volatile float pwm_value_left = 0;
volatile float pwm_value_right = 0;
volatile float prev_time_left = 0;
volatile float prev_time_right = 0;

volatile float measureRPMTime= 0;
volatile float initialTime = 0;
volatile float finalTime = 0;

volatile float motor_encoder_right_value;
volatile float motor_encoder_left_value;

float encValues_right[9];
float encValues_left[9];

float rpm_right;
float rpm_left;
 
void setup() {
  Serial.begin(115200);
  md.init();
  pinMode(PIN_RIGHT_MOTOR, INPUT);
  pinMode(PIN_LEFT_MOTOR, INPUT);
  // attach interrputs to the encoders output pins with PinChangeInt
  PCintPort::attachInterrupt(PIN_LEFT_MOTOR, rising_left, RISING);
  PCintPort::attachInterrupt(PIN_RIGHT_MOTOR, rising_right, RISING);
}
 
void loop() {
  Serial.println("New Loop Started with speed 300");
  md.setM1Speed(250);
  md.setM2Speed(245);
  int i = 0;
  initialTime=micros();
  Serial.print("Initial Time= ");
  Serial.println(initialTime/pow(10,3));
  measureRPMTime=micros();
  while((micros()-measureRPMTime)<500000)
  {
    float start = millis();
    rpm_left= (60*pow(10,6))/(getMotorLeftEncoderOutput()*562.25*2);
    rpm_right= (60*pow(10,6))/(getMotorRightEncoderOutput()*562.25*2);
    //Serial.print("At Time= ");
    Serial.print((micros()-initialTime)/pow(10,3));
    Serial.print(", ");
    Serial.print(rpm_left); 
    Serial.print(", ");
    Serial.println(rpm_right);
    // Serial.print("motor_encoder_right_value =");
    // Serial.println(motor_encoder_right_value);
    // Serial.print("motor_encoder_left_value =");
    // Serial.println(motor_encoder_left_value);
   	// Serial.print(prev_time_right);
   	// Serial.println(prev_time_left);
   	// Serial.print("Burp left rise: ");
   	// Serial.println(burp_left_rise);
   	// Serial.print("Burp right rise: ");
   	// Serial.println(burp_right_rise);
   	// Serial.print("Burp left fall: ");
   	// Serial.println(burp_left_fall);
   	// Serial.print("Burp right fall: ");
   	// Serial.println(burp_right_fall);
    while(millis()-start < 5);
  }
  Serial.println("Speed M2 Changed to 300");
  md.setM1Speed(300);
  md.setM2Speed(300);
  i=0;
  measureRPMTime=micros(); // before decrease
  while((micros()-measureRPMTime)<500000)
  {
    float start = millis();
    rpm_left = (60*pow(10,6))/(getMotorLeftEncoderOutput()*562.25*2);
    rpm_right = (60*pow(10,6))/(getMotorRightEncoderOutput()*562.25*2);
    //Serial.print("At Time= ");
    Serial.print((micros()-initialTime)/pow(10,3));
    Serial.print(", ");
    Serial.print(rpm_left); 
    Serial.print(", ");
    Serial.println(rpm_right);
    // Serial.print("motor_encoder_right_value =");
    // Serial.println(motor_encoder_right_value);
    // Serial.print("motor_encoder_left_value =");
    // Serial.println(motor_encoder_left_value);
   	// Serial.print(prev_time_right);
   	// Serial.println(prev_time_left);
   	// Serial.print("Burp left rise: ");
   	// Serial.println(burp_left_rise);
   	// Serial.print("Burp right rise: ");
   	// Serial.println(burp_right_rise);
   	// Serial.print("Burp left fall: ");
   	// Serial.println(burp_left_fall);
   	// Serial.print("Burp right fall: ");
   	// Serial.println(burp_right_fall);
    while(millis()-start < 5);
  }
  finalTime=micros();
  Serial.print("Final Time= ");
  Serial.println(finalTime/pow(10,3));
}
 
void rising_left() {
  PCintPort::attachInterrupt(PIN_LEFT_MOTOR, falling_left, FALLING);
  burp_left_rise++;
  motor_encoder_left_value++;
  prev_time_left = micros();
}

void rising_right() {
  PCintPort::attachInterrupt(PIN_RIGHT_MOTOR, falling_right, FALLING);
  burp_right_rise++;
  motor_encoder_right_value++;
  prev_time_right = micros();
}

void falling_left() {
  PCintPort::attachInterrupt(PIN_LEFT_MOTOR, rising_left, RISING);
  burp_left_fall++;
  pwm_value_left = micros()-prev_time_left;
}

void falling_right() {
  PCintPort::attachInterrupt(PIN_RIGHT_MOTOR, rising_right, RISING);
  burp_right_fall++;
  pwm_value_right = micros()-prev_time_right;
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

float getMotorLeftEncoderOutput() {
  for(int i=0; i < 9 ; i++)
      encValues_right[i]= pwm_value_right;
    isort(encValues_right, 9);
    return encValues_right[9/2];
}

float getMotorRightEncoderOutput() {
  for(int i=0; i < 9 ; i++)
      encValues_left[i]= pwm_value_left;
    isort(encValues_left, 9);
    return encValues_left[9/2];
}


