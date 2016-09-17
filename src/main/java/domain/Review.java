package domain;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.xml.bind.annotation.XmlRootElement;

import org.joda.time.DateTime;

@XmlRootElement
@Entity
public class Review {

	@Id
	private Long id;
	@ManyToOne(fetch=FetchType.LAZY)
	private Customer reviewer;
	@ManyToOne(fetch=FetchType.LAZY)
	private Item item;
	private Double starRating;
	private DateTime dateTime;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
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
