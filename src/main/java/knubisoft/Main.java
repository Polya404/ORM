package knubisoft;

import knubisoft.classes.Person;
import knubisoft.dbConnection.DBService;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.math.BigInteger;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.List;

public class Main {

    public static void main(String[] args) throws Exception {
        URL url1 = Main.class.getClassLoader().getResource("dataset.xml");
        URL url2 = Main.class.getClassLoader().getResource("MOCK_DATA.json");

        ORMInterface _interface = (ORMInterface) new ORM();

        List<Person> result = _interface.readAll(new ORMInterface.FileReadWriteSource(new File(url1.toURI())), Person.class);
        result.add(new Person("Ilya Voronin", BigInteger.valueOf(23L), BigInteger.valueOf(1999L), "Manager", LocalDate.now(), 0F));
        _interface.writeAll(new ORMInterface.FileReadWriteSource(new File(url1.toURI())), result);

        DBService dbService = new DBService();

        result = _interface.readAll(new ORMInterface.ConnectionReadWriteSource(dbService.connection(), "students"), Person.class);
        result.add(new Person("Ilya Voronin", BigInteger.valueOf(23L), BigInteger.valueOf(1999L), "Manager", LocalDate.now(), 0F));
        _interface.writeAll(new ORMInterface.ConnectionReadWriteSource(dbService.connection(), "person"), result);


    }
}