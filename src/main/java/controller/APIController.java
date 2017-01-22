package controller;

import dao.JDBC.AnalyticsDaoJDBC;
import model.Analytics;
import model.LocationModel;
import org.json.simple.JSONObject;
import spark.ModelAndView;
import spark.Request;
import spark.Response;

import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @author Bebama
 * @version 1.0
 * @since 1.0
 */
public class APIController {

    private String sessionId;
    private int webShopId = 1;
    private Timestamp startTime;
    private Timestamp endTime;
    private Date start;
    private Date stop;
    private DateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    private Map<String, Integer> topLocations;

    /**
     * @return the webshop ID
     */
    public int getWebShopId() {
        return webShopId;
    }

    /**
     * @return the session ID
     */
    public String getSessionId() {
        return sessionId;
    }

    /**
     * Render the homepage template
     *
     * @param req - incoming request
     * @param res - server response
     * @return the homepage
     */
    public ModelAndView renderMain(Request req, Response res) {
        Map<Object, Object> params = new HashMap<>();
        startSession(req, res);
        return new ModelAndView(params, "time_location");
    }

    private void getTimes(Request req, Response res) throws ParseException {
        start = customDateParser(req.queryParams("startTime"));
        stop = customDateParser(req.queryParams("endTime"));
    }

    /**
     * Set the endTime property
     *
     * @param req - incoming request
     * @param res - server response
     * @return I have no idea
     */
    public String stopSession(Request req, Response res) {
        String time = req.queryParams("time");
        Date date = new Date(Long.parseLong(time));
        this.endTime = convertToTimeStamp(date);
        analytics(req, res);
        return "";
    }

    /**
     * Set the startTime property
     *
     * @param req - incoming request
     * @param res - server response
     * @return Still no clue
     */
    public String startSession(Request req, Response res) {
        sessionId = req.session().id();
        Date date = new Date();
        this.startTime = convertToTimeStamp(date);
        return "";
    }

    /**
     * Insert new Analytics record in the database
     *
     * @param req - incoming request
     * @param res - server response
     */
    public void analytics(Request req, Response res) {
        LocationModel location = LocationModel.getAllLocations().get(0);
        float amount = 10;
        Currency currency = Currency.getInstance(Locale.US);
        Analytics model = new Analytics(getWebShopId(), getSessionId(), this.startTime, this.endTime, location, amount, String.valueOf(currency));
        try {
            new AnalyticsDaoJDBC().add(model);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Create a JSON of basic analytic details
     *
     * @param req - incoming request
     * @param res - server response
     * @return JSON string of webshop analytics
     * @throws ParseException
     */
    public String api(Request req, Response res) throws ParseException {
        webShopId = Integer.parseInt(req.queryParams("webshopId"));
        topLocations = LocationVisitorController.topLocations(Integer.parseInt(req.queryParams("webshopId")));
        int highestVisitorCount = Collections.max(topLocations.values());
        String topLocation = topLocations.entrySet()
                .stream()
                .filter(entry -> Objects.equals(entry.getValue(), highestVisitorCount))
                .map(Map.Entry::getKey).findFirst().orElse(null);
        Map<String, Object> analytic = new HashMap<>();
        analytic.put("visitors", VisitorCountController.totalVisitors(webShopId));
        analytic.put("average_visit_time", VisitTimeController.averageVisitTime(webShopId));
        analytic.put("most_visited_from", topLocation);
        analytic.put("revenue", RevenueController.totalRevenue(webShopId));
        return convertMapToJSONString(analytic);
    }

    /**
     * Create JSON of visitor counter details
     *
     * @param req - incoming request
     * @param res - server response
     * @return JSON string of webshop visitor analytics
     * @throws ParseException
     */
    public String visitorCounter(Request req, Response res) throws ParseException {
        webShopId = Integer.parseInt(req.queryParams("webshopId"));
        sessionId = req.queryParams("sessionId");
        Map<String, Integer> counter = new HashMap<>();
        if (req.queryParams().size() == 3) {
            getTimes(req, res);
            counter.put("visitors", VisitorCountController.visitorsByTime(webShopId, convertToTimeStamp(start), convertToTimeStamp(stop)));
        } else {
            counter.put("visitors", VisitorCountController.totalVisitors(webShopId));
        }
        return convertMapToJSONString(counter);
    }

    /**
     * Create JSON of visit time details
     *
     * @param req - incoming request
     * @param res - server response
     * @return JSON string of webshop visit time analytics
     * @throws ParseException
     */
    public String visitTimeCounter(Request req, Response res) throws ParseException {
        webShopId = Integer.parseInt(req.queryParams("webshopId"));
        sessionId = req.queryParams("sessionId");
        if (req.queryParams().size() == 3) {
            getTimes(req, res);
            return convertMapToJSONString(VisitTimeController.averageVisitTimeByTime(webShopId, convertToTimeStamp(start), convertToTimeStamp(stop)));
        } else {
            return convertMapToJSONString(VisitTimeController.averageVisitTime(webShopId));
        }
    }

    /**
     * Create JSON of location details
     *
     * @param req - incoming request
     * @param res - server response
     * @return JSON string of webshop location analytics
     * @throws ParseException
     */
    public String locationVisits(Request req, Response res) throws ParseException {
        sessionId = req.queryParams("sessionId");
        webShopId = Integer.parseInt(req.queryParams("webshopId"));
        if (req.queryParams().size() == 3) {
            getTimes(req, res);
            return convertMapToJSONString(LocationVisitorController.topLocationsByTime(webShopId, convertToTimeStamp(start), convertToTimeStamp(stop)));
        } else return convertMapToJSONString(LocationVisitorController.topLocations(webShopId));
    }

    /**
     * Create JSON of revenue details
     *
     * @param req - incoming request
     * @param res - server response
     * @return JSON string of webshop revenue analytics
     * @throws ParseException
     */
    public String countRevenue(Request req, Response res) throws ParseException {
        sessionId = req.queryParams("sessionId");
        webShopId = Integer.parseInt(req.queryParams("webshopId"));
        Map<String, Float> revenue = new HashMap<>();
        if (req.queryParams().size() == 3) {
            getTimes(req, res);
            revenue.put("revenue", RevenueController.revenueByTime(webShopId, convertToTimeStamp(start), convertToTimeStamp(stop)));
        } else {
            revenue.put("revenue", RevenueController.totalRevenue(webShopId));
        }
        return convertMapToJSONString(revenue);
    }

    private Date customDateParser(String inputDate) throws ParseException {
        try {
            SimpleDateFormat inputFormat = new SimpleDateFormat("yyyyMMddHHmmss");
            Date tempDate = inputFormat.parse(inputDate);
            String formattedDate = format.format(tempDate);
            return format.parse(formattedDate);
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }
    }

    private Timestamp convertToTimeStamp(Date date) {
        if (date != null) {
            Calendar cal = Calendar.getInstance();
            cal.setTime(date);
            cal.set(Calendar.MILLISECOND, 0);
            return new java.sql.Timestamp(date.getTime());
        } else {
            return null;
        }
    }

    private String convertMapToJSONString(Map map){
        return (new JSONObject(){{putAll(map);}}).toString();
    }

}