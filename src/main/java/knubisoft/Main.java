package knubisoft;

import knubisoft.model.Person;
import knubisoft.dbConnection.DBService;
import knubisoft.readWriteSources.ConnectionReadWriteSource;
import knubisoft.readWriteSources.DataReadWriteSource;
import knubisoft.readWriteSources.FileReadWriteSource;
import lombok.SneakyThrows;

import java.io.File;
import java.net.URL;
import java.sql.Connection;
import java.sql.ResultSet;
import java.util.List;

public class Main {
    private static final ORMInterface ORM = new ORM();

    public static void main(String[] args) throws Exception {
        URL url1 = Main.class.getClassLoader().getResource("TargetFile.json");
        URL url2 = Main.class.getClassLoader().getResource("TargetFile.xml");
        URL url3 = Main.class.getClassLoader().getResource("TargetFile.csv");

        URL url4 = Main.class.getClassLoader().getResource("dataset.xml");


        DataReadWriteSource<?> file = new FileReadWriteSource(new File(url4.toURI()));
        List<Person> list = ORM.readAll(file,Person.class);

        DataReadWriteSource<?> target1 = new FileReadWriteSource(new File(url3.toURI()));
        DataReadWriteSource<?> target2 = new FileReadWriteSource(new File(url2.toURI()));
        DataReadWriteSource<?> target3 = new FileReadWriteSource(new File(url1.toURI()));
        if (!list.isEmpty()){
        ORM.writeAll(target1, list);
        ORM.writeAll(target2, list);
        ORM.writeAll(target3, list);
        }

        DBService dbService = new DBService();
        process(dbService.connection());

    }

    @SneakyThrows
    public static void process(Connection connection) {
        URL url5 = Main.class.getClassLoader().getResource("MOCK_DATA.json");

        List<Person> result;
        DataReadWriteSource<ResultSet> rw = new ConnectionReadWriteSource(connection, Person.class);
        result = ORM.readAll(rw, Person.class);

        DataReadWriteSource<?> file = new FileReadWriteSource(new File(url5.toURI()));
        List<Person> list = ORM.readAll(file,Person.class);

        ORM.writeAll(rw, list);

    }
}