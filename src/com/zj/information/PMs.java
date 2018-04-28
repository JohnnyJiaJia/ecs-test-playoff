package com.zj.information;

import java.util.ArrayList;
import java.util.List;

public class PMs {

	//PM种类集合
	List<PM> PMlists;
	public PMs() {
		PMlists = new ArrayList<PM>();
	}

	public PMs(int num) {
		PMlists = new ArrayList<PM>(num);
	}

	/**
	 * 添加PM
	 * @param pm
	 */
	public void addPM(PM pm) {
		PMlists.add(pm);
	}

	/**
	 * 添加PM
	 * @param name
	 * @param CPUcapacity
	 * @param MEMcapacity
	 * @param DISKcapacity
	 */
	public void addPM(int index, String name, int CPUcapacity, int MEMcapacity, int DISKcapacity) {
		PM pm = new PM(index, name, CPUcapacity, MEMcapacity, DISKcapacity);
		PMlists.add(pm);
	}

	/**
	 * 查找PM
	 * @param index
	 * @return
	 */
	public PM getPM(int index) {
		return PMlists.get(index);
	}

	/**
	 * 通过PM名字得到PM
	 * @param name
	 * @return
	 */
	public PM getPMByName(String name) {
		for(PM pm : PMlists){
			if (pm.getName().equals(name)) {
				return pm;
			}
		}
		return null;
	}

	public List<PM> getPMs() {
		return PMlists;
	}

	/**
	 * 得到PM类型的数量
	 * @return
	 */
	public int numPMs() {
		return PMlists.size();
	}



}
