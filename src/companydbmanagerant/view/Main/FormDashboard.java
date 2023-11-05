/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JPanel.java to edit this template
 */
package companydbmanagerant.view.Main;

import com.formdev.flatlaf.FlatClientProperties;
import companydbmanagerant.model.TableModel.EmployeeTableModel;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Shape;
import java.awt.event.ActionListener;
import java.awt.event.AdjustmentEvent;
import java.awt.event.AdjustmentListener;
import java.awt.event.ItemListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;
import javax.swing.GroupLayout;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JLayer;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.plaf.LayerUI;
import javax.swing.table.TableModel;

/**
 *
 * @author PHC
 */
public class FormDashboard extends javax.swing.JPanel {

    private List<JCheckBox> checkBoxList;
    JLayer<JScrollPane> jlayer;

    /**
     * Creates new form FormDashboard
     */
    public FormDashboard() {
        initComponents();
        //열목록 체크박스 초기화및 설정 
        initializeCheckBoxList();
        setCheckBoxTexts();
        filterBtn.putClientProperty(FlatClientProperties.STYLE, ""
                + "arc:25;"
                + "background : rgb(242, 242, 242);"
                + "foreground : rgb(20, 20, 20);"
                + "borderWidth : 1;"
                + "borderColor : rgb(242, 242, 242);"
                + "focusedBackground : rgb(230, 230, 230);"
                + "focusedBorderColor : rgb(220, 220, 220)");
        HeaderSelectPanel.revalidate();
        HeaderSelectPanel.repaint();

        EmployeeTable.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
        EmployeeDelBtn.setEnabled(false);
        EmployeeEditBtn.setEnabled(false);

        jTextField1.setVisible(false);
        setTableListener();
        setTableListeners();
        setTablePopupMenu();
        setDrawCellEdited();
    }

