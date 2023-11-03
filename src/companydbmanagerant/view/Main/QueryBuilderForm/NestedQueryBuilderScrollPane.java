/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package companydbmanagerant.view.Main.QueryBuilderForm;

import com.formdev.flatlaf.FlatClientProperties;
import companydbmanagerant.model.Department.Department;
import companydbmanagerant.view.DataViewUtil;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;
import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import net.miginfocom.swing.MigLayout;

/**
 *
 * @author PHC
 */
public class NestedQueryBuilderScrollPane extends JScrollPane {

    private ConditionNode rootNode;
    private JPanel innerPanel;
    private JPanel rootPanel;
    List<String> departments;
    private int filtercnt = 0;
    // Constructor

    private void refreshFiltercnt() {
        if (rootNode != null) {
            filtercnt = rootNode.countFilters(rootNode);
        }
    }

    public int getFiltercnt() {
        refreshFiltercnt();
        return filtercnt;
    }

    public NestedQueryBuilderScrollPane(List<String> departments) {
        this.departments = departments;
        getVerticalScrollBar().setUnitIncrement(16); // 수직 스크롤바의 클릭당 이동 픽셀 설정
        getHorizontalScrollBar().setUnitIncrement(16); // 수평 스크롤바의 클릭당 이동 픽셀 설정

        getVerticalScrollBar().setBlockIncrement(50); // 수직 스크롤바의 블록 이동 픽셀 설정
        getHorizontalScrollBar().setBlockIncrement(50); // 수평 스크롤바의 블록 이동 픽셀 설정

        rootPanel = new JPanel(new MigLayout("fill, insets 0", "[left]", "[top]"));

        //rootPanel.setBackground(Color.CYAN);
        innerPanel = new JPanel(new MigLayout("fill, insets 0", "[left]", "[top]"));
        innerPanel.setBorder(BorderFactory.createTitledBorder(""));

        // 버튼을 패널에 추가
        //innerPanel.setBackground(Color.GREEN);
        rootPanel.add(innerPanel);

//        innerPanel = new JPanel();
//        innerPanel.setLayout(new BoxLayout(innerPanel, BoxLayout.Y_AXIS));
//        innerPanel.setBackground(Color.GREEN);
        // Set the inner panel as the viewport view
        this.setViewportView(rootPanel);
        this.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        this.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);

        // Add initial condition group
        rootNode = new ConditionNode("Group", true); // 루트 노드 생성
//
//        JButton traverseButton = new JButton("Traverse Tree");
//        traverseButton.addActionListener(new ActionListener() {
//            @Override
//            public void actionPerformed(ActionEvent e) {
//                System.out.println("=====================================");
//                System.out.println(rootNode.countFilters(rootNode));
//                System.out.println(rootNode.toSQL());
//            }
//        });
        // 버튼을 패널에 추가
        //innerPanel.add(traverseButton, "dock south");

