package knubisoft.dbConnection;

import lombok.SneakyThrows;

import java.sql.*;
import java.util.function.Function;

public class DBService {
    public Connection connection() throws SQLException {
        return DriverManager
                .getConnection("jdbc:mysql://localhost/person?allowPublicKeyRetrieval=true&useSSL=false"
                        + "&user=root&password=rootroot&serverTimezone=UTC");
    }

    @SneakyThrows
    public void withConnection(Function<Connection, Void> function) {
        try (Connection с = connection()) {
            try (Statement stmt = с.createStatement()) {
                stmt.executeUpdate("CREATE TABLE IF NOT EXISTS person " +
                        "(id SERIAL PRIMARY KEY, " +
                        " name VARCHAR(255), " +
                        " position VARCHAR(255), " +
                        " age INTEGER)");

                stmt.executeUpdate("DELETE FROM person");
            }
            function.apply(с);
        }
    }

}
