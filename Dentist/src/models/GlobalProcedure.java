package models;

import javafx.beans.property.*;
import managers.SessionManager;

import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 * Created by Shane on 31/01/2017.
 */
public class GlobalProcedure {
    private static IntegerProperty procCount = new SimpleIntegerProperty(0); // Doing this allows binding and listeners. Can trigger events when value changes. Default value 0.
    private IntegerProperty procId = new SimpleIntegerProperty();
    private StringProperty procName = new SimpleStringProperty();
    private DoubleProperty procCost = new SimpleDoubleProperty();

    public GlobalProcedure() {
        setCount(getCount() + 1);
        setId(getCount());
    }

    public GlobalProcedure(String procName, double procCost) {
        this();
        setName(procName);
        setCost(procCost);
    }

    public static IntegerProperty countProperty() {
        return procCount;
    } // Provide access to the property so that it can be bound or listeners can be added. Allows event handling on attribute.

    public static int getCount() {
        return countProperty().get();
    } // We want the int value, not the property, hence .get()

    public static void setCount(int procCount) {
        countProperty().set(procCount);
    } // Change  the int value, not the property itself.

    public IntegerProperty idProperty() {
        return procId;
    }

    public int getId() {
        return idProperty().get();
    }

    public void setId(int procId) {
        PreparedStatement setIdStatement = null;
        try {
            setIdStatement = SessionManager.getConnection().prepareStatement("UPDATE \"procedure\" SET \"id\" = ? WHERE \"id\" = ?");
            setIdStatement.setInt(1, procId);
            setIdStatement.setInt(2, getId());
            setIdStatement.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            SessionManager.tryCloseSQL(setIdStatement);
        }
        idProperty().set(procId);
    }

    public StringProperty nameProperty() {
        return procName;
    }

    public String getName() {
        return nameProperty().get();
    }

    public void setName(String procName) {
        PreparedStatement setNameStatement = null;
        try {
            setNameStatement = SessionManager.getConnection().prepareStatement("UPDATE \"procedure\" SET \"name\" = ? WHERE \"id\" = ?");
            setNameStatement.setString(1, procName);
            setNameStatement.setInt(2, getId());
            setNameStatement.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            SessionManager.tryCloseSQL(setNameStatement);
        }
        nameProperty().set(procName);
    }

    public DoubleProperty costProperty() {
        return procCost;
    }

    public double getCost() {
        return costProperty().get();
    }

    public void setCost(double procCost) {
        PreparedStatement setNameStatement = null;
        try {
            setNameStatement = SessionManager.getConnection().prepareStatement("UPDATE \"procedure\" SET \"cost\" = ? WHERE \"id\" = ?");
            setNameStatement.setDouble(1, procCost);
            setNameStatement.setInt(2, getId());
            setNameStatement.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            SessionManager.tryCloseSQL(setNameStatement);
        }
        costProperty().set(procCost);
    }

    @Override
    public String toString() {
        return "GlobalProcedure: {" +
                "id: " + getId() +
                ", name: " + getName() +
                ", cost: " + getCost() +
                "}";
    }

    public void print() {
        System.out.println(this);
    }

}
