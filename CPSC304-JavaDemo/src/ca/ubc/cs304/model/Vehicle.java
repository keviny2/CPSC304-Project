package ca.ubc.cs304.model;

/**
 * The intent for this class is to update/store information about a single branch
 */
public class Vehicle {
    private final int vid;
    private final String vlicense;
    private final String make;
    private final String model;
    private final String year;
    private final String color;
    private final int odometer;
    private final int statusID;
    private final String vtname;
    private final String location;
    private final String city;

    public Vehicle(int vid, String vlicense, String make, String model, String year, String color, int odometer, int statusID, String vtname, String location, String city) {
        this.vid = vid;
        this.vlicense = vlicense;
        this.make = make;
        this.model = model;
        this.year = year;
        this.color = color;
        this.odometer = odometer;
        this.statusID = statusID;
        this.vtname = vtname;
        this.location = location;
        this.city = city;
    }

    public int getVid() {
        return vid;
    }

    public String getVlicense() {
        return vlicense;
    }

    public String getMake() {
        return make;
    }

    public String getModel() {
        return model;
    }

    public String getYear() {
        return year;
    }

    public String getColor() {
        return color;
    }

    public int getOdometer() {
        return odometer;
    }

    public int getStatusID() {
        return statusID;
    }

    public String getVtname() {
        return vtname;
    }

    public String getLocation() {
        return location;
    }

    public String getCity() {
        return city;
    }
}

