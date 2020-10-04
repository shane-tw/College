package managers;

import controllers.LoginController;
import controllers.PatientListController;
import models.Dentist;
import models.Patient;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by Shane on 12/02/2017.
 */
public final class DentistManager {

    private static List<Dentist> dentistList = new ArrayList<>();
    private static LoginController loginController;

    public static LoginController getLoginController() {
        return loginController;
    }

    public static void setLoginController(LoginController loginController) {
        DentistManager.loginController = loginController;
    }

    private static void setDentists(List<Dentist> dentistList) {
        DentistManager.dentistList = dentistList;
    }

    // Should only ever be ran by SessionManager.load once (on startup). Appends/loads list.
    public static void loadDentistsFromDB(List<Dentist> dentistList) {
        DentistManager.dentistList.addAll(dentistList);
        int newCount = 0;
        for (Dentist dentist : dentistList) {
            newCount = Math.max(newCount, dentist.getId());
        }
        Dentist.setCount(newCount + 1);
    }

    public static List<Dentist> getDentistsUnmodifiable() {
        return Collections.unmodifiableList(new ArrayList<>(dentistList));
    }

    public static void addDentist(Dentist dentist) {
        dentistList.add(dentist);
        PreparedStatement addDentistStatement = null;
        try {
            addDentistStatement = SessionManager.getConnection().prepareStatement("INSERT INTO \"dentist\"(\"id\", \"name\", \"address\", \"contactNumber\", \"password\") VALUES (?, ?, ?, ?, ?)");
            addDentistStatement.setInt(1, dentist.getId());
            addDentistStatement.setString(2, dentist.getName());
            addDentistStatement.setString(3, dentist.getAddress());
            addDentistStatement.setString(4, dentist.getContactNumber());
            addDentistStatement.setString(5, dentist.getPassword());
            addDentistStatement.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            SessionManager.tryCloseSQL(addDentistStatement);
        }
    }

    public static void removeDentist(Dentist dentist) {
        dentistList.remove(dentist);
        PreparedStatement removeDentistStatement = null;
        try {
            removeDentistStatement = SessionManager.getConnection().prepareStatement("DELETE FROM \"dentist\" WHERE \"id\" = ?");
            removeDentistStatement.setInt(1, dentist.getId());
            removeDentistStatement.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            SessionManager.tryCloseSQL(removeDentistStatement);
        }
    }

    public static Dentist findDentistById(int dentistId) {
        for (Dentist dentist : getDentistsUnmodifiable()) {
            if (dentist.getId() == dentistId) {
                return dentist;
            }
        }
        return null;
    }

    public static Dentist findDentistByName(String dentistName) {
        for (Dentist dentist : getDentistsUnmodifiable()) {
            if (dentist.getName().equalsIgnoreCase(dentistName)) {
                return dentist;
            }
        }
        return null;
    }

    public static void removeDentistById(int dentistId) {
        for (Dentist dentist : getDentistsUnmodifiable()) {
            if (dentist.getId() == dentistId) {
                removeDentist(dentist);
                break;
            }
        }
    }

}
