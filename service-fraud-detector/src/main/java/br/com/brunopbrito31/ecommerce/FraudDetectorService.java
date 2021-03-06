package br.com.brunopbrito31.ecommerce;

import br.com.brunopbrito31.ecommerce.models.Order;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.math.BigDecimal;

public class FraudDetectorService {

    public static void main(String[] args) {
        CallBackListen<Order> call = (ConsumerRecord<String, Order> record) -> {
            try{
                System.out.println("Entrou no sistema de detecção de fraude");
                System.out.println("Valor do offset ="+record.offset());
                System.out.println("Chave ="+record.key());
                Order orderToAnalyze = record.value();

                // Simulando processamento
                Thread.sleep(8000);
                // Compras acima de 1000
                if( orderToAnalyze.getTotal().compareTo( BigDecimal.valueOf(10000) ) >= 0){
                    System.out.println("Compra com valor elevado, registrando");
                    Thread.sleep(6000);
                }

                StringBuilder builder = new StringBuilder();

                builder.append("Dados do Pedido: Cliente= ")
                        .append(orderToAnalyze.getNameUser())
                        .append(", Valor total= ")
                        .append(orderToAnalyze.getTotal())
                        .append(", Método de Pagamento= ")
                        .append(orderToAnalyze.getPaymentMethod());

                System.out.println(builder);

                Thread.sleep(5000);
                System.out.println("Análise antifraude completa");
            }catch (InterruptedException e) {
                e.printStackTrace();
            }
        };

        try(KafkaService consumer = new KafkaService<Order>("ECOMMERCE_NEW_ORDER", FraudDetectorService.class.getSimpleName(),Order.class.getName())){
            consumer.run(call);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }


}
