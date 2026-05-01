package com.imetro.util;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.logging.ConsoleHandler;
import java.util.logging.FileHandler;
import java.util.logging.Formatter;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.LogRecord;
import java.util.logging.Logger;

public final class AppLogger {

    private static final AtomicBoolean CONFIGURED = new AtomicBoolean(false);
    private static final String LOGGER_NAMESPACE = "com.imetro";
    private static final Path LOG_DIR = Paths.get("logs");
    private static final Path LOG_FILE = LOG_DIR.resolve("imetro-app.log");
    private static final DateTimeFormatter TIMESTAMP_FORMAT = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    private AppLogger() {
    }

    public static Logger getLogger(Class<?> type) {
        configure();
        return Logger.getLogger(type.getName());
    }

    public static Path getLogFilePath() {
        configure();
        return LOG_FILE.toAbsolutePath().normalize();
    }

    public static void configure() {
        if (!CONFIGURED.compareAndSet(false, true)) {
            return;
        }

        try {
            Files.createDirectories(LOG_DIR);
        } catch (IOException e) {
            System.err.println("Nao foi possivel criar a pasta de logs: " + e.getMessage());
        }

        Logger namespaceLogger = Logger.getLogger(LOGGER_NAMESPACE);
        namespaceLogger.setUseParentHandlers(false);
        namespaceLogger.setLevel(Level.ALL);

        Formatter formatter = new AppLogFormatter();
        namespaceLogger.addHandler(buildConsoleHandler(formatter));

        FileHandler fileHandler = buildFileHandler(formatter);
        if (fileHandler != null) {
            namespaceLogger.addHandler(fileHandler);
        }
    }

    public static void installDefaultExceptionHandler() {
        configure();
        Thread.setDefaultUncaughtExceptionHandler((thread, throwable) -> {
            Logger logger = getLogger(AppLogger.class);
            logger.log(
                Level.SEVERE,
                "Excecao nao tratada na thread " + thread.getName(),
                throwable
            );
        });
    }

    private static Handler buildConsoleHandler(Formatter formatter) {
        ConsoleHandler handler = new ConsoleHandler();
        handler.setLevel(Level.ALL);
        handler.setFormatter(formatter);
        return handler;
    }

    private static FileHandler buildFileHandler(Formatter formatter) {
        try {
            FileHandler handler = new FileHandler(LOG_FILE.toString(), true);
            handler.setEncoding("UTF-8");
            handler.setLevel(Level.ALL);
            handler.setFormatter(formatter);
            return handler;
        } catch (IOException e) {
            System.err.println("Nao foi possivel abrir o ficheiro de logs: " + e.getMessage());
            return null;
        }
    }

    private static final class AppLogFormatter extends Formatter {
        @Override
        public String format(LogRecord record) {
            StringBuilder out = new StringBuilder();
            out.append(LocalDateTime.now().format(TIMESTAMP_FORMAT))
                .append(" [")
                .append(record.getLevel().getName())
                .append("] ")
                .append("[")
                .append(Thread.currentThread().getName())
                .append("] ")
                .append(record.getLoggerName())
                .append(" - ")
                .append(formatMessage(record))
                .append(System.lineSeparator());

            Throwable thrown = record.getThrown();
            if (thrown != null) {
                out.append(stackTraceToString(thrown));
            }
            return out.toString();
        }

        private String stackTraceToString(Throwable throwable) {
            StringBuilder out = new StringBuilder();
            out.append(throwable).append(System.lineSeparator());
            for (StackTraceElement element : throwable.getStackTrace()) {
                out.append("\tat ").append(element).append(System.lineSeparator());
            }
            Throwable cause = throwable.getCause();
            while (cause != null) {
                out.append("Caused by: ").append(cause).append(System.lineSeparator());
                for (StackTraceElement element : cause.getStackTrace()) {
                    out.append("\tat ").append(element).append(System.lineSeparator());
                }
                cause = cause.getCause();
            }
            return out.toString();
        }
    }
}
