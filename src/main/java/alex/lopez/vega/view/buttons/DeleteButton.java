package alex.lopez.vega.view.buttons;

import java.awt.event.ActionEvent;

import javax.swing.JButton;
import javax.swing.JOptionPane;

import alex.lopez.vega.controller.Controller;

public class DeleteButton extends JButton {
	private static final long serialVersionUID = 1L;

	private static final String[] BUTTONS_NAME = { "Delete recipients", "Delete template", "Delete both", "Cancel" };

	public DeleteButton(Controller controller) {
		super("Delete...");

		init(controller);
	}

	private void init(Controller controller) {
		addActionListener((ActionEvent ae) -> {
			if (ae.getSource() == this) {
				int choice = JOptionPane.showOptionDialog(null, "What would you like to delete?", "Delete",
						JOptionPane.DEFAULT_OPTION, JOptionPane.QUESTION_MESSAGE, null, BUTTONS_NAME, BUTTONS_NAME[0]);

				switch (choice) {
				case 0:
					controller.newRecipients();
					controller.newKeys();
					break;
				case 1:
					controller.newTemplate();
					break;
				case 2:
					controller.newRecipients();
					controller.newKeys();
					controller.newTemplate();
					break;
				default:
				}
			}
		});
	}
}
