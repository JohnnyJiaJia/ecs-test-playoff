package com.zj.information;

public class PM {
	//索引，从0开始
	int _index;
	//PM名字
	String _name;
	//CPU容量
	int _CPUcapacity;
	//MEM容量
	int _MEMcapacity;
	//硬盘容量
	int _DISKcapacity;

	public PM(int _index, String _name, int CPUcapacity, int MEMcapacity, int DISKcapacity) {
		this._index = _index;
		this._name = _name;
		this._CPUcapacity = CPUcapacity;
		this._MEMcapacity = MEMcapacity * 1024;
		this._DISKcapacity = DISKcapacity * 1024;
	}

	/**
	 *
	 * @param name
	 * @return
	 */
	public boolean equalByName(String name){
		return getName().equals(name);
	}

	public int getIndex() {
		return _index;
	}

	public void setIndex(int _index) {
		this._index = _index;
	}

	public String getName() {
		return _name;
	}

	public void setName(String _name) {
		this._name = _name;
	}



	public int getCPUcapacity() {
		return _CPUcapacity;
	}
	public void setCPUcapacity(int CPUcapacity) {
		_CPUcapacity = CPUcapacity;
	}
	public int getMEMcapacity() {
		return _MEMcapacity;
	}
	public void setMEMcapacity(int MEMcapacity) {
		_MEMcapacity = MEMcapacity;
	}
	public int getDISKcapacity() {
		return _DISKcapacity;
	}
	public void setDISKcapacity(int DISKcapacity) {
		_DISKcapacity = DISKcapacity;
	}

	@Override
	public boolean equals(Object obj) {
		if(!(obj instanceof PM)){
			return false;
		}
		PM pm = (PM) obj;
		return pm.getName().equals(_name);
	}

	@Override
	public int hashCode() {
		return _name.hashCode();
	}

}
