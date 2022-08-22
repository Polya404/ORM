package knubisoft.writeStrategy;

import knubisoft.ModelSQLHelper;
import knubisoft.TableAnnotation;
import knubisoft.dbConnection.DBService;
import lombok.SneakyThrows;

import java.lang.reflect.Field;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

public class DataBaseWriteStrategy implements WriteStrategy {
    @Override
    public void write(List<?> list) {
        for (Object o : list) {
            DBService dbService = new DBService();
            dbService.withConnection(new Function<>() {
                @SneakyThrows
                @Override
                public Void apply(Connection connection) {
                    ModelSQLHelper helper = new ModelSQLHelper(collectMetaInformation(connection, o));
                    String sql = helper.buildSQL(o);
                    PreparedStatement ps = connection.prepareStatement(sql);
                    helper.bindArguments(o, ps);
                    ps.execute();
                    return null;
                }

                @SneakyThrows
                private List<String> collectMetaInformation(Connection connection,
                                                            Object objectToInsert) {
                    List<String> result = new ArrayList<>();
                    String tableName = objectToInsert.getClass().getAnnotation(TableAnnotation.class).value();
                    String sql = String.format("select * from %s", tableName);
                    PreparedStatement preparedStatement;
                    preparedStatement = connection.prepareStatement(sql);
                    ResultSet resultSet = preparedStatement.executeQuery(sql);
                    ResultSetMetaData resultSetMetaData = resultSet.getMetaData();
                    for (int i = 2; i <= resultSetMetaData.getColumnCount(); i++) {
                        result.add(resultSetMetaData.getColumnName(i));
                    }
                    return result;
                }

                @SneakyThrows
                private void bindArguments(PreparedStatement ps, Object objectToInsert) {
                    int index = 1;
                    for (Field field : objectToInsert.getClass().getDeclaredFields()) {
                        field.setAccessible(true);
                        ps.setObject(index, field.get(objectToInsert));
                        index++;
                    }
                }
            });
        }
    }
}
