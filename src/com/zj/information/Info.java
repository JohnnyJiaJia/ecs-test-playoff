package com.zj.information;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.temporal.ChronoUnit;
import java.util.Date;

public class Info {
	//预测开始时间
	Date _startDate;
	//预测结束时间
	Date _endDate;
	//物理服务器类型
	PMs _PMs;
	//虚拟机规格集
	VMs _VMs;
	//预测天数
	int _predictDays;
	//预测间隔天数
	int _spaceDays;

	public Info(Date _startDate, Date _endDate, PMs _PMs, VMs _VMs) {
		this._startDate = _startDate;
		this._endDate = _endDate;
		this._PMs = _PMs;
		this._VMs = _VMs;
		int totalHours =  (int) ChronoUnit.HOURS.between(this._startDate.toInstant(), this._endDate.toInstant()) + 1;
		this._predictDays = totalHours / 24;
	}

	public Info(String startDateString, String endDateString, PMs _PMs, VMs _VMs) {
		this._PMs = _PMs;
		this._VMs = _VMs;
		DateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//日期格式
		Date startDate = null;
		Date endDate = null;
		try {
			startDate = format.parse(startDateString);
			endDate = format.parse(endDateString);
		} catch (ParseException e) {
			e.printStackTrace();
		}

		this._startDate = startDate;
		this._endDate = endDate;
		int totalHours =  (int) ChronoUnit.HOURS.between(this._startDate.toInstant(), this._endDate.toInstant()) + 1;
		this._predictDays = totalHours / 24;
	}

	public Date getStartdate() {
		return _startDate;
	}
	public void setStartdate(Date _startdate) {
		this._startDate = _startdate;
	}
	public Date getEndDate() {
		return _endDate;
	}
	public void setEndDate(Date _endDate) {
		this._endDate = _endDate;
	}


	public int getPredictDays() {
		return _predictDays;
	}

	public PMs getPMs() {
		return _PMs;
	}

	public void setPMs(PMs _PMs) {
		this._PMs = _PMs;
	}

	public VMs getVMs() {
		return _VMs;
	}

	public void setVMs(VMs _VMs) {
		this._VMs = _VMs;
	}
	public int getSpaceDays() {
		return _spaceDays;
	}

	public void setSpaceDays(int _spaceDays) {
		this._spaceDays = _spaceDays;
	}



}
