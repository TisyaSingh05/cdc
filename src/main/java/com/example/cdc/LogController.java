package com.example.cdc;

import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.nio.file.*;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@RestController
public class LogController {

    private final AppLogRepository repo;

    private volatile boolean importing = false;
    private final List<String> logLines = new ArrayList<>();
    private int currentIndex = 0;

    public LogController(AppLogRepository repo) {
        this.repo = repo;
    }

    @GetMapping("/login")
    public String login() {
        long start = System.currentTimeMillis();

        AppLog log = new AppLog();
        log.setLogLevel("INFO");
        log.setAppUser("emma.watson");
        log.setIpAddress("10.0.5.21");
        log.setEndpoint("/user/login");
        log.setAction("USER_LOGIN");
        log.setErrorMessage(null);
        log.setResponseTimeMs(System.currentTimeMillis() - start);
        repo.save(log);
        return "Login success";
    }

    @GetMapping("/make-error")
    public String makeError() {
        long start = System.currentTimeMillis();

        AppLog log = new AppLog();
        log.setLogLevel("ERROR");
        log.setAppUser("olivia.smith");
        log.setIpAddress("10.10.2.18");
        log.setEndpoint("/make-error");
        log.setAction("ERROR");
        log.setErrorMessage("AppRuntimeException: Something went wrong");
        log.setResponseTimeMs(System.currentTimeMillis() - start);

        repo.save(log);
        return "Error log saved";
    }

    @GetMapping("/start-import")
    public String startImport() {
        if (importing) {
            return "Import is already running. Progress: " + currentIndex + "/" + logLines.size();
        }

        loadLogFiles();

        if (logLines.isEmpty()) {
            return "No log lines found in C:\\APP_LOGS";
        }

        importing = true;
        currentIndex = 0;

        Thread thread = new Thread(() -> {
            while (importing && currentIndex < logLines.size()) {
                String line = logLines.get(currentIndex);
                currentIndex++;

                if (line != null && !line.isBlank()) {
                    try {
                        AppLog log = parseLineToAppLog(line);
                        repo.save(log);
                        System.out.println("Inserted log line " + currentIndex);
                    } catch (Exception e) {
                        System.out.println("Skipped bad log line " + currentIndex + ": " + line);
                        e.printStackTrace();
                    }
                }

                try {
                    Thread.sleep(10); // 100 log lines per second
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    importing = false;
                }
            }

            importing = false;
            System.out.println("Log import completed.");
        });

        thread.start();

        return "Started importing " + logLines.size() + " log lines at 100 lines per second";
    }

    @GetMapping("/stop-import")
    public String stopImport() {
        importing = false;
        return "Import stopped at line " + currentIndex;
    }

    @GetMapping("/import-status")
    public String importStatus() {
        return "Importing: " + importing +
                ", Progress: " + currentIndex + "/" + logLines.size();
    }

    private void loadLogFiles() {
        logLines.clear();

        Path folder = Paths.get("C:\\APP_LOGS");

        try {
            List<Path> files = Files.list(folder)
                    .filter(path ->
                            path.toString().endsWith(".log") ||
                            path.toString().endsWith(".txt"))
                    .toList();

            for (Path file : files) {
                logLines.addAll(Files.readAllLines(file));
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private AppLog parseLineToAppLog(String line) {
        AppLog log = new AppLog();

        log.setAppUser("unknown");
        log.setIpAddress("127.0.0.1");
        log.setEndpoint("/unknown");
        log.setAction("LOG_FILE_IMPORT");
        log.setErrorMessage(null);
        log.setResponseTimeMs((long) (Math.random() * 900) + 100);

        String lower = line.toLowerCase();

        if (lower.contains("error") || lower.contains("exception") || lower.contains("failed")) {
            log.setLogLevel("ERROR");
            log.setAction("ERROR");
            log.setErrorMessage(line);
        } else if (lower.contains("warn")) {
            log.setLogLevel("WARN");
            log.setAction("WARNING");
            log.setErrorMessage(line);
        } else {
            log.setLogLevel("INFO");
        }

        Matcher ipMatcher = Pattern.compile("\\b(?:\\d{1,3}\\.){3}\\d{1,3}\\b").matcher(line);
        if (ipMatcher.find()) {
            log.setIpAddress(ipMatcher.group());
        }

        Matcher endpointMatcher = Pattern.compile("(/[^\\s]+)").matcher(line);
        if (endpointMatcher.find()) {
            log.setEndpoint(endpointMatcher.group(1));
        }

        if (lower.contains("login")) {
            log.setAction("LOGIN");
        } else if (lower.contains("logout")) {
            log.setAction("LOGOUT");
        } else if (lower.contains("post")) {
            log.setAction("POST_HANDLER");
        } else if (lower.contains("get")) {
            log.setAction("GET_HANDLER");
        } else if (lower.contains("delete")) {
            log.setAction("DELETE_HANDLER");
        } else if (lower.contains("put")) {
            log.setAction("PUT_HANDLER");
        }

        if (lower.contains("kamal")) {
            log.setAppUser("kamal");
        } else if (lower.contains("john")) {
            log.setAppUser("john.doe");
        } else if (lower.contains("admin")) {
            log.setAppUser("admin");
        } else if (lower.contains("test")) {
            log.setAppUser("test.user");
        }

        return log;
    }
}