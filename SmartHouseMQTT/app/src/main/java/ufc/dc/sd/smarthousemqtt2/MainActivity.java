package ufc.dc.sd.smarthousemqtt2;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.Toast;

import org.eclipse.paho.android.service.MqttAndroidClient;
import org.eclipse.paho.client.mqttv3.DisconnectedBufferOptions;
import org.eclipse.paho.client.mqttv3.IMqttActionListener;
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.IMqttToken;
import org.eclipse.paho.client.mqttv3.MqttCallbackExtended;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;

import java.io.UnsupportedEncodingException;

public class MainActivity extends AppCompatActivity {
    // initiate a Switch
    Switch switchPorta;
    Switch switchLampadas;
    Switch switchArCondicionado;
    Switch switchTV;
    Switch switchSom;
    Context context=this;
    String TAG = "log";


    public static String topicoPorta = "ramonkathlianafelipe/door";
    public static String topicoLampada = "ramonkathlianafelipe/lamp";
    public static String topicoArCondicionado = "ramonkathlianafelipe/air";
    public static String topicoTV = "ramonkathlianafelipe/tv";
    public static String topicoSom = "ramonkathlianafelipe/sound";

    final String serverUri = "tcp://192.168.1.105:1883";

    String clientId = "ClienteAndroid";

    MqttAndroidClient mqttAndroidClient;
    private MqttConnectOptions getMqttConnectionOption() {
        MqttConnectOptions mqttConnectOptions = new MqttConnectOptions();
        mqttConnectOptions.setCleanSession(false);
        mqttConnectOptions.setAutomaticReconnect(true);
//        mqttConnectOptions.setWill(Constants.PUBLISH_TOPIC, "I am going offline".getBytes(), 1, true);
        //mqttConnectOptions.setUserName("username");
        //mqttConnectOptions.setPassword("password".toCharArray());
        return mqttConnectOptions;
    }
    private DisconnectedBufferOptions getDisconnectedBufferOptions() {
        DisconnectedBufferOptions disconnectedBufferOptions = new DisconnectedBufferOptions();
        disconnectedBufferOptions.setBufferEnabled(true);
        disconnectedBufferOptions.setBufferSize(100);
        disconnectedBufferOptions.setPersistBuffer(true);
        disconnectedBufferOptions.setDeleteOldestMessages(false);
        return disconnectedBufferOptions;
    }
    public MqttAndroidClient getMqttClient(Context context, String brokerUrl, String clientId) {
        mqttAndroidClient = new MqttAndroidClient(context, brokerUrl, clientId);
        try {
            IMqttToken token = mqttAndroidClient.connect(getMqttConnectionOption());
            token.setActionCallback(new IMqttActionListener() {
                @Override
                public void onSuccess(IMqttToken asyncActionToken) {
                    mqttAndroidClient.setBufferOpts(getDisconnectedBufferOptions());
                    Log.d(TAG, "Success ");
                }
                @Override
                public void onFailure(IMqttToken asyncActionToken, Throwable exception) {
                    Log.d(TAG, "Failure " + exception.toString());
                }
            });
        } catch (MqttException e) {
            e.printStackTrace();
        }
        return mqttAndroidClient;
    }

    public void publishMessage(@NonNull MqttAndroidClient client,
                               @NonNull String msg, int qos, @NonNull String topic)
            throws MqttException, UnsupportedEncodingException {
        byte[] encodedPayload = new byte[0];
        encodedPayload = msg.getBytes("UTF-8");
        MqttMessage message = new MqttMessage(encodedPayload);
        message.setId(5866);
        message.setRetained(true);
        message.setQos(qos);
        client.publish(topic, message);
    }

