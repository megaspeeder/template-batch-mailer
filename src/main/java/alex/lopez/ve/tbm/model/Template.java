package alex.lopez.ve.tbm.model;

import java.io.Serializable;

public class Template implements Serializable {
	private static final long serialVersionUID = 1L;
	
	private String subject;
	private String content;

	public Template() {
		this.subject = "";
		this.content = "";
	}

	public Template(String subject, String content) {
		this.subject = subject;
		this.content = content;
	}

	public String getSubject() {
		return subject;
	}

	public void setSubject(String subject) {
		this.subject = subject;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

}
