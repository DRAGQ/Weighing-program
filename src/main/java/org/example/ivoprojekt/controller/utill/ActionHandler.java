package org.example.ivoprojekt.controller.utill;

import java.io.IOException;

@FunctionalInterface
public interface ActionHandler {
    void handle() throws IOException;
}
