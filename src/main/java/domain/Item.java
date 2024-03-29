package domain;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import jaxb.SingleImageAdapter;

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
@Entity
public class Item {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(nullable = false)
	private String name;
	private Integer stockLevel;
	@Column(nullable = false)
	private BigDecimal price;
	@XmlJavaTypeAdapter(SingleImageAdapter.class)
	// @XmlElementWrapper(name = "Images")
	// @XmlElement(name = "Image")
	@ElementCollection
	private Set<Image> images = new HashSet<Image>();
	@ManyToOne
	private Category category;
	private Boolean dealItem = false;

	/**
	 * Default constructor needed for persistent classes
	 */
	protected Item() {
	}

	public Item(String name) {
		this.name = name;
	}

	/**
	 * Creates an item with the required name and price fields
	 * 
	 * @param name
	 *            the name of the item
	 * @param price
	 *            the price of the item
	 */
	public Item(String name, BigDecimal price) {
		this.name = name;
		this.price = price;
	}

	public Item(String name, Integer stockLevel) {
		this(name);
		this.stockLevel = stockLevel;
	}

	public Item(String name, Category category) {
		this(name);
		this.category = category;
	}

	public Item(String name, Category category, Integer stockLevel) {
		this(name, category);
		this.stockLevel = stockLevel;
	}

	/**
	 * Adds the input image to the colleciton of images
	 * 
	 * @param image
	 *            the image to add
	 */
	public void addImage(Image image) {
		images.add(image);
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Integer getStockLevel() {
		return stockLevel;
	}

	public void setStockLevel(Integer stockLevel) {
		this.stockLevel = stockLevel;
	}

	public BigDecimal getPrice() {
		return price;
	}

	public void setPrice(BigDecimal price) {
		this.price = price;
	}

	public Set<Image> getImages() {
		return images;
	}

	public void setImages(Set<Image> images) {
		this.images = images;
	}

	public Category getCategory() {
		return category;
	}

	public void setCategory(Category category) {
		this.category = category;
	}

	public Boolean isDealItem() {
		return dealItem;
	}

	public void setDealItem(Boolean dealItem) {
		this.dealItem = dealItem;
	}

	/**
	 * Gets the rating for an item
	 * 
	 * @return
	 */
	public Double getRating() {
		// TODO
		return 0.0;
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("Item:{ id:" + id).append(", name:" + name)
				.append(", stockLevel:" + stockLevel)
				.append(", price:" + price).append(", images:{ ");
		Iterator<Image> imgIterator = images.iterator();
		while (imgIterator.hasNext()) {
			sb.append(imgIterator.next().toString() + ", ");
		}
		if (images.isEmpty()) {
			sb.append("}");
		} else {
			sb.replace(sb.length() - 2, sb.length() - 1, " }");
		}
		sb.append(", category:" + category).append(", dealItem:" + dealItem)
				.append(" }");
		return sb.toString();
	}

}
