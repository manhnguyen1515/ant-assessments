import java.util.Set;
import java.util.HashSet;
import java.util.List;
import java.util.Arrays;

public class WordBreak {
    /**
     * The technique I used here is Dynamic Programming (DP).
     * How it works: 
     * We use a 1D boolean DP array dp where:
     * dp[i] is true if the substring s[0..i-1] can be segmented using words in the dictionary.
     * dp[0] = true because an empty string is trivially considered segmented.
     * We then iterate from 1 to s.length() and for each index i, 
     * we check all possible previous positions j < i. 
     * If: dp[j] is true (i.e., s[0..j-1] can be segmented), and
     * s[j..i-1] is a word in the dictionary, 
     * then we can say dp[i] = true, meaning s[0..i-1] can be segmented.
     *
     * Time complexity is approximately O(n²) in the worst case because for each i, 
     * we might look at all j < i, and each substring operation takes up to O(n) time.
     * Using a HashSet for the dictionary gives O(1) average lookup time.
     *
     */
    public static boolean wordBreak(String s, List<String> wordDict) {
        Set<String> wordSet = new HashSet<>(wordDict);  // For O(1) lookup
        boolean[] dp = new boolean[s.length() + 1];
        dp[0] = true;  // Empty string is always "breakable"

        for (int i = 1; i <= s.length(); i++) {
            for (int j = 0; j < i; j++) {
                if (dp[j] && wordSet.contains(s.substring(j, i))) {
                    dp[i] = true;
                    break;
                }
            }
        }

        return dp[s.length()];
    }

    public static void main(String[] args) {
        // Example 1
        String s1 = "leetcode";
        List<String> dict1 = Arrays.asList("leet", "code");
        System.out.println(wordBreak(s1, dict1));  // Output: true

        // Example 2
        String s2 = "applepenapple";
        List<String> dict2 = Arrays.asList("apple", "pen");
        System.out.println(wordBreak(s2, dict2));  // Output: true
    }
}
