package org.jb.stream.reflection.util;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

public class FieldUtil {
	public static Method getSetMethod(Field field) throws NoSuchMethodException, SecurityException {
		String fieldName = field.getName();
		fieldName = "set" + fieldName.substring(0, 1).toUpperCase() + fieldName.substring(1);
		
		Method setter = field.getDeclaringClass().getMethod(fieldName, field.getType());
		
		return setter;
	}
	
	public static Method getGetMethod(Field field) throws NoSuchMethodException, SecurityException {
		String fieldName = field.getName();
		fieldName = "get" + fieldName.substring(0, 1).toUpperCase() + fieldName.substring(1);
		
		Method setter = field.getDeclaringClass().getMethod(fieldName);
		
		return setter;
	}
	
	public static Class<?> getCollectionType(Field field) {
		Type genericFieldType = field.getGenericType();
		
		if(genericFieldType instanceof ParameterizedType){
		    ParameterizedType aType = (ParameterizedType) genericFieldType;
		    Type[] fieldArgTypes = aType.getActualTypeArguments();
		    for(Type fieldArgType : fieldArgTypes){
		        Class<?> fieldArgClass = (Class<?>) fieldArgType;
		        return fieldArgClass;
		    }
		}
		return null;
	}
}
