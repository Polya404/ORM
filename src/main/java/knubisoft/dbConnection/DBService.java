package knubisoft.dbConnection;

import lombok.SneakyThrows;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.function.Function;

public class DBService {
    public Connection connection() throws SQLException {
        return DriverManager
                .getConnection("jdbc:mysql://localhost/person?allowPublicKeyRetrieval=true&useSSL=false"
                        + "&user=root&password=rootroot&serverTimezone=UTC");
    }

    @SneakyThrows
    public void withConnection(Function<Connection,Void> function){
        try(Connection c = connection()) {
            try(Statement statement = c.createStatement()) {
                statement.executeUpdate("CREATE TABLE IF NOT EXISTS person " +
                        "(id INTEGER not NULL, " +
                        " name VARCHAR(255), " +
                        " position VARCHAR(255), " +
                        " age INTEGER, " +
                        " PRIMARY KEY ( id ))");

                statement.executeUpdate("DELETE FROM person");
                for (int index = 0; index < 10; index++) {
                    statement.executeUpdate("INSERT INTO person (name, position, age) VALUES ('1', '1', 1)");
                }
            }
            function.apply(c);
        }
    }
}
