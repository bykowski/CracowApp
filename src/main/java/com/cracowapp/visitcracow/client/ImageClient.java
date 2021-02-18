package com.cracowapp.visitcracow.client;

import com.cracowapp.visitcracow.model.OutputImageUrl;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Controller;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;


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
        try{
            RestTemplate restTemplate = new RestTemplate();
            MultiValueMap<String, String> myMap = new LinkedMultiValueMap<>();
            myMap.add("source_image_url", sourceImage);
            myMap.add("bg_image_url", IMAGE_BACKGROUND);
            HttpHeaders httpHeaders = getHttpHeaders();
            HttpEntity<MultiValueMap<String, String>> httpEntity = new HttpEntity(myMap, httpHeaders);
            ResponseEntity<String> response = restTemplate.exchange(IMAGE_CHANGE_URL, HttpMethod.POST,
                    httpEntity, String.class);
            OutputImageUrl outputImageUrl = new ObjectMapper().readValue(response.getBody(), OutputImageUrl.class);
            return  outputImageUrl.getOutputImageUrl();
        }catch (Exception e){
            LOGGER.error(e.getMessage(), e);
            throw new RuntimeException(e.getMessage(), e);
        }
    }

    private HttpHeaders getHttpHeaders() {
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.MULTIPART_FORM_DATA);
        httpHeaders.add("API-KEY", API_KEY);
        httpHeaders.add("user-agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/54.0.2840.99 Safari/537.36");
        return httpHeaders;
    }
}

