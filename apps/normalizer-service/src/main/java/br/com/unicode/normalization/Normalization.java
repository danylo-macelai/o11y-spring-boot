package br.com.unicode.normalization;

import static jakarta.persistence.EnumType.STRING;
import static jakarta.persistence.GenerationType.IDENTITY;

import java.io.Serializable;
import java.time.Instant;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@SuppressWarnings("serial")
@Entity
@Table(name = "NORMALIZATIONS", indexes = {
        @Index(name = "IDX_NORMALIZATIONS_TEXT_ID", columnList = "text_id"),
        @Index(name = "IDX_NORMALIZATIONS_FORM", columnList = "normalization_form"),
        @Index(name = "IDX_NORMALIZATIONS_CREATED_AT", columnList = "created_at")
})
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Normalization implements Serializable {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long id;

    @Column(name = "text_id", nullable = false)
    private String textId;

    @Column(name = "input_text", nullable = false)
    private String inputText;

    @Column(name = "output_text", nullable = false)
    private String outputText;

    @Enumerated(STRING)
    @Column(name = "normalization_form", nullable = false)
    private NormalizationForm normalizationForm;

    @Column(name = "changed", nullable = false)
    private boolean changed;

    @Column(name = "created_at", nullable = false, updatable = false)
    private Instant createdAt;

    @PrePersist
    void prePersist() {
        this.createdAt = Instant.now();
    }
}