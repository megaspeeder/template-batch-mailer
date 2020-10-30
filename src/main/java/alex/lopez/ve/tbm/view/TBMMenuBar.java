package alex.lopez.ve.tbm.view;

import java.awt.event.ActionEvent;
import java.io.File;

import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JSeparator;

import alex.lopez.ve.tbm.interfaces.Callback;
import alex.lopez.ve.tbm.interfaces.IControllerAccess;
import alex.lopez.ve.tbm.interfaces.Initialisable;

public class TBMMenuBar extends JMenuBar implements Initialisable {

	private static final long serialVersionUID = 6527668553010898685L;

	private IControllerAccess ica;

	public TBMMenuBar(IControllerAccess ica) {
		this.ica = ica;
		initialise();
	}

	@Override
	public void initialise() {
		JMenu menu = new JMenu("File");

		JMenuItem loadRecipientsMI = createMenuItem("Load recipients file...", () -> {
			try {
				loadRecipients();
			} catch (Exception e) {
				JOptionPane optionPane = new JOptionPane("Could not load recipients: " + e.getMessage(),
						JOptionPane.ERROR_MESSAGE);
				JDialog dialog = optionPane.createDialog("Error");
				dialog.setVisible(true);
			}
		});
		JMenuItem loadTemplateMI = createMenuItem("Load template file...", () -> {
			try {
				loadTemplate();
			} catch (Exception e) {
				JOptionPane optionPane = new JOptionPane("Could not load template: " + e.getMessage(),
						JOptionPane.ERROR_MESSAGE);
				JDialog dialog = optionPane.createDialog("Error");
				dialog.setVisible(true);
			}
		});
		JMenuItem exitMI = createMenuItem("Exit", () -> {
			exit();
		});

		menu.add(loadRecipientsMI);
		menu.add(loadTemplateMI);
		menu.add(new JSeparator(JSeparator.HORIZONTAL));
		menu.add(exitMI);

		add(menu);
	}

	private JMenuItem createMenuItem(String name, Callback callback) {
		JMenuItem menuItem = new JMenuItem(name);

		menuItem.addActionListener((ActionEvent ae) -> {
			if (ae.getSource() == menuItem) {
				callback.execute();
			}
		});

		return menuItem;
	}

	private void loadTemplate() {
		JFileChooser fileChooser = new JFileChooser();

		int choice = fileChooser.showOpenDialog(null);

		if (choice == JFileChooser.APPROVE_OPTION) {
			File file = fileChooser.getSelectedFile();

			ica.loadTemplate(file);
		}
	}

	private void loadRecipients() {
		JFileChooser fileChooser = new JFileChooser();

		int choice = fileChooser.showOpenDialog(null);

		if (choice == JFileChooser.APPROVE_OPTION) {
			File file = fileChooser.getSelectedFile();

			ica.loadRecipients(file);
		}
	}

	private void exit() {
		int choice = JOptionPane.showConfirmDialog(null, "Are you sure you want to exit?", "Confirm",
				JOptionPane.YES_NO_OPTION, JOptionPane.INFORMATION_MESSAGE);

		if (choice == JOptionPane.YES_OPTION) {
			System.exit(0);
		}
	}

}
