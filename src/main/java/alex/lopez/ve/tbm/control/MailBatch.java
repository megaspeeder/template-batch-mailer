package alex.lopez.ve.tbm.control;

import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;
import java.util.Properties;

import javax.mail.Address;
import javax.mail.Authenticator;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMessage.RecipientType;

import alex.lopez.ve.tbm.interfaces.MailBatchListener;
import alex.lopez.ve.tbm.model.Recipient;
import alex.lopez.ve.tbm.model.Template;

public class MailBatch implements Serializable {
	private static final long serialVersionUID = 1L;

	private static final String HOST = "smtp.hostinger.com";
	private static final String USERNAME = "andrew.lopez@agentescd.com";
	private static final String PASSWORD = "AlexLucas1955";

	private static final Properties DEFAULT_PROPERTIES;
	private static final Authenticator DEFAULT_AUTHENTICATOR;
	private static final Address DEFAULT_FROM_ADDRESS;

	static {
		// Initialize properties

		DEFAULT_PROPERTIES = new Properties();

		DEFAULT_PROPERTIES.put("mail.smtp.host", HOST);
		DEFAULT_PROPERTIES.put("mail.smtp.auth", true);

		// Initialize authenticator

		DEFAULT_AUTHENTICATOR = new Authenticator() {
			protected PasswordAuthentication getPasswordAuthentication() {
				return new PasswordAuthentication(USERNAME, PASSWORD);
			}
		};

		// Initialize from address
		InternetAddress testAddress = null;

		try {
			testAddress = new InternetAddress("andrew.lopez@agentescd.com");
		} catch (AddressException e) {
			e.printStackTrace();
		}

		DEFAULT_FROM_ADDRESS = testAddress;
	}

	private List<MailBatchListener> listeners;

	private Address fromAddress;
	private List<Recipient> recipients;
	private int currentRecipientIndex;
	private Template template;
	private transient Properties properties;
	private transient Authenticator auth;

	public MailBatch(List<Recipient> recipients, Template template) {
		this(DEFAULT_FROM_ADDRESS, recipients, template, DEFAULT_PROPERTIES, DEFAULT_AUTHENTICATOR);
	}

	private MailBatch(Address fromAddress, List<Recipient> recipients, Template template, Properties properties,
			Authenticator auth) {
		listeners = new LinkedList<MailBatchListener>();
		this.fromAddress = fromAddress;
		this.setRecipients(recipients);
		setCurrentRecipient(0);
		this.template = template;
		this.properties = properties;
		this.auth = auth;
	}

	public boolean sendNextRecipient() {
		if (currentRecipientIndex + 1 >= recipients.size())
			return false;

		List<Recipient> sentRecipients = recipients.subList(currentRecipientIndex, currentRecipientIndex + 1);

		MimeMessage message = getIndividualRecipientMessage();

		System.out.println("Sent individual message to " + sentRecipients.get(0));

//		try {
//			Transport.send(message);
//		} catch (MessagingException e) {
//			e.printStackTrace();
//		}

		for (MailBatchListener listener : listeners)
			listener.onOneMailSent(this, sentRecipients);
		
		currentRecipientIndex += sentRecipients.size();

		return true;
	}

	public boolean sendAll() {
		if (currentRecipientIndex + 1 >= recipients.size())
			return false;

		List<Recipient> sentRecipients = recipients.subList(currentRecipientIndex, currentRecipientIndex + 1);

		MimeMessage message = getAllRecipientMessage();

		System.out.println("Sent message to many");

//		try {
//			Transport.send(message);
//		} catch (MessagingException e) {
//			e.printStackTrace();
//		}

		for (MailBatchListener listener : listeners)
			listener.onAllMailSent(this, sentRecipients);

		currentRecipientIndex = sentRecipients.size();

		return true;
	}

	private MimeMessage getIndividualRecipientMessage() {
		Session session = Session.getDefaultInstance(properties, auth);
		MimeMessage message = new MimeMessage(session);

		try {
			message.setFrom(fromAddress);
			message.setSubject(template.getSubject());
			message.setContent(template.getContent(), "text/html;charset=UTF-8");
			message.addRecipient(RecipientType.BCC,
					InternetAddress.parse(getRecipients().get(getCurrentRecipientIndex()).getData("email"))[0]);

		} catch (MessagingException me) {
			me.printStackTrace();
		}

		return message;
	}

	private MimeMessage getAllRecipientMessage() {
		Session session = Session.getDefaultInstance(properties, auth);
		MimeMessage message = new MimeMessage(session);

		try {
			message.setFrom(fromAddress);
			message.setSubject(template.getSubject());
			message.setContent(template.getContent(), "text/html;charset=UTF-8");

			String recipientEmails = "";

			for (int i = currentRecipientIndex; i < recipients.size(); ++i)
				recipientEmails += recipients.get(currentRecipientIndex).getData("email") + ",";

			recipientEmails = recipientEmails.substring(0, recipientEmails.length() - 1);

			message.addRecipients(RecipientType.BCC, InternetAddress.parse(recipientEmails));

		} catch (MessagingException me) {
			me.printStackTrace();
		}

		return message;
	}

	public int getCurrentRecipientIndex() {
		return currentRecipientIndex;
	}

	public void setCurrentRecipient(int currentRecipientIndex) {
		this.currentRecipientIndex = currentRecipientIndex;
	}

	public List<Recipient> getRecipients() {
		return recipients;
	}

	public void setRecipients(List<Recipient> recipients) {
		this.recipients = recipients;
	}

	public Template getTemplate() {
		return template;
	}

	public Recipient getCurrentRecipient() {
		return recipients.get(currentRecipientIndex);
	}

	public void addListener(MailBatchListener listener) {
		if (listener != null) {
			listeners.add(listener);
		}
	}

	public void removeListener(MailBatchListener listener) {
		if (listener != null) {
			listeners.remove(listener);
		}
	}

	public List<Recipient> getRemainingRecipients() {
		return recipients.subList(currentRecipientIndex, recipients.size());
	}

}