package engine.xmlAdapters;

import javax.xml.bind.annotation.adapters.XmlAdapter;
import java.time.LocalDateTime;

public class XmlLocalDateTimeAdapter extends XmlAdapter<String, LocalDateTime> {
    @Override
    public LocalDateTime unmarshal(String v) throws Exception {
        return LocalDateTime.parse(v);
    }

    @Override
    public String marshal(LocalDateTime v) throws Exception {
        return v.toString();
    }
}
