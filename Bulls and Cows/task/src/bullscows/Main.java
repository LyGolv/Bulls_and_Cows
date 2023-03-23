package bullscows;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Scanner;
import java.util.stream.IntStream;

public class Main {

    private static final Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        System.out.println("Input the length of the secret code:");
        String size = scanner.nextLine();
        if (handleDigitError(size)) return;
        System.out.println("Input the number of possible symbols in the code:");
        String range = scanner.nextLine();
        if (handleDigitError(range)) return;
        if (handleGenerationError(Integer.parseInt(size), Integer.parseInt(range))) return;
        Character[] bounds = getBounds(Integer.parseInt(range));
        String secret = generateSecret(Integer.parseInt(size), bounds);
        start(secret, bounds);
    }

    private static void start(String secret, Character[] bounds) {
        System.out.printf("The secret is prepared: %s %s.\n", "*".repeat(secret.length()), showBounds(bounds));
        System.out.println("Okay, let's start a game!");
        int attempt = 0;
        do {
            System.out.println("Turn " + ++attempt);
        } while (!grade(scanner.nextLine(), secret));
        System.out.println("Congratulations! You guessed the secret code.");
    }

    private static Character[] getBounds(int range) {
        List<Character> l = new ArrayList<>();
        l.add('0');
        if (10 - range < 0) {
            for (char c : new char[]{'9', 'a', (char) ('a' + (range - 11))}) {
                l.add(c);
            }
        } else l.add((char)('0'+range-1));
        return l.toArray(Character[]::new);
    }

    private static String showBounds(Character[] bounds) {
        List<String> l = new ArrayList<>();
        IntStream.iterate(0, i -> i < bounds.length, i -> i + 2)
                .forEach(i -> l.add("%c-%c".formatted(bounds[i], bounds[i+1])));
        return "(" + String.join(", ", l) + ")";
    }

    private static String generateSecret(int size, Character[] bounds) {
        List<String> list = new ArrayList<>();
        IntStream.iterate(0, i -> i < bounds.length, i -> i + 2)
                .forEach(i -> {for (char j = bounds[i]; j < bounds[i+1]+1; ++j) list.add(j+"");});
        Collections.shuffle(list);
        StringBuilder result = new StringBuilder();
        while ((--size) >= 0) result.append(list.get(size));
        return result.toString();
    }

    private static boolean grade(String nb, String secret) {
        int[] grade = new int[2];
        for (int i = 0; i < secret.length(); i++) {
            if (nb.charAt(i) == secret.charAt(i)) grade[0]++;
            else if (secret.contains(nb.charAt(i)+"")) grade[1]++;
        }
        System.out.println("Grade: "
                + (grade[0] > 0 ? grade[0] + " bull" + (grade[0] == 1 ? "" : "s") : "")
                + (grade[1] > 0 ? grade[1] + " cow" + (grade[1] == 1 ? "" : "s") : "")
                + (grade[0] == 0 && grade[1] == 0 ? "None" : ""));
        return grade[0] == secret.length();
    }

    private static boolean handleGenerationError(int size, int range) {
        if (range < size || size == 0) {
            System.out.println("Error: it's not possible to generate a code with a length of " + size
                    + " with " + range + " unique symbols.");
            return true;
        } else if (range > 36) {
            System.out.println("Error: maximum number of possible symbols in the code is 36 (0-9, a-z).");
            return true;
        }
        return false;
    }

    private static boolean handleDigitError(String number) {
        if (number.matches("^\\d*$")) return false;
        System.out.printf("Error: \"%s\" isn't a valid number.", number);
        return true;
    }
}
