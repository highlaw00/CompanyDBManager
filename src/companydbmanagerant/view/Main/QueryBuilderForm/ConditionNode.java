/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package companydbmanagerant.view.Main.QueryBuilderForm;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Consumer;
import java.util.stream.Collectors;

/**
 *
 * @author PHC
 */
public class ConditionNode {

    private String type; // "Condition" 또는 "Group"
    private String logic; // "AND" 또는 "OR" (그룹일 경우에만 사용)
    private List<ConditionNode> children; // 자식 노드들
    private String field; // 조건 필드 (조건문일 경우에만 사용)
    private String operation; // 조건 연산자 (조건문일 경우에만 사용)
    private String value; // 조건 값 (조건문일 경우에만 사용)
    private boolean isRoot;
    Map<String, String> fieldMap = new HashMap<>();
    Set<String> numericFields;

    public ConditionNode(String type,boolean isRoot) {
        this.isRoot = isRoot;
        this.type = type;
        children = new ArrayList<>();
        fieldMap.put("First Name", "fname");
        fieldMap.put("Minit", "minit");
        fieldMap.put("Last Name", "lname");
        fieldMap.put("SSN", "ssn");
        fieldMap.put("Birth Date", "bdate");
        fieldMap.put("Address", "address");
        fieldMap.put("Sex", "sex");
        fieldMap.put("Salary", "salary");
        fieldMap.put("Super SSN", "superSsn");
        fieldMap.put("Dname", "dname");

        numericFields = new HashSet<>(Arrays.asList("salary"));
    }

    // Getters
    public String getType() {
        return type;
    }

    public String getLogic() {
        return logic;
    }

    public List<ConditionNode> getChildren() {
        return children;
    }

    public String getField() {
        return field;
    }

    public String getOperation() {
        return operation;
    }

    public String getValue() {
        return value;
    }

    // Setters
    public void setType(String type) {
        this.type = type;
    }

    public void setLogic(String logic) {
        this.logic = logic;
    }

    public void setField(String field) {
        this.field = field;
    }

    public void setOperation(String operation) {
        this.operation = operation;
    }

    public void setValue(String value) {
        this.value = value;
    }

    // Node operations
    public void addChild(ConditionNode child) {
        this.children.add(child);
    }

    public void removeChild(ConditionNode child) {
        this.children.remove(child);
    }

    // 이 메소드는 트리를 순회하면서 각 노드를 처리합니다.
    public static void inOrderTraversal(ConditionNode node, Consumer<ConditionNode> consumer) {
        if (node == null) {
            return;
        }

        // 먼저 왼쪽 자식을 순회합니다.
        for (ConditionNode child : node.getChildren()) {
            inOrderTraversal(child, consumer);
        }

        // 현재 노드를 처리합니다.
        consumer.accept(node);

        // 인-오더 순회를 위해서는 여기에 오른쪽 자식을 순회하는 코드를 추가해야 하지만,
        // 이 경우 자식 노드가 리스트로 관리되므로 필요하지 않습니다.
    }

    public String toSQL() {
        StringBuilder sql = new StringBuilder();
        if ("Group".equals(this.type)) {
            // It's a group node, so we must concatenate its children with the logic operator
            List<String> childClauses = children.stream()
                    .map(ConditionNode::toSQL) // Recursively convert each child to SQL
                    .filter(s -> !s.isEmpty()) // Filter out any empty SQL strings (from empty groups)
                    .collect(Collectors.toList());

            if (!childClauses.isEmpty()) {
                // Join all child clauses with the logic operator and enclose in parentheses
                sql.append("(").append(String.join(" " + this.logic + " ", childClauses)).append(")");
            }
        } else {
            // It's a condition node, so we directly convert its field, operation and value to SQL
            String actualField = fieldMap.getOrDefault(this.field, this.field);

            // 숫자형 필드인지 확인
            boolean isNumeric = numericFields.contains(actualField);

            if (this.value == null || this.value.isEmpty()) {
                if ("=".equals(this.operation)) {
                    sql.append(String.format("%s IS NULL", actualField));
                } else if ("!=".equals(this.operation)) {
                    sql.append(String.format("%s IS NOT NULL", actualField));
                } else if ("LIKE".equals(this.operation)) {
                    sql.append(String.format("%s IS NULL", actualField));
                }
            } else {
                // Format the value based on whether the field is numeric
                String valueFormat = isNumeric ? "%s" : "'%s'";
                sql.append(String.format("%s %s " + valueFormat, actualField, this.operation, this.value));
            }
        }
        return sql.toString();

    }

    public int countFilters(ConditionNode node) {
        // 필터 수를 초기화합니다.
        int count = 0;

        // 노드가 그룹인 경우 자식 노드의 필터 수를 재귀적으로 카운트합니다.
        if ("Group".equals(node.type)) {
            for (ConditionNode child : node.children) {
                count += countFilters(child);
            }
            // root 노드가 아닌 그룹의 경우, 로직 연산자를 하나의 필터로 카운트합니다.
            // 여기서 node.parent != null를 통해 root 노드인지를 체크합니다.
            if (!node.children.isEmpty() && node.isRoot == false) {
                count++; // 자식 노드들이 있고 부모가 있을 때만 연산자를 카운트합니다.
            }
        } else {
            // 실제 조건인 경우 1을 추가합니다.
            count = 1;
        }

        return count;
    }
}
