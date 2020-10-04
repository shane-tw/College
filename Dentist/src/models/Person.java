package models;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

import java.io.*;

/**
 * Created by Shane on 31/01/2017.
 */
public abstract class Person {
    private StringProperty name = new SimpleStringProperty();
    private StringProperty address = new SimpleStringProperty();
    private StringProperty contactNumber = new SimpleStringProperty();

    public StringProperty nameProperty() {
        return name;
    }

    public String getName() {
        return nameProperty().get();
    }

    public abstract void setName(String name);

    public StringProperty addressProperty() {
        return address;
    }

    public String getAddress() {
        return addressProperty().get();
    }

    public abstract void setAddress(String address);

    public StringProperty contactNumberProperty() {
        return contactNumber;
    }

    public String getContactNumber() {
        return contactNumberProperty().get();
    }

    public abstract void setContactNumber(String contactNumber);

}
