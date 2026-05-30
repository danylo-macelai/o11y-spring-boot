package br.com.unicode.normalization;

import java.text.Normalizer;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class NormalizationService {

    private final NormalizationRepository repository;

    @Transactional
    public NormalizationResponse normalize(final NormalizationRequest request) {

        var outputText = normalizeText(request.inputText(), request.normalizationForm());

        var changed = !outputText.equals(request.inputText());

        var entity = Normalization.builder()
                .textId(request.textId())
                .inputText(request.inputText())
                .outputText(outputText)
                .normalizationForm(request.normalizationForm())
                .changed(changed)
                .build();

        repository.save(entity);

        return new NormalizationResponse(
                request.textId(),
                outputText,
                request.normalizationForm(),
                changed,
                toCodePoints(outputText));
    }

    private String normalizeText(final String text, final NormalizationForm normalizationForm) {
        return Normalizer.normalize(text, normalizationForm.form());
    }

    private String[] toCodePoints(final String outputText) {
        return outputText.codePoints()
                .mapToObj(cp -> String.format("U+%04X", cp))
                .toArray(String[]::new);
    }
}