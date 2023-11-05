/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JPanel.java to edit this template
 */
package companydbmanagerant.view.Main;

import companydbmanagerant.view.Main.*;
import com.formdev.flatlaf.FlatClientProperties;
import com.formdev.flatlaf.ui.FlatUIUtils;
import companydbmanagerant.model.Department.Department;
import companydbmanagerant.model.Employee.Employee;
import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;
import javax.swing.JOptionPane;
import raven.toast.Notifications;

import java.awt.Color;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import javax.swing.JButton;
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
public class EmployeeAddPanel extends javax.swing.JPanel {

    Employee employee;
    List<Department> departments;
    List<String> Employees;

    /**
     * Creates new form EmployeeEditPanel
     */
    public EmployeeAddPanel(List<Department> departments, List<String> Employees) {
        initComponents();
        this.Employees = Employees;
        this.employee = employee;
        this.departments = departments;
        jButton1.setName("exitBtnCircle");
        // TextField와 ComboBox에 Employee 객체의 값을 설정
        jButton2.setEnabled(false);
        jButton2.setName("executeBtn");
        jButton3.setName("checkValidBtn");
        setDepartmentCombo();
        setSuperSSNCombo();
        setEmployeeValues();
    }

    public JComboBox<String> getComboDname() {
        return comboDname;
    }

    public JComboBox<String> getComboSEX() {
        return comboSEX;
    }

    public JComboBox<String> getComboSuperSSN() {
        return comboSuperSSN;
    }

    public JTextField getTxtAddress() {
        return txtAddress;
    }

    public JTextField getTxtBirth() {
        return txtBirth;
    }

    public JTextField getTxtFirstName() {
        return txtFirstName;
    }

    public JTextField getTxtLastName() {
        return txtLastName;
    }

    public JTextField getTxtMinit() {
        return txtMinit;
    }

    public JTextField getTxtSSN() {
        return txtSSN;
    }

    public JTextField getTxtSalary() {
        return txtSalary;
    }

    public JButton getjButton2() {
        return jButton2;
    }

    
    
    private void setDepartmentCombo() {
        comboDname.removeAllItems();
        for (Department department : departments) {
            comboDname.addItem(department.getDname());
        }
    }

    private void setSuperSSNCombo() {
        comboSuperSSN.removeAllItems();
        for (String superssn : Employees) {
            comboSuperSSN.addItem(superssn);
        }
    }

    private void setEmployeeValues() {
        //플레이스홀더 만들어야함
        txtAddress.setText("");
        txtBirth.setText("");
        txtFirstName.setText("");
        txtLastName.setText("");
        txtMinit.setText("");
        txtSSN.setText("");
        txtSalary.setText("");
        addDocumentListenerToResetBorder(txtAddress);
        addDocumentListenerToResetBorder(txtBirth);
        addDocumentListenerToResetBorder(txtFirstName);
        addDocumentListenerToResetBorder(txtLastName);
        addDocumentListenerToResetBorder(txtMinit);
        addDocumentListenerToResetBorder(txtSSN);
        addDocumentListenerToResetBorder(txtSalary);
        addItemListenerToResetBorder(comboDname);
        addItemListenerToResetBorder(comboSEX);
        addItemListenerToResetBorder(comboSuperSSN);


    }

