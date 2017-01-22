package controller;


import dao.JDBC.AnalyticsDaoJDBC;

import java.sql.Timestamp;

/**
 * @author Bebama
 * @version 1.0
 * @since 1.0
 */
public class VisitorCountController {

    /**
     * Count total visitors
     *
     * @param webshop - webshop's ID
     * @return number of visitors
     */
    public static int totalVisitors(Integer webshop) {
         return new AnalyticsDaoJDBC().findByWebshop(webshop).size();
    }

    /**
     * Count visitors in given time interval
     *
     * @param webshop - webshop's ID
     * @param startTime - start of time interval
     * @param endTime - end of time interval
     * @return number of visitors
     */
    public static int visitorsByTime(Integer webshop, Timestamp startTime, Timestamp endTime){
        return new AnalyticsDaoJDBC().findByWebshopTime(webshop, startTime, endTime).size();
    }
}
