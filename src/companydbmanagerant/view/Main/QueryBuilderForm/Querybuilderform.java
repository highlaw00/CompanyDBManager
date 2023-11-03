/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package companydbmanagerant.view.Main.QueryBuilderForm;

import com.formdev.flatlaf.FlatClientProperties;
import java.awt.Dimension;
import java.util.List;
import javax.swing.*;

/**
 *
 * @author PHC
 */
public class Querybuilderform extends JPanel {

    private JPanel topPanel;
    private List<String> departments;
    private JPanel bottomPanel;
    private NestedQueryBuilderScrollPane queryBuilderPanel;
    public Querybuilderform(List<String> departments) {
        this.departments = departments;
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        topPanel = new JPanel();
        queryBuilderPanel = new NestedQueryBuilderScrollPane(departments);
        bottomPanel = new JPanel();
        //topPanel.setBackground(Color.GREEN);
        JLabel barlabel = new JLabel("Search Filter Builder");
        topPanel.add(barlabel);
        topPanel.putClientProperty(FlatClientProperties.STYLE, ""
                + "background: lighten(@background,7%)");

        //JButton btn1 = new JButton("필터 초기화");
        //JButton btn2 = new JButton("필터 저장");
        JButton btn3 = new JButton("닫기");
        btn3.setName("exitBtn");
        //bottomPanel.add(btn1);
        //bottomPanel.add(Box.createHorizontalStrut(10)); // 10 픽셀 간격 추가
        //bottomPanel.add(btn2);
        // bottomPanel.add(Box.createHorizontalStrut(10)); // 10 픽셀 간격 추가
        bottomPanel.add(btn3);
        bottomPanel.setLayout(new BoxLayout(bottomPanel, BoxLayout.X_AXIS));

        queryBuilderPanel.setPreferredSize(new Dimension(800, 400)); // 예시로 너비 600, 높이 400 설정
        add(topPanel);
        add(queryBuilderPanel);
        add(bottomPanel);
        
        
    }
   public ConditionNode getRootNode() {
        return queryBuilderPanel.getRootNode();
    }
    public int getFiltercnt() {
        return queryBuilderPanel.getFiltercnt();
    }
}
