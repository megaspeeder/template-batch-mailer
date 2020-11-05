package alex.lopez.vega.model;

import java.io.Serializable;

public class Message implements Serializable {
	private static final long serialVersionUID = 1L;

	private String fromAddress;
	private String toAddress;
	private String subject;
	private String content;

	public Message(String fromAddress, String toAddress, String subject, String content) {
		this.fromAddress = fromAddress;
		this.toAddress = toAddress;
		this.subject = subject;
		this.content = content;
	}

	public String getFromAddress() {
		return fromAddress;
	}

	public String getToAddress() {
		return toAddress;
	}

	public String getSubject() {
		return subject;
	}

	public String getContent() {
		return content;
	}

}
