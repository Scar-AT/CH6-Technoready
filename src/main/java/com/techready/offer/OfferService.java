package com.techready.offer;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class OfferService {
    private static final Logger logger = LoggerFactory.getLogger(OfferService.class);

    private List<Offer> offers = new ArrayList<>();

    public void addOffer(Offer offer) {
        offers.add(offer);
    }

    public List<Offer> getAllOffers() { return offers; }

    public List<Offer> filterOffers(Double minPrice, Double maxPrice, String seller, String item) {
        return offers.stream()
                .filter(o -> {
                    double priceValue;
                    try {
                        priceValue = Double.parseDouble(o.getPrice());
                    } catch (NumberFormatException e) {
                        System.out.println("⚠ Invalid price format for offer: " + o.getItem());
                        return false;
                    }

                    boolean matchesMin = (minPrice == null || priceValue >= minPrice);
                    boolean matchesMax = (maxPrice == null || priceValue <= maxPrice);
                    boolean matchesSeller = (seller == null || seller.isBlank() ||
                            (o.getSeller() != null && o.getSeller().equalsIgnoreCase(seller)));
                    boolean matchesItem = (item == null || item.isBlank() ||
                            (o.getItem() != null && o.getItem().toLowerCase().contains(item.toLowerCase())));

                    return matchesMin && matchesMax && matchesSeller && matchesItem;
                })
                .collect(Collectors.toList());
    }




}
