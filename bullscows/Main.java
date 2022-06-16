package bullscows;

import java.util.Random;
import java.util.Scanner;

public class Main {
    private static final Scanner scanner = new Scanner(System.in);
    private static final String symbols = "0123456789abcdefghijklmnopqrstuvwxyz";

    public static void main(String[] args) {

        System.out.println("Input the length of the secret code:");
        runApp();

    }

    public static void runApp() {
        int length = validateInput();
        if (length < 1) {
            System.out.println("Error: the length of the secret code cannot be 0 or negative.");
            return;
        }
        System.out.println("Input the number of possible symbols in the code:");
        int characters = validateInput();
        if (!symbolsAmountValidation(length, characters)) return;

        printSecretNumberStars(length, characters);

        System.out.println("Okay, let's start a game!");

        String secretNumber = generateSecretNumber(length, characters);
        System.out.println(secretNumber);
        SecretCode secretCode = new SecretCode(secretNumber);

        int turnCounter = 1;
        while (true) {
            System.out.println("Turn " + turnCounter + ". Answer:");
            String playerGuess = scanner.nextLine();
            if (secretCode.compareGuessWithSecretCode(playerGuess)) {
                return;
            }
            turnCounter++;
        }
    }

    public static boolean symbolsAmountValidation(int length, int characters) {
        if (characters < length) {
            System.out.println("Error: it's not possible to generate a code with a length of " + length +
                    " with " + characters + " unique symbols.");
            return false;
        }
        if (characters > symbols.length()) {
            System.out.println("Error: maximum number of possible symbols in the code is 36 (0-9, a-z).");
            return false;
        }
        return true;
    }

    public static int validateInput() {
        int number = 0;
        String numberRead = scanner.nextLine();
        try {
            number = Integer.parseInt(numberRead);
            return number;
        } catch (NumberFormatException e) {
            System.out.println("Error: \"" + numberRead + "\" isn't a valid number.");
        }
        return number;
    }


    public static void printSecretNumberStars(int length, int chars) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("The secret is prepared: ");
        stringBuilder.append("*".repeat(Math.max(0, length)));

        stringBuilder.append(" (0-");
        if (chars <= 10) {
            stringBuilder.append(symbols.charAt(chars - 1));
        }
        if (chars > 10) {
            stringBuilder.append("9").append(", ").append("a-").append(symbols.charAt(chars - 1));
        }
        stringBuilder.append(").");

        System.out.println(stringBuilder);
    }

    public static String generateSecretNumber(int length, int characters) {
        StringBuilder result = new StringBuilder();
        StringBuilder symbolsBuilder = new StringBuilder(symbols);
        Random random = new Random();

        while (result.length() != length) {
            char symbol = symbolsBuilder.charAt(random.nextInt(characters));
            if (result.indexOf(Character.toString(symbol)) == -1)
                result.append(symbol);
        }

        return result.toString();
    }
}

class SecretCode {
    private final String secretCode;

    public SecretCode(String secretCode) {
        this.secretCode = secretCode;
    }

    private Grade getGrade(String guess) {
        int bulls = 0;
        int cows = 0;

        for (int i = 0; i < guess.length(); i++) {
            if (guess.charAt(i) == secretCode.charAt(i)) {
                bulls += 1;
            } else if (secretCode.indexOf(guess.charAt(i)) != -1) {
                cows += 1;
            }
        }

        return new Grade(bulls, cows);
    }


    public boolean compareGuessWithSecretCode(String guess) {

        int cows = getGrade(guess).cows;
        int bulls = getGrade(guess).bulls;

        StringBuilder result = new StringBuilder();
        result.append("Grade: ");
        if (bulls > 0) {
            result.append(bulls).append(" bulls");
        }
        if (cows > 0) {
            if (bulls > 0) result.append(" and ");
            result.append(cows).append(cows == 1 ? " cow." : " cows.");
        }

        if (cows == 0 && bulls == 0) {
            result.append("None.");
        }
        String code = secretCode + "";
        if (bulls == code.length()) {
            System.out.println("Grade: " + bulls + (bulls == 1 ? " bull." : " bulls."));
            System.out.println("Congratulations! You guessed the secret code.");
            return true;
        }
        System.out.println(result);

        return false;
    }
}

class Grade {
    int bulls;
    int cows;

    Grade(int bulls, int cows) {
        this.bulls = bulls;
        this.cows = cows;
    }
}


