package org.example.adventuretime.ui;

import org.example.adventuretime.configuration.UiMode;
import org.example.adventuretime.ui.cli.CliInterface;
import org.example.adventuretime.ui.gui.GuiInterface;

public final class InterfaceFactory {

    private InterfaceFactory() {
    }

    public static ApplicationInterface create(UiMode mode) {
        return switch (mode) {
            case GUI -> new GuiInterface();
            case CLI -> new CliInterface();
        };
    }
}
