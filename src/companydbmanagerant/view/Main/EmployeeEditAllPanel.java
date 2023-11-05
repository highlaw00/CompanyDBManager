/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JPanel.java to edit this template
 */
package companydbmanagerant.view.Main;

import com.formdev.flatlaf.FlatClientProperties;
import com.formdev.flatlaf.ui.FlatUIUtils;
import companydbmanagerant.model.Department.Department;
import companydbmanagerant.model.Employee.Employee;
import companydbmanagerant.model.TableModel.AllEditTableModel;
import companydbmanagerant.view.DataViewUtil;
import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;
import javax.swing.JOptionPane;
import raven.toast.Notifications;

import java.awt.Color;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.ArrayList;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JTextField;
import javax.swing.border.LineBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

/**
 *
 * @author PHC
 */
public class EmployeeEditAllPanel extends javax.swing.JPanel {

    AllEditTableModel allEditTableModel;
    Employee employee;
    List<Department> departments;
    List<String> notSubordinates;

    /**
     * Creates new form EmployeeEditPanel
     */
    public EmployeeEditAllPanel(AllEditTableModel allEditTableModel, List<Department> departments, List<String> notSubordinates) {
        initComponents();
        this.notSubordinates = notSubordinates;
        this.allEditTableModel = allEditTableModel;
        EmployeeTable.setModel(allEditTableModel);
        this.departments = departments;
        jButton1.setName("exitBtnCircle");
        // TextField와 ComboBox에 Employee 객체의 값을 설정
        jButton2.setEnabled(false);
        jButton2.setName("executeBtn");
        addComboListeners();
        init();
        //setDepartmentCombo();
//        setSuperSSNCombo();
//        setEmployeeValues();
    }

    public JComboBox<String> getComboDname() {
        return comboBox3;
    }

    public JComboBox<String> getComboSEX() {
        return comboBox2;
    }

    private void setDepartmentCombo() {
        comboBox3.removeAllItems();
        for (Department department : departments) {
            comboBox3.addItem(department.getDname());
        }
    }

    public void init() {
        JComboBox fieldCombo = comboBox1;
        JComboBox operationCombo = comboBox2;
        JComboBox valueOptionsComboBox = comboBox3;
        JTextField valueField = textField1;
        String selectedItem = "Fname";
        updateTableByComboBox1(selectedItem);

        DataViewUtil.setComboBoxEnabled(operationCombo, selectedItem);

        boolean useComboBoxForValue = "Sex".equals(selectedItem) || "Dname".equals(selectedItem);

        valueOptionsComboBox.setVisible(useComboBoxForValue);
        valueField.setVisible(!useComboBoxForValue);

        updateComboBoxBasedOnSelection(operationCombo, valueOptionsComboBox, selectedItem);
        updateTextFieldBasedOnSelection(valueField, selectedItem);
    }

