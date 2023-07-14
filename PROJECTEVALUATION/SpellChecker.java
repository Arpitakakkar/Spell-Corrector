import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

public class SpellChecker {
    private Set<String> dictionary;

    public SpellChecker(String dictionaryFile) {
        dictionary = new HashSet<>();
        loadDictionary(dictionaryFile);
    }

    private void loadDictionary(String dictionaryFile) {
        try (BufferedReader reader = new BufferedReader(new FileReader(dictionaryFile))) {
            String word;
            while ((word = reader.readLine()) != null) {
                dictionary.add(word.toLowerCase());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void checkSpelling(String textFile) {
        try (BufferedReader reader = new BufferedReader(new FileReader(textFile))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] words = line.split("\\s+");
                for (String word : words) {
                    String lowercaseWord = word.toLowerCase();
                    if (!dictionary.contains(lowercaseWord)) {
                        System.out.println("Misspelled word: " + word);
                        suggestCorrectSpelling(lowercaseWord);
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void suggestCorrectSpelling(String misspelledWord) {
        System.out.print("Suggestions: ");
        int minDistance = Integer.MAX_VALUE;
        String suggestedWord = null;

        for (String word : dictionary) {
            int distance = calculateLevenshteinDistance(misspelledWord, word);
            if (distance < minDistance) {
                minDistance = distance;
                suggestedWord = word;
            }
        }

        System.out.println(suggestedWord);
    }

    private int calculateLevenshteinDistance(String word1, String word2) {
        int[][] dp = new int[word1.length() + 1][word2.length() + 1];

        for (int i = 0; i <= word1.length(); i++) {
            dp[i][0] = i;
        }

        for (int j = 0; j <= word2.length(); j++) {
            dp[0][j] = j;
        }

        for (int i = 1; i <= word1.length(); i++) {
            for (int j = 1; j <= word2.length(); j++) {
                int cost = Character.toLowerCase(word1.charAt(i - 1)) == Character.toLowerCase(word2.charAt(j - 1)) ? 0 : 1;
                dp[i][j] = Math.min(dp[i - 1][j] + 1, Math.min(dp[i][j - 1] + 1, dp[i - 1][j - 1] + cost));
            }
        }

        return dp[word1.length()][word2.length()];
    }

    public static void main(String[] args) {
        String dictionaryFile = "dictionary.txt"; // Replace with the actual path to your dictionary file
        String textFile = "text.txt"; // Replace with the actual path to your text file

        SpellChecker spellChecker = new SpellChecker(dictionaryFile);
        spellChecker.checkSpelling(textFile);
    }
}

