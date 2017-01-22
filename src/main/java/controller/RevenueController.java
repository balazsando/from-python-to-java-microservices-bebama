package controller;

import dao.JDBC.AnalyticsDaoJDBC;
import model.Analytics;

import java.sql.Timestamp;
import java.util.List;

/**
 * @author Bebama
 * @version 1.0
 * @since 1.0
 */
public class RevenueController {

    /**
     * Calculate total revenue amount
     *
     * @param webshop - webshop's ID
     * @return revenue details
     */
    public static Float totalRevenue(Integer webshop) {
        return countRevenue(new AnalyticsDaoJDBC().findByWebshop(webshop));
    }

    /**
     * Calculate revenue in given time interval
     *
     * @param webshop - webshop's ID
     * @param startTime - start of time interval
     * @param endTime - end of time interval
     * @return revenue details
     */
    public static Float revenueByTime(Integer webshop, Timestamp startTime, Timestamp endTime){
        return countRevenue(new AnalyticsDaoJDBC().findByWebshopTime(webshop, startTime, endTime));
    }

    private static Float countRevenue(List<Analytics> purchases){
        Double avenue = purchases.stream().map(Analytics::getAmount).mapToDouble(Float::floatValue).sum();
        return avenue.floatValue();
    }
}
