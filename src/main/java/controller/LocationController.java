package controller;

import model.LocationModel;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import spark.Request;
import spark.Response;

/**
 * @author Bebama
 * @version 1.0
 * @since 1.0
 */
public class LocationController {

    /**
     * Extract location details
     *
     * @param req - incoming request
     * @param res - server response
     * @return Response. Cause why not
     * @throws ParseException
     */
    public static Response getData(Request req, Response res) throws ParseException {
        JSONObject jsonLocation = (JSONObject) new JSONParser().parse(req.queryParams().iterator().next());
        new LocationModel(jsonLocation.get("city").toString(),
                jsonLocation.get("country").toString(), jsonLocation.get("countryCode").toString());
        return res;
    }

}
