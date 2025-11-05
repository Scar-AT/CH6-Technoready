package com.techready.offer;

public class Offer {
    private String item;
    private String price;
    private String seller;

    public Offer(String item, String price, String seller) {
        this.item = item;
        this.price = price;
        this.seller = seller;
    }

    public String getItem() { return item; }
    public String getPrice() { return price; }
    public String getSeller() { return seller; }
}
