package br.com.brunopbrito31.ecommerce;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.serialization.StringDeserializer;


import java.io.Closeable;
import java.io.IOException;
import java.time.Duration;
import java.util.Collections;
import java.util.Properties;

public class KafkaService<T> implements Closeable {

    private static String BOOTSTRAP_SERVERS_CONFIG = "127.0.0.1:9092";
    private String topic = "ECOMMERCE_NEW_ORDER";
    private KafkaConsumer consumer;
    private String groupIdConfig;
    private String typeConfig;

    public KafkaService(String topic, String groupId, String typeConfig){
        this.typeConfig = typeConfig;
        this.groupIdConfig = groupId;
        this.topic = topic;
        this.consumer = new KafkaConsumer<String, T>(geProperties());
    }

    public void run(CallBackListen<T> callBackListen) {
        this.consumer.subscribe(Collections.singletonList(this.topic));

        while(true){
            ConsumerRecords<String, T> records = consumer.poll(Duration.ofMillis(100));
            if(!records.isEmpty()){
                System.out.println("Encontrei "+records.count()+" registros");
                for( ConsumerRecord<String, T> record: records){
                    System.out.println("----------------------------------------------------");
                    System.out.println("Processing ... Key = "+ ((ConsumerRecord)record).key());
                    try{
                        // Inserir ma função
                        callBackListen.run(record);
                        Thread.sleep(5000);

                    } catch (RuntimeException | InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    private Properties geProperties() {
        Properties properties = new Properties();
        properties.setProperty(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, BOOTSTRAP_SERVERS_CONFIG );
        properties.setProperty(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
        properties.setProperty(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, GsonDeserializer.class.getName());

        // Testar
        properties.setProperty(GsonDeserializer.TYPE_CONFIG , this.typeConfig);

        properties.setProperty(ConsumerConfig.MAX_POLL_RECORDS_CONFIG, "1");
        properties.setProperty(ConsumerConfig.GROUP_ID_CONFIG, this.groupIdConfig);
        return properties;
    }

    @Override
    public void close() throws IOException {
        this.consumer.close();
    }
}
