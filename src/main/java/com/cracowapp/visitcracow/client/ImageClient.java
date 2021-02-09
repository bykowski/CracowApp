package com.cracowapp.visitcracow.client;

import com.cracowapp.visitcracow.model.OutputImageUrl;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;

@Controller
public class ImageClient {

    private static final String IMAGE_CHANGE_URL = "https://slazzer.com/api/v1/remove_image_background";
    private static final String IMAGE_BACKGROUND = "https://cdn.pixabay.com/photo/2020/04/04/20/25/krakow-5003750_960_720.jpg";


    @Value("${API-KEY}")
    private String apiKey;

    public String getPhotoFromKrakow(String sourceImage) {
        MultiValueMap<String, String> formData = new LinkedMultiValueMap<>();
        formData.add("source_image_url", sourceImage);
        formData.add("bg_image_url", IMAGE_BACKGROUND);

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
        return outputImageUrl.getOutputImageUrl();
    }
}

