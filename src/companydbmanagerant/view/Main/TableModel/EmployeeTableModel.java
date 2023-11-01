/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package companydbmanagerant.view.Main.TableModel;

import java.text.SimpleDateFormat;
import java.text.ParseException;
import companydbmanagerant.model.Employee.Employee;
import java.awt.Color;
import java.awt.Component;
import java.awt.Point;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import javax.swing.BorderFactory;
import javax.swing.JTable;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.DefaultTableCellRenderer;

/**
 *
 * @author PHC
 */
public class EmployeeTableModel extends AbstractTableModel {

    private List<Employee> employees;
    private List<String> activeColumns = new ArrayList<>();  // 활성화된 열들의 목록

    // EmployeeTableModel 클래스 내부에 추가
    private Set<Point> editedCells;
    private Set<Point> deletedCells;

    public EmployeeTableModel(List<Employee> employees) {
        this.employees = employees;
        fireTableStructureChanged(); // 모델이 변경되었음을 JTable에 알립니다.

        editedCells = new HashSet<>();
        deletedCells = new HashSet<>();

        this.activeColumns = new ArrayList<>(); // 새로운 ArrayList로 초기화합니다.
        this.activeColumns.add("Selected"); // 체크 박스에 사용될 열 이름
        this.activeColumns.addAll(Arrays.asList(
                "First Name", "Minit", "Last Name", "SSN", "Birth Date",
                "Address", "Sex", "Salary", "Super SSN", "Dname"
        ));
//        this.activeColumns = Arrays.asList(
//                "First Name", "Minit", "Last Name", "SSN", "Birth Date",
//                "Address", "Sex", "Salary", "Super SSN", "Dname"  
//        );  //, "Created", "Modified"
    }

    public EmployeeTableModel(List<Employee> employees, List<String> initialActiveColumns) {
        this.employees = employees;
        fireTableStructureChanged(); // 모델이 변경되었음을 JTable에 알립니다.
        this.activeColumns = initialActiveColumns;
    }

    @Override
    public Class<?> getColumnClass(int columnIndex) {
        if (columnIndex == 0) {
            return Boolean.class; // 첫 번째 열은 체크 박스입니다.
        }
        return super.getColumnClass(columnIndex);
    }

    @Override
    public int getColumnCount() {
        return activeColumns.size();
    }

    @Override
    public int getRowCount() {
        return employees.size();
    }

    @Override
    public String getColumnName(int column) {
        return activeColumns.get(column);
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        Employee employee = employees.get(rowIndex);
        if (columnIndex == 0) {
            return employee.isSelected();
        }
        String columnName = getColumnName(columnIndex);
        switch (columnName) {
            case "Selected":
                return employee.isSelected();
            case "First Name":
                return employee.getFname();
            case "Minit":
                return employee.getMinit();
            case "Last Name":
                return employee.getLname();
            case "SSN":
                return employee.getSsn();
            case "Birth Date":

                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                return sdf.format(employee.getBdate());

//                return employee.getBdate();  // 날짜 형식이라면 적절한 형식으로 변환할 수 있습니다.
            case "Address":
                return employee.getAddress();
            case "Sex":
                return employee.getSex();
            case "Salary":
                return employee.getSalary();  // 숫자 형식이므로 필요에 따라 문자열로 변환할 수 있습니다.
            case "Super SSN":
                return employee.getSuperSsn();
            case "Dname":
                return employee.getDname();
            case "Created":
                return employee.getCreated();  // 날짜 형식이므로, 화면에 표시하기 전에 적절한 형식으로 변환할 수 있습니다.
            case "Modified":
                return employee.getModified();  // 마찬가지로 날짜 형식의 적절한 변환을 고려해야 합니다.
            default:
                return null;  // 알 수 없는 열 이름에 대해서는 null 반환
        }
    }

    // 사용자가 열 선택을 변경할 때 호출될 메서드
    public void setActiveColumns(List<String> newActiveColumns) {
        this.activeColumns = newActiveColumns;
        fireTableStructureChanged(); // 테이블 구조 변경을 알림
    }

    @Override
    public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
        Employee employee = employees.get(rowIndex);
        if (columnIndex == 0 && aValue instanceof Boolean) {
            employee.setSelected((Boolean) aValue);
            fireTableCellUpdated(rowIndex, columnIndex);
        } else {
            String columnName = getColumnName(columnIndex);
            switch (columnName) {
                case "First Name":
                    if (!employee.getFname().equals(aValue)) {
                        employee.setFname((String) aValue);
                        markEdited(rowIndex, columnIndex);
                    }
                    break;
                case "Minit":
                    if (!employee.getMinit().equals(aValue)) {
                        employee.setMinit((String) aValue);
                        markEdited(rowIndex, columnIndex);
                    }
                    break;
                case "Last Name":
                    if (!employee.getLname().equals(aValue)) {
                        employee.setLname((String) aValue);
                        markEdited(rowIndex, columnIndex);
                    }
                    break;
                case "SSN":
                    if (!employee.getSsn().equals(aValue)) {
                        employee.setSsn((String) aValue);
                        markEdited(rowIndex, columnIndex);
                    }
                    break;
                case "Birth Date":
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                    try {
                        Date date = sdf.parse((String) aValue);
                        if (!employee.getBdate().equals(date)) {
                            employee.setBdate(date);
                            markEdited(rowIndex, columnIndex);
                        }
                    } catch (ParseException e) {
                        e.printStackTrace();
                        // 적절한 오류 처리
                    }
                    break;
                case "Address":
                    if (!employee.getAddress().equals(aValue)) {
                        employee.setAddress((String) aValue);
                        markEdited(rowIndex, columnIndex);
                    }
                    break;
                case "Sex":
                    if (!employee.getSex().equals(aValue)) {
                        employee.setSex((String) aValue);
                        markEdited(rowIndex, columnIndex);
                    }
                    break;
                case "Salary":
                    if (employee.getSalary() != (Double) aValue) {
                        employee.setSalary((Double) aValue);
                        markEdited(rowIndex, columnIndex);
                    }
                    break;
                case "Super SSN":
                    if (!employee.getSuperSsn().equals(aValue)) {
                        employee.setSuperSsn((String) aValue);
                        markEdited(rowIndex, columnIndex);
                    }
                    break;
                case "Dname":
                    if (!employee.getDname().equals(aValue)) {
                        employee.setDname((String) aValue);
                        markEdited(rowIndex, columnIndex);
                    }
                    break;
                // ... 필요한 경우 다른 열에 대한 처리를 추가 ...
            }
        }
    }

    @Override
    public boolean isCellEditable(int rowIndex, int columnIndex) {
        return false; // columnIndex == 0첫 번째 열의 체크 박스만 수정 가능합니다.
    }

    public void markEdited(int row, int column) {
        editedCells.add(new Point(row, column));
    }

    public void markDeleted(int row, int column) {
        deletedCells.add(new Point(row, column));
    }

    public boolean isEdited(int row, int column) {
        return editedCells.contains(new Point(row, column));
    }

    public boolean isDeleted(int row, int column) {
        return deletedCells.contains(new Point(row, column));
    }

    public Employee getSelectedEmployee(int selectedRow) {
    if (selectedRow >= 0 && selectedRow < employees.size()) {
        return employees.get(selectedRow);
    }
    return null;  // 선택된 행이 없거나 범위를 벗어난 경우 null을 반환합니다.
}
}
