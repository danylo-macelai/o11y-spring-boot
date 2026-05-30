package br.com.unicode.normalization;

import java.text.Normalizer;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.annotation.JsonProperty;

@RestController
@RequestMapping("/normalization")
class NormalizationController {

    @PostMapping("/form")
    ResponseEntity<NormalizationResponse> normalize(
            @RequestBody NormalizationRequest request) {

        var response = new NormalizationResponse(
                request.textId(),
                request.inputText(),
                request.normalizationForm(),
                true,
                new String[] {
                        "U+0063",
                        "U+0061",
                        "U+0066",
                        "U+0065",
                        "U+0301"
                });

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