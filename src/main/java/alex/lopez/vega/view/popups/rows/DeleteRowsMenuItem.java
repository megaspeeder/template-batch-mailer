package alex.lopez.vega.view.popups.rows;

import java.awt.event.ActionEvent;
import java.util.List;

import javax.swing.JMenuItem;
import javax.swing.JTable;

import alex.lopez.vega.controller.Controller;
import alex.lopez.vega.model.Recipient;
import alex.lopez.vega.view.RecipientPanel;
import alex.lopez.vega.view.RecipientTableModel;

public class DeleteRowsMenuItem extends JMenuItem {
	private static final long serialVersionUID = 1L;

	public DeleteRowsMenuItem(RecipientPanel recipientPanel, Controller controller) {
		super("Delete rows");

		init(recipientPanel, controller);
	}

	private void init(RecipientPanel recipientPanel, Controller controller) {
		addActionListener((ActionEvent ae) -> {
			if (ae.getSource() == this) {
				JTable table = recipientPanel.getTable();
				RecipientTableModel recipientTableModel = (RecipientTableModel) table.getModel();
				int[] selectedRows = table.getSelectedRows();

				List<Recipient> rows = recipientTableModel.getRows(selectedRows);
				controller.removeRecipients(rows);
			}
		});
	}
}
