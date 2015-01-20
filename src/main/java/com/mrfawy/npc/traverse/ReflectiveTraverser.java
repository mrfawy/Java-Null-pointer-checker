package com.mrfawy.npc.traverse;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.mrfawy.npc.common.FunctorIF;

public abstract class  ReflectiveTraverser<T> {
private T object;
	
	private Set<String> cycleDetectionSet ;
	private static final List<String> ignorableFields ;	//if any field must be ignored from population then its declared here.
	
	static{
		ignorableFields = new ArrayList<String>();		
		ignorableFields.add("serialVersionUID") ;
	}
	

	public ReflectiveTraverser(T object) {
		super();
		this.object = object;
	}
	
	public ReflectiveTraverser(Class<T> objClass) {

		try {
			object = ((T) objClass.newInstance());
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InstantiationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	
	/**
	 * Process the object and apply the intended operation on all it's hierarchy fields
	 * @return
	 */
	protected abstract  T process();
	
	protected void traverse(FunctorIF f){
		traverse(object, f);
		reset();
	}
	private void reset(){
		cycleDetectionSet=null;
	}
	protected void traverse(Object o,FunctorIF f) {
		if (o == null) {
			return;
		}
//		T value = null;
		if(ClassUtil.checkPrimitive(o.getClass())){		
			f.visitPrimitive(o);
		}else if(ClassUtil.isCollection(o.getClass())){			
			f.visitCollection(o);
			traverseCollection(o,f) ;
			
		}else{
			f.visitNonPrimitive(o);//if Not primitive means this is a class we need to iterate through the fields
			traverseObject(o,f) ;
		}

	}
	
	private void traverseCollection(Object o, FunctorIF f) {
		if (o != null) {
			f.visitCollection(o);
			/*
			 * were handling only List as of now in Collections. Also we only
			 * check for one of the class as we have cheked for class equality
			 * in previous method. SO if code reaches this point both objects
			 * has to be of same class
			 */
			if (List.class.isAssignableFrom(o.getClass())) {
				processList((List<?>) o, f);
			}
			if (Map.class.isAssignableFrom(o.getClass())) {
				processMap((Map<?,?>) o, f);
			}
		}

	}
	
	private void processMap(Map<?, ?> o, FunctorIF f) {
		Set<?> keys=o.keySet();
		for(Object e:keys){
			
		}
		
	}

	private void processList(List<?> oList,FunctorIF f ){
		if(oList!=null){
			for(Object o :oList){
				traverse(o, f);
			}
		}
		
	}
	
	/**
	 * Assumption is that this method is only called for Class which are 
	 * 1. Not primtive
	 * 2. Not Collection.
	 */
	private void traverseObject(Object o, FunctorIF f ){
		if (o != null) {
			if (cycleDetectionSet == null) {
				cycleDetectionSet = new HashSet<String>();
			}

			if (cycleDetectionSet.contains(o.getClass().getCanonicalName())) {
				return;

			} else {
				cycleDetectionSet.add(o.getClass().getCanonicalName());
			}
		
//		Field[] f = objClass.getDeclaredFields() ;
		
		
			//This gets all the fields belonging to current class as well as its parent.
			List<Field> fieldsList = ClassUtil.getInheritedAndInstanceFields(o.getClass()) ;
			
			for(Field field:fieldsList){
				
					if(field!=null){				//Handle primitive fields
						field.setAccessible(true) ;					
						//if the object is null or is one of the field in the ignore-able list we 'continue' to next iteration of loop.
						//NOTE : we DONOT return. because we need to populate all the fields only then we return.
						if(ignorableFields.contains(field.getName())){
							continue;
						}
						//Object fieldObject=runGetter(o,field);
						//Object objectValue = field.getType().newInstance();
						//Object fieldObject=field.get(objectValue);
						
						traverseField(o,field,f);
						
					}
				
			}
			cycleDetectionSet.remove(o.getClass().getCanonicalName()) ;
		}
		
		return  ;
	}
	private void traverseField(Object o,Field field,FunctorIF f){	
		
		if(ClassUtil.checkPrimitive(field.getType())){		
			f.visitPrimitiveField(o,field);			
		}else if(ClassUtil.isCollection(field.getType())){			
			f.visitCollectionField(o, field);
			Object fieldObject=ClassUtil.runGetter(o, field);
			traverseCollection(fieldObject,f) ;
			
		}else{
			f.visitNonPrimitiveField(o, field);//if Not primitive means this is a class we need to iterate through the fields
			try {
				traverseObject(field.get(o),f) ;
			} catch (IllegalArgumentException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}



	public T getObject() {
		return object;
	}

	protected void setObject(T object) {
		this.object = object;
	}
	
	


	
	

}
