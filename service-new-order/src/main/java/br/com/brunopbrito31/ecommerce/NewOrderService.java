package br.com.brunopbrito31.ecommerce;
import br.com.brunopbrito31.ecommerce.models.Order;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.concurrent.ExecutionException;

public class NewOrderService {

    public static void main(String[] args)  {
        try( KafkaDispatcher dispatcher = new KafkaDispatcher<Order>("ECOMMERCE_NEW_ORDER")){
            Order newOrder = new Order(
                "cliente teste",
                BigDecimal.valueOf(15),
                "credit-card"
            );
            dispatcher.sendMessage("nova-ordem", newOrder, null);
        } catch (IOException | ExecutionException | InterruptedException e) {
            e.printStackTrace();
        }
    }

}
