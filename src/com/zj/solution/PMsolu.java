package com.zj.solution;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import com.zj.information.Info;
import com.zj.information.PM;
import com.zj.information.VM;
import com.zj.solution.PMsolu.PMentry;

public class PMsolu implements Comparable<PMsolu>{

	private List<PMentry> _PMsoluList;

	private Info _info;



	public PMsolu(Info info) {
		this._info = info;
		_PMsoluList = new ArrayList<PMsolu.PMentry>();
		//初始开辟一个物理服务器0
		PMentry pmentry= new PMentry(_info.getPMs().getPM(0));
		_PMsoluList.add(pmentry);
	}

	public PMsolu(Info info, PM initPM) {
		this._info = info;
		_PMsoluList = new ArrayList<PMsolu.PMentry>();
		PMentry pmentry= new PMentry(initPM);
		_PMsoluList.add(pmentry);
	}

	/**
	 *
	 * @param index
	 * @return
	 */
	public PMentry getPMsolu(int index) {
		return _PMsoluList.get(index);
	}

	public int numPMsolu() {
		return _PMsoluList.size();
	}

	/**
	 * 在已有物理服务器上添加部署方案
	 * @param index
	 * @param vm
	 * @param num
	 */
	public void addPMsolu(int index, VM vm, int num) {
		getPMsolu(index).addVMplaceNum(vm, num);
	}

	/**
	 * 添加空闲物理服务器(默认添加索引0（通用型）的服务器)
	 */
	//TODO: 待修改
	public void addNewPMsolu() {
		PMentry pmentry= new PMentry(_info.getPMs().getPM(0));
		_PMsoluList.add(pmentry);
	}

	/**
	 * 添加空闲物理服务器
	 */
	public void addNewPMsolu(PM pm) {
		PMentry pmentry= new PMentry(pm);
		_PMsoluList.add(pmentry);
	}

	public boolean hasPMsolu(int index) {
		if (_PMsoluList.get(index) != null) {
			return true;
		} else {
			return false;
		}
	}

	public void removePMsolu(int index) {
		if (hasPMsolu(index)) {
			_PMsoluList.remove(index);
		}else {
			System.err.println("移除服务器失败，未找到该索引！");
		}
	}


	/**
	 * 改变已部署的服务器类型
	 * @param index
	 * @param pm
	 */
	public void replacePMsolu(int index, PM pm) {
		PMentry olderPM = getPMsolu(index);
		if (olderPM != null) {
			int usedCPU = _info.getPMs().getPMByName(pm.getName()).getCPUcapacity() -olderPM._idleCPU;
			int usedMEM = _info.getPMs().getPMByName(pm.getName()).getCPUcapacity() -olderPM._idleCPU;
			if (usedCPU <= pm.getCPUcapacity() && usedMEM <= pm.getMEMcapacity()) {
				//替换
				olderPM.setPlacedPM(pm);
				olderPM.setIdleCPU(pm.getCPUcapacity() - usedCPU);
				olderPM.setIdleMEM(pm.getMEMcapacity() - usedMEM);
			}

		}
	}


	@Override
	public String toString() {
		//PM类型数量
		int numPMs = _info.getPMs().numPMs();
		//每种PM类型的部署数量
		int[] numPlacedPM = new int[numPMs];

		String[] output = new String[numPMs];
		for (int i = 0; i < output.length; i++) {
			output[i] = "";
		}

		for (int i = 0; i < _PMsoluList.size(); i++) {
			PMentry pm = _PMsoluList.get(i);
			int PMindex = pm.getPlacedPM().getIndex();
			numPlacedPM[PMindex]++;
			output[PMindex] += pm.getPlacedPM().getName() + "-" +
				numPlacedPM[PMindex] + " " + pm.toString() + "\n";

		}

		StringBuilder result = new StringBuilder();
		for (int i = 0; i < output.length; i++) {
			if ("".equals(output[i])) {
				continue;
			}
			result.append(_info.getPMs().getPM(i).getName() + " " + numPlacedPM[i] + "\n");
			result.append(output[i]);
			if (i != output.length-1) {
				result.append("\n");
			}
		}

		return result.toString();
	}


