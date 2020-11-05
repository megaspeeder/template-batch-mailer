package alex.lopez.vega.mail;

import java.io.EOFException;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.LinkedList;
import java.util.List;
import java.util.Properties;

import javax.mail.Authenticator;
import javax.mail.Message.RecipientType;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import alex.lopez.vega.data.DataSupply;
import alex.lopez.vega.model.Message;
import alex.lopez.vega.model.Recipient;
import alex.lopez.vega.model.Template;
import alex.lopez.vega.util.FileUtils;
import alex.lopez.vega.view.Dialog;
import alex.lopez.vega.view.listeners.MailLimiterListener;
import alex.lopez.vega.view.listeners.SerializationListener;

public class Mailer implements SerializationListener, MailLimiterListener {
	private LinkedList<Message> scheduledMessageList;
	private MailLimiter mailLimiter;
	private Properties properties;
	private Authenticator authenticator;

	public Mailer() {
		this.scheduledMessageList = new LinkedList<Message>();
		this.mailLimiter = new MailLimiter(DataSupply.MAIL_HOURLY_LIMIT, 0);

		properties = new Properties();

		properties.put("mail.smtp.host", "" + DataSupply.HOST);
		properties.put("mail.smtp.port", "" + DataSupply.PORT);
		properties.put("mail.smtp.auth", "" + true);
		properties.put("mail.smtp.ssl.enable", "true");
		properties.put("mail.debug", "true");
		
		authenticator = new Authenticator() {
			protected PasswordAuthentication getPasswordAuthentication() {
				return new PasswordAuthentication(DataSupply.USERNAME, DataSupply.PASSWORD);
			}
		};

		try {
			onDeserialize();
		} catch (ClassNotFoundException | IOException e) {
			Dialog.showErrorMessage(e.getMessage());
		}
	}

	public void addMailLimiterListener(MailLimiterListener listener) {
		mailLimiter.addListener(listener);
	}

	public int getAvailableMessages() {
		return mailLimiter.getAvailableMessages();
	}

	public List<Message> processMessages(List<Recipient> recipients, Template template) {
		LinkedList<Message> enqueuedMessages = new LinkedList<Message>();

		for (Recipient recipient : recipients) {
			Message message = new Message(DataSupply.FROM_ADDRESS, getEmail(recipient), template.getSubject(),
					template.getContent());

			enqueuedMessages.addLast(message);
			enqueueMessage(message);
		}

		return enqueuedMessages;
	}

	private String getEmail(Recipient recipient) {
		String value;

		value = recipient.get("email");

		if (value != null)
			return value;

		value = recipient.get("Email");

		if (value != null)
			return value;

		value = recipient.get("e-mail");

		if (value != null)
			return value;

		return recipient.get("E-mail");
	}

	public void enqueueMessage(Message message) {
		if (message != null)
			scheduledMessageList.addLast(message);
	}

	public Message dequeueMessage() {
		return scheduledMessageList.removeFirst();
	}

	public void sendMessagesUntilLimit() {
		try {
			int i = 0;
			while (!scheduledMessageList.isEmpty() && mailLimiter.canSendMessage()) {
				sendMessage(dequeueMessage());
				++i;
			}

			Dialog.showInfoMessage("Sent " + i + " messages.");
		} catch (MessagingException e) {
			Dialog.showErrorMessage(e.getMessage());
		}
	}

	private void sendMessage(Message message) throws AddressException, MessagingException {
		Session session = Session.getInstance(properties, authenticator);
		MimeMessage mimeMessage = new MimeMessage(session);

		mimeMessage.setFrom(new InternetAddress(message.getFromAddress()));
		mimeMessage.setRecipient(RecipientType.BCC, new InternetAddress(message.getToAddress()));
		mimeMessage.setSubject(message.getSubject());
		mimeMessage.setContent(message.getContent(), "text/html;charset=UTF-8");

		Transport.send(mimeMessage);

		mailLimiter.onMailSent();
	}

	public void onSerialize() throws IOException {
		// Create and/or load working folder
		String workDir = FileUtils.getWorkingDirectory() + "\\template-batch-mailer\\data";
		File workDirFolder = new File(workDir);

		if (workDirFolder.mkdir())
			System.out.println("Created working directory");

		// Create and/or load messages file
		String messagesDir = workDir + "\\mailer-data.txt";
		File messagesFile = new File(messagesDir);

		if (messagesFile.createNewFile())
			System.out.println("Created new mailer data file");

		// Serialize messages
		FileOutputStream fos = new FileOutputStream(messagesFile);
		ObjectOutputStream oos = new ObjectOutputStream(fos);

		oos.writeObject(scheduledMessageList);

		oos.close();
		fos.close();
	}

	@SuppressWarnings("unchecked")
	public void onDeserialize() throws IOException, ClassNotFoundException {
		// Create and/or load working folder
		String workDir = FileUtils.getWorkingDirectory() + "\\template-batch-mailer\\data";
		File workDirFolder = new File(workDir);

		if (workDirFolder.mkdir())
			System.out.println("Created working directory");

		// Create and/or load messages file
		String messagesDir = workDir + "\\mailer-data.txt";
		File dataFile = new File(messagesDir);

		if (dataFile.createNewFile())
			System.out.println("Created new mailer data file");

		// Deserialize messages
		FileInputStream fis = new FileInputStream(dataFile);
		ObjectInputStream ois = null;

		try {
			ois = new ObjectInputStream(fis);
		} catch (NullPointerException | EOFException e) {
			if (fis != null)
				fis.close();
			if (ois != null)
				ois.close();

			Dialog.showInfoMessage("Dev message: Created mail data.");

			return;
		}

		LinkedList<Message> deserializedMessageList = (LinkedList<Message>) ois.readObject();
		scheduledMessageList = deserializedMessageList;

		ois.close();
		fis.close();

		sendMessagesUntilLimit();
	}

	public MailLimiter getMailLimiter() {
		return mailLimiter;
	}

	@Override
	public void onNumSentMailsChanged(int mailRateLimit, int numSentMails) {
		sendMessagesUntilLimit();
	}
}
