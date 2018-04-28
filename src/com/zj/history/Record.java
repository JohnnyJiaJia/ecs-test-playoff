package com.zj.history;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Record {

	//id
	String _id;
	//VM类型
	String _flavor;
	//日期
	Date _createdate;



	public Record(String _id, String _flavor, Date _createdate) {
		this._id = _id;
		this._flavor = _flavor;
		this._createdate = _createdate;
	}

	public Record(String _id, String _flavor, String _createdatestring) {
		//DateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//日期格式
		DateFormat format = new SimpleDateFormat("yyyy-MM-dd");//日期格式
		Date date = null;
		try {
			date = format.parse(_createdatestring);
		} catch (ParseException e) {
			e.printStackTrace();

		}
		this._id = _id;
		this._flavor = _flavor;
		this._createdate = date;
	}

	public String getId() {
		return _id;
	}
	public void setId(String _id) {
		this._id = _id;
	}
	public String getFlavor() {
		return _flavor;
	}
	public void setFlavor(String _flavor) {
		this._flavor = _flavor;
	}
	public Date getCreatedate() {
		return _createdate;
	}
	public void setCreatedate(Date _createdate) {
		this._createdate = _createdate;
	}

	@Override
	public boolean equals(Object obj) {
		if(!(obj instanceof Record)){
			return false;
		}
		Record record = (Record) obj;
		return record.getId().equals(_id);
	}

	@Override
	public int hashCode() {
		return _id.hashCode();
	}

}
