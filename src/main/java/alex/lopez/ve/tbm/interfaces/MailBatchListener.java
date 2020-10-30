package alex.lopez.ve.tbm.interfaces;

import java.util.List;

import alex.lopez.ve.tbm.control.MailBatch;
import alex.lopez.ve.tbm.model.Recipient;

public interface MailBatchListener {

	void onOneMailSent(MailBatch mailBatch, List<Recipient> sentRecipients);

	void onAllMailSent(MailBatch mailBatch, List<Recipient> sentRecipients);

}
