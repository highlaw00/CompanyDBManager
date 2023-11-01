/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package companydbmanagerant.view.Main.TableModel;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.OverlayLayout;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableCellRenderer;

/**
 *
 * @author PHC
 */
public class CustomCellRenderer extends DefaultTableCellRenderer {

    private EmployeeTableModel model;

    public CustomCellRenderer(EmployeeTableModel model) {
        this.model = model;
    }

    @Override
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
        super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);


        if (model.isEdited(row, column)) {
            setBorder(new RoundedBorder(Color.BLUE, 10, 10));
        } else if (model.isDeleted(row, column)) {
            setBorder(new RoundedBorder(Color.RED, 10, 10));
        } else {
            // 기본 설정
            
            //setBorder(BorderFactory.createEmptyBorder());
        }

        setText(String.valueOf(value));

        return this;
    }
}