    private void setTableListeners() {
        // TableModel에 대한 listener 추가
        EmployeeTable.getModel().addTableModelListener(new TableModelListener() {
            @Override
            public void tableChanged(TableModelEvent e) {
                updateButtonState();
            }
        });

        EmployeeTable.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                if (!e.getValueIsAdjusting()) {
                    updateButtonState();
                }
            }
        });
    }

    public void updateButtonState() {
        boolean isAnyRowChecked = false;
        if (EmployeeTable.getModel() == null) {
            tableSearchBtn.setEnabled(false); // 모델이 없다면 버튼을 비활성화
            allEditBtn.setEnabled(false);
            return;
        }

        EmployeeTableModel model = (EmployeeTableModel) EmployeeTable.getModel();

        for (int i = 0; i < model.getRowCount(); i++) {
            Boolean isChecked = (Boolean) model.getValueAt(i, 0);
            if (isChecked != null && isChecked) {
                isAnyRowChecked = true;
                break;
            }
        }

        // 조건에 따라 allEditBtn의 활성화 상태를 변경
        allEditBtn.setEnabled(isAnyRowChecked);
    }

    private void setTablePopupMenu() {
        JPopupMenu contextMenu = new JPopupMenu();
        JMenuItem selectChecked = new JMenuItem("선택 체크");
        selectChecked.addActionListener(e -> {
            int[] selectedRows = EmployeeTable.getSelectedRows();
            for (int selectedRow : selectedRows) {
                EmployeeTable.setValueAt(true, selectedRow, 0); // 첫 번째 열이 체크 박스라고 가정
            }
            updateButtonState();
        });
        contextMenu.add(selectChecked);

        JMenuItem deselectChecked = new JMenuItem("선택 체크 해제");
        deselectChecked.addActionListener(e -> {
            
            int[] selectedRows = EmployeeTable.getSelectedRows();
            for (int selectedRow : selectedRows) {
                EmployeeTable.setValueAt(false, selectedRow, 0); // 첫 번째 열이 체크 박스라고 가정
            }
             updateButtonState();
        });
        contextMenu.add(deselectChecked);
        // 메뉴 아이템 생성 및 액션 리스너 추가
        JMenuItem selectAll = new JMenuItem("전체 체크");
        selectAll.addActionListener(e -> {
            for (int i = 0; i < EmployeeTable.getRowCount(); i++) {
                EmployeeTable.setValueAt(true, i, 0); // 첫 번째 열이 선택 상태를 나타내는 것으로 가정
            }
             updateButtonState();
        });
        contextMenu.add(selectAll);

        JMenuItem deselectAll = new JMenuItem("전체 체크 해제");
        deselectAll.addActionListener(e -> {
            for (int i = 0; i < EmployeeTable.getRowCount(); i++) {
                EmployeeTable.setValueAt(false, i, 0); // 첫 번째 열이 선택 상태를 나타내는 것으로 가정
            }
             updateButtonState();
        });
        contextMenu.add(deselectAll);

        EmployeeTable.addMouseListener(new MouseAdapter() {
            public void mousePressed(MouseEvent e) {
                showContextMenu(e);
            }

            public void mouseReleased(MouseEvent e) {
                showContextMenu(e);
            }

            private void showContextMenu(MouseEvent e) {
                if (e.isPopupTrigger()) {
                    int row = EmployeeTable.rowAtPoint(e.getPoint());
                    int[] selectedRows = EmployeeTable.getSelectedRows();

                    // 현재 선택된 행이 없거나 클릭한 위치의 행이 이미 선택된 상태가 아니라면
                    if (selectedRows.length <= 1 || !isRowSelected(selectedRows, row)) {
                        // 행 선택 상태 변경
                        EmployeeTable.changeSelection(row, 0, false, false);
                    }

                    // 팝업 메뉴 표시
                    contextMenu.show(e.getComponent(), e.getX(), e.getY());
                }
            }

            private boolean isRowSelected(int[] selectedRows, int row) {
                for (int selectedRow : selectedRows) {
                    if (selectedRow == row) {
                        return true;
                    }
                }
                return false;
            }
        });

        EmployeeTable.setComponentPopupMenu(contextMenu);
    }

    private void setTableListener() {
        // 테이블의 selection 모델에 대한 ListSelectionListener 추가
        EmployeeTable.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                // getValueIsAdjusting는 이벤트가 한 번만 처리되도록 해줌
                if (!e.getValueIsAdjusting()) {
                    // 선택된 행의 개수 확인
                    int[] selectedRows = EmployeeTable.getSelectedRows();

                    // 선택된 행이 정확히 하나일 경우 버튼을 활성화
                    boolean enableButtons = selectedRows.length == 1;
                    EmployeeDelBtn.setEnabled(enableButtons);
                    EmployeeEditBtn.setEnabled(enableButtons);
                }
            }
        });

    }

    private void setDrawCellEdited() {

        LayerUI<JScrollPane> layerUI = new LayerUI<JScrollPane>() {
            @Override
            public void paint(Graphics g, JComponent c) {
                super.paint(g, c);

                if (EmployeeTable == null) {
                    // EmployeeTable이 초기화되지 않았으므로 함수를 종료
                    return;
                }
                TableModel currentModel = EmployeeTable.getModel();
                if (!(currentModel instanceof EmployeeTableModel)) {
                    // 모델이 EmployeeTableModel이 아닌 경우 함수를 종료
                    return;
                }
                EmployeeTableModel model = (EmployeeTableModel) currentModel;
                Graphics2D g2d = (Graphics2D) g;
                Point viewPosition = jScrollPane1.getViewport().getViewPosition(); // get scroll offset
                int xOffset = 0;
                int yOffset = 0; // Adjust y-coordinate as you need
                for (int row = 0; row < EmployeeTable.getRowCount(); row++) {
                    for (int column = 0; column < EmployeeTable.getColumnCount(); column++) {

                        Object cellValue = EmployeeTable.getValueAt(row, column);
                        if (model.isEdited(row, column)) { //cellValue != null && 
                            Rectangle cellRect = EmployeeTable.getCellRect(row, column, false);
                            cellRect.translate(-viewPosition.x, -viewPosition.y); // adjust for the scroll offset
                            if (cellRect.y >= 0) { //

                                FontMetrics fm = g.getFontMetrics();
                                int headerHeight = EmployeeTable.getTableHeader().getHeight();
                                int stringWidth = fm.stringWidth("Edited");
                                int stringHeight = fm.getHeight();
                                float borderWidth = 1.2f;
                                g2d.setStroke(new BasicStroke(borderWidth));

                                g2d.setColor(new Color(23, 88, 189));
                                g2d.drawRoundRect(cellRect.x + xOffset, cellRect.y + yOffset + headerHeight, cellRect.width + 1, cellRect.height + 1, 6, 6);

                                //수정해야할 파트
                                int labelPaddingY = 0;
                                int labelPaddingX = 4;
                                int labelHeight = stringHeight + 2 * labelPaddingY;
                                int labelWidth = stringWidth + 2 * labelPaddingX;
                                int deletelabelmargin = 2;
                                int labelX = cellRect.x + xOffset + cellRect.width - labelWidth + deletelabelmargin;
                                int labelY = cellRect.y + yOffset + headerHeight - labelHeight / 2;
                                int labelArc = 3;

                                g2d.setColor(new Color(23, 88, 189));
                                g2d.fillRoundRect(labelX, labelY, labelWidth, labelHeight, labelArc, labelArc);

                                g.setColor(Color.white);
//                                Font customFont = new Font("Monospaced", Font.PLAIN, 13); // "Arial" 글씨체, 볼드체, 크기 14
//                                g.setFont(customFont);
                                g.drawString("Edited", labelX + labelPaddingX, labelY + fm.getAscent());

                            } else if (cellRect.y + cellRect.height > 0) {

                                FontMetrics fm = g.getFontMetrics();
                                int headerHeight = EmployeeTable.getTableHeader().getHeight();
                                int stringWidth = fm.stringWidth("Edited");
                                int stringHeight = fm.getHeight();
                                float borderWidth = 1.2f;
                                g2d.setStroke(new BasicStroke(borderWidth));
                                // 현재 그리기 상태를 저장
                                Shape oldClip = g2d.getClip();

                                // 상자의 하단 절반만 그리기 위해서 클리핑 영역을 설정
                                g2d.clipRect(cellRect.x + xOffset - 5, yOffset + headerHeight, cellRect.width + 1 + 10, cellRect.height - yOffset + headerHeight - cellRect.y);
                                g2d.setColor(new Color(23, 88, 189));
                                // 상자를 그립니다. 
                                g2d.drawRoundRect(cellRect.x + xOffset, cellRect.y + yOffset + headerHeight, cellRect.width + 1, cellRect.height + 1, 6, 6);
                                // 이전 그리기 상태로 복원
                                g2d.setClip(oldClip);
                            } else {
                                // System.out.println(cellRect.y);
                            }
                        }
                    }
                }
            }
        };
        jScrollPane1.getVerticalScrollBar().addAdjustmentListener(new AdjustmentListener() {
            private int lastValue = -1;

            @Override
            public void adjustmentValueChanged(AdjustmentEvent e) {
                if (!e.getValueIsAdjusting()) {  // 스크롤 동작이 끝났는지 확인
                    if (e.getValue() != lastValue) {  // 스크롤 위치가 변경되었는지 확인
                        lastValue = e.getValue();
                        jScrollPane1.repaint();  // repaint 호출
                    }
                }
            }
        });

        jlayer = new JLayer<>(jScrollPane1, layerUI);

        // DbMonitorPanel의 현재 GroupLayout 가져오기
        GroupLayout layout = (GroupLayout) DbMonitorPanel.getLayout();

        // 현재 GroupLayout의 설정을 기반으로 jLayer를 추가하기
        GroupLayout.ParallelGroup horizontalGroup = layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                .addGroup(layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jlayer, GroupLayout.DEFAULT_SIZE, 1008, Short.MAX_VALUE)
                        .addContainerGap());

        GroupLayout.ParallelGroup verticalGroup = layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                .addGroup(layout.createSequentialGroup()
                        .addContainerGap(GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jlayer, GroupLayout.PREFERRED_SIZE, 270, GroupLayout.PREFERRED_SIZE)
                        .addGap(35, 35, 35));

        layout.setHorizontalGroup(horizontalGroup);
        layout.setVerticalGroup(verticalGroup);
    }

    private void initializeCheckBoxList() {
        checkBoxList = new ArrayList<>();
        // 체크박스들을 리스트에 추가
        checkBoxList.add(jCheckBox1);
        checkBoxList.add(jCheckBox2);
        checkBoxList.add(jCheckBox3);
        checkBoxList.add(jCheckBox4);
        checkBoxList.add(jCheckBox5);
        checkBoxList.add(jCheckBox6);
        checkBoxList.add(jCheckBox7);
        checkBoxList.add(jCheckBox8);
        checkBoxList.add(jCheckBox9);
        checkBoxList.add(jCheckBox10);
    }

    // 체크박스들의 텍스트를 일괄적으로 설정하는 메서드
    private void setCheckBoxTexts() {
        // 컴포넌트의 그래픽스 환경을 사용하여 폰트 메트릭스 가져오기

        // 체크박스에 설정할 텍스트 목록
        List<String> texts = List.of(
                "First Name", "Minit", "Last Name", "SSN", "Birth Date",
                "Address", "Sex", "Salary", "Super SSN", "Dname"
        );

        // 리스트의 크기가 체크박스의 수와 동일한지 확인
        if (checkBoxList.size() == texts.size()) {
            for (int i = 0; i < checkBoxList.size(); i++) {
                JCheckBox checkBox = checkBoxList.get(i);
                String text = texts.get(i);
                checkBox.setText(text); // 체크박스의 텍스트 설정

            }
        } else {
            throw new IllegalStateException("체크박스와 텍스트의 수가 일치하지 않습니다.");
        }
    }

    public void addEmployeeAddBtnListener(ActionListener listener) {
        EmployeeAddBtn.addActionListener(listener);

    }

    public void addEmployeeEditBtnListener(ActionListener listener) {
        EmployeeEditBtn.addActionListener(listener);

    }

    public void addEmployeeDelBtnListener(ActionListener listener) {
        EmployeeDelBtn.addActionListener(listener);

    }

    public void addjButton1Listener(ActionListener listener) {
        RetrieveDBBtn.addActionListener(listener);

    }

