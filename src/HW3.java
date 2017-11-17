import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

import GUI.*;
import SQLs.SQLManager;

public class HW3 {
	JPanel mainPanel;
	JFrame mainFrame;
	ConditionSelector dayOfWeek;
	ConditionSelector openHour;
	ConditionSelector closeHour;
	ConditionSelector location;
	JLabel title;
	MainCategoryPanel mainCategoryPanel;
	MainCategoryPanel subCategorypanel;
	ResultPanel resultPanel;
	MainCategoryPanel attributePanel;
	SQLManager sqlManager;
	JButton search;
	
	HW3() {
		try {
			sqlManager = new SQLManager();
			String query = sqlManager.generateQuery("category","YelpMainCategory", null, null, null, "category", true);
			//System.out.println(query);
			List<String> mainCategories = sqlManager.getCategories(query);
				
			mainFrame = new JFrame("Yelp Datebase Application");
			mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			mainPanel = new JPanel();
			mainFrame.add(mainPanel);			
			mainPanel.setLayout(new BorderLayout());
			
			mainCategoryPanel = new MainCategoryPanel("MainCategory", mainCategories, sqlManager, "category", "YelpMainCategory", "category","category");
			subCategorypanel = new MainCategoryPanel("SubCategory", null, sqlManager, "category", "YelpSubCategory", "category","category");
			attributePanel = new MainCategoryPanel("Attributes", null, sqlManager, "attributes", "YelpBusinessAttributes", "attributes", "attributes");
			
			mainCategoryPanel.addObserver(subCategorypanel);
			subCategorypanel.addObserver(attributePanel);
			//subCategorypanel.getPanel().setBackground(Color.BLACK);
			JPanel categoryPanel = new JPanel();
			categoryPanel.setLayout(new BorderLayout());
			categoryPanel.add(mainCategoryPanel.getPanel(), BorderLayout.WEST);
			categoryPanel.add(subCategorypanel.getPanel(), BorderLayout.CENTER);
			categoryPanel.add(attributePanel.getPanel(), BorderLayout.EAST);
			mainPanel.add(categoryPanel, BorderLayout.CENTER);
	
			resultPanel = new ResultPanel(sqlManager);
			mainPanel.add(resultPanel, BorderLayout.EAST);
			attributePanel.addObserver(resultPanel);
			
			title = new JLabel("Yelp Database Application");
			mainPanel.add(title, BorderLayout.NORTH);
			
			JPanel buttonPanel = new JPanel();
			buttonPanel.setLayout(new GridLayout(1, 5));
			mainPanel.add(buttonPanel, BorderLayout.SOUTH);
			
			location = new ConditionSelector("city, state", "YelpBusiness", "state", sqlManager);
			dayOfWeek = new ConditionSelector("dayOfWeek", "Hours", "dayOfWeek", sqlManager);
			openHour = new ConditionSelector("openHour", "Hours", "openHour", sqlManager);
			closeHour = new ConditionSelector("closeHour", "Hours", "closeHour", sqlManager);
			
			mainCategoryPanel.addObserver(location);
			subCategorypanel.addObserver(location);
			attributePanel.addObserver(location);
			mainCategoryPanel.addObserver(dayOfWeek);
			subCategorypanel.addObserver(dayOfWeek);
			attributePanel.addObserver(dayOfWeek);
			mainCategoryPanel.addObserver(openHour);
			subCategorypanel.addObserver(openHour);
			attributePanel.addObserver(openHour);
			mainCategoryPanel.addObserver(closeHour);
			subCategorypanel.addObserver(closeHour);
			attributePanel.addObserver(closeHour);
			
			search = new JButton("Search");
			buttonPanel.add(dayOfWeek);
			buttonPanel.add(openHour);
			buttonPanel.add(closeHour);
			buttonPanel.add(location);
			buttonPanel.add(search);
			search.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					try {
						resultPanel.updateTable(dayOfWeek.getSelectedValue(), openHour.getSelectedValue(), closeHour.getSelectedValue(), location.getSelectedValue());
					} catch (SQLException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
					
				}
				
			});
			mainFrame.setSize(1000,700);
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
