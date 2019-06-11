
#include <PubSubClient.h>
#include <ESP8266WiFi.h>

int LED = 2;
const char* mqttServer = "192.168.1.105";
const int mqttPort = 1883;

WiFiClient espClient;
PubSubClient client(espClient);
void setup() {
  Serial.begin(115200);
  Serial.println();

  WiFi.begin("TP-LINK_C45A14", "27C45A14");

  Serial.print("Connecting");
  while (WiFi.status() != WL_CONNECTED)
  {
    delay(500);
    Serial.print(".");
  }
  Serial.println();
 client.setServer(mqttServer, mqttPort);
  client.setCallback(callback);

  Serial.print("Connected, IP address: ");
  Serial.println(WiFi.localIP());
  pinMode(LED, OUTPUT); 
  


    Serial.println("Connected to server successful!");
    
        digitalWrite(LED, HIGH); 
          while (!client.connected()) {
    Serial.println("Connecting to MQTT...");
 
    if (client.connect("ESP8266Client")) {
 
      Serial.println("connected");  
 
    } else {
 
      Serial.print("failed with state ");
      Serial.print(client.state());
      delay(2000);
 
    }
  }
  client.subscribe("ramonkathlianafelipe/lamp");

}
void callback(char* topic, byte* payload, unsigned int length) {
 
  Serial.print("Message arrived in topic: ");
  Serial.println(topic);
 
  Serial.print("Message:");
  String msg="";
  for (int i = 0; i < length; i++) {
    msg.concat((char)payload[i]);
  }
  if(msg.equals("1")){
    digitalWrite(LED, LOW); 
  }if(msg.equals("0")){
    digitalWrite(LED, HIGH); 
  }
 
  Serial.println();
  Serial.println("-----------------------");
 
}

void loop() {
client.loop();
}
