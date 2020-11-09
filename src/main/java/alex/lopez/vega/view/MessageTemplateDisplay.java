package alex.lopez.vega.view;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import alex.lopez.vega.controller.Controller;
import alex.lopez.vega.model.Message;

public class MessageTemplateDisplay extends JPanel {
	private static final long serialVersionUID = 1L;

	private JTextField subject;
	private JTextArea content;

	private Message currentMessage;

	public MessageTemplateDisplay(JList<Message> messageList, Controller controller) {
		currentMessage = null;

		init(messageList, controller);
	}

	private void init(JList<Message> messageList, Controller controller) {
		setLayout(new BorderLayout(5, 5));

		subject = new JTextField();
		content = new JTextArea();

		content.setWrapStyleWord(true);
		content.setLineWrap(true);

		JLabel subjectLabel = new JLabel("Subject");
		JLabel contentLabel = new JLabel("Content");
		JButton saveButton = new JButton("Save message");
		JButton deleteButton = new JButton("Delete message");

		saveButton.addActionListener((ActionEvent ae) -> {
			if (ae.getSource() == saveButton) {
				if (saveCurrentMessage())
					Dialog.showInfoMessage("Message saved.");
			}
		});

		deleteButton.addActionListener((ActionEvent ae) -> {
			if (ae.getSource() == deleteButton) {
				if (deleteCurrentMessage(messageList, controller))
					Dialog.showInfoMessage("Message deleted.");
			}
		});

		JPanel subjectPanel = new JPanel();
		JPanel contentPanel = new JPanel();
		JPanel buttonPanel = new JPanel();

		subjectPanel.setLayout(new BorderLayout());
		contentPanel.setLayout(new BorderLayout());
		buttonPanel.setLayout(new FlowLayout(FlowLayout.RIGHT));

		subjectLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
		subject.setAlignmentX(Component.LEFT_ALIGNMENT);

		contentLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
		content.setAlignmentX(Component.LEFT_ALIGNMENT);

		subjectPanel.add(subjectLabel, BorderLayout.NORTH);
		subjectPanel.add(subject, BorderLayout.CENTER);
		subjectPanel.add(Box.createVerticalStrut(10), BorderLayout.SOUTH);

		contentPanel.add(contentLabel, BorderLayout.NORTH);
		contentPanel.add(new JScrollPane(content), BorderLayout.CENTER);

		buttonPanel.add(saveButton);
		buttonPanel.add(deleteButton);

		JPanel container = new JPanel();

		container.setLayout(new BorderLayout());

		container.add(subjectPanel, BorderLayout.NORTH);
		container.add(contentPanel, BorderLayout.CENTER);
		container.add(buttonPanel, BorderLayout.SOUTH);

		add(container);
		setBorder(BorderFactory.createCompoundBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5),
				BorderFactory.createTitledBorder("Template")));

		setPreferredSize(new Dimension(300, 400));
	}

	public void setSelectedMessage(Message message) {
		currentMessage = message;

		if (currentMessage != null) {
			subject.setText(currentMessage.getSubject());
			content.setText(currentMessage.getContent());
		}
	}

	public boolean saveCurrentMessage() {
		if (currentMessage != null) {
			currentMessage.setSubject(subject.getText());
			currentMessage.setContent(content.getText());

			return true;
		}

		return false;
	}

	private boolean deleteCurrentMessage(JList<Message> messageList, Controller controller) {
		if (messageList.getModel().getSize() > 0) {
			int previousMessageIndex = messageList.getSelectedIndex();
			Message value = messageList.getSelectedValue();

			messageList.setSelectedIndex(previousMessageIndex + 1);
			((DefaultListModel<Message>) messageList.getModel()).remove(previousMessageIndex);

			setSelectedMessage(messageList.getSelectedValue());

			messageList.repaint();

			controller.removeMessage(value);

			return true;
		}

		return false;
	}
}
