package domain;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
@Embeddable
public class Image {

	@Column(nullable = false)
	private String name;
	private int width;
	private int height;

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
	public Image(String name, int width, int height) {
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

	public int getWidth() {
		return width;
	}

	public void setWidth(int width) {
		this.width = width;
	}

	public int getHeight() {
		return height;
	}

	public void setHeight(int height) {
		this.height = height;
	}
	
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("Image:{ name:" + name)
		.append(", width:" + width)
		.append(", height" + height)
		.append(" }");
		return sb.toString();
	}

}
