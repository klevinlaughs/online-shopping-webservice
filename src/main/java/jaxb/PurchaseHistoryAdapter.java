package jaxb;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.adapters.XmlAdapter;

import domain.Item;

public class PurchaseHistoryAdapter extends XmlAdapter<List<Item>, List<Item>> {

	@Override
	public List<Item> unmarshal(List<Item> v) throws Exception {
		return v;
	}

	@Override
	public List<Item> marshal(List<Item> v) throws Exception {
		List<Item> items = new ArrayList<Item>(5);
		for (int i = 0; i < 5; i++) {
			try {
				items.add(v.get(i));
			} catch (IndexOutOfBoundsException e) {
				break;
			}
		}
		return items;
	}

}
