package ru.clevertec.repository;

import org.apache.commons.dbcp2.BasicDataSource;
import org.yaml.snakeyaml.Yaml;

import java.io.InputStream;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Map;

public class ConnectionPoolManager {

    private static BasicDataSource dataSource;
    public static final String URL = "url";
    public static final String USER_NAME = "username";
    public static final String PASSWORD = "password";
    public static final String DRIVER = "driver";
    public static final String CONFIG_DB = "configDB.yaml";

    private static Map<String, String> readConfigDBYaml() {

        Yaml yaml = new Yaml();
        InputStream inputStream = ConnectionPoolManager.class.getClassLoader()
                .getResourceAsStream(CONFIG_DB);
        Map<String, String> str = yaml.load(inputStream);

        return str;
    }

    private static Map<String, String> stringMap = readConfigDBYaml();

    static {
        dataSource = new BasicDataSource();
        dataSource.setUrl(stringMap.get(URL));
        dataSource.setUsername(stringMap.get(USER_NAME));
        dataSource.setPassword(stringMap.get(PASSWORD));
        dataSource.setDriverClassName(stringMap.get(DRIVER));
//        dataSource.setMaxTotal(Integer.parseInt(stringMap.get("maxTotalConnections")));
//        dataSource.setMaxIdle(Integer.parseInt(stringMap.get("maxIdleConnections")));
        dataSource.setDefaultAutoCommit(false);
    }

    public static Connection getConnection() throws SQLException {
        return dataSource.getConnection();
    }

    public static void releaseConnection(Connection connection) throws SQLException {
        if (connection != null) {
            connection.close();
        }
    }
}
