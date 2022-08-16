package knubisoft;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;

import java.io.File;
import java.sql.Connection;
import java.sql.ResultSet;
import java.util.List;

public interface ORMInterface {
    @SneakyThrows
    <T> List<T> readAll(DataReadWriteSource content, Class<T> cls);  //TODO

    @SneakyThrows
    default <T> void writeAll(DataReadWriteSource content, List<T> object){}  //TODO

    interface DataReadWriteSource<T> {
        T getContent();
    }

    @RequiredArgsConstructor
    @Getter
    final class FileReadWriteSource implements DataReadWriteSource<String> {
        private final File source;

        @Override
        public String getContent() {
            return null;
        }
    }

    @RequiredArgsConstructor
    @Getter
    final class ConnectionReadWriteSource implements DataReadWriteSource<ResultSet> {
        private final Connection source;
        private final String table;

        @Override
        public ResultSet getContent() {
            return null;
        }
    }

}

