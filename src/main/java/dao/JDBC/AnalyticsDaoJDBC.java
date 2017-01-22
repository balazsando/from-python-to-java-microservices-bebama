package dao.JDBC;

import dao.AnalyticsDao;
import model.Analytics;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Bebama
 * @version 1.0
 * @since 1.0
 */
public class AnalyticsDaoJDBC extends AbstractDaoJDBC implements AnalyticsDao{

    /**
     * Insert/Update Analytics details in DB
     *
     * @param model - Analytics instance to add/update
     */
    public void add(Analytics model) {
        try (Connection connection = AbstractDaoJDBC.getConnection()) {
            PreparedStatement query;
            if (findSessionId(model.getSessionId()).size() > 0) {
                query = connection.prepareStatement("UPDATE webshopAnalytics SET visit_end = ?, amount = ?, currency  = ? WHERE session_id = ?");
                query.setTimestamp(1, model.getEndTime());
                query.setFloat(2, model.getAmount());
                query.setString(3, String.valueOf(model.getCurrency()));
                query.setString(4, model.getSessionId());
            } else {
                query = connection.prepareStatement("INSERT INTO webshopAnalytics (webshop_id," +
                        " session_id, visit_start, visit_end, location, amount, currency) VALUES (?, ?, ?, ?, ?, ?, ?);");
                query.setInt(1, model.getWebshopId());
                query.setString(2, model.getSessionId());
                query.setTimestamp(3, model.getStartTime());
                query.setTimestamp(4, model.getEndTime());
                query.setString(5, model.getLocation().toString());
                query.setFloat(6, model.getAmount());
                query.setString(7, String.valueOf(model.getCurrency()));
            }
            query.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Load Analytics details from DB for given webshop
     *
     * @param webshop - webshop's ID
     * @return List of Analytics objects
     */
    public List<Analytics> findByWebshop(int webshop) {
        return getAnalyticsList("SELECT * FROM webshopAnalytics WHERE webshop_id ='" + webshop + "';");
    }

    /**
     * Load Analytics details from DB for given webshop in time interval
     *
     * @param webshop - webshop's ID
     * @param start - start of time interval
     * @param end - end of time interval
     * @return List of Analytics objects
     */
    public List<Analytics> findByWebshopTime(int webshop, Timestamp start, Timestamp end) {
        return getAnalyticsList("SELECT * FROM webshopAnalytics" +
                " WHERE webshop_id ='" + webshop +
                "' AND visit_start >='" + start +
                "' AND visit_end <='" + end + "';");
    }


    private List<Analytics> getAnalyticsList(String query) {
        List<Analytics> result = new ArrayList<>();
        try (Connection connection = getConnection();
             Statement statement = connection.createStatement();
             ResultSet rs = statement.executeQuery(query);
        ) {
            while (rs.next()) {
                Analytics analytics = new Analytics(rs.getInt("webshop_id"),
                        rs.getString("session_id"),
                        rs.getTimestamp("visit_start"),
                        rs.getTimestamp("visit_end"),
                        stringToLocation(rs.getString("location")),
                        rs.getFloat("amount"),
                        rs.getString("currency"));
                analytics.setId(rs.getInt("an_id"));
                result.add(analytics);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return result;
    }

    /**
     * Find Analytics details in DB by session ID
     *
     * @param sessionId
     * @return Analytics object with matching session ID
     */
    public List<Analytics> findSessionId(String sessionId) {
        return getAnalyticsList("SELECT * FROM webshopAnalytics WHERE session_id ='" + sessionId + "';");
    }

}
