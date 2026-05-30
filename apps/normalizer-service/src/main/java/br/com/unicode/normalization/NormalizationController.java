package br.com.unicode.normalization;

import java.text.Normalizer;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/normalization")
@RequiredArgsConstructor
class NormalizationController {

    private final NormalizationService normalizationService;

    @PostMapping("/form")
    ResponseEntity<NormalizationResponse> normalize(
            final @RequestBody NormalizationRequest request) {

        var response = normalizationService.normalize(request);

        return ResponseEntity.ok(response);
    }

}

record NormalizationRequest(

        @JsonProperty("text_id") String textId,

        @JsonProperty("input_text") String inputText,

        @JsonProperty("normalization_form") NormalizationForm normalizationForm) {
}

record NormalizationResponse(

        @JsonProperty("text_id") String textId,

        @JsonProperty("output_text") String outputText,

        @JsonProperty("normalization_form") NormalizationForm normalizationForm,

        boolean changed,

        @JsonProperty("code_points") String[] codePoints) {
}

enum NormalizationForm {

    NFC(Normalizer.Form.NFC),
    NFD(Normalizer.Form.NFD),
    NFKC(Normalizer.Form.NFKC),
    NFKD(Normalizer.Form.NFKD);

    private final Normalizer.Form form;

    NormalizationForm(Normalizer.Form form) {
        this.form = form;
    }

    public Normalizer.Form form() {
        return form;
    }
}