/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package companydbmanagerant.controller;

import companydbmanagerant.model.DataModel;
import companydbmanagerant.model.Department.Department;
import companydbmanagerant.model.Employee.Employee;
import companydbmanagerant.model.Employee.EmployeeTableModel;
import companydbmanagerant.view.DataViewUtil;
import companydbmanagerant.view.DataView;
import companydbmanagerant.view.Login.LoginForm;
import companydbmanagerant.view.Main.FormDashboard;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.SwingWorker;
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
        formDashboard.addCheckBoxListeners(createCheckBoxItemListener());
        formDashboard.addjComboBox1Listener(new addjComboBox1Listener());
        formDashboard.addjComboBox2Listener(new addjComboBox2Listener());

        //LoginForm 리스너 관리
        this.view.getLoginForm().addCmdLoginListener(new addCmdLoginListener());

        //콤보박스 초기화
        handleComboBoxSelectionChanged("전체");
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

    //콤보박스1 리스너 정의
    class addjComboBox1Listener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            JComboBox<String> comboBox = view.getMainForm().getFormDashboard().getjComboBox1();
            JComboBox<String> comboBox2 = view.getMainForm().getFormDashboard().getjComboBox2();
            JComboBox<String> comboBox3 = view.getMainForm().getFormDashboard().getjComboBox3();
            JTextField textfield1 = view.getMainForm().getFormDashboard().getjTextField1();

            String selected = (String) comboBox.getSelectedItem();
            handleComboBoxSelectionChanged(selected);

        }
    }

    //콤보박스2 리스너 정의
    class addjComboBox2Listener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            JComboBox<String> comboBox = view.getMainForm().getFormDashboard().getjComboBox2();
            String selected = (String) comboBox.getSelectedItem();
        }
    }

    //콤보박스3 리스너 정의
    class addjComboBox3Listener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            JComboBox<String> comboBox = view.getMainForm().getFormDashboard().getjComboBox3();
            String selected = (String) comboBox.getSelectedItem();
        }
    }

    //DB불러오기 버튼 리스너 정의
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
                    return model.tryLogin(id, pass, dbname,url);
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
            JComboBox<String> comboBox1 = view.getMainForm().getFormDashboard().getjComboBox1();
            JComboBox<String> comboBox2 = view.getMainForm().getFormDashboard().getjComboBox2();
            JComboBox<String> comboBox3 = view.getMainForm().getFormDashboard().getjComboBox3();
            JTextField textfield1 = view.getMainForm().getFormDashboard().getjTextField1();

            String condition1 = (String) comboBox1.getSelectedItem();
            String condition2 = (String) comboBox2.getSelectedItem();
            String condition3 = (String) comboBox3.getSelectedItem();
            String searchText = (String) textfield1.getText();

            // Where절 쿼리 생성 
            String condition = SQLQueryBuilder.createEmpolyeeWhereClause(condition1, condition2, condition3, searchText);

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

            //검색완료 알림 
            Notifications.getInstance().show(Notifications.Type.SUCCESS, "DB 검색이 완료되었습니다.");
        }
    }
    
    
//-------------------------------------------------------
//-- FormDashboard의 콤보박스 상호작용 로직 --------------
//-------------------------------------------------------
    public void handleComboBoxSelectionChanged(String selectedItem) {
        FormDashboard formdashboard = view.getMainForm().getFormDashboard();
        updateComboBoxBasedOnSelection(formdashboard.getjComboBox2(), formdashboard.getjComboBox3(), selectedItem);
        updateTextFieldBasedOnSelection(formdashboard.getjTextField1(), selectedItem);
    }

    private void updateTextFieldBasedOnSelection(JTextField textField, String selectedItem) {
        boolean isVisible = !("전체".equals(selectedItem) || "Sex".equals(selectedItem) || "Dname".equals(selectedItem));
        DataViewUtil.setTextFieldVisible(textField, isVisible);
    }

    private void updateComboBoxBasedOnSelection(JComboBox<String> comboBox1, JComboBox<String> comboBox2, String selectedItem) {
        // 첫 번째 콤보 박스에 대한 아이템 설정
        List<String> newItemsForComboBox1 = generateItemsForComboBox1(selectedItem);
        DataViewUtil.setComboBoxItems(comboBox1, newItemsForComboBox1);
        DataViewUtil.setComboBoxVisible(comboBox1, !newItemsForComboBox1.isEmpty());

        // 두 번째 콤보 박스에 대한 아이템 설정
        List<String> newItemsForComboBox2 = generateItemsForComboBox2(selectedItem);
        DataViewUtil.setComboBoxItems(comboBox2, newItemsForComboBox2);
        DataViewUtil.setComboBoxVisible(comboBox2, !newItemsForComboBox2.isEmpty());
    }

    private List<String> generateItemsForComboBox1(String selectedItem) {
        List<String> newItems = new ArrayList<>();
        switch (selectedItem) {
            case "전체":

                break;
            case "First Name":
            case "Minit":
            case "Last Name":
            case "SSN":
            case "Address":
            case "Sex":
            case "Super SSN":
            case "Dname":
                newItems.add("=");
                newItems.add("!=");
                break;
            case "Birth Date":
                newItems.add("=");
                newItems.add(">");
                newItems.add("<");
                break;
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
            case "Dname":
                model.loadDepartmentsData();
                List<Department> departments = model.getDepartments();
                for (Department department : departments) {
                    newItems.add(department.getDname());
                }
                break;
            // ... (필요한 경우 추가 case 구문을 여기에 배치할 수 있습니다.)
        }
        return newItems;
    }

}
