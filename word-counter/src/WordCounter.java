import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

public class WordCounter {

    private static final ConcurrentMap<String, AtomicInteger> wordCount = new ConcurrentHashMap<>();

    public static void main(String[] args) throws InterruptedException {
        // Define a list of file name
        List<String> files = List.of("./uploads/file1.txt", "./uploads/file2.txt", "./uploads/file3.txt");

        // Use multithreading to speed up processing
        // Create a thread pool with number of threads equal to number of files
        ExecutorService executor = Executors.newFixedThreadPool(files.size());

        // Submit one task per file to the thread pool
        for (String file : files) {
            executor.submit(() -> processFile(file)); // Multithreaded processing
        }

        // Wait for all tasks to finish
        executor.shutdown();
        executor.awaitTermination(1, TimeUnit.MINUTES);

        // Output results
        wordCount.forEach((word, count) ->
            System.out.println(word + " " + count)
        );
    }

    // Function to read and process a single file
    private static void processFile(String filePath) {
        try {
            // Read file content line by line
            List<String> lines = Files.readAllLines(Paths.get(filePath));
            for (String line : lines) {
                // Split line into words
                String[] words = line.split("\\s+");
                for (String word : words) {
                    // Update word count in a thread-safe way
                    wordCount
                        .computeIfAbsent(word, k -> new AtomicInteger(0))
                        .incrementAndGet();
                }
            }
        } catch (IOException e) {
            System.err.println("Failed to read file: " + filePath);
            e.printStackTrace();
        }
    }
}
