package alex.lopez.vega.view.listeners;

import java.io.IOException;

import javax.mail.MessagingException;
import javax.mail.internet.AddressException;

public interface SerializationListener {

	public void onSerialize() throws IOException;
	
	public void onDeserialize() throws IOException, ClassNotFoundException, AddressException, MessagingException;
	
}
