#include <PinChangeInt.h>
#include "DualVNH5019MotorShield.h"
#define PIN_LEFT_MOTOR 3
#define PIN_RIGHT_MOTOR 5

volatile float pwm_value_left = 0;
volatile float pwm_value_right = 0;
volatile float prev_time_left = 0;
volatile float prev_time_right = 0;

volatile float measureRPMTime= 0;
volatile float initialTime = 0;
volatile float finalTime = 0;

volatile float motor_encoder_right_value;
volatile float motor_encoder_left_value;
  
float encValues_right[5];
float encValues_left[5];

float rpm_right;
float rpm_left;
  
DualVNH5019MotorShield md;
float encValues[5];

void setup() {
  Serial.begin(115200);
  md.init();
  pinMode(PIN_RIGHT_MOTOR, INPUT);
  pinMode(PIN_LEFT_MOTOR, INPUT);
  // attach interrputs to the encoders output pins with PinChangeInt
  PCintPort::attachInterrupt(PIN_LEFT_MOTOR, rising_left, RISING);
  PCintPort::attachInterrupt(PIN_RIGHT_MOTOR, rising_right, RISING);
  //md.setSpeeds(150,145);
}

//PID
const double set_rpm = 75.5;
const double set_left_speed = 250;
const double set_right_speed = 249;

double outputMotorLeft = 0;
double outputMotorRight = 0;

void loop() {
  // initialTime = micros();
  // Serial.print("Initial Time: ");
  // Serial.println(initialTime);
  // Serial.println("PID Loop Started with rpm 75.4 corresponding to speed 300");
  // Serial.print("At time= ");
  // Serial.print((micros()-initialTime)/pow(10,3));
  // Serial.print(", ");
  // Serial.print((60*pow(10,6))/(getMotorLeftEncoderOutput()*562.25*2)); 
  // Serial.print(", ");
  // Serial.println((60*pow(10,6))/(getMotorRightEncoderOutput()*562.25*2));

  for (int i=50; i<250; i++) {
    double current_left_rpm = (60*pow(10,6))/(getMotorLeftEncoderOutput()*562.25*2);
    double set_left_speed = motorLeftPID(i, current_left_rpm);
    md.setM1Speed(set_left_speed);
  }
  motorRightPID();
  // finalTime = micros();
  // Serial.print("Final Time: ");
  // Serial.println(finalTime-initialTime);
}

double motorLeftPID(int current_speed, double rpm) {
  float K1 = 0.03025;
  float K2 = -0.01117;
  float K3 = 0.00103;
  double current_rpm, previous_rpm, oldest_rpm, output;
  double zeroth_error=0, first_error=0, second_error=0;
  current_rpm = (60*pow(10,6))/(getMotorLeftEncoderOutput()*562.25*2);
  zeroth_error = set_rpm - current_rpm;
  outputMotorLeft = K1 * zeroth_error + K2 * first_error + K3 * second_error;
  Serial.print("Left Motor Speed : ");
  Serial.println(current_rpm);
  previous_rpm = current_rpm;
  Serial.print("Left Motor PID Difference : ");
  Serial.println(outputMotorLeft);
  Serial.print("Left Motor PID Controlled Output : ");
  Serial.println(previous_rpm + outputMotorLeft);
  second_error = first_error;
  first_error = zeroth_error;
  return current_speed + outputMotorLeft; 
}

double motorRightPID(double rpm) {
  float K1 = 0.02554;
  float K2 = -0.00925;
  float K3 = 0.00084;
  double current_rpm, previous_rpm, oldest_rpm, output;
  double zeroth_error=0, first_error=0, second_error=0;
  current_rpm = (60*pow(10,6))/(getMotorRightEncoderOutput()*562.25*2);
  zeroth_error = set_rpm - current_rpm;
  outputMotorRight = K1 * zeroth_error + K2 * first_error + K3 * second_error;
  Serial.print("Right Motor Speed : ");
  Serial.println(current_rpm);
  previous_rpm = current_rpm;
  Serial.print("Right Motor PID Difference : ");
  Serial.println(outputMotorRight);
  Serial.print("Right Motor PID Controlled Output : ");
  Serial.println(previous_rpm + outputMotorRight);
  second_error = first_error;
  first_error = zeroth_error; 
}

void rising_left() {
  PCintPort::attachInterrupt(PIN_LEFT_MOTOR, falling_left, FALLING);
  motor_encoder_left_value++;
  prev_time_left = micros();
}

void rising_right() {
  PCintPort::attachInterrupt(PIN_RIGHT_MOTOR, falling_right, FALLING);
  motor_encoder_right_value++;
  prev_time_right = micros();
}

void falling_left() {
  PCintPort::attachInterrupt(PIN_LEFT_MOTOR, rising_left, RISING);
  pwm_value_left = micros()-prev_time_left;
}

void falling_right() {
  PCintPort::attachInterrupt(PIN_RIGHT_MOTOR, rising_right, RISING);
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


