package nl.Databeest.TabItems.Room.Index;

import nl.Databeest.Helpers.*;
import nl.Databeest.TabItems.IndexAbstractTableModel;
import nl.Databeest.TabItems.SubMenuItem;

import javax.swing.*;
import javax.swing.table.TableCellRenderer;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 * Created by timok on 15-12-16.
 */
public class Index extends SubMenuItem{
    private JPanel mainPanel;
    private JTable roomIndexTable;
    private JButton btnRefreshRooms;

    @Override
    protected String getMenuItemName() {
        return "Index";
    }

    @Override
    protected Component getMainPanel() {
        return mainPanel;
    }

    public Index(){
        setTable();
        btnRefreshRooms.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                refreshRooms();
            }
        });
    }

    private void setTable(){
        getRooms();

        if(RoleHelper.isDeleter()) {
            TableCellRenderer buttonRenderer = new JTableButtonRenderer();
            roomIndexTable.getColumn("Delete").setCellRenderer(buttonRenderer);
            roomIndexTable.addMouseListener(new JTableButtonMouseListener(roomIndexTable));
        }
    }

    private void refreshRooms() {
        setTable();
    }

    public void getRooms() {
        Connection con = getConnection();
        PreparedStatement stmt = null;


        try {
            stmt = con.prepareStatement("SELECT ROOM_ID, ROOM_NO, ROOM_TYPE.NAME FROM ROOM INNER JOIN ROOM_TYPE ON ROOM_TYPE.ROOM_TYPE_ID = ROOM.ROOM_TYPE_ID  WHERE ROOM.END_TIME IS NULL");
            stmt.setEscapeProcessing(true);

            roomIndexTable.setModel(new IndexAbstractTableModel(stmt.executeQuery()) {
                @Override
                public void deleteRow(Object[] row) {
                    deleteRoom(Integer.parseInt((String) row[0]));
                }
            });

            closeConn(con, stmt);
        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(null, ex.getMessage());
        }
    }

    private void deleteRoom(int roomId){
        Connection con = getConnection();
        PreparedStatement stmt = null;


        try {
            stmt = con.prepareStatement("EXEC SP_DELETE_ROOM ?,?");
            stmt.setEscapeProcessing(true);

            stmt.setInt(1, roomId);
            stmt.setInt(2, User.getInstance().getUserId());


            stmt.execute();

            closeConn(con, stmt);
        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(null, ex.getMessage());
        }

        setTable();
    }
}
