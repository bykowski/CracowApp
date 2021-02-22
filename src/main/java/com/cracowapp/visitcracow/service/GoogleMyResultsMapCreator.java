package com.cracowapp.visitcracow.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.util.UriComponentsBuilder;

@Service
public class GoogleMyResultsMapCreator {

    @Value("${google_key}")
    private String googleKey;

    private static final String GOOGLE_MY_RESULTS_MAP_BASE_URL = "https://maps.googleapis.com/maps/api/staticmap?" +
            "center=Krakow,Sukiennice&zoom=15&size=1300x2500&";

    public String getGoogleMyResultsMapFinalUrl(String markers){
        return UriComponentsBuilder
                .fromUriString(GOOGLE_MY_RESULTS_MAP_BASE_URL)
                .queryParam("markers", markers)
                .queryParam("key", googleKey)
                .encode().toUriString();
    }
}
