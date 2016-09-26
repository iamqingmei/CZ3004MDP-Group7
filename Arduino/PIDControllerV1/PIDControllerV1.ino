#include <PinChangeInt.h>
#include "DualVNH5019MotorShield.h"
#define LEFT_MOTOR_PIN 3
#define RIGHT_MOTOR_PIN 5

DualVNH5019MotorShield md;
volatile float pwm_value_left = 0;
volatile float pwm_value_right = 0;
volatile float prev_time_left = 0;
volatile float prev_time_right = 0;

volatile float measureRPMTime= 0;
volatile float initialTime = 0;
volatile float finalTime = 0;

volatile float encoder_left = 0, encoder_right = 0;

float rpm_right;
float rpm_left;
 
void setup() {
  Serial.begin(115200);
  md.init();
  pinMode(LEFT_MOTOR_PIN, INPUT);
  pinMode(RIGHT_MOTOR_PIN, INPUT);
  // attach interrputs to the encoders output pins with PinChangeInt
  PCintPort::attachInterrupt(LEFT_MOTOR_PIN, left_encoder_rising, RISING);
  PCintPort::attachInterrupt(RIGHT_MOTOR_PIN, right_encoder_rising, RISING);
}

void loop() {
  move_forward();
  while(1){
    
  }
}

void move_forward() {
  encoder_left = 0;
  encoder_right = 0;
  while(encoder_right < 1000){
    Serial.print("encoder_left: ");
    Serial.println(encoder_left);
    Serial.print("encoder_right: ");
    Serial.println(encoder_right);
    md.setSpeeds(300, 300);
  }
  md.setBrakes(100, 100);
}
 
void left_encoder_rising() {
  encoder_left++;
}

void right_encoder_rising() {
  encoder_right++;
}
