# Who Wants to Be a Millionaire (WWTBAM)

A Java Swing-based desktop trivia quiz game inspired by *Who Wants to Be a Millionaire*.

---

## Prerequisites & Dependencies

Ensure you have the following installed:
- **Java JDK 25** or higher (`java -version`)
- **Apache Maven 3.8+** (`mvn -version`)

All library dependencies (such as Google Gson) and game assets are bundled and resolved automatically through Maven.

---

## Building the Project

To compile the project and download required dependencies:

```bash
mvn clean compile
```

To package the application into a JAR:

```bash
mvn clean package
```

---

## Running the Application

### Option 1: Using Maven (Recommended)
Run the application directly using the Maven exec plugin:

```bash
mvn exec:java
```

*(Alternatively: `mvn compile exec:java` to compile and launch in one step)*

### Option 2: Using the Built JAR
After running `mvn clean package`, launch the JAR with:

```bash
java -cp target/Who_Wants_to_be_a_Millionaire-1.0-SNAPSHOT.jar org.example.wwtbam.Main
```
