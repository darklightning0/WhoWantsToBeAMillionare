package org.example.wwtbam.data;

import java.util.List;
import org.example.wwtbam.model.Difficulty;
import org.example.wwtbam.model.QuestionType;

public record QuestionDto(
    String id,
    QuestionType type,
    Difficulty difficulty,
    String category,
    String question,
    Boolean answer,
    String correctAnswer,
    List<String> incorrectAnswers,
    List<String> acceptedAnswers
) {

}