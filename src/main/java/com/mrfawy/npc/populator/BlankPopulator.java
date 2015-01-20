package com.mrfawy.npc.populator;

import com.mrfawy.npc.populate.BlankFunctor;
import com.mrfawy.npc.traverse.ReflectiveTraverser;

/**
 * Use this Class to populate an object with Blank values on all of Non complex
 * fields and at any level of it's hierarchy
 * 
 * @author abdelm2
 *
 * @param <T>
 *            The type of the object to process
 */
public class BlankPopulator<T> extends ReflectiveTraverser<T> {

	public BlankPopulator(T object) {
		super(object);

	}

	public BlankPopulator(Class<T> objClass) {
		super(objClass);

	}

	@Override
	protected T process() {
		traverse(new BlankFunctor());
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