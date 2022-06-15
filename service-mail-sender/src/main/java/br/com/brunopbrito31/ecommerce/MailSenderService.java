package br.com.brunopbrito31.ecommerce;

import br.com.brunopbrito31.ecommerce.models.Order;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import java.io.IOException;

public class MailSenderService {

    public static void main(String[] args) {
        CallBackListen<Order> call = (ConsumerRecord<String, Order> record) -> {
            try{
                System.out.println("Entrou no sistema de envio de email");
                System.out.println("Valor do offset ="+record.offset());
                System.out.println("Chave ="+record.key());
                Order orderToAnalyze = record.value();

                StringBuilder builder = new StringBuilder();

                builder.append("Dados do Pedido: Cliente= ")
                        .append(orderToAnalyze.getNameUser())
                        .append(", Valor total= ")
                        .append(orderToAnalyze.getTotal())
                        .append(", Método de Pagamento= ")
                        .append(orderToAnalyze.getPaymentMethod());

                System.out.println(builder);

                Thread.sleep(5000);
                System.out.println("Email enviado");
            }catch (InterruptedException e) {
                e.printStackTrace();
            }
        };

        try(KafkaService consumer = new KafkaService<Order>("ECOMMERCE_NEW_ORDER", MailSenderService.class.getSimpleName(),Order.class.getName())){
            consumer.run(call);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
