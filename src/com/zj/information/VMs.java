package com.zj.information;

import java.util.ArrayList;
import java.util.List;


public class VMs {
	//VM规格集
	private List<VM> VMlists;


	public VMs() {
		VMlists = new ArrayList<VM>();
	}

	public VMs(int num) {
		VMlists = new ArrayList<VM>(num);
	}

	/**
	 * 添加VM规格
	 * @param vm
	 */
	public void addVM(VM vm) {
		VMlists.add(vm);
	}

	/**
	 * 添加VM规格
	 * @param name
	 * @param CPU
	 * @param MEM
	 */
	public void addVM(int index, String name, int CPU, int MEM) {
		VM vm = new VM(index, name, CPU, MEM);
		addVM(vm);
	}


	/**
	 * 查找VM规格
	 * @param index
	 * @return
	 */
	public VM getVM(int index) {
		return VMlists.get(index);
	}

	/**
	 * 通过VM名字得到VM
	 * @param name
	 * @return
	 */
	public VM getVMByName(String name){
		for(VM vm : VMlists){
			if (vm.getName().equals(name)) {
				return vm;
			}
		}
		return null;
	}

	/**
	 * 得到VM类型的数量
	 * @return
	 */
	public int numVMs() {
		return VMlists.size();
	}

	public List<VM> getVMs() {
		return VMlists;
	}

}
