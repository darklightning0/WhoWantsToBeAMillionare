# Who Wants to Be a Millionaire (WWTBAM)

A Java Swing-based desktop trivia quiz game inspired by *Who Wants to Be a Millionaire*.

**GitHub repository:** https://github.com/darklightning0/WhoWantsToBeAMillionare

## 1. Unzip the project

Unzip the submitted `.zip` file. The unzipped folder that contains `pom.xml` and `src/` is the project.

## 2. Prerequisites

Ensure you have the following installed:
- **Java JDK 25** or higher (`java -version`)
- **Apache Maven 3.8+** (`mvn -version`)

All library dependencies (such as Google Gson) and game assets are bundled and resolved automatically through Maven.

## 3. Open the project and load the Gson dependency

The game reads its questions from JSON files using the **Gson** library. Gson is declared as a Maven dependency in `pom.xml`, so you don't need to install it yourself:

```xml
<dependency>
    <groupId>com.google.code.gson</groupId>
    <artifactId>gson</artifactId>
    <version>2.14.0</version>
</dependency>
```

1. In IntelliJ IDEA, choose **File > Open** and select the unzipped project folder (the one with `pom.xml`).
2. IntelliJ downloads Gson automatically; an internet connection is needed the first time. If it doesn't, open the **Maven** tool window and click **Reload All Maven Projects**.
3. The project uses **JDK 25**. If IntelliJ asks for an SDK, select JDK 25 under **File > Project Structure > Project > SDK**.



## Running the Application

## 4. Run in IntelliJ IDEA

1. Choose **File > Open** and select the unzipped project folder (the one with `pom.xml`).
2. Gson is downloaded automatically. If it isn't, open the **Maven** tool window and click **Reload All Maven Projects**.
3. If IntelliJ asks for an SDK, select JDK 25 under **File > Project Structure > Project > SDK**.
4. Open `src/main/java/org/example/wwtbam/Main.java` and click the green run arrow next to `main`, or right-click the file and choose **Run 'Main.main()'**.

## 5. Run in Apache NetBeans

1. Choose **File > Open Project** and select the unzipped project folder. NetBeans recognises it as a Maven project.
2. Right-click the project and choose **Clean and Build**. This downloads Gson. If NetBeans shows a warning about missing dependencies, right-click the project and choose **Resolve Project Problems**.
3. If JDK 25 is not selected, add it under **Tools > Java Platforms**, then choose it under project **Properties > Build > Compile > Java Platform**.
4. In the **Projects** window, open **Source Packages > org.example.wwtbam**, right-click `Main.java` and choose **Run File** (Shift+F6).

## 6. Without an IDE

Run `mvn compile exec:java` from the project folder (requires Maven and JDK 25).

