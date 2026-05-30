package br.com.unicode.normalization;

import static org.springframework.http.HttpMethod.POST;

import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import br.com.unicode.texts.Text;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class NormalizationClient {

    private final RestTemplate restTemplate = new RestTemplate();

    @Value("${normalizer.service.url}")
    private String normalizerUrl;

    public Map<String, Object> normalize(final Text text) {

        var url = normalizerUrl + "/normalization/form";

        var body = Map.of(
                "text_id", text.getId().toString(),
                "input_text", text.getInputText(),
                "normalization_form", text.getNormalizationForm());

        log.atInfo()
                .addKeyValue("event", "http.client.request.out")
                .addKeyValue("text_id", text.getId())
                .addKeyValue("url", url)
                .log();

        ResponseEntity<Map<String, Object>> response = restTemplate.exchange(
                url,
                POST,
                new HttpEntity<>(body),
                new ParameterizedTypeReference<>() {
                });

        log.atInfo()
                .addKeyValue("event", "http.client.response.in")
                .addKeyValue("text_id", text.getId())
                .addKeyValue("url", url)
                .addKeyValue("status_code", response.getStatusCode().value())
                .log();

        var responseBody = response.getBody();

        if (responseBody == null) {

            log.atInfo()
                    .addKeyValue("event", "http.client.response.received.empty")
                    .addKeyValue("text_id", text.getId())
                    .log();

            return Map.of();
        }

        return responseBody;
    }
}