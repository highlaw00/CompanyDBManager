/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package companydbmanagerant.view;

import java.util.List;
import javax.swing.JComboBox;
import javax.swing.JTextField;

/**
 *
 * @author PHC
 */
public class DataViewUtil {

    // 콤보 박스에 새로운 아이템들을 설정하는 메서드
    public static void setComboBoxItems(JComboBox<String> comboBox, List<String> newItems) {
        comboBox.removeAllItems(); // 현재 아이템들을 모두 제거
        for (String item : newItems) {
            comboBox.addItem(item); // 새로운 아이템들을 추가
        }
    }

    // 콤보 박스의 가시성을 설정하는 메서드
    public static void setComboBoxVisible(JComboBox<String> comboBox, boolean isVisible) {
        comboBox.setVisible(isVisible);
    }
    
    public static void setTextFieldVisible(JTextField textField, boolean isVisible) {
        textField.setVisible(isVisible);
    }
}

