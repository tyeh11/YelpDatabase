import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.LineBorder;
import javax.swing.border.TitledBorder;

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
	JButton clear;
	
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

			categoryPanel.setLayout(new GridLayout(1,3));
			categoryPanel.add(mainCategoryPanel.getPanel());
			categoryPanel.add(subCategorypanel.getPanel());
			categoryPanel.add(attributePanel.getPanel());
			mainPanel.add(categoryPanel, BorderLayout.WEST);
	
			resultPanel = new ResultPanel(sqlManager);
			mainPanel.add(resultPanel, BorderLayout.CENTER);
			attributePanel.addObserver(resultPanel);
			
			title = new JLabel("Yelp Database Application");
			mainPanel.add(title, BorderLayout.NORTH);
			
			
			JPanel buttonPanel = new JPanel();
			buttonPanel.setLayout(new GridLayout(1, 6));
			mainPanel.add(buttonPanel, BorderLayout.SOUTH);
			
			location = new ConditionSelector("city, state", "YelpBusiness", "state", sqlManager);
			dayOfWeek = new ConditionSelector("dayOfWeek", "Hours", "dayOfWeek", sqlManager);
			openHour = new ConditionSelector("openHour", "Hours", "openHour", sqlManager);
			closeHour = new ConditionSelector("closeHour", "Hours", "closeHour", sqlManager);
			
			JPanel temp = new JPanel();
			temp.setLayout(new GridLayout(1,1));
//			temp.add(new JLabel("Day Of Week"));
			temp.add(dayOfWeek);
			buttonPanel.add(temp);
			//temp.setBorder(BorderFactory.createLineBorder(Color.black));
			//TitledBorder title = new TitledBorder(LineBorder.createGrayLineBorder(), "Search Result");
			TitledBorder title = BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.black), "title");
			title.setTitleJustification(TitledBorder.CENTER);
			title.setTitlePosition(TitledBorder.ABOVE_TOP);
			temp.setBorder(new TitledBorder(LineBorder.createGrayLineBorder(), "Day of Week"));
			
			temp = new JPanel();
			temp.setLayout(new GridLayout(1,1));
//			temp.add(new JLabel("Open Hour"));
			temp.add(openHour);
			buttonPanel.add(temp);
			//temp.setBorder(BorderFactory.createLineBorder(Color.black));
			temp.setBorder(new TitledBorder(LineBorder.createGrayLineBorder(), "Open Hour"));
			
			temp = new JPanel();
			temp.setLayout(new GridLayout(1,1));
//			temp.add(new JLabel("Close Hour"));
			temp.add(closeHour);
			buttonPanel.add(temp);
			//temp.setBorder(BorderFactory.createLineBorder(Color.black));
			temp.setBorder(new TitledBorder(LineBorder.createGrayLineBorder(), "Close Hour"));
			
			temp = new JPanel();
			temp.setLayout(new GridLayout(1,1));
//			temp.add(new JLabel("Location"));
			temp.add(location);
			buttonPanel.add(temp);
			//temp.setBorder(BorderFactory.createLineBorder(Color.black));
			temp.setBorder(new TitledBorder(LineBorder.createGrayLineBorder(), "Location"));

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
			
			clear = new JButton("Clear");
			buttonPanel.add(clear);
			clear.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					mainCategoryPanel.unCheckALL();
					dayOfWeek.clear();
					openHour.clear();
					closeHour.clear();
					location.clear();
				}
				
			});
			
			mainFrame.setSize(1500,700);
			mainFrame.setLocationRelativeTo(null); 
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
