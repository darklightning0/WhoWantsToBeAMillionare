package org.example.wwtbam.data;

import org.example.wwtbam.model.Difficulty;
import org.example.wwtbam.model.Question;
import org.example.wwtbam.model.QuestionType;

public interface QuestionSource {
    Question draw(QuestionType type, Difficulty difficulty);
}
