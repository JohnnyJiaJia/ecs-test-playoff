package com.zj.solution;


import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map.Entry;

import com.zj.information.VM;

public class VMsolu {
	//VM预测数量方案
	private LinkedHashMap<VM, Integer> _VMsoluMap;

	public VMsolu() {
		_VMsoluMap = new LinkedHashMap<>();
	}

	public  VMsolu(int num) {
		_VMsoluMap = new LinkedHashMap<>(num);
	}

	/**
	 * 添加num个vm
	 * @param vm
	 * @param num
	 */
	public void addVMsolu(VM vm, int num) {
		if (_VMsoluMap.get(vm) != null ) {
			num += getPredictNum(vm);
		}
		_VMsoluMap.put(vm, num);

	}

	/**
	 * 移除num个vm
	 * @param vm
	 * @param num
	 */
	public void removeVMsoule(VM vm, int num) {
		int old = getPredictNum(vm);
		if (old < num) {
			System.err.println("需要移除的vm数量不够！");
			return;
		}
		_VMsoluMap.put(vm, old - num);
	}

	/**
	 * 得到vm的预测数量
	 * @param vm
	 * @return
	 */
	public int getPredictNum(VM vm) {
		if (_VMsoluMap.get(vm) == null) {
			return 0;
		}else {
			return _VMsoluMap.get(vm);
		}


	}

	public LinkedHashMap<VM, Integer> getPredict() {
		return _VMsoluMap;

	}

	/**
	 * 得到VMsolu数量
	 * @return
	 */
	public int numVMsolu() {
		return _VMsoluMap.size();

	}

	@Override
	public String toString() {

		StringBuilder output = new StringBuilder();

		Iterator<Entry<VM, Integer>> iterator= _VMsoluMap.entrySet().iterator();
		int numPreVMs = 0;
		while(iterator.hasNext())
		{
		    Entry<VM, Integer> entry = iterator.next();
		    output.append(entry.getKey().getName() + " " + entry.getValue() + "\n");
		    numPreVMs += entry.getValue();
		}
		output.append("\n");
		output.insert(0, numPreVMs + "\n");
        return output.toString();
	}



}
