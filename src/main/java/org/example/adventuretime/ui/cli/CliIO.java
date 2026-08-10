package org.example.adventuretime.ui.cli;

import java.io.FileDescriptor;
import java.io.FileOutputStream;
import java.io.PrintWriter;
import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.Scanner;

public final class CliIO {

    private final Scanner scanner;
    private final PrintWriter output;
    private final PrintWriter errors;

    public CliIO(Scanner scanner) {
        this(
                scanner,
                writerFor(FileDescriptor.out),
                writerFor(FileDescriptor.err)
        );
    }

    CliIO(Scanner scanner, PrintWriter output, PrintWriter errors) {
        this.scanner = scanner;
        this.output = output;
        this.errors = errors;
    }

    public void title(String text) {
        output.println();
        output.println("==================================================");
        output.println(text);
        output.println("==================================================");
    }

    public void info(String text) {
        output.println(text);
    }

    public void error(String text) {
        errors.println("ERRORE: " + text);
    }

    public String readText(String prompt) {
        output.print(prompt);
        output.flush();
        return scanner.nextLine().trim();
    }

    public int readInt(String prompt) {
        while (true) {
            String text = readText(prompt);
            try {
                return Integer.parseInt(text);
            } catch (NumberFormatException e) {
                error("Inserire un numero intero.");
            }
        }
    }

    public long readLong(String prompt) {
        while (true) {
            String text = readText(prompt);
            try {
                return Long.parseLong(text);
            } catch (NumberFormatException e) {
                error("Inserire un identificativo numerico.");
            }
        }
    }

    public BigDecimal readDecimal(String prompt) {
        while (true) {
            String text = readText(prompt).replace(',', '.');
            try {
                return new BigDecimal(text);
            } catch (NumberFormatException e) {
                error("Inserire un importo valido.");
            }
        }
    }

    public LocalDate readDate(String prompt) {
        while (true) {
            String text = readText(prompt + " [AAAA-MM-GG]: ");
            try {
                return LocalDate.parse(text);
            } catch (DateTimeParseException e) {
                error("Data non valida. Esempio: 2026-09-15.");
            }
        }
    }

    public boolean readYesNo(String prompt) {
        while (true) {
            String text = readText(prompt + " [s/n]: ");
            if ("s".equalsIgnoreCase(text)) {
                return true;
            }
            if ("n".equalsIgnoreCase(text)) {
                return false;
            }
            error("Rispondere con s oppure n.");
        }
    }

    private static PrintWriter writerFor(FileDescriptor descriptor) {
        return new PrintWriter(
                new FileOutputStream(descriptor),
                true,
                StandardCharsets.UTF_8
        );
    }
}
