package jaxb;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.adapters.XmlAdapter;

import domain.Item;

/**
 * An XmlAdapter to return a limited amount of items from a user's
 * purchaseHistory. The marshalled type can't be an interface
 * 
 * @author Kelvin
 *
 */
public class PurchaseHistoryAdapter extends XmlAdapter<ArrayList<Item>, List<Item>> {

	@Override
	public List<Item> unmarshal(ArrayList<Item> v) throws Exception {
		return v;
	}

	@Override
	public ArrayList<Item> marshal(List<Item> v) throws Exception {
		ArrayList<Item> items = new ArrayList<Item>(5);
		for (int i = 0; i < 5; i++) {
			try {
				items.add(v.get(i));
			} catch (IndexOutOfBoundsException e) {
				// If less than 5, then return all that
				break;
			}
		}
		return items;
	}

}
