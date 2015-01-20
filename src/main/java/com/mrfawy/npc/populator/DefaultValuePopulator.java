package com.mrfawy.npc.populator;

import com.mrfawy.npc.populate.DefaultValueFunctor;
import com.mrfawy.npc.traverse.ReflectiveTraverser;

/**
 * Use this Class to populate an object and all of it's hierarchy objects with
 * default values
 * 
 * @author abdelm2
 *
 * @param <T>
 *            The type of the object to process
 */

public class DefaultValuePopulator<T> extends ReflectiveTraverser<T> {

	public DefaultValuePopulator(T object) {
		super(object);

	}

	public DefaultValuePopulator(Class<T> objClass) {
		super(objClass);

	}

	@Override
	protected T process() {
		traverse(new DefaultValueFunctor());
		return getObject();
	}

	public T initProcess() {
		return process();
	}

}
