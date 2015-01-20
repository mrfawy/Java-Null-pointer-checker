package com.mrfawy.npc.traverse;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


public class ClassUtil {
	
	private static final Object[] EMPTY_VALUES = {};
	private static final Class<?>[]  EMPTY_ARGUMENTS = {};
	private static final Set<Class<?>> WRAPPER_TYPES = getWrapperTypes();		//used to check whether a field is declared as a wrapper or not.
	
	public static List<Field> getInheritedAndInstanceFields(Class<?> objectClass) {
		 List<Field> result = new ArrayList<Field>();
		 boolean isParent = false ;

		    Class<?> i = objectClass;
		    while (i != null && i != Object.class) {
		    	
		    	//During first iteration it will get fields of the passed class.
		    	if(isValidPackage(i)){
			    	Field[] declaredFields = i.getDeclaredFields();
					for (Field field : declaredFields) {
						if (!field.isSynthetic()) {
							if(isParent){
					//			if(Modifier.isPrivate(field.getModifiers())){
					//				continue ;
					//			}
							}
							if(!Modifier.isFinal(field.getModifiers())){
								result.add(field);
							}
							
						}
					}
		    	}
		    	
		    	//Once we get the fields of the current class above we point 'i' to parentClass if 'valid' parent Class if found, else we terminate by pointing 'i' to null
				if(isValidPackage(i)){
					i = i.getSuperclass();
					if(i==Object.class){
						i=null ;
					}
					isParent = true ;
				}else{
				   	i=null;
				}
			}
		    return result;
	}

	private static boolean isValidPackage(Class<?> className) {
		return className.getPackage().getName().contains("com.allied")||className.getPackage().getName().contains("com.nationwide")||className.getPackage().getName().contains("org.oasis_open");
	}
	
	//--------------------Set Field Value Start---------------------------------------------------------------------
	
	public static void set(Object bean, Field field, Object value,Integer depth,ObjectInfo objInfo) {
		if(value!=null){
			String setterMethod = createSetterMethodName(field);
			Class<?> beanClass = bean.getClass();
			
			Class<?>[] arguments = { field.getType() };
			try {
				Method method = findMethod(setterMethod, beanClass, arguments);
				if (method == null){
					setValueThroughReflection(bean, field, value,depth);
				}else{
					invoke(bean, method,value);
				}
			} catch (Exception e) {
				setValueThroughReflection(bean, field, value,depth);
				
			}
			
			//System.out.println("Setting value for Class : "+beanClass+": fieldName : "+field.getName());
		}
	}


	private static void setValueThroughReflection(Object bean, Field field,Object value,Integer depth) {
//		System.out.println("depth : "+depth+" : No setter found for Class : "+bean.getClass()+" : Field : " +field.getName()+ " : Setting Value through Reflection.");
		field.setAccessible(true) ;
		try {
			field.set(bean, value) ;
		} catch (IllegalArgumentException e1) {
			System.out.println("depth : "+depth+" : Class : "+bean.getClass()+" : Field : " +field.getName()+ " : Value not Set");
		} catch (IllegalAccessException e1) {
			System.out.println("depth : "+depth+" : Class : "+bean.getClass()+" : Field : " +field.getName()+ " : Value not Set");
		}
	}
	
	private static Method findMethod(String methodName, Class<?> type,
			Class<?>[] arguments) throws Exception {
		try {
			return type.getDeclaredMethod(methodName, arguments);
		} catch (Exception e) {
			throw new Exception(e);
		}
	}
	
	private static void invoke(Object bean, Method method, Object value) throws Exception {
		Object[] values = { value };
		invoke(bean, method, values);
	}
	
	private static Object invoke(Object object, Method method, Object[] values) throws Exception {
		try {
			return method.invoke(object, values);
		} catch (Exception e) {
			throw new Exception(method.getName(), e);
		}
	}
	
	private static String createSetterMethodName(Field field) {
		if (field.getType().equals(boolean.class)) {
			if (field.getName().startsWith("is"))
				return "set" + field.getName().substring(2);
			else
				return "set" + upperFirst(field.getName());
		} else
			return "set" + upperFirst(field.getName());
	}
	
