/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package companydbmanagerant.view.Modal;

/**
 *
 * @author PHC
 */

import com.formdev.flatlaf.FlatClientProperties;
import com.formdev.flatlaf.FlatLaf;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextField;

/**
 *
 * @author PHC
 */
public class Modal {

    private final JComponent glassPane;
    private final JFrame mainFrame;
    private final JPanel modalPanel;
    private final String exitBtnName;

    public Modal(JFrame frame, JPanel modalPane, String exitButtonName) {
        this.mainFrame = frame;
        this.modalPanel = modalPane;
        this.exitBtnName = exitButtonName;
        this.glassPane = createGlassPane();
        initialize();
    }

    private JComponent createGlassPane() {
        return new JComponent() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                g.setColor(new Color(0, 0, 0, 100));
                g.fillRect(0, 0, getWidth(), getHeight());
            }
        };
    }

    private void initialize() {
        setupGlassPane();
        setupModalPanel();
        setupExitButton();
        preventUnderlyingInteractions();
        
        mainFrame.setGlassPane(glassPane);
        glassPane.setVisible(true);
        adjustComboBoxPopups();
    }
    private void adjustComboBoxPopups() {  // 모달패널 뒤에 콤보박스가 뜨는 현상 제거 
    for (java.awt.Component comp : modalPanel.getComponents()) {
        if (comp instanceof JComboBox) {
            JComboBox comboBox = (JComboBox) comp;
            comboBox.setLightWeightPopupEnabled(false);
        }
    }
}
    private void setupGlassPane() {
        glassPane.setLayout(null);
        glassPane.add(modalPanel);
        modalPanel.setBounds(
            (mainFrame.getWidth() - modalPanel.getPreferredSize().width) / 2,
            (mainFrame.getHeight() - modalPanel.getPreferredSize().height) / 2,
            modalPanel.getPreferredSize().width,
            modalPanel.getPreferredSize().height
        );
    }

    private void setupModalPanel() {
        modalPanel.putClientProperty(FlatClientProperties.STYLE,
            "[light]background: tint(@background,50%);"
            + "[dark]background: shade(@background,15%);"
            + "[light]border: 16,16,16,16,shade(@background,10%),,8;"
            + "[dark]border: 16,16,16,16,tint(@background,10%),,8");
    }

    private void setupExitButton() {
        JButton exitButton = findButtonByName(exitBtnName);
        if (exitButton != null) {
            exitButton.addActionListener(e -> glassPane.setVisible(false));
            exitButton.putClientProperty(FlatClientProperties.STYLE, "arc: 40;");
        }
    }
    
    private void preventUnderlyingInteractions() {
        glassPane.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                evt.consume();
            }
        });
    }

    private JButton findButtonByName(String name) {
        for (java.awt.Component comp : modalPanel.getComponents()) {
            if (comp instanceof JButton && name.equals(comp.getName())) {
                return (JButton) comp;
            }
        }
        return null;
    }
}
