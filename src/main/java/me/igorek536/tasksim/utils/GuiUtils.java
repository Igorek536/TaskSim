package me.igorek536.tasksim.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.*;
import java.awt.*;

public class GuiUtils {
    private static final Logger logger = LoggerFactory.getLogger("GuiUtils");

    public static void setlaf(String laf) {
        try {
            for (UIManager.LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
                if (laf.equals(info.getName())) {
                    UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (Exception e) {
            logger.error("LookAndFeel " + laf + " not found!", e);
        }
    }

    public static void updateComponentsUi(Component... components) {
        for (Component component : components) {
            SwingUtilities.updateComponentTreeUI(component);
        }
    }

    public static boolean isWindowIconified() {
        boolean result = false;
        for (Frame frame : JFrame.getFrames()) {
            result = frame.getExtendedState() == JFrame.ICONIFIED;
        }
        return result;
    }

    /**
     * Example:
     *         setJtableColWidth(table1, new int[]{1, 5}, new int[]{2, 5})
     *         set 5 width for col with index 1 and 2
     *
     *
     * @param table - JTable's object. Should be not null!
     * @param colWidth - int[column index, width]
     */
    public static void setJtableColWidth(JTable table, int[]... colWidth) {
        for (int[] a : colWidth) {
            if (a.length == 2) {
                if (a[1] != 0) {
                    table.getColumnModel().getColumn(a[0]).setMinWidth(a[1]);
                    table.getColumnModel().getColumn(a[0]).setPreferredWidth(a[1]);

                }
            }
        }
    }
}
