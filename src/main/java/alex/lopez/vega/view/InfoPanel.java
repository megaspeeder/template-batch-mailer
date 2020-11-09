package alex.lopez.vega.view;

import java.awt.BorderLayout;

import javax.swing.JPanel;

import alex.lopez.vega.controller.Controller;
import alex.lopez.vega.view.buttons.DeleteButton;
import alex.lopez.vega.view.buttons.SendButton;

public class InfoPanel extends JPanel {
	private static final long serialVersionUID = 1L;

	public InfoPanel(Controller controller, TemplatePanel templatePanel) {
		init(controller, templatePanel);
	}

	private void init(Controller controller, TemplatePanel templatePanel) {
		setLayout(new BorderLayout());

		MailsPanel mailsPanel = new MailsPanel(controller);
		JPanel buttonContainer = new JPanel();
		
		controller.addMailLimiterListener(mailsPanel);

		buttonContainer.add(new DeleteButton(controller));
		buttonContainer.add(new SendButton(controller, templatePanel));

		add(buttonContainer, BorderLayout.EAST);
		add(mailsPanel, BorderLayout.WEST);
	}
}
