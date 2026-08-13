package org.example.adventuretime.ui.gui;

import javafx.application.Application;
import org.example.adventuretime.ui.ApplicationInterface;

public final class GuiInterface implements ApplicationInterface {

    @Override
    public void start(String[] args) {
        Application.launch(GuiApplication.class, args);
    }
}
