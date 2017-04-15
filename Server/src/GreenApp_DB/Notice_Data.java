package GreenApp_DB;

public class Notice_Data {

	private String title;
	private String content;
	private String url;
	private String date;
	
	public Notice_Data()
	{
		title = "";
		content = "";
		url = "";
		date = "";
	}
	
	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}
}
