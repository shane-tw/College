package managers;

import models.*;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Shane on 12/02/2017.
 */
public final class SessionManager {
    private static Dentist loggedInDentist;
    private static Connection connection;
    private static final String DB_URL = "jdbc:derby:dentistdb_r00141078;create=true";

    public static Dentist getLoggedInDentist() { // ManagementController or another class may need this at some point.
        return loggedInDentist;
    }

    public static void setLoggedInDentist(Dentist loggedInDentist) {
        SessionManager.loggedInDentist = loggedInDentist;
    }

    public static void tryCreateTables() {
        try {
            Statement createDentistStatement = getConnection().createStatement();
            createDentistStatement.executeUpdate(
                "CREATE TABLE \"dentist\" (\n" +
                "  \"id\" int NOT NULL,\n" +
                "  \"name\" clob(255) NOT NULL,\n" +
                "  \"address\" clob(255),\n" +
                "  \"contactNumber\" clob(255),\n" +
                "  \"password\" clob(255) NOT NULL,\n" +
                "  PRIMARY KEY (\"id\")\n" +
                ")"
            );
            Statement createPatientStatement = getConnection().createStatement();
            createPatientStatement.executeUpdate(
                "CREATE TABLE \"patient\" (\n" +
                "  \"id\" int NOT NULL,\n" +
                "  \"name\" clob(255) NOT NULL,\n" +
                "  \"address\" clob(255),\n" +
                "  \"contactNumber\" clob(255),\n" +
                "  PRIMARY KEY (\"id\")\n" +
                ")"
            );
            Statement createDentistPatientStatement = getConnection().createStatement();
            createDentistPatientStatement.executeUpdate( // Allowing for a many-many dentist-patient relationship.
                "CREATE TABLE \"dentist_patient\" (\n" + // A patient may use more than one dentist.
                "  \"dentist_id\" int NOT NULL,\n" + // Future-proofing.
                "  \"patient_id\" int NOT NULL,\n" +
                "  PRIMARY KEY (\"dentist_id\",\"patient_id\"),\n" +
                "  CONSTRAINT \"patients_dentist_id\" FOREIGN KEY (\"dentist_id\") REFERENCES \"dentist\" (\"id\") ON DELETE CASCADE,\n" +
                "  CONSTRAINT \"patients_patient_id\" FOREIGN KEY (\"patient_id\") REFERENCES \"patient\" (\"id\") ON DELETE CASCADE\n" +
                ")"
            );
            Statement createInvoiceStatement = getConnection().createStatement();
            createInvoiceStatement.executeUpdate(
                "CREATE TABLE \"invoice\" (\n" +
                "  \"id\" int NOT NULL,\n" +
                "  \"amount\" decimal(6,2) NOT NULL,\n" +
                "  \"date\" date DEFAULT NULL,\n" +
                "  \"is_paid\" boolean DEFAULT FALSE,\n" +
                "  \"patient_id\" int NOT NULL,\n" +
                "  CONSTRAINT \"invoice_patient_id\" FOREIGN KEY (\"patient_id\") REFERENCES \"patient\" (\"id\") ON DELETE CASCADE,\n" +
                "  PRIMARY KEY (\"id\")\n" +
                ")"
            );
            Statement createPaymentStatement = getConnection().createStatement();
            createPaymentStatement.executeUpdate(
                "CREATE TABLE \"payment\" (\n" +
                "  \"id\" int NOT NULL,\n" +
                "  \"amount\" decimal(6,2) NOT NULL,\n" +
                "  \"date\" date DEFAULT NULL,\n" +
                "  \"invoice_id\" int NOT NULL,\n" +
                "  CONSTRAINT \"payment_invoice_id\" FOREIGN KEY (\"invoice_id\") REFERENCES \"invoice\" (\"id\") ON DELETE CASCADE,\n" +
                "  PRIMARY KEY (\"id\")\n" +
                ")"
            );
            Statement createProcedureStatement = getConnection().createStatement();
            createProcedureStatement.executeUpdate(
                "CREATE TABLE \"procedure\" (\n" +
                "  \"id\" int NOT NULL,\n" +
                "  \"name\" clob(255),\n" +
                "  \"cost\" decimal(6,2) NOT NULL,\n" +
                "  PRIMARY KEY (\"id\")\n" +
                ")"
            );
            Statement createInvoiceProcedureStatement = getConnection().createStatement();
            createInvoiceProcedureStatement.executeUpdate(
                "CREATE TABLE \"invoice_procedure\" (\n" +
                "  \"id\" int NOT NULL,\n" +
                "  \"invoice_id\" int NOT NULL,\n" +
                "  \"name\" clob(255),\n" + // Totally independent from global procedure.
                "  \"cost\" decimal(6,2) NOT NULL,\n" + // Ensuring if the global cost changes, it doesn't affect invoices made in past.
                "  CONSTRAINT \"invoice_invoice_id\" FOREIGN KEY (\"invoice_id\") REFERENCES \"invoice\" (\"id\") ON DELETE CASCADE ,\n" +
                "  PRIMARY KEY (\"id\")\n" +
                ")"
            );
        } catch (SQLException e) {
            if (e.getSQLState().equals("X0Y32")) { // Table already exists
                return; // Do nothing.
            }
            e.printStackTrace();
        }
    }

