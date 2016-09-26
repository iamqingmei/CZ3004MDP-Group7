#include <PinChangeInt.h>
 
#define PIN 3  // the pin we are interested in
volatile byte burp=0;    // a counter to see how many times the pin has changed
byte cmd=0;     // a place to put our serial data
 
void setup() {
  Serial.begin(9600);
  Serial.print("PinChangeInt test on pin ");
  Serial.print(PIN);
  Serial.println();
  
  pinMode(PIN, INPUT);     //set the pin to input
  PCintPort::attachInterrupt(3, burpcount, RISING); // attach a PinChange Interrupt to our pin on the rising edge
  // (RISING, FALLING and CHANGE all work with this library)
  // and execute the function burpcount when that pin changes
}
 
void loop() {
  Serial.print("burpcount:\t");
  Serial.println(burp);
}
 
void burpcount()
{
  burp++;
}
