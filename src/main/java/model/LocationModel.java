package model;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Bebama
 * @version 1.0
 * @since 1.0
 */
public class LocationModel {

    private String city;
    private String country;
    private String countryCode;
    private static List<LocationModel> allLocations = new ArrayList<>();

    /**
     * @param city
     * @param country
     * @param countryCode
     */
    public LocationModel(String city, String country, String countryCode) {
        this.city = city;
        this.country = country;
        this.countryCode = countryCode;
        addLocation(this);
    }

    public String getCity() {
        return city;
    }

    public String getCountry() {
        return country;
    }

    public String getCountryCode() {
        return countryCode;
    }

    public static List<LocationModel> getAllLocations() {
        return allLocations;
    }

    private void addLocation(LocationModel location) {
        allLocations.add(location);
    }

    @Override
    public String toString(){
        return String.format("%s, %s, %s", getCity(), getCountry(), getCountryCode());
    }

}
