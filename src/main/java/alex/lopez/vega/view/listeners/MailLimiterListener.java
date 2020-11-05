package alex.lopez.vega.view.listeners;

public interface MailLimiterListener {

	void onNumSentMailsChanged(int mailRateLimit, int numSentMails);
	
}
