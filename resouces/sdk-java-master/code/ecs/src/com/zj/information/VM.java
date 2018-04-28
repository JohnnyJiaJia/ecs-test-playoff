package com.zj.information;

public class VM {
	//序号，从0开始
	int _index;
	//名称
	String _name;
	//CPU
	int _CPU;
	//MEN
	int _MEM;

	public VM(int index, String name, int CPU, int MEM) {
		this._index = index;
		this._name = name;
		this._CPU = CPU;
		this._MEM = MEM;
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
	public void setName(String name) {
		this._name = name;
	}
	public int getCPU() {
		return _CPU;
	}
	public void setCPU(int CPU) {
		_CPU = CPU;
	}
	public int getMEM() {
		return _MEM;
	}
	public void setMEM(int MEM) {
		_MEM = MEM;
	}

	@Override
	public boolean equals(Object obj) {
		if(!(obj instanceof VM)){
			return false;
		}
		VM vm = (VM) obj;
		return vm.getName().equals(_name);
	}

	@Override
	public int hashCode() {
		return _name.hashCode();
	}

}