    public void addComboListeners() {

        JComboBox fieldCombo = comboBox1;
        JComboBox operationCombo = comboBox2;
        JComboBox valueOptionsComboBox = comboBox3;
        JTextField valueField = textField1;
        valueField.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                resetBorderToDefault(textField1);
                jButton2.setEnabled(false);
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                resetBorderToDefault(textField1);
                jButton2.setEnabled(false);
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                resetBorderToDefault(textField1);
                jButton2.setEnabled(false);
            }
        });
    


        valueOptionsComboBox.addActionListener(e -> {
            resetBorderToDefault(valueOptionsComboBox);
            resetBorderToDefault(valueField);
            jButton2.setEnabled(false);
        });

        operationCombo.addActionListener(e -> {
            resetBorderToDefault(valueOptionsComboBox);
            resetBorderToDefault(valueField);
            jButton2.setEnabled(false);
        });

        fieldCombo.addActionListener(e -> {
            resetBorderToDefault(valueOptionsComboBox);
            resetBorderToDefault(valueField);
            jButton2.setEnabled(false);

            Object items = fieldCombo.getSelectedItem();
            if (items != null) {
                String selectedItem = items.toString();
                updateTableByComboBox1(selectedItem);
                DataViewUtil.setComboBoxEnabled(operationCombo, selectedItem);
                boolean useComboBoxForValue = "Sex".equals(selectedItem) || "Dname".equals(selectedItem);

                valueOptionsComboBox.setVisible(useComboBoxForValue);
                valueField.setVisible(!useComboBoxForValue);

                updateComboBoxBasedOnSelection(operationCombo, valueOptionsComboBox, selectedItem);
                updateTextFieldBasedOnSelection(valueField, selectedItem);
            }

        });

    }

    public void updateTableByComboBox1(String selectedItem) {
        JComboBox fieldCombo = comboBox1;
        JComboBox operationCombo = comboBox2;
        JComboBox valueOptionsComboBox = comboBox3;
        JTextField valueField = textField1;

        List<String> activecolumns = new ArrayList<String>();
        activecolumns.add("Full Name");
        activecolumns.add("SSN");
        activecolumns.add(selectedItem);
        activecolumns.add("-> 변경될 내용");
        allEditTableModel.setActiveColumns(activecolumns);
        EmployeeTable.setModel(allEditTableModel);
    }

    public void handleComboBoxSelectionChanged(JComboBox<String> comboBox1, JComboBox<String> comboBox2, JComboBox<String> comboBox3, JTextField jTextField1) {
        String selectedItem = comboBox1.getSelectedItem().toString();
        updateComboBoxBasedOnSelection(comboBox2, comboBox3, selectedItem);
        updateTextFieldBasedOnSelection(jTextField1, selectedItem);
    }

    private void updateTextFieldBasedOnSelection(JTextField textField, String selectedItem) {
        boolean isVisible = !("Sex".equals(selectedItem) || "Dname".equals(selectedItem) || "Super_ssn".equals(selectedItem));
        DataViewUtil.setTextFieldVisible(textField, isVisible);
    }

    private void updateComboBoxBasedOnSelection(JComboBox<String> comboBox1, JComboBox<String> comboBox2, String selectedItem) {
        // 첫 번째 콤보 박스에 대한 아이템 설정
        List<String> newItemsForComboBox1 = generateItemsForComboBox1(selectedItem);
        DataViewUtil.setComboBoxItems(comboBox1, newItemsForComboBox1);
        //DataViewUtil.setComboBoxVisible(comboBox1, !(newItemsForComboBox1.size()==1));

        // 두 번째 콤보 박스에 대한 아이템 설정
        List<String> newItemsForComboBox2 = generateItemsForComboBox2(selectedItem);
        DataViewUtil.setComboBoxItems(comboBox2, newItemsForComboBox2);
        //DataViewUtil.setComboBoxVisible(comboBox2, !newItemsForComboBox2.isEmpty());

        boolean isVisible = ("Sex".equals(selectedItem) || "Dname".equals(selectedItem) || "Super_ssn".equals(selectedItem));
        DataViewUtil.setComboBoxVisible(comboBox2, isVisible);
    }

    private List<String> generateItemsForComboBox1(String selectedItem) {
        List<String> newItems = new ArrayList<>();
        switch (selectedItem) {
            case "Fname":
            case "Minit":
            case "Lname":
            case "Address":
            case "Dname":
            case "Super_ssn":
            case "Sex":
            case "Bdate":
                newItems.add("SET");
                break;

            case "Salary":
                newItems.add("SET");
                newItems.add("*=");
                newItems.add("+=");
                newItems.add("-=");
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
            case "Dname":
                for (Department department : departments) {
                    newItems.add(department.getDname());
                }
                break;
            case "Super_ssn":
                for (String superssn : notSubordinates) {
                    newItems.add(superssn);
                }
                break;
        }
        return newItems;
    }


    private void setBorderToError(JComponent component) {
        component.putClientProperty(FlatClientProperties.STYLE, ""
                + "outlineColor:$ToError.outlineColor;"
                + "focusedBorderColor:$ToError.focusedBorderColor;"
                + "arc:10");
    }

    private void setBorderToValid(JComponent component) {
        component.putClientProperty(FlatClientProperties.STYLE, ""
                + "outlineColor:$ToValid.outlineColor;"
                + "focusedBorderColor:$ToValid.focusedBorderColor;"
                + "arc:10");
    }

    private void resetBorderToDefault(JComponent component) {
        component.putClientProperty(FlatClientProperties.STYLE, ""
                + "outlineColor:$Component.outlineColor;"
                + "focusedBorderColor:$Component.focusedBorderColor;"
                + "arc:10");
    }

    private void addItemListenerToResetBorder(JComboBox<String> comboBox) {
        comboBox.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                if (e.getStateChange() == ItemEvent.SELECTED) {
                    resetBorderToDefault(comboBox);
                    jButton2.setEnabled(false);
                }
            }
        });
    }

    private void addDocumentListenerToResetBorder(JTextField textField) {
        textField.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                resetBorderToDefault(textField);
                jButton2.setEnabled(false);
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                resetBorderToDefault(textField);
                jButton2.setEnabled(false);
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                resetBorderToDefault(textField);
                jButton2.setEnabled(false);
            }
        });

    }

    private boolean validateInput() {
        JComboBox fieldCombo = comboBox1;
        JComboBox operationCombo = comboBox2;
        JComboBox valueOptionsComboBox = comboBox3;
        JTextField valueField = textField1;
        String selectedItem = comboBox1.getSelectedItem().toString();
        boolean isUseCombo = ("Sex".equals(selectedItem) || "Dname".equals(selectedItem) || "Super_ssn".equals(selectedItem));

        String field = fieldCombo.getSelectedItem().toString();
        String operation = operationCombo.getSelectedItem().toString();
        String value = "";
        if (isUseCombo) {
            Object Item = valueOptionsComboBox.getSelectedItem();
            if (Item != null) {
                value = Item.toString();
            } else {
                value = "";
            }
        } else {
            value = valueField.getText();
        }

        resetBorderToDefault(valueOptionsComboBox);
        resetBorderToDefault(valueField);

        Boolean isValidate = true;

        if (field.contains("Fname") || field.contains("Lname")) {
            if (value.equals("")) {
                Notifications.getInstance().show(Notifications.Type.ERROR, 2000, field + " cannot be empty");
                setBorderToError(valueField);
                isValidate = false;
            } else if (value.length() > 15) {
                Notifications.getInstance().show(Notifications.Type.ERROR, 2000, field + " is too long");
                setBorderToError(valueField);
                isValidate = false;
            } else {
                setBorderToValid(valueField);
            }
            //Minit
        } else if (field.contains("Minit")) {
            if (!value.matches("^[A-Z]$")) {
                Notifications.getInstance().show(Notifications.Type.ERROR, 2000, "Minit must be a single uppercase English letter");
                setBorderToError(valueField);
                isValidate = false;
            } else {
                setBorderToValid(valueField);
            }
        } else if (field.contains("Bdate")) {
            System.out.println(value);
            if (!value.matches("^\\d{4}-\\d{2}-\\d{2}$")) {
                Notifications.getInstance().show(Notifications.Type.ERROR, 2000, "Invalid birthdate format(must be yyyy-MM-dd)");
                setBorderToError(valueField);
                isValidate = false;
            } else {
                setBorderToValid(valueField);
            }
        } else if (field.contains("Address")) {
            if (value.length() > 30) {
                Notifications.getInstance().show(Notifications.Type.ERROR, 2000, "Address is too long");
                setBorderToError(valueField);
                isValidate = false;
            } else {
                setBorderToValid(valueField);
            }
        } else if (field.contains("Sex") || field.contains("Dname")) {
            setBorderToValid(valueOptionsComboBox);

        } else if (field.contains("Super_ssn")) {
            if (value.equals("")) {
                Notifications.getInstance().show(Notifications.Type.ERROR, 2000, "트리구조를 유지하면서, 변경이 가능한 직속상사가 존재하지 않습니다. ");
                setBorderToError(valueOptionsComboBox);
                isValidate = false;
            } else {
                setBorderToValid(valueOptionsComboBox);
            }
        } else if (field.contains("Salary")) {
            if (!operation.contains("*=")) {
                if (!value.matches("^\\d*(\\.\\d{1,2})?$")) {
                    Notifications.getInstance().show(Notifications.Type.ERROR, 2000, "Salary must be a number with up to two decimal places");
                    setBorderToError(valueField);
                    isValidate = false;
                } else {
                    setBorderToValid(valueField);
                }
            }else{
                 if (!value.matches("^\\d*(\\.\\d{1,4})?$")) {
                    Notifications.getInstance().show(Notifications.Type.ERROR, 2000, "Salary must be a number with up to four decimal places");
                    setBorderToError(valueField);
                    isValidate = false;
                } else {
                    setBorderToValid(valueField);
                }
            }


            
        }

        //        if (!txtSalary.getText().matches("^-?\\d*(\\.\\d{1,2})?$")) {
//            Notifications.getInstance().show(Notifications.Type.ERROR, 2000, "Salary must be a number with up to two decimal places");
//            setBorderToError(txtSalary);
//            isValidate = false;
//        } else {
//            setBorderToValid(txtSalary);
//        }
        return isValidate;
    }

