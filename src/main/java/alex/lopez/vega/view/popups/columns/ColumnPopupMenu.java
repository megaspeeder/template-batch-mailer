package alex.lopez.vega.view.popups.columns;

import javax.swing.JPopupMenu;

import alex.lopez.vega.controller.Controller;
import alex.lopez.vega.view.RecipientPanel;

public class ColumnPopupMenu extends JPopupMenu {
	private static final long serialVersionUID = 1L;

	public ColumnPopupMenu(RecipientPanel recipientPanel, Controller controller) {
		init(recipientPanel, controller);
	}

	private void init(RecipientPanel recipientPanel, Controller controller) {
		add(new InsertColumnVariableMenuItem(recipientPanel, controller));
	}
}
