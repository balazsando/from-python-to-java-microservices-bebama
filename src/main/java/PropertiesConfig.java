import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Properties;

/**
 * @author Bebama
 * @version 1.0
 * @since 1.0
 */
public class PropertiesConfig {

    /**
     * Create config file for DB connection
     */
    public static void config() {

        Properties prop = new Properties();
        OutputStream output = null;

        try {

            output = new FileOutputStream("./src/main/resources/test_connection.properties");

            // set the properties value
            prop.setProperty("DBURL", "jdbc:postgresql://localhost:5432/testwebshopAnalytics");
            prop.setProperty("DB_USER", "postgres");
            prop.setProperty("DB_PASSWORD", "postgres");

            // save properties to project root folder
            prop.store(output, null);

        } catch (IOException io) {
            io.printStackTrace();
        } finally {
            if (output != null) {
                try {
                    output.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

        }
    }

}
