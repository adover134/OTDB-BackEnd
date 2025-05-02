package org.duckdns.OTDB_backend.models;

import java.util.ArrayList;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class QuizSetRequest {
    private ArrayList<Quiz> quizSet;
    private Integer nextQuiz;
    private Integer quizNum;
}
