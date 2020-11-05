package alex.lopez.vega.view;

import javax.swing.JDialog;
import javax.swing.JOptionPane;

public class Dialog {
	public static void showErrorMessage(String message) {
		JOptionPane optionPane = new JOptionPane("Error: " + message, JOptionPane.ERROR_MESSAGE);
		JDialog dialog = optionPane.createDialog("Error");
		dialog.setVisible(true);
	}

	public static void showInfoMessage(String message) {
		JOptionPane optionPane = new JOptionPane(message, JOptionPane.INFORMATION_MESSAGE);
		JDialog dialog = optionPane.createDialog("Information");
		dialog.setVisible(true);
	}
}
