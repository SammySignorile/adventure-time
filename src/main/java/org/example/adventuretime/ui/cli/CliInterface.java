package org.example.adventuretime.ui.cli;

import org.example.adventuretime.ui.ApplicationInterface;

public final class CliInterface implements ApplicationInterface {

    @Override
    public void start(String[] args) {
        new CliApplication().start();
    }
}
