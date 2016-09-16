package domain;

import javax.xml.bind.annotation.XmlID;
import javax.xml.bind.annotation.XmlIDREF;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class Category {
	
	private Long id;
	@XmlID
	private String xmlId;
	
	private String name;
	@XmlIDREF
	private Category parentCategory;
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
		xmlId = getClass().getName() + ":" + id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public Category getParentCategory() {
		return parentCategory;
	}
	public void setParentCategory(Category parentCategory) {
		this.parentCategory = parentCategory;
	}

}
