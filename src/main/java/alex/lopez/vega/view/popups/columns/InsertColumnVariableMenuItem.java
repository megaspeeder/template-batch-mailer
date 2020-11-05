package alex.lopez.vega.view.popups.columns;

import java.awt.event.ActionEvent;

import javax.swing.JMenuItem;

import alex.lopez.vega.controller.Controller;
import alex.lopez.vega.view.RecipientPanel;

public class InsertColumnVariableMenuItem extends JMenuItem {
	private static final long serialVersionUID = 1L;

	public InsertColumnVariableMenuItem(RecipientPanel recipientPanel, Controller controller) {
		super("Insert column variable");

		init(recipientPanel, controller);
	}

	private void init(RecipientPanel recipientPanel, Controller controller) {
		addActionListener((ActionEvent ae) -> {
			if (ae.getSource() == this) {
				int selectedColumn = recipientPanel.getSelectedColumn();

				controller.insertColumnVariable(selectedColumn);
			}
		});
	}
}
