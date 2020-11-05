package alex.lopez.vega.view.menu;

import java.awt.event.ActionEvent;
import java.io.File;

import javax.swing.JFileChooser;
import javax.swing.JMenuItem;

import alex.lopez.vega.controller.Controller;

public class LoadTemplateMenuItem extends JMenuItem {
	private static final long serialVersionUID = 1L;

	private File currentDir;
	private Controller controller;

	public LoadTemplateMenuItem(Controller controller) {
		super("Load template...");

		this.currentDir = null;
		this.controller = controller;

		init();
	}

	private void init() {
		addActionListener((ActionEvent ae) -> {
			if (ae.getSource() == this) {
				JFileChooser fc = new JFileChooser(currentDir);

				int choice = fc.showDialog(null, "Load...");

				if (choice == JFileChooser.APPROVE_OPTION) {
					File selectedFile = fc.getSelectedFile();

					controller.loadTemplate(selectedFile);

					currentDir = fc.getCurrentDirectory();
				}
			}
		});
	}

}
