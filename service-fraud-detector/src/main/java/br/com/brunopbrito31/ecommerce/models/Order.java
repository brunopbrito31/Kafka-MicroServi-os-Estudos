package br.com.brunopbrito31.ecommerce.models;

import java.math.BigDecimal;

public class Order {

    private String nameUser;

    private BigDecimal total;

    private String paymentMethod;

    public Order(String nameUser, BigDecimal total, String paymentMethod){
        this.nameUser = nameUser;
        this.total = total;
        this.paymentMethod = paymentMethod;
    }

    public void setNameUser(String nameUser) {
        this.nameUser = nameUser;
    }

    public String getNameUser() {
        return this.nameUser;
    }

    public void setTotal(BigDecimal total){
        this.total = total;
    }

    public BigDecimal getTotal() {
        return this.total;
    }

    public void setPaymentMethod(String paymentMethod){
        this.paymentMethod = paymentMethod;
    }

    public String getPaymentMethod() {
        return this.paymentMethod;
    }
}
