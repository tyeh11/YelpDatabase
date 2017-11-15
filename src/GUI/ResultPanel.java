package GUI;

import java.awt.BorderLayout;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;

public class ResultPanel extends JPanel{
	JPanel panel;
	JTable table;
	DefaultTableModel model;
	
	public ResultPanel() {
		panel = new JPanel();
		panel.setLayout(new BorderLayout());
		model = new DefaultTableModel() {
			//name, business_id, full_address, city, state, stars, review_count, checkin_count
		    private static final long serialVersionUID = 1L;
		    String[] columnNames = {"Name", "Business ID", "Address", "City", "State", "Stars", "ReviewCount", "Check Count"};

		    @Override
		    public int getColumnCount() {
		         return columnNames.length;
		    }

		    @Override
		    public boolean isCellEditable(int row, int col) {
		         return false;
		    }

		    @Override
		    public int getRowCount() {
		         return 0;
		    }

		    @Override
		    public String getColumnName(int index) {
		        return columnNames[index];
		    }
		};
		table = new JTable(null, new String[]{"Name", "Business ID", "Address", "City", "State", "Stars", "ReviewCount", "Check Count"});
		JScrollPane tempS = new JScrollPane(table);
		panel.add(tempS, BorderLayout.CENTER);
	}
}
