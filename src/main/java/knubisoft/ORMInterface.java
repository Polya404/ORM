package knubisoft;

import knubisoft.readWriteSources.DataReadWriteSource;
import lombok.SneakyThrows;

import java.util.List;

public interface ORMInterface {
    @SneakyThrows
    <T> List<T> readAll(DataReadWriteSource<?> inputSource, Class<T> cls);

    @SneakyThrows
    default <T> void writeAll(DataReadWriteSource target, List<T> object){}






}

