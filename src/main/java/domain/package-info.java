@XmlJavaTypeAdapters({
    @XmlJavaTypeAdapter(type=DateTime.class,
        value=DateTimeAdapter.class)
//    ,
//    @XmlJavaTypeAdapter(type=List.class,
//    	value=PurchaseHistoryAdapter.class)
})

package domain;

import java.util.List;

import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapters;
 
import jaxb.DateTimeAdapter;
import jaxb.PurchaseHistoryAdapter;

import org.joda.time.DateTime;