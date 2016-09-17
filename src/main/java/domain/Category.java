package domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlID;
import javax.xml.bind.annotation.XmlIDREF;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
@Entity
public class Category {

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE)
	private Long id;
	@XmlID
	private String xmlId;

	@Column(nullable = false)
	private String name;
	@XmlIDREF
	private Category parentCategory;

	/**
	 * Default constructor needed for persistent classes
	 */
	protected Category() {
	}

	/**
	 * Creates a category with no parent. Parent can be set
	 * 
	 * @param name
	 *            the name of the category
	 */
	public Category(String name) {
		this.name = name;
	}

	/**
	 * Creates a sub-category under the specified parent category
	 * 
	 * @param name
	 *            the name of the category
	 * @param parentCategory
	 *            the parent category
	 */
	public Category(String name, Category parentCategory) {
		this.name = name;
		this.parentCategory = parentCategory;
	}

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
