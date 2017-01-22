package controller;

import dao.JDBC.AnalyticsDaoJDBC;
import model.Analytics;

import java.sql.Timestamp;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.IntStream;

/**
 * @author Bebama
 * @version 1.0
 * @since 1.0
 */
public class VisitTimeController {

    /**
     * Calculate average visit time
     *
     * @param webshop - webshop's ID
     * @return visit time details
     */
    public static Map<String, String> averageVisitTime(Integer webshop) {
        return countAverage(new AnalyticsDaoJDBC().findByWebshop(webshop));
    }

    /**
     * Calculate average visit time
     *
     * @param webshop - webshop's ID
     * @param startTime - start of time interval
     * @param endTime - end of time interval
     * @return visit time details
     */
    public static Map<String, String> averageVisitTimeByTime(Integer webshop, Timestamp startTime, Timestamp endTime) {
        return countAverage(new AnalyticsDaoJDBC().findByWebshopTime(webshop, startTime, endTime));
    }

    private static Map<String, String> countAverage(List<Analytics> visits) {
        Map<String, String> statistics = new HashMap<String, String>(){{
            put("average", "00:00:00");
            put("min", "00:00:00");
            put("max", "00:00:00");
        }};
        if (visits.size() > 0) {
            statistics.put("average", intToString(getStream(visits).sum() / visits.size()));
            statistics.put("min", intToString(getStream(visits).min().getAsInt()));
            statistics.put("max", intToString(getStream(visits).max().getAsInt()));
        }
        return statistics;
    }

    private static IntStream getStream(List<Analytics> visits){
        return visits.stream().map(Analytics::secondsSpent).mapToInt(Integer::intValue);
    }

    private static String intToString(Integer duration) {
        Integer hours = duration / 3600;
        Integer minutes = (duration % 3600) / 60;
        Integer seconds = duration % 60;
        return String.format("%02d:%02d:%02d", hours, minutes, seconds);
    }
}
