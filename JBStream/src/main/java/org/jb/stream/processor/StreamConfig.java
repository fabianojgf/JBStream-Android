package org.jb.stream.processor;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.converters.basic.DateConverter;

import org.jb.common.config.enums.MappingType;
import org.jb.common.dictionary.project.ProjectMapping;
import org.jb.common.dictionary.project.enums.ProjectLayer;
import org.jb.stream.annotation.StreamAttribute;
import org.jb.stream.annotation.StreamElement;
import org.jb.stream.annotation.StreamEntity;
import org.jb.stream.annotation.StreamEnumerated;
import org.jb.stream.annotation.StreamTemporal;
import org.jb.stream.annotation.StreamTransient;
import org.jb.stream.annotation.enums.EnumType;
import org.jb.stream.util.DateFormatUtil;
import org.jb.stream.converters.EnumOrdinalConverter;
import org.jb.stream.reflection.util.ClassUtil;
import org.jb.stream.xml.XMLStreamAttribute;
import org.jb.stream.xml.XMLStreamElement;
import org.jb.stream.xml.XMLStreamEntity;
import org.jb.stream.xml.XMLStreamEnumerated;
import org.jb.stream.xml.XMLStreamTemporal;
import org.jb.stream.xml.XMLStreamTransient;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by fabiano on 04/04/17.
 */

public class StreamConfig {
    List<String> visitedEntities;
    XStream stream;

    public StreamConfig(XStream stream) {
        super();
        this.stream = stream;
        this.visitedEntities = new ArrayList<String>();

        registerDefaultConverters();
    }

    public void configure(Class<?> c) {
        if(c == null)
            return;

        if(visitedEntities.contains(c.getName())) {
            return;
        }
        else {
            /** Entity */
            configureElement(c);

            /** Attributes */
            for (int i = 0; i < c.getDeclaredFields().length; i++) {
                Field field = c.getDeclaredFields()[i];
                configureElement(field);
            }
        }
    }

    public void configureElement(Class<?> c) {
        XMLStreamEntity anEntity = null;
        if(ProjectMapping.getMappingType(c.getClassLoader(), ProjectLayer.PERSISTENCE)
                == MappingType.ANNOTATION) {
            anEntity = XMLStreamEntity.adapt(c.getAnnotation(StreamEntity.class));
        }
        else if(ProjectMapping.getMappingType(c.getClassLoader(), ProjectLayer.PERSISTENCE)
                == MappingType.XML) {
            anEntity = XMLStreamEntity.getAnnotation(c);
        }
        else {
            //DO NOTHING
        }

        if(anEntity != null) {
            stream.alias(anEntity.getName(), c);
            if (visitedEntities.isEmpty()) {
                stream.alias(anEntity.getCollectionName(), List.class);
            }
        }

        /** Add entity to visited list */
        visitedEntities.add(c.getName());
    }

    @SuppressWarnings("unchecked")
    public void configureElement(Field field) {
        /** Required */
        XMLStreamTransient aTransient = null;
        XMLStreamAttribute anAttribute = null;
        XMLStreamElement anElement = null;
        /** Optional */
        XMLStreamTemporal aTemporal = null;
        XMLStreamEnumerated anEnumerated = null;

        if(ProjectMapping.getMappingType(field.getDeclaringClass().getClassLoader(), ProjectLayer.PERSISTENCE)
                == MappingType.ANNOTATION) {
            /** Required */
            aTransient = XMLStreamTransient.adapt(field.getAnnotation(StreamTransient.class));
            anAttribute = XMLStreamAttribute.adapt(field.getAnnotation(StreamAttribute.class));
            anElement = XMLStreamElement.adapt(field.getAnnotation(StreamElement.class));
            /** Optional */
            aTemporal = XMLStreamTemporal.adapt(field.getAnnotation(StreamTemporal.class));
            anEnumerated = XMLStreamEnumerated.adapt(field.getAnnotation(StreamEnumerated.class));
        }
        else if(ProjectMapping.getMappingType(field.getDeclaringClass().getClassLoader(), ProjectLayer.PERSISTENCE)
                == MappingType.XML) {
            /** Required */
            aTransient = XMLStreamTransient.getAnnotation(field);
            anAttribute = XMLStreamAttribute.getAnnotation(field);
            anElement = XMLStreamElement.getAnnotation(field);
            /** Optional */
            aTemporal = XMLStreamTemporal.getAnnotation(field);
            anEnumerated = XMLStreamEnumerated.getAnnotation(field);
        }
        else {
            //DO NOTHING
        }

        /** Required */

        if(aTransient != null) {
            stream.omitField(field.getDeclaringClass(), field.getName());
        }
        else if(anAttribute != null) {
            stream.aliasAttribute(field.getDeclaringClass(), field.getName(), anAttribute.getName());
        }
        else if(anElement != null) {
            stream.aliasField(anElement.getName(), field.getDeclaringClass(), field.getName());
        }
        else {
            stream.aliasField(field.getName(), field.getDeclaringClass(), field.getName());
        }

        /** Optional */

        if(field.getType().isAssignableFrom(Date.class)) {
            if(aTemporal != null) {
                stream.registerLocalConverter(field.getDeclaringClass(),
                        field.getName(), new DateConverter(aTemporal.getPattern(), DateFormatUtil.defaultFormats()));
            }
        }

        if(field.getType().isEnum()) {
            if(anEnumerated != null) {
                if(anEnumerated.getValue() == EnumType.ORDINAL) {
                    stream.registerLocalConverter(field.getDeclaringClass(),
                            field.getName(), new EnumOrdinalConverter((Class<? extends Enum<?>>) field.getType()));
                }
            }
        }

        /** Attribute Type is a Class */

        if(field.getType().isAnnotationPresent(StreamEntity.class)
                || XMLStreamEntity.isAnnoted(field.getType())) {
            configure(field.getType());
        }
        else if(field.getType().isAssignableFrom(List.class)) {
            Class<?> c = ClassUtil.getCollectionType(field.getType());
            if(c != null && (c.isAnnotationPresent(StreamEntity.class)
                    || XMLStreamEntity.isAnnoted(c))) {
                configure(c);
            }
            else {
                //Do nothing!!!
            }
        }
        else {
            //Do nothing!!!
        }
    }

    private void registerDefaultConverters() {
        if(stream != null) {
            /** Date Converter */
            String[] dateFormats = DateFormatUtil.defaultFormats();
            stream.registerConverter(new DateConverter(dateFormats[0], dateFormats));
        }
    }
}