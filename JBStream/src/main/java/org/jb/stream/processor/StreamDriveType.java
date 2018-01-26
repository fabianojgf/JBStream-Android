package org.jb.stream.processor;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.json.JettisonMappedXmlDriver;
import com.thoughtworks.xstream.io.json.JsonHierarchicalStreamDriver;
import com.thoughtworks.xstream.io.xml.DomDriver;
import com.thoughtworks.xstream.io.xml.StaxDriver;

/**
 * Created by fabiano on 04/04/17.
 */

public enum StreamDriveType {
    XML {
        @Override
        public XStream getStreamDrive() {
            return new XStream(new DomDriver());
        }
    },
    XML_DOM {
        @Override
        public XStream getStreamDrive() {
            return new XStream(new DomDriver());
        }
    },
    XML_STAX {
        @Override
        public XStream getStreamDrive() {
            return new XStream(new StaxDriver());
        }
    },
    JSON {
        @Override
        public XStream getStreamDrive() {
            return new XStream(new JsonHierarchicalStreamDriver());
        }
    },
    JSON_HIERARCHICAL {
        @Override
        public XStream getStreamDrive() {
            return new XStream(new JsonHierarchicalStreamDriver());
        }
    },
    JSON_JETTISON_MAPPED_XML {
        @Override
        public XStream getStreamDrive() {
            return new XStream(new JettisonMappedXmlDriver());
        }
    };

    public abstract XStream getStreamDrive();
}