        addConditionGroup(innerPanel, rootNode, true);

    }

    private void addConditionGroup(JPanel parentPanel, ConditionNode parentNode, Boolean isRoot) {
        JPanel groupPanel = new JPanel();

        // 조건 그룹에 대한 트리 노드 생성 및 추가
        final ConditionNode finalGroupNode; // Define a new final or effectively final variable

        if (!isRoot) {
            ConditionNode groupNode = new ConditionNode("Group", false);
            parentNode.addChild(groupNode); // Add as a child if it's not the root
            finalGroupNode = groupNode; // Assign it to the final variable for use in lambdas
        } else {
            finalGroupNode = parentNode; // If it is the root, use the parentNode directly
        }

        Color borderColor = Color.BLUE; // 원하는 색상으로 설정
        Border border = BorderFactory.createMatteBorder(1, 1, 1, 1, new Color(136, 115, 145));

        groupPanel.setLayout(new BoxLayout(groupPanel, BoxLayout.X_AXIS)); // X_AXIS로 변경
        //groupPanel.setBorder(BorderFactory.createTitledBorder(""));
//        groupPanel.putClientProperty(FlatClientProperties.STYLE, ""

        // AND/OR 콤보박스를 위한 새로운 패널 추가
//    JPanel logicPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        JPanel logicPanel = new JPanel();
        logicPanel.setLayout(new BoxLayout(logicPanel, BoxLayout.Y_AXIS));
        JComboBox<String> logicCombo = new JComboBox<>(new String[]{"AND", "OR"});
        // 콤보 박스의 선택된 항목을 groupNode의 logic 속성에 설정
        finalGroupNode.setLogic((String) logicCombo.getSelectedItem());
        logicCombo.addActionListener(e -> finalGroupNode.setLogic((String) logicCombo.getSelectedItem()));

        Dimension preferredSize = logicCombo.getPreferredSize();
        logicCombo.setMaximumSize(preferredSize);
        logicCombo.setPreferredSize(preferredSize);
        logicCombo.setMinimumSize(preferredSize);
        logicCombo.setLightWeightPopupEnabled(false);
        //logicCombo.setMaximumSize(new Dimension(Integer.MAX_VALUE, preferredSize.height));
        logicPanel.add(Box.createVerticalGlue());
        logicPanel.add(logicCombo);
        logicPanel.add(Box.createVerticalGlue());
        logicPanel.setBorder(border);
        logicPanel.putClientProperty(FlatClientProperties.STYLE, ""
                + "background: lighten(@background,3%)");
        groupPanel.add(logicPanel); //, BorderLayout.WEST

        JPanel rightPanel = new JPanel();
        rightPanel.setLayout(new BoxLayout(rightPanel, BoxLayout.Y_AXIS));

        border = BorderFactory.createMatteBorder(1, 0, 1, 1, new Color(136, 115, 145));

        rightPanel.setBorder(border);
        JButton addConditionButton = new JButton("+ Rule");
        addConditionButton.addActionListener(e -> addCondition(rightPanel, finalGroupNode));

        JButton addSubGroupButton = new JButton("+ SubGroup");
        addSubGroupButton.addActionListener(e -> addConditionGroup(rightPanel, finalGroupNode, false));

        JButton removeGroupButton = new JButton("- Delete");
        removeGroupButton.addActionListener(e -> {
            parentPanel.remove(groupPanel);
            parentNode.removeChild(finalGroupNode);
            parentPanel.revalidate();
            parentPanel.repaint();
        });

        JPanel controlPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        controlPanel.add(addConditionButton);
        controlPanel.add(addSubGroupButton);

        if (!isRoot) {
            controlPanel.add(removeGroupButton);
        }
        rightPanel.add(controlPanel);

        groupPanel.add(rightPanel);
        if (!isRoot) {
            parentPanel.add(groupPanel);
        } else {
            parentPanel.add(groupPanel, "cell 0 0");
        }

        parentPanel.revalidate();
        parentPanel.repaint();
    }

    private void addCondition(JPanel parentPanel, ConditionNode parentNode) {
        JPanel conditionPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        // 조건문에 대한 트리 노드 생성 및 추가
        ConditionNode conditionNode = new ConditionNode("Condition", false);
        // 부모 노드에 자식 노드 추가
        parentNode.addChild(conditionNode);

        JComboBox<String> fieldCombo = new JComboBox<>(new String[]{
            "First Name", "Minit", "Last Name", "SSN",
            "Address", "Sex", "Super SSN", "Dname",
            "Birth Date", "Salary"
        });

        JComboBox<String> operationCombo = new JComboBox<>(new String[]{"=", "!=", "LIKE"});

        JComboBox<String> valueOptionsComboBox = new JComboBox<>();
        JTextField valueField = new JTextField(10);

        // Setup the third combo box as an empty one by default
        valueOptionsComboBox.setVisible(false);

        JButton removeButton = new JButton("Delete");
        removeButton.addActionListener(e -> {
            parentPanel.remove(conditionPanel);
            parentNode.removeChild(conditionNode);
            parentPanel.revalidate();
            parentPanel.repaint();
        });

        // 콤보박스 프레임 밑에 파뭍히는 출력 버그 수정
        fieldCombo.setLightWeightPopupEnabled(false);
        operationCombo.setLightWeightPopupEnabled(false);
        valueOptionsComboBox.setLightWeightPopupEnabled(false);

        conditionNode.setField((String) fieldCombo.getSelectedItem());
        conditionNode.setOperation((String) operationCombo.getSelectedItem());
        conditionNode.setValue(valueField.getText());

        // Register the action listener that will handle the interaction logic
        ActionListener comboBoxInteractionListener = e -> handleComboBoxSelectionChanged(fieldCombo, operationCombo, valueOptionsComboBox, valueField);

        // 조건문 세부 정보 설정
        ActionListener detailSetter = e -> {
            conditionNode.setField((String) fieldCombo.getSelectedItem());
            conditionNode.setOperation((String) operationCombo.getSelectedItem());
            if (valueOptionsComboBox.isVisible()) {
                conditionNode.setValue((String) valueOptionsComboBox.getSelectedItem());
            } else {
                conditionNode.setValue(valueField.getText());
            }
        };

        fieldCombo.addActionListener(e -> {
            String selectedItem = fieldCombo.getSelectedItem().toString();
            boolean useComboBoxForValue = "Sex".equals(selectedItem) || "Dname".equals(selectedItem);

            valueOptionsComboBox.setVisible(useComboBoxForValue);
            valueField.setVisible(!useComboBoxForValue);

            updateComboBoxBasedOnSelection(operationCombo, valueOptionsComboBox, selectedItem);
            updateTextFieldBasedOnSelection(valueField, selectedItem);
            detailSetter.actionPerformed(e);
        });

        DocumentListener documentListener = new DocumentListener() {
            public void update() {
                conditionNode.setField((String) fieldCombo.getSelectedItem());
                conditionNode.setOperation((String) operationCombo.getSelectedItem());
                conditionNode.setValue(valueField.getText());
            }

            @Override
            public void insertUpdate(DocumentEvent e) {
                update();
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                update();
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                update();
            }
        };
        valueOptionsComboBox.addActionListener(detailSetter);
        valueField.getDocument().addDocumentListener(documentListener);

        // 콤보 박스와 텍스트 필드의 액션 리스너 등록
        fieldCombo.addActionListener(detailSetter);

        operationCombo.addActionListener(detailSetter);

        valueField.addActionListener(detailSetter);

        conditionPanel.add(fieldCombo);
        conditionPanel.add(operationCombo);
        conditionPanel.add(valueOptionsComboBox); // Add the third combo box to the panel
        conditionPanel.add(valueField);
        conditionPanel.add(removeButton);

        parentPanel.add(conditionPanel);

        parentPanel.revalidate();

        parentPanel.repaint();
    }

    public void handleComboBoxSelectionChanged(JComboBox<String> comboBox1, JComboBox<String> comboBox2, JComboBox<String> comboBox3, JTextField jTextField1) {
        String selectedItem = comboBox1.getSelectedItem().toString();
        updateComboBoxBasedOnSelection(comboBox2, comboBox3, selectedItem);
        updateTextFieldBasedOnSelection(jTextField1, selectedItem);
    }

    private void updateTextFieldBasedOnSelection(JTextField textField, String selectedItem) {
        boolean isVisible = !("Sex".equals(selectedItem) || "Dname".equals(selectedItem));
        DataViewUtil.setTextFieldVisible(textField, isVisible);
    }

    private void updateComboBoxBasedOnSelection(JComboBox<String> comboBox1, JComboBox<String> comboBox2, String selectedItem) {
        // 첫 번째 콤보 박스에 대한 아이템 설정
        List<String> newItemsForComboBox1 = generateItemsForComboBox1(selectedItem);
        DataViewUtil.setComboBoxItems(comboBox1, newItemsForComboBox1);
        //DataViewUtil.setComboBoxVisible(comboBox1, !newItemsForComboBox1.isEmpty());

        // 두 번째 콤보 박스에 대한 아이템 설정
        List<String> newItemsForComboBox2 = generateItemsForComboBox2(selectedItem);
        DataViewUtil.setComboBoxItems(comboBox2, newItemsForComboBox2);
        DataViewUtil.setComboBoxVisible(comboBox2, !newItemsForComboBox2.isEmpty());
    }

    private List<String> generateItemsForComboBox1(String selectedItem) {
        List<String> newItems = new ArrayList<>();
        switch (selectedItem) {
            case "First Name":
            case "Minit":
            case "Last Name":
            case "Address":
                // 'LIKE' operator is useful for string pattern matching
                newItems.add("=");
                newItems.add("!=");
                newItems.add("LIKE");
                break;
            case "Dname":
            case "SSN":
            case "Sex":
            case "Super SSN":
                // These fields are typically exact values, so 'LIKE' may not be as useful.
                newItems.add("=");
                newItems.add("!=");
                break;
            case "Birth Date":
            case "Salary":
                // For numerical and date fields, the 'LIKE' operator is generally not used.
                newItems.add("=");
                newItems.add(">");
                newItems.add(">=");
                newItems.add("<");
                newItems.add("<=");
                newItems.add("!=");
                break;
            default:
            // No default action or you can add some default operators if required
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
                //model.loadDepartmentsData();
                for (String department : departments) {
                    newItems.add(department);
                }
                break;
            // ... (필요한 경우 추가 case 구문을 여기에 배치할 수 있습니다.)
        }
        return newItems;
    }

    public ConditionNode getRootNode() {
        return rootNode;
    }
}
