package br.com.brunopbrito31.ecommerce;

import org.apache.kafka.clients.consumer.ConsumerRecord;

import java.io.IOException;

public class LogRegisterService {

    public static void main(String[] args) {
        CallBackListen<String> call = (ConsumerRecord<String, String> record) -> {
            try{
                System.out.println("Registrando log");
                System.out.println("Valor do offset ="+record.offset());
                System.out.println("Chave ="+record.key());
                System.out.println("Partição = "+record.partition());
                System.out.println("Valor ="+record.value());

                Thread.sleep(5000);
                System.out.println("Log registrado com sucesso!");
            }catch (InterruptedException e) {
                e.printStackTrace();
            }
        };

        try (KafkaService<String> consumerService = new KafkaService<String> (
                "ECOMMERCE_NEW_ORDER",
                LogRegisterService.class.getSimpleName()
                ,String.class.getName())
        ){
            consumerService.run(call);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
