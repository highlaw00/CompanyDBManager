/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package companydbmanagerant.model;

import companydbmanagerant.model.Department.Department;
import companydbmanagerant.model.Department.DepartmentDAO;
import companydbmanagerant.model.Dependent.Dependent;
import companydbmanagerant.model.Dependent.DependentDAO;
import companydbmanagerant.model.Employee.EmployeeDAO;
import companydbmanagerant.model.Employee.Employee;
import companydbmanagerant.model.DeptLocations.DeptLocations;
import companydbmanagerant.model.DeptLocations.DeptLocationsDAO;
import companydbmanagerant.model.Employee.EmployeeUtils;
import companydbmanagerant.model.Project.Project;
import companydbmanagerant.model.Project.ProjectDAO;
import companydbmanagerant.model.TableModel.AllEditTableModel;
import companydbmanagerant.model.TableModel.EmployeeTableModel;
import companydbmanagerant.model.WorksOn.WorksOn;
import companydbmanagerant.model.WorksOn.WorksOnDAO;
import java.text.ParseException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import raven.toast.Notifications;

/**
 *
 * @author PHC
 *
 * DAO를 통합관리하는 클래스
 */
public class DataModel {

    // 데이터를 보관하는 리스트
    private List<Employee> employees;
    private List<Department> departments;
    private List<WorksOn> worksons;
    private List<DeptLocations> deptlocationss;
    private List<Project> projects;
    private List<Dependent> dependents;
    private Employee selectedEmployee;
    private Employee editingEmployee;
    List<Employee> checkedEmployees;
    // 테이블모델
    EmployeeTableModel employeeTableModel;
    AllEditTableModel allEditTableModel;

    private void TestCode() {
        // 테스트코드: EMPLOYEE
        loadEmployeesData();
        System.out.println("EMPLOYEE TEST");
        for (Employee e : employees) {
            System.out.println(e.toString());
        }

        // 테스트코드: DEPARTMENT
        loadDepartmentsData();
        System.out.println("\nDEPARTMENT TEST");
        for (Department d : departments) {
            System.out.println(d.toString());
        }

        // 테스트코드: WORKS_ON
        loadWorksOnData();
        System.out.println("\nWORKS_ON TEST");
        for (WorksOn w : worksons) {
            System.out.println(w.toString());
        }

        // 테스트코드: DEPT_LOCATIONS
        loadDeptLocationssOnData();
        System.out.println("\nDEPT_LOCATIONS TEST");
        for (DeptLocations dl : deptlocationss) {
            System.out.println(dl.toString());
        }

        // 테스트코드: PROJECT
        loadProjectsOnData();
        System.out.println("\nPROJECT TEST");
        for (Project p : projects) {
            System.out.println(p.toString());
        }

        // 테스트코드: DEPENDENT
        loadDepentdentsOnData();
        System.out.println("\nDEPENDENT TEST");
        for (Dependent dep : dependents) {
            System.out.println(dep.toString());
        }
    }

    public DataModel() {
        // 모델 객체가 생성될 때 데이터를 로드.
        //loadEmployeesData();
        //테스트코드
        //TestCode();

    }

    //======================================================================
    // Edit Employee & ADD Employee 관련 (MODEL 단)
    //======================================================================   
    public void refreshSelectedEmployee(int selectedRow) {
        selectedEmployee = getEmployeeAt(selectedRow);
    }

    public Employee getSelectedEmployee() {
        return selectedEmployee;
    }

    //=====================================================================
    // 직원 수정============================================================
    //=====================================================================
    public void refreshSelectedEmployee() {
        selectedEmployee = editingEmployee;
    }

