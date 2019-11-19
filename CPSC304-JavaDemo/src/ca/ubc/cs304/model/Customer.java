package ca.ubc.cs304.model;

/**
 * The intent for this class is to update/store information about a single branch
 */
public class Customer {
    private final int dlicense;
    private final int cellphone;
    private final String name;
    private final String address;

    public Customer(int dlicense, int cellphone, String name, String address) {
        this.dlicense = dlicense;
        this.cellphone = cellphone;
        this.name = name;
        this.address = address;
    }

    public int getDlicense() {
        return dlicense;
    }

    public int getCellPhone() {
        return cellphone;
    }

    public String getName() {
        return name;
    }

    public String getAddress() {
        return address;
    }
}
