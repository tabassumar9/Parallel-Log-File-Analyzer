package parallelloganalyzer;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

class LogResult {
    int errorCount = 0;
    int warningCount = 0;
    int infoCount = 0;
    int linesProcessed = 0;
}

class LogProcessorTask implements Callable<LogResult> {
    private final List<String> logLines;

    public LogProcessorTask(List<String> logLines) {
        this.logLines = logLines;
    }

    @Override
    public LogResult call() {
   LogResult result = new LogResult();
   for (String line : logLines) {
   result.linesProcessed++;
   if (line.contains("ERROR")) {
 result.errorCount++;
} else if (line.contains("WARNING")) {
 result.warningCount++;
} else if (line.contains("INFO")) {
 result.infoCount++;
            }
        }
        return result;
    }
}

public class ParallelLogAnalyzer {
    public static void main(String[] args) {
        
List<String> masterLogData = generateSampleLogs(50000);

int numThreads = Runtime.getRuntime().availableProcessors();
 System.out.println("=== Parallel Log File Analyzer ===");
System.out.println("Total lines to process: " + masterLogData.size());
System.out.println("Available CPU Cores (Threads): " + numThreads);

long startTimeParallel = System.currentTimeMillis();

        ExecutorService executor = Executors.newFixedThreadPool(numThreads);
        int chunkSize = (int) Math.ceil((double) masterLogData.size() / numThreads);
        List<Future<LogResult>> futures = new ArrayList<>();

        for (int i = 0; i < masterLogData.size(); i += chunkSize) {
            int end = Math.min(i + chunkSize, masterLogData.size());
            List<String> chunk = masterLogData.subList(i, end);
            futures.add(executor.submit(new LogProcessorTask(chunk)));
        }

        LogResult finalParallelResult = new LogResult();
        try {
            for (Future<LogResult> future : futures) {
                LogResult res = future.get();
                finalParallelResult.errorCount += res.errorCount;
                finalParallelResult.warningCount += res.warningCount;
                finalParallelResult.infoCount += res.infoCount;
                finalParallelResult.linesProcessed += res.linesProcessed;
            }
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        } finally {
            executor.shutdown();
        }

        long endTimeParallel = System.currentTimeMillis();
        long parallelTime = endTimeParallel - startTimeParallel;

        System.out.println("\n--- Analysis Results ---");
        System.out.println("Total Lines Processed : " + finalParallelResult.linesProcessed);
        System.out.println("Total ERRORS Found    : " + finalParallelResult.errorCount);
        System.out.println("Total WARNINGS Found  : " + finalParallelResult.warningCount);
        System.out.println("Total INFOS Found     : " + finalParallelResult.infoCount);
        System.out.println("Parallel Execution Time: " + parallelTime + " ms");
    }

    private static List<String> generateSampleLogs(int count) {
        List<String> logs = new ArrayList<>();
        String[] types = {"INFO: System started successfully", "WARNING: High memory usage detected", "ERROR: Failed to connect to database", "INFO: User logged in"};
        for (int i = 0; i < count; i++) {
            String log = "2026-10-21 10:00:0" + (i % 10) + " [" + types[i % types.length] + "]";
            logs.add(log);
        }
        return logs;
    }
}