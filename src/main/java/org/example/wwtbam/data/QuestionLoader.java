package org.example.wwtbam.data;

import com.google.gson.Gson;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.UncheckedIOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import org.example.wwtbam.model.Difficulty;
import org.example.wwtbam.model.Question;
import org.example.wwtbam.model.QuestionType;

public class QuestionLoader {

    private static final String FOLDER = "/questions/";

    private final Gson gson = new Gson();
    private final QuestionFactory factory;

    public QuestionLoader(QuestionFactory factory) {
        this.factory = factory;
    }

    public List<Question> loadAll() {
        List<Question> all = new ArrayList<>();
        for (QuestionType type : QuestionType.values()) {
            for (Difficulty difficulty : Difficulty.values()) {
                all.addAll(load(type, difficulty));
            }
        }
        return all;
    }
    
    public List<Question> load(QuestionType type, Difficulty difficulty) {
        String path = FOLDER + type.name().toLowerCase(Locale.ROOT)
            + "_" + difficulty.name().toLowerCase(Locale.ROOT) + ".json";
        InputStream stream = QuestionLoader.class.getResourceAsStream(path);
        if (stream == null) {
            throw new IllegalStateException("Missing question file: " + path);
        }
        try (Reader reader = new InputStreamReader(stream, StandardCharsets.UTF_8)) {
            QuestionDto[] dtos = gson.fromJson(reader, QuestionDto[].class);
            if (dtos == null) {
                throw new IllegalStateException("Empty question file: " + path);
            }
            List<Question> questions = new ArrayList<>();
            for (QuestionDto dto : dtos) {
                if (dto.type() != type || dto.difficulty() != difficulty) {
                    throw new IllegalStateException(
                        path + " contains " + dto.id() + " with the wrong type or difficulty");
                }
                questions.add(factory.create(dto));
            }
            return questions;
        } catch (IOException e) {
            throw new UncheckedIOException("Could not read " + path, e);
        }
    }
}
