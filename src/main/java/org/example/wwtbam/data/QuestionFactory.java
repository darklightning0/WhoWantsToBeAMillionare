package org.example.wwtbam.data;

import java.util.Random;
import org.example.wwtbam.model.MultipleChoiceQuestion;
import org.example.wwtbam.model.Question;
import org.example.wwtbam.model.TrueFalseQuestion;
import org.example.wwtbam.model.TypedAnswerQuestion;

public class QuestionFactory {

    private final Random random;

    public QuestionFactory(Random random) {
        this.random = random;
    }

    public Question create(QuestionDto dto) {
        return switch (dto.type()) {
            case TRUE_FALSE -> new TrueFalseQuestion(
                dto.id(), dto.question(), dto.category(), dto.difficulty(),
                required(dto.answer(), dto, "answer"));
            case MULTIPLE_CHOICE -> new MultipleChoiceQuestion(
                dto.id(), dto.question(), dto.category(), dto.difficulty(),
                required(dto.correctAnswer(), dto, "correctAnswer"),
                required(dto.incorrectAnswers(), dto, "incorrectAnswers"),
                random);
            case TYPED_ANSWER -> new TypedAnswerQuestion(
                dto.id(), dto.question(), dto.category(), dto.difficulty(),
                required(dto.acceptedAnswers(), dto, "acceptedAnswers"));
        };
    }

    private static <T> T required(T value, QuestionDto dto, String field) {
        if (value == null) {
            throw new IllegalArgumentException(dto.id() + ": missing field '" + field + "'");
        }
        return value;
    }
}
