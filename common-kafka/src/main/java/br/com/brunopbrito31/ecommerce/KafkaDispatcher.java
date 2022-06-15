package br.com.brunopbrito31.ecommerce;

import org.apache.kafka.clients.producer.Callback;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.serialization.StringSerializer;

import java.io.Closeable;
import java.io.IOException;
import java.util.Properties;
import java.util.concurrent.ExecutionException;

public class KafkaDispatcher<T> implements Closeable {

    private static String BOOTSTRAP_SERVERS_CONFIG = "127.0.0.1:9092";
    private String topic = "ECOMMERCE_NEW_ORDER";
    private KafkaProducer producer;
    private Callback callBack = (data, ex) -> {
        if(ex != null){
            ex.printStackTrace();
            return;
        }
        System.out.println(data.topic() + ":::"+ data.partition()+ "/ offset "+data.offset()+"/ timestamp "+data.timestamp());
    };

    public KafkaDispatcher( String topic ){
        this.producer = new KafkaProducer<String, String>(getProperties());
        this.topic = topic;
    }

    public void sendMessage(String key, T message, Callback callbackFunction) throws ExecutionException, InterruptedException {
        if(callbackFunction != null) this.callBack = callbackFunction;
        ProducerRecord record = new ProducerRecord<String, T>( this.topic, key, message );
        producer.send( record, callBack).get();
    }

    private static Properties getProperties() {
        Properties properties = new Properties();
        properties.setProperty(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, BOOTSTRAP_SERVERS_CONFIG);

        properties.setProperty(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        properties.setProperty(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, GsonSerializer.class.getName());
        return properties;
    }

    @Override
    public void close() throws IOException {
        this.producer.close();
    }
}