    public void setBorderToError(JComponent component) {
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

    public boolean validateInput() { //무결성 검사 

        boolean isValidate = true;
        // Reset borders to default
        resetBorderToDefault(txtAddress);
        resetBorderToDefault(txtBirth);
        resetBorderToDefault(txtFirstName);
        resetBorderToDefault(txtLastName);
        resetBorderToDefault(txtMinit);
        resetBorderToDefault(txtSSN);
        resetBorderToDefault(txtSalary);
        // Address validation
        if (txtAddress.getText().length() > 30) {
            Notifications.getInstance().show(Notifications.Type.ERROR, 2000, "Address is too long");
            setBorderToError(txtAddress);
            isValidate = false;
        } else {
            setBorderToValid(txtAddress);
        }

        // Birthdate validation
        if (!txtBirth.getText().matches("^\\d{4}-\\d{2}-\\d{2}$")) {
            Notifications.getInstance().show(Notifications.Type.ERROR, 2000, "Invalid birthdate format(must be yyyy-MM-dd)");
            setBorderToError(txtBirth);
            isValidate = false;
        } else {
            setBorderToValid(txtBirth);
        }

        // First name validation
        if (txtFirstName.getText().isEmpty()) {
            Notifications.getInstance().show(Notifications.Type.ERROR, 2000, "First name cannot be empty");
            setBorderToError(txtFirstName);
            isValidate = false;
        } else if (txtFirstName.getText().length() > 15) {
            Notifications.getInstance().show(Notifications.Type.ERROR, 2000, "First name is too long");
            setBorderToError(txtFirstName);
            isValidate = false;
        } else {
            setBorderToValid(txtFirstName);
        }

        // Last name validation
        if (txtLastName.getText().isEmpty()) {
            Notifications.getInstance().show(Notifications.Type.ERROR, 2000, "Last name cannot be empty");
            setBorderToError(txtLastName);
            isValidate = false;
        } else if (txtLastName.getText().length() > 15) {
            Notifications.getInstance().show(Notifications.Type.ERROR, 2000, "Last name is too long");
            setBorderToError(txtLastName);
            isValidate = false;
        } else {
            setBorderToValid(txtLastName);
        }

        // Minit validation
        if (!txtMinit.getText().matches("^[A-Z]$")) {
            Notifications.getInstance().show(Notifications.Type.ERROR, 2000, "Minit must be a single uppercase English letter");
            setBorderToError(txtMinit);
            isValidate = false;
        } else {
            setBorderToValid(txtMinit);
        }

        // SSN validation
        if (txtSSN.getText().length() != 9 || !txtSSN.getText().matches("\\d{9}")) {
            Notifications.getInstance().show(Notifications.Type.ERROR, 2000, "Invalid SSN");
            setBorderToError(txtSSN);
            isValidate = false;
        } else {
            setBorderToValid(txtSSN);
        }

        // Salary validation
        if (!txtSalary.getText().matches("^\\d*(\\.\\d{1,2})?$")) {
            Notifications.getInstance().show(Notifications.Type.ERROR, 2000, "Salary must be a number with up to two decimal places");
            setBorderToError(txtSalary);
            isValidate = false;
        } else {
            setBorderToValid(txtSalary);
        }

        // Additional validations can be added for combo boxes, depending on their possible values
        // 콤보박스는 모두 삽입할때 예외처리를해서 안전함( 다른 사용자에 의해서 바뀌지 않는이상 - 이를 체크해야할지 )
        setBorderToValid(comboSEX);
        setBorderToValid(comboDname);
        setBorderToValid(comboSuperSSN);
        return isValidate;
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jButton1 = new javax.swing.JButton();
        txtFirstName = new javax.swing.JTextField();
        txtSSN = new javax.swing.JTextField();
        txtBirth = new javax.swing.JTextField();
        txtAddress = new javax.swing.JTextField();
        txtSalary = new javax.swing.JTextField();
        jButton2 = new javax.swing.JButton();
        jLabel1 = new javax.swing.JLabel();
        txtMinit = new javax.swing.JTextField();
        jLabel2 = new javax.swing.JLabel();
        txtLastName = new javax.swing.JTextField();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();
        jLabel11 = new javax.swing.JLabel();
        comboDname = new javax.swing.JComboBox<>();
        comboSEX = new javax.swing.JComboBox<>();
        comboSuperSSN = new javax.swing.JComboBox<>();
        jLabel7 = new javax.swing.JLabel();
        jButton3 = new javax.swing.JButton();

        jButton1.setText("X");

        txtFirstName.setText("jTextField1");
        txtFirstName.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtFirstNameActionPerformed(evt);
            }
        });

        txtSSN.setText("jTextField1");

        txtBirth.setText("jTextField1");

        txtAddress.setText("jTextField1");

        txtSalary.setText("jTextField1");

        jButton2.setText("직원 정보 추가");
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });

        jLabel1.setText("First Name");

        txtMinit.setText("A");

        jLabel2.setText("Minit");

        txtLastName.setText("jTextField1");
        txtLastName.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtLastNameActionPerformed(evt);
            }
        });

        jLabel3.setText("Last Name");

        jLabel4.setText("SSN");

        jLabel5.setText("Birth Date");

        jLabel6.setText("Address");

        jLabel8.setText("Super SSN");

        jLabel9.setText("Salary");

        jLabel10.setText("SEX");

        jLabel11.setText("Dname");

        comboDname.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));

        comboSEX.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "F", "M" }));

        comboSuperSSN.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));

        jLabel7.setFont(new java.awt.Font("맑은 고딕", 0, 14)); // NOI18N
        jLabel7.setText("직원 정보 추가 ");

        jButton3.setText("무결성 검사");
        jButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton3ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(23, 23, 23)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(txtAddress)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel6)
                        .addGap(324, 324, 324))
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(jLabel5)
                                    .addComponent(txtBirth, javax.swing.GroupLayout.PREFERRED_SIZE, 163, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(txtSalary, javax.swing.GroupLayout.DEFAULT_SIZE, 191, Short.MAX_VALUE)
                                    .addGroup(layout.createSequentialGroup()
                                        .addComponent(jLabel9)
                                        .addGap(0, 0, Short.MAX_VALUE))))
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                .addComponent(jButton3, javax.swing.GroupLayout.PREFERRED_SIZE, 118, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jButton2, javax.swing.GroupLayout.PREFERRED_SIZE, 118, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addGroup(layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(txtFirstName, javax.swing.GroupLayout.PREFERRED_SIZE, 163, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jLabel1))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(jLabel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(txtMinit, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel3)
                                    .addComponent(txtLastName, javax.swing.GroupLayout.PREFERRED_SIZE, 163, javax.swing.GroupLayout.PREFERRED_SIZE)))
                            .addGroup(layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(txtSSN, javax.swing.GroupLayout.PREFERRED_SIZE, 163, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jLabel4))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(layout.createSequentialGroup()
                                        .addComponent(jLabel10)
                                        .addGap(0, 0, Short.MAX_VALUE))
                                    .addComponent(comboSEX, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                .addComponent(jLabel7)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(jButton1))))
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel11)
                            .addComponent(comboDname, javax.swing.GroupLayout.PREFERRED_SIZE, 191, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jLabel8)
                                .addGap(114, 114, 114))
                            .addComponent(comboSuperSSN, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
                .addContainerGap(39, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jButton1)
                        .addGap(11, 11, 11))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addComponent(jLabel7)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)))
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel3)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txtLastName, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                        .addGroup(layout.createSequentialGroup()
                            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(jLabel2)
                                .addComponent(jLabel1))
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addComponent(txtMinit, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addComponent(txtFirstName, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel4)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(txtSSN, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(comboSEX, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel10)
                        .addGap(38, 38, 38)))
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel5)
                    .addComponent(jLabel9))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtBirth, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtSalary, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel6)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(txtAddress, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel11)
                    .addComponent(jLabel8))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(comboDname, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(comboSuperSSN, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButton2, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton3, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(38, Short.MAX_VALUE))
        );
    }// </editor-fold>//GEN-END:initComponents

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed

    }//GEN-LAST:event_jButton2ActionPerformed

    private void txtFirstNameActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtFirstNameActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtFirstNameActionPerformed

    private void txtLastNameActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtLastNameActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtLastNameActionPerformed

    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton3ActionPerformed

    }//GEN-LAST:event_jButton3ActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JComboBox<String> comboDname;
    private javax.swing.JComboBox<String> comboSEX;
    private javax.swing.JComboBox<String> comboSuperSSN;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton3;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JTextField txtAddress;
    private javax.swing.JTextField txtBirth;
    private javax.swing.JTextField txtFirstName;
    private javax.swing.JTextField txtLastName;
    private javax.swing.JTextField txtMinit;
    private javax.swing.JTextField txtSSN;
    private javax.swing.JTextField txtSalary;
    // End of variables declaration//GEN-END:variables
}
