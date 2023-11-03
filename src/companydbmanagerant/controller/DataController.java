/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package companydbmanagerant.controller;

import com.formdev.flatlaf.FlatClientProperties;
import companydbmanagerant.model.DataModel;
import companydbmanagerant.model.Department.Department;
import companydbmanagerant.model.Employee.Employee;
import companydbmanagerant.view.Main.TableModel.EmployeeTableModel;
import companydbmanagerant.view.DataViewUtil;
import companydbmanagerant.view.DataView;
import companydbmanagerant.view.Login.LoginForm;
import companydbmanagerant.view.Main.EmployeeAddPanel;
import companydbmanagerant.view.Main.EmployeeEditPanel;
import companydbmanagerant.view.Main.FormDashboard;
import companydbmanagerant.view.Main.QueryBuilderForm.NestedQueryBuilderScrollPane;
import companydbmanagerant.view.Main.QueryBuilderForm.Querybuilderform;

import companydbmanagerant.view.Main.TableModel.CustomCellRenderer;
import companydbmanagerant.view.Modal.Modal;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.DefaultCellEditor;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.SwingWorker;
import javax.swing.table.TableModel;
import net.miginfocom.swing.MigLayout;
import raven.toast.Notifications;

/**
 *
 * @author PHC
 */
public class DataController {

    private final DataModel model;
    private final DataView view;

    public DataController(DataModel model, DataView view) {
        this.model = model;
        this.view = view;
        init();

    }

    public void init() {
        //MainForm -> Dashboard 리스너 관리
        FormDashboard formDashboard = this.view.getMainForm().getFormDashboard();
        formDashboard.addjButton1Listener(new addjButton1Listener());
        formDashboard.addjButton2Listener(new addjButton2Listener());
        formDashboard.addCheckBoxListeners(createCheckBoxItemListener());
        formDashboard.addEmployeeEditBtnListener(new addEmployeeEditBtnListener());
        formDashboard.addEmployeeAddBtnListener(new addEmployeeAddBtnListener());
        formDashboard.addEmployeeDelBtnListener(new addEmployeeDelBtnListener());
        formDashboard.addFilterBtnListener(new addFilterBtnListener());

        //LoginForm 리스너 관리
        this.view.getLoginForm().addCmdLoginListener(new addCmdLoginListener());

    }

//-------------------------------------------------------
//--리스너 정의-------------------------------------------
//-------------------------------------------------------
    public ItemListener createCheckBoxItemListener() {
        return new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                // 체크박스의 상태가 변경될 때 수행할 동작
                JCheckBox checkBox = (JCheckBox) e.getSource();
                boolean selected = checkBox.isSelected();
                //... 체크박스 상태 변경에 따른 UI로직 처리 ...
            }
        };
    }
    //로그인 버튼 리스너 정의
    class addCmdLoginListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            LoginForm loginForm = view.getLoginForm();
            String id = loginForm.getTxtUser().getText();
            char[] pass = loginForm.getTxtPass().getPassword();
            String url = loginForm.getTxtURL().getText();
            String dbname = loginForm.getTxtDB().getText();
            // 로그인시도시 JDBC_URL이 틀리면 창랙이 너무 심해서 백그라운드 수행으로 바꾸었음
            // 다른 트랙젝션은 굳이 백그라운드에서 실행 할 필요 없음

            // SwingWorker를 사용하여 데이터베이스 로그인 시도를 백그라운드에서 수행
            SwingWorker<Boolean, Void> worker = new SwingWorker<Boolean, Void>() {
                @Override
                protected Boolean doInBackground() {
                    // 백그라운드에서 데이터베이스 로그인 시도
                    // 'model.tryLogin(id, pass, url)'는 실제 로그인 메서드 호출을 나타냅니다.
                    return model.tryLogin(id, pass, dbname, url);
                }

                @Override
                protected void done() {
                    // 백그라운드 작업이 완료된 후 호출됩니다.
                    try {
                        // 로그인 시도 결과를 가져옵니다.
                        boolean success = get();
                        if (success) {
                            // 로그인 성공 알림 표시
                            Notifications.getInstance().clearAll();
                            Notifications.getInstance().show(Notifications.Type.SUCCESS, "데이터베이스 접속 성공");
                            view.showMainForm();
                        }
                    } catch (InterruptedException | ExecutionException e) {
                        // 예외 처리 (필요한 경우 오류 알림 표시)
                        Notifications.getInstance().show(Notifications.Type.ERROR, e.getMessage());

                    }
                }
            };

            // 백그라운드 작업 시작
            worker.execute();
        }
    }

    //DB불러오기 버튼 리스너 정의
    class addjButton1Listener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            model.loadEmployeesData();

            //GUI 읽어옴
