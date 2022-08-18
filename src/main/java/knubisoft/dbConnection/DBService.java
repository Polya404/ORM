package knubisoft.dbConnection;

import knubisoft.classes.Person;
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
    public int addPerson(Person person){
        Connection connection = connection();
        Statement statement = connection.createStatement();
        String SQL = "insert into  persons (name, position, age) value  ('%s', '%s', '%s')";
        return statement.executeUpdate(String.format(SQL, person.getName(), person.getPosition(), person.getAge()));
    }

}