    public boolean updateEmployeeInfo(Map<String, String> EditedEmployee) {
        boolean isSuccessful = false;
        try {
            //previousEmployee
            //전달할 객체 생성
            Employee employee = EmployeeUtils.createEmployeeFromMap(EditedEmployee);
            editingEmployee = employee;
            // Dname to Dno
            String key = "Dname";
            if (EditedEmployee.containsKey("Dname")) { // 키가 존재하는지 먼저 확인
                String dname = EditedEmployee.get(key);
                List<String> dnos = DepartmentDAO.findDnoByDname(dname);
                if (dnos.size() == 1) {
                    String dnoStr = dnos.get(0);
                    int dno = Integer.parseInt(dnoStr);
                    employee.setDno(dno);

                    Map<String, String> diff = EmployeeUtils.findDiffEmployeeInfo(selectedEmployee, employee);
                    System.out.println(diff.toString());
                    //쿼리 빌딩

                    //트렌젝션 수행 
                    if (!diff.isEmpty()) {
                        isSuccessful = EmployeeDAO.updateEmployee(selectedEmployee.getSsn(), diff);
                    } else {
                        Notifications.getInstance().show(Notifications.Type.ERROR, "변경된 사항이 없습니다.");

                        return false;
                    }
                }
            }

        } catch (ParseException ex) {
            Logger.getLogger(DataModel.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        } catch (NumberFormatException e) {
            return false;
        }

        return isSuccessful;
    }

    //=====================================================================
    // 직원 추가============================================================
    //=====================================================================
    public boolean addEmployeeInfo(Map<String, String> AddEmployee) {
        boolean isSuccessful = false;
        try {
            //전달할 객체 생성
            Employee employee = EmployeeUtils.createEmployeeFromMap(AddEmployee);
            System.out.println(employee.toString());
            // Dname to Dno
            String key = "Dname";
            if (AddEmployee.containsKey("Dname")) { // 키가 존재하는지 먼저 확인
                String dname = AddEmployee.get(key);
                List<String> dnos = DepartmentDAO.findDnoByDname(dname);
                if (dnos.size() == 1) {
                    String dnoStr = dnos.get(0);
                    int dno = Integer.parseInt(dnoStr);
                    employee.setDno(dno);

                    //트렌젝션 수행 
                    isSuccessful = EmployeeDAO.insertEmployee(employee);
                }
            }

        } catch (ParseException ex) {
            Logger.getLogger(DataModel.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        } catch (NumberFormatException e) {
            return false;
        }

        return isSuccessful;
    }

    //=====================================================================
    // 직원 삭제============================================================
    //=====================================================================
    public boolean deleteEmployee(Employee selectedEmployee) {
        return EmployeeDAO.deleteEmployee(selectedEmployee);
    }

    //=====================================================================
    // 테이블에서 조건문으로 직원 찾기==================================================
    //=====================================================================  
    public void checkSelectedByCondition(Map<String, String> condition) {
        employeeTableModel.selectedByCondition(condition);
    }

    //=====================================================================
    // 직원 일괄변경========================================================
    //=====================================================================
    public List<Employee> getCheckedEmployees() {
        return checkedEmployees;
    }

    //체크된 모든사람의 부하직원 또는 부하직원의부하직원의.. 가 아닌 직원의 SSN 찾기
    public List<String> findNotAllSubordinates(List<Employee> checkedEmployees) {
        return EmployeeDAO.findNotAllSubordinates(checkedEmployees);
    }

    public boolean editAllSelectedEmloyee() {
        AllEditTableModel md = allEditTableModel;
        if (md.getRowCount() <= 0) {
            return false;
        }
        String columnName = md.getColumnName(2);
        Map<String, String> datas = new HashMap<>();
        
        for (int i = 0; i < md.getRowCount(); i++) {
            String ssn = (String)md.getValueAt(i, 1);
            String value = (String)md.getValueAt(i, 3);
            datas.put(ssn, value);
        }
        
        boolean isSuccess = EmployeeDAO.editAllSelectedEmloyee(columnName,datas);

        return isSuccess;
    }

//    public boolean modifyEmployeesInfo(List<Employee> beEditedEmployee, String WhatTodo, String value) {
//
//        
//        if(WhatTodo.equals( "직원성별일괄변경"))
//        {
//            EmployeeDAO.allsexChange(value);
//            
//        }
//        return true;
////        try {
////        //전달할 객체 생성
////            Employee employee = EmployeeUtils.createEmployeeFromMap(EditedEmployee);
////            boolean isSuccessful = EmployeeDAO.insertEmployee(employee);
////            
////        } catch (ParseException ex) {
////            Logger.getLogger(DataModel.class.getName()).log(Level.SEVERE, null, ex);
////        }
////        
////        return true;
//    }
    //======================================================================
    // 로그인 관련 관련 (MODEL 단)
    //======================================================================   
    public boolean tryLogin(LoginFormDataDTO logindata) {
        return DatabaseUtils.try_login(logindata);
    }

    //======================================================================
    //TableModel 관련 (MODEL 단)
    //======================================================================   
    public boolean buildEmployeeTableModel(String condition) {
        loadEmployeesDataFittered(condition);
        employeeTableModel = new EmployeeTableModel(employees);

        if (employees.isEmpty()) {
            return true;
        }
        return false;
    }

    public EmployeeTableModel getEmployeeTableModel() {
        return employeeTableModel;
    }

    public Employee getEmployeeAt(int selectedRow) {
        if (employeeTableModel != null) {
            return employeeTableModel.getSelectedEmployee(selectedRow);
        }
        return null;
    }

    public void buildTableModelByCheckedEmployees() {
        checkedEmployees = employeeTableModel.getCheckedEmployees();
        allEditTableModel = new AllEditTableModel(checkedEmployees);
    }

    public AllEditTableModel getAllEditTableModel() {
        return allEditTableModel;
    }

    //======================================================================
    // 아래는 모두 JDBC DAO 관련 코드
    //======================================================================
    //======================================================================
    //EMPLOYEE==============================================================
    //======================================================================   
    //부하직원 또는 부하직원의부하직원의.. 가 아닌 직원의 SSN 찾기
    public List<String> findNotSubordinates(String query) {
        return EmployeeDAO.findNotSubordinates(query);
    }

    // Employee 데이터를 로드하는 메서드
    public void loadEmployeesData() {
        this.employees = EmployeeDAO.loadData();  // 데이터베이스에서 직원 정보 로드
    }

    public void loadEmployeesDataFittered(String condition) {
        this.employees = EmployeeDAO.loadDataFittered(condition);  // 데이터베이스에서 직원 정보 로드
    }

    public List<Employee> getEmployees() {
        return employees;
    }


    //======================================================================
    //DEPARTMENT==============================================================
    //======================================================================   
    public List<String> loadDepartmentsList() {
        return DepartmentDAO.loadDepartmentsList();  // 데이터베이스에서 부서 정보 로드
    }

    public void loadDepartmentsData() {
        this.departments = DepartmentDAO.loadData();  // 데이터베이스에서 부서 정보 로드
    }

    public void loadDepartmentsDataFittered(String condition) {
        this.departments = DepartmentDAO.loadDataFittered(condition);  // 데이터베이스에서 부서 정보 로드
    }

    public List<Department> getDepartments() {
        return departments;
    }

    //======================================================================      
    //DEPENDENT==============================================================
    //======================================================================   
    public void loadDepentdentsOnData() {
        this.dependents = DependentDAO.loadData();  // 데이터베이스에서 DeptLocations 정보 로드
    }

    public void loadDepentdentsFittered(String condition) {
        this.dependents = DependentDAO.loadDataFittered(condition);   // 데이터베이스에서 DeptLocations 정보 로드
    }

    public List<Dependent> getloadDepentdents() {
        return dependents;
    }

    //======================================================================
    //WORKS_ON==============================================================
    //======================================================================   
    public void loadWorksOnData() {
        this.worksons = WorksOnDAO.loadData();  // 데이터베이스에서 WORKS_ON 정보 로드
    }

    public void loadWorksOnsDataFittered(String condition) {
        this.worksons = WorksOnDAO.loadDataFittered(condition);  // 데이터베이스에서 WORKS_ON 정보 로드
    }

    public List<WorksOn> getWorksOns() {
        return worksons;
    }

    //======================================================================      
    //DEPT_LOCATIONS==============================================================
    //======================================================================   
    public void loadDeptLocationssOnData() {
        this.deptlocationss = DeptLocationsDAO.loadData();  // 데이터베이스에서 DeptLocations 정보 로드
    }

    public void loadDeptLocationssFittered(String condition) {
        this.deptlocationss = DeptLocationsDAO.loadDataFittered(condition);   // 데이터베이스에서 DeptLocations 정보 로드
    }


    public List<DeptLocations> getloadDeptLocationss() {
        return deptlocationss;
    }

    //======================================================================      
    //PROJECT==============================================================
    //======================================================================   
    public void loadProjectsOnData() {
        this.projects = ProjectDAO.loadData();  // 데이터베이스에서 DeptLocations 정보 로드
    }

    public void loadProjectsFittered(String condition) {
        this.projects = ProjectDAO.loadDataFittered(condition);   // 데이터베이스에서 DeptLocations 정보 로드
    }

    public List<Project> getloadProjects() {
        return projects;
    }

}
