package ca.ubc.cs304.ui.utils;

public class VehicleTypeNames {
    private String economy = "Economy";
    private String compact = "Compact";
    private String midsize = "Mid-Size";
    private String standard = "Standard";
    private String fullsize = "Full-Size";
    private String suv = "SUV";
    private String truck = "Truck";

    public String[] getNames() {
        String[] names = { economy, compact, midsize, standard, fullsize, suv, truck };
        return names;
    }
}
