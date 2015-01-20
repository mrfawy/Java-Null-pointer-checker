package com.mrfawy.npc.populator;

import com.mrfawy.npc.nullify.PrimitiveNullifyFunctor;
import com.mrfawy.npc.traverse.ReflectiveTraverser;

/**
 * Use this Class to populate an object with null values on all of Non complex
 * fields and at any level of it's hierarchy
 * 
 * @author abdelm2
 *
 * @param <T>
 *            The type of the object to process
 */
public class PrimitiveNullifyPopulator<T> extends ReflectiveTraverser<T> {

	public PrimitiveNullifyPopulator(T object) {
		super(object);
		// TODO Auto-generated constructor stub
	}

	public PrimitiveNullifyPopulator(Class<T> objClass) {
		super(objClass);

	}

	@Override
	public T process() {
		PrimitiveNullifyFunctor f = new PrimitiveNullifyFunctor();
		traverse(f);
		return getObject();
	}

	/**
	 * Implicitly call defaultValue Population to build the object hirarchy
	 * 
	 * @return
	 */
	public T initProcess() {
		DefaultValuePopulator<T> dp = new DefaultValuePopulator<T>(getObject());
		setObject(dp.process());
		return process();
	}

}
