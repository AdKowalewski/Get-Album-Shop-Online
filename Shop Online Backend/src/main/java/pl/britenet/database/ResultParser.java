package pl.britenet.database;

import java.sql.ResultSet;

public interface ResultParser<T> {
    T parse(ResultSet resultSet);
}