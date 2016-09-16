package domain;

import java.math.BigDecimal;
import java.util.Set;

public class Item {
	
	private String name;
	private Long stockLevel;
	private BigDecimal price;
	private Set<Image> images;
	// TODO Category?
	
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
	
	public double getRating(){
		return 0.0;
	}
	
}
