const int ledPin = 13;
void setup() {
  // put your setup code here, to run once:
  pinMode(ledPin, OUTPUT);
  Serial.begin(115200);

}

void loop() {
  // put your main code here, to run repeatedly:
  if (Serial.available()) {
    light(Serial.read()-'0');
  }
  delay(500);
}

void sendRPiMessage(char *msg){
  Serial.println(msg);
}

void light(int n){
  for (int i = 0; i < n; i++) {
    digitalWrite(ledPin, HIGH);
    delay(100);
    digitalWrite(ledPin, LOW);
    delay(100);
  }
}

