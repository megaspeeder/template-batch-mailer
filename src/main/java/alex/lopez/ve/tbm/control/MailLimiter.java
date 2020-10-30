package alex.lopez.ve.tbm.control;

import java.io.Serializable;
import java.util.List;

import alex.lopez.ve.tbm.interfaces.MailBatchListener;
import alex.lopez.ve.tbm.model.Recipient;

public class MailLimiter implements MailBatchListener, Serializable {
	private static final long serialVersionUID = 1L;

	private static final int DEFAULT_MAIL_LIMIT = 450;

	private int sentMails;
	private int mailLimit;

	public MailLimiter() {
		sentMails = 0;
		mailLimit = DEFAULT_MAIL_LIMIT;
	}

	public void decreaseSentMails() {
		if (sentMails > 0)
			--sentMails;
	}

	public boolean hasExceededHourlyMails() {
		return sentMails > DEFAULT_MAIL_LIMIT;
	}

	public int getNumberAvailableMails() {
		return DEFAULT_MAIL_LIMIT - sentMails;
	}

	public int getMailLimit() {
		return mailLimit;
	}

	@Override
	public void onOneMailSent(MailBatch mailBatch, List<Recipient> sentRecipients) {
		sentMails += sentRecipients.size();
	}

	@Override
	public void onAllMailSent(MailBatch mailBatch, List<Recipient> sentRecipients) {
		sentMails += sentRecipients.size();
	}
}
