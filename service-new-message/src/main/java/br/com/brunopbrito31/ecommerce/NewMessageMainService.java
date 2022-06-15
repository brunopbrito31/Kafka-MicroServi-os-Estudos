package br.com.brunopbrito31.ecommerce;

import br.com.brunopbrito31.ecommerce.KafkaDispatcher;
import br.com.brunopbrito31.ecommerce.Message;

import java.io.IOException;
import java.util.concurrent.ExecutionException;

public class NewMessageMainService {

    public static void main(String[] args) {
        try(KafkaDispatcher<Message> dispatcher = new KafkaDispatcher("ECOMMERCE_NEW_ORDER")){
            Message messageToSend = new Message();
            messageToSend.setNome("Autor - Bruno");
            messageToSend.setIdade("30");

            dispatcher.sendMessage("Chave",messageToSend, null);
        }catch (IOException | ExecutionException | InterruptedException e){
            e.printStackTrace();
        }
    }
}
