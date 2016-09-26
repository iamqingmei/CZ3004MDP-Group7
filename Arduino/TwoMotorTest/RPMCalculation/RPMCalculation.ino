#include <PinChangeInt.h>
#include "DualVNH5019MotorShield.h"

volatile float pwm_value = 0;
volatile float prev_time = 0;
DualVNH5019MotorShield md;
float encValues[20];
float rpm;

void setup() {
  Serial.begin(9600);
  // when pin D2 goes high, call the rising function
  attachInterrupt(1, rising, RISING);
  md.init();
}

void loop() {
  md.setM2Speed(308);
  int i = 0;
  while (i < 11)
  {
    encValues[i] = pwm_value * 2;
    i++;
  }
  rpm = (60 * pow(10, 6)) / (median() * 562.25);
  Serial.println(rpm, " ");
  /*
    for(int i = 0; i<10; i++)
    {
    Serial.println(encValues[i]);
    }*/
}

void rising() {
  attachInterrupt(1, falling, FALLING);
  prev_time = micros();
}

void falling() {
  attachInterrupt(1, rising, RISING);
  pwm_value = micros() - prev_time;
  //Serial.println(pwm_value);
}

float median() {
  float median = 0;
  float temp = 0;
  int j;
  for (int i = 1; i < 11; i++)
  {
    temp = encValues[i];
    for (j = i - 1; j >= 0 && temp < encValues[j]; j--)
    {
      encValues[j + 1] = encValues[j];
    }
    encValues[j + 1] = temp;
  }
  median = encValues[11 / 2];
  return median;
}
