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
import java.awt.Component;
import java.awt.Container;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Map;
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
    private final JComponent modalPanel;
//    private final String exitBtnName;
//    private final boolean setBtnCircle;

    Map<String, ActionListener> buttonListeners;

    ActionListener additionalAction;

    public Modal(JFrame frame, JComponent modalPane, Map<String, ActionListener> buttonListeners) {
        this.mainFrame = frame;
        this.modalPanel = modalPane;

        this.buttonListeners = buttonListeners;

        this.glassPane = createGlassPane();

//                this.exitBtnName = exitButtonName;
//        this.additionalAction = additionalAction;
//        this.setBtnCircle = setBtnCircle;
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

    public JComponent getGlassPane(){
        return glassPane;
    }
    
    private void initialize() {
        setupGlassPane();
        setupModalPanel();
        setupActionListners();
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

    private void setupActionListners() {
        for (Map.Entry<String, ActionListener> entry : buttonListeners.entrySet()) {
            String buttonName = entry.getKey();
            ActionListener listener = entry.getValue();

            if ("exitBtn".equalsIgnoreCase(buttonName) || "exitBtnCircle".equalsIgnoreCase(buttonName)) {
                setupExitButtonActionListener(buttonName, listener);
            } else {
                setupActionListener(buttonName, listener);
            }

        }
    }

    private void setupActionListener(String buttonName, ActionListener listener) {
        if (listener == null) {
            return;
        }
        JButton findedButton = findButtonByName(modalPanel, buttonName);

        if (findedButton != null) {
            findedButton.addActionListener(listener);
        }
    }

    private void setupExitButtonActionListener(String buttonName, ActionListener listener) {
        JButton findedButton = findButtonByName(modalPanel, buttonName);
        if (findedButton != null) {
            findedButton.addActionListener(e -> glassPane.setVisible(false));

            if (listener != null) {
                findedButton.addActionListener(listener);
            }

            // 버튼을 원형으로 만드는 스타일 적용
            if ("exitBtnCircle".equalsIgnoreCase(buttonName)) {
                findedButton.putClientProperty(FlatClientProperties.STYLE, "arc: 40;");
            }
        }
    }

    public JFrame getMainFrame() {
        return mainFrame;
    }

    public JComponent getModalPanel() {
        return modalPanel;
    }

//    private void setupExitButton(ActionListener additionalAction) {
//        buttonListeners JButton exitButton = findButtonByName(modalPanel, exitBtnName);
//        if (exitButton != null) {
//            // 기존의 ActionListener를 추가
//            exitButton.addActionListener(e -> glassPane.setVisible(false));
//
//            // 추가적인 ActionListener를 등록
//            if (additionalAction != null) {
//                exitButton.addActionListener(additionalAction);
//            }
//
//            // 버튼을 원형으로 만드는 스타일 적용
//            if (setBtnCircle) {
//                exitButton.putClientProperty(FlatClientProperties.STYLE, "arc: 40;");
//            }
//        }
//    }
    private void preventUnderlyingInteractions() {
        glassPane.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                evt.consume();
            }
        });
    }

    private JButton findButtonByName(Container container, String name) {
        for (Component comp : container.getComponents()) {
            if (comp instanceof JButton && name.equals(comp.getName())) {
                return (JButton) comp;
            } else if (comp instanceof Container) {
                JButton button = findButtonByName((Container) comp, name);
                if (button != null) {
                    return button;
                }
            }
        }
        return null;
    }
//    private JButton findButtonByName(String name) {
//        for (java.awt.Component comp : modalPanel.getComponents()) {
//            if (comp instanceof JButton && name.equals(comp.getName())) {
//                return (JButton) comp;
//            }
//        }
//        return null;
//    }
}
