/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package companydbmanagerant.controller;

import companydbmanagerant.model.SQLQueryBuilder;
import com.formdev.flatlaf.FlatClientProperties;
import companydbmanagerant.model.DataModel;
import companydbmanagerant.model.Department.Department;
import companydbmanagerant.model.Employee.Employee;
import companydbmanagerant.model.Employee.EmployeeDAO;
import companydbmanagerant.model.LoginFormDataDTO;
import companydbmanagerant.model.TableModel.EmployeeTableModel;

import companydbmanagerant.view.DataViewUtil;
import companydbmanagerant.view.DataView;
import companydbmanagerant.view.Login.LoginForm;
import companydbmanagerant.view.Main.EmployeeAddPanel;
import companydbmanagerant.view.Main.EmployeeEditPanel;
import companydbmanagerant.view.Main.FormDashboard;
import companydbmanagerant.view.Main.QueryBuilderForm.NestedQueryBuilderScrollPane;
import companydbmanagerant.view.Main.QueryBuilderForm.QueryBuilderForm;

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
import java.util.Map;
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

        //로그인버튼 리스너 주입 
        this.view.getLoginForm().addCmdLoginListener(new CmdLoginListener());

        //직원 삭제,추가,수정 리스너 주입
        this.view.addDelButtonListener(new DelButtonListener(this));
        this.view.addAddButtonListener(new AddButtonListener(this));
        this.view.addEditButtonListener(new EditButtonListener(this));

        //DB 갱신버튼 리스너 주입
        this.view.addRetrieveDBButtonListener(new RetrieveDBButtonListener());

        //검색필터버튼 리스너 주입
        this.view.addAddFilterBtnListener(new AddFilterBtnListener(this));

    }

    // ==========================================================
    // 로그인폼 관련 
    // ==========================================================
    private class CmdLoginListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            executeLogin();
        }
    }

    private void executeLogin() {

        LoginFormDataDTO logindata = view.getLoginFormTextFieldsText();

        // 로그인시도시 JDBC_URL이 틀리면 창랙이 너무 심해서 백그라운드 수행으로 바꾸었음
        // 다른 트랙젝션은 굳이 백그라운드에서 실행 할 필요 없음
        // SwingWorker를 사용하여 데이터베이스 로그인 시도를 백그라운드에서 수행
        SwingWorker<Boolean, Void> worker = new SwingWorker<Boolean, Void>() {
            @Override
            protected Boolean doInBackground() {
                // 백그라운드에서 데이터베이스 로그인 시도
                // 'model.tryLogin(id, pass, url)'는 실제 로그인 메서드 호출을 나타냅니다.
                return model.tryLogin(logindata);
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

    // ================================================
    // DB 불러오기 버튼 관련 (CONTROLL 단)
    // ================================================
    private class RetrieveDBButtonListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            retrieveDB();
        }
    }

    private void retrieveDB() {
        // 여기에서 데이터 처리를 수행하고 뷰를 업데이트하기 위한 메소드 호출
        String condition = SQLQueryBuilder.createWhereClauseIfNotEmpty(view.getQueryCondition());
        model.buildEmployeeTableModel(condition);
        view.updateEmployeeTable(model.getEmployeeTableModel());
    }

    // ================================================
    // EmployeeADD 버튼 관련  (CONTROLL 단)
    // ================================================
    class AddButtonListener implements ActionListener {

        private DataController controller;

        public AddButtonListener(DataController controller) {
            this.controller = controller;
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            controller.openAddDialog(); // Controller의 메소드를 호출합니다.
        }
    }

    public void openAddDialog() {

        model.loadDepartmentsData();
        List<Department> departments = model.getDepartments();

        String query = SQLQueryBuilder.createFindSSNsQuery();
        List<String> SSNs = model.findNotSubordinates(query);

        view.showAddDialog(departments, SSNs, new ExecuteAddButtonListener(this));

    }

    // 2. DB 변경 수행 관련 정의
    class ExecuteAddButtonListener implements ActionListener {

        private DataController controller;

        public ExecuteAddButtonListener(DataController controller) {
            this.controller = controller;
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            controller.executeAdd(); // Controller의 메소드를 호출합니다.
        }
    }

    public void executeAdd() {
        //Employee 정보 갱신 관련 작성 

        Map<String, String> AddEmployee = view.getAddPanelFieldTexts();

        //모델단에서 트렌젝션 실행 
        boolean updateSuccess = model.addEmployeeInfo(AddEmployee);

        // 데이터베이스 업데이트 결과를 확인하고 적절한 처리를 수행할 수 있습니다.
        if (updateSuccess) {
            //성공시 model단에서 해야할거 호출(ex, 테이블 다시불러오기)

            retrieveDB();
            //성공시 View단에서 해야할거 호출(ex 성공알림, modal창 닫기)            
            view.whenEmployeeAddingSuccess();

        } else {
            //실패시 View단에서 해야할거 호출(ex 실패알림 ) 
            view.whenEmployeeAddingFailed();
        }
    }

    // ================================================
    // EmployeeDel 버튼 관련  (CONTROLL 단)
    // ================================================
    class DelButtonListener implements ActionListener {

        private DataController controller;

        public DelButtonListener(DataController controller) {
            this.controller = controller;
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            controller.executeDelete();
        }
    }

    public void executeDelete() {
        //Employee 정보 갱신 관련 작성 
        int selectedRow = view.getSelectedRow(); // View를 통해 선택된 행 가져오기
        if (selectedRow >= 0) {
            model.refreshSelectedEmployee(selectedRow);
            Employee selectedEmployee = model.getSelectedEmployee();
            if (selectedEmployee != null) {
                boolean deleteSuccess = EmployeeDAO.deleteEmployee(selectedEmployee);
                if (deleteSuccess) {
                    // 업데이트 성공

                    retrieveDB();//테이블 갱신
                    view.notifyUpdateSuccess("데이터 삭제 완료");
                } else {
                    // 업데이트 실패
                    view.notifyUpdateFailed("데이터 삭제 실패");
                }

            }

        }

    }
//    public void openDelDialog() {
//        int selectedRow = view.getSelectedRow(); // View를 통해 선택된 행 가져오기
//        if (selectedRow >= 0) {
//            Employee selectedEmployee = model.getEmployeeAt(selectedRow);
//            if (selectedEmployee != null) {
//
//                view.showDelDialog(selectedEmployee); // View를 통해 대화상자 표시
//            }
//        }
//    }

    // ================================================
    // EmployeeEdit 버튼 관련  (CONTROLL 단)
    // ================================================
    // 1. 버튼 여는 파트
    class EditButtonListener implements ActionListener {

        private DataController controller;

        public EditButtonListener(DataController controller) {
            this.controller = controller;
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            controller.executeOpenEditDialog(); // Controller의 메소드를 호출합니다.
        }
    }

    public void executeOpenEditDialog() {
        int selectedRow = view.getSelectedRow(); // View를 통해 선택된 행 가져오기
        System.out.println(selectedRow);
        if (selectedRow >= 0) {
            model.refreshSelectedEmployee(selectedRow);
            Employee selectedEmployee = model.getSelectedEmployee();
            if (selectedEmployee != null) {
                model.loadDepartmentsData(); // Model을 통해 데이터 로딩
                List<Department> departments = model.getDepartments();
                String query = SQLQueryBuilder.createFindNotSubordinatesQuery(selectedEmployee.getSsn());
                List<String> notSubordinates = model.findNotSubordinates(query);
                view.showEditDialog(selectedEmployee, departments, notSubordinates, new ExecuteEditButtonListener(this)); // View를 통해 대화상자 표시
            }
        }
    }

    // 2. DB 변경 수행 관련 정의
    class ExecuteEditButtonListener implements ActionListener {

        private DataController controller;

        public ExecuteEditButtonListener(DataController controller) {
            this.controller = controller;
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            controller.executeEdit(); // Controller의 메소드를 호출합니다.
        }
    }

    public void executeEdit() {
        //Employee 정보 갱신 관련 작성 
        Employee previousEmployee = model.getSelectedEmployee();
        Map<String, String> EditedEmployee = view.getEditPanelFieldTexts();
        boolean updateSuccess = model.updateEmployeeInfo(previousEmployee, EditedEmployee);

        // 데이터베이스 업데이트 결과를 확인하고 적절한 처리를 수행할 수 있습니다.
        if (updateSuccess) {
            // 업데이트 성공
            view.notifyUpdateSuccess("데이터베이스 업데이트 성공!");
            System.out.println("");
        } else {
            // 업데이트 실패
            view.notifyUpdateFailed("데이터베이스 업데이트 실패");
        }
    }

    // ================================================
    // Filter 버튼 관련 (CONTROLL 단)
    // ================================================
    class AddFilterBtnListener implements ActionListener {

        private DataController controller;

        public AddFilterBtnListener(DataController controller) {
            this.controller = controller;
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            controller.handleAddFilterAction();
        }
    }

    public void handleAddFilterAction() {
        QueryBuilderForm form = view.getQueryBuilderForm();
        if (form == null) {
            List<String> departments = model.loadDepartmentsList();
            view.initQueryBuilderForm(departments);
        }
        view.displayQuerybuilderform();
    }

//-------------------------------------------------------
//기타 리스너 정의-------------------------------------------
//-------------------------------------------------------
    public ItemListener createCheckBoxItemListener() { //현재 미사용
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

}
