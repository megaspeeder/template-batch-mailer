package alex.lopez.vega.model;

public class Template {

	private String subject;
	private String content;

	public Template() {
		subject = "";
		content = "";
	}

	public void insertContent(String string) {
		content += "{" + string + "}";
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
