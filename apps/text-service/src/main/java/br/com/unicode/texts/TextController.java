package br.com.unicode.texts;

import java.text.Normalizer;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/texts")
@RequiredArgsConstructor
class TextController {

    private final TextService textService;

    @PostMapping("/normalize")
    ResponseEntity<TextResponse> normalize(
            final @RequestBody TextRequest request) {

        var response = textService.normalize(request);

        return ResponseEntity.ok(response);
    }

}

record TextRequest(
        @JsonProperty("input_text") String inputText,

        @JsonProperty("normalization_form") NormalizationForm normalizationForm) {
}

record TextResponse(
        @JsonProperty("text_id") String textId,

        @JsonProperty("input_text") String inputText,

        @JsonProperty("output_text") String outputText,

        @JsonProperty("normalization_status") NormalizationStatus normalizationStatus) {
}

enum NormalizationForm {

    NFC(Normalizer.Form.NFC),
    NFD(Normalizer.Form.NFD),
    NFKC(Normalizer.Form.NFKC),
    NFKD(Normalizer.Form.NFKD);

    private final Normalizer.Form value;

    NormalizationForm(Normalizer.Form value) {
        this.value = value;
    }

    public Normalizer.Form value() {
        return value;
    }
}

enum NormalizationStatus {

    CHANGED,
    UNCHANGED,
    ERROR;

    static NormalizationStatus from(boolean changed) {
        return changed ? CHANGED : UNCHANGED;
    }
}