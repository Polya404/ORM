package knubisoft.writeStrategy;

import knubisoft.classes.Person;
import knubisoft.dbConnection.DBService;

import java.util.List;

public class DataBaseWriteStrategy implements WriteStrategy{
    @Override
    public void write(List<?> list) {
        DBService dbService = new DBService();
        if(list.get(0) instanceof Person) {
            for (Object o : list) {
                dbService.addPerson((Person) o);
            }
        }
    }
}
