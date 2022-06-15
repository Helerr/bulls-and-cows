package bullscows;

import java.util.Scanner;

public class Main {
    private static final Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {

        System.out.println("Please, enter the secret code's length:");
        runApp();

    }

    public static void runApp() {

        int length = scanner.nextInt();
        if (length > 10) {
            System.out.println("Error: can't generate a secret number with a length" +
                    " of 11 because there aren't enough unique digits.");
            return;
        } else {
            System.out.println("Okay, let's start a game!");
        }

        long secretNumber = generateSecretNumber(length);
        System.out.println(secretNumber);
        SecretCode secretCode = new SecretCode(secretNumber);

        int turnCounter = 1;
        while (true) {
            System.out.println("Turn " + turnCounter + ". Answer:");
            int playerGuess = scanner.nextInt();
            if (secretCode.compareGuessWithSecretCode(playerGuess)) {
                return;
            }
        }
    }

    public static long generateSecretNumber(int length) {
        long min = (long) Math.pow(10, length - 1);
        long max = (long) Math.pow(10, length) - 1;
        long random = min - 1;

        while (random < min) {
            random = (long) (Math.random() * max);
        }

        return random;
    }
}

class SecretCode {
    private final long secretCode;

    public SecretCode(long secretCode) {
        this.secretCode = secretCode;
    }

    private int checkBulls(int guess) {
        long secretCodeNumber = secretCode;
        int secretCodeDigit;
        int guessDigit;
        int counter = 0;
        while (secretCodeNumber > 0) {
            secretCodeDigit = (int) secretCodeNumber % 10;
            guessDigit = guess % 10;
            if (secretCodeDigit == guessDigit) {
                counter++;
            }
            secretCodeNumber /= 10;
            guess /= 10;
        }
        return counter;
    }

    private int checkCows(int guess) {
        int counter = 0;
        String secretCodeNumber = secretCode + "";
        String guessNumber = guess + "";

        for (String digit : guessNumber.split("")) {
            if (secretCodeNumber.contains(digit)) {
                counter++;
            }
        }

        return counter;
    }

    public long getSecretCode() {
        return secretCode;
    }

    public boolean compareGuessWithSecretCode(int guess) {

        int cows = checkCows(guess);
        int bulls = checkBulls(guess);

        StringBuilder result = new StringBuilder();
        result.append("Grade: ");
        if (bulls > 0) {
            result.append(bulls).append(" bulls");
        }
        if (cows - bulls > 0) {
            cows -= bulls;
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


