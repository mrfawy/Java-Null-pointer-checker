package com.mrfawy.npc.compare;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import javax.xml.bind.JAXBElement;

import com.mrfawy.npc.traverse.ClassUtil;



/**
 * 
 * @author <b>Rushi Desai</b>.</br>
 * This class will recursively check between two objects of same class to check if both are same or not
 * Currently it only handles Lists in Collection. Further Collections will be added as per needed. 
 *
 */
public class ReflectiveCompareObject {
	
	private static int depth = 0 ;
//	private static int depthDecrement = 0 ;
	private static final Set<Class<?>> WRAPPER_TYPES = getWrapperTypes();
	
	/**
	 * 
	 * @param <T>
	 * @param obj1
	 * @param obj2
	 * 
	 * NOTE: obj1 and obj2 have to be Objects of same class.
	 * @return
	 * @throws NoSuchMethodException
	 * @throws InvocationTargetException
	 */
	public static <T> boolean compareDeepObjectEquality(T obj1 ,T obj2){
		depth += 1 ;
		if(obj1==null && obj2==null){depth -= 1;return true ;}  //if both obj1 and obj2 are null return true.
		
		if((obj1==null && obj2!=null) || (obj1!=null && obj2==null)){printStat(obj1!=null?obj1.getClass():obj2.getClass());depth -= 1; 
		return false ;}			//if Only one of them is null return false. 

		if(obj1!=null && obj2 !=null){
			
			Class<?> obj1Class = obj1.getClass();
			Class<?> obj2Class = obj2.getClass();			
			
			if(obj1Class!=obj2Class){     	//both don't belong to same class
				printStat(obj1.getClass()) ;
				depth -= 1;
				return false ;
			}else{							//both belong to same class
				if(obj1==obj2){				//References are same so objects are equal
					return true ;
				}else{						//References are not same so do a deep compare
					depth -= 1;
					return checkEqualitybyDeepCompare(obj1,obj2,obj1Class) ;    //return true or false whatever it returns ;
				}
			}
		}
		//this is dummy. Code can never reach here. Needed to satisfy Compiler.
			printStat(obj1.getClass()) ;depth -= 1;
			return false ;					
	}


