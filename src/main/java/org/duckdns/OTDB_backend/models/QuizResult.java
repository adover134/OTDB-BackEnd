package org.duckdns.OTDB_backend.models;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class QuizResult {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
    private Integer amount;
    private Integer category;
    private String difficulty;
    private String type;
    private Integer corrects;
    @JsonIgnore
    private String userId;
}
