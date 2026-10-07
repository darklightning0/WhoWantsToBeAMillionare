# Millionaire quiz – question bank

Nine JSON files: one per question type and difficulty. No question appears in more than one file.

| Type              | Easy                          | Medium                          | Hard                          |
|-------------------|-------------------------------|---------------------------------|-------------------------------|
| `TRUE_FALSE`      | `true_false_easy.json` (61)      | `true_false_medium.json` (71)      | `true_false_hard.json` (14)      |
| `MULTIPLE_CHOICE` | `multiple_choice_easy.json` (81) | `multiple_choice_medium.json` (184) | `multiple_choice_hard.json` (121) |
| `TYPED_ANSWER`    | `typed_answer_easy.json` (51)    | `typed_answer_medium.json` (45)    | `typed_answer_hard.json` (35)    |

663 questions in total. Every file is named `<type>_<difficulty>.json`, so you can build the
path in code, e.g. `type.name().toLowerCase() + "_" + difficulty.name().toLowerCase() + ".json"`.

Categories: General Knowledge, Science & Nature, Geography, History, Mathematics,
Computers, Animals, Mythology, Sports, Film, Music.

## Format

Each file is a plain JSON array. Every question has the same base fields
(`type` and `difficulty` are still stored on each question, even though the file name also says them):

| Field        | Example                         | Notes                                           |
|--------------|---------------------------------|-------------------------------------------------|
| `id`         | `"mc-042"`                      | Unique across all nine files (`tf-`, `mc-`, `ta-`) |
| `type`       | `"MULTIPLE_CHOICE"`             | Matches a Java enum constant                    |
| `difficulty` | `"EASY"`, `"MEDIUM"`, `"HARD"`  | Matches a Java enum constant                    |
| `category`   | `"Geography"`                   |                                                 |
| `question`   | `"What is the capital of ...?"` | Plain UTF-8 text, no HTML entities              |

Plus one type-specific part:

```json
{ "id": "tf-001", "type": "TRUE_FALSE", ..., "answer": true }

{ "id": "mc-001", "type": "MULTIPLE_CHOICE", ...,
  "correctAnswer": "Pup",
  "incorrectAnswers": ["Cub", "Chick", "Kid"] }

{ "id": "ta-001", "type": "TYPED_ANSWER", ...,
  "acceptedAnswers": ["Homo sapiens", "Homo sapiens sapiens"] }
```

## Using it

- **Multiple choice:** the correct answer is stored separately, so shuffle the four options before showing them.
- **Typed answers:** `acceptedAnswers` lists the valid spellings and forms (e.g. `"6"` and `"six"`, `"Wayne Gretzky"` and `"Gretzky"`). Compare the player's input to each after normalising both sides: trim, lowercase, strip accents (`java.text.Normalizer`), collapse spaces. Typed questions are harder than multiple-choice questions with the same difficulty label, since there is nothing to guess from.
- **Loading with Gson:** the field names match Java naming, so you can load a file directly into a list of your own classes, e.g.
  `new Gson().fromJson(reader, new TypeToken<List<MultipleChoiceQuestionDto>>(){}.getType())`.

## Source and licence

The questions come from the [Open Trivia Database](https://opentdb.com), licensed under
[CC BY-SA 4.0](https://creativecommons.org/licenses/by-sa/4.0/).

Changes made to the original data:

- Questions were selected (dated, niche or doubtful ones were left out).
- Wording and typos were edited, some joke or incorrect wrong answers were replaced, and a few difficulty labels were adjusted.
- HTML entities were decoded.
- The typed-answer questions were adapted from Open Trivia DB multiple-choice questions and given lists of accepted answers.

CC BY-SA requires attribution. Credit Open Trivia DB (for example on an About screen), and if you
publish these files, publish them under the same licence.
