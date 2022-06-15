package br.com.brunopbrito31.ecommerce;

import org.apache.kafka.clients.consumer.ConsumerRecord;

public interface CallBackListen<T> {

    void run(ConsumerRecord<String,T> consumer);
}
