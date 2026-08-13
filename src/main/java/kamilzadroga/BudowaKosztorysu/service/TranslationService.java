package kamilzadroga.BudowaKosztorysu.service;

import kamilzadroga.BudowaKosztorysu.dto.TranslateRequest;
import kamilzadroga.BudowaKosztorysu.dto.TranslateResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;


import java.util.List;

@Service
public class TranslationService {

    @Value("${google.translate.api-key}")
    private String apiKey;

    private final RestClient restClient = RestClient.create();

    public List<String> translate(List<String> texts, String targetLang) {
        TranslateRequest request = new TranslateRequest(texts, "pl", targetLang, "text");

        TranslateResponse response = restClient.post()
                .uri("https://translation.googleapis.com/language/translate/v2?key=" + apiKey)
                .contentType(MediaType.APPLICATION_JSON)
                .body(request)
                .retrieve()
                .body(TranslateResponse.class);

        return response.data().translations().stream()
                .map(TranslateResponse.Translation::translatedText)
                .toList();
    }
}