    public static void createConnectionIfNeeded() { // Returns true if successful.
        if (getConnection() != null) {
            return;
        }
        try {
            setConnection(DriverManager.getConnection(getDbURL()));
            getConnection().setAutoCommit(false);
            tryCreateTables();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void shutdownConnection() {
        try {
            DriverManager.getConnection(getDbURL() + ";shutdown=true");
            getConnection().close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void tryCloseSQL(Statement statement) {
        if (statement == null) {
            return;
        }
        try {
            statement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void tryCloseSQL(Connection connection) {
        if (connection == null) {
            return;
        }
        try {
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static String getDbURL() {
        return DB_URL;
    }

    public static Connection getConnection() {
        return connection;
    }

    public static void setConnection(Connection connection) {
        SessionManager.connection = connection;
    }

    public static void save() {
        createConnectionIfNeeded();
        try {
            getConnection().commit();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        tryCloseSQL(getConnection());
    }

    public static void load() {
        try {
            createConnectionIfNeeded();
            tryCreateTables();
            List<Dentist> dentistList = new ArrayList<>();

            Statement getDentistsStatement = getConnection().createStatement();
            ResultSet dentistResults = getDentistsStatement.executeQuery(
                "SELECT \"id\", \"name\", \"address\", \"contactNumber\", \"password\" FROM \"dentist\""
            );

            while (dentistResults.next()) {
                Dentist dentist = new Dentist();
                dentist.setId(dentistResults.getInt(1));
                dentist.setName(dentistResults.getString(2));
                dentist.setAddress(dentistResults.getString(3));
                dentist.setContactNumber(dentistResults.getString(4));
                dentist.setPassword(dentistResults.getString(5));

                List<Patient> patientList = new ArrayList<>();
                Statement getPatientsStatement = getConnection().createStatement();
                ResultSet patientResults = getPatientsStatement.executeQuery(
                    "SELECT \"patient\".\"id\", \"patient\".\"name\", \"patient\".\"address\", \"patient\".\"contactNumber\" " +
                    "FROM \"patient\" " +
                    "RIGHT JOIN \"dentist_patient\" " +
                    "ON \"patient\".\"id\" = \"dentist_patient\".\"patient_id\" " +
                    "WHERE \"dentist_patient\".\"dentist_id\" = " + dentist.getId()
                );
                while (patientResults.next()) {
                    Patient patient = new Patient();
                    patient.setId(patientResults.getInt(1));
                    patient.setName(patientResults.getString(2));
                    patient.setAddress(patientResults.getString(3));
                    patient.setContactNumber(patientResults.getString(4));

                    List<Invoice> invoiceList = new ArrayList<>();
                    Statement getInvoicesStatement = getConnection().createStatement();
                    ResultSet invoiceResults = getInvoicesStatement.executeQuery(
                        "SELECT \"invoice\".\"id\", \"invoice\".\"amount\", \"invoice\".\"date\" " +
                        "FROM \"invoice\" " +
                        "WHERE \"invoice\".\"patient_id\" = " + patient.getId()
                    );
                    while (invoiceResults.next()) {
                        Invoice invoice = new Invoice();
                        invoice.setId(invoiceResults.getInt(1));
                        invoice.setAmount(invoiceResults.getDouble(2));
                        invoice.setDate(invoiceResults.getDate(3));

                        List<Payment> paymentList = new ArrayList<>();
                        Statement getPaymentsStatement = getConnection().createStatement();
                        ResultSet paymentResults = getPaymentsStatement.executeQuery(
                            "SELECT \"payment\".\"id\", \"payment\".\"amount\", \"payment\".\"date\" " +
                            "FROM \"payment\" " +
                            "WHERE \"payment\".\"invoice_id\" = " + invoice.getId()
                        );
                        while (paymentResults.next()) {
                            Payment payment = new Payment();
                            payment.setId(paymentResults.getInt(1));
                            payment.setAmount(paymentResults.getDouble(2));
                            payment.setDate(paymentResults.getDate(3));
                            paymentList.add(payment);
                        }
                        tryCloseSQL(getPaymentsStatement);
                        invoice.loadPaymentsFromDB(paymentList);
                        List<Procedure> procedureList = new ArrayList<>();
                        Statement getProceduresStatement = getConnection().createStatement();
                        ResultSet procedureResults = getProceduresStatement.executeQuery(
                            "SELECT \"invoice_procedure\".\"id\", \"invoice_procedure\".\"name\", \"invoice_procedure\".\"cost\" " +
                            "FROM \"invoice_procedure\" " +
                            "WHERE \"invoice_procedure\".\"invoice_id\" = " + invoice.getId()
                        );
                        while (procedureResults.next()) {
                            Procedure procedure = new Procedure(invoice.getId());
                            procedure.setId(procedureResults.getInt(1));
                            procedure.setName(procedureResults.getString(2));
                            procedure.setCost(procedureResults.getDouble(3));
                            procedureList.add(procedure);
                        }
                        tryCloseSQL(getProceduresStatement);
                        invoice.loadProceduresFromDB(procedureList);
                        invoiceList.add(invoice);
                    }
                    tryCloseSQL(getInvoicesStatement);
                    patient.getInvoiceManager().loadInvoicesFromDB(invoiceList);
                    patientList.add(patient);
                }
                tryCloseSQL(getPatientsStatement);
                dentist.getPatientManager().loadPatientsFromDB(patientList);
                dentistList.add(dentist);
            }
            tryCloseSQL(getDentistsStatement);
            DentistManager.loadDentistsFromDB(dentistList);

            List<GlobalProcedure> globalProcedureList = new ArrayList<>();

            Statement getGlobalProcedures = getConnection().createStatement();
            ResultSet getGlobalProcedureResults = getGlobalProcedures.executeQuery(
                    "SELECT \"procedure\".\"id\", \"procedure\".\"name\", \"procedure\".\"cost\" " +
                            "FROM \"procedure\""
            );

            while (getGlobalProcedureResults.next()) {
                GlobalProcedure globalProcedure = new GlobalProcedure();
                globalProcedure.setId(getGlobalProcedureResults.getInt(1));
                globalProcedure.setName(getGlobalProcedureResults.getString(2));
                globalProcedure.setCost(getGlobalProcedureResults.getDouble(3));
                globalProcedureList.add(globalProcedure);
            }
            tryCloseSQL(getGlobalProcedures);
            GlobalProcedureManager.loadGlobalProceduresFromDB(globalProcedureList);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
