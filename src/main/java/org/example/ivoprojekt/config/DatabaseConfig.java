package org.example.ivoprojekt.config;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.flywaydb.core.Flyway;
import org.jdbi.v3.core.Jdbi;

public class DatabaseConfig {

    private static final HikariDataSource dataSource;

    static {
        HikariConfig config = new HikariConfig();
        config.setJdbcUrl("jdbc:sqlite:app.db");
        config.setDriverClassName("org.sqlite.JDBC");
        config.setConnectionInitSql("PRAGMA foreign_keys=ON");
        dataSource = new HikariDataSource(config);

        Flyway flyway = Flyway.configure()
                .dataSource(dataSource)
                .locations("classpath:db/migration")
                .load();
        flyway.migrate();
    }
        public static Jdbi getJdbi() {
            Jdbi jdbi = Jdbi.create(dataSource);
            jdbi.installPlugin(new org.jdbi.v3.sqlobject.SqlObjectPlugin());
            return jdbi;
        }
}
