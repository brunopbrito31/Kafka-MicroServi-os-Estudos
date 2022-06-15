package br.com.brunopbrito31.ecommerce;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.apache.kafka.common.serialization.Deserializer;

import java.util.Map;

public class GsonDeserializer<T> implements Deserializer<T> {

    public static final String TYPE_CONFIG = "br.com.brunopbrito31.ecommerce.type_config";
    private final Gson gson = new GsonBuilder().create();
    private Class<T> type;

    public void configure(Map<String, ?> configs, boolean isKey) {
        String typeName = String.valueOf(configs.get(TYPE_CONFIG));
        try{
            type = (Class<T>)Class.forName(typeName);
        }catch(ClassNotFoundException e ){
            throw new RuntimeException("Houve um erro ao tentar configurar a classe");
        }
    }

    @Override
    public T deserialize(String s, byte[] bytes) {
        return gson.fromJson(new String(bytes), type);
    }
}
