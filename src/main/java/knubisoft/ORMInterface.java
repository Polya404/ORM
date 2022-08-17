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
    <T> List<T> readAll(DataReadWriteSource<?> inputSource, Class<T> cls);

    @SneakyThrows
    default <T> void writeAll(DataReadWriteSource target, List<T> object){}  //TODO






}

