package alex.lopez.vega.mail;

import java.io.EOFException;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;

import javax.mail.MessagingException;
import javax.mail.internet.AddressException;

import alex.lopez.vega.util.FileUtils;
import alex.lopez.vega.view.Dialog;
import alex.lopez.vega.view.listeners.MailLimiterListener;
import alex.lopez.vega.view.listeners.SerializationListener;

public class MailLimiter implements SerializationListener {

	private List<MailLimiterListener> listenerList;

	private int mailRateLimit;
	private int numSentMailsInPreviousHour;

	public MailLimiter(int mailRateLimit, int numSentMailsInPreviousHour) {
		listenerList = new LinkedList<MailLimiterListener>();

		this.mailRateLimit = mailRateLimit;
		this.numSentMailsInPreviousHour = numSentMailsInPreviousHour;

		TimerTask timerTask = new TimerTask() {
			@Override
			public void run() {
				decreaseSentMails();
			}
		};
		Timer timer = new Timer();
		timer.schedule(timerTask, 0, 3600000L / (long) mailRateLimit);

		try {
			onDeserialize();
		} catch (ClassNotFoundException | IOException | MessagingException e) {
			Dialog.showErrorMessage(e.getMessage());
		}
	}

	public void addListener(MailLimiterListener listener) {
		if (listener != null)
			listenerList.add(listener);
	}

	public void removeListener(MailLimiterListener listener) {
		if (listener != null)
			listenerList.add(listener);
	}

	public boolean canSendMessage() {
		return numSentMailsInPreviousHour < mailRateLimit;
	}

	public void onMailSent() {
		++numSentMailsInPreviousHour;

		for (MailLimiterListener listener : listenerList)
			listener.onNumSentMailsChanged(mailRateLimit, numSentMailsInPreviousHour);
	}

	public int getAvailableMessages() {
		return mailRateLimit - numSentMailsInPreviousHour;
	}

	private void decreaseSentMails() {
		if (numSentMailsInPreviousHour > 0) {
			--numSentMailsInPreviousHour;

			for (MailLimiterListener listener : listenerList)
				listener.onNumSentMailsChanged(mailRateLimit, numSentMailsInPreviousHour);
		}
	}

	@Override
	public void onSerialize() throws IOException {
		// Create and/or load working folder
		String workDir = FileUtils.getWorkingDirectory() + "\\template-batch-mailer\\data";
		File workDirFolder = new File(workDir);

		if (workDirFolder.mkdir())
			System.out.println("Created working directory");

		// Create and/or load messages file
		String messagesDir = workDir + "\\mail-limiter-data.txt";
		File messagesFile = new File(messagesDir);

		if (messagesFile.createNewFile())
			System.out.println("Created new mail limiter data file");

		// Serialize messages
		FileOutputStream fos = new FileOutputStream(messagesFile);
		ObjectOutputStream oos = new ObjectOutputStream(fos);

		oos.writeObject(numSentMailsInPreviousHour);
		oos.writeObject(new Date(System.currentTimeMillis()));

		oos.close();
		fos.close();
	}

	@Override
	public void onDeserialize() throws IOException, ClassNotFoundException, AddressException, MessagingException {
		// Create and/or load working folder
		String workDir = FileUtils.getWorkingDirectory() + "\\template-batch-mailer\\data";
		File workDirFolder = new File(workDir);

		if (workDirFolder.mkdir())
			System.out.println("Created working directory");

		// Create and/or load messages file
		String messagesDir = workDir + "\\mail-limiter-data.txt";
		File dataFile = new File(messagesDir);

		if (dataFile.createNewFile())
			System.out.println("Created new mail limiter data  file");

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

			Dialog.showInfoMessage("Dev message: Created mail limiter data.");

			return;
		}

		int numSentMails = (int) ois.readObject();
		Date previousDate = (Date) ois.readObject();
		Date now = new Date(System.currentTimeMillis());

		int timeDiffInSeconds = (int) TimeUnit.SECONDS.convert(Math.abs(previousDate.getTime() - now.getTime()),
				TimeUnit.MILLISECONDS);

		numSentMailsInPreviousHour = numSentMails
				- (int) ((float) mailRateLimit * ((float) timeDiffInSeconds / (float) 3600));
		numSentMailsInPreviousHour = Math.max(numSentMailsInPreviousHour, 0);

		for (MailLimiterListener listener : listenerList)
			listener.onNumSentMailsChanged(mailRateLimit, numSentMailsInPreviousHour);

		ois.close();
		fis.close();
	}
}
