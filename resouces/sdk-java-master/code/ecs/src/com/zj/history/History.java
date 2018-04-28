package com.zj.history;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class History {
	//记录集合
	private List<Record> recordlists;

	public History() {
		recordlists = new ArrayList<Record>();
	}

	public History(int num) {
		recordlists = new ArrayList<Record>(num);
	}



	/**
	 * 添加记录
	 * @param record
	 */
	public void addRecord(Record record) {
		recordlists.add(record);
	}

	/**
	 * 添加记录
	 * @param id
	 * @param flavor
	 * @param date
	 */
	public void addRecord(String id, String flavor, Date date) {
		Record record = new Record(id, flavor, date);
		recordlists.add(record);
	}

	/**
	 * 添加记录
	 * @param id
	 * @param flavor
	 * @param datestring
	 */
	public void addRecord(String id, String flavor, String datestring) {
		Record record = new Record(id, flavor, datestring);
		recordlists.add(record);
	}

	/**
	 * 根据索引得到记录
	 * @param index
	 * @return
	 */
	public Record getRecord(int index) {
		return recordlists.get(index);
	}

	/**
	 * 记录数量
	 * @return
	 */
	public int numRecord(){
		return recordlists.size();
	}

}
