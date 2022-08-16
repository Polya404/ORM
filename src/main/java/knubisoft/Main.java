package knubisoft;

import knubisoft.classes.Person;
import knubisoft.classes.Person2;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.List;

public class Main {

    public static void main(String[] args) throws Exception {
        URL url1 = Main.class.getClassLoader().getResource("dataset.xml");
        URL url2 = Main.class.getClassLoader().getResource("MOCK_DATA.json");
        String content = FileUtils.readFileToString(new File(url1.toURI()), StandardCharsets.UTF_8);

        ORMInterface _interface = (ORMInterface) new ORM();
        List<Person> result = _interface.transform(new ORMInterface.StringInputSource(content), Person.class);
    }
}