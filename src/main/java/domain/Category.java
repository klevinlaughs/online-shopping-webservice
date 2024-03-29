package domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
@Entity
public class Category {

	@XmlAttribute(name = "id")
	@Id
	//@XmlID
	//@XmlJavaTypeAdapter(CategoryIdAdapter.class)
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(nullable = false, unique = true)
	private String name;
	// Persist the parentCategory too
	//@ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.PERSIST)
	//@XmlIDREF
	//private Category parentCategory;

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
//	public Category(String name, Category parentCategory) {
//		this.name = name;
//		this.parentCategory = parentCategory;
//	}

	public Long getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Override
	public String toString() {
		// if (parentCategory == null) {
		// return "Category:{ " + name;
		// } else {
		// return parentCategory.toString() + " > " + name + " }";
		// }
		return name;
	}

}
