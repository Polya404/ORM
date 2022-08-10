package knubisoft;

import knubisoft.classes.Person;
import knubisoft.classes.Person2;

import java.io.File;
import java.net.URL;
import java.util.List;

public class Main {

    public static void main(String[] args) throws Exception {
        URL url1 = Main.class.getClassLoader().getResource("dataset.xml");
        URL url2 = Main.class.getClassLoader().getResource("MOCK_DATA.json");
        List<Person> result = new ORM().transform(new File(url1.toURI()), Person.class);
    }
}