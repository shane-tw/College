package managers;

import controllers.InvoiceListController;
import controllers.PatientListController;
import javafx.scene.Node;
import models.Invoice;
import models.Patient;

import java.io.Serializable;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.*;

/**
 * Created by Shane on 12/02/2017.
 */
public final class InvoiceManager implements Serializable {
    private static Map<Invoice, Node> invoiceNodeMap = new HashMap<>();
    private List<Invoice> invoiceList;

    public InvoiceManager() {
        setInvoices(new ArrayList<>());
    }

    private void setInvoices(List<Invoice> invoiceList) {
        this.invoiceList = invoiceList;
    }

    // Should only ever be ran by SessionManager.load once (on startup). Appends/loads list.
    public void loadInvoicesFromDB(List<Invoice> invoiceList) {
        this.invoiceList.addAll(invoiceList);
        int newCount = 0;
        for (Invoice invoice : invoiceList) {
            newCount = Math.max(newCount, invoice.getId());
        }
        Invoice.setCount(newCount + 1);
    }

    public List<Invoice> getInvoicesUnmodifiable() {
        return Collections.unmodifiableList(new ArrayList<>(invoiceList));
    }

    public static void addInvoiceAssociation(Invoice invoice, Node root) {
        invoiceNodeMap.put(invoice, root);
    }

    public static Invoice getInvoiceByRoot(Node root) {
        for (Map.Entry<Invoice, Node> entry : invoiceNodeMap.entrySet()) {
            if (entry.getValue().equals(root)) {
                return entry.getKey();
            }
        }
        throw new IllegalArgumentException("The node supplied must exist in the map.");
    }

    public static Node getRootByInvoice(Invoice invoice) {
        for (Map.Entry<Invoice, Node> entry : invoiceNodeMap.entrySet()) {
            if (entry.getKey().equals(invoice)) {
                return entry.getValue();
            }
        }
        throw new IllegalArgumentException("The invoice supplied must exist in the map.");
    }

    public void addInvoice(Invoice invoice) {
        invoiceList.add(invoice);
        PreparedStatement addInvoiceStatement = null;
        try {
            addInvoiceStatement = SessionManager.getConnection().prepareStatement("INSERT INTO \"invoice\"(\"id\", \"amount\", \"date\", \"is_paid\", \"patient_id\") VALUES (?, ?, ?, ?, ?)");
            addInvoiceStatement.setInt(1, invoice.getId());
            addInvoiceStatement.setDouble(2, invoice.getAmount());
            addInvoiceStatement.setDate(3, new java.sql.Date(invoice.getDate().getTime()));
            addInvoiceStatement.setBoolean(4, invoice.isPaid());
            addInvoiceStatement.setInt(5, invoice.getPatient().getId());
            addInvoiceStatement.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            SessionManager.tryCloseSQL(addInvoiceStatement);
        }
        Node root = InvoiceListController.getInstance().showInvoice(invoice);
        invoiceNodeMap.put(invoice, root);
    }

    public boolean removeInvoice(Invoice invoice) {
        PreparedStatement removeInvoiceStatement = null;
        try {
            removeInvoiceStatement = SessionManager.getConnection().prepareStatement("DELETE FROM \"invoice\" WHERE \"id\" = ?");
            removeInvoiceStatement.setInt(1, invoice.getId());
            removeInvoiceStatement.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            SessionManager.tryCloseSQL(removeInvoiceStatement);
        }
        if (InvoiceListController.getInstance() != null) {
            InvoiceListController.getInstance().hideInvoice(invoice);
        }
        invoiceNodeMap.keySet().remove(invoice);
        return invoiceList.remove(invoice);
    }
}
