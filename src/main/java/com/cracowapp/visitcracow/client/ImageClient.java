package com.cracowapp.visitcracow.client;

import com.cracowapp.visitcracow.model.OutputImageUrl;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.http.*;
import org.springframework.stereotype.Controller;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;


@Controller
public class ImageClient {
    private static final String IMAGE_CHANGE_URL = "https://slazzer.com/api/v1/remove_image_background";

    @Value("${API-KEY}")
    private String apiKey;

    @EventListener(ApplicationReadyEvent.class)
    public void get(){
        RestTemplate restTemplate = new RestTemplate();

        MultiValueMap<String,String> myMap = new LinkedMultiValueMap<>();
        myMap.add("source_image_url", "https://upload.wikimedia.org/wikipedia/commons/thumb/5/56/Donald_Trump_official_portrait.jpg/1200px-Donald_Trump_official_portrait.jpg");
        myMap.add("bg_image_url", "https://www.kawiarniany.pl/wp-content/uploads/2020/03/rynek-w-krakowie-ciekawostki.jpg");

        HttpHeaders httpHeaders = getHttpHeaders();

        HttpEntity<MultiValueMap<String, String>> httpEntity = new HttpEntity(myMap, httpHeaders);

        ResponseEntity<OutputImageUrl> outputImageUrl = restTemplate.exchange(IMAGE_CHANGE_URL, HttpMethod.POST,
                httpEntity, OutputImageUrl.class);

        System.out.println(outputImageUrl.getBody().getOutputImageUrl());

    }

    private HttpHeaders getHttpHeaders() {
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.MULTIPART_FORM_DATA);
        httpHeaders.add("API-KEY", apiKey);
        return httpHeaders;
    }
}
