package org.example.wwtbam.ui;

import java.util.List;
import java.util.Random;
import javax.swing.*;
import org.example.wwtbam.data.QuestionBank;
import org.example.wwtbam.data.QuestionFactory;
import org.example.wwtbam.data.QuestionLoader;
import org.example.wwtbam.game.GameSession;
import org.example.wwtbam.game.Round;
import org.example.wwtbam.joker.Joker;
import org.example.wwtbam.model.Question;
import org.example.wwtbam.ui.screens.EndScreen;
import org.example.wwtbam.ui.screens.InBetweenScreen;
import org.example.wwtbam.ui.screens.OpenInputQuestionScreen;
import org.example.wwtbam.ui.screens.QuestionScreen;
import org.example.wwtbam.ui.screens.StartScreen;
import org.example.wwtbam.ui.screens.TrueFalseQuestionScreen;

public class GameWindow extends JFrame {

    private final ScreenManager screens = new ScreenManager();
    private final GameSession session;

    private final QuestionScreen questionScreen = new QuestionScreen();
    private final TrueFalseQuestionScreen trueFalseScreen = new TrueFalseQuestionScreen();
    private final OpenInputQuestionScreen openInputScreen = new OpenInputQuestionScreen();
    private final InBetweenScreen inBetweenScreen;
    private final EndScreen endScreen;

    public GameWindow() {
        setTitle("Who Wants to Be a Millionaire");
        setSize(1000, 700);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        Random random = new Random();
        QuestionFactory factory = new QuestionFactory(random);
        QuestionLoader loader = new QuestionLoader(factory);
        List<Question> questions = loader.loadAll();
        QuestionBank bank = new QuestionBank(questions, random);
        session = new GameSession(bank, random);

        inBetweenScreen = new InBetweenScreen(
            this::loadLevel,
            this::handleWithdraw
        );

        endScreen = new EndScreen(() -> screens.showScreen(ScreenManager.START));

        screens.addScreen(
            ScreenManager.START,
            new StartScreen(() -> {
                session.reset();
                loadLevel();
            })
        );

        screens.addScreen(ScreenManager.QUESTION, questionScreen);
        screens.addScreen(ScreenManager.TRUE_FALSE, trueFalseScreen);
        screens.addScreen(ScreenManager.OPEN_INPUT, openInputScreen);
        screens.addScreen(ScreenManager.BETWEEN, inBetweenScreen);
        screens.addScreen(ScreenManager.END, endScreen);

        setContentPane(screens);
        screens.showScreen(ScreenManager.START);
    }

    private void loadLevel() {
        if (session.hasWon()) {
            endScreen.setResults("Congratulations!", "You are a Millionaire!", "€1,000,000");
            screens.showScreen(ScreenManager.END);
            return;
        }

        Round round = session.startLevel();
        int level = session.getLevel();
        Question q = round.getQuestion();
        List<Joker> jokers = session.getJokers().jokersFor(q);

        switch (q.getType()) {
            case MULTIPLE_CHOICE -> {
                questionScreen.setRound(round, level, jokers, this::handleAnswerSubmitted);
                screens.showScreen(ScreenManager.QUESTION);
            }
            case TRUE_FALSE -> {
                trueFalseScreen.setRound(round, level, jokers, this::handleAnswerSubmitted);
                screens.showScreen(ScreenManager.TRUE_FALSE);
            }
            case TYPED_ANSWER -> {
                openInputScreen.setRound(round, level, jokers, this::handleAnswerSubmitted);
                screens.showScreen(ScreenManager.OPEN_INPUT);
            }
        }
    }

    private void handleAnswerSubmitted(String answer) {
        int levelBeforeAnswer = session.getLevel();
        boolean correct = session.submitAnswer(answer);

        if (correct) {
            if (session.hasWon()) {
                endScreen.setResults("Congratulations!", "You are a Millionaire!", "€1,000,000");
                screens.showScreen(ScreenManager.END);
            } else {
                int nextLevel = session.getLevel();
                String bankedMoney = InBetweenScreen.PRIZES[levelBeforeAnswer - 1];
                boolean isSafePoint = (levelBeforeAnswer == 5 || levelBeforeAnswer == 10);
                inBetweenScreen.setRoundInfo(nextLevel, bankedMoney, isSafePoint, session.getJokers().all());
                screens.showScreen(ScreenManager.BETWEEN);
            }
        } else {
            String safePrize;
            if (levelBeforeAnswer <= 5) {
                safePrize = "€0";
            } else if (levelBeforeAnswer <= 10) {
                safePrize = InBetweenScreen.PRIZES[4]; // €1,000 (Safe Point 1)
            } else {
                safePrize = InBetweenScreen.PRIZES[9]; // €32,000 (Safe Point 2)
            }
            endScreen.setResults("Game Over", "Your guaranteed safe prize:", safePrize);
            screens.showScreen(ScreenManager.END);
        }
    }

    private void handleWithdraw() {
        int currentLevel = session.getLevel();
        String walkedAwayMoney = (currentLevel > 1) ? InBetweenScreen.PRIZES[currentLevel - 2] : "€0";
        endScreen.setResults("You Walked Away", "Your final prize:", walkedAwayMoney);
        screens.showScreen(ScreenManager.END);
    }
}