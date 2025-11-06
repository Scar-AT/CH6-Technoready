package com.techready.offer;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class OfferService {
    private List<Offer> offers = new ArrayList<>();

    public void addOffer(Offer offer) {
        offers.add(offer);
    }

    public List<Offer> getAllOffers() { return offers; }

    public List<Offer> filterOffers(double minPrice, double maxPrice, String seller) {
        return offers.stream()
                .filter(o -> (minPrice == null || Double.parseDouble(o.getPrice()) >= minPrice))
                .filter(o -> (maxPrice == null || Double.parseDouble(o.getPrice()) <= maxPrice))
                .filter(o -> (seller == null || Double.parseDouble(o.getSeller().equalsIgnoreCase(seller) ) ) )
                .collect(Collectors.toList());
    }

}