	//Both Classes are same.
	private static <T> boolean checkEqualitybyDeepCompare(T obj1,T obj2,Class<?> objClass){
		
		
		if(checkPrimitive(obj1,obj2)){						//at this point both the Objects are not null and both belong to same class.
			return handlePrimitive(obj1, obj2,objClass) ; //if fields are not equal return false. If fields are equal continue more cheking till we find even one field not equal.
		}else{											//if Not primitive means this is a class we need to iterate through the fields	
		//	Field[] f = objClass.getDeclaredFields() ;
			List<Field> fList = ClassUtil.getInheritedAndInstanceFields(objClass) ;
			try {
				Field f2 ;
				for(Field f1:fList){
					
					if(f1!=null){
						f2 = f1;
					}else continue ;
					List<String> ignorableFields = new ArrayList<String>();
					ignorableFields.add("systemTime");
					ignorableFields.add("totalElapsedTime");
					ignorableFields.add("integrityTimeStamp");
					if(f1!=null && f2!=null){				//Handle primitive fields
						f1.setAccessible(true) ;
						f2.setAccessible(true) ;
						
						Object memberObject1 = f1.get(obj1) ;
						Object memberObject2 = f2.get(obj2) ;
//						Class<?> member1Class = f1.getDeclaringClass() ;
//						Class<?> member2Class = f2.getDeclaringClass() ; 

						if((memberObject1==null && memberObject2 == null) || ignorableFields.contains(f1.getName())){					//if both null means objects equal hence donot do further testing skip inside if and go outside
							continue;
						}
						
						
//We are in a loop. Hence we cannot return true unless all the fields are same. Thus we return true only after loop completes. Inside the loop we will only return false. As if one field is false objects are not equal.
						
				/*		if((memberObject1!=null&&memberObject2==null) || (memberObject1==null&&memberObject2!=null)){printStat(obj1.getClass(),4) ;
						return false;}
				*/		
						//--------------------------
						if(memberObject1!=null&&memberObject2!=null){
							Class<?> memberClass = memberObject1.getClass() ;
							if(checkPrimitive(memberObject1,memberObject2)){						//at this point both the Objects are not null and both belong to same class.
								boolean primitiveEqual = handlePrimitive(memberObject1, memberObject2,memberClass) ; //if fields are not equal return false. If fields are equal continue more cheking till we find even one field not equal.
								if(!primitiveEqual){
									printStat(objClass,f1) ;		//Here we pass objClass because the fields which didnot match are contained in objClass.
									return false ;				//again we need to do this since we donot want return true unless all the fields are broken.
								}
							}
							
							if(!handleNonPrimitiveFields(memberObject1,memberObject2)){
								printStat(objClass,f1) ;		//Here we pass objClass because the fields which didnot match are contained in objClass.
								return false ;
							}
						}else{
							Class<?> c = null ;
							int i =0 ;
							if(memberObject1!=null){								//String has to be dealt specially Hence we need to filter String.class out
								c = memberObject1.getClass() ;
								i = 1 ;
							}
							if(memberObject2!=null){								//String has to be dealt specially Hence we need to filter String.class out
								c= memberObject2.getClass() ;
								i = 2 ;
							}
							if(c!=null&&c.getSimpleName().equals("String")){			//if it is String.class deal specially
								String s1 = (String)memberObject1 ;
								String s2 = (String)memberObject2 ;
								if((i==2 &&(s1==null&&s2.trim().equals(""))) || (i==1&&(s1.trim().equals("")&&s2==null))){
									continue ;											//for us String having 'null' and "" is the same.
								}
							}
							
							//Below we Handle for Collection. For collection is one of them is null and the other is empty we consider both to be equal.
							if(Collection.class.isAssignableFrom(f1.getType())||Collection.class.isAssignableFrom(f2.getType())){
								if(memberObject1!=null&&memberObject2==null){
									Collection<?> cm = (Collection<?>) memberObject1 ;
									if(cm.isEmpty()){
										continue ;
									}
								}
								if(memberObject2!=null&&memberObject1==null){
									Collection<?> cm = (Collection<?>) memberObject2 ;
									if(cm.isEmpty()){
										continue ;
									}
								}
							}
							
							if((memberObject1!=null&&memberObject2==null) || (memberObject1==null&&memberObject2!=null)){
								printStat(objClass,f1) ;		//Here we pass objClass because the fields which didnot match are contained in objClass.
								return false;
							}
						}
					}else{
						//Dead Code. If both fields are null do Nothing
					}
				}
			
			} catch (SecurityException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				return false ;
			} catch (IllegalArgumentException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				return false ;
			} catch (IllegalAccessException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				return false ;
			}
		}
//		printStat(obj1.getClass(),7) ;
		return true ;    //All the fields are equal so return true.
	}


	private static boolean checkPrimitive(Object o1, Object o2) {
		
		Class<?> c = o1.getClass() ;

		if(c.isPrimitive() || isWrapper(c) || c.getSimpleName().contains("XMLGregorianCalendar")) {
			
			return true;
		}
		return false ;
	}