//            JComboBox<String> comboBox1 = view.getMainForm().getFormDashboard().getjComboBox1();
//            JComboBox<String> comboBox2 = view.getMainForm().getFormDashboard().getjComboBox2();
//            JComboBox<String> comboBox3 = view.getMainForm().getFormDashboard().getjComboBox3();
//            JTextField textfield1 = view.getMainForm().getFormDashboard().getjTextField1();
//
//            String condition1 = (String) comboBox1.getSelectedItem();
//            String condition2 = (String) comboBox2.getSelectedItem();
//            String condition3 = (String) comboBox3.getSelectedItem();
//            String searchText = (String) textfield1.getText();
//
//            // Where절 쿼리 생성 
//            String condition = SQLQueryBuilder.createEmpolyeeWhereClause(condition1, condition2, condition3, searchText);
             String condition = "";
            Querybuilderform form =  view.getQuerybuilderform();
            if(form != null){
                String filterQuery = form.getRootNode().toSQL();
                condition = SQLQueryBuilder.createWhereClauseIfNotEmpty(filterQuery);
            }
 
           
            System.out.println(condition);

            // 쿼리 실행
            model.loadEmployeesDataFittered(condition);

            // 결과 조회
            List<Employee> employees = model.getEmployees();

            // 테이블 모델 생성
            EmployeeTableModel employeeTableModel = new EmployeeTableModel(employees);

            // 테이블 갱신
            JTable employeeTable = view.getMainForm().getFormDashboard().getEmployeeTable();
            employeeTable.setModel(employeeTableModel);
            List<String> columnsToDisplay = view.getMainForm().getFormDashboard().getSelectedCheckBoxLabels();
            employeeTableModel.setActiveColumns(columnsToDisplay);
//            employeeTable.setDefaultRenderer(Object.class, new CustomCellRenderer(employeeTableModel)); //
            employeeTable.setDefaultEditor(Object.class, new DefaultCellEditor(new JTextField()) {
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
            //검색완료 알림 
            Notifications.getInstance().show(Notifications.Type.SUCCESS, "DB 검색이 완료되었습니다.");
        }
    }

    class addjButton2Listener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
        }
    }

    class addEmployeeAddBtnListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {

            model.loadDepartmentsData();
            List<Department> departments = model.getDepartments();

            String query = SQLQueryBuilder.createFindSSNsQuery();
            List<String> SSNs = model.findNotSubordinates(query);
            Modal modal = new Modal(view, new EmployeeAddPanel(departments, SSNs), "exitBtn", true, null);

        }
    }

    class addEmployeeEditBtnListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            JTable EmployeeTable = view.getMainForm().getFormDashboard().getEmployeeTable();

            TableModel currentModel = EmployeeTable.getModel();
            if (!(currentModel instanceof EmployeeTableModel)) {
                // 모델이 EmployeeTableModel이 아닌 경우 함수를 종료
                return;
            }
            EmployeeTableModel tablemodel = (EmployeeTableModel) currentModel;

            int selectedRow = EmployeeTable.getSelectedRow();
            Employee selectedEmployee = tablemodel.getSelectedEmployee(selectedRow);
            if (selectedEmployee != null) {
                model.loadDepartmentsData();
                List<Department> departments = model.getDepartments();

                String query = SQLQueryBuilder.createFindNotSubordinatesQuery(selectedEmployee.getSsn());
                List<String> notSubordinates = model.findNotSubordinates(query);
                Modal modal = new Modal(view, new EmployeeEditPanel(selectedEmployee, departments, notSubordinates), "exitBtn", true, null);
            }
        }
    }

    class addFilterBtnListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {

            Querybuilderform form = view.getQuerybuilderform();
            if (form == null) {
                List<String> departments = model.loadDepartmentsList();
                view.setQuerybuilderform(new Querybuilderform(departments));
                form = view.getQuerybuilderform();
            }

            // 닫기 버튼에 대한 ActionListener 정의
            ActionListener closeBtnListener = new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    FormDashboard formDashboard = view.getMainForm().getFormDashboard();
                    JButton filterBtn = formDashboard.getFilterBtn();
                    int filtercnt = view.getQuerybuilderform().getFiltercnt();
                    if (filtercnt == 0) {
                        filterBtn.setText("Filters");
                    } else {
                        filterBtn.setText("Filters (" + String.valueOf(filtercnt) + ")");

                    }

                }
            };

            Modal modal = new Modal(view, form, "exitBtn", false, closeBtnListener); //리스너 주입
        }
    }

    class addEmployeeDelBtnListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {

        }
    }

}
