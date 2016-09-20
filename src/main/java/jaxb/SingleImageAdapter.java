package jaxb;

import java.util.HashSet;
import java.util.NoSuchElementException;
import java.util.Set;

import javax.xml.bind.annotation.adapters.XmlAdapter;

import domain.Image;

public class SingleImageAdapter extends XmlAdapter<Image, Set<Image>> {

	@Override
	public Set<Image> unmarshal(Image v) throws Exception {
		Set<Image> images = new HashSet<Image>();
		images.add(v);
		return images;
	}

	@Override
	public Image marshal(Set<Image> v) throws Exception {
		try {
			return v.iterator().next();
		} catch (NoSuchElementException e) {
			return null;
		}
	}

}
