package myRetail.Model;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
         * Author: Manju Bhargavi
         * Date : 05/11/2018
 * */
@Entity
@Table(name = "product")
@JsonIgnoreProperties
public class Product {

    @Id
    public String productId;
    public String prodName;
    public String currentPrice;
    public String currencyCode;

    public Product() {
    }

    public Product(String productId, String prodName, String currentPrice, String currencyCode) {
        this.productId = productId;
        this.prodName = prodName;
        this.currentPrice = currentPrice;
        this.currencyCode = currencyCode;
    }

    public String getCurrencyCode() {
        return currencyCode;
    }

    public void setCurrencyCode(String currencyCode) {
        this.currencyCode = currencyCode;
    }

    public String getCurrentPrice() {
        return currentPrice;
    }

    public void setCurrentPrice(String currentPrice) {
        this.currentPrice = currentPrice;
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public String getProdName() {
        return prodName;
    }

    public void setProdName(String prodName) {
        this.prodName = prodName;
    }

    @Override
    public String toString() {
        return "{\"productId\":\""+productId+"\",\"prodName\":\""+ prodName+"\",\"currentPrice\":\""+currentPrice+"\",\"currencyCode\":\""+currencyCode+"\"}";
    }

}