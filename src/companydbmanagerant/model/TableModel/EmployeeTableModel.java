/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package companydbmanagerant.model.TableModel;

import java.text.SimpleDateFormat;
import java.text.ParseException;
import companydbmanagerant.model.Employee.Employee;
import java.awt.Color;
import java.awt.Component;
import java.awt.Point;
import java.lang.reflect.Field;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.swing.BorderFactory;
import javax.swing.JTable;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.DefaultTableCellRenderer;
import raven.toast.Notifications;

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
        fireTableStructureChanged(); // 모델이 변경되었음을 JTable에 알림

        editedCells = new HashSet<>();
        deletedCells = new HashSet<>();

        this.activeColumns = new ArrayList<>(); 
        this.activeColumns.add("Selected"); // 체크 박스에 사용될 열 이름
        this.activeColumns.addAll(Arrays.asList(
                "First Name", "Minit", "Last Name", "SSN", "Birth Date",
                "Address", "Sex", "Salary", "Super SSN", "Dname"
        ));
    }

    public EmployeeTableModel(List<Employee> employees, List<String> initialActiveColumns) {
        this.employees = employees;
        fireTableStructureChanged(); // 모델이 변경되었음을 JTable에 알림
        this.activeColumns = initialActiveColumns;
    }

    @Override
    public Class<?> getColumnClass(int columnIndex) {
        if (columnIndex == 0) {
            return Boolean.class; // 첫 번째 열은 체크 박스이므로
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

            case "Address":
                return employee.getAddress();
            case "Sex":
                return employee.getSex();
            case "Salary":

                Double salary = employee.getSalary();

                if (salary != null) {
                    try {
                        return salary.toString();
                    } catch (NumberFormatException e) {
                        // 숫자 형식으로 변환할 수 없을 때의 예외 처리, salary는 이미 null로 초기화됨.
                    }

                } else {
                    return "";
                }

                return employee.getSalary().toString();
            case "Super SSN":
                return employee.getSuperSsn();
            case "Dname":
                return employee.getDname();
            case "Created":
                return employee.getCreated(); 
            case "Modified":
                return employee.getModified();  
            default:
                return null;  
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
            }
        }
    }

    private String mapToEmployeeFieldName(String columnName) {
        switch (columnName) {
            case "First Name":
                return "fname";
            case "Last Name":
                return "lname";
            case "Super SSN":
                return "superSsn";
            case "Birth Date":
                return "bdate";
            default:
                return columnName.toLowerCase();
        }
    }

    private boolean checkCondition(Object fieldValue, String operation, String value) throws ParseException {
        // 필드 값이 null인 경우
        if (fieldValue == null) {
            return false;
        }

        // 필드 값이 String 타입인 경우
        if (fieldValue instanceof String) {
            return checkStringCondition((String) fieldValue, operation, value);
        } // 필드 값이 Double 타입인 경우
        else if (fieldValue instanceof Double) {
            try {
                return checkDoubleCondition((Double) fieldValue, operation, value);
            } catch (NumberFormatException e) {
                throw e;
            }
        } // 필드 값이 Date 타입인 경우
        else if (fieldValue instanceof Date) {
            try {
                return checkDateCondition((Date) fieldValue, operation, value);
            } catch (ParseException e) {
                throw e;
            }
        }

        return false;
    }

    public void selectedByCondition(Map<String, String> condition) {
        int count = 0;
        // 조건에서 필드명, 연산, 값을 가져옴
        String field = condition.get("field");
        String operation = condition.get("operation");
        String value = condition.get("value");

        // 필드명을 Employee 클래스의 필드명으로 매핑
        String mappedFieldName = mapToEmployeeFieldName(field);

        // 테이블에 존재하는 모든 튜플에 순회하며 체크함
        NumberFormatException nfe = null;
        ParseException pe = null;
        boolean parseException = false;
        boolean numberFormatException = false;
        for (Employee employee : employees) {
            try {
                // 데이터필드명을 가지고 해당 객체타입을 가져옴
                Field fieldObject = Employee.class.getDeclaredField(mappedFieldName);
                fieldObject.setAccessible(true);
                Object fieldValue = fieldObject.get(employee);

                // 조건에 부합하는지를 체크함
                if (checkCondition(fieldValue, operation, value)) {

                    employee.setSelected(true);
                    count++;
                } else {
                    employee.setSelected(false);
                }

            } catch (NoSuchFieldException | IllegalAccessException e) {
                Notifications.getInstance().show(Notifications.Type.ERROR, e.toString());

                e.printStackTrace();

            } catch (NumberFormatException e) {
                employee.setSelected(false);
                numberFormatException = true;
                nfe = e;
            } catch (ParseException e) {
                parseException = true;
                pe = e;
            }
        }
        if (numberFormatException) {
            Notifications.getInstance().show(Notifications.Type.ERROR, nfe.toString());
        } else if (parseException) {
            Notifications.getInstance().show(Notifications.Type.ERROR, pe.toString());
        } else {
            Notifications.getInstance().show(Notifications.Type.INFO, "조건에 부합하는 직원을 " + count + "명 찾았습니다.");
        }
        // 테이블 데이터가 변경되었음을 알림.
        fireTableDataChanged();
    }

    private boolean checkStringCondition(String fieldValue, String operation, String value) {
        switch (operation) {
            case "=":
                return fieldValue.equals(value);
            case "!=":
                return !fieldValue.equals(value);
            case "Contain":
                return fieldValue.contains(value);
            default:
                return false;
        }
    }

    private boolean checkDoubleCondition(Double fieldValue, String operation, String value) {
        try {

            if (value == null) {
                switch (operation) {
                    case "=":
                        return fieldValue == null;
                    case "!=":
                        return fieldValue != null;
                    default:
                        return false;
                }
            } else if (value.isEmpty()) {
                switch (operation) {
                    case "=":
                        return fieldValue == null;
                    case "!=":
                        return fieldValue != null;
                    default:
                        return false;
                }

            } else if (fieldValue == null) {
                switch (operation) {
                    case "=":
                        return value == null;
                    case "!=":
                        return value != null;
                    default:
                        return false;
                }
            } else {
                double doubleValue = Double.parseDouble(value);
                switch (operation) {
                    case "=":
                        return Double.compare(fieldValue, doubleValue) == 0;
                    case "!=":
                        return Double.compare(fieldValue, doubleValue) != 0;
                    case ">":
                        return Double.compare(fieldValue, doubleValue) > 0;
                    case ">=":
                        return Double.compare(fieldValue, doubleValue) >= 0;
                    case "<":
                        return Double.compare(fieldValue, doubleValue) < 0;
                    case "<=":
                        return Double.compare(fieldValue, doubleValue) <= 0;
                    default:
                        return false;
                }
            }
        } catch (NumberFormatException e) {
            throw e;

        }
    }

    private boolean checkDateCondition(Date fieldValue, String operation, String value) throws ParseException {
        // 날짜 형식으로 value를 파싱
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date dateValue = sdf.parse(value);
        int comparisonResult = fieldValue.compareTo(dateValue);
        
        switch (operation) {
            case "=":
                return comparisonResult == 0;
            case "!=":
                return comparisonResult != 0;
            case ">":
                return comparisonResult > 0;
            case ">=":
                return comparisonResult >= 0;
            case "<":
                return comparisonResult < 0;
            case "<=":
                return comparisonResult <= 0;
            default:
                return false;
        }
    }

    public List<Employee> getCheckedEmployees() {
        List<Employee> selectedEmployees = new ArrayList<>();

        for (Employee employee : employees) {
            if (employee.isSelected()) {
                selectedEmployees.add(employee);
            }
        }
        return selectedEmployees;
    }

    @Override
    public boolean isCellEditable(int rowIndex, int columnIndex) {
        return columnIndex == 0; // 첫 번째 열의 체크 박스만 수정 가능함
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
