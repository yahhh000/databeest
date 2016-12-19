package nl.Databeest.TabItems.Feature.addFeatureMaintenance;

import nl.Databeest.Helpers.DateHelper;
import nl.Databeest.TabItems.SubMenuItem;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

/**
 * Created by A on 19/12/2016.
 */
public class addFeatureMaintenance extends SubMenuItem{
    private JButton btnAddFeatureMaintenance;
    private JPanel mainPanel;
    private JComboBox cmbFeatureTypeNameForMaintenance;
    private JTextField txtFeatureMaintenanceStartTime;
    private JPanel startDatePanel;
    private JSpinner startDaySpinner;
    private JComboBox startMonthComboBox;
    private JSpinner startYearSpinner;
    private JPanel endDatePanel;
    private JSpinner endDaySpinner;
    private JComboBox endMonthComboBox;
    private JSpinner endYearSpinner;
    private JTextField txtReasonFeatureMaintenance;

    private final String[] MONTHS = new String[]{"January","February","March","April","May","June","July","August","September","October","November","December"};


    public addFeatureMaintenance() {
        getRoomTypeNameAndFeatureSeqNo();
        startMonthComboBox.addItem("");
        endMonthComboBox.addItem("");
        for (int i = 0; i < MONTHS.length; i++) {
            startMonthComboBox.addItem(MONTHS[i]);
            endMonthComboBox.addItem(MONTHS[i]);}
        btnAddFeatureMaintenance.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                addFeatureMaintenance();

            }
        });
    }

    @Override
    protected String getMenuItemName() {
        return "Feature Maintenance";
    }

    @Override
    protected Component getMainPanel() {
        return mainPanel;
    }

    public void addFeatureMaintenance() {
        Connection con = getConnection();
        PreparedStatement stmt = null;

        String string = cmbFeatureTypeNameForMaintenance.getSelectedItem().toString();
        String[] parts = string.split("\\|");
        String part1 = parts[0];
        int part2 = Integer.parseInt(parts[1]);

        System.out.println(part2);



        Date startDate = DateHelper.createSqlDate(
                startDaySpinner.getValue().toString(),
                startMonthComboBox.getSelectedIndex(),
                startYearSpinner.getValue().toString()
        );

        Date endDate = DateHelper.createSqlDate(
                endDaySpinner.getValue().toString(),
                endMonthComboBox.getSelectedIndex(),
                endYearSpinner.getValue().toString()
        );


        try{
            stmt = con.prepareStatement("SP_ADD_FEATURE_MAINTENANCE ?,?,?,?,?");
            stmt.setEscapeProcessing(true);


            stmt.setString(1, part1);
            stmt.setInt(2, part2);
            stmt.setDate(3, startDate);
            stmt.setDate(4, endDate);
            stmt.setString(5, txtReasonFeatureMaintenance.getText());

            stmt.execute();



        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(null, ex.getMessage());
        }

    }

    public void getRoomTypeNameAndFeatureSeqNo() {
        Connection con = getConnection();
        PreparedStatement stmt = null;

        try{
            stmt = con.prepareStatement("SELECT FEATURE_TYPE_NAME, FEATURE_SEQ_NO FROM FEATURE WHERE DELETE_STATUS <> 1");
            stmt.setEscapeProcessing(true);


            ResultSet rs = stmt.executeQuery();

            while(rs.next()) {
                String Feature_Type_Name = rs.getString(1);
                String Feature_Seq_No = rs.getString(2);

                cmbFeatureTypeNameForMaintenance.addItem(Feature_Type_Name + "|" + Feature_Seq_No);
            }
            closeConn(con, stmt);



        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(null, ex.getMessage());
        }



    }


}