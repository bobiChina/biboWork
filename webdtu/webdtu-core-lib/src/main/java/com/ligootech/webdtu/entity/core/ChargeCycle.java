package com.ligootech.webdtu.entity.core;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "chargeCycle")
public class ChargeCycle {
    @Id
    @Column(name = "id", insertable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
    @ManyToOne
    @JoinColumn(name = "dtuId")
	private Dtu dtu;
    
    @Column(name = "startTime")
	private Date startTime;
    
    @Column(name = "endTime")
	private Date endTime;
    
    @Column(name = "startSoc")
	private float startSoc;
    
    @Column(name = "endSoc")
	private float endSoc;
    
    @Column(name = "totalCapacity")
    private float totalCapacity;
    
    @Column(name = "runningMilege")
    private float runningMilege;
    
    @Column(name = "runningTimeLength")
    private int runningTimeLength;
    
    @Column(name = "totalMilege")
    private float totalMilege;

	public float getTotalCapacity() {
		return totalCapacity;
	}

	public void setTotalCapacity(float totalCapacity) {
		this.totalCapacity = totalCapacity;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Dtu getDtu() {
		return dtu;
	}

	public void setDtu(Dtu dtu) {
		this.dtu = dtu;
	}

	public Date getStartTime() {
		return startTime;
	}

	public void setStartTime(Date startTime) {
		this.startTime = startTime;
	}

	public Date getEndTime() {
		return endTime;
	}

	public void setEndTime(Date endTime) {
		this.endTime = endTime;
	}

	public float getStartSoc() {
		return startSoc;
	}

	public void setStartSoc(float startSoc) {
		this.startSoc = startSoc;
	}

	public float getEndSoc() {
		return endSoc;
	}

	public void setEndSoc(float endSoc) {
		this.endSoc = endSoc;
	}

	public float getRunningMilege() {
		return runningMilege;
	}

	public void setRunningMilege(float runningMilege) {
		this.runningMilege = runningMilege;
	}

	public int getRunningTimeLength() {
		return runningTimeLength;
	}

	public void setRunningTimeLength(int runningTimeLength) {
		this.runningTimeLength = runningTimeLength;
	}

	public float getTotalMilege() {
		return totalMilege;
	}

	public void setTotalMilege(float totalMilege) {
		this.totalMilege = totalMilege;
	}
	
}
