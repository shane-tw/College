package models;

import javafx.beans.property.*;
import managers.SessionManager;

import java.io.*;
import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 * Created by Shane on 31/01/2017.
 */
public class Procedure {
    private static IntegerProperty count = new SimpleIntegerProperty(0);
    private IntegerProperty id = new SimpleIntegerProperty();
    private IntegerProperty invoiceId = new SimpleIntegerProperty();
    private StringProperty name = new SimpleStringProperty();
    private DoubleProperty cost = new SimpleDoubleProperty();

    public Procedure(int invoiceId) {
        setCount(getCount() + 1);
        setId(getCount());
        setInvoiceId(invoiceId);
    }

    public static int getCount() {
        return count.get();
    }

    public static IntegerProperty countProperty() {
        return count;
    }

    public static void setCount(int count) {
        Procedure.count.set(count);
    }

    public int getId() {
        return id.get();
    }

    public IntegerProperty idProperty() {
        return id;
    }

    public void setId(int id) {
        PreparedStatement setIdStatement = null;
        try {
            setIdStatement = SessionManager.getConnection().prepareStatement("UPDATE \"invoice_procedure\" SET \"id\" = ? WHERE \"id\" = ?");
            setIdStatement.setInt(1, id);
            setIdStatement.setInt(2, getId());
            setIdStatement.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            SessionManager.tryCloseSQL(setIdStatement);
        }
        this.id.set(id);
    }

    public int getInvoiceId() {
        return invoiceId.get();
    }

    public IntegerProperty invoiceIdProperty() {
        return invoiceId;
    }

    public String getName() {
        return name.get();
    }

    public StringProperty nameProperty() {
        return name;
    }

    public void setName(String name) {
        PreparedStatement setNameStatement = null;
        try {
            setNameStatement = SessionManager.getConnection().prepareStatement("UPDATE \"invoice_procedure\" SET \"name\" = ? WHERE \"id\" = ?");
            setNameStatement.setString(1, name);
            setNameStatement.setInt(2, getId());
            setNameStatement.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            SessionManager.tryCloseSQL(setNameStatement);
        }
        this.name.set(name);
    }

    public double getCost() {
        return cost.get();
    }

    public DoubleProperty costProperty() {
        return cost;
    }

    public void setCost(double cost) {
        PreparedStatement setCostStatement = null;
        try {
            setCostStatement = SessionManager.getConnection().prepareStatement("UPDATE \"invoice_procedure\" SET \"cost\" = ? WHERE \"id\" = ?");
            setCostStatement.setDouble(1, cost);
            setCostStatement.setInt(2, getId());
            setCostStatement.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            SessionManager.tryCloseSQL(setCostStatement);
        }
        this.cost.set(cost);
    }

    public void setInvoiceId(int invoiceId) {
        PreparedStatement setIdStatement = null;
        try {
            setIdStatement = SessionManager.getConnection().prepareStatement("UPDATE \"invoice_procedure\" SET \"invoice_id\" = ? WHERE \"id\" = ?");
            setIdStatement.setInt(1, invoiceId);
            setIdStatement.setInt(2, getId());
            setIdStatement.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            SessionManager.tryCloseSQL(setIdStatement);
        }
        invoiceIdProperty().set(invoiceId);
    }

    @Override
    public String toString() {
        return "Procedure: {" +
                "id: " + getId() +
                ", invoiceId: " + getInvoiceId() +
                ", name: " + getName() +
                ", cost: " + getCost() +
                "}";
    }

    public void print() {
        System.out.println(this);
    }

}
