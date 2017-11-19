package GUI;

import java.awt.GridLayout;
import java.sql.SQLException;
import java.util.List;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

import SQLs.SQLManager;

public class ReviewWindow extends JFrame{

	private SQLManager sqlManager;
	private JScrollPane tablePanel;
	private JPanel panel;
	private DefaultTableModel model;
	private String[] tableIdentifier;
	
	public ReviewWindow(SQLManager sqlManager) {
		this.setTitle("Search Result");
		this.sqlManager = sqlManager;
		tablePanel = new JScrollPane();
		tableIdentifier = new String[]{"Review date", "Stars", "Review Text", "User", "Cool Vote", "Funny Vote", "Usefull Vote"};
		model = new DefaultTableModel(0, tableIdentifier.length);
		model.setColumnIdentifiers(tableIdentifier);
		tablePanel = new JScrollPane(new JTable(model));
		panel = new JPanel();
		setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
		panel.setLayout(new GridLayout(1,1));
		this.add(panel);
		setSize(730, 500);
		setLocationRelativeTo(null); 
		//this.setVisible(true);
		//exampleTitle = new TitledBorder(LineBorder.createGrayLineBorder(), "Business");
	}
	
	public void updateTable(String business_id) throws SQLException {
		String query = sqlManager.generateQuery("review_date, stars, text, user_name, votes_cool, votes_funny, votes_useful", "Yelp_Review", "business_id = '" + business_id + "'", "", "", "review_date", false);
//		System.out.println("!!!!!!!!!!!!!!!!");
//		System.out.println(query);
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
		panel.setSize(730, 500);
		
		jt.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);	
		jt.getColumnModel().getColumn(0).setPreferredWidth(70);
		jt.getColumnModel().getColumn(1).setPreferredWidth(50);
		jt.getColumnModel().getColumn(2).setPreferredWidth(300);
		jt.getColumnModel().getColumn(3).setPreferredWidth(70);
		jt.getColumnModel().getColumn(4).setPreferredWidth(70);
		jt.getColumnModel().getColumn(6).setPreferredWidth(70);
		
		this.setVisible(true);
		panel.add(tablePanel);
		panel.revalidate();
		panel.repaint();
		//pack();
	}
	
	public void setVisible() {
		this.setVisible(true);
	}
}
