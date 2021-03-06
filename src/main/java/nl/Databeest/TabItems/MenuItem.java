package nl.Databeest.TabItems;


import javax.swing.*;
import java.util.ArrayList;

/**
 * Created by timok on 17-11-16.
 */
public abstract class MenuItem extends JPanel {

    public MenuItem(){
        createSideMenu();
    }

    protected abstract ArrayList<SubMenuItem> getMenuItems();

    private void createSideMenu(){

        JTabbedPane tabbedPane = new JTabbedPane();

        for (SubMenuItem subMenuItem : getMenuItems()) {
            tabbedPane.addTab(subMenuItem.getMenuItemName(), subMenuItem.getMainPanel());
        }

        //Add the tabbed pane to this panel.
        add(tabbedPane);

        tabbedPane.setTabLayoutPolicy(JTabbedPane.SCROLL_TAB_LAYOUT);

        //The following line enables to use scrolling tabs.
        tabbedPane.setTabPlacement( JTabbedPane.LEFT );
    }
}
