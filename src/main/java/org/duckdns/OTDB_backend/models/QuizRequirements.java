package org.duckdns.OTDB_backend.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class QuizRequirements {
    private Integer amount;
    private Integer category;
    private String difficulty;
    private String type;
}
