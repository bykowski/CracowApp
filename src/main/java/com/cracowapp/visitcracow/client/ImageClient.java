package com.cracowapp.visitcracow.client;

import com.cracowapp.visitcracow.model.OutputImageUrl;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;

@Controller
public class ImageClient {

    private static final String IMAGE_CHANGE_URL = "https://slazzer.com/api/v1/remove_image_background";

    @Value("${API-KEY}")
    private String apiKey;

    @EventListener(ApplicationReadyEvent.class)
    public void get2() {

        MultiValueMap<String, String> formData = new LinkedMultiValueMap<>();
        formData.add("source_image_url", "https://upload.wikimedia.org/wikipedia/commons/thumb/5/56/Donald_Trump_official_portrait.jpg/1200px-Donald_Trump_official_portrait.jpg");
        formData.add("bg_image_url", "https://www.kawiarniany.pl/wp-content/uploads/2020/03/rynek-w-krakowie-ciekawostki.jpg");

        String response = WebClient.create()
                .post()
                .uri(IMAGE_CHANGE_URL)
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .header("API-KEY", apiKey)
                .body(BodyInserters.fromFormData(formData))
                .retrieve()
                .bodyToMono(String.class)
                .block();

        OutputImageUrl outputImageUrl = new OutputImageUrl(response.substring(22, response.length() -2));
        System.out.println(outputImageUrl.getOutputImageUrl());
    }
}

