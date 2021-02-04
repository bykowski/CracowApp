package com.cracowapp.visitcracow;

import com.cracowapp.visitcracow.model.GoogleMapPlaces;
import com.cracowapp.visitcracow.model.Result;
import org.apache.http.client.utils.URIBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Controller;
import org.springframework.web.client.RestTemplate;

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

    @EventListener(ApplicationReadyEvent.class)
    public List<Result> getGoogleMapPlaces(){
        RestTemplate restTemplate = new RestTemplate();
        GoogleMapPlaces googleMapPlaces = restTemplate.getForObject(getGoogleMapPlacesFinalUrl(), GoogleMapPlaces.class);
        List<Result> resultList = new ArrayList<>();
        for (Result result: googleMapPlaces.getResults()){
            resultList.add(result);
        }
        return resultList;
    }

    private URI getGoogleMapPlacesFinalUrl(){
        URI uri = null;
        try{
            URIBuilder uriBuilder = new URIBuilder(GOOGLE_MAP_PLACES_BASE_URL);
            uriBuilder.addParameter("location",CRACOW_COORDINATES );
            uriBuilder.addParameter("radius", radius);
            uriBuilder.addParameter("name", name);
            uriBuilder.addParameter("key", googleKey);
            uri = uriBuilder.build();
        }catch (URISyntaxException e){
            e.printStackTrace();
        }
        return uri;
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
