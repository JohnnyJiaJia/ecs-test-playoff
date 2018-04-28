package com.zj.test;

import com.zj.history.History;
import com.zj.information.Info;
import com.zj.solution.PMsolu;
import com.zj.solution.VMsolu;
import com.zj.solution.PMsolu.PMentry;
import com.zj.step.Step0;
import com.zj.step.Step1;
import com.zj.step.Step2;
import com.zj.step.Step3;

public class Predict {

	public static String[] predictVm(String[] ecsContent, String[] inputContent) {
		/** =========do your work here========== **/
		long startTime = System.currentTimeMillis();
		//训练集
		History history = Step0.parseTrainData(ecsContent);
		//输入信息
		Info info = Step0.parseInputData(inputContent);
		//按天得到矩阵数据(是否需要更精细)
		int[][] data = Step0.computeMatrixDataByDay(history, info);
/*
		for (int i = 0; i < data.length; i++) {
			for (int j = 0; j < data[i].length; j++) {
				System.out.print(data[i][j] + " ");
			}
			System.out.println();
		}

*/
		VMsolu vmsolu = Step1.predict(data, info);
		PMsolu pmsolu = Step2.placement(vmsolu, info, startTime);

		//打印
		System.out.print(vmsolu);
		System.out.print(pmsolu);

		System.out.println("\n-----------------------------------");
		for (int i = 0; i < pmsolu.numPMsolu(); i++) {
			PMentry pm = pmsolu.getPMsolu(i);
			System.out.print(pm.getPlacedPM().getName() + "-" + i + " ");
			System.out.println("[CPU:" + pm.getIdleCPU() + " MEM:" + pm.getIdleMEM() / 1024 + "]" );
		}

		Step3.adjust(info, vmsolu, pmsolu, 10);

		String[] result = (vmsolu.toString() + pmsolu.toString()).split("\n");

		return result;
	}


}
