package models;

import javafx.beans.property.*;
import managers.PatientManager;
import managers.SessionManager;

import java.io.*;
import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 * Created by Shane on 31/01/2017.
 */
public class Dentist extends Person {
    private static IntegerProperty dentistCount = new SimpleIntegerProperty(0);
    private IntegerProperty dentistId = new SimpleIntegerProperty();
    private StringProperty password = new SimpleStringProperty();
    private ObjectProperty<PatientManager> patientManager = new SimpleObjectProperty<>(new PatientManager());

    public Dentist() {
        setCount(getCount() + 1);
        setId(getCount());
    }

    public static IntegerProperty countProperty() {
        return dentistCount;
    }

    public static int getCount() {
        return countProperty().get();
    }

    public static void setCount(int dentistCount) {
        countProperty().set(dentistCount);
    }

    public IntegerProperty idProperty() {
        return dentistId;
    }

    public int getId() {
        return idProperty().get();
    }

    public void setId(int dentistId) {
        PreparedStatement setIdStatement = null;
        try {
            setIdStatement = SessionManager.getConnection().prepareStatement("UPDATE \"dentist\" SET \"id\" = ? WHERE \"id\" = ?");
            setIdStatement.setInt(1, dentistId);
            setIdStatement.setInt(2, getId());
            setIdStatement.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            SessionManager.tryCloseSQL(setIdStatement);
        }
        idProperty().set(dentistId);
    }

    @Override
    public void setName(String name) {
        PreparedStatement setNameStatement = null;
        try {
            setNameStatement = SessionManager.getConnection().prepareStatement("UPDATE \"dentist\" SET \"name\" = ? WHERE \"id\" = ?");
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
            setAddressStatement = SessionManager.getConnection().prepareStatement("UPDATE \"dentist\" SET \"address\" = ? WHERE \"id\" = ?");
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
            setContactNumberStatement = SessionManager.getConnection().prepareStatement("UPDATE \"dentist\" SET \"contactNumber\" = ? WHERE \"id\" = ?");
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

    public StringProperty passwordProperty() {
        return password;
    }

    public String getPassword() {
        return passwordProperty().get();
    }

    public void setPassword(String password) {
        PreparedStatement setPasswordStatement = null;
        try {
            setPasswordStatement = SessionManager.getConnection().prepareStatement("UPDATE \"dentist\" SET \"password\" = ? WHERE \"id\" = ?");
            setPasswordStatement.setString(1, password);
            setPasswordStatement.setInt(2, getId());
            setPasswordStatement.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            SessionManager.tryCloseSQL(setPasswordStatement);
        }
        passwordProperty().set(password);
    }

    public ObjectProperty<PatientManager> patientManagerProperty() {
        return patientManager;
    }

    public void setPatientManager(PatientManager patientManager) {
        patientManagerProperty().set(patientManager);
    }

    public PatientManager getPatientManager() {
        return patientManagerProperty().get();
    }

    @Override
    public String toString() {
        return "Dentist: {" +
                "id: " + getId() +
                ", name: " + getName() +
                ", address: " + getAddress() +
                ", contactNumber: " + getContactNumber() +
                ", password: " + getPassword() +
                ", patients: {" + getPatientManager().getPatientsUnmodifiable() + "}" +
                "}";
    }

    public void print() {
        System.out.println(this);
    }

}
