/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package companydbmanagerant.view;

import companydbmanagerant.view.Main.MainForm;
import companydbmanagerant.view.Login.LoginForm;
import com.formdev.flatlaf.FlatDarkLaf;
import com.formdev.flatlaf.FlatLaf;
import com.formdev.flatlaf.FlatLightLaf;
import companydbmanagerant.controller.DataController;
import companydbmanagerant.model.Department.Department;
import companydbmanagerant.model.Employee.Employee;
import companydbmanagerant.model.LoginFormDataDTO;
import companydbmanagerant.model.SQLQueryBuilder;
import companydbmanagerant.model.TableModel.EmployeeTableModel;
import companydbmanagerant.view.Main.EmployeeAddPanel;
import companydbmanagerant.view.Main.EmployeeEditPanel;
import companydbmanagerant.view.Main.FormDashboard;
import companydbmanagerant.view.Main.QueryBuilderForm.QueryBuilderForm;

import companydbmanagerant.view.Modal.Modal;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FontMetrics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemListener;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import javax.swing.BorderFactory;
import javax.swing.DefaultCellEditor;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import raven.toast.Notifications;

/**
 *
 * @author PHC
 */
public class DataView extends javax.swing.JFrame {

    private final MainForm mainForm;
    private final LoginForm loginForm;
    private QueryBuilderForm queryBuilderForm;
    private DataController controller;

    /**
     * Creates new form MainFrame
     */
    public DataView() {

        initComponents();
        queryBuilderForm = null;
        mainForm = new MainForm();
        loginForm = new LoginForm();

        init();
    }

    public void setController(DataController controller) {
        this.controller = controller;
    }

    private void init() {
        setSize(new Dimension(1050, 600));
        setContentPane(loginForm);
        Notifications.getInstance().setJFrame(this);
        FlatLaf.updateUI();
    }

    // ==========================================================
    // 로그인 버튼 관련(VIEW 단)
    // ==========================================================
    public LoginFormDataDTO getLoginFormTextFieldsText() {
        String id = loginForm.getTxtUser().getText();
        char[] pass = loginForm.getTxtPass().getPassword();
        String url = loginForm.getTxtURL().getText();
        String dbname = loginForm.getTxtDB().getText();

        return new LoginFormDataDTO(id, pass, url, dbname);
    }

    // ==========================================================
    // DB 불러오기 버튼 관련(VIEW 단)
    // ==========================================================
    public void addRetrieveDBButtonListener(ActionListener listener) {
        FormDashboard formDashboard = mainForm.getFormDashboard();
        formDashboard.getRetrieveDBBtn().addActionListener(listener);
    }

    public String getQueryCondition() {
        if (queryBuilderForm != null) {
            return queryBuilderForm.getRootNode().toSQL();
        }
        return "";
    }

    public void updateEmployeeTable(EmployeeTableModel tableModel) {
        FormDashboard formDashboard = mainForm.getFormDashboard();
        formDashboard.getEmployeeTable().setModel(tableModel);
        tableModel.setActiveColumns(formDashboard.getSelectedCheckBoxLabels());
        // 컬럼 설정과 에디터 설정 로직
        setTableColumnsAndEditors();
        // 알림 로직
        notifySuccess();
    }

    private List<String> getSelectedCheckBoxLabels() {
        FormDashboard formDashboard = mainForm.getFormDashboard();
        return formDashboard.getSelectedCheckBoxLabels();
    }