//    private boolean validateInput() { //무결성 검사 
//
//        boolean isValidate = true;
//        // Reset borders to default
//        resetBorderToDefault(txtAddress);
//        resetBorderToDefault(txtBirth);
//        resetBorderToDefault(txtFirstName);
//        resetBorderToDefault(txtLastName);
//        resetBorderToDefault(txtMinit);
//        resetBorderToDefault(txtSSN);
//        resetBorderToDefault(txtSalary);
//        // Address validation
//        if (txtAddress.getText().length() > 30) {
//            Notifications.getInstance().show(Notifications.Type.ERROR, 2000, "Address is too long");
//            setBorderToError(txtAddress);
//            isValidate = false;
//        } else {
//            setBorderToValid(txtAddress);
//        }
//
//        // Birthdate validation
//        if (!txtBirth.getText().matches("^\\d{4}-\\d{2}-\\d{2}$")) {
//            Notifications.getInstance().show(Notifications.Type.ERROR, 2000, "Invalid birthdate format(must be yyyy-MM-dd)");
//            setBorderToError(txtBirth);
//            isValidate = false;
//        } else {
//            setBorderToValid(txtBirth);
//        }
//
//        // First name validation
//        if (txtFirstName.getText().isEmpty()) {
//            Notifications.getInstance().show(Notifications.Type.ERROR, 2000, "First name cannot be empty");
//            setBorderToError(txtFirstName);
//            isValidate = false;
//        } else if (txtFirstName.getText().length() > 15) {
//            Notifications.getInstance().show(Notifications.Type.ERROR, 2000, "First name is too long");
//            setBorderToError(txtFirstName);
//            isValidate = false;
//        } else {
//            setBorderToValid(txtFirstName);
//        }
//
//        // Last name validation
//        if (txtLastName.getText().isEmpty()) {
//            Notifications.getInstance().show(Notifications.Type.ERROR, 2000, "Last name cannot be empty");
//            setBorderToError(txtLastName);
//            isValidate = false;
//        } else if (txtLastName.getText().length() > 15) {
//            Notifications.getInstance().show(Notifications.Type.ERROR, 2000, "Last name is too long");
//            setBorderToError(txtLastName);
//            isValidate = false;
//        } else {
//            setBorderToValid(txtLastName);
//        }
//
//        // Minit validation
//        if (!txtMinit.getText().matches("^[A-Z]$")) {
//            Notifications.getInstance().show(Notifications.Type.ERROR, 2000, "Minit must be a single uppercase English letter");
//            setBorderToError(txtMinit);
//            isValidate = false;
//        } else {
//            setBorderToValid(txtMinit);
//        }
//
//        // SSN validation
//        if (txtSSN.getText().length() != 9 || !txtSSN.getText().matches("\\d{9}")) {
//            Notifications.getInstance().show(Notifications.Type.ERROR, 2000, "Invalid SSN");
//            setBorderToError(txtSSN);
//            isValidate = false;
//        } else {
//            setBorderToValid(txtSSN);
//        }
//        // Salary validation
//        if (!txtSalary.getText().matches("^-?\\d*(\\.\\d{1,2})?$")) {
//            Notifications.getInstance().show(Notifications.Type.ERROR, 2000, "Salary must be a number with up to two decimal places");
//            setBorderToError(txtSalary);
//            isValidate = false;
//        } else {
//            setBorderToValid(txtSalary);
//        }
//
//        // Additional validations can be added for combo boxes, depending on their possible values
//        // 콤보박스는 모두 삽입할때 예외처리를해서 안전함( 다른 사용자에 의해서 바뀌지 않는이상 - 이를 체크해야할지 )
//        setBorderToValid(comboSEX);
//        setBorderToValid(comboDname);
//        setBorderToValid(comboSuperSSN);
//        return isValidate;
//    }
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jButton1 = new javax.swing.JButton();
        textField1 = new javax.swing.JTextField();
        jButton2 = new javax.swing.JButton();
        comboBox3 = new javax.swing.JComboBox<>();
        comboBox2 = new javax.swing.JComboBox<>();
        jLabel7 = new javax.swing.JLabel();
        jButton3 = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        EmployeeTable = new javax.swing.JTable();
        comboBox1 = new javax.swing.JComboBox<>();

        jButton1.setText("X");

        jButton2.setText("직원 정보 일괄 수정");
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });

        comboBox3.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));

        comboBox2.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "F", "M" }));

        jLabel7.setFont(new java.awt.Font("맑은 고딕", 0, 14)); // NOI18N
        jLabel7.setText("직원 정보 일괄 수정");

        jButton3.setText("무결성 검사");
        jButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton3ActionPerformed(evt);
            }
        });

        EmployeeTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Full Name", "SNN", "변경할데이터", "변경후데이터"
            }
        ));
        jScrollPane1.setViewportView(EmployeeTable);

        comboBox1.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Fname", "Minit", "Lname", "Bdate", "Address", "Sex", "Salary", "Super_ssn", "Dname" }));

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addGap(14, 14, 14)
                        .addComponent(jLabel7)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jButton1)
                        .addGap(5, 5, 5))
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(jButton3, javax.swing.GroupLayout.PREFERRED_SIZE, 142, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButton2))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(11, 11, 11)
                        .addComponent(comboBox1, javax.swing.GroupLayout.PREFERRED_SIZE, 119, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(comboBox2, javax.swing.GroupLayout.PREFERRED_SIZE, 93, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(textField1, javax.swing.GroupLayout.PREFERRED_SIZE, 139, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(comboBox3, javax.swing.GroupLayout.PREFERRED_SIZE, 139, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 58, Short.MAX_VALUE)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButton1)
                    .addComponent(jLabel7))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 22, Short.MAX_VALUE)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 195, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(comboBox2, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(textField1, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(comboBox1, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(comboBox3, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(24, 24, 24)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButton3, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton2, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );
    }// </editor-fold>//GEN-END:initComponents

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed

    }//GEN-LAST:event_jButton2ActionPerformed

    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton3ActionPerformed
        if (validateInput()) {
            tableUpdate();
            jButton2.setEnabled(true);
        }
    }//GEN-LAST:event_jButton3ActionPerformed
    private void tableUpdate() {
        JComboBox fieldCombo = comboBox1;
        JComboBox operationCombo = comboBox2;
        JComboBox valueOptionsComboBox = comboBox3;
        JTextField valueField = textField1;
        String selectedItem = comboBox1.getSelectedItem().toString();
        boolean isUseCombo = ("Sex".equals(selectedItem) || "Dname".equals(selectedItem) || "Super_ssn".equals(selectedItem));

        String field = fieldCombo.getSelectedItem().toString();
        String operation = operationCombo.getSelectedItem().toString();
        String value = "";
        if (isUseCombo) {
            Object Item = valueOptionsComboBox.getSelectedItem();
            if (Item != null) {
                value = Item.toString();
            } else {
                value = "";
            }
        } else {
            value = valueField.getText();
        }
        allEditTableModel.callTableUpdate(field, operation, value);
        allEditTableModel.fireTableDataChanged();

    }


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JTable EmployeeTable;
    private javax.swing.JComboBox<String> comboBox1;
    private javax.swing.JComboBox<String> comboBox2;
    private javax.swing.JComboBox<String> comboBox3;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton3;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTextField textField1;
    // End of variables declaration//GEN-END:variables
}
