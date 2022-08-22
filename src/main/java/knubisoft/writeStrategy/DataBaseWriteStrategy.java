package knubisoft.writeStrategy;

import knubisoft.ModelSQLHelper;
import knubisoft.dbConnection.DBService;
import lombok.SneakyThrows;

import java.sql.*;
import java.util.List;
import java.util.function.Function;

public class DataBaseWriteStrategy implements WriteStrategy {
    @Override
    public void write(List<?> list) {
        DBService dbService = new DBService();
        dbService.withConnection(new Function<>() {
            @SneakyThrows
            @Override
            public Void apply(Connection connection) {
                for (Object o : list) {
                    ModelSQLHelper helper = new ModelSQLHelper(ModelSQLHelper.collectMetaInformation(connection, o));
                    String sql = helper.buildSQL(o);
                    PreparedStatement ps = connection.prepareStatement(sql);
                    helper.bindArguments(o, ps);
                    ps.execute();
                }
                return null;
            }
        });
    }
}
