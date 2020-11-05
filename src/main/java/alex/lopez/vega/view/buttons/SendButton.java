package alex.lopez.vega.view.buttons;

import java.awt.event.ActionEvent;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JOptionPane;

import alex.lopez.vega.controller.Controller;
import alex.lopez.vega.model.Message;

public class SendButton extends JButton {
	private static final long serialVersionUID = 1L;

	public SendButton(Controller controller) {
		super("Send");

		init(controller);
	}

	private void init(Controller controller) {
		addActionListener((ActionEvent ae) -> {
			if (ae.getSource() == this) {
				int choice = JOptionPane.showConfirmDialog(null, "Are you sure you would like to send this message?",
						"Confirm", JOptionPane.YES_NO_OPTION, JOptionPane.INFORMATION_MESSAGE);

				if (choice == JOptionPane.YES_OPTION) {
					List<Message> enqueuedMessages = controller.addCurrentMessages();

					if (enqueuedMessages.size() > controller.getAvailableMessages()) {
						JOptionPane.showConfirmDialog(null,
								"Cannot send all messages at the same time without exceeding spam limit. Messages have been enqueued.",
								"Warning", JOptionPane.DEFAULT_OPTION, JOptionPane.WARNING_MESSAGE);
					}

					controller.sendMessagesUntilLimit();
					controller.newRecipients();
				}
			}
		});
	}
}