	//Helper method for checkPrimitive(). This is used to find out if the class is a wrapper type or not to a primitive type
	private static boolean isWrapper(Class<?> c) {
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


	private static <T> boolean handleNonPrimitiveFields(T memberObject1,T memberObject2){
		
		Class<?> c1 = memberObject1.getClass() ;
		Class<?> c2 = memberObject2.getClass() ;
			
		if(c1 != c2){printStat(memberObject1.getClass()) ;
		return false ;}
		
		if (Collection.class.isAssignableFrom(c1)){										
			return handleCollection(memberObject1,memberObject2,c1,c2) ;												// Handle Collections differently
		}
		
		return handleObject(memberObject1,memberObject2,c1,c2) ;
		
	}
	
	@SuppressWarnings("rawtypes")
	private static boolean handleCollection(Object memberObject1,Object memberObject2,Class<?> c1,Class<?> c2){
		
		if(List.class.isAssignableFrom(c1)){					//were handling only List as of now in Collections. Also we only check for one of the class as we have cheked for class equality in previous method. SO if code reaches this point both objects has to be of same class
			
			List li1 = (List) memberObject1 ;
			List li2 = (List) memberObject2 ;
	
			if(li1==null && li2 ==null){									//if both are null return true
				return true ;
			}else if((li1==null && li2!=null)||li1!=null && li2 == null){		//if only one of them is null return false
				if(li1==null){
					li2.isEmpty() ;
					return true ;
				}
				if(li2==null){
					li1.isEmpty() ;
					return true ;
				}
				printStat(c1) ;
				return false;
			}else if(li1!=null && li2!=null){
				
				if(li1.size()!=li2.size()){
					System.out.println("Size of two Lists not equal.");
					return false ;
				}
				
				Iterator i1 = li1.iterator() ;
				Iterator i2 = li2.iterator() ;
					
				while(i1.hasNext() && i2.hasNext()){
					Object o1 = i1.next() ;							//get Object from List1
					Object o2 = i2.next() ;							//get Object from List2
								
					if(!compareDeepObjectEquality(o1,o2)){			//Make a recursive call with the two Objects obtained from the List
						printStat(c1) ;
						return false ;				// if we find even one one set of objects unequal return false 
					}
				}
				return true;				//if we have reached here means all the objects in the corresponding Lists are same Hence return true indicating both the lists are same.
			
			}
		}
		printStat(c1) ;			
		return false ; // we return false as currently we are not Handling cases except List.class

	}
	
	private static boolean handleObject(Object memberObject1,Object memberObject2,Class<?> c1,Class<?> c2){
		
		//If it is not a collection make a recursive call giving the current objects to check if they are equal.
		return compareDeepObjectEquality(memberObject1,memberObject2) ;
	}
	
	private static boolean handlePrimitive(Object memberObject1,Object memberObject2,Class<?> c1){
	
		boolean b = memberObject1.equals(memberObject2) ;

	//If we want to ovverride the default comparism for any primitive type we put below-------------	
		if(c1==java.math.BigDecimal.class)
		{
			BigDecimal memberObject1BD = (BigDecimal)memberObject1 ;
			BigDecimal memberObject2BD = (BigDecimal)memberObject2 ;
			if(memberObject1BD.compareTo(memberObject2BD) == 0)
			{
				b = true;
			}
		}
		if(c1==String.class){
			b = handleString(memberObject1, memberObject2);
		}
		
		if(c1==javax.xml.bind.JAXBElement.class){
			JAXBElement<?> member1 = (JAXBElement<?>)memberObject1 ;
			JAXBElement<?> member2 = (JAXBElement<?>)memberObject2 ;
			
			Object memberObject1String = (Object) member1.getValue() ;
			Object memberObject2String = (Object) member2.getValue() ;
			b = compareDeepObjectEquality(memberObject1String, memberObject2String);
			
		}
		//overwrite to true if both date but not equal value , assume true
		if(c1==Date.class){
			if(memberObject1!=null&&memberObject2!=null){
				b=true;
			}
		}
	
		//-------------------------------------------------------		
		if(!b){
			System.out.println(memberObject1.getClass().getSimpleName());
			System.out.println("Field1 value :" + memberObject1);
			System.out.println("Field2 value :" + memberObject2);
			printStat(c1) ;
		}
		return b ;
	}


	private static boolean handleString(Object memberObject1, Object memberObject2) {
		boolean b;
		String memberObject1String = (String)memberObject1 ;
		String memberObject2String = (String)memberObject2 ;
		if(memberObject1String==null&&memberObject2String==null){
			return true ;
		}else if((memberObject1String==null&memberObject2String!=null) || memberObject1String!=null&&memberObject2String==null){
			return false ;
		}else{
			b = memberObject1String.replaceAll("[\\n\\t\\r\\s]", "").equals(memberObject2String.replaceAll("[\\n\\t\\r\\s]", "")) ;
			return b ;
		}
	} 
	
	private static void printStat(Class<? extends Object> class1) {	
		System.out.println("depth : "+depth +": " + class1);	
	}
	
	private static void printStat(Class<?> class1, Field f1) {
		System.out.println("depth : "+depth +" Field : "+f1.getName()+": of Class : " + class1 + ": doesnot match");	
	}

}
