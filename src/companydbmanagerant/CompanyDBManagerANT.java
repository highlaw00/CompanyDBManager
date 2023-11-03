/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */
package companydbmanagerant;

import companydbmanagerant.controller.DataController;
import companydbmanagerant.model.DataModel;
import companydbmanagerant.view.DataView;
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
                DataController controller = new DataController(model, view);
                view.setController(controller);
                        
                view.setVisible(true);

            }
        });
    }
}
