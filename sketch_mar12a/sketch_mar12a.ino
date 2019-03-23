#include <adk.h>
#define LED LED_BUILTIN

#ifdef dobogusinclude
#include <spi4teensy3.h>
#include <SPI.h>
#endif

USB Usb;
int mc1 = 2; // 
int mc2 = 3;
int en = 9;

// Ususal por ferq = 115200

int COMMAND_SIZE = 5;
char strVal[5];
int bytes_read = 0;

ADK adk(&Usb, "erdmkon", // Manufacturer Name
              "weels", // Model Name
              "May brand robo app", // Description (user-visible string)
              "1.0", // Version
              "Robo app", // URL (web page to visit if no installed apps support the accessory)
              "123456789"); // Serial Number (optional)

uint32_t timer;
bool connected;

void setup() {
  Serial.begin(115200);
  Serial.println("Start");
  pinMode(en, OUTPUT);
  pinMode(mc1, OUTPUT);
  pinMode(mc2, OUTPUT);
  brake();
  if (Usb.Init() == -1) {
    Serial.println("OSC did not start.");
    while (1); // halt
  }
  pinMode(LED, OUTPUT);
}
void brake (){
  digitalWrite(en, LOW);
  digitalWrite(mc1, LOW);
  digitalWrite(mc2, LOW);
  digitalWrite(en, HIGH);
}
void reverse(int rate) {
  digitalWrite(en, LOW);
  digitalWrite(mc1, LOW);
  digitalWrite(mc2, HIGH);
  analogWrite(en, rate);
}
void forward(int rate) {
  digitalWrite(en, LOW);
  digitalWrite(mc1, HIGH);
  digitalWrite(mc2, LOW);
  analogWrite(en, rate);
}
void adkHandle() {
  Usb.Task();
  if (adk.isReady()) {
    if (!connected) {
      connected = true;
      Serial.print("\r\nConnected to accessory");
    }

    uint8_t received[1];
    uint16_t len = sizeof(received);
    uint8_t rcode = adk.RcvData(&len, received);
    if (rcode && rcode != hrNAK) {
      Serial.print("\r\nData rcv: ");
      Serial.print(rcode);
    } else if (len > 0) {
      Serial.print("\r\nData Packet: ");
      Serial.print(received[0]);
      digitalWrite(LED, received[0] ? HIGH : LOW);
      received[0] ? forward(1000) : brake();
    }

    if (millis() - timer >= 1000) { // Send data every 1s
      timer = millis();
      rcode = adk.SndData(sizeof(timer), (uint8_t*)&timer);
      if (rcode && rcode != hrNAK) {
        Serial.print(F("\r\nData send: "));
        Serial.print(rcode, HEX);
      } else if (rcode != hrNAK) {
        Serial.print(F("\r\nTimer: "));
        Serial.print(timer);
      }
    }
  } else {
    if (connected) {
      connected = false;
      Serial.print(F("\r\nDisconnected from accessory"));
      digitalWrite(LED, LOW);
    }
  }
}
void loop() {
  adkHandle();
  if (Serial.available()){
    strVal[bytes_read] = Serial.read();
    bytes_read ++;
  }
  if (bytes_read == COMMAND_SIZE) {
    String info = String(strVal);
    Serial.println(info);
    String command = info.substring(0, 1);
    String rate = info.substring(1, COMMAND_SIZE);
    char rateInfo[4]; 
    rate.toCharArray(rateInfo, COMMAND_SIZE);
    int initRate = atoi(rateInfo);
    if (command == "F") {
      Serial.println(rate);
      forward(initRate);
    } else if (command == "R") {
      reverse(initRate);
    } else if (command == "B") {
      brake();
    }
    bytes_read = 0;
  }
}
