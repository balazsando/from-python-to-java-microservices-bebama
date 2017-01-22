package model;

/**
 * @author Bebama
 * @version 1.0
 * @since 1.0
 */
public class LocationVisitor {

    private LocationModel location;
    private Integer visitors;

    /**
     * @param location
     * @param visitors
     */
    public LocationVisitor(LocationModel location, Integer visitors) {
        this.location = location;
        this.visitors = visitors;
    }

    public LocationModel getLocation() {
        return location;
    }

    public void setLocation(LocationModel location) {
        this.location = location;
    }

    public Integer getVisitors() {
        return visitors;
    }

    public void setVisitors(Integer visitors) {
        this.visitors = visitors;
    }
}
