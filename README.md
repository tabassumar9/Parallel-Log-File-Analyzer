# Parallel Log File Analyzer

A Java-based multi-threaded application designed to parse and analyze large log files efficiently by utilizing multi-core CPU architectures.

## 📌 Project Overview
Modern software systems generate massive log files daily. Sequential log parsing creates performance bottlenecks. This project uses Java Concurrency utilities (`ExecutorService`, `Callable`, and `Future`) to partition log datasets and process them concurrently, significantly reducing execution time.

---

## 🏗️ System Architecture & Data Flow
```mermaid
graph TD
    A[Main Controller: ParallelLogAnalyzer] -->|Splits Log Data into Chunks| B(ExecutorService Thread Pool)
    B --> C1[Worker Thread 1]
    B --> C2[Worker Thread 2]
    B --> CN[Worker Thread N]
    C1 -->|Returns LogResult via Future| D[Result Aggregator]
    C2 -->|Returns LogResult via Future| D
    CN -->|Returns LogResult via Future| D
    D --> E[Final Consolidated Output & Performance Benchmark]
🚀 Key Features
Multi-Threaded Processing: Distributes log analysis across available CPU cores using a fixed thread pool.

Concurrency Control: Utilizes Callable and Future to safely fetch and aggregate results from worker threads.

Performance Benchmarking: Automatically measures and compares execution speeds.

📊 Testing & Performance Results
Dataset Size: 50,000 simulated log entries (INFO, WARNING, ERROR)

Single-Threaded Execution Time: ~145 ms

Multi-Threaded Execution Time: ~42 ms

Performance Speedup: ~3.4x faster execution!

🛠️ How to Run
Clone the repository or download ParallelLogAnalyzer.java.

Open the file in an IDE (e.g., Apache NetBeans or VS Code).

Run the project to view the multi-threaded log analysis and performance metrics in the console.
