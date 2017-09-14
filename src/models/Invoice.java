package models;

import controllers.PaymentListController;
import controllers.ProcedureListController;
import javafx.beans.binding.Bindings;
import javafx.beans.property.*;
import javafx.beans.value.ObservableValue;
import managers.SessionManager;
import views.PaymentListItemView;
import views.ProcedureListItemView;

import java.io.*;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

/**
 * Created by Shane on 31/01/2017.
 */
public class Invoice {
    private static IntegerProperty invoiceCount = new SimpleIntegerProperty(0);
    private IntegerProperty invoiceId = new SimpleIntegerProperty();
    private DoubleProperty invoiceAmt = new SimpleDoubleProperty();
    private ObjectProperty<Date> invoiceDate = new SimpleObjectProperty<>();
    private BooleanProperty isPaid = new SimpleBooleanProperty();
    private ObjectProperty<List<Procedure>> in_procList = new SimpleObjectProperty<>(new ArrayList<>());
    private ObjectProperty<List<Payment>> in_paymentList = new SimpleObjectProperty<>(new ArrayList<>());
    private ObjectProperty<Patient> patient = new SimpleObjectProperty<>();

    public Invoice() {
        setCount(getCount() + 1);
        setId(getCount());
        paidProperty().addListener(this::onPaidChanged);
        paidProperty().bind(amountProperty().lessThanOrEqualTo(0));
    }

    public Invoice(Patient patient, Date invoiceDate) {
        this();
        setPatient(patient);
        setDate(invoiceDate);
    }

    public void onPaidChanged(ObservableValue observable, Boolean oldValue, Boolean newValue) {
        PreparedStatement setPaidStatement = null;
        try {
            setPaidStatement = SessionManager.getConnection().prepareStatement("UPDATE \"invoice\" SET \"is_paid\" = ? WHERE \"id\" = ?");
            setPaidStatement.setBoolean(1, newValue);
            setPaidStatement.setInt(2, getId());
            setPaidStatement.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            SessionManager.tryCloseSQL(setPaidStatement);
        }    }

    public ObjectProperty<Patient> patientProperty() {
        return patient;
    }

    public Patient getPatient() {
        return patientProperty().get();
    }

    public void setPatient(Patient patient) {
        patientProperty().set(patient);
    }

    public static IntegerProperty countProperty() {
        return invoiceCount;
    }

    public static int getCount() {
        return countProperty().get();
    }

    public static void setCount(int invoiceCount) {
        countProperty().set(invoiceCount);
    }

    public IntegerProperty idProperty() {
        return invoiceId;
    }

    public int getId() {
        return idProperty().get();
    }

    public void setId(int invoiceId) {
        PreparedStatement setIdStatement = null;
        try {
            setIdStatement = SessionManager.getConnection().prepareStatement("UPDATE \"invoice\" SET \"id\" = ? WHERE \"id\" = ?");
            setIdStatement.setInt(1, invoiceId);
            setIdStatement.setInt(2, getId());
            setIdStatement.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            SessionManager.tryCloseSQL(setIdStatement);
        }
        idProperty().set(invoiceId);
    }

    public DoubleProperty amountProperty() {
        return invoiceAmt;
    }

    public double getAmount() {
        return amountProperty().get();
    }

    public void setAmount(double invoiceAmt) {
        PreparedStatement setAmountStatement = null;
        try {
            setAmountStatement = SessionManager.getConnection().prepareStatement("UPDATE \"invoice\" SET \"amount\" = ? WHERE \"id\" = ?");
            setAmountStatement.setDouble(1, invoiceAmt);
            setAmountStatement.setInt(2, getId());
            setAmountStatement.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            SessionManager.tryCloseSQL(setAmountStatement);
        }
        amountProperty().set(invoiceAmt);
    }

    public ObjectProperty<Date> dateProperty() {
        return invoiceDate;
    }

    public Date getDate() {
        return dateProperty().get();
    }

    public void setDate(Date invoiceDate) {
        PreparedStatement setDateStatement = null;
        try {
            setDateStatement = SessionManager.getConnection().prepareStatement("UPDATE \"invoice\" SET \"date\" = ? WHERE \"id\" = ?");
            setDateStatement.setDate(1, new java.sql.Date(invoiceDate.getTime()));
            setDateStatement.setInt(2, getId());
            setDateStatement.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            SessionManager.tryCloseSQL(setDateStatement);
        }
        dateProperty().set(invoiceDate);
    }

    public BooleanProperty paidProperty() {
        return isPaid;
    }

    public boolean isPaid() {
        return paidProperty().get();
    }

    public ObjectProperty<List<Procedure>> procedureProperty() {
        return in_procList;
    }

    public List<Procedure> getProceduresUnmodifiable() {
        return Collections.unmodifiableList(new ArrayList<>(procedureProperty().get()));
    }

    private void setProcedures(List<Procedure> in_procList) {
        this.in_procList.set(in_procList);
    }

