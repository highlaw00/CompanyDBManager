/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */
package companydbmanagerant;

import companydbmanagerant.controller.DataController;
import companydbmanagerant.model.DataModel;
import companydbmanagerant.view.DataView;
import companydbmanagerant.view.EmpolyEditFrame;
import com.formdev.flatlaf.FlatDarkLaf;
import com.formdev.flatlaf.FlatLightLaf;
import javax.swing.SwingUtilities;

/**
 *
 * @author PHC
 */
public class CompanyDBManagerANT {

    public static void main(String[] args) {

        FlatDarkLaf.registerCustomDefaultsSource("companydbmanagerant/theme");
        FlatDarkLaf.setup();

//        FlatLightLaf.registerCustomDefaultsSource("companydbmanagerant/theme");
//        FlatLightLaf.setup();
        SwingUtilities.invokeLater(new Runnable() {

            public void run() {
                DataModel model = new DataModel();
                DataView view = new DataView();
//                EmpolyEditFrame view2 = new EmpolyEditFrame();
//                  view2.setVisible(true);
                // Controller는 모델과 뷰를 모두 필요로 합니다.
                DataController controller = new DataController(model, view);
                // 화면에 뷰를 표시하도록 강제하는 추가 코드
                view.setVisible(true);

            }
        });
    }
}
