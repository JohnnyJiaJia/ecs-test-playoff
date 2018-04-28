package com.zj.step;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Random;

import com.zj.information.Info;
import com.zj.information.VMs;
import com.zj.information.VM;
import com.zj.solution.PMsolu;
import com.zj.solution.VMsolu;
import com.zj.utils.Util;


/**
 * 步骤2：放置VM到PM
 * @Title: Step2.java
 * @Description: TODO
 * @author ZhangJing
 * @date 2018年3月21日 上午10:56:35
 *
 */
public class Step2 {

	public static PMsolu placement(VMsolu vmsolu, Info info, long startTime) {
		PMsolu pmsolu = placementByFDD(vmsolu, info);

		System.out.println("IdleRate: " + pmsolu.getUtilization());

		//GA算法
		GAsolver ga = new GAsolver(pmsolu, info, startTime);
		pmsolu = ga.run();

		return pmsolu;
	}


	/**
	 * 装箱问题FFD放置算法
	 * @param vmsolu
	 * @param info
	 * @return
	 */
	public static PMsolu placementByFDD(VMsolu vmsolu, Info info) {
		//根据优化目标对VM规格集进行排序
		ArrayList<VM> sortedList = sortVMs(info.getVMs(), Util.CPU);
		//默认开启物理服务器0
		PMsolu pmsolu = new PMsolu(info);
		int indexPM = 0;
		//i代表虚拟机规格索引，j代表物理服务器索引
		for (int i = 0; i < sortedList.size(); i++) {
			VM vm = sortedList.get(i);
			int VMpredictNum = vmsolu.getPredictNum(vm);

			while (VMpredictNum > 0){
				//放置成功标志
				boolean flag_placed = false;
				for (int j = 0; j <= indexPM; j++) {
					int idelCPUcapacity = pmsolu.getPMsolu(j).getIdleCPU();
					int idelMEMcapacity = pmsolu.getPMsolu(j).getIdleMEM();
					if (vm.getCPU() <= idelCPUcapacity && vm.getMEM() <= idelMEMcapacity) {
						pmsolu.addPMsolu(j, vm, 1);
						VMpredictNum--;
						flag_placed = true;
						break;
					}
				}
				//放置失败
				if (flag_placed == false) {
					boolean flag_replaced = false;
					for (int j = 0; j < indexPM; j++) {
						if (pmsolu.getPMsolu(j).getPlacedPM().equalByName(Util.General)) {
							//替换服务器
							int idelCPUcapacity = pmsolu.getPMsolu(j).getIdleCPU();
							int idelMEMcapacity = pmsolu.getPMsolu(j).getIdleMEM();
							if (vm.getCPU() > idelCPUcapacity && vm.getMEM() <= idelMEMcapacity) {
								//替换High_Performance服务器
								pmsolu.replacePMsolu(j, info.getPMs().getPMByName(Util.Large_Memory));
								int newIdelCPUcapacity = pmsolu.getPMsolu(j).getIdleCPU();
								int newIdelMEMcapacity = pmsolu.getPMsolu(j).getIdleMEM();
								if (vm.getCPU() <= newIdelCPUcapacity && vm.getMEM() <= newIdelMEMcapacity){
									pmsolu.addPMsolu(j, vm, 1);
									VMpredictNum--;
									flag_replaced = true;
									break;
								}
							}else if (vm.getCPU() <= idelCPUcapacity && vm.getMEM() > idelMEMcapacity) {
								//替换Large_Memory服务器
								pmsolu.replacePMsolu(j, info.getPMs().getPMByName(Util.High_Performance));
								int newIdelCPUcapacity = pmsolu.getPMsolu(j).getIdleCPU();
								int newIdelMEMcapacity = pmsolu.getPMsolu(j).getIdleMEM();
								if (vm.getCPU() <= newIdelCPUcapacity && vm.getMEM() <= newIdelMEMcapacity){
									pmsolu.addPMsolu(j, vm, 1);
									VMpredictNum--;
									flag_replaced = true;
									break;
								}
							}
						}
					}
					//开辟新物理服务器
					if (flag_replaced == false) {
						pmsolu.addNewPMsolu();
						indexPM++;
						pmsolu.addPMsolu(indexPM, vm, 1);
						VMpredictNum--;
					}
				}
			}


		}
		return pmsolu;
	}

	/**
	 * 根据优化目标对VM规格进行重新排序
	 * @param vms
	 * @param obj
	 * @return
	 */
	public static ArrayList<VM> sortVMs(VMs vms, int parameter) {
		ArrayList<VM> sortedList = new ArrayList<>(vms.getVMs());

		Collections.sort(sortedList, new Comparator<VM>() {
			@Override
			public int compare(VM o1, VM o2) {

				switch (parameter) {
				case Util.CPU:
					int a1 = o2.getCPU() - o1.getCPU();
					if (a1 == 0) {
						return o2.getMEM() - o1.getMEM();
					}
					return a1;
				case Util.MEM:
					int a2 = o2.getMEM() - o1.getMEM();
					if (a2 == 0) {
						return o2.getCPU() - o1.getCPU();
					}
					return a2;
				case Util.RATE:
					double a3 = o2.getMEM()/o2.getCPU() - o1.getMEM()/o1.getCPU();
					if (a3 == 0) {
						return o2.getCPU() - o1.getCPU();
					}
					return (int) a3;

				case Util.RAM:
					Random r = new Random();
					int a4 = o2.getCPU() - o1.getCPU();
					if (a4 == 0) {
						return r.nextInt() > 0 ? 1 :-1;
					}
					return a4;
				default:
					return 0;
				}

			}

		});
		return sortedList;
	}
}