//    public void addjButton2Listener(ActionListener listener) {
//        jButton2.addActionListener(listener);
//
//    }
    public void addCheckBoxListeners(ItemListener checkBoxItemListener) {
        ItemListener itemListener = checkBoxItemListener;
        for (JCheckBox checkBox : checkBoxList) {
            checkBox.addItemListener(itemListener);
        }
    }

    public void addFilterBtnListener(ActionListener listener) {
        filterBtn.addActionListener(listener);

    }

    public void setTableSearchPanelEnabled(boolean bool) {
        allEditLabel.setEnabled(bool);
        allEditBtn.setEnabled(bool);
        tableSearchBtn.setEnabled(bool);
        jComboBox1.setEnabled(bool);
        jComboBox2.setEnabled(bool);
        jComboBox3.setEnabled(bool);
        jTextField1.setEnabled(bool);
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        DbMonitorPanel = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        EmployeeTable = new javax.swing.JTable();
        SearchConditionPanel = new javax.swing.JPanel();
        RetrieveDBBtn = new javax.swing.JButton();
        HeaderSelectPanel = new javax.swing.JPanel();
        jCheckBox1 = new javax.swing.JCheckBox();
        jCheckBox2 = new javax.swing.JCheckBox();
        jCheckBox3 = new javax.swing.JCheckBox();
        jCheckBox4 = new javax.swing.JCheckBox();
        jCheckBox5 = new javax.swing.JCheckBox();
        jCheckBox6 = new javax.swing.JCheckBox();
        jCheckBox7 = new javax.swing.JCheckBox();
        jCheckBox8 = new javax.swing.JCheckBox();
        jCheckBox9 = new javax.swing.JCheckBox();
        jCheckBox10 = new javax.swing.JCheckBox();
        filterBtn = new javax.swing.JButton();
        jPanel1 = new javax.swing.JPanel();
        SearchConditionPanel1 = new javax.swing.JPanel();
        allEditLabel = new javax.swing.JLabel();
        jComboBox1 = new javax.swing.JComboBox<>();
        jComboBox2 = new javax.swing.JComboBox<>();
        jTextField1 = new javax.swing.JTextField();
        jComboBox3 = new javax.swing.JComboBox<>();
        jPanel2 = new javax.swing.JPanel();
        tableSearchBtn = new javax.swing.JButton();
        allEditBtn = new javax.swing.JButton();
        EmployeeAddBtn = new javax.swing.JButton();
        EmployeeEditBtn = new javax.swing.JButton();
        EmployeeDelBtn = new javax.swing.JButton();

        setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        DbMonitorPanel.setBackground(new java.awt.Color(0, 102, 0));
        DbMonitorPanel.setOpaque(false);

        EmployeeTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4", "Title 5", "Title 6", "Title 7", "Title 8", "Title 9", "Title 10"
            }
        ));
        jScrollPane1.setViewportView(EmployeeTable);

        javax.swing.GroupLayout DbMonitorPanelLayout = new javax.swing.GroupLayout(DbMonitorPanel);
        DbMonitorPanel.setLayout(DbMonitorPanelLayout);
        DbMonitorPanelLayout.setHorizontalGroup(
            DbMonitorPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(DbMonitorPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 1008, Short.MAX_VALUE)
                .addContainerGap())
        );
        DbMonitorPanelLayout.setVerticalGroup(
            DbMonitorPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(DbMonitorPanelLayout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 270, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(35, 35, 35))
        );

        add(DbMonitorPanel, new org.netbeans.lib.awtextra.AbsoluteConstraints(6, 74, 1020, 281));

        SearchConditionPanel.setBackground(new java.awt.Color(255, 51, 51));
        SearchConditionPanel.setOpaque(false);

        RetrieveDBBtn.setText("DB조회");
        RetrieveDBBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                RetrieveDBBtnActionPerformed(evt);
            }
        });

        HeaderSelectPanel.setBackground(new java.awt.Color(0, 255, 102));
        HeaderSelectPanel.setOpaque(false);
        HeaderSelectPanel.setLayout(new javax.swing.BoxLayout(HeaderSelectPanel, javax.swing.BoxLayout.LINE_AXIS));

        jCheckBox1.setSelected(true);
        jCheckBox1.setText("jCheckBox1");
        jCheckBox1.setDoubleBuffered(true);
        jCheckBox1.setHideActionText(true);
        jCheckBox1.setHorizontalTextPosition(javax.swing.SwingConstants.RIGHT);
        jCheckBox1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jCheckBox1ActionPerformed(evt);
            }
        });
        HeaderSelectPanel.add(jCheckBox1);

        jCheckBox2.setSelected(true);
        jCheckBox2.setText("HI");
        jCheckBox2.setDoubleBuffered(true);
        jCheckBox2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jCheckBox2ActionPerformed(evt);
            }
        });
        HeaderSelectPanel.add(jCheckBox2);

        jCheckBox3.setSelected(true);
        jCheckBox3.setText("jCheckBox1");
        jCheckBox3.setDoubleBuffered(true);
        HeaderSelectPanel.add(jCheckBox3);

        jCheckBox4.setSelected(true);
        jCheckBox4.setText("jCheckBox1");
        jCheckBox4.setDoubleBuffered(true);
        HeaderSelectPanel.add(jCheckBox4);

        jCheckBox5.setSelected(true);
        jCheckBox5.setText("jCheckBox1");
        jCheckBox5.setDoubleBuffered(true);
        HeaderSelectPanel.add(jCheckBox5);

        jCheckBox6.setSelected(true);
        jCheckBox6.setText("jCheckBox1");
        jCheckBox6.setDoubleBuffered(true);
        HeaderSelectPanel.add(jCheckBox6);

        jCheckBox7.setSelected(true);
        jCheckBox7.setText("jCheckBox1");
        jCheckBox7.setDoubleBuffered(true);
        HeaderSelectPanel.add(jCheckBox7);

        jCheckBox8.setSelected(true);
        jCheckBox8.setText("jCheckBox1");
        jCheckBox8.setDoubleBuffered(true);
        HeaderSelectPanel.add(jCheckBox8);

        jCheckBox9.setSelected(true);
        jCheckBox9.setText("jCheckBox1");
        jCheckBox9.setDoubleBuffered(true);
        HeaderSelectPanel.add(jCheckBox9);

        jCheckBox10.setSelected(true);
        jCheckBox10.setText("jCheckBox1");
        jCheckBox10.setDoubleBuffered(true);
        HeaderSelectPanel.add(jCheckBox10);

        filterBtn.setIcon(new javax.swing.ImageIcon(getClass().getResource("/companydbmanagerant/icons/filter.png"))); // NOI18N
        filterBtn.setText("Filters");

        javax.swing.GroupLayout SearchConditionPanelLayout = new javax.swing.GroupLayout(SearchConditionPanel);
        SearchConditionPanel.setLayout(SearchConditionPanelLayout);
        SearchConditionPanelLayout.setHorizontalGroup(
            SearchConditionPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(SearchConditionPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(SearchConditionPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(SearchConditionPanelLayout.createSequentialGroup()
                        .addComponent(HeaderSelectPanel, javax.swing.GroupLayout.DEFAULT_SIZE, 905, Short.MAX_VALUE)
                        .addGap(31, 31, 31))
                    .addGroup(SearchConditionPanelLayout.createSequentialGroup()
                        .addComponent(filterBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 114, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                .addComponent(RetrieveDBBtn)
                .addContainerGap())
        );
        SearchConditionPanelLayout.setVerticalGroup(
            SearchConditionPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(SearchConditionPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(SearchConditionPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(RetrieveDBBtn)
                    .addComponent(filterBtn))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(HeaderSelectPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        add(SearchConditionPanel, new org.netbeans.lib.awtextra.AbsoluteConstraints(6, 6, 1020, -1));

        jPanel1.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        jPanel1.setDoubleBuffered(false);
        jPanel1.setEnabled(false);

        SearchConditionPanel1.setBackground(new java.awt.Color(255, 51, 51));
        SearchConditionPanel1.setOpaque(false);

        allEditLabel.setText("테이블 내 검색하기");
        allEditLabel.setEnabled(false);

        jComboBox1.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "First Name", "Minit", "Last Name", "SSN", "Birth Date", "Address", "Sex", "Salary", "Super SSN", "Dname" }));
        jComboBox1.setEnabled(false);
        jComboBox1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jComboBox1ActionPerformed(evt);
            }
        });

        jComboBox2.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "=" }));
        jComboBox2.setEnabled(false);
        jComboBox2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jComboBox2ActionPerformed(evt);
            }
        });

        jTextField1.setEnabled(false);
        jTextField1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextField1ActionPerformed(evt);
            }
        });

        jComboBox3.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "=" }));
        jComboBox3.setEnabled(false);
        jComboBox3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jComboBox3ActionPerformed(evt);
            }
        });

        tableSearchBtn.setText("테이블 내에서 검색 후 체크");
        tableSearchBtn.setEnabled(false);
        tableSearchBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                tableSearchBtnActionPerformed(evt);
            }
        });

        allEditBtn.setText("체크된 것 일괄 수정");
        allEditBtn.setEnabled(false);
        allEditBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                allEditBtnActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(tableSearchBtn, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(allEditBtn, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                .addContainerGap(30, Short.MAX_VALUE)
                .addComponent(tableSearchBtn)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(allEditBtn)
                .addGap(18, 18, 18))
        );

        javax.swing.GroupLayout SearchConditionPanel1Layout = new javax.swing.GroupLayout(SearchConditionPanel1);
        SearchConditionPanel1.setLayout(SearchConditionPanel1Layout);
        SearchConditionPanel1Layout.setHorizontalGroup(
            SearchConditionPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(SearchConditionPanel1Layout.createSequentialGroup()
                .addContainerGap(12, Short.MAX_VALUE)
                .addGroup(SearchConditionPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(allEditLabel)
                    .addGroup(SearchConditionPanel1Layout.createSequentialGroup()
                        .addComponent(jComboBox1, javax.swing.GroupLayout.PREFERRED_SIZE, 104, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jComboBox2, javax.swing.GroupLayout.PREFERRED_SIZE, 104, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jComboBox3, javax.swing.GroupLayout.PREFERRED_SIZE, 104, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, 104, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        );
        SearchConditionPanel1Layout.setVerticalGroup(
            SearchConditionPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(SearchConditionPanel1Layout.createSequentialGroup()
                .addGroup(SearchConditionPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(SearchConditionPanel1Layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(allEditLabel)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(SearchConditionPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jComboBox1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jComboBox2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jComboBox3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addComponent(SearchConditionPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 52, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(SearchConditionPanel1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        add(jPanel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 360, 710, 110));
        jPanel1.getAccessibleContext().setAccessibleName("");

        EmployeeAddBtn.setText("직원 추가");
        EmployeeAddBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                EmployeeAddBtnActionPerformed(evt);
            }
        });
        add(EmployeeAddBtn, new org.netbeans.lib.awtextra.AbsoluteConstraints(730, 360, 90, -1));

        EmployeeEditBtn.setText("직원 수정");
        EmployeeEditBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                EmployeeEditBtnActionPerformed(evt);
            }
        });
        add(EmployeeEditBtn, new org.netbeans.lib.awtextra.AbsoluteConstraints(930, 360, 90, -1));

        EmployeeDelBtn.setText("직원 삭제");
        EmployeeDelBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                EmployeeDelBtnActionPerformed(evt);
            }
        });
        add(EmployeeDelBtn, new org.netbeans.lib.awtextra.AbsoluteConstraints(830, 360, 90, -1));
    }// </editor-fold>//GEN-END:initComponents

    private void RetrieveDBBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_RetrieveDBBtnActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_RetrieveDBBtnActionPerformed

    private void jCheckBox1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jCheckBox1ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jCheckBox1ActionPerformed

    private void jCheckBox2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jCheckBox2ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jCheckBox2ActionPerformed

    private void EmployeeAddBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_EmployeeAddBtnActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_EmployeeAddBtnActionPerformed

    private void EmployeeEditBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_EmployeeEditBtnActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_EmployeeEditBtnActionPerformed

    private void EmployeeDelBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_EmployeeDelBtnActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_EmployeeDelBtnActionPerformed

    private void tableSearchBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_tableSearchBtnActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_tableSearchBtnActionPerformed

    private void jComboBox3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jComboBox3ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jComboBox3ActionPerformed

    private void jTextField1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextField1ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextField1ActionPerformed

    private void jComboBox2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jComboBox2ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jComboBox2ActionPerformed

    private void jComboBox1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jComboBox1ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jComboBox1ActionPerformed

    private void allEditBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_allEditBtnActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_allEditBtnActionPerformed

    public JButton getEmployeeAddBtn() {
        return EmployeeAddBtn;
    }

    public void setEmployeeAddBtn(JButton EmployeeAddBtn) {
        this.EmployeeAddBtn = EmployeeAddBtn;
    }

    public JButton getEmployeeDelBtn() {
        return EmployeeDelBtn;
    }

    public void setEmployeeDelBtn(JButton EmployeeDelBtn) {
        this.EmployeeDelBtn = EmployeeDelBtn;
    }

    public JButton getEmployeeEditBtn() {
        return EmployeeEditBtn;
    }

    public void setEmployeeEditBtn(JButton EmployeeEditBtn) {
        this.EmployeeEditBtn = EmployeeEditBtn;
    }

    public JButton getRetrieveDBBtn() {
        return RetrieveDBBtn;
    }

    public JTable getEmployeeTable() {
        return EmployeeTable;
    }

    public List<JCheckBox> getCheckBoxList() {
        return checkBoxList;
    }

    public JButton getFilterBtn() {
        return filterBtn;
    }

    public JButton getAllEditBtn() {
        return allEditBtn;
    }

    public JLabel getAllEditLabel() {
        return allEditLabel;
    }

    public JComboBox<String> getjComboBox1() {
        return jComboBox1;
    }

    public JComboBox<String> getjComboBox2() {
        return jComboBox2;
    }

    public JComboBox<String> getjComboBox3() {
        return jComboBox3;
    }

    public JTextField getjTextField1() {
        return jTextField1;
    }

    public JButton getTableSearchBtn() {
        return tableSearchBtn;
    }

    public List<String> getSelectedCheckBoxLabels() {
        List<String> selectedLabels = new ArrayList<>();
        selectedLabels.add("Selected");
        for (JCheckBox checkBox : checkBoxList) { 
            if (checkBox.isSelected()) {
                selectedLabels.add(checkBox.getText());
            }
        }
        return selectedLabels;
    }
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel DbMonitorPanel;
    private javax.swing.JButton EmployeeAddBtn;
    private javax.swing.JButton EmployeeDelBtn;
    private javax.swing.JButton EmployeeEditBtn;
    private javax.swing.JTable EmployeeTable;
    private javax.swing.JPanel HeaderSelectPanel;
    private javax.swing.JButton RetrieveDBBtn;
    private javax.swing.JPanel SearchConditionPanel;
    private javax.swing.JPanel SearchConditionPanel1;
    private javax.swing.JButton allEditBtn;
    private javax.swing.JLabel allEditLabel;
    private javax.swing.JButton filterBtn;
    private javax.swing.JCheckBox jCheckBox1;
    private javax.swing.JCheckBox jCheckBox10;
    private javax.swing.JCheckBox jCheckBox2;
    private javax.swing.JCheckBox jCheckBox3;
    private javax.swing.JCheckBox jCheckBox4;
    private javax.swing.JCheckBox jCheckBox5;
    private javax.swing.JCheckBox jCheckBox6;
    private javax.swing.JCheckBox jCheckBox7;
    private javax.swing.JCheckBox jCheckBox8;
    private javax.swing.JCheckBox jCheckBox9;
    private javax.swing.JComboBox<String> jComboBox1;
    private javax.swing.JComboBox<String> jComboBox2;
    private javax.swing.JComboBox<String> jComboBox3;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTextField jTextField1;
    private javax.swing.JButton tableSearchBtn;
    // End of variables declaration//GEN-END:variables
}
