package org.example.wwtbam.game;

import java.util.*;
import org.example.wwtbam.model.ChoiceQuestion;
import org.example.wwtbam.model.Question;

public class Round {

    private Question question;
    private final Set<String> hiddenOptions = new HashSet<>();
    private Map<String, Integer> audienceVotes = Map.of();
    private String hint;
    private final List<String> jokerMessages = new ArrayList<>();

    public Round(Question question) {
        this.question = Objects.requireNonNull(question, "question");
    }

    public Question getQuestion() {
        return question;
    }

    public List<String> getVisibleOptions() {
        if (question instanceof ChoiceQuestion choice) {
            return choice.getOptions().stream()
                .filter(option -> !hiddenOptions.contains(option))
                .toList();
        }
        return List.of();
    }

    public void hideOptions(Collection<String> options) {
        hiddenOptions.addAll((options));
    }

    public Map<String, Integer> getAudienceVotes() {
        return audienceVotes;
    }

    public void setAudienceVotes(Map<String, Integer> votes) {
        this.audienceVotes = Collections.unmodifiableMap(new LinkedHashMap<>(votes));
    }

    public Optional<String> getHint() {
        return Optional.ofNullable(hint);
    }

    public void setHint(String hint) {
        this.hint = hint;
    }

    public List<String> getJokerMessages() {
        return Collections.unmodifiableList(jokerMessages);
    }

    public void addJokerMessage(String message) {
        jokerMessages.add(message);
    }

    public void replaceQuestion(Question newQuestion) {
        this.question = Objects.requireNonNull(newQuestion, "newQuestion");
        hiddenOptions.clear();
        audienceVotes = Map.of();
        hint = null;
        jokerMessages.clear();
    }

}