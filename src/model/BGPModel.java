package ca.ubc.cs304.model;
public class BGPModel {
	private final String publisherName;
	private final String title;
	private final String genre;

	public BGPModel(String publisherName, String title, String genre) {
		this.publisherName = publisherName;
		this.title = title;
		this.genre = genre;
	}

	public String getpublisherName() {
		return publisherName;
	}

	public String getTitle() {
		return title;
	}

	public String getGenre() {
		return genre;
	}

}
