package managers;

import controllers.PatientListController;
import models.Invoice;
import models.Patient;
import views.PatientListItemView;

import java.io.Serializable;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.*;

/**
 * Created by Shane on 12/02/2017.
 */
public class PatientManager implements Serializable {
    private List<Patient> patientList;

    public PatientManager() {
        setPatients(new ArrayList<>());
    }

    private void setPatients(List<Patient> patientList) {
        this.patientList = patientList;
    }

    // Should only ever be ran by SessionManager.load once (on startup). Appends/loads list.
    public void loadPatientsFromDB(List<Patient> patientList) {
        this.patientList.addAll(patientList);
        int newCount = 0;
        for (Patient patient : patientList) {
            newCount = Math.max(newCount, patient.getId());
        }
        Patient.setCount(newCount + 1);
    }

    public List<Patient> getPatientsUnmodifiable() {
        return Collections.unmodifiableList(new ArrayList<>(patientList));
    }

    public void addPatient(Patient patient) {
        patientList.add(patient);
        PreparedStatement addPatientStatement = null;
        PreparedStatement addAssociationStatement = null;
        try {
            addPatientStatement = SessionManager.getConnection().prepareStatement("INSERT INTO \"patient\"(\"id\", \"name\", \"address\", \"contactNumber\") VALUES (?, ?, ?, ?)");
            addPatientStatement.setInt(1, patient.getId());
            addPatientStatement.setString(2, patient.getName());
            addPatientStatement.setString(3, patient.getAddress());
            addPatientStatement.setString(4, patient.getContactNumber());
            addPatientStatement.execute();
            addAssociationStatement = SessionManager.getConnection().prepareStatement("INSERT INTO \"dentist_patient\" (\"dentist_id\", \"patient_id\") VALUES (?, ?)");
            addAssociationStatement.setInt(1, SessionManager.getLoggedInDentist().getId());
            addAssociationStatement.setInt(2, patient.getId());
            addAssociationStatement.execute();

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            SessionManager.tryCloseSQL(addPatientStatement);
            SessionManager.tryCloseSQL(addAssociationStatement);
        }
        PatientListController.getInstance().showPatient(patient);
    }

    public boolean removePatient(PatientListItemView patientListItemView) {
        PatientListController.getInstance().hidePatientView(patientListItemView);
        for (Invoice invoice : patientListItemView.getPatient().getInvoiceManager().getInvoicesUnmodifiable()) {
            patientListItemView.getPatient().getInvoiceManager().removeInvoice(invoice);
        }
        PreparedStatement removePatientStatement = null;
        try {
            removePatientStatement = SessionManager.getConnection().prepareStatement("DELETE FROM \"patient\" WHERE \"id\" = ?");
            removePatientStatement.setInt(1, patientListItemView.getPatient().getId());
            removePatientStatement.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            SessionManager.tryCloseSQL(removePatientStatement);
        }
        return patientList.remove(patientListItemView.getPatient());
    }
}
