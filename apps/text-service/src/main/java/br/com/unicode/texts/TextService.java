package br.com.unicode.texts;

import static br.com.unicode.texts.NormalizationStatus.CHANGED;
import static br.com.unicode.texts.NormalizationStatus.ERROR;
import static java.lang.Boolean.TRUE;
import static java.util.UUID.randomUUID;

import org.springframework.stereotype.Service;

import br.com.unicode.normalization.NormalizationClient;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class TextService {

    private final TextRepository      repository;
    private final NormalizationClient normalizationClient;

    public TextResponse normalize(final TextRequest request) {

        var textId = randomUUID();

        var entity = Text.builder()
                .id(textId)
                .inputText(request.inputText())
                .normalizationForm(request.normalizationForm())
                .normalizationStatus(CHANGED)
                .build();

        repository.save(entity);

        NormalizationStatus status;
        String outputText = null;

        try {

            var normalization = normalizationClient.normalize(entity);

            outputText = String.valueOf(normalization.get("output_text"));
            var changed = TRUE.equals(normalization.get("changed"));

            status = NormalizationStatus.from(changed);
        } catch (Exception e) {

            status = ERROR;
        }

        entity.setNormalizationStatus(status);
        repository.save(entity);

        return new TextResponse(
                entity.getId().toString(),
                entity.getInputText(),
                outputText,
                entity.getNormalizationStatus());

    }
}