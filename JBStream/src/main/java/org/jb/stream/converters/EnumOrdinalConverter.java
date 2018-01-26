package org.jb.stream.converters;

import com.thoughtworks.xstream.converters.enums.EnumSingleValueConverter;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * Created by fabiano on 04/04/17.
 */

public class EnumOrdinalConverter extends EnumSingleValueConverter {
    Class<?> type;

    public EnumOrdinalConverter(Class<? extends Enum<?>> type) {
        super(type);
        this.type = type;
    }

    public Object fromString(String rt) {
        Method m = null;
        try {
            m = type.getMethod("values");
            Enum<?>[] enums = (Enum<?>[]) m.invoke(type);
            return enums[Integer.parseInt(rt)];
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return null;
    }

    public String toString(Object obj) {
        try {
            Method m = type.getMethod("ordinal");
            int e = (int) m.invoke(obj);
            return String.valueOf(e);
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return null;
    }
}
