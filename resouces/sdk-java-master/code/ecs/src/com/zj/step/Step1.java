package com.zj.step;

import java.math.BigDecimal;
import java.math.RoundingMode;

import com.zj.information.Info;
import com.zj.solution.VMsolu;

/**
 * 步骤1：预测VM
 * @Title: Step1.java
 * @Description: TODO
 * @author ZhangJing
 * @date 2018年3月21日 上午10:56:14
 *
 */
public class Step1 {

	/**
	 * step1：预测各规格VM数量
	 * @param data
	 * @param info
	 * @return
	 */
	public static VMsolu predict(int[][] data, Info info) {

		VMsolu vmsolu = predictByAvg(data, info);
		//VMsolu vmsolu = presictByESMperDay(data, info);
		//VMsolu vmsolu = predictByLSTM(data, info);
		return vmsolu;
	}


	/**
	 * 方法：LSTM预测
	 * @param data
	 * @param info
	 * @return
	 */
	public static VMsolu predictByLSTM(int[][] data, Info info) {
		// 预测天数
		int numPredictDays = info.getPredictDays();
		// 预测间隔天数
		int numSpaceDays = info.getSpaceDays();
		// VM规格数
		int numFlavor = data.length;
		VMsolu vmsolu = new VMsolu(numFlavor);

		for (int i = 0; i < numFlavor; i++) {
			int result = LSTMsolver.run(data[i], numSpaceDays, numPredictDays);
			vmsolu.addVMsolu(info.getVMs().getVM(i), result);
		}
		return vmsolu;

	}

	/**
	 * 方法：平均数预测
	 * @param data
	 * @param info
	 */
	public static VMsolu predictByAvg(int[][] data, Info info) {
		//VM规格数
		int numFlavor = data.length;
		VMsolu vmsolu = new VMsolu(numFlavor);
		double[] avg = computeAvgExcludeZero(data);
		for (int i = 0; i < numFlavor; i++) {

			BigDecimal b = new  BigDecimal(avg[i] * info.getPredictDays() * 2.5);
			int c = b.setScale(0, RoundingMode.HALF_UP).intValue();
			vmsolu.addVMsolu(info.getVMs().getVM(i), c);
		}
		return vmsolu;
	}

	/**
	 * 方法：指数平滑法预测(以天为单位，间隔预测天数预测)
	 * @param data
	 * @param info
	 * @return
	 */
	public static VMsolu presictByESMperDay(int[][] data, Info info) {
		// 去噪
		// data = deleteAbnormalData(data);
		// 预测天数
		int numPredictDays = info.getPredictDays();
		// 预测间隔天数
		int numSpaceDays = info.getSpaceDays();

		// VM规格数
		int numFlavor = data.length;

		double[][] spaceData = EMSsolver.run(data, info, numSpaceDays, 0.3);
		double[][] newData = addMartix(data, spaceData);
		double[][] result = EMSsolver.run(newData, info, numPredictDays, 0.3);
		VMsolu vmsolu = new VMsolu(numFlavor);
		for (int i = 0; i < numFlavor; i++) {
			double sum = 0;
			for (int j = 0; j < result[i].length; j++) {
				sum += result[i][j];
			}
			BigDecimal b = new  BigDecimal(sum*2);
			int c = b.setScale(0, RoundingMode.HALF_UP).intValue();
			vmsolu.addVMsolu(info.getVMs().getVM(i), c);
		}
		return vmsolu;
	}




