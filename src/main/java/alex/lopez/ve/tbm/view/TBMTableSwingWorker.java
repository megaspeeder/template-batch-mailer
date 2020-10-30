package alex.lopez.ve.tbm.view;

import java.util.List;

import javax.swing.JTable;
import javax.swing.SwingWorker;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;

import alex.lopez.ve.tbm.model.Recipient;

public class TBMTableSwingWorker extends SwingWorker<RecipientTableModel, Recipient> {

	private JTable table;
	private RecipientTableModel model;
	private List<String> columnNames;
	private List<Recipient> recipientList;

	public TBMTableSwingWorker(JTable table, List<String> columnNames, List<Recipient> recipientList) {
		this.table = table;
		this.model = (RecipientTableModel) table.getModel();
		this.columnNames = columnNames;
		this.recipientList = recipientList;
	}

	@Override
	protected RecipientTableModel doInBackground() throws Exception {
		TableColumnModel columnModel = table.getColumnModel();

		for (int i = 0; i < columnNames.size(); ++i) {
			if (columnModel.getColumnCount() <= i) {
				TableColumn tableColumn = new TableColumn(i);
				tableColumn.setHeaderValue(columnNames.get(i));
				columnModel.addColumn(tableColumn);
			} else
				columnModel.getColumn(i).setHeaderValue(columnNames.get(i));
		}

		int numColumns = columnModel.getColumnCount();

		while (columnModel.getColumnCount() > recipientList.size()) {
			columnModel.removeColumn(columnModel.getColumn(numColumns - 1));
			--numColumns;
		}

		model.setColumnNames(columnNames);

		Thread.sleep(5L);

		for (int i = 0; i < recipientList.size(); ++i) {
			publish(recipientList.get(i));
			Thread.sleep(5L);
		}

		return model;
	}

	@Override
	protected void process(List<Recipient> chunks) {
		model.addRows(chunks);
	}

}
