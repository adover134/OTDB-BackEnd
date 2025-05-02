package org.duckdns.OTDB_backend.models;

import java.util.ArrayList;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Quiz {
    private String type;
    private String difficulty;
    private String category;
    private String question;
    private String correct_answer;
    private ArrayList<String> incorrect_answers;
    private Boolean correct;
    private Integer chosen;
}
