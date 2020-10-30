package alex.lopez.ve.tbm.control;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.util.LinkedList;
import java.util.List;

import alex.lopez.ve.tbm.interfaces.IControllerAccess;
import alex.lopez.ve.tbm.interfaces.TBMWindowListener;
import alex.lopez.ve.tbm.model.Recipient;
import alex.lopez.ve.util.MyFileUtils;

public class Mailer implements TBMWindowListener {
	private IControllerAccess ica;
	private List<MailBatch> scheduledBatchQueue;

	public Mailer(IControllerAccess ica) {
		this.ica = ica;
		scheduledBatchQueue = new LinkedList<MailBatch>();

		loadBatches();
	}

	public void sendOne() {
		if(scheduledBatchQueue.isEmpty())
			return;
		
		MailBatch batch = scheduledBatchQueue.get(0);

		if (batch.getCurrentRecipientIndex() >= batch.getRecipients().size()) {
			scheduledBatchQueue.remove(0);
			batch = scheduledBatchQueue.get(0);
		}

		if (!batch.sendNextRecipient())
			scheduledBatchQueue.remove(0);
	}

	public void sendWholeBatch() {
		if(scheduledBatchQueue.isEmpty())
			return;
		
		MailBatch batch = scheduledBatchQueue.get(0);
		batch.sendAll();

		scheduledBatchQueue.remove(0);
	}

	public void scheduleSendMessage() {
		List<MailBatch> newBatches = processBatches(ica.getRecipients());

		for (MailBatch batch : newBatches) {
			scheduledBatchQueue.add(batch);
		}
	}

	private List<MailBatch> processBatches(List<Recipient> recipients) {
		List<MailBatch> newBatches = new LinkedList<MailBatch>();
		List<Recipient> processedRecipients = new LinkedList<Recipient>();

		final int batchSize = ica.getMailLimit();

		for (int i = 0; i < recipients.size(); i += batchSize) {
			int upperBound = (i + batchSize > recipients.size()) ? recipients.size() : i + batchSize;

			List<Recipient> subList = new LinkedList<Recipient>(recipients.subList(i, upperBound));

			MailBatch batch = new MailBatch(subList, ica.getTemplate());

			newBatches.add(batch);
			processedRecipients.addAll(subList);
		}

		ica.removeRecipients(processedRecipients);

		return newBatches;

		// --------------------------------------------------

		/*
		 * List<MailBatch> batchQueue = new LinkedList<MailBatch>(); Template template =
		 * ica.getTemplate();
		 * 
		 * int numberOfRecipients = recipients.size(); int batchSize =
		 * ica.getMailLimit(); int numBatches = (int) Math.ceil((double)
		 * numberOfRecipients / (double) batchSize);
		 * 
		 * for (int processedBatches = 0; processedBatches < numBatches;
		 * ++processedBatches) { boolean wholeBatch = (numberOfRecipients -
		 * processedBatches * batchSize) > batchSize; int lowerBound = processedBatches
		 * * batchSize; int upperBound = (wholeBatch) ? batchSize : recipients.size();
		 * 
		 * List<Recipient> batchRecipients = new
		 * LinkedList<Recipient>(recipients.subList(lowerBound, upperBound));
		 * 
		 * MailBatch batch = new MailBatch(batchRecipients, template);
		 * 
		 * batchQueue.add(batch);
		 * 
		 * ica.removeRecipients(batchRecipients); }
		 * 
		 * return batchQueue;
		 */
	}

	private void saveBatches() {
		File batchesFile = new File(MyFileUtils.getWorkingDirectory() + "\\template-batch-mailer\\batches.txt");

		try {
			if (!batchesFile.createNewFile()) {
				OutputStream out = new FileOutputStream(batchesFile);
				ObjectOutputStream oos = new ObjectOutputStream(out);

				oos.writeObject(scheduledBatchQueue);

				oos.close();
				out.close();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@SuppressWarnings("unchecked")
	private void loadBatches() {
		File workingDirectory = new File(MyFileUtils.getWorkingDirectory() + "\\template-batch-mailer");

		if (workingDirectory.mkdir())
			System.out.println("Created roaming data file");

		try {
			File batchesFile = new File(MyFileUtils.getWorkingDirectory() + "\\template-batch-mailer\\batches.txt");

			if (!batchesFile.createNewFile()) {
				InputStream in = new FileInputStream(batchesFile);
				ObjectInputStream ois = new ObjectInputStream(in);

				List<MailBatch> batchList = (List<MailBatch>) ois.readObject();

				scheduledBatchQueue.addAll(0, batchList);

				ois.close();
				in.close();
			}
		} catch (IOException | ClassNotFoundException e) {
			e.printStackTrace();
		}

	}

	@Override
	public void onWindowClosing(IControllerAccess ica) {
		saveBatches();
	}

	public boolean canSendWholeBatch() {
		return scheduledBatchQueue.get(0).getRemainingRecipients().size() < ica.getNumberAvailableMails();
	}
}