	private static String upperFirst(String fieldName) {
		return fieldName.substring(0, 1).toUpperCase() + fieldName.substring(1);
	}
	
	
	//--------------------Set Field Value End---------------------------------------------------------------------
	
	//--------------------Get Field Value Start---------------------------------------------------------------------
	
	public static Object get(Object bean, Field field,Integer depth) {
		Object o = null ;
		try {
			String getterMethod = createGetterMethodName(field);
			Class<?> beanClass = bean.getClass();
			 Method method = findMethod(getterMethod, beanClass, EMPTY_ARGUMENTS);
			o = invoke(bean, method, EMPTY_VALUES);
		} catch (Exception e) {
			o = getValueThroughReflection(bean, field, depth);
			
		}
		return o ;
	}
	
	private static String createGetterMethodName(Field field) {
		if (field.getType().equals(boolean.class)) {
			if (field.getName().startsWith("is"))
				return field.getName();
			else
				return "is" + upperFirst(field.getName());
		} else
			return "get" + upperFirst(field.getName());
	}
	
	private static Object getValueThroughReflection(Object bean, Field field,Integer depth) {
//		System.out.println("depth : "+depth+" : No getter found for Class : "+bean.getClass()+" : Field : " +field.getName()+ " : Getting Value through Reflection.");
		field.setAccessible(true) ;
		try {
			return field.get(bean) ;
		} catch (IllegalArgumentException e1) {
			System.out.println("depth : "+depth+" : Class : "+bean.getClass()+" : Field : " +field.getName()+ " : Value not found");
		} catch (IllegalAccessException e1) {
			System.out.println("depth : "+depth+" : Class : "+bean.getClass()+" : Field : " +field.getName()+ " : Value not found");
		}
		return null ;
	}
	
	public static Object runGetter(Object o,Field field)
	{
	    // MZ: Find the correct method
	    for (Method method : o.getClass().getMethods())
	    {
	        if ((method.getName().startsWith("get")) && (method.getName().length() == (field.getName().length() + 3)))
	        {
	            if (method.getName().toLowerCase().endsWith(field.getName().toLowerCase()))
	            {
	                // MZ: Method found, run it
	                try
	                {
	                    return method.invoke(o);
	                }
	                catch (IllegalAccessException e)
	                {
	                    System.out.println("Could not determine method: " + method.getName());
	                }
	                catch (InvocationTargetException e)
	                {
	                	 System.out.println("Could not determine method: " + method.getName());
	                }

	            }
	        }
	    }


	    return null;
	}
	//--------------------Get Field Value End---------------------------------------------------------------------
	
	
	//-----------------------------For primitive fields---------------------------------------------------------
	public static boolean checkPrimitive(Class<?> c) {
		
		if(c.isPrimitive() || isWrapper(c) || c.getSimpleName().contains("XMLGregorianCalendar") || c.isEnum()) {
			return true;
		}
		return false ;
	}


	//Helper method for checkPrimitive(). This is used to find out if the class is a wrapper type or not to a primitive type
	public static boolean isWrapper(Class<?> c) {
		        return WRAPPER_TYPES.contains(c);
	}
	
	//Helper to isWrapper() method. Contains all the wrapper class. NOTE: if some class is not a wrapper but we want to deal with it like we deal primitive types we can add them here.
	private static Set<Class<?>> getWrapperTypes()
    {
        Set<Class<?>> ret = new HashSet<Class<?>>();
        ret.add(Boolean.class);
        ret.add(Character.class);
        ret.add(Byte.class);
        ret.add(Short.class);
        ret.add(Integer.class);
        ret.add(Long.class);
        ret.add(Float.class);
        ret.add(Double.class);
        ret.add(Void.class);
        ret.add(String.class) ;
        ret.add(BigDecimal.class) ;
        ret.add(BigInteger.class) ;
        ret.add(Date.class) ;
        ret.add(javax.xml.bind.JAXBElement.class) ;
        return ret;
    }
	
	 public static String RemoveLineTerminationCharacterss(String inputText){
		 if(inputText != null){
		   return inputText.replaceAll("[\\r\\n\\t]+\\s", "");
		 }
		 return null;
		   }
	
	
	public static <U> boolean isCollection(Class<U> fieldClass) {
		return Collection.class.isAssignableFrom(fieldClass);
	}
}
