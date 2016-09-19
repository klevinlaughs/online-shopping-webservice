package jaxb;

import javax.xml.bind.annotation.adapters.XmlAdapter;

/**
 * An adapter for XmlID and XmlIDREF annotations in Category, converts the Long
 * id to a String as required by jaxb
 * 
 * @author Kelvin Lau
 *
 */
public class CategoryIdAdapter extends XmlAdapter<String, Long> {

	@Override
	public Long unmarshal(String v) throws Exception {
		return Long.valueOf(v.substring(9));
	}

	@Override
	public String marshal(Long v) throws Exception {
		StringBuilder sb = new StringBuilder();
		sb.append("Category:");
		sb.append(v.toString());
		return sb.toString();
	}

}
