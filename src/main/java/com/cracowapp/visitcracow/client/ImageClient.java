package com.cracowapp.visitcracow.client;

import com.cracowapp.visitcracow.model.OutputImageUrl;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Controller;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;


@Controller
public class ImageClient {

    public static final Logger LOGGER = LoggerFactory.getLogger(ImageClient.class);

    @Value("${API-KEY}")
    private String API_KEY;

    @Value("${IMAGE_CHANGE_URL}")
    private String IMAGE_CHANGE_URL;

    @Value("${IMAGE_BACKGROUND}")
    private String IMAGE_BACKGROUND;

    public String getPhotoFromKrakow(String sourceImage) {
        MultiValueMap<String, String> formData = new LinkedMultiValueMap<>();
        formData.add("source_image_url", sourceImage);
        formData.add("bg_image_url", IMAGE_BACKGROUND);

        String response = WebClient.create()
                .post()
                .uri(IMAGE_CHANGE_URL)
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .header("API-KEY", API_KEY)
                .body(BodyInserters.fromFormData(formData))
                .retrieve()
                .bodyToMono(String.class)
                .block();

        try{
            OutputImageUrl outputImageUrl = new ObjectMapper().readValue(response, OutputImageUrl.class);
            return  outputImageUrl.getOutputImageUrl();
        }catch (JsonProcessingException e){
            LOGGER.error(e.getMessage(), e);
            throw new RuntimeException(e.getMessage(), e);
        }
    }

}