    public void subscribe(@NonNull MqttAndroidClient client,
                          @NonNull final String topic, int qos) throws MqttException {
        IMqttToken token = client.subscribe(topic, qos);
        token.setActionCallback(new IMqttActionListener() {
            @Override
            public void onSuccess(IMqttToken iMqttToken) {
                Log.d(TAG, "Subscribe Successfully " + topic);
            }
            @Override
            public void onFailure(IMqttToken iMqttToken, Throwable throwable) {
                Log.e(TAG, "Subscribe Failed " + topic);
            }
        });
    }
    public void unSubscribe(@NonNull MqttAndroidClient client,
                            @NonNull final String topic) throws MqttException {
        IMqttToken token = client.unsubscribe(topic);
        token.setActionCallback(new IMqttActionListener() {
            @Override
            public void onSuccess(IMqttToken iMqttToken) {
                Log.d(TAG, "UnSubscribe Successfully " + topic);
            }
            @Override
            public void onFailure(IMqttToken iMqttToken, Throwable throwable) {
                Log.e(TAG, "UnSubscribe Failed " + topic);
            }
        });
    }
    public void disconnect(@NonNull MqttAndroidClient client)
            throws MqttException {
        IMqttToken mqttToken = client.disconnect();
        mqttToken.setActionCallback(new IMqttActionListener() {
            @Override
            public void onSuccess(IMqttToken iMqttToken) {
                Log.d(TAG, "Successfully disconnected");
            }
            @Override
            public void onFailure(IMqttToken iMqttToken, Throwable throwable) {
                Log.d(TAG, "Failed to disconnected " + throwable.toString());
            }
        });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
//
//        try {
//            mqttAndroidClient.connect();
//        } catch (MqttException e) {
//            e.printStackTrace();
//        }
        switchPorta = (Switch) findViewById(R.id.switchPorta);
        switchLampadas = (Switch) findViewById(R.id.switchLampadas);
        switchArCondicionado = (Switch) findViewById(R.id.switchArCondicionado);
        switchTV = (Switch) findViewById(R.id.switchTV);
        switchSom = (Switch) findViewById(R.id.switchSom);
        mqttAndroidClient = getMqttClient(context, serverUri, clientId);
//        publishMessage(topicoLampada,1);
//        publishMessage(topicoArCondicionado,1);
//        publishMessage(topicoTV,1);
//        publishMessage(topicoSom,1);

        mqttAndroidClient.setCallback(new MqttCallbackExtended() {
            @Override
            public void connectComplete(boolean reconnect, String serverURI) {

                try {
                    subscribe(mqttAndroidClient,topicoPorta,2);
                    subscribe(mqttAndroidClient, topicoLampada,2);
                    subscribe(mqttAndroidClient, topicoArCondicionado,2);
                    subscribe(mqttAndroidClient, topicoTV,2);
                    subscribe(mqttAndroidClient, topicoSom,2);

                } catch (MqttException e) {
                    e.printStackTrace();
                }
                if (reconnect) {
                    System.out.println("Reconnected to : " + serverURI);
                } else {
                    System.out.println("Connected to: " + serverURI);
                }
            }

            @Override
            public void connectionLost(Throwable cause) {
                System.out.println("The Connection was lost.");
            }

            @Override
            public void messageArrived(String topic, MqttMessage message) throws Exception {
                System.out.println("Incoming message: " + new String(message.getPayload()));
            }

            @Override
            public void deliveryComplete(IMqttDeliveryToken token) {
                System.out.println("deliveryComplete.");

            }
        });


        switchPorta.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
//                subscribeToTopic(topicoPorta);
                if (isChecked) {
                    Toast.makeText(getApplicationContext(), "Porta aberta!", Toast.LENGTH_LONG).show();
                    try {
                        publishMessage(mqttAndroidClient,new String("1"),1,topicoPorta);
                    } catch (MqttException e) {
                        e.printStackTrace();
                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                    }
                    //Add chamada metodo abrir porta
                } else {
                    Toast.makeText(getApplicationContext(), "Porta fechada!", Toast.LENGTH_LONG).show();
                    try {
                        publishMessage(mqttAndroidClient,new String("0"),1,topicoPorta);
                    } catch (MqttException e) {
                        e.printStackTrace();
                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                    }
                    //Add chamada metodo fechar porta
                }
            }
        });
//
        switchLampadas.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
