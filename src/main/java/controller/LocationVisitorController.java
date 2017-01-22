package controller;

import dao.JDBC.LocationVisitorDaoJDBC;
import model.LocationVisitor;

import java.sql.Timestamp;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Bebama
 * @version 1.0
 * @since 1.0
 */
public class LocationVisitorController {

    /**
     * Collect cities with most visitors
     *
     * @param webshop - webshop's ID
     * @return Location details
     */
    public static Map<String, Integer> topLocations(Integer webshop) {
        return converter(new LocationVisitorDaoJDBC().locationsByWebshop(webshop));
    }

    /**
     * Collect cities with most visitors in a given time interval
     *
     * @param webshop - webshop's ID
     * @param startTime - start of time interval
     * @param endTime - end of time interval
     * @return Location details
     */
    public static Map<String, Integer> topLocationsByTime(Integer webshop, Timestamp startTime, Timestamp endTime) {
        return converter(new LocationVisitorDaoJDBC().locationsByWebshopTime(webshop, startTime, endTime));
    }

    private static Map<String, Integer> converter(List<LocationVisitor> locations) {
        Map<String, Integer> locationMap = new HashMap<>();
        for (LocationVisitor location : locations) {
            locationMap.put(location.getLocation().toString(), location.getVisitors());
        }
        return locationMap;
    }
}
