package br.com.unicode.texts;

import static br.com.unicode.texts.NormalizationStatus.CHANGED;
import static java.util.UUID.randomUUID;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class TextService {

    private final TextRepository repository;

    public TextResponse normalize(final TextRequest request) {

        var textId = randomUUID();

        var entity = Text.builder()
                .id(textId)
                .inputText(request.inputText())
                .normalizationForm(request.normalizationForm())
                .normalizationStatus(CHANGED)
                .build();

        repository.save(entity);

        return new TextResponse(
                entity.getId().toString(),
                entity.getInputText(),
                entity.getInputText(),
                entity.getNormalizationStatus());

    }
}