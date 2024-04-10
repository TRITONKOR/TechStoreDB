package com.tritonkor.persistence.util;

import com.tritonkor.persistence.ConnectionPool;
import com.tritonkor.persistence.exception.persistence.PersistenceException;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.function.Supplier;
import java.util.stream.Collectors;

public final class DbInitialization {

    private DbInitialization() {}

    static private final String DDL_PATH = "ddl.sql";
    static private final String DML_PATH = "dml.sql";

    /** Read ddl.sql and dml.sql from resources and apply it to database. */
    public static void apply() {
        getSqlFromFile(DDL_PATH);
        try (Connection connection = ConnectionPool.get();
                Statement statementForDDL = connection.createStatement();
                Statement statementForDML = connection.createStatement()) {
            statementForDDL.execute(getSqlFromFile(DDL_PATH));
            statementForDML.execute(getSqlFromFile(DML_PATH));
        } catch (SQLException e) {
            throw new PersistenceException("Помилка, під час створення та заповнення бази даних. %s".formatted(e.getMessage()));
        }
    }

    private static String getSqlFromFile(final String resourceName) {
        Supplier<URI> uriSupplier =
                () -> {
                    URL url = ConnectionPool.class.getClassLoader().getResource(resourceName);
                    if (url == null) {
                        throw new NullPointerException("Відсутній SQL файл");
                    }
                    try {
                        return url.toURI();
                    } catch (URISyntaxException e) {
                        throw new PersistenceException(
                                "Помилка формування URI посилання до ресурсного файлу.");
                    }
                };

        try (var stream = Files.lines(Path.of(uriSupplier.get()), StandardCharsets.UTF_8)) {
            return stream.collect(Collectors.joining("\n"));
        } catch (IOException e) {
            throw new PersistenceException("Помилка, під час зчитування DLL чи DML файлу.");
        }
    }
}
