package dao.JDBC;

import model.LocationModel;

import java.io.FileInputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

/**
 * @author Bebama
 * @version 1.0
 * @since 1.0
 */
public abstract class AbstractDaoJDBC {

    private static String DBURL;
    private static String DB_USER;
    private static String DB_PASSWORD;

    /**
     * Sets up connection with DB
     *
     * @return communication channel with DB
     * @throws SQLException
     */
    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(DBURL, DB_USER, DB_PASSWORD);
    }

    /**
     * Set up connection details
     *
     * @param config - configuration file name
     * @throws IOException
     */
    public static void setConnection(String config) throws IOException {
        Properties pro = new Properties();
        FileInputStream in = new FileInputStream("./src/main/resources/" + config);
        pro.load(in);

        // getting values from property file
        DBURL = pro.getProperty("DBURL");
        DB_USER = pro.getProperty("DB_USER");
        DB_PASSWORD = pro.getProperty("DB_PASSWORD");
    }

    /**
     * convert string to LocationModel object
     *
     * @param location - string format of LocationModel
     * @return generated LocationModel instance
     */
    protected static LocationModel stringToLocation(String location) {
        String[] details = location.split(",");
        return new LocationModel(details[0],
                details[1].substring(1),
                details[2].substring(1));
    }

}
