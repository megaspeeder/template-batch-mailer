package alex.lopez.vega.view;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import javax.swing.table.AbstractTableModel;

import alex.lopez.vega.model.Recipient;
import alex.lopez.vega.model.Template;
import alex.lopez.vega.view.listeners.ModelListener;

public class RecipientTableModel extends AbstractTableModel implements ModelListener {
	private static final long serialVersionUID = 1L;

	private List<String> keys;
	private List<Recipient> recipientList;

	public RecipientTableModel() {
		keys = new LinkedList<String>();
		recipientList = new LinkedList<Recipient>();
	}

	@Override
	public String getColumnName(int column) {
		return keys.get(column);
	}

	@Override
	public int getRowCount() {
		return recipientList.size();
	}

	public List<Recipient> getRows(int[] selectedRows) {
		List<Recipient> selectedRecipients = new LinkedList<Recipient>();

		if (selectedRows.length == 0)
			return selectedRecipients;

		int i = 0;
		int j = 0;
		Iterator<Recipient> it = recipientList.iterator();

		while (it.hasNext() && i <= selectedRows[selectedRows.length - 1]) {
			Recipient r = it.next();

			if (i++ == selectedRows[j]) {
				selectedRecipients.add(r);
				j++;
			}
		}

		return selectedRecipients;
	}

	@Override
	public int getColumnCount() {
		return keys.size();
	}

	@Override
	public Object getValueAt(int rowIndex, int columnIndex) {
		return recipientList.get(rowIndex).get(keys.get(columnIndex));
	}

	@Override
	public void onRecipientsChanged(List<Recipient> newRecipientsList) {
		recipientList = newRecipientsList;
		fireTableDataChanged();
	}

	@Override
	public void onTemplateChanged(Template template) {

	}

	@Override
	public void onKeysChanged(List<String> newKeys) {
		keys = newKeys;
		fireTableStructureChanged();
	}
}
