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
import companydbmanagerant.model.TableModel.AllEditTableModel;
import companydbmanagerant.model.TableModel.EmployeeTableModel;
import companydbmanagerant.view.Main.EmployeeAddPanel;
import companydbmanagerant.view.Main.EmployeeEditAllPanel;
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
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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

     private Modal emplyoeeAllEditModal;
    private Modal emplyoeeEditModal;
    private Modal emplyoeeAddModal;
    private Modal emplyoeeDelModal;

    /**
     * Creates new form MainFrame
     */
    public DataView() {

        initComponents();
        queryBuilderForm = null;
        mainForm = new MainForm();
        loginForm = new LoginForm();
        addComboListeners();
        init();
    }

    public void setController(DataController controller) {
        this.controller = controller;
    }

    private void init() {
        setSize(new Dimension(1050, 530));
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

    public boolean updateEmployeeTable(EmployeeTableModel tableModel) {
        FormDashboard formDashboard = mainForm.getFormDashboard();
        formDashboard.getEmployeeTable().setModel(tableModel);

        List<String> selectedLabels = formDashboard.getSelectedCheckBoxLabels();
        if (selectedLabels.size() <= 1) {
            Notifications.getInstance().show(Notifications.Type.ERROR, 2000, "컬럼을 하나이상 선택하셔야 합니다.");
            tableModel.setActiveColumns(new ArrayList<String>());
            return false;
        } else {
            tableModel.setActiveColumns(selectedLabels);
            // 컬럼 설정과 에디터 설정 로직
            setTableColumnsAndEditors();
            // 알림 로직
            notifySuccess();
            return true;
        }

    }

    //일괄검색에 대한 콤보박스 아이템 수정 
    public void updateButtonState() {
        FormDashboard formDashboard = mainForm.getFormDashboard();
        formDashboard.updateButtonState();
        //임시로 넣음 
        formDashboard.getjTextField1().setText("");

    }

    public void updateSearchComboBox() {
        FormDashboard formDashboard = mainForm.getFormDashboard();
        setComboBox1ByActiveColumns(formDashboard.getSelectedCheckBoxLabels());
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
    public void notifySuccess() {
        Notifications.getInstance().show(Notifications.Type.SUCCESS, "DB 검색이 완료되었습니다.");
    }

    public void notifyUpdateSuccess(String msg) {
        Notifications.getInstance().show(Notifications.Type.SUCCESS, msg);
    }

    public void notifyUpdateFailed(String msg) {
        Notifications.getInstance().show(Notifications.Type.ERROR, msg);
    }

    // ==========================================================
    // EmployeeADD 버튼 관련 
    // ==========================================================
    public void addAddButtonListener(ActionListener listener) {
        FormDashboard formDashboard = mainForm.getFormDashboard();
        formDashboard.getEmployeeAddBtn().addActionListener(listener);
    }

    public void showAddDialog(List<Department> departments, List<String> SSNs, ActionListener listener, ActionListener listener2) {
        Map<String, ActionListener> buttonListeners = new HashMap<>();
        buttonListeners.put("exitBtnCircle", null);
        buttonListeners.put("executeBtn", listener);
        buttonListeners.put("checkValidBtn", listener2);
        emplyoeeAddModal = new Modal(this, new EmployeeAddPanel(departments, SSNs), buttonListeners);
    }

    public boolean checkValidAddDialog() {
        JComponent panel = emplyoeeAddModal.getModalPanel();
        Map<String, String> fieldTexts = new HashMap<>();
        if (panel instanceof EmployeeAddPanel) {
            EmployeeAddPanel employeeEditPanel = (EmployeeAddPanel) panel;

            if (employeeEditPanel.validateInput()) {
                return true;
            }
        }

        return false;
    }

    public void setSsnFieldError() {
        JComponent panel = emplyoeeAddModal.getModalPanel();
        Map<String, String> fieldTexts = new HashMap<>();
        if (panel instanceof EmployeeAddPanel) {
            EmployeeAddPanel employeeEditPanel = (EmployeeAddPanel) panel;
            employeeEditPanel.setBorderToError(employeeEditPanel.getTxtSSN());

        }
    }

    public void setEnabledAddExecuteBtn(boolean bool) {
        JComponent panel = emplyoeeAddModal.getModalPanel();
        Map<String, String> fieldTexts = new HashMap<>();
        if (panel instanceof EmployeeAddPanel) {
            EmployeeAddPanel employeeEditPanel = (EmployeeAddPanel) panel;
            employeeEditPanel.getjButton2().setEnabled(bool);
        }
    }

    // GUI 필드상의 정보를 리턴
    public Map<String, String> getAddPanelFieldTexts() {
        JComponent panel = emplyoeeAddModal.getModalPanel();
        Map<String, String> fieldTexts = new HashMap<>();
        if (panel instanceof EmployeeAddPanel) {
            EmployeeAddPanel employeeAddPanel = (EmployeeAddPanel) panel;
            // JComboBox의 선택된 아이템을 추출하고 그 값을 Map에 넣음
            fieldTexts.put("Dname", (String) employeeAddPanel.getComboDname().getSelectedItem());
            fieldTexts.put("SEX", (String) employeeAddPanel.getComboSEX().getSelectedItem());
            fieldTexts.put("SuperSSN", (String) employeeAddPanel.getComboSuperSSN().getSelectedItem());

            // JTextField의 텍스트를 추출하고 그 값을 Map에 넣음
            fieldTexts.put("Address", employeeAddPanel.getTxtAddress().getText());
            fieldTexts.put("Birth", employeeAddPanel.getTxtBirth().getText());
            fieldTexts.put("Fname", employeeAddPanel.getTxtFirstName().getText());
            fieldTexts.put("Lname", employeeAddPanel.getTxtLastName().getText());
            fieldTexts.put("Minit", employeeAddPanel.getTxtMinit().getText());
            fieldTexts.put("SSN", employeeAddPanel.getTxtSSN().getText());
            fieldTexts.put("Salary", employeeAddPanel.getTxtSalary().getText());
        }
        return fieldTexts;

    }

    public void whenEmployeeAddingSuccess() {
        //성공시 해야할것
        //1. notify 알림
        notifyUpdateSuccess("DB에 새로운 직원이 삽입되었습니다.");
        emplyoeeAddModal.getGlassPane().setVisible(false);
        //2. ....
    }

    public void whenEmployeeAddingFailed() {
        //실패시 해야할것
        //1. notify 알림
        notifyUpdateFailed("DB에 새로운 직원이 삽입에 실패하였습니다.");
        //2. ....
    }

    // ==========================================================
    // EmployeeDel 버튼 관련 
    // ==========================================================
    public void addDelButtonListener(ActionListener listener) {
        FormDashboard formDashboard = mainForm.getFormDashboard();
        formDashboard.getEmployeeDelBtn().addActionListener(listener);
    }

    public void showDelDialog(Employee employee) {

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

    public void showEditDialog(Employee employee, List<Department> departments, List<String> notSubordinates, ActionListener listener) {
        Map<String, ActionListener> buttonListeners = new HashMap<>();
        buttonListeners.put("exitBtnCircle", null);
        buttonListeners.put("executeBtn", listener);
        emplyoeeEditModal = new Modal(this, new EmployeeEditPanel(employee, departments, notSubordinates), buttonListeners);
    }

    public Map<String, String> getEditPanelFieldTexts() {
        JComponent panel = emplyoeeEditModal.getModalPanel();
        Map<String, String> fieldTexts = new HashMap<>();
        if (panel instanceof EmployeeEditPanel) {
            EmployeeEditPanel employeeEditPanel = (EmployeeEditPanel) panel;
            // JComboBox의 선택된 아이템을 추출하고 그 값을 Map에 넣음
            fieldTexts.put("Dname", (String) employeeEditPanel.getComboDname().getSelectedItem());
            fieldTexts.put("SEX", (String) employeeEditPanel.getComboSEX().getSelectedItem());
            fieldTexts.put("SuperSSN", (String) employeeEditPanel.getComboSuperSSN().getSelectedItem());

            // JTextField의 텍스트를 추출하고 그 값을 Map에 넣음
            fieldTexts.put("Address", employeeEditPanel.getTxtAddress().getText());
            fieldTexts.put("Birth", employeeEditPanel.getTxtBirth().getText());
            fieldTexts.put("Fname", employeeEditPanel.getTxtFirstName().getText());
            fieldTexts.put("Lname", employeeEditPanel.getTxtLastName().getText());
            fieldTexts.put("Minit", employeeEditPanel.getTxtMinit().getText());
            fieldTexts.put("SSN", employeeEditPanel.getTxtSSN().getText());
            fieldTexts.put("Salary", employeeEditPanel.getTxtSalary().getText());
        }
        return fieldTexts;

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
        Map<String, ActionListener> buttonListeners = new HashMap<>();
        buttonListeners.put("exitBtn", closeBtnListener);
        Modal modal = new Modal(this, queryBuilderForm, buttonListeners);
    }
    // ==========================================================
    // 테이블 내의 검색 관련 
    // ==========================================================

    public void addTableSearchButtonListener(ActionListener listener) {
        FormDashboard formDashboard = mainForm.getFormDashboard();
        formDashboard.getTableSearchBtn().addActionListener(listener);
    }

    public void setTableSearchPanelEnabled(boolean bool) {
        FormDashboard formDashboard = mainForm.getFormDashboard();
        formDashboard.setTableSearchPanelEnabled(bool);

    }

    public void setComboBox1ByActiveColumns(List<String> selectedLabels) {
        List<String> modifySelectedLabels;
        if (selectedLabels.size() > 1) { // 리스트에 최소 두 개 이상의 요소가 있는지 확인.
            modifySelectedLabels = selectedLabels.subList(1, selectedLabels.size());

            FormDashboard formDashboard = mainForm.getFormDashboard();
            JComboBox comboBox1 = formDashboard.getjComboBox1();
            DataViewUtil.setComboBoxItems(comboBox1, modifySelectedLabels);
        }
    }

    public void addComboListeners() {
        FormDashboard formDashboard = mainForm.getFormDashboard();
        JComboBox fieldCombo = formDashboard.getjComboBox1();
        JComboBox operationCombo = formDashboard.getjComboBox2();
        JComboBox valueOptionsComboBox = formDashboard.getjComboBox3();
        JTextField valueField = formDashboard.getjTextField1();

        fieldCombo.addActionListener(e -> {
            Object items = fieldCombo.getSelectedItem();
            if (items != null) {
                String selectedItem = items.toString();
                boolean useComboBoxForValue = "Sex".equals(selectedItem);

                valueOptionsComboBox.setVisible(useComboBoxForValue);
                valueField.setVisible(!useComboBoxForValue);

                updateComboBoxBasedOnSelection(operationCombo, valueOptionsComboBox, selectedItem);
                updateTextFieldBasedOnSelection(valueField, selectedItem);
            }

        });

    }

    public void handleComboBoxSelectionChanged(JComboBox<String> comboBox1, JComboBox<String> comboBox2, JComboBox<String> comboBox3, JTextField jTextField1) {
        String selectedItem = comboBox1.getSelectedItem().toString();
        updateComboBoxBasedOnSelection(comboBox2, comboBox3, selectedItem);
        updateTextFieldBasedOnSelection(jTextField1, selectedItem);
    }

    private void updateTextFieldBasedOnSelection(JTextField textField, String selectedItem) {
        boolean isVisible = !("Sex".equals(selectedItem));
        DataViewUtil.setTextFieldVisible(textField, isVisible);
    }

    private void updateComboBoxBasedOnSelection(JComboBox<String> comboBox1, JComboBox<String> comboBox2, String selectedItem) {
        // 첫 번째 콤보 박스에 대한 아이템 설정
        List<String> newItemsForComboBox1 = generateItemsForComboBox1(selectedItem);
        DataViewUtil.setComboBoxItems(comboBox1, newItemsForComboBox1);
        //DataViewUtil.setComboBoxVisible(comboBox1, !newItemsForComboBox1.isEmpty());

        // 두 번째 콤보 박스에 대한 아이템 설정
        List<String> newItemsForComboBox2 = generateItemsForComboBox2(selectedItem);
        DataViewUtil.setComboBoxItems(comboBox2, newItemsForComboBox2);
        DataViewUtil.setComboBoxVisible(comboBox2, !newItemsForComboBox2.isEmpty());
    }

    private List<String> generateItemsForComboBox1(String selectedItem) {
        List<String> newItems = new ArrayList<>();
        switch (selectedItem) {
            case "First Name":
            case "Minit":
            case "Last Name":
            case "Address":
            case "Dname":
            case "SSN":
            case "Super SSN":
                newItems.add("=");
                newItems.add("!=");
                newItems.add("Contain");
                break;

            case "Sex":
                newItems.add("=");
                newItems.add("!=");
                break;
            case "Birth Date":
            case "Salary":

                newItems.add("=");
                newItems.add(">");
                newItems.add(">=");
                newItems.add("<");
                newItems.add("<=");
                newItems.add("!=");
                break;
            default:

        }
        return newItems;
    }

    private List<String> generateItemsForComboBox2(String selectedItem) {
        List<String> newItems = new ArrayList<>();
        switch (selectedItem) {
            case "Sex":
                newItems.add("F");
                newItems.add("M");
                break;

        }
        return newItems;
    }

    public Map<String, String> getSearchConditon() {
        Map<String, String> condition = new HashMap<>();
        FormDashboard formDashboard = mainForm.getFormDashboard();
        JComboBox fieldCombo = formDashboard.getjComboBox1();
        JComboBox operationCombo = formDashboard.getjComboBox2();
        JComboBox valueOptionsComboBox = formDashboard.getjComboBox3();
        JTextField valueField = formDashboard.getjTextField1();
        Object items = fieldCombo.getSelectedItem();
        if (items != null) {
            String selectedItem = items.toString();
            boolean useComboBoxForValue = "Sex".equals(selectedItem);
            condition.put("field", (String) fieldCombo.getSelectedItem());
            condition.put("operation", (String) operationCombo.getSelectedItem());

            if (!useComboBoxForValue) {
                condition.put("value", (String) valueField.getText());
            } else {
                condition.put("value", (String) valueOptionsComboBox.getSelectedItem());
            }
        }
        return condition;
    }

    // ==========================================================
    // 일괄 수정 패널 관련
    // ==========================================================
    public void showEditAllDialog(AllEditTableModel allEditTableModel, List<Department> departments, List<String> notSubordinates, ActionListener listener) {
        Map<String, ActionListener> buttonListeners = new HashMap<>();
        buttonListeners.put("exitBtnCircle", null);
        buttonListeners.put("executeBtn", listener);
        List<String> activecolumns = new ArrayList<String>();
        activecolumns.add("Full Name");
        activecolumns.add("SSN");
        activecolumns.add("Fname");
        activecolumns.add("-> 변경될 내용");
        allEditTableModel.setActiveColumns(activecolumns);
        emplyoeeAllEditModal = new Modal(this, new EmployeeEditAllPanel(allEditTableModel, departments, notSubordinates), buttonListeners);
    }

    public void addAllEditButtonListener(ActionListener listener) {

        FormDashboard formDashboard = mainForm.getFormDashboard();
        formDashboard.getAllEditBtn().addActionListener(listener);

    }

    public void whenEmployeeAllAddingSuccess() {
        //성공시 해야할것
        //1. notify 알림
        emplyoeeAllEditModal.getGlassPane().setVisible(false);
        //2. ....
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

        pack();
        setVisible(true);

  
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                JTextField textField = loginForm.getTxtPass(); 
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
