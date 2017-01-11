package model;

public class LocationVisitor {

    private LocationModel location;
    private Integer visitors;

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

    public String toString() {
        return this.location.getCity() + ", " + this.visitors;
    }
}
