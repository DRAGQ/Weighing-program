module org.example.ivoprojekt {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires flyway.core;
    requires com.zaxxer.hikari;
    requires org.jdbi.v3.core;
    requires org.xerial.sqlitejdbc;
    requires org.jdbi.v3.sqlobject;
    requires java.desktop;
    requires java.prefs;
    requires javafx.graphics;
    requires javafx.base;
    requires com.github.librepdf.openpdf;
    requires org.slf4j;

    opens org.example.ivoprojekt to javafx.fxml;
    opens org.example.ivoprojekt.javaFXUtill to javafx.fxml;
    opens db.migration;
    exports org.example.ivoprojekt.domain;
    exports org.example.ivoprojekt;
    exports org.example.ivoprojekt.controller;
    exports org.example.ivoprojekt.controller.dials;
    exports org.example.ivoprojekt.controller.weighing;
    exports org.example.ivoprojekt.api.request;
    exports org.example.ivoprojekt.api.response;
    exports org.example.ivoprojekt.repository;
    exports org.example.ivoprojekt.service;
    opens org.example.ivoprojekt.domain to org.jdbi.v3.core;
    opens org.example.ivoprojekt.controller to javafx.fxml;
    opens org.example.ivoprojekt.controller.dials to javafx.fxml;
    opens org.example.ivoprojekt.controller.weighing to javafx.fxml;
    opens org.example.ivoprojekt.api.response to javafx.base;
    exports org.example.ivoprojekt.controller.utill;
    opens org.example.ivoprojekt.controller.utill to javafx.fxml;
    exports org.example.ivoprojekt.controller.user;
    opens org.example.ivoprojekt.controller.user to javafx.fxml;
}