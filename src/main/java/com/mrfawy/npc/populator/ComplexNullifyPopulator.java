package com.mrfawy.npc.populator;

import java.util.ArrayList;
import java.util.List;

import com.mrfawy.npc.nullify.ComplexCountFunctor;
import com.mrfawy.npc.nullify.NullifyObjectAtIndexFunctor;
import com.mrfawy.npc.traverse.ReflectiveTraverser;

/**
 * Use this Class to get all the possible combinations resulting from complex
 * object is set to null at any level
 * 
 * @author abdelm2
 *
 * @param <T>
 *            The type of the object to process
 */
public class ComplexNullifyPopulator<T> extends ReflectiveTraverser<T> {

	// default value of max number of generated permutations , prevents timeout
	// for large complex objects
	private static final int PERM_LIMIT = 500;

	public ComplexNullifyPopulator(T object) {
		super(object);
		// TODO Auto-generated constructor stub
	}

	public ComplexNullifyPopulator(Class<T> objClass) {
		super(objClass);

	}

	@Override
	protected T process() {
		return getObject();

	}

	/**
	 * Use this method to get all the possible combinations resulting from
	 * complex object is set to null at any level
	 * 
	 * @return a list of all object combinations
	 */
	public List<T> getNullPermutations() {
		int complexObjectCount = countComplexObjects();
		List<T> result = new ArrayList<T>();
		result.add(null);
		for (int i = 0; i < complexObjectCount; i++) {

			T obj = initNewObject();
			result.add(NullifyObjectAtIndex(obj, i));

		}
		return result;
	}

	/**
	 * provides a limited size list of all permutations
	 * 
	 * @param limit
	 * @return
	 */
	public List<T> getNullPermutations(int limit) {
		int complexObjectCount = countComplexObjects();
		List<T> result = new ArrayList<T>();
		result.add(null);
		int loopCount = limit;
		if (loopCount > complexObjectCount) {
			loopCount = complexObjectCount;
		}
		if (limit < 0 || (limit > complexObjectCount && limit != PERM_LIMIT))
			throw new IllegalArgumentException("invalid limit value");
		for (int i = 0; i < loopCount; i++) {
			T obj = initNewObject();
			result.add(NullifyObjectAtIndex(obj, i));
		}

		return result;
	}

	/**
	 * Use this method to pre-populate the object with default values and
	 * nullify all the primitive
	 * 
	 * @return a list of all possible combinations resulting from complex object
	 *         is set to null at any level
	 */
	public List<T> initGetNullPermutations() {

		return getNullPermutations(PERM_LIMIT);
	}

	public List<T> initGetNullPermutations(int limit) {
		DefaultValuePopulator<T> rp = new DefaultValuePopulator<T>(getObject());
		PrimitiveNullifyPopulator<T> pnp = new PrimitiveNullifyPopulator<T>(
				rp.process());
		setObject(pnp.process());
		return getNullPermutations(limit);
	}

	private T initNewObject() {
		T obj;
		try {
			obj = (T) getObject().getClass().newInstance();
			DefaultValuePopulator<T> dfp = new DefaultValuePopulator<T>(obj);
			dfp.process();
			PrimitiveNullifyPopulator<T> pnp = new PrimitiveNullifyPopulator<T>(
					obj);
			pnp.process();
			return obj;
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InstantiationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	private int countComplexObjects() {
		ComplexCountFunctor f = new ComplexCountFunctor();
		traverse(f);
		return f.getComplexCount();
	}

	private T NullifyObjectAtIndex(T o, int i) {
		NullifyObjectAtIndexFunctor f = new NullifyObjectAtIndexFunctor(i);
		traverse(o, f);
		return o;
	}

}
