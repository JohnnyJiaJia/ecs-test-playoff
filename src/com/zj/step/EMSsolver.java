package com.zj.step;

import com.zj.information.Info;

public class EMSsolver {
	/**
	 * 方法：指数平滑法预测(以天为单位，间隔预测天数预测)
	 * @param data
	 * @param info
	 * @return
	 */
	public static double[][] run(double[][] data, Info info, int numPredictDays, double factor) {

		//数据天数
		int numDays = data[0].length;
		// 预测的次数
		int number;
		//VM规格数
		int numFlavor = data.length;

		double[][] result = new double[numFlavor][numPredictDays];

		for (int k = 0; k < numPredictDays; k++) {
			//从矩阵中提起出以预测天数为间隔的子矩阵1,2,..,k
			if (k < numDays % numPredictDays) {
				number= numDays / numPredictDays + 1;
			}else{
			    number= numDays / numPredictDays;
			}
			double[][] newData = new double[numFlavor][number];

			for (int i = 0; i < numFlavor; i++) {
				for (int j = 0; j < number; j++) {
					int d = numDays-j*numPredictDays-k-1;
					newData[i][number-j-1] = data[i][d];

				}
			}

			double[] temp = computeSESM(newData, factor);
			for (int i = 0; i < numFlavor; i++) {
				if (temp[i] >= 0) {
					result[i][k] = temp[i];
				}
			}
		}

		return result;

	}

	public static double[][] run(int[][] data, Info info, int numPredictDays, double factor) {

		//数据天数
		int numDays = data[0].length;
		// 预测的次数
		int number;
		//VM规格数
		int numFlavor = data.length;

		double[][] result = new double[numFlavor][numPredictDays];

		for (int k = 0; k < numPredictDays; k++) {
			//从矩阵中提起出以预测天数为间隔的子矩阵1,2,..,k
			if (k < numDays % numPredictDays) {
				number= numDays / numPredictDays + 1;
			}else{
			    number= numDays / numPredictDays;
			}
			double[][] newData = new double[numFlavor][number];

			for (int i = 0; i < numFlavor; i++) {
				for (int j = 0; j < number; j++) {
					int d = numDays-j*numPredictDays-k-1;
					newData[i][number-j-1] = data[i][d];

				}
			}

			double[] temp = computeSESM(newData, factor);
			for (int i = 0; i < numFlavor; i++) {
				if (temp[i] >= 0) {
					result[i][k] = temp[i];
				}
			}
		}

		return result;

	}

	//一次指数平滑法（First exponential smoothing method）
		public static double[] computeFESM(double[][] data, double factor) {
			int numFlavor = data.length;
			double factorLeft = 1 - factor;
			double[] result = new double[numFlavor];
			for (int i = 0; i < numFlavor; i++) {
				double firIndex = data[i][0];
				for (int j = 0; j < data[i].length; j++) {
					 firIndex = factor * data[i][j] + factorLeft * firIndex;

				}

				result[i] = firIndex ;

			}
			return result;
		}


	//二次指数平滑法（Second exponential smoothing method）
	public static double[] computeSESM(double[][] data, double factor) {
		int numFlavor = data.length;
		double factorLeft = 1 - factor;
		double[] result = new double[numFlavor];
		for (int i = 0; i < numFlavor; i++) {

			double firIndex = data[i][0];
			double secIndex = data[i][0];
			for (int j = 0; j < data[i].length; j++) {

				 firIndex = factor * data[i][j] + factorLeft * firIndex;
		         secIndex = factor * firIndex + factorLeft * secIndex;
			}
			Double a = 2 * firIndex - secIndex;
	        Double b = (factor / factorLeft) * (firIndex - secIndex);
			result[i] = a + b;

		}
		return result;
	}

	// 三次指数平滑法（Third exponential smoothing method）
	public static double[] computeTESM(int[][] data, double factor) {
		int numFlavor = data.length;
		double factorLeft = 1 - factor;
		double[] result = new double[numFlavor];
		for (int i = 0; i < numFlavor; i++) {

			double firIndex = data[i][0];
			double secIndex = data[i][0];
			double thiIndex = data[i][0];
			for (int j = 0; j < data[j].length; j++) {

				firIndex = factor * data[i][j] + factorLeft * firIndex;
				secIndex = factor * firIndex + factorLeft * secIndex;
				thiIndex = factor * secIndex + factorLeft * thiIndex;
			}
			Double a = 3 * firIndex - 3 * secIndex + thiIndex;
			Double b = factor / (2 * factorLeft * factorLeft) * (((6 - 5 * factor) * firIndex)
					- (2 * (5 - 4 * factor) * secIndex) + ((4 - 3 * factor) * thiIndex));
			Double c = factor * factor / (2 * factorLeft * factorLeft) * (firIndex - 2 * secIndex + thiIndex);
			result[i] = a + b + c;
		}
		return result;
	}
}
