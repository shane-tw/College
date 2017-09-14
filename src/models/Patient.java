package models;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import managers.InvoiceManager;
import managers.SessionManager;

import java.io.*;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Comparator;
import java.util.List;

/**
 * Created by Shane on 31/01/2017.
 */
public class Patient extends Person { // The patient is a person. It implements externalizable, meaning you can read/write it as a stream.
    //  Using properties allows you to trigger events when the values in them change.
    private static IntegerProperty patientCount = new SimpleIntegerProperty(0);
    private IntegerProperty patientId = new SimpleIntegerProperty();
    private ObjectProperty<InvoiceManager> invoiceManager = new SimpleObjectProperty<>(new InvoiceManager());

    public Patient(){
        setCount(getCount() + 1); // Increment count due to newly created patient.
        setId(getCount()); // Set id of patient to new count.
    }

    public Patient(String name, String address) {
        this();
        setName(name);
        setAddress(address);
    }

    public static IntegerProperty countProperty() {
        return patientCount;
    }

    public static int getCount() {
        return countProperty().get();
    }

    public static void setCount(int patientCount) {
        countProperty().set(patientCount);
    }

    public IntegerProperty idProperty() {
        return patientId;
    }

    public int getId() {
        return idProperty().get();
    }

    public void setId(int patientId) {
        PreparedStatement setIdStatement = null;
        try {
            setIdStatement = SessionManager.getConnection().prepareStatement("UPDATE \"patient\" SET \"id\" = ? WHERE \"id\" = ?");
            setIdStatement.setInt(1, patientId);
            setIdStatement.setInt(2, getId());
            setIdStatement.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            SessionManager.tryCloseSQL(setIdStatement);
        }
        idProperty().set(patientId);
    }

    @Override
    public void setName(String name) {
        PreparedStatement setNameStatement = null;
        try {
            setNameStatement = SessionManager.getConnection().prepareStatement("UPDATE \"patient\" SET \"name\" = ? WHERE \"id\" = ?");
            setNameStatement.setString(1, name);
            setNameStatement.setInt(2, getId());
            setNameStatement.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            SessionManager.tryCloseSQL(setNameStatement);
        }
        nameProperty().set(name);
    }

    @Override
    public void setAddress(String address) {
        PreparedStatement setAddressStatement = null;
        try {
            setAddressStatement = SessionManager.getConnection().prepareStatement("UPDATE \"patient\" SET \"address\" = ? WHERE \"id\" = ?");
            setAddressStatement.setString(1, address);
            setAddressStatement.setInt(2, getId());
            setAddressStatement.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            SessionManager.tryCloseSQL(setAddressStatement);
        }
        addressProperty().set(address);
    }

    @Override
    public void setContactNumber(String contactNumber) {
        PreparedStatement setContactNumberStatement = null;
        try {
            setContactNumberStatement = SessionManager.getConnection().prepareStatement("UPDATE \"patient\" SET \"contactNumber\" = ? WHERE \"id\" = ?");
            setContactNumberStatement.setString(1, contactNumber);
            setContactNumberStatement.setInt(2, getId());
            setContactNumberStatement.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            SessionManager.tryCloseSQL(setContactNumberStatement);
        }
        contactNumberProperty().set(contactNumber);
    }

    public ObjectProperty<InvoiceManager> invoiceManagerProperty() {
        return invoiceManager;
    }

    public InvoiceManager getInvoiceManager() {
        return invoiceManagerProperty().get();
    }

    private void setInvoiceManager(InvoiceManager invoiceManager) {
        invoiceManagerProperty().set(invoiceManager);
    }

    public static int compareByName(Patient p1, Patient p2) {
        return p1.getName().compareToIgnoreCase(p2.getName());
    }

    public static int compareByAmountOwed(Patient p1, Patient p2) {
        Double p1InvoicesAmount = 0.0;
        for (Invoice p1Invoice : p1.getInvoiceManager().getInvoicesUnmodifiable()) {
            p1InvoicesAmount += p1Invoice.getAmount();
        } // Essentially get the sum of all amounts outstanding by a patient.
        Double p2InvoicesAmount = 0.0;
        for (Invoice p2Invoice : p2.getInvoiceManager().getInvoicesUnmodifiable()) {
            p2InvoicesAmount += p2Invoice.getAmount();
        } // Same as above, but for other patient.
        return p1InvoicesAmount.compareTo(p2InvoicesAmount); // Sort patient by amount outstanding in ascending order.
    }

    @Override
    public String toString() {
        return "Patient: {" +
                "id: " + getId() +
                ", name: " + getName() +
                ", address: " + getAddress() +
                ", contactNumber: " + getContactNumber() +
                ", invoices: {" + getInvoiceManager().getInvoicesUnmodifiable() + "}" +
                "}";
    }

    public void print() {
        System.out.println(this);
    }

}
