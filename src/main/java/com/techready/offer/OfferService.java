package com.techready.offer;

import java.util.ArrayList;
import java.util.List;

public class OfferService {
    private List<Offer> offers = new ArrayList<>();

    public void addOffer(Offer offer) {
        offers.add(offer);
    }

    public List<Offer> getAllOffers() { return offers; }
}
