package GUI;

import java.awt.BorderLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.SQLException;
import java.util.List;
import java.util.Observable;
import java.util.Observer;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.border.LineBorder;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableModel;

import SQLs.SQLManager;

public class ResultPanel extends JPanel implements Observer{
	private JTable resultTable;
	private JTable emptyTable;
	private JScrollPane tablePanel;
	private DefaultTableModel model;
	private DefaultTableModel emptyModel;
	private String[] tableIdentifier;
	private SQLManager sqlManager;
	private String subQuery;
	private TitledBorder exampleTitle;
	private ReviewWindow reviewWindow;
	//name, business_id, full_address, city, state, stars, review_count, checkin_count
	//table = new JTable(null, new String[]{"Name", "Business ID", "Address", "City", "State", "Stars", "ReviewCount", "Check Count"});
	public ResultPanel(SQLManager sqlManager) {
		setLayout(new BorderLayout());
		subQuery = "";
		this.sqlManager = sqlManager;
		tableIdentifier = new String[]{"Name", "Business ID", "Address", "City", "State", "Stars", "ReviewCount", "Check Count"};
		emptyModel = new DefaultTableModel(0, tableIdentifier.length);
		emptyModel.setColumnIdentifiers(tableIdentifier);
		emptyTable = new JTable(emptyModel);
		reviewWindow = new ReviewWindow(sqlManager);
		exampleTitle = new TitledBorder(LineBorder.createGrayLineBorder(), "Search Result");
		this.setBorder(exampleTitle);
		// = new HashMap<String, String>();
		showEmptyTable();
	}
	
	public void updateTable(String dayOfWeek, String openHour, String closeHour, String location) throws SQLException {
		if (subQuery.equals("")) return;
		
		String hourQuery = "";
		if (!dayOfWeek.equals("ALL")) {
			hourQuery =  hourQuery + "dayOfWeek = '" + dayOfWeek + "' and ";
		}
		if (!openHour.equals("ALL")) {
			hourQuery =  hourQuery + "openHour <= '" + openHour + "' and ";
		}

		if (!closeHour.equals("ALL")) {
			hourQuery =  hourQuery + "closeHour > '" + closeHour + "' and ";
		}

		if (!hourQuery.equals("")) {
			hourQuery = sqlManager.generateQuery("business_id", "Hours", hourQuery.substring(0, hourQuery.length() -  5), "business_id", subQuery, "", true);
		}
		else hourQuery = subQuery;
		
		String locationCondition = "";
		if (!location.equals("ALL")) {
			String[] temp = location.split(",");
			locationCondition = "city = '" + temp[0] + "' and state = '" + temp[1] + "'";
//			System.out.println("@@@@@@@@@@@@");
//			System.out.println(locationCondition);
		}
		
		String query = sqlManager.generateQuery("name, business_id, full_address, city, state, stars, review_count, checkin_count", "YelpBusiness", locationCondition, "business_id", hourQuery, "name", true);
//		System.out.println("****************************************************");
//		System.out.println(query);
		List<List<String>> data = sqlManager.getBusiness(query);
		model = new DefaultTableModel() {
			/**
			 * 
			 */
			private static final long serialVersionUID = 1L;

			public boolean isCellEditable(int row, int column){  
		          return false;  
		      }
		};
		model.setColumnIdentifiers(tableIdentifier);
		
		for (List<String> aRow: data) {
			model.addRow(aRow.toArray());
		}
		this.removeAll();
		JTable jt = new JTable();
		jt.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent evt) {
				JTable table =(JTable) evt.getSource();
		        int row = table.rowAtPoint(evt.getPoint());
		        try {
					reviewWindow.updateTable((String)table.getValueAt(row, 1));
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		});
		jt.setModel(model);
		tablePanel = new JScrollPane(jt);
		this.add(tablePanel, BorderLayout.CENTER);
		revalidate();
		repaint();
	}
	
	private void showEmptyTable() {
		this.removeAll();
		tablePanel = new JScrollPane(emptyTable);
		this.add(tablePanel, BorderLayout.CENTER);
		revalidate();
		repaint();
	}
	
	
	@Override
	public void update(Observable o, Object arg) {
		subQuery = (String)arg;
		System.out.println(subQuery);
		showEmptyTable();
//		revalidate();
//		repaint();
	}
}
