package dao.JDBC;

import dao.LocationVisitorDao;
import model.LocationVisitor;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Bebama
 * @version 1.0
 * @since 1.0
 */
public class LocationVisitorDaoJDBC extends AnalyticsDaoJDBC implements LocationVisitorDao{

    /**
     * Find top locations for given webshop
     *
     * @param webshop - webshop's ID
     * @return List of locations and their number of visitors
     */
    public List<LocationVisitor> locationsByWebshop(int webshop) {
        return getLocationList("SELECT count(*) AS totalVisitors, location FROM webshopAnalytics " +
                "WHERE webshop_id ='" + webshop + "' GROUP BY location ORDER BY totalVisitors LIMIT 10;");
    }

    /**
     * Find top locations for given webshop in given time interval
     *
     * @param webshop - webshop's ID
     * @param start - start of time interval
     * @param end - end of time interval
     * @return List of locations and their number of visitors
     */
    public List<LocationVisitor> locationsByWebshopTime(int webshop, Timestamp start, Timestamp end) {
        return getLocationList("SELECT count(*) AS totalVisitors, location FROM webshopAnalytics " +
                " WHERE webshop_id ='" + webshop +
                "' AND visit_start >='" + start +
                "' AND visit_end <='" + end + "' GROUP BY location ORDER BY totalVisitors LIMIT 10;");
    }

    private List<LocationVisitor> getLocationList(String query) {
        List<LocationVisitor> result = new ArrayList<>();
        try (Connection connection = getConnection();
             Statement statement = connection.createStatement();
             ResultSet rs = statement.executeQuery(query);
        ) {
            while (rs.next()) {
                LocationVisitor location = new LocationVisitor(stringToLocation(rs.getString("location")),
                        rs.getInt("totalVisitors"));
                result.add(location);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return result;
    }
}
