package com.ligootech.webdtu.entity.core;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "batteryModel")
public class BatteryModel {
    @Id
    @Column(name = "id", insertable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(name = "batteryName")
    private String batteryName;
    
    @Column(name = "factoryName")
    private String factoryName;
    
    @Column(name = "batteryNumber")
    private int batteryNumber;
    
    @Column(name = "capacity")
    private int capacity;
    
    @ManyToOne
    @JoinColumn(name = "batteryType")
    private BatteryType batteryType;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getBatteryName() {
		return batteryName;
	}

	public void setBatteryName(String batteryName) {
		this.batteryName = batteryName;
	}

	public String getFactoryName() {
		return factoryName;
	}

	public void setFactoryName(String factoryName) {
		this.factoryName = factoryName;
	}

	public int getBatteryNumber() {
		return batteryNumber;
	}

	public void setBatteryNumber(int batteryNumber) {
		this.batteryNumber = batteryNumber;
	}

	public int getCapacity() {
		return capacity;
	}

	public void setCapacity(int capacity) {
		this.capacity = capacity;
	}

	public BatteryType getBatteryType() {
		return batteryType;
	}

	public void setBatteryType(BatteryType batteryType) {
		this.batteryType = batteryType;
	}
	
}
