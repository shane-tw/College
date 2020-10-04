package controllers;

import javafx.event.ActionEvent;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import managers.SessionManager;
import models.*;
import views.Controller;
import views.ReportingView;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.time.Duration;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Date;

/**
 * Created by Shane on 26/03/2017.
 */
public class ReportingController extends Controller<ReportingView> {

    public ReportingController(ReportingView view) {
        super(view);
        getView().getReportByNameBtn().setOnAction(this::generateReportByName);
        getView().getReportByOwedBtn().setOnAction(this::generateReportByOwed);
    }

    public void generateReportByName(ActionEvent actionEvent) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Text Files", "*.txt"));
        File file = fileChooser.showSaveDialog(new Stage());
        if (file == null) {
            return;
        }
        PrintWriter printWriter;
        try {
            printWriter = new PrintWriter(file);
        } catch (FileNotFoundException e) {
            e.printStackTrace(); // I don't see how this could possibly ever happen.
            // It happens when the directory does not exist, but the user must choose the file via the file chooser - hence ensuring it exists.
            // I guess a race condition maybe?
            return; // If/When it happens, do nothing for the remainder of the method. Don't generate report.
        }
        List<Patient> patientList = new ArrayList<>(SessionManager.getLoggedInDentist().getPatientManager().getPatientsUnmodifiable());
        Collections.sort(patientList, Patient::compareByName);
        printPatients(printWriter, patientList);
    }

    public void generateReportByOwed(ActionEvent actionEvent) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Text Files", "*.txt"));
        File file = fileChooser.showSaveDialog(new Stage());
        if (file == null) {
            return;
        }
        PrintWriter printWriter;
        try {
            printWriter = new PrintWriter(file);
        } catch (FileNotFoundException e) {
            e.printStackTrace(); // I don't see how this could possibly ever happen.
            return;
        }
        List<Patient> patientList = new ArrayList<>(SessionManager.getLoggedInDentist().getPatientManager().getPatientsUnmodifiable());
        Collections.sort(patientList, Patient::compareByAmountOwed);
        Date currentDate = new Date();
        for (Patient patient : new ArrayList<>(patientList)) { // Wrapping in new arraylist so as to not affect list we're iterating when removing.
            boolean paidMoneyLast6Months = false;
            Double highestInvoiceAmount = 0.0;
            for (Invoice invoice : patient.getInvoiceManager().getInvoicesUnmodifiable()) {
                highestInvoiceAmount = Double.max(highestInvoiceAmount, invoice.getAmount());
                if (currentDate.getTime() - invoice.getDate().getTime() <= GlobalConstants.SIX_MONTHS_IN_MILLISECS) {
                    paidMoneyLast6Months = true;
                    break;
                }
            }
            boolean oweMoney = (highestInvoiceAmount > 0);
            if (!oweMoney || paidMoneyLast6Months) { // Remove the possibilities that would mean this patient does not match what we're looking for (owes money and didn't pay)
                patientList.remove(patient);
            }
        }
        printPatients(printWriter, patientList);
    }

    public void printPatients(PrintWriter printWriter, List<Patient> patientList) {
        printWriter.println("== PATIENT LIST START ==");
        for (Patient patient : patientList) {
            printWriter.printf("%4s== PATIENT START ==%n", "");
            printWriter.printf("%8sid: %d, name: %s, address: %s, contactNumber: %s%n", "",
                    patient.getId(), patient.getName(), patient.getAddress(), patient.getContactNumber());
            printWriter.printf("%8s== INVOICE LIST START ==%n", "");
            for (Invoice invoice : patient.getInvoiceManager().getInvoicesUnmodifiable()) {
                printWriter.printf("%12s== INVOICE START ==%n", "");
                printWriter.printf("%16sid: %d, date: %s, amount: %.2f, paid: %b%n", "", invoice.getId(), GlobalConstants.getDateFormat().format(invoice.getDate()), invoice.getAmount(), invoice.isPaid());
                printWriter.printf("%16s== PAYMENT LIST START ==%n", "");
                for (Payment payment : invoice.getPaymentsUnmodifiable()) {
                    printWriter.printf("%20s== PAYMENT START ==%n", "");
                    printWriter.printf("%24sid: %d, date: %s, amount: %.2f%n", "", payment.getId(), GlobalConstants.getDateFormat().format(payment.getDate()), payment.getAmount());
                    printWriter.printf("%20s== PAYMENT END ==%n", "");
                }
                printWriter.printf("%16s== PAYMENT LIST END ==%n", "");
                printWriter.printf("%16s== PROCEDURE LIST START ==%n", "");
                for (Procedure procedure : invoice.getProceduresUnmodifiable()) {
                    printWriter.printf("%20s== PROCEDURE START ==%n", "");
                    printWriter.printf("%24sid: %d, name: %s, cost: %.2f%n", "", procedure.getId(), procedure.getName(), procedure.getCost());
                    printWriter.printf("%20s== PROCEDURE END ==%n", "");
                }
                printWriter.printf("%16s== PROCEDURE LIST END ==%n", "");
                printWriter.printf("%12s== INVOICE END ==%n", "");
            }
            printWriter.printf("%8s== INVOICE LIST END ==%n", "");
            printWriter.printf("%4s== PATIENT END ==%n", "");
        }
        printWriter.printf("== PATIENT LIST END ==%n");
        printWriter.close();
    }

}
