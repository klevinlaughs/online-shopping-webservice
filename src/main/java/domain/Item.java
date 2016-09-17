package domain;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
@Entity
public class Item {

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE)
	private Long id;

	private String name;
	private Long stockLevel;
	private BigDecimal price;
	@XmlElementWrapper(name = "Images")
	@XmlElement(name = "Image")
	private Set<Image> images = new HashSet<Image>();
	private Category category;
	private boolean dealItem = false;

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

	public Long getStockLevel() {
		return stockLevel;
	}

	public void setStockLevel(Long stockLevel) {
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

	public boolean isDealItem() {
		return dealItem;
	}

	public void setDealItem(boolean dealItem) {
		this.dealItem = dealItem;
	}

	/**
	 * Gets the rating for an item
	 * 
	 * @return
	 */
	public double getRating() {
		// TODO
		return 0.0;
	}

}
