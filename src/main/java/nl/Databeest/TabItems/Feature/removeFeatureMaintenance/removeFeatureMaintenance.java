package nl.Databeest.TabItems.Feature.removeFeatureMaintenance;

import nl.Databeest.Helpers.DateHelper;
import nl.Databeest.Helpers.JTableButtonMouseListener;
import nl.Databeest.Helpers.JTableButtonRenderer;
import nl.Databeest.TabItems.IndexAbstractTableModel;
import nl.Databeest.TabItems.SubMenuItem;

import javax.swing.*;
import javax.swing.table.TableCellRenderer;
import java.awt.*;
import java.sql.*;
import java.util.Date;
import java.text.DateFormat;
import java.text.SimpleDateFormat;


/**
 * Created by A on 20/12/2016.
 */
public class removeFeatureMaintenance extends SubMenuItem{
    private JPanel mainPanel;
    private JTable tblFeatureInMaintenance;

    public removeFeatureMaintenance() {
        getRoomTypeNameAndFeatureSeqNoFromMaintenance();

    }

    @Override
    protected String getMenuItemName() {
        return "Remove Feature Maintenance";
    }

    @Override
    protected Component getMainPanel() {
        return mainPanel;
    }

    public String getFeatureTypeStartTime(String typeName) {
        Connection con = getConnection();
        PreparedStatement stmt = null;
        try {
            stmt = con.prepareStatement("SELECT START_TIME FROM FEATURE_TYPE WHERE NAME = ?");
            stmt.setEscapeProcessing(true);

            stmt.setString(1, typeName);

            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                return rs.getString("START_TIME");

            }

        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(null, ex.getMessage());
        }
        return "1";
    }

    public void getRoomTypeNameAndFeatureSeqNoFromMaintenance() {
        Connection con = getConnection();
        PreparedStatement stmt = null;

        try {
            stmt = con.prepareStatement("SELECT FEATURE_TYPE_NAME, FEATURE_SEQ_NO, START_TIME FROM FEATURE_MAINTENANCE WHERE IS_CANCELLED <> 1");
            stmt.setEscapeProcessing(true);


            ResultSet rs = stmt.executeQuery();

            tblFeatureInMaintenance.setModel(new IndexAbstractTableModel(rs) {

                @Override
                public void deleteRow(Object[] row) {

                    deleteFeatureMaintenance((String) row[0], Integer.parseInt((String) row[1]), (String) row[2]);
                }
            });
            closeConn(con, stmt);

            TableCellRenderer buttonRenderer = new JTableButtonRenderer();
            tblFeatureInMaintenance.getColumn("Delete").setCellRenderer(buttonRenderer);
            tblFeatureInMaintenance.addMouseListener(new JTableButtonMouseListener(tblFeatureInMaintenance));


        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(null, ex.getMessage());
        }
    }


        private void deleteFeatureMaintenance(String typeName, int seqNo, String startTime){
            Connection con = getConnection();
            PreparedStatement stmt = null;

            try {
                stmt = con.prepareStatement("EXEC SP_DELETE_FEATURE_MAINTENANCE ?,?,?,?");
                stmt.setEscapeProcessing(true);

                stmt.setString(1, typeName);
                stmt.setInt(2, seqNo);
                stmt.setString(3, getFeatureTypeStartTime(typeName));
                stmt.setString(4, startTime);

                stmt.execute();
                JOptionPane.showMessageDialog(null, "The feature maintenance type has been removed successfully.", "Success!", 1);
                closeConn(con, stmt);
            } catch (SQLException ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(null, ex.getMessage());
            }

            setTable();
        }

    private void setTable(){
        getRoomTypeNameAndFeatureSeqNoFromMaintenance();

        TableCellRenderer buttonRenderer = new JTableButtonRenderer();
        tblFeatureInMaintenance.getColumn("Delete").setCellRenderer(buttonRenderer);
        tblFeatureInMaintenance.addMouseListener(new JTableButtonMouseListener(tblFeatureInMaintenance));
    }


}
