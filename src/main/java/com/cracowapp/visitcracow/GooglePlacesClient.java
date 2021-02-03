package com.cracowapp.visitcracow;

import com.cracowapp.visitcracow.model.GoogleMapPlaces;
import org.springframework.stereotype.Controller;
import org.springframework.web.client.RestTemplate;

@Controller
public class GooglePlacesClient {

    private static final String GOOGLE_PLACES_URL = "https://maps.googleapis.com/maps/api/place/nearbysearch/json?location=50.0614300,19.9365800&radius=500&types=food&name=cafe&key=AIzaSyDqUGHtgxlFl6XranIPLR8ZMi5x04DGS60";

    public void get(){
        RestTemplate restTemplate = new RestTemplate();

        GoogleMapPlaces googleMapPlaces = restTemplate.getForObject(GOOGLE_PLACES_URL, GoogleMapPlaces.class);

        System.out.println(googleMapPlaces);
    }
}
