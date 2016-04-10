package szu.wifichat.android.entity;

public class MyNews {

	private String title;
	private String description;
	private String imgURL;
	private String pubDate;

	public MyNews(String title, String description, String imgURL, String pubDate) {
		super();
		this.title = title;
		this.description = description;
		this.imgURL = imgURL;
		this.pubDate = pubDate;
	}

	public MyNews() {
		super();
		// TODO Auto-generated constructor stub
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getImgURL() {
		return imgURL;
	}

	public void setImgURL(String imgURL) {
		this.imgURL = imgURL;
	}

	public String getPubDate() {
		return pubDate;
	}

	public void setPubDate(String pubDate) {
		this.pubDate = pubDate;
	}

	@Override
	public String toString() {
		return "MyNews [title=" + title + ", description=" + description + ", imgURL=" + imgURL + ", pubDate=" + pubDate + "]";
	}

}
