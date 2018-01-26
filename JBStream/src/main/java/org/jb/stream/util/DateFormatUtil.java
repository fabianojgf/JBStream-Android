package org.jb.stream.util;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.DomDriver;

import java.io.InputStream;

/**
 * Created by fabiano on 04/04/17.
 */

public class DateFormatUtil {
    public static String[] defaultFormats() {
        String file = "assets/date-formats.xml";
        InputStream in = DateFormatUtil.class.getClassLoader().getResourceAsStream(file);

        XStream stream = new XStream(new DomDriver());
        stream.alias("date-formats", String[].class);
        stream.alias("date-format", String.class);

        String[] dateFormats = (String[]) stream.fromXML(in);
        return dateFormats;
    }
}
