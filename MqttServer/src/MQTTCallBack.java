

import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttMessage;

public class MQTTCallBack implements MqttCallback {

    String topicoPorta = "ramonkathlianafelipe/door";
    String topicoLampada = "ramonkathlianafelipe/lamp";
    String topicoArCondicionado = "ramonkathlianafelipe/air";
    String topicoTV = "ramonkathlianafelipe/tv";
    String topicoSom = "ramonkathlianafelipe/sound";
	  MqttClient myClient;
		MqttConnectOptions connOpt;
		@Override
		public void connectionLost(Throwable cause) {
			// TODO Auto-generated method stub
			
		}
		@Override
		public void messageArrived(String topic, MqttMessage message) throws Exception {
			// TODO Auto-generated method stub
			String receive = new String(message.getPayload());
			if(topic.equals(topicoLampada)) {
				if(receive.equals("1")) {
					System.out.println("Lampada Ligada");
				}
				if(receive.equals("0")) {
					System.out.println("Lampada Apagada");
				}
			}

			if(topic.equals(topicoArCondicionado)) {
				if(receive.equals("1")) {
					System.out.println("Ar Condicionado Ligado");
				}
				if(receive.equals("0")) {
					System.out.println("Ar Condicionado Desligado");
				}
			}

			if(topic.equals(topicoPorta)) {
				if(receive.equals("1")) {
					System.out.println("Porta Aberta");
				}
				if(receive.equals("0")) {
					System.out.println("Porta Fechada");
				}
			}

			if(topic.equals(topicoSom)) {
				if(receive.equals("1")) {
					System.out.println("Som Ligado");
				}
				if(receive.equals("0")) {
					System.out.println("Som Desligado");
				}
			}

			if(topic.equals(topicoTV)) {
				if(receive.equals("1")) {
					System.out.println("TV Ligada");
				}
				if(receive.equals("0")) {
					System.out.println("TV Desligada");
				}
			}
			
		}
		@Override
		public void deliveryComplete(IMqttDeliveryToken token) {
			// TODO Auto-generated method stub
			
		}

}