	/**
	 * 得到空闲资源利用率，相当于资源利用率
	 * @return
	 */
	public double getUtilization(){
		int sumIdleCPU = 0;
		int sumIdleMEM = 0;
		int CPUcapacity = 0;
		int MEMcapacity = 0;
		int numPM = _PMsoluList.size();
		for (int i = 0; i < numPM; i++) {
			PMentry pmentry = _PMsoluList.get(i);
			sumIdleCPU += pmentry.getIdleCPU();
			sumIdleMEM += pmentry.getIdleMEM();
			CPUcapacity += pmentry.getPlacedPM().getCPUcapacity();
			MEMcapacity += pmentry.getPlacedPM().getMEMcapacity();
		}
		//return 0.5 * sumIdleCPU + 0.5 * sumIdleMEM;
		return 0.5 * sumIdleCPU / CPUcapacity  + 0.5 * sumIdleMEM / MEMcapacity;

	}


	@Override
	public int compareTo(PMsolu o) {
		if (this.getUtilization() > o.getUtilization()) {
			return 1;
			}else if (this.getUtilization() < o.getUtilization()) {
				return -1;
			}else {
				return 0;
		}
	}



	/*
	 * 单个物理服务器部署情况
	 */
	public class PMentry{
		//PM
		private PM _placedPM;
		//放置的VM规格
		private Map<VM, Integer> _VMplaceMap;
		//物理服务器空闲CPU容量
		private int _idleCPU;
		//物理服务器空闲MEM容量
		private int _idleMEM;

		public PMentry(PM placedPM) {
			_placedPM = placedPM;
			_VMplaceMap = new LinkedHashMap<VM, Integer>();
			_idleCPU = placedPM.getCPUcapacity();
			_idleMEM = placedPM.getMEMcapacity();
		}


		public PM getPlacedPM() {
			return _placedPM;
		}

		public void setPlacedPM(PM _placedPM) {
			this._placedPM = _placedPM;
		}

		public void setIdleCPU(int _idleCPU) {
			this._idleCPU = _idleCPU;
		}

		public void setIdleMEM(int _idleMEM) {
			this._idleMEM = _idleMEM;
		}


		public int getIdleCPU() {
			return _idleCPU;
		}

		public int getIdleMEM() {
			return _idleMEM;
		}

		public int getVMplaceNum(VM vm) {
			return _VMplaceMap.get(vm);
		}

		public int getVMplaceTotalNum() {
			int sum = 0;
			Iterator<Entry<VM, Integer>> iterator= _VMplaceMap.entrySet().iterator();
			while(iterator.hasNext())
			{
			    Entry<VM, Integer> entry = iterator.next();
			    sum += entry.getValue();
			}
			return sum;
		}

		public Map<VM, Integer> getVMplace(){
			return _VMplaceMap;
		}

		public boolean hasVMplace(VM vm){
			if (!_VMplaceMap.containsKey(vm) || getVMplaceNum(vm) == 0) {
				return false;
			}else {
				return true;
			}
		}

		public void addVMplaceNum(VM vm, int num){
			if (hasVMplace(vm)) {
				num += getVMplaceNum(vm);
			}
			_idleCPU -= vm.getCPU();
			_idleMEM -= vm.getMEM();
			_VMplaceMap.put(vm, num);
		}

		@Override
		public String toString() {
			StringBuilder output = new StringBuilder();
			Iterator<Entry<VM, Integer>> iterator= _VMplaceMap.entrySet().iterator();

			while(iterator.hasNext())
			{
			    Entry<VM, Integer> entry = iterator.next();
			    output.append(" " + entry.getKey().getName() + " " + entry.getValue());
			}
			return output.toString();
		}


	}

}
