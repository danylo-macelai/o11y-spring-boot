package br.com.unicode.normalization;

import java.text.Normalizer;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class NormalizationService {

    private final NormalizationRepository repository;

    @Transactional
    public NormalizationResponse normalize(final NormalizationRequest request) {

        log.atInfo()
                .addKeyValue("event", "normalization.started")
                .addKeyValue("text_id", request.textId())
                .addKeyValue("input_text", request.inputText())
                .addKeyValue("normalization_form", request.normalizationForm().name())
                .log(); 

        var outputText = normalizeText(request.inputText(), request.normalizationForm());

        var changed = !outputText.equals(request.inputText());

        log.atInfo()
                .addKeyValue("event", "normalization.completed")
                .addKeyValue("text_id", request.textId())
                .addKeyValue("output_text", outputText)
                .addKeyValue("changed", changed)
                .log();

        var entity = Normalization.builder()
                .textId(request.textId())
                .inputText(request.inputText())
                .outputText(outputText)
                .normalizationForm(request.normalizationForm())
                .changed(changed)
                .build();

        log.atInfo()
                .addKeyValue("event", "normalization.persisted")
                .addKeyValue("text_id", entity.getTextId())
                .log();

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