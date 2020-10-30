package alex.lopez.ve.tbm.view;

import java.awt.event.ActionEvent;

import javax.swing.JButton;
import javax.swing.JOptionPane;

import alex.lopez.ve.tbm.interfaces.IControllerAccess;
import alex.lopez.ve.tbm.interfaces.Initialisable;

public class DeleteButton extends JButton implements Initialisable {
	
	private static final long serialVersionUID = -6007490988630062158L;

	private IControllerAccess ica;
	
	public DeleteButton(IControllerAccess ica) {
		super("Delete...");
		this.ica = ica;
		initialise();
	}

	@Override
	public void initialise() {
		addActionListener((ActionEvent ae) -> {
			if(ae.getSource() == this) {
				String[] buttons = { "Delete recipients", "Delete template", "Delete both", "Cancel" };

				int choice = JOptionPane.showOptionDialog(null, "What would you like to delete?", "Delete",
						JOptionPane.DEFAULT_OPTION, JOptionPane.QUESTION_MESSAGE, null, buttons, buttons[0]);

				switch (choice) {
				case 0:
					ica.deleteRecipients();
					break;
				case 1:
					ica.deleteTemplate();
					break;
				case 2: {
					ica.deleteRecipients();
					ica.deleteTemplate();
				}
					break;
				default:
				}
			}
		});
	}
}