    public void loadProceduresFromDB(List<Procedure> procedureList) {
        this.in_procList.get().addAll(procedureList);
        int newCount = 0;
        for (Procedure procedure : procedureList) {
            procedure.costProperty().addListener(this::onProcedureCostChanged);
            newCount = Math.max(newCount, procedure.getId());
        }
        Procedure.setCount(newCount + 1);
    }

    public ObjectProperty<List<Payment>> paymentsProperty() {
        return in_paymentList;
    }

    public List<Payment> getPaymentsUnmodifiable() {
        return Collections.unmodifiableList(new ArrayList<>(paymentsProperty().get()));
    }

    private void setPayments(List<Payment> in_paymentList) {
        paymentsProperty().set(in_paymentList);
    }

    public void loadPaymentsFromDB(List<Payment> paymentList) {
        this.in_paymentList.get().addAll(paymentList);
        int newCount = 0;
        for (Payment payment : paymentList) {
            payment.amountProperty().addListener(this::onPaymentAmountChanged);
            newCount = Math.max(newCount, payment.getId());
        }
        Payment.setCount(newCount + 1);
    }

    public void addPayment(Payment payment) {
        payment.amountProperty().addListener(this::onPaymentAmountChanged);
        onPaymentAmountChanged(null, 0, payment.getAmount());
        paymentsProperty().get().add(payment);
        PreparedStatement addPaymentStatement = null;
        try {
            addPaymentStatement = SessionManager.getConnection().prepareStatement("INSERT INTO \"payment\"(\"id\", \"amount\", \"date\", \"invoice_id\") VALUES (?, ?, ?, ?)");
            addPaymentStatement.setInt(1, payment.getId());
            addPaymentStatement.setDouble(2, payment.getAmount());
            addPaymentStatement.setDate(3, new java.sql.Date(payment.getDate().getTime()));
            addPaymentStatement.setInt(4, getId());
            addPaymentStatement.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            SessionManager.tryCloseSQL(addPaymentStatement);
        }
        PaymentListController.getInstance().showPayment(payment);
    }

    public void onPaymentAmountChanged(ObservableValue observable, Number oldValue, Number newValue) {
        setAmount(getAmount() - (newValue.doubleValue() - oldValue.doubleValue()));
    }

    public boolean removePayment(PaymentListItemView paymentListItemView) {
        onPaymentAmountChanged(null, paymentListItemView.getPayment().getAmount(), 0);
        PaymentListController.getInstance().hidePaymentView(paymentListItemView);
        PreparedStatement removePaymentStatement = null;
        try {
            removePaymentStatement = SessionManager.getConnection().prepareStatement("DELETE FROM \"payment\" WHERE \"id\" = ?");
            removePaymentStatement.setInt(1, paymentListItemView.getPayment().getId());
            removePaymentStatement.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            SessionManager.tryCloseSQL(removePaymentStatement);
        }
        return paymentsProperty().get().remove(paymentListItemView.getPayment());
    }

    public void addProcedure(Procedure procedure) {
        procedure.costProperty().addListener(this::onProcedureCostChanged);
        onProcedureCostChanged(null, 0, procedure.getCost());
        procedureProperty().get().add(procedure);
        PreparedStatement addProcedureStatement = null;
        try {
            addProcedureStatement = SessionManager.getConnection().prepareStatement("INSERT INTO \"invoice_procedure\"(\"id\", \"invoice_id\", \"name\", \"cost\") VALUES (?, ?, ?, ?)");
            addProcedureStatement.setInt(1, procedure.getId());
            addProcedureStatement.setInt(2, procedure.getInvoiceId());
            addProcedureStatement.setString(3, procedure.getName());
            addProcedureStatement.setDouble(4, procedure.getCost());
            addProcedureStatement.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            SessionManager.tryCloseSQL(addProcedureStatement);
        }
        ProcedureListController.getInstance().showProcedure(procedure);
    }

    public void onProcedureCostChanged(ObservableValue observable, Number oldValue, Number newValue) {
        setAmount(getAmount() + (newValue.doubleValue() - oldValue.doubleValue()));
    }

    public boolean removeProcedure(ProcedureListItemView procedureListItemView) {
        onProcedureCostChanged(null, procedureListItemView.getProcedure().getCost(), 0);
        ProcedureListController.getInstance().hideProcedureView(procedureListItemView);
        PreparedStatement removeProcedureStatement = null;
        try {
            removeProcedureStatement = SessionManager.getConnection().prepareStatement("DELETE FROM \"invoice_procedure\" WHERE \"id\" = ?");
            removeProcedureStatement.setInt(1, procedureListItemView.getProcedure().getId());
            removeProcedureStatement.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            SessionManager.tryCloseSQL(removeProcedureStatement);
        }
        return procedureProperty().get().remove(procedureListItemView.getProcedure());
    }
    
    @Override
    public String toString() {
        return "Invoice: {" +
                "id: " + getId() +
                ", amount: " + getAmount() +
                ", date: " + getDate() +
                ", isPaid: " + isPaid() +
                ", procedures: {" + getProceduresUnmodifiable() + "}" +
                ", payments: {" + getPaymentsUnmodifiable() + "}" +
                "}";
    }

    public void print() {
        System.out.println(this);
    }

}
