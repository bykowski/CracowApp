package com.cracowapp.visitcracow.service;

import org.apache.http.client.utils.URIBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.net.URI;
import java.net.URISyntaxException;

@Service
public class GoogleMyResultsMapCreator {

    @Value("${google_key}")
    private String googleKey;

    private static final String GOOGLE_MY_RESULTS_MAP_BASE_URL = "https://maps.googleapis.com/maps/api/staticmap?" +
            "center=Krakow,Sukiennice&zoom=15&size=1300x2500&";

    public URI getGoogleMyResultsMapFinalUrl(String markers){
        URI uri = null;
        try{
            URIBuilder uriBuilder = new URIBuilder(GOOGLE_MY_RESULTS_MAP_BASE_URL);
            uriBuilder.addParameter("markers", markers);
            uriBuilder.addParameter("key", googleKey);
            uri = uriBuilder.build();
        }catch (URISyntaxException e){
            e.printStackTrace();
        }
        return uri;
    }
}
