package alex.lopez.ve.tbm.view;

import java.awt.BorderLayout;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JTable;

import alex.lopez.ve.tbm.interfaces.Callback;
import alex.lopez.ve.tbm.interfaces.Initialisable;
import alex.lopez.ve.tbm.interfaces.TBMModelListener;
import alex.lopez.ve.tbm.model.Recipient;
import alex.lopez.ve.tbm.model.Template;

public class TBMRecipientPanel extends JPanel implements Initialisable, TBMModelListener {

	private static final long serialVersionUID = -8010014351508099945L;

	private TBMWindow window;
	private TBMTableSwingWorker worker;

	private JPopupMenu columnPopupMenu;
	private JPopupMenu rowPopupMenu;
	private JTable table;

	private int selectedColumn;

	public TBMRecipientPanel(TBMWindow window) {
		this.window = window;
		selectedColumn = 0;
		initialise();
	}

	@Override
	public void initialise() {
		setLayout(new BorderLayout());

		table = new JTable(new RecipientTableModel());

		columnPopupMenu = new JPopupMenu();

		JMenuItem insertColumnVariableMI = createMenuItem("Insert Column Variable", () -> {
			if (selectedColumn != table.getColumnCount() - 1)
				insertColumnVariable(table.getColumnName(selectedColumn));
		});

		columnPopupMenu.add(insertColumnVariableMI);

		rowPopupMenu = new JPopupMenu();

		JMenuItem deleteRowMI = createMenuItem("Delete Rows", () -> {
			deleteRows();
		});

		rowPopupMenu.add(deleteRowMI);

		table.setComponentPopupMenu(rowPopupMenu);
		table.getTableHeader().setComponentPopupMenu(columnPopupMenu);

		table.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {
				Point p = e.getPoint();

				int row = table.rowAtPoint(p);

				if (row > -1 && table.getSelectedRows().length <= 1)
					table.setRowSelectionInterval(row, row);
			}
		});
		table.getTableHeader().addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {
				Point p = e.getPoint();

				int col = table.columnAtPoint(p);

				if (col > -1)
					selectedColumn = col;
			}
		});

		add(new JScrollPane(table), BorderLayout.CENTER);

		setBorder(BorderFactory.createTitledBorder("Recipients"));
	}

	private JMenuItem createMenuItem(String name, Callback c) {
		JMenuItem menuItem = new JMenuItem(name);

		menuItem.addActionListener((ActionEvent ae) -> {
			if (ae.getSource() == menuItem)
				c.execute();
		});

		return menuItem;
	}

	private void deleteRows() {
		int[] selectedRows = table.getSelectedRows();

		((RecipientTableModel) table.getModel()).removeRows(selectedRows);
	}

	private void insertColumnVariable(String columnName) {
		window.insertColumnVariable(columnName);
	}

	@Override
	public void onRecipientListLoaded(List<String> columnNames, List<Recipient> recipientList) {
		worker = new TBMTableSwingWorker(table, columnNames, recipientList);
		worker.execute();
		table.repaint();
	}

	@Override
	public void onTemplateLoaded(Template template) {
		
	}

	@Override
	public void onRecipientsDeleted(List<String> columnNames, List<TBMModelListener> modelListenerList) {
		table.setModel(new RecipientTableModel());
	}

	@Override
	public void onTemplateDeleted(Template template) {
		
	}

}
