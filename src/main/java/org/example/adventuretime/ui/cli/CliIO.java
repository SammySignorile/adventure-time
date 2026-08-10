package org.example.adventuretime.ui.cli;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.Scanner;

public final class CliIO {

    private final Scanner scanner;

    public CliIO(Scanner scanner) {
        this.scanner = scanner;
    }

    public void title(String text) {
        System.out.println();
        System.out.println("==================================================");
        System.out.println(text);
        System.out.println("==================================================");
    }

    public void info(String text) {
        System.out.println(text);
    }

    public void error(String text) {
        System.err.println("ERRORE: " + text);
    }

    public String readText(String prompt) {
        System.out.print(prompt);
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
}
