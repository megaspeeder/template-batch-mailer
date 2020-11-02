package alex.lopez.ve.tbm.view;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import javax.swing.table.AbstractTableModel;

import alex.lopez.ve.tbm.interfaces.TBMModelListener;
import alex.lopez.ve.tbm.model.Recipient;
import alex.lopez.ve.tbm.model.Template;

public class RecipientTableModel extends AbstractTableModel implements TBMModelListener {
	private static final long serialVersionUID = 2477125538272279353L;

	private List<Recipient> recipientList;
	private List<String> columnNames;

	public RecipientTableModel() {
		this(new LinkedList<Recipient>(), new ArrayList<String>());
	}

	public RecipientTableModel(List<Recipient> recipientList, List<String> columnNames) {
		this.recipientList = recipientList;
		this.columnNames = columnNames;
	}
	
	public void deleteAllRows() {
		this.recipientList = new LinkedList<Recipient>();
		fireTableDataChanged();
	}

	@Override
	public Class<?> getColumnClass(int columnIndex) {
		if (columnNames != null && columnNames.size() > 2 && columnIndex == columnNames.size() - 1)
			return Boolean.class;

		return String.class;
	}

	public void setColumnNames(List<String> columnNames) {
		this.columnNames = columnNames;
	}

	@Override
	public int getRowCount() {
		return recipientList.size();
	}

	@Override
	public int getColumnCount() {
		return columnNames.size();
	}

	public String[] getColumns() {
		return columnNames.toArray(new String[] {});
	}

	@Override
	public String getColumnName(int columnIndex) {
		return columnNames.get(columnIndex);
	}

	@Override
	public Object getValueAt(int rowIndex, int columnIndex) {
		Recipient r = recipientList.get(rowIndex);

		if (columnNames != null && columnNames.size() > 2 && columnIndex == columnNames.size() - 1) 
			return r.isActive();
		
		String data = r.getData(columnNames.get(columnIndex));

		return data;
	}

	@Override
	public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
		if (columnIndex == columnNames.size() - 1)
			recipientList.get(rowIndex).setIsActive((Boolean) aValue);
		else
			super.setValueAt(aValue, rowIndex, columnIndex);
	}

	@Override
	public boolean isCellEditable(int rowIndex, int columnIndex) {
		return true;
	}

	public void addRows(List<Recipient> chunks) {
		int previousRowCount = getRowCount();
		recipientList.addAll(chunks);
		fireTableRowsInserted(previousRowCount, getRowCount() - 1);
	}

	public void removeRows(int[] selectedRows) {
		for (int i = selectedRows.length - 1; i >= 0; --i) {
			recipientList.remove(selectedRows[i]);
			fireTableRowsDeleted(selectedRows[i], selectedRows[i]);
		}
	}

	@Override
	public void onRecipientListLoaded(List<String> columnNames, List<Recipient> recipientList) {
		this.recipientList = recipientList;
		this.columnNames = columnNames;
		fireTableDataChanged();
	}

	@Override
	public void onTemplateLoaded(Template template) {
		
	}

	@Override
	public void onRecipientsDeleted(List<String> columnNames, List<Recipient> recipientList) {
		this.recipientList = recipientList;
		this.columnNames = columnNames;
		fireTableDataChanged();
	}

	@Override
	public void onTemplateDeleted(Template template) {
		
	}
}