	/**
	 * 方法：指数平滑法预测(以天为单位，间隔预测天数预测)
	 * @param data
	 * @param info
	 * @return
	 */
	/*
	public static VMsolu presictByESMperDay(int[][] data, Info info) {
		//去噪
		//data = deleteAbnormalData(data);
		//预测天数
		int numPredictDays = info.getPredictDays();
		//预测间隔天数
		int numSpaceDays = info.getSpaceDays();
		//数据天数
		int numDataDays = data[0].length;
		//总天数
		int numDays = numDataDays + numSpaceDays ;
		// 预测的次数
		int number;
		//VM规格数
		int numFlavor = data.length;

		double[] result = new double[numFlavor];

		for (int k = 0; k < numPredictDays; k++) {
			//从矩阵中提起出以预测天数为间隔的子矩阵1,2,..,k
			if (k < numDays % numPredictDays) {
				number= numDays / numPredictDays + 1;
			}else{
			    number= numDays / numPredictDays;
			}
			int[][] newData = new int[numFlavor][number];

			for (int i = 0; i < numFlavor; i++) {
				for (int j = 0; j < number; j++) {
					int d = numDays-j*numPredictDays-k-1;
					if (d > data[i].length-1) {
						newData[i][number-j-1] = -1;
					}else {
						newData[i][number-j-1] = data[i][d];
					}
				}
			}

			//double[] temp = computeSESM(newData, 0.6);
			double[] temp = computeSESM(newData, 0.7);
			for (int i = 0; i < numFlavor; i++) {
				if (temp[i] >= 0) {
					result[i] += temp[i];
				}
			}
		}
		VMsolu vmsolu = new VMsolu(numFlavor);
		for (int i = 0; i < numFlavor; i++) {

			BigDecimal b = new  BigDecimal(result[i]);
			int c = b.setScale(0, RoundingMode.HALF_UP).intValue();
			vmsolu.addVMsolu(info.getVMs().getVM(i), c);
		}
		return vmsolu;

	}

*/

	public static int[][] deleteAbnormalData(int[][] data) {
		//平均数(不包括0的数据)
		double[] avg = computeAvgExcludeZero(data);
		//标准差(不包括0的数据)
		double[] sdv = computeSdvExcludeZero(data, avg);

		for (int i = 0; i < data.length; i++) {
			for (int j = 0; j < data[i].length; j++) {
				//只考虑大于平均值的异常点
				if (sdv[i] != 0 && data[i][j] - avg[i] >= 3 * sdv[i] ) {
					//data[i][j] = 0;
					BigDecimal b = new  BigDecimal(avg[i]);
					data[i][j] = b.setScale(0, RoundingMode.HALF_UP).intValue();
					//System.out.println(i + " " + j + " " + data[i][j]);
				}
			}
		}
		return data;
	}

	/**
	 * 计算平均数(不包括0的数据)
	 * @param data
	 * @param start
	 * @param days
	 * @return
	 */
	public static double[] computeAvgExcludeZero(int[][] data) {
		int numFlavor = data.length;
		double[] avg = new double[numFlavor];
		for (int i = 0; i < numFlavor; i++) {
			int days = 0;
			for (int j = 0; j < data[i].length; j++) {
				if (data[i][j] >= 0) {
					avg[i] += data[i][j];
					days++;
				}
			}
			if (days != 0) {
				avg[i] /= days;
			}
		}
		return avg;
	}

	/**
	 * 计算标准差(不包括0元素)
	 * @param data
	 * @param avg
	 * @return
	 */
	public static double[] computeSdvExcludeZero(int[][] data, double[] avg) {
		int numFlavor = data.length;
		double[] sdv = new double[numFlavor];
		for (int i = 0; i < numFlavor; i++) {
			if (avg[i] == 0) {
				sdv[i] = 0;
			}else {
				int days = 0;
				for (int j = 0; j < data[i].length; j++) {
					if (data[i][j] != 0) {
						sdv[i] += ((data[i][j] - avg[i]) * (data[i][j] - avg[i]));
						days++;
					}
				}
				sdv[i] = Math.sqrt(sdv[i] / days);
			}
		}
		return sdv;
	}


	private static double[][] addMartix(int[][] add1, double[][] add2) {
		int num1 = add1[0].length;
		int num2 = add2[0].length;
		double[][] data = new double[add1.length][num1 + num2];
		for (int i = 0; i < data.length; i++) {
			for (int j = 0; j < data[i].length; j++) {
				if (j < num1) {
					data[i][j] = add1[i][j];
				}else {
					data[i][j] = add2[i][j-num1];
				}

			}
		}
		return data;
	}


}
