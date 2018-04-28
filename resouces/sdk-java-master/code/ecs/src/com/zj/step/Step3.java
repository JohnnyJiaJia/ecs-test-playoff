package com.zj.step;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import com.zj.information.Info;
import com.zj.information.VM;
import com.zj.solution.PMsolu;
import com.zj.solution.PMsolu.PMentry;
import com.zj.utils.Util;
import com.zj.solution.VMsolu;

/**
 * 步骤3：补充或删除虚拟机，已达到最大资源利用率
 * @Title: Step3.java
 * @Description: TODO
 * @author ZhangJing
 * @date 2018年4月24日 下午1:09:17
 *
 */
public class Step3 {

	public static void adjust(Info info, VMsolu vmsolu, PMsolu pmsolu, int threshold) {
		//一般最后一个服务器放不满
		int lastIndex = pmsolu.numPMsolu()-1;
		PMentry lastPM = pmsolu.getPMsolu(lastIndex);
		if (lastPM.getIdleCPU() == 0 || lastPM.getIdleMEM() == 0) {
			return;
		}

		System.out.println("\n------------调整---------------");
		int lastIdleCPU = lastPM.getIdleCPU();
		int lastIdleMEM = lastPM.getIdleMEM();
		//根据优化目标对VM规格集进行排序
		ArrayList<VM> sortedList = Step2.sortVMs(info.getVMs(), Util.CPU);
		//待添加集合
		ArrayList<VM> preAddedVMList = new ArrayList<>();
		for (int i = 0; i < sortedList.size(); i++) {
			boolean flag = true;
			while (flag) {
				VM vm = sortedList.get(i);
				if (vm.getCPU() <= lastIdleCPU && vm.getMEM() <= lastIdleMEM) {
					preAddedVMList.add(vm);
					lastIdleCPU -= vm.getCPU();
					lastIdleMEM -= vm.getMEM();
				}else {
					flag = false;
				}
			}

		}

		int numAdd = preAddedVMList.size();
		int numDet = lastPM.getVMplaceTotalNum();


		if (numAdd < numDet) {
			//添加调整
			for (int i = 0; i < numAdd && i < threshold; i++) {
				//添加该vm到VMsolu
				vmsolu.addVMsolu(preAddedVMList.get(i), 1);
				//添加该vm到PMsolu
				pmsolu.addPMsolu(lastIndex, preAddedVMList.get(i), 1);
			}
			System.out.println("在最后的服务器中添加VM");
			System.out.println("VM个数:" + preAddedVMList.size());
			System.out.println("[CPU:" + lastPM.getIdleCPU() + " MEM:" + lastPM.getIdleMEM() / 1024 + "]" );

		}else {
			if (numDet > threshold) {
				return;
			}
			//删除调整
			Map<VM, Integer> placedVMs = lastPM.getVMplace();
			Iterator<Entry<VM, Integer>> iterator= placedVMs.entrySet().iterator();
			while(iterator.hasNext())
			{
			    Entry<VM, Integer> entry = iterator.next();
			    //从VMsolu中删除vm
			    vmsolu.removeVMsoule(entry.getKey(), 1);
			}
			//从PMsolu中删除pm
			pmsolu.removePMsolu(lastIndex);
			System.out.println("删除服务器: " + lastPM.getPlacedPM().getName() + "-" + lastIndex);
			System.out.println("VM个数: " + numDet);
		}

		System.out.println("IdleRate: " + pmsolu.getUtilization());

	}


}
