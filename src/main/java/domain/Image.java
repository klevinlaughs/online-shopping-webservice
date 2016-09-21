package domain;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.xml.bind.annotation.XmlRootElement;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

@XmlRootElement
@Embeddable
public class Image {

	@Column(nullable = false)
	private String name;
	private Integer width;
	private Integer height;

	/**
	 * Default constructor needed for persistent classes
	 */
	protected Image() {
	}

	/**
	 * Takes required name for image
	 * 
	 * @param name
	 *            the name of the image
	 */
	public Image(String name) {
		this.name = name;
	}

	/**
	 * Takes required name of image and optional width and height dimensions
	 * 
	 * @param name
	 *            the name of the image
	 * @param width
	 *            the width of the image
	 * @param height
	 *            the height of the image
	 */
	public Image(String name, Integer width, Integer height) {
		this.name = name;
		this.width = width;
		this.height = height;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Integer getWidth() {
		return width;
	}

	public void setWidth(Integer width) {
		this.width = width;
	}

	public Integer getHeight() {
		return height;
	}

	public void setHeight(Integer height) {
		this.height = height;
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("Image:{ name:" + name).append(", width:" + width)
				.append(", height" + height).append(" }");
		return sb.toString();
	}

	@Override
	public boolean equals(Object obj) {
		if (!(obj instanceof Image))
			return false;
		if (obj == this)
			return true;

		Image rhs = (Image) obj;
		return new EqualsBuilder().append(name, rhs.name)
				.append(width, rhs.width).append(height, rhs.height).isEquals();
	}

	@Override
	public int hashCode() {
		return new HashCodeBuilder(17, 31).append(name).append(width)
				.append(height).toHashCode();
	}

}
