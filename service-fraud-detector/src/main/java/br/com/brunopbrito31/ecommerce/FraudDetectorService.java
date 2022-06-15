package br.com.brunopbrito31.ecommerce;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;

public class FraudDetectorService {

    private static String URL_RECEPTOR_API = "http://localhost:3211/tasks/test2";

    public static void main(String[] args) {
        RestTemplate clientHttp = new RestTemplate();

        CallBackListen<String> call = (ConsumerRecord<String, String> record) -> {
            try{
                System.out.println("Entrou no sistema de detecção de fraude");
                System.out.println("Valor do offset ="+record.offset());
                System.out.println("Chave ="+record.key());
                System.out.println("Partição ="+record.partition());
                System.out.println("Valor ="+record.value());

                MultiValueMap<String, String> map = new LinkedMultiValueMap<String, String>();
                map.add("Mensagem", String.valueOf( ( (ConsumerRecord)record).key() ) );

                String result = clientHttp.postForObject(URL_RECEPTOR_API, map ,String.class);

                Thread.sleep(5000);
                System.out.println("Order Processed"+result);
            }catch (InterruptedException e) {
                e.printStackTrace();
            } catch (HttpClientErrorException | ResourceAccessException e) {
                System.out.println("Houve um erro no acesso a api externa, tentando novamente");
            }

        };

        try(KafkaService consumer = new KafkaService<String>("ECOMMERCE_NEW_ORDER", FraudDetectorService.class.getSimpleName(),String.class.getName())){
            consumer.run(call);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }


}
