package alex.lopez.vega.view.menu;

import java.awt.event.ActionEvent;
import java.io.File;
import java.io.IOException;

import javax.swing.JFileChooser;
import javax.swing.JMenuItem;

import org.apache.poi.EncryptedDocumentException;

import alex.lopez.vega.controller.Controller;
import alex.lopez.vega.view.Dialog;

public class LoadRecipientsMenuItem extends JMenuItem {
	private static final long serialVersionUID = 1L;

	private File currentDir;
	private Controller controller;

	public LoadRecipientsMenuItem(Controller controller) {
		super("Load recipients...");

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

					try {
						controller.loadRecipients(selectedFile);
					} catch (EncryptedDocumentException | IOException e) {
						Dialog.showErrorMessage(e.getMessage());
					}

					currentDir = fc.getCurrentDirectory();
				}
			}
		});
	}
}
