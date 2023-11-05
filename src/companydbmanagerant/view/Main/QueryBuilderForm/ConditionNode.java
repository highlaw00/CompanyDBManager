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

    //중위순회
    public static void inOrderTraversal(ConditionNode node, Consumer<ConditionNode> consumer) {
        if (node == null) {
            return;
        }

        // 먼저 왼쪽 자식을 순회
        for (ConditionNode child : node.getChildren()) {
            inOrderTraversal(child, consumer);
        }

        // 현재 노드를 처리함
        consumer.accept(node);
    }

    public String toSQL() {
        StringBuilder sql = new StringBuilder();
        if ("Group".equals(this.type)) {
            // 그룹 노드이므로 연산자를 사용하여 자식 노드를 연결해야함
            List<String> childClauses = children.stream()
                    .map(ConditionNode::toSQL) // 재귀적으로 자식노드들을 SQL로 변환 
                    .filter(s -> !s.isEmpty()) // 빈 SQL인경우 필터링함(그룹에서)
                    .collect(Collectors.toList());

            if (!childClauses.isEmpty()) {
                // 모든 하위식을 논리 연산자로 결합한다음 괄호로 묶음
                sql.append("(").append(String.join(" " + this.logic + " ", childClauses)).append(")");
            }
        } else {
            //조건 노드이므로 SQL로 직접 변환
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
                // 숫자형인경우 따음표를 넣지 않음
                String valueFormat = isNumeric ? "%s" : "'%s'";
                sql.append(String.format("%s %s " + valueFormat, actualField, this.operation, this.value));
            }
        }
        return sql.toString();

    }

    public int countFilters(ConditionNode node) {
        // 필터 수를 초기화
        int count = 0;

        // 노드가 그룹인 경우 자식 노드의 필터 수를 재귀적으로 카운트
        if ("Group".equals(node.type)) {
            for (ConditionNode child : node.children) {
                count += countFilters(child);
            }
            // root 노드가 아닌 그룹의 경우, 연산자를 하나의 필터로 카운팅
            // node.parent != null를 통해 root 노드인지를 체크
            if (!node.children.isEmpty() && node.isRoot == false) {
                count++; // 자식 노드들이 있고 부모가 있을 때만 연산자를 카운트
            }
        } else {
            // leafNode에서 실제연산자인경우 1을 리턴함.
            count = 1;
        }

        return count;
    }
}
