package GUI;

import java.sql.SQLException;
import java.util.List;
import java.util.Observable;
import java.util.Observer;

import javax.swing.JComboBox;

import SQLs.SQLManager;

public class ConditionSelector extends JComboBox implements Observer{
	private SQLManager sqlManager;
	private String subQuery;
	private String select;
	private String from;
	private String orderBy;
	
	public ConditionSelector(String select, String from, String orderBy, SQLManager sqlManager) {
		this.sqlManager = sqlManager;
		this.setEditable(false);
		this.select = select;
		this.from = from;
		this.orderBy = orderBy;
	}
	
	@Override
	public void update(Observable o, Object arg) {
		subQuery = (String)arg;
		if (subQuery.equals("")) return;
		String sql = sqlManager.generateQuery(select, from, "", "business_id", subQuery, orderBy, true);
//		System.out.println("------------------------");
//		System.out.println(sql);
		try {
			List<String> queryResult = sqlManager.getCategories(sql);
			this.removeAllItems();
			this.addItem("ALL");
			for (String r: queryResult) {
				this.addItem(r);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	public String getSelectedValue() {
		return (String)this.getSelectedItem();
	}
	
}
