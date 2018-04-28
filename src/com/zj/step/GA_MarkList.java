package com.zj.step;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import com.zj.information.VM;
import com.zj.solution.PMsolu;
import com.zj.solution.VMsolu;

public class GA_MarkList {

	private List<VM> _markList;

	public GA_MarkList(PMsolu pmsolu) {
		_markList = new ArrayList<VM>();
		for (int i = 0; i < pmsolu.numPMsolu(); i++) {
			Map<VM, Integer> VMplaceMap = pmsolu.getPMsolu(i).getVMplace();
			Iterator<Entry<VM, Integer>> iterator= VMplaceMap.entrySet().iterator();
			while(iterator.hasNext())
			{

			    Entry<VM, Integer> entry = iterator.next();
				int num = entry.getValue();
				while (num > 0) {
					_markList.add(entry.getKey());
					num--;
				}

			}
		}
	}

//	public Mark(VMsolu vmsolu) {
//		_markList = new ArrayList<VM>();
//		LinkedHashMap<VM, Integer> predicMap = vmsolu.getPredic();
//		Iterator<Entry<VM, Integer>> iterator= predicMap.entrySet().iterator();
//		while(iterator.hasNext())
//		{
//			Entry<VM, Integer> entry = iterator.next();
//			int num = entry.getValue();
//			while (num > 0) {
//				_markList.add(entry.getKey());
//				num--;
//			}
//		}
//	}

	public int numMark(){
		return _markList.size();
	}

	public VM getMark(int index) {
		return _markList.get(index);

	}
	@Override
	public String toString() {
		StringBuilder output = new StringBuilder();

		for (int i = 0; i < _markList.size(); i++) {
			output.append(i + ":" + _markList.get(i).getName() + "  ");
		}
		return output.toString();
	}




}
