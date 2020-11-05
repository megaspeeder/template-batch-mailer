package alex.lopez.vega.view.popups.rows;

import javax.swing.JPopupMenu;

import alex.lopez.vega.controller.Controller;
import alex.lopez.vega.view.RecipientPanel;

public class RowPopupMenu extends JPopupMenu {
	private static final long serialVersionUID = 1L;

	public RowPopupMenu(RecipientPanel recipientPanel, Controller controller) {
		init(recipientPanel, controller);
	}

	private void init(RecipientPanel recipientPanel, Controller controller) {
		add(new DeleteRowsMenuItem(recipientPanel, controller));
	}
}
