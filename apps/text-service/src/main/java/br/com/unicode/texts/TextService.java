package br.com.unicode.texts;

import static br.com.unicode.texts.NormalizationStatus.CHANGED;
import static br.com.unicode.texts.NormalizationStatus.ERROR;
import static java.lang.Boolean.TRUE;
import static java.util.UUID.randomUUID;

import org.springframework.stereotype.Service;

import br.com.unicode.normalization.NormalizationClient;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class TextService {

    private final TextRepository      repository;
    private final NormalizationClient normalizationClient;

    public TextResponse normalize(final TextRequest request) {

        var textId = randomUUID();

        log.atInfo()
                .addKeyValue("event", "text.received")
                .addKeyValue("text_id", textId)
                .addKeyValue("normalization_form", request.normalizationForm())
                .log();

        var entity = Text.builder()
                .id(textId)
                .inputText(request.inputText())
                .normalizationForm(request.normalizationForm())
                .normalizationStatus(CHANGED)
                .build();

        repository.save(entity);

        log.atInfo()
                .addKeyValue("event", "text.persisted.success")
                .addKeyValue("text_id", textId)
                .addKeyValue("normalization_status", entity.getNormalizationStatus().name())
                .log();

        NormalizationStatus status;
        String outputText = null;

        try {

            log.atInfo()
                    .addKeyValue("event", "normalization.request.sent")
                    .addKeyValue("text_id", textId)
                    .log();

            var normalization = normalizationClient.normalize(entity);

            outputText = String.valueOf(normalization.get("output_text"));
            var changed = TRUE.equals(normalization.get("changed"));

            log.atInfo()
                    .addKeyValue("event", "normalization.result.received")
                    .addKeyValue("text_id", textId)
                    .addKeyValue("changed", changed)
                    .log();

            status = NormalizationStatus.from(changed);
        } catch (Exception e) {

            log.atInfo()
                    .addKeyValue("event", "normalization.failed")
                    .addKeyValue("text_id", textId)
                    .addKeyValue("error", e.getMessage())
                    .log();

            status = ERROR;
        }

        entity.setNormalizationStatus(status);

        log.atInfo()
                .addKeyValue("event", "text.status.changed")
                .addKeyValue("text_id", textId)
                .addKeyValue("normalization_status", entity.getNormalizationStatus().name())
                .log();

        repository.save(entity);

        log.atInfo()
                .addKeyValue("event", "text.response.sent")
                .addKeyValue("text_id", textId)
                .addKeyValue("normalization_status", entity.getNormalizationStatus().name())
                .log();

        return new TextResponse(
                entity.getId().toString(),
                entity.getInputText(),
                outputText,
                entity.getNormalizationStatus());

    }
}