package alex.lopez.ve.tbm.view;

import java.awt.BorderLayout;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.LinkedList;
import java.util.List;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JSplitPane;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.border.EmptyBorder;

import alex.lopez.ve.tbm.control.TBMController;
import alex.lopez.ve.tbm.interfaces.IControllerAccess;
import alex.lopez.ve.tbm.interfaces.Initialisable;
import alex.lopez.ve.tbm.interfaces.TBMWindowListener;

public class TBMWindow extends JFrame implements Initialisable, TBMWindowListener {

	private static final long serialVersionUID = -3424654641572932950L;

	private List<TBMWindowListener> windowListeners;

	private TBMController controller;
	private TBMRecipientPanel recipientPanel;
	private TBMTemplatePanel templatePanel;

	public TBMWindow() {
		controller = new TBMController();
		windowListeners = new LinkedList<TBMWindowListener>();

		initialise();
		prepareWindow();
	}

	@Override
	public void initialise() {
		IControllerAccess ica = controller;

		JPanel contentPane = new JPanel();

		setContentPane(contentPane);

		contentPane.setLayout(new BorderLayout());

		setJMenuBar(new TBMMenuBar(ica));

		recipientPanel = new TBMRecipientPanel(this);
		templatePanel = new TBMTemplatePanel(ica);

		controller.addTBMModelListener(recipientPanel);
		controller.addTBMModelListener(templatePanel);

		windowListeners.add(ica.getMailer());

		JSplitPane splitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT, recipientPanel, templatePanel);
		JPanel buttonPanel = new TBMButtonPanel(ica);

		splitPane.setResizeWeight(0.5);

		splitPane.setBorder(null);

		contentPane.add(splitPane, BorderLayout.CENTER);
		contentPane.add(buttonPanel, BorderLayout.SOUTH);

		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
	}

	private void prepareWindow() {
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (ClassNotFoundException | InstantiationException | IllegalAccessException
				| UnsupportedLookAndFeelException e) {
			System.err.println("Could not set system look and feel.");
		}

		pack();
		setLocationRelativeTo(null);
		setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		setVisible(true);

		addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent we) {
				int choice = JOptionPane.showConfirmDialog(null, "Are you sure you want to exit?", "Confirm",
						JOptionPane.YES_NO_OPTION, JOptionPane.INFORMATION_MESSAGE);

				if (choice == JOptionPane.YES_OPTION) {
					for (TBMWindowListener listener : windowListeners)
						listener.onWindowClosing(controller);

					System.exit(0);
				}
			}
		});
	}

	public void insertColumnVariable(String columnName) {
		templatePanel.insertColumnVariable(columnName);
	}

	@Override
	public void onWindowClosing(IControllerAccess ica) {

	}

}
