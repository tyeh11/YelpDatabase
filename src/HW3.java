import javax.swing.JFrame;
import javax.swing.JPanel;

import GUI.*;

public class HW3 {
	JPanel mainPanel;
	JFrame mainFrame;
	MainCategoryPanel mainCategoryPanel;
	SubCategoryPanel subCategorypanel;
	ResultPanel resultPanel;
	AttributePanel attributePanel;
	
	HW3() {
		
	}
	
	public static void main(String args[]) {
		HW3 a = new HW3();
		
		a.mainFrame.add(a.mainPanel);
	}
	
}
