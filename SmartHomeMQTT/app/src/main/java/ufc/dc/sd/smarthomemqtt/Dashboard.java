package ufc.dc.sd.smarthomemqtt;

import android.os.Build;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.util.Log;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.Toast;

import ufc.dc.sd.smarthomesocket.R;

public class Dashboard extends AppCompatActivity {

    // initiate a Switch
    Switch switchPorta;
    Switch switchLampadas;
    Switch switchArCondicionado;
    Switch switchTV;
    Switch switchSom;

    String topicoPorta = "door";
    String topicoLampada = "lamp";
    String topicoArCondicionado = "ar";
    String topicoTV = "tv";
    String topicoSom = "sound";

    MqttAndroidClient mqttAndroidClient;

    final String serverUri = "tcp://iot.eclipse.org:1883";

    String clientId = "Cliente";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        switchPorta = (Switch) findViewById(R.id.switchPorta);
        switchLampadas = (Switch) findViewById(R.id.switchLampadas);
        switchArCondicionado = (Switch) findViewById(R.id.switchArCondicionado);
        switchTV = (Switch) findViewById(R.id.switchTV);
        switchSom = (Switch) findViewById(R.id.switchSom);


        mqttAndroidClient = new MqttAndroidClient(getApplicationContext(), serverUri, clientId);
        mqttAndroidClient.setCallback(new MqttCallbackExtended() {
            @Override
            public void connectComplete(boolean reconnect, String serverURI) {

                if (reconnect) {
                    addToHistory("Reconnected to : " + serverURI);
                    subscribeToTopic();
                } else {
                    addToHistory("Connected to: " + serverURI);
                }
            }

            @Override
            public void connectionLost(Throwable cause) {
                addToHistory("The Connection was lost.");
            }

            @Override
            public void messageArrived(String topic, MqttMessage message) throws Exception {
                addToHistory("Incoming message: " + new String(message.getPayload()));
            }

            @Override
            public void deliveryComplete(IMqttDeliveryToken token) {

            }
        });

        MqttConnectOptions mqttConnectOptions = new MqttConnectOptions();
        mqttConnectOptions.setAutomaticReconnect(true);
        mqttConnectOptions.setCleanSession(false);

        try {
            mqttAndroidClient.connect(mqttConnectOptions, null, new IMqttActionListener() {
                @Override
                public void onSuccess(IMqttToken asyncActionToken) {
                    DisconnectedBufferOptions disconnectedBufferOptions = new DisconnectedBufferOptions();
                    disconnectedBufferOptions.setBufferEnabled(true);
                    disconnectedBufferOptions.setBufferSize(100);
                    disconnectedBufferOptions.setPersistBuffer(false);
                    disconnectedBufferOptions.setDeleteOldestMessages(false);
                    mqttAndroidClient.setBufferOpts(disconnectedBufferOptions);
                }

                @Override
                public void onFailure(IMqttToken asyncActionToken, Throwable exception) {
                    addToHistory("Failed to connect to: " + serverUri);
                }
            });

        } catch (MqttException ex){
            ex.printStackTrace();
        }


        switchPorta.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                subscribeToTopic(topicoPorta);
                if(isChecked) {
                    Toast.makeText(getApplicationContext(),"Porta aberta!", Toast.LENGTH_LONG).show();
                    publishMessage(topicoPorta, 1);
                    //Add chamada metodo abrir porta
                }
                else {
                    Toast.makeText(getApplicationContext(),"Porta fechada!", Toast.LENGTH_LONG).show();
                    publishMessage(topicoPorta, 0);
                    //Add chamada metodo fechar porta
                }
            }
        });

        switchLampadas.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                subscribeToTopic(topicoLampada);
                if(isChecked) {
                    Toast.makeText(getApplicationContext(),"Lâmpadas ligadas!", Toast.LENGTH_LONG).show();
                    publishMessage(topicoLampada, 1);
                    //Add chamada metodo ligar lâmpadas
                }
                else {
                    Toast.makeText(getApplicationContext(),"Lâmpadas desligadas!", Toast.LENGTH_LONG).show();
                    publishMessage(topicoLampada, 0);
                    //Add chamada metodo desligar lâmpadas
                }
            }
        });

        switchArCondicionado.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                subscribeToTopic(topicoArCondicionado);
                if(isChecked) {
                    Toast.makeText(getApplicationContext(),"Ar condicionado ligado!", Toast.LENGTH_LONG).show();
                    publishMessage(topicoArCondicionado, 1);
                    //Add chamada metodo ligar ar condicionado
                }
                else {
                    Toast.makeText(getApplicationContext(),"Ar condicionado desligado!", Toast.LENGTH_LONG).show();
                    publishMessage(topicoArCondicionado, 0);
                    //Add chamada metodo desligar ar condicionado
                }
            }
        });

        switchTV.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                subscribeToTopic(topicoTV);
                if(isChecked) {
                    Toast.makeText(getApplicationContext(),"TV ligada!", Toast.LENGTH_LONG).show();
                    publishMessage(topicoTV, 1);
                    //Add chamada metodo ligar TV
                }
                else {
                    Toast.makeText(getApplicationContext(),"TV desligada!", Toast.LENGTH_LONG).show();
                    publishMessage(topicoTV, 0);
                    //Add chamada metodo desligar TV
                }
            }
        });

        switchSom.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                subscribeToTopic(topicoSom);
                if(isChecked) {
                    Toast.makeText(getApplicationContext(),"Som ligado!", Toast.LENGTH_LONG).show();
                    publishMessage(topicoSom, 1);
                    //Add chamada metodo ligar som
                }
                else {
                    Toast.makeText(getApplicationContext(),"Som desligado!", Toast.LENGTH_LONG).show();
                    publishMessage(topicoSom, 0);
                    //Add chamada metodo desligar som
                }
            }
        });
    }

    public void subscribeToTopic(String subscriptionTopic){
        try {
            mqttAndroidClient.subscribe(subscriptionTopic, 0, null, new IMqttActionListener() {
                @Override
                public void onSuccess(IMqttToken asyncActionToken) {
                    System.out.println("Inscrito no tópico");
                }

                @Override
                public void onFailure(IMqttToken asyncActionToken, Throwable exception) {
                    System.out.println("Falha na inscrição");
                }
            });

        } catch (MqttException ex){
            System.err.println("Exception whilst subscribing");
            ex.printStackTrace();
        }
    }

    public void publishMessage(String topic, Integer valor){
        try {
            MqttMessage message = new MqttMessage();
            message.setPayload(publishMessage.getBytes());
            mqttAndroidClient.publish(topic, valor, message);
            System.out.println("Mensagem Publicada");
            if(!mqttAndroidClient.isConnected()){
                System.out.println(mqttAndroidClient.getBufferedMessageCount() + " mensagens no buffer.");
            }
        } catch (MqttException e) {
            System.err.println("Erro Publicando: " + e.getMessage());
            e.printStackTrace();
        }
    }
}