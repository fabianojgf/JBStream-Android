package org.jb.stream.processor;

import com.thoughtworks.xstream.XStream;

public class StreamProcessor {
    XStream stream;
    Class<?> type;

    public StreamProcessor(StreamDriveType streamDriveType) {
        super();
        stream = streamDriveType.getStreamDrive();
    }

    public void configure(Class<?> c) {
        StreamConfig config = new StreamConfig(stream);
        config.configure(c);
    }

    public Object read(String content) {
        return stream.fromXML(content);
    }

    public String write(Object object) {
        return stream.toXML(object);
    }
}
