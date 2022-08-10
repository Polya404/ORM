package knubisoft;

import knubisoft.classes.Person2;

import java.io.File;
import java.net.URL;
import java.util.List;

public class Main {

    public static void main(String[] args) throws Exception {
        URL url = Main.class.getClassLoader().getResource("MOCK_DATA.json");
        List<Person2> result = new ORM().transform(new File(url.toURI()), Person2.class);
    }
}