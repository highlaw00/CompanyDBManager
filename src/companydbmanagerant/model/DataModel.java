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
import companydbmanagerant.model.Project.Project;
import companydbmanagerant.model.Project.ProjectDAO;
import companydbmanagerant.model.TableModel.EmployeeTableModel;
import companydbmanagerant.model.WorksOn.WorksOn;
import companydbmanagerant.model.WorksOn.WorksOnDAO;
import java.util.List;

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

    // 테이블모델
    EmployeeTableModel employeeTableModel;

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
        // 모델 객체가 생성될 때 데이터를 로드합니다.
        //loadEmployeesData();
        //테스트코드
        //TestCode();

    }

    //======================================================================
    // 로그인 관련 관련 (MODEL 단)
    //======================================================================   

    public boolean tryLogin(LoginFormDataDTO logindata) {
        return DatabaseUtils.try_login(logindata);
    }
    
    //======================================================================
    //TableModel 관련 (MODEL 단)
    //======================================================================   

    public void buildEmployeeTableModel(String condition) {
        loadEmployeesDataFittered(condition);
        employeeTableModel = new EmployeeTableModel(employees);
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

    // 외부(예: 컨트롤러)에서 이 메서드를 호출하여 모델의 데이터를 가져올 수 있습니다.
    public void loadEmployeesDataFittered(String condition) {
        this.employees = EmployeeDAO.loadDataFittered(condition);  // 데이터베이스에서 직원 정보 로드
    }

    // 외부(예: 컨트롤러)에서 이 메서드를 호출하여 모델의 데이터를 가져올 수 있습니다.
    public List<Employee> getEmployees() {
        return employees;
    }

    //    // 데이터 변경이 있을 때 다시 로드하거나 업데이트할 수 있는 메서드를 제공합니다.
    //    public void reloadEmployeesData() {
    //        loadEmployeesData();
    //    }
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

    // 외부(예: 컨트롤러)에서 이 메서드를 호출하여 모델의 데이터를 가져올 수 있습니다.
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

    // 외부(예: 컨트롤러)에서 이 메서드를 호출하여 모델의 데이터를 가져올 수 있습니다.
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

    // 외부(예: 컨트롤러)에서 이 메서드를 호출하여 모델의 데이터를 가져올 수 있습니다.
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

    // 외부(예: 컨트롤러)에서 이 메서드를 호출하여 모델의 데이터를 가져올 수 있습니다.
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

    // 외부(예: 컨트롤러)에서 이 메서드를 호출하여 모델의 데이터를 가져올 수 있습니다.
    public List<Project> getloadProjects() {
        return projects;
    }

}
