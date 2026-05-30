package br.com.unicode.texts;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

public interface TextRepository extends JpaRepository<Text, UUID> {

}
