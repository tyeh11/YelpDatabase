import java.awt.BorderLayout;
import java.awt.Color;
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
	MainCategoryPanel subCategorypanel;
	ResultPanel resultPanel;
	MainCategoryPanel attributePanel;
	SQLManager sqlManager;
	
	HW3() {
		try {
			sqlManager = new SQLManager();
			String query = sqlManager.generateQuery("category","YelpMainCategory", null, null, null, "category");
			//System.out.println(query);
			List<String> mainCategories = sqlManager.getCategories(query);
				
			mainFrame = new JFrame("Yelp Datebase Application");
			mainPanel = new JPanel();
			mainFrame.add(mainPanel);			
			mainPanel.setLayout(new BorderLayout());
			
			mainCategoryPanel = new MainCategoryPanel(mainCategories, sqlManager, "category", "YelpMainCategory", "category","category");
			mainPanel.add(mainCategoryPanel.getPanel(), BorderLayout.WEST);
			mainCategoryPanel.getPanel().setBackground(Color.BLACK);
			
			
			subCategorypanel = new MainCategoryPanel(null, sqlManager, "category", "YelpSubCategory", "category","category");
			attributePanel = new MainCategoryPanel(null, sqlManager, "attributes", "YelpBusinessAttributes", "attributes", "attributes");
			
			mainCategoryPanel.addObserver(subCategorypanel);
			subCategorypanel.addObserver(attributePanel);
			subCategorypanel.getPanel().setBackground(Color.BLACK);
			mainPanel.add(subCategorypanel.getPanel(), BorderLayout.CENTER);
			mainPanel.add(attributePanel.getPanel(), BorderLayout.EAST);
			
			resultPanel = new ResultPanel();
			//mainPanel.add(resultPanel, BorderLayout.CENTER);
			
			
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
