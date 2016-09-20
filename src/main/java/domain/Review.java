package domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.xml.bind.annotation.XmlRootElement;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;

@XmlRootElement
@Entity
public class Review {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@ManyToOne(fetch = FetchType.LAZY)
	private Customer reviewer;
	@ManyToOne(fetch = FetchType.LAZY)
	private Item item;
	@Column(nullable = false)
	private Double starRating;
	private String comment;
	private DateTime dateTime;

	/**
	 * Default constructor needed for persistent classes
	 */
	protected Review() {
	}

	/**
	 * Initializes the required fields
	 * 
	 * @param reviewer
	 * @param item
	 * @param starRating
	 */
	public Review(Customer reviewer, Item item, Double starRating) {
		this.reviewer = reviewer;
		this.item = item;
		this.starRating = starRating;
		dateTime = new DateTime();
	}

	/**
	 * Initializes the required fields with an optional comment about the
	 * reviewed item
	 * 
	 * @param reviewer
	 * @param item
	 * @param starRating
	 * @param comment
	 */
	public Review(Customer reviewer, Item item, Double starRating,
			String comment) {
		this(reviewer, item, starRating);
		this.setComment(comment);
	}

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

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

	public DateTime getDateTime() {
		return dateTime;
	}

	public void setDateTime(DateTime dateTime) {
		this.dateTime = dateTime;
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("Review:{ id:" + id)
				.append(", reviewer:" + reviewer.getUserName())
				.append(", item:" + item.getName())
				.append(", starRating:" + starRating)
				.append(", comment:" + comment)
				.append(", dateTime:"
						+ dateTime.toString(DateTimeFormat
								.forPattern("[dd/MM/yyyy HH:mm:ss]")))
				.append(" }");
		return sb.toString();
	}

}
