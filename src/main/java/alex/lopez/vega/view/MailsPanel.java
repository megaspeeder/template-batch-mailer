package alex.lopez.vega.view;

import javax.swing.JLabel;
import javax.swing.JPanel;

import alex.lopez.vega.controller.Controller;
import alex.lopez.vega.view.listeners.MailLimiterListener;

public class MailsPanel extends JPanel implements MailLimiterListener {
	private static final long serialVersionUID = 6953779930021983866L;

	private JLabel availableMails;

	public MailsPanel(Controller controller) {
		init(controller);
	}

	private void init(Controller controller) {
		availableMails = new JLabel();

		updateAvailableMails(controller.getAvailableMessages());

		add(availableMails);
	}

	private void updateAvailableMails(int availableMessages) {
		availableMails.setText("Available messages: " + availableMessages);
	}

	@Override
	public void onNumSentMailsChanged(int mailRateLimit, int numSentMails) {
		updateAvailableMails(mailRateLimit - numSentMails);
	}
}
