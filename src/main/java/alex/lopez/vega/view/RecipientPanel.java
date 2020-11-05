package alex.lopez.vega.view;

import java.awt.BorderLayout;
import java.awt.Point;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.BorderFactory;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JTable;

import alex.lopez.vega.controller.Controller;
import alex.lopez.vega.view.popups.columns.ColumnPopupMenu;
import alex.lopez.vega.view.popups.rows.RowPopupMenu;

public class RecipientPanel extends JPanel {
	private static final long serialVersionUID = 1L;

	private JTable table;
	private int selectedColumn;

	public RecipientPanel(Controller controller) {
		init(controller);
	}

	private void init(Controller controller) {
		setLayout(new BorderLayout());

		table = new JTable(new RecipientTableModel());

		JPopupMenu columnPopupMenu = new ColumnPopupMenu(this, controller);
		RowPopupMenu rowPopupMenu = new RowPopupMenu(this, controller);

		table.getTableHeader().setComponentPopupMenu(columnPopupMenu);
		table.setComponentPopupMenu(rowPopupMenu);

		addSelectionListeners();

		add(new JScrollPane(table), BorderLayout.CENTER);
		setBorder(BorderFactory.createTitledBorder("Recipients"));
	}

	private void addSelectionListeners() {
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
	}

	public RecipientTableModel getTableModel() {
		return (RecipientTableModel) table.getModel();
	}

	public JTable getTable() {
		return table;
	}

	public int getSelectedColumn() {
		return selectedColumn;
	}
}
