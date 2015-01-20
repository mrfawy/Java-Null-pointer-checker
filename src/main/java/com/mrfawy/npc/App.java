package com.mrfawy.npc;

import java.util.List;

import com.mrfawy.npc.common.JSONUtil;
import com.mrfawy.npc.nullify.PrimitiveCounter;
import com.mrfawy.npc.objects.TestObjA;
import com.mrfawy.npc.populator.BlankPopulator;
import com.mrfawy.npc.populator.ComplexNullifyPopulator;
import com.mrfawy.npc.populator.DefaultValuePopulator;
import com.mrfawy.npc.populator.PrimitiveNullifyPopulator;

/**
 * Hello world!
 *
 */
public class App {
	public static void testDefaultValuePopulator() {

		DefaultValuePopulator<TestObjA> rp = new DefaultValuePopulator<TestObjA>(
				TestObjA.class);
		rp.initProcess();
		System.out
				.println("==========AFTER defaultValuesPopulation ===========");
		JSONUtil.printJson(rp.getObject());
	}

	public static void testPrimitiveNullifyPopulator() {
		// DefaultValuePopulator<TestObjA> rp=new
		// DefaultValuePopulator<TestObjA>(TestObjA.class);
		PrimitiveNullifyPopulator<TestObjA> pnp = new PrimitiveNullifyPopulator<TestObjA>(
				TestObjA.class);
		pnp.initProcess();
		System.out
				.println("==========AFTER PRIMITIV NULL Population ===========");
		JSONUtil.printJson(pnp.getObject());
	}

	public static void testBlankPopulator() {
		BlankPopulator<TestObjA> pnp = new BlankPopulator<TestObjA>(
				TestObjA.class);
		pnp.initProcess();
		System.out.println("==========AFTER BLANK Population ===========");
		JSONUtil.printJson(pnp.getObject());
	}

	public static void testComplexNullify() {
		ComplexNullifyPopulator<TestObjA> cnp = new ComplexNullifyPopulator<TestObjA>(
				TestObjA.class);
		List<TestObjA> perList = cnp.initGetNullPermutations();
		System.out
				.println("==========AFTER COMPLEX NULL POPULATION ===========");
		int i = 0;
		for (TestObjA tmp : perList) {
			System.out.println("==========||||||||" + i++
					+ "||||||| ===========");
			JSONUtil.printJson(tmp);
			System.out.println("==========|||||||||||||||| ===========");
		}
	}

	public static void testPrimitiveCounter() {

		PrimitiveCounter<TestObjA> rp = new PrimitiveCounter<TestObjA>(
				TestObjA.class);
		rp.initProcess();
		System.out.println("==========AFTER testPrimitiveCounter ===========");
		JSONUtil.printJson(rp.getObject());
		System.out.println("NotPopulatedFieldList"
				+ rp.getNotPopulatedFieldList());
		System.out.println("PopulatedFieldList:" + rp.getPopulatedFieldList());
		System.out.println("TotalFields" + rp.getTotalFields());
	}

	public static void main(String[] args) {
		testComplexNullify();
	}
}
