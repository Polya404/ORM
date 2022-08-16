package knubisoft.dbConnection;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBService {
    public Connection connection() throws SQLException {
        return DriverManager
                .getConnection("jdbc:mysql://localhost/student?allowPublicKeyRetrieval=true&useSSL=false"
                        + "&user=root&password=rootroot&serverTimezone=UTC");
    }
}
