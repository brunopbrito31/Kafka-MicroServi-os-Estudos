package br.com.brunopbrito31.ecommerce;

import br.com.brunopbrito31.ecommerce.KafkaDispatcher;

import java.io.IOException;
import java.util.concurrent.ExecutionException;

public class NewOrderMain {

    public static void main(String[] args)  {
        try( KafkaDispatcher dispatcher = new KafkaDispatcher<String>("ECOMMERCE_NEW_ORDER")){
            dispatcher.sendMessage("chave","oi teste",null);
        } catch (IOException | ExecutionException | InterruptedException e) {
            e.printStackTrace();
        }
    }

}