//                subscribeToTopic(topicoLampada);
                if (isChecked) {
                    Toast.makeText(getApplicationContext(), "Lâmpadas ligadas!", Toast.LENGTH_LONG).show();
                    try {
                        publishMessage(mqttAndroidClient,new String("1"),1,topicoLampada);
                    } catch (MqttException e) {
                        e.printStackTrace();
                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                    }
                    //Add chamada metodo ligar lâmpadas
                } else {
                    Toast.makeText(getApplicationContext(), "Lâmpadas desligadas!", Toast.LENGTH_LONG).show();
                    try {
                        publishMessage(mqttAndroidClient,new String("0"),1,topicoLampada);
                    } catch (MqttException e) {
                        e.printStackTrace();
                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                    }
                    //Add chamada metodo desligar lâmpadas
                }
            }
        });

        switchArCondicionado.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
//                subscribeToTopic(topicoArCondicionado);
                if (isChecked) {
                    Toast.makeText(getApplicationContext(), "Ar condicionado ligado!", Toast.LENGTH_LONG).show();
                    try {
                        publishMessage(mqttAndroidClient,new String("1"),1,topicoArCondicionado);
                    } catch (MqttException e) {
                        e.printStackTrace();
                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                    }
                    //Add chamada metodo ligar ar condicionado
                } else {
                    Toast.makeText(getApplicationContext(), "Ar condicionado desligado!", Toast.LENGTH_LONG).show();
                    try {
                        publishMessage(mqttAndroidClient,new String("0"),1,topicoArCondicionado);
                    } catch (MqttException e) {
                        e.printStackTrace();
                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                    }
                    //Add chamada metodo desligar ar condicionado
                }
            }
        });

        switchTV.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
//                subscribeToTopic(topicoTV);
                if (isChecked) {
                    Toast.makeText(getApplicationContext(), "TV ligada!", Toast.LENGTH_LONG).show();
                    try {
                        publishMessage(mqttAndroidClient,new String("1"),1,topicoTV);
                    } catch (MqttException e) {
                        e.printStackTrace();
                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                    }
                    //Add chamada metodo ligar TV
                } else {
                    Toast.makeText(getApplicationContext(), "TV desligada!", Toast.LENGTH_LONG).show();
                    try {
                        publishMessage(mqttAndroidClient,new String("0"),1,topicoTV);
                    } catch (MqttException e) {
                        e.printStackTrace();
                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                    }
                    //Add chamada metodo desligar TV
                }
            }
        });

        switchSom.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
//                subscribeToTopic(topicoSom);
                if (isChecked) {
                    Toast.makeText(getApplicationContext(), "Som ligado!", Toast.LENGTH_LONG).show();
                    try {
                        publishMessage(mqttAndroidClient,new String("1"),1,topicoSom);
                    } catch (MqttException e) {
                        e.printStackTrace();
                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                    }
                    //Add chamada metodo ligar som
                } else {
                    Toast.makeText(getApplicationContext(), "Som desligado!", Toast.LENGTH_LONG).show();
                    try {
                        publishMessage(mqttAndroidClient,new String("0"),1,topicoSom);
                    } catch (MqttException e) {
                        e.printStackTrace();
                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                    }
                    //Add chamada metodo desligar som
                }
            }
        });
//    }


//    public void subscribeToTopic(MqttAndroidClient mqttAndroidClient, String subscriptionTopic) {
//        try {
//
//            mqttAndroidClient.subscribe(subscriptionTopic, 2, context, new IMqttActionListener() {
//                @Override
//                public void onSuccess(IMqttToken asyncActionToken) {
//                    System.out.println("Inscrito no tópico");
//                }
//
//                @Override
//                public void onFailure(IMqttToken asyncActionToken, Throwable exception) {
//                    System.out.println("Falha na inscrição");
//                }
//            });
//
//        } catch (MqttException ex) {
//            System.err.println("Exception whilst subscribing");
//            ex.printStackTrace();
//        }
//    }
//
//    public void publishMessage(String topic, Integer valor) {
//        try {
//            MqttMessage message = new MqttMessage();
//            message.setPayload(String.valueOf(valor).getBytes());
//            if(mqttAndroidClient!=null){
//
//                mqttAndroidClient.publish(topic, message);
//                System.out.println("Mensagem Publicada");
//                if (!mqttAndroidClient.isConnected()) {
//                    System.out.println(mqttAndroidClient.getBufferedMessageCount() + " mensagens no buffer.");
//                }
//            }
//        } catch (MqttException e) {
//            System.err.println("Erro Publicando: " + e.getMessage());
//            e.printStackTrace();
//        }
//    }
    }
}
