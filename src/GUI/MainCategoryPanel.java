package GUI;

import java.awt.GridLayout;
import java.util.List;

import javax.swing.JCheckBox;
import javax.swing.JPanel;

public class MainCategoryPanel extends JPanel{
	JCheckBox[] categories;
	
	public MainCategoryPanel(List<String> values) {
		this.setLayout(new GridLayout(values.size(), 1));
		
		categories = new JCheckBox[values.size()];
		for (int i = 0; i < values.size(); i++) {
			categories[i] = new JCheckBox(values.get(i));
			this.add(categories[i]);
		}
		
	}
	
	
}

