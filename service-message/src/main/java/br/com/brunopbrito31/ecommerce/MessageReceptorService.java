package br.com.brunopbrito31.ecommerce;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;

public class MessageReceptorService {

    private static String URL_RECEPTOR_API = "http://localhost:3211/tasks/test2";

    public static void main(String[] args) {
        CallBackListen<Message> call = (ConsumerRecord<String, Message> record) -> {
            try{
                System.out.println("Entrou no ouvinte");
                System.out.println("Valor do offset ="+record.offset());
                System.out.println("Chave ="+record.key());
                System.out.println("Partição = "+record.partition());
                System.out.println("Valor ="+record.value());

                RestTemplate clientHttp = new RestTemplate();
                Message messageToSend  = record.value();
                String result = clientHttp.postForObject(URL_RECEPTOR_API, messageToSend ,String.class);

                Thread.sleep(5000);
//                System.out.println("Order Processed"+result);
                System.out.println("Order Processed ="+result);
            }catch (InterruptedException e) {
                e.printStackTrace();
            } catch (HttpClientErrorException | ResourceAccessException e) {
                System.out.println("Houve um erro no acesso a api externa, tentando novamente");
            }
        };

        try (KafkaService<Message> consumerService = new KafkaService<Message> (
            "ECOMMERCE_NEW_ORDER",
            MessageReceptorService.class.getSimpleName()
            ,Message.class.getName())
        ){
            consumerService.run(call);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
