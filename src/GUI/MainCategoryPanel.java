package GUI;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.List;
import java.util.Observable;
import java.util.Observer;

import javax.swing.ButtonGroup;
import javax.swing.JCheckBox;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollBar;
import javax.swing.JScrollPane;
import javax.swing.ScrollPaneLayout;

import SQLs.SQLManager;

public class MainCategoryPanel extends Observable implements Observer{
	private JPanel panel;
	private JCheckBox[] categories;
	private ButtonGroup bGroup;
	private JRadioButton andCondition;
	private JRadioButton orCondition;
	private ActionListener checkBoxListener;
	private ActionListener conditionListener;
	
	private HashSet<String> selectedValues;
	private SQLManager sqlManager;
	private String subQuery;
	private String select;
	private String from;
	private String where;
	private String orderBy;
	private boolean condition;
	
	private JPanel checkBoxPanel;
	
	public MainCategoryPanel(List<String> values, SQLManager sqlManager, String select, String from, String where, String orderBy) {
		this.panel = new JPanel();
		this.checkBoxPanel = new JPanel();
		this.andCondition = new JRadioButton("AND");
		this.orCondition = new JRadioButton("OR");
		this.bGroup = new ButtonGroup();
		this.sqlManager = sqlManager;
		this.select = select;
		this.from = from;
		this.orderBy = orderBy;
		this.where = where;
		this.subQuery = "";
		this.condition = true;
		
		
		panel.setLayout(new BorderLayout());
		bGroup.add(andCondition);
		andCondition.setSelected(true);
		bGroup.add(orCondition);
		JPanel tempP = new JPanel();
		tempP.setLayout(new GridLayout(1,2));
		tempP.add(andCondition);
		tempP.add(orCondition);
		panel.add(tempP, BorderLayout.SOUTH);
		conditionListener = new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				JRadioButton r = (JRadioButton)e.getSource();
				if (r.getText().equals("AND")) {
					condition = true;
				}
				else {
					condition = false;
				}
				if (selectedValues.size() == 0) {
					setChanged();
					notifyObservers("");
					return;
				}
				String temp;
				if (condition) { //and
					temp = sqlManager.generateAndQuery("business_id", from, "business_id",selectedValues.toArray(new String[selectedValues.size()]), null);
				}
				else { // or
					temp = sqlManager.generateOrQuery("business_id", from, selectedValues.toArray(new String[selectedValues.size()]), "", "");
				}
				setChanged();
				notifyObservers(temp);
			}
		};
		andCondition.addActionListener(conditionListener);
		orCondition.addActionListener(conditionListener);
		
		selectedValues = new HashSet<String>();
		checkBoxListener = new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				JCheckBox b = (JCheckBox)e.getSource(); //update where statement once user click any checkbox
				if (b.isSelected()) {
					selectedValues.add(where + " = '" + b.getText() + "'");
				}
				else {
					selectedValues.remove(where + " = '" + b.getText() + "'");
				}
				if (selectedValues.size() == 0) {
					setChanged();
					notifyObservers("");
					return;
				}
				String temp;
				if (condition) { //and
					temp = sqlManager.generateAndQuery("business_id", from, "business_id",selectedValues.toArray(new String[selectedValues.size()]), null);
				}
				else { // or
					temp = sqlManager.generateOrQuery("business_id", from, selectedValues.toArray(new String[selectedValues.size()]), "", "");
				}
				setChanged();
				notifyObservers(temp);
			}
			
		};
		JScrollPane tempS = new JScrollPane(checkBoxPanel);
		
		if (values != null) {
			checkBoxPanel.setLayout(new GridLayout(values.size(), 1));
			categories = new JCheckBox[values.size()];
			for (int i = 0; i < values.size(); i++) {
				categories[i] = new JCheckBox(values.get(i));
				categories[i].addActionListener(checkBoxListener);
				checkBoxPanel.add(categories[i]);
			}
		}
		panel.add(tempS, BorderLayout.CENTER);

			
	}
	

	@Override
	public void update(Observable o, Object arg) {
		subQuery = (String) arg; 
		selectedValues.clear();
		checkBoxPanel.removeAll();
		if (subQuery.equals("")) { // if arg is null, clean panel, uesd for if a super super panel is changed
			panel.revalidate();
			panel.repaint();
			setChanged();
			notifyObservers(""); //notify next panel to clean its panel
			return;
		}
		String temp = sqlManager.generateQuery(select, from, null, "business_id", subQuery, select); //sql for query

		try {
			System.out.println("=====================");
			System.out.println(temp);
			List<String> newValues = sqlManager.getCategories(temp);

			categories = new JCheckBox[newValues.size()];
			checkBoxPanel.setLayout(new GridLayout(newValues.size(), 1));
			
			for (int i = 0; i < newValues.size(); i++) {
				categories[i] = new JCheckBox(newValues.get(i));
				categories[i].addActionListener(checkBoxListener);
				checkBoxPanel.add(categories[i]);
			}
			
			//panel.add(checkBoxPanel, BorderLayout.CENTER);
			panel.revalidate();
			panel.repaint();
			setChanged();
			notifyObservers(""); //notify next panel to clean its panel
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public JPanel getPanel() {
		return panel;
	}
	
	public void setCondition(Boolean condition) {
		this.condition = condition;
	}
}

