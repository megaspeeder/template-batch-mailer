package alex.lopez.vega.view;

import java.awt.BorderLayout;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JSplitPane;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.border.EmptyBorder;

import alex.lopez.vega.controller.Controller;
import alex.lopez.vega.view.listeners.SerializationListener;
import alex.lopez.vega.view.menu.MenuBar;

public class View extends JFrame {
	private static final long serialVersionUID = 1L;

	private List<SerializationListener> serializationListenerList;

	private Controller controller;

	private RecipientPanel recipientPanel;
	private TemplatePanel templatePanel;

	public View() {
		super();
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (ClassNotFoundException | InstantiationException | IllegalAccessException
				| UnsupportedLookAndFeelException e) {
			e.printStackTrace();
		}

		serializationListenerList = new LinkedList<SerializationListener>();
		controller = new Controller();

		init();
	}

	public void addSerializationListener(SerializationListener listener) {
		if (listener != null)
			serializationListenerList.add(listener);
	}

	public void removeSerializationListener(SerializationListener listener) {
		if (listener != null)
			serializationListenerList.remove(listener);
	}

	private void init() {
		addComponents();
		addListeners();
		postprocess();
	}

	private void addComponents() {
		// Create and set content pane
		JPanel contentPane = new JPanel();
		contentPane.setLayout(new BorderLayout());
		setContentPane(contentPane);

		// Add menubar
		setJMenuBar(new MenuBar(this, controller));

		addContent(contentPane);

		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
	}

	private void addListeners() {
		serializationListenerList.add(controller.getMailer());
		serializationListenerList.add(controller.getMailer().getMailLimiter());

		controller.addModelListener(recipientPanel.getTableModel());
		controller.addModelListener(templatePanel);
	}

	private void addContent(JPanel contentPane) {
		// Create recipient and template panels
		recipientPanel = new RecipientPanel(controller);
		templatePanel = new TemplatePanel(controller);

		// Create parent and button panel
		JSplitPane splitPanel = new JSplitPane(JSplitPane.VERTICAL_SPLIT, recipientPanel, templatePanel);
		JPanel buttonPanel = new InfoPanel(controller, templatePanel);

		// Configure parent panel
		splitPanel.setResizeWeight(0.5);
		splitPanel.setBorder(null);

		// Add parent and button panels to content pane
		contentPane.add(splitPanel, BorderLayout.CENTER);
		contentPane.add(buttonPanel, BorderLayout.SOUTH);
	}

	private void postprocess() {
		setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		pack();
		setLocationRelativeTo(null);
		setVisible(true);
		setVisible(true);

		addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent we) {
				int choice = JOptionPane.showConfirmDialog(null, "Are you sure you want to exit?", "Confirm",
						JOptionPane.YES_NO_OPTION, JOptionPane.INFORMATION_MESSAGE);

				if (choice == JOptionPane.YES_OPTION) {
					for (SerializationListener listener : serializationListenerList) {
						try {
							listener.onSerialize();
						} catch (IOException e) {
							Dialog.showErrorMessage(e.getMessage());
						}
					}

					System.exit(0);
				}
			}
		});
	}
}
