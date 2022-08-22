package knubisoft.readWriteSources;

import knubisoft.TableAnnotation;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

@RequiredArgsConstructor
@Getter
public class ConnectionReadWriteSource implements DataReadWriteSource<ResultSet> {
    private final Connection source;
    private final Class<?> clazz;

    @SneakyThrows
    @Override
    public ResultSet getContent() {
        Statement statement = source.createStatement();
        return statement.executeQuery("SELECT * FROM " + clazz.getAnnotation(TableAnnotation.class).value());
    }
}
