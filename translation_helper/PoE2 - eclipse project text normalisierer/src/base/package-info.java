@XmlSchema(
    namespace = "",
    elementFormDefault = XmlNsForm.QUALIFIED,
    xmlns = {
    	@XmlNs(prefix="xsi",namespaceURI= "http://www.w3.org/2001/XMLSchema-instance"),
        @XmlNs(prefix="xsd", namespaceURI="http://www.w3.org/2001/XMLSchema")
    }
) 
package base;

import javax.xml.bind.annotation.XmlNs;
import javax.xml.bind.annotation.XmlSchema;
import javax.xml.bind.annotation.XmlNsForm;