    private void setTableColumnsAndEditors() {
        // 컬럼 및 에디터 설정
        FormDashboard formDashboard = mainForm.getFormDashboard();

        formDashboard.getEmployeeTable().setDefaultEditor(Object.class, new DefaultCellEditor(new JTextField()) {
            @Override
            public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
                Component c = super.getTableCellEditorComponent(table, value, isSelected, row, column);
                if (c instanceof JComponent) {
                    ((JComponent) c).setBorder(BorderFactory.createLineBorder(new Color(136, 119, 141)));
                    c.setBackground(new Color(136, 119, 141));
                }
                return c;
            }
        });
    }

    // ==========================================================
    // 노티 관련 
    // ==========================================================

    
    
    private void notifySuccess() {
        Notifications.getInstance().show(Notifications.Type.SUCCESS, "DB 검색이 완료되었습니다.");
    }

    // ==========================================================
    // EmployeeADD 버튼 관련 
    // ==========================================================
    public void addAddButtonListener(ActionListener listener) {
        FormDashboard formDashboard = mainForm.getFormDashboard();
        formDashboard.getEmployeeAddBtn().addActionListener(listener);
    }

    public void showAddDialog(List<Department> departments, List<String> SSNs) {
        Modal modal = new Modal(this, new EmployeeAddPanel(departments, SSNs), "exitBtn", true, null);
    }

    // ==========================================================
    // EmployeeDel 버튼 관련 
    // ==========================================================
    public void addDelButtonListener(ActionListener listener) {
        FormDashboard formDashboard = mainForm.getFormDashboard();
        formDashboard.getEmployeeDelBtn().addActionListener(listener);
    }

    public void showDelDialog(Employee employee) {
//       .. Delete에 대한 모달폼을 띄워야함 .. 
//   ex Modal modal = new Modal(this, new EmployeeDelPanel(employee), "exitBtn", true, null);
//      EmployeeDelPanel는 아직 정의 안되어 있고 
    }

    // ==========================================================
    // EmployeeEdit 버튼 관련 
    // ==========================================================
    public int getSelectedRow() {
        FormDashboard formDashboard = mainForm.getFormDashboard();
        return formDashboard.getEmployeeTable().getSelectedRow();
    }

    public void addEditButtonListener(ActionListener listener) {
        FormDashboard formDashboard = mainForm.getFormDashboard();
        formDashboard.getEmployeeEditBtn().addActionListener(listener);
    }

    public void showEditDialog(Employee employee, List<Department> departments, List<String> notSubordinates) {
        Modal modal = new Modal(this, new EmployeeEditPanel(employee, departments, notSubordinates), "exitBtn", true, null);
    }

    // ==========================================================
    // Filter버튼 관련 
    // ==========================================================
    public void addAddFilterBtnListener(ActionListener listner) {
        FormDashboard formDashboard = mainForm.getFormDashboard();
        JButton filterBtn = formDashboard.getFilterBtn();
        filterBtn.addActionListener(listner);
    }

    public void displayQuerybuilderform() {
        if (queryBuilderForm == null) {
            return;
        }
        ActionListener closeBtnListener = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                FormDashboard formDashboard = getMainForm().getFormDashboard();
                JButton filterBtn = formDashboard.getFilterBtn();
                int filtercnt = getQueryBuilderForm().getFiltercnt();
                if (filtercnt == 0) {
                    filterBtn.setText("Filters");
                } else {
                    filterBtn.setText("Filters (" + String.valueOf(filtercnt) + ")");

                }

            }
        };

        Modal modal = new Modal(this, queryBuilderForm, "exitBtn", false, closeBtnListener); //리스너 주입
    }

    // ==========================================================
    // 쿼리 빌더 폼 관련 
    // ==========================================================
    public QueryBuilderForm getQueryBuilderForm() {
        return queryBuilderForm;
    }

    public void initQueryBuilderForm(List<String> departments) {
        if (queryBuilderForm == null) {
            queryBuilderForm = new QueryBuilderForm(departments);
        }
    }

    // ================================================
    // ================================================
    // ================================================
    public MainForm getMainForm() {
        return mainForm;
    }

    public LoginForm getLoginForm() {
        return loginForm;
    }

    public void showMainForm() {
        setContentPane(mainForm);
        FlatLaf.updateUI();
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("CompanyDBManager");
        setBackground(new java.awt.Color(255, 255, 255));
        setResizable(false);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 1076, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 600, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    /**
     * @param args the command line arguments
     */
    public void showGUI() {
        // GUI 요소가 준비된 후에 텍스트 필드에 포커스를 설정합니다.
        pack();
        setVisible(true);

        // GUI 이벤트 처리 스레드에서 실행될 작업을 예약
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                JTextField textField = loginForm.getTxtPass(); // LoginForm 내부의 텍스트 필드 접근 메서드가 필요합니다.
                textField.requestFocusInWindow();
            }
        });
    }

    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        //FlatLightLaf.setup();

//        try {
//            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
//                if ("FlatLaf Dark".equals(info.getName())) {
//                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
//                    break;
//                }
//            }
//        } catch (ClassNotFoundException ex) {
//            java.util.logging.Logger.getLogger(DataView.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
//        } catch (InstantiationException ex) {
//            java.util.logging.Logger.getLogger(DataView.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
//        } catch (IllegalAccessException ex) {
//            java.util.logging.Logger.getLogger(DataView.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
//        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
//            java.util.logging.Logger.getLogger(DataView.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
//        }
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>

        /* Create and display the form */
        DataView dataView = new DataView();
        dataView.showGUI();
    }


    // Variables declaration - do not modify//GEN-BEGIN:variables
    // End of variables declaration//GEN-END:variables
}
