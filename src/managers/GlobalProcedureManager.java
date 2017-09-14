package managers;

import controllers.GlobalProcedureListController;
import models.GlobalProcedure;
import views.GlobalProcedureListItemView;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by Shane on 27/04/2017.
 */
public class GlobalProcedureManager {

    private static List<GlobalProcedure> globalProcedureList = new ArrayList<>();
    
    private static void setGlobalProcedures(List<GlobalProcedure> globalProcedureList) {
        GlobalProcedureManager.globalProcedureList = globalProcedureList;
    }

    // Should only ever be ran by SessionManager.load once (on startup). Appends/loads list.
    public static void loadGlobalProceduresFromDB(List<GlobalProcedure> globalProcedureList) {
        GlobalProcedureManager.globalProcedureList.addAll(globalProcedureList);
        int newCount = 0;
        for (GlobalProcedure globalProcedure : globalProcedureList) {
            newCount = Math.max(newCount, globalProcedure.getId());
        }
        GlobalProcedure.setCount(newCount + 1);
    }

    public static List<GlobalProcedure> getGlobalProceduresUnmodifiable() {
        return Collections.unmodifiableList(new ArrayList<>(globalProcedureList));
    }
    
    public static void addGlobalProcedure(GlobalProcedure globalProcedure) {
        globalProcedureList.add(globalProcedure);
        PreparedStatement addGlobalProcedureStatement = null;
        try {
            addGlobalProcedureStatement = SessionManager.getConnection().prepareStatement("INSERT INTO \"procedure\"(\"id\", \"name\", \"cost\") VALUES (?, ?, ?)");
            addGlobalProcedureStatement.setInt(1, globalProcedure.getId());
            addGlobalProcedureStatement.setString(2, globalProcedure.getName());
            addGlobalProcedureStatement.setDouble(3, globalProcedure.getCost());
            addGlobalProcedureStatement.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            SessionManager.tryCloseSQL(addGlobalProcedureStatement);
        }
        GlobalProcedureListController.getInstance().showGlobalProcedure(globalProcedure);
    }

    public static boolean removeGlobalProcedureView(GlobalProcedureListItemView globalProcedureListItemView) {
        PreparedStatement removeGlobalProcedureStatement = null;
        try {
            removeGlobalProcedureStatement = SessionManager.getConnection().prepareStatement("DELETE FROM \"procedure\" WHERE \"id\" = ?");
            removeGlobalProcedureStatement.setInt(1, globalProcedureListItemView.getProcedure().getId());
            removeGlobalProcedureStatement.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            SessionManager.tryCloseSQL(removeGlobalProcedureStatement);
        }
        GlobalProcedureListController.getInstance().hideGlobalProcedureView(globalProcedureListItemView);
        return globalProcedureList.remove(globalProcedureListItemView.getProcedure());
    }
    
}
