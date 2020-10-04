package models;

import javafx.beans.property.*;
import managers.SessionManager;

import java.io.*;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Date;

/**
 * Created by Shane on 31/01/2017.
 */
public class Payment {
    //  Using properties allows you to trigger events when the values in them change.
    private static IntegerProperty paymentCount = new SimpleIntegerProperty(0);
    private IntegerProperty paymentId = new SimpleIntegerProperty();
    private DoubleProperty paymentAmt = new SimpleDoubleProperty();
    private ObjectProperty<Date> paymentDate = new SimpleObjectProperty<>();

    public Payment() {
        setCount(getCount() + 1); // Incrementing count because we just created a new payment
        setId(getCount()); // Set id to the new count.
    }

    public static IntegerProperty countProperty() {
        return paymentCount;
    }

    public static int getCount() {
        return countProperty().get();
    }

    public static void setCount(int paymentCount) {
        countProperty().set(paymentCount);
    }

    public IntegerProperty idProperty() {
        return paymentId;
    }

    public int getId() {
        return idProperty().get();
    }

    public void setId(int paymentId) {
        PreparedStatement setIdStatement = null;
        try {
            setIdStatement = SessionManager.getConnection().prepareStatement("UPDATE \"payment\" SET \"id\" = ? WHERE \"id\" = ?");
            setIdStatement.setInt(1, paymentId);
            setIdStatement.setInt(2, getId());
            setIdStatement.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            SessionManager.tryCloseSQL(setIdStatement);
        }
        idProperty().set(paymentId);
    }

    public DoubleProperty amountProperty() {
        return paymentAmt;
    }

    public double getAmount() {
        return amountProperty().get();
    }

    public void setAmount(double paymentAmt) {
        PreparedStatement setAmountStatement = null;
        try {
            setAmountStatement = SessionManager.getConnection().prepareStatement("UPDATE \"payment\" SET \"amount\" = ? WHERE \"id\" = ?");
            setAmountStatement.setDouble(1, paymentAmt);
            setAmountStatement.setInt(2, getId());
            setAmountStatement.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            SessionManager.tryCloseSQL(setAmountStatement);
        }
        amountProperty().set(paymentAmt);
    }

    public ObjectProperty<Date> dateProperty() {
        return paymentDate;
    }

    public Date getDate() {
        return dateProperty().get();
    }

    public void setDate(Date paymentDate) {
        PreparedStatement setDateStatement = null;
        try {
            setDateStatement = SessionManager.getConnection().prepareStatement("UPDATE \"payment\" SET \"date\" = ? WHERE \"id\" = ?");
            setDateStatement.setDate(1, new java.sql.Date(paymentDate.getTime()));
            setDateStatement.setInt(2, getId());
            setDateStatement.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            SessionManager.tryCloseSQL(setDateStatement);
        }
        dateProperty().set(paymentDate);
    }

    @Override
    public String toString() {
        return "Payment: {" +
                "id: " + getId() +
                ", amount: " + getAmount() +
                ", date: " + getDate() +
                "}";
    }

    public void print() {
        System.out.println(this);
    }

}
