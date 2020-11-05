package alex.lopez.vega.view;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import alex.lopez.vega.controller.Controller;
import alex.lopez.vega.model.Recipient;
import alex.lopez.vega.model.Template;
import alex.lopez.vega.view.listeners.ModelListener;

public class TemplatePanel extends JPanel implements ModelListener {
	private static final long serialVersionUID = 1L;

	private JTextField subject;
	private JTextArea content;

	public TemplatePanel(Controller controller) {
		init(controller);
	}

	private void init(Controller controller) {
		setLayout(new BorderLayout());

		subject = new JTextField();
		content = new JTextArea();

		content.setWrapStyleWord(true);
		content.setLineWrap(true);

		subject.addFocusListener(new FocusListener() {
			@Override
			public void focusLost(FocusEvent e) {
				controller.setSubject(subject.getText());
			}

			@Override
			public void focusGained(FocusEvent e) {

			}
		});
		content.addFocusListener(new FocusListener() {
			@Override
			public void focusLost(FocusEvent e) {
				controller.setContent(content.getText());
			}

			@Override
			public void focusGained(FocusEvent e) {

			}
		});

		JLabel subjectLabel = new JLabel("Subject");
		JLabel contentLabel = new JLabel("Content");

		JPanel subjectPanel = new JPanel();
		JPanel contentPanel = new JPanel();

		subjectPanel.setLayout(new BorderLayout());
		contentPanel.setLayout(new BorderLayout());

		subjectLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
		subject.setAlignmentX(Component.LEFT_ALIGNMENT);

		contentLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
		content.setAlignmentX(Component.LEFT_ALIGNMENT);

		subjectPanel.add(subjectLabel, BorderLayout.NORTH);
		subjectPanel.add(subject, BorderLayout.CENTER);
		subjectPanel.add(Box.createVerticalStrut(10), BorderLayout.SOUTH);

		contentPanel.add(contentLabel, BorderLayout.NORTH);
		contentPanel.add(new JScrollPane(content), BorderLayout.CENTER);

		JPanel container = new JPanel();

		container.setLayout(new BorderLayout());

		container.add(subjectPanel, BorderLayout.NORTH);
		container.add(contentPanel, BorderLayout.CENTER);

		add(container);
		setBorder(BorderFactory.createTitledBorder("Template"));
	}

	@Override
	public void onRecipientsChanged(List<Recipient> newRecipientsList) {

	}

	@Override
	public void onKeysChanged(List<String> newKeys) {

	}

	@Override
	public void onTemplateChanged(Template template) {
		subject.setText(template.getSubject());
		content.setText(template.getContent());
	}
}
