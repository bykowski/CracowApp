package com.cracowapp.visitcracow.client;

import com.cracowapp.visitcracow.model.GoogleMapPlaces;
import com.cracowapp.visitcracow.model.Result;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

@Controller
public class GooglePlacesClient {

    private final String CRACOW_COORDINATES = "50.0614300,19.9365800";
    private String radius;
    private String name;

    @Value("${google_key}")
    private String googleKey;

    private static final String GOOGLE_MAP_PLACES_BASE_URL = "https://maps.googleapis.com/maps/api/place/nearbysearch/json?";

    public List<Result> getGoogleMapPlaces() {
        RestTemplate restTemplate = new RestTemplate();
        GoogleMapPlaces googleMapPlaces = restTemplate.getForObject(getGoogleMapPlacesFinalUrl(), GoogleMapPlaces.class);
        List<Result> resultList = new ArrayList<>();
        for (Result result : googleMapPlaces.getResults()) {
            resultList.add(result);
        }
        return resultList;
    }

    private String getGoogleMapPlacesFinalUrl() {
        return UriComponentsBuilder
                .fromUriString(GOOGLE_MAP_PLACES_BASE_URL)
                .queryParam("location", CRACOW_COORDINATES)
                .queryParam("radius", radius)
                .queryParam("name", name)
                .queryParam("key", googleKey)
                .encode().toUriString();
    }

    public String getRadius() {
        return radius;
    }

    public void setRadius(String radius) {
        this.radius = radius;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getGoogleKey() {
        return googleKey;
    }
}
