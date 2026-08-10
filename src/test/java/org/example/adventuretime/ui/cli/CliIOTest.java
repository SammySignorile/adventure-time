package org.example.adventuretime.ui.cli;

import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

/** Responsabile test: Sammy Signorile (matricola da inserire). */
class CliIOTest {

    @Test
    void writesMessagesToInjectedStreams() {
        ByteArrayOutputStream outputBytes = new ByteArrayOutputStream();
        ByteArrayOutputStream errorBytes = new ByteArrayOutputStream();
        CliIO io = new CliIO(
                new Scanner("testo inserito"),
                writer(outputBytes),
                writer(errorBytes)
        );

        io.info("Messaggio");
        io.error("Problema");
        String value = io.readText("Valore: ");

        assertEquals("testo inserito", value);
        assertTrue(outputBytes.toString(StandardCharsets.UTF_8)
                .contains("Messaggio"));
        assertTrue(errorBytes.toString(StandardCharsets.UTF_8)
                .contains("ERRORE: Problema"));
    }

    private static PrintWriter writer(ByteArrayOutputStream bytes) {
        return new PrintWriter(bytes, true, StandardCharsets.UTF_8);
    }
}
