package GUI;

import java.awt.BorderLayout;
import java.sql.SQLException;
import java.util.List;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.border.LineBorder;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableModel;

import SQLs.SQLManager;

public class ReviewWindow extends JFrame{

	private SQLManager sqlManager;
	private JScrollPane tablePanel;
	private JPanel panel;
	private DefaultTableModel model;
	private String[] tableIdentifier;
	
	public ReviewWindow(SQLManager sqlManager) {
		this.sqlManager = sqlManager;
		tablePanel = new JScrollPane();
		tableIdentifier = new String[]{"Review date", "Stars", "Review Text", "User", "Cool Vote", "Funny Vote", "Usefull Vote"};
		model = new DefaultTableModel(0, tableIdentifier.length);
		model.setColumnIdentifiers(tableIdentifier);
		tablePanel = new JScrollPane(new JTable(model));
		panel = new JPanel();
		this.add(panel);
		this.setVisible(true);
		setSize(1000,700);
		//exampleTitle = new TitledBorder(LineBorder.createGrayLineBorder(), "Business");
	}
	
	public void updateTable(String business_id) throws SQLException {
		String query = sqlManager.generateQuery("review_date, stars, text, user_name, votes_cool, votes_funny, votes_useful", "Yelp_Review", "business_id = '" + business_id + "'", "", "", "review_date", false);
		System.out.println("!!!!!!!!!!!!!!!!");
		System.out.println(query);
		List<List<String>> data = sqlManager.getReview(query);
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
		panel.removeAll();
		JTable jt = new JTable();
		jt.setModel(model);
		tablePanel = new JScrollPane(jt);
		panel.add(tablePanel);
		panel.revalidate();
		panel.repaint();
	}
	
	public void setVisible() {
		this.setVisible(true);
	}
}
