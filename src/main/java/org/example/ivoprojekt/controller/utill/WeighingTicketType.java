package org.example.ivoprojekt.controller.utill;

public enum WeighingTicketType {
    ACCEPTANCE("Príjemka"),
    EXTRADITION("Výdajka");

    public final String name;
    WeighingTicketType(String name) {
        this.name = name;
    }
}
