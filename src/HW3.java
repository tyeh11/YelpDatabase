import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.sql.SQLException;
import java.util.List;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

import GUI.*;
import SQLs.SQLManager;

public class HW3 {
	JPanel mainPanel;
	JFrame mainFrame;
	MainCategoryPanel mainCategoryPanel;
	SubCategoryPanel subCategorypanel;
	ResultPanel resultPanel;
	AttributePanel attributePanel;
	SQLManager sqlManager;
	
	HW3() {
		try {
			sqlManager = new SQLManager();
			String query = sqlManager.generateQuery(new String[]{"category"}, new String[]{"YelpMainCategory"}, null, null, "category");
			System.out.println(query);
			List<String> mainCategories = sqlManager.getCategories(query);
			
			
			mainFrame = new JFrame("Yelp Datebase Application");
			mainPanel = new JPanel();
			mainFrame.add(mainPanel);			
			mainPanel.setLayout(new BorderLayout());
			
			mainCategoryPanel = new MainCategoryPanel(mainCategories);
			mainPanel.add(mainCategoryPanel, BorderLayout.EAST);
			mainFrame.setSize(500,500);
			mainFrame.setVisible(true);
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static void main(String args[]) {
		HW3 a = new HW3();
		
		//a.mainFrame.add(a.mainPanel);
	}
	
}
