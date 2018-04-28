package com.zj.step;

import java.time.temporal.ChronoUnit;
import java.util.Date;

import com.zj.history.History;
import com.zj.information.Info;
import com.zj.information.PM;
import com.zj.information.PMs;
import com.zj.information.VMs;
import com.zj.information.VM;

/**
 * 预步骤：解析数据
 * @Title: Step0.java
 * @Description: TODO
 * @author ZhangJing
 * @date 2018年3月21日 上午10:55:49
 *
 */
public class Step0 {

	/**
	 * 按天整理训练集得到矩阵数据(是否需要更精细?)
	 * 行代表VM规格，列代表天数
	 * @param history
	 * @param info
	 * @return
	 */
	public static int[][] computeMatrixDataByDay(History history, Info info) {
		//训练集日期范围
		Date startDate = history.getRecord(0).getCreatedate();
		Date endDate = history.getRecord((history.numRecord()-1)).getCreatedate();
		//补充预测间隔天数
		int spaceDays = (int) ChronoUnit.DAYS.between(endDate.toInstant(), info.getStartdate().toInstant());
		info.setSpaceDays(spaceDays-1);
		//训练集日期范围
		int numTrainDate = (int) ChronoUnit.DAYS.between(startDate.toInstant(), endDate.toInstant()) + 1;
		int numVM = info.getVMs().numVMs();
		//按天的请求量矩阵
		int[][] trainData = new int[numVM][numTrainDate];
		for (int k = 0; k < history.numRecord(); k++) {
			int j = (int) ChronoUnit.DAYS.between(startDate.toInstant(), history.getRecord(k).getCreatedate().toInstant());
			//过滤VMs没有的VM规格
			VM flavor = info.getVMs().getVMByName(history.getRecord(k).getFlavor());
			if (flavor != null) {
				int i = flavor.getIndex();
				trainData[i][j]++;
			}

		}
		return trainData;
	}



	/**
	 * 按星期重新构造矩阵数据(不足的删去)
	 * @param data
	 * @return
	 */
	public static int[][] computeMatrixDataByWeek(int[][] data) {
		int days = data[0].length;
		int weeks = days / 7;
		int numFlavor = data.length;
		int[][] newData = new int[numFlavor][weeks];
		for (int i = 0; i < numFlavor; i++) {
			for (int j = 0; j < weeks * 7 ; j++) {
				newData[i][weeks-j/7-1] += data[i][days-j-1];
			}
		}
		return newData;
	}

	/**
	 * 解析训练集数据
	 * @param ecsContent
	 * @return history
	 */
	public static History parseTrainData(String[] ecsContent) {
		History history = new History(ecsContent.length);
		for (int i = 0; i < ecsContent.length; i++) {
			String[] array = ecsContent[i].split("\\s+|\t", 3);
			history.addRecord(array[0], array[1], array[2]);
		}
		return history;
	}

	/**
	 * 解析输入文件数据
	 * @param inputContent
	 * @return info
	 */
	public static Info parseInputData(String[] inputContent) {
		int index = 0;
		//解析物理服务器类型
		int PMnum = Integer.valueOf(inputContent[index++]);
		PMs pms = new PMs(PMnum);
		for (int i = 0; i < PMnum; i++) {
			String[] PMarray = inputContent[index++].split("\\s+|\t");
			pms.addPM(i, PMarray[0], Integer.valueOf(PMarray[1]), Integer.valueOf(PMarray[2]), Integer.valueOf(PMarray[3]));
		}
		index ++;
		//解析虚拟机规格集
		int VMnum = Integer.valueOf(inputContent[index++]);
		VMs vms = new VMs(VMnum);
		for (int i = 0; i < VMnum; i++) {
			String[] VMarray = inputContent[index++].split("\\s+|\t");
			vms.addVM(i, VMarray[0], Integer.valueOf(VMarray[1]), Integer.valueOf(VMarray[2]));
		}
		index++;
		//解析预测时间范围
		String startdateString = inputContent[index++];
		String enddateString = inputContent[index++];
		//得到输入信息
		Info info = new Info(startdateString, enddateString, pms, vms);
		return info;
	}


}
