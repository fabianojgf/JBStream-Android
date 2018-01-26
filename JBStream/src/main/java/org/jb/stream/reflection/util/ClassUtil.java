package org.jb.stream.reflection.util;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

public class ClassUtil {

	public static Class<?> getCollectionType(Type type) {
		if(type instanceof ParameterizedType) {
			ParameterizedType aType = (ParameterizedType) type;
			Type[] fieldArgTypes = aType.getActualTypeArguments();
			for (Type fieldArgType : fieldArgTypes) {
				Class<?> fieldArgClass = (Class<?>) fieldArgType;
				return fieldArgClass;
			}
		}
		return null;
	}
}
