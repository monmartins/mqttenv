import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;

        
        
public class ArCondicionadoMQTT {
    public static String topicoPorta = "ramonkathlianafelipe/door";
    public static String topicoLampada = "ramonkathlianafelipe/lamp";
    public static String topicoArCondicionado = "ramonkathlianafelipe/air";
    public static String topicoTV = "ramonkathlianafelipe/tv";
    public static String topicoSom = "ramonkathlianafelipe/sound";
    

	public static void main(String[] args) {
		String broker = "tcp://127.0.0.1:1883";

		MQTTCallBack s = new MQTTCallBack();
	    String content      = "Message from MqttPublishSample";
	    int qos             = 2;
	    String clientId     = "JavaARCondicionado";
	    MemoryPersistence persistence = new MemoryPersistence();

	    try {
	    	
	        MqttClient sampleClient = new MqttClient(broker, clientId, persistence);
	        MqttConnectOptions connOpts = new MqttConnectOptions();
	        connOpts.setCleanSession(true);
	        System.out.println("Connecting to broker: "+broker);
	        sampleClient.connect(connOpts);
	        System.out.println("Connected");
	        System.out.println("Client: "+clientId);

	        sampleClient.setCallback(s);//create here callback 

			MqttConnectOptions mqOptions = new MqttConnectOptions();
			mqOptions.setCleanSession(true);

			sampleClient.subscribe(topicoArCondicionado); 

	    } catch(MqttException me) {
	        System.out.println("reason "+me.getReasonCode());
	        System.out.println("msg "+me.getMessage());
	        System.out.println("loc "+me.getLocalizedMessage());
	        System.out.println("cause "+me.getCause());
	        System.out.println("excep "+me);
	        me.printStackTrace();
	    }
	}
}
