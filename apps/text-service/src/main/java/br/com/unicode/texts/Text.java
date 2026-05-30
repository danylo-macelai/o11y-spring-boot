package br.com.unicode.texts;

import static jakarta.persistence.EnumType.STRING;

import java.io.Serializable;
import java.time.Instant;
import java.util.UUID;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@SuppressWarnings("serial")
@Entity
@Table(name = "TEXTS", //
        uniqueConstraints = {
                @UniqueConstraint(name = "UK_TEXTS_TEXT_ID", columnNames = "text_id") }, //
        indexes = {
                @Index(name = "IDX_TEXTS_INPUT_TEXT", columnList = "input_text"),
                @Index(name = "IDX_TEXTS_FORM", columnList = "normalization_form"),
                @Index(name = "IDX_TEXTS_CREATED_AT", columnList = "created_at") })
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Text implements Serializable {

    @Id
    @Column(name = "text_id", updatable = false, nullable = false, unique = true)
    private UUID id;

    @Column(name = "input_text", nullable = false)
    private String inputText;

    @Enumerated(STRING)
    @Column(name = "normalization_form", nullable = false)
    private NormalizationForm normalizationForm;

    @Enumerated(STRING)
    @Column(name = "normalization_status", nullable = false)
    private NormalizationStatus normalizationStatus;

    @Column(name = "created_at", updatable = false)
    private java.time.Instant createdAt;

    @PrePersist
    void prePersist() {
        this.createdAt = Instant.now();
    }
}