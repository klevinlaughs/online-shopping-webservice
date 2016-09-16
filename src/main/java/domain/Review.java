package domain;

import javax.xml.bind.annotation.XmlRootElement;

import org.joda.time.DateTime;

@XmlRootElement
public class Review {

	private Customer reviewer;
	private Item item;
	private Double starRating;
	private DateTime dateTime;

	protected Review() {
	}

	public Review(Customer reviewer, Item item, double starRating) {
		this.reviewer = reviewer;
		this.item = item;
		this.starRating = starRating;
		dateTime = new DateTime();
	}

	public Customer getReviewer() {
		return reviewer;
	}

	public void setReviewer(Customer reviewer) {
		this.reviewer = reviewer;
	}

	public Item getItem() {
		return item;
	}

	public void setItem(Item item) {
		this.item = item;
	}

	public Double getStarRating() {
		return starRating;
	}

	public void setStarRating(Double starRating) {
		this.starRating = starRating;
	}

	public DateTime getDateTime() {
		return dateTime;
	}

	public void setDateTime(DateTime dateTime) {
		this.dateTime = dateTime;
	}

}
