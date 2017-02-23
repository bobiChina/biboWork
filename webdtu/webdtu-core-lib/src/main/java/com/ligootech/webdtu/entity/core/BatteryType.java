package com.ligootech.webdtu.entity.core;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "BatteryType")
public class BatteryType {
    @Id
    @Column(name = "id", insertable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Column(name = "typeName")
    private String typeName;
    @Column(name = "typeVoltage")
    private String typeVoltage;
    
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getTypeName() {
		return typeName;
	}
	public void setTypeName(String typeName) {
		this.typeName = typeName;
	}
	public String getTypeVoltage() {
		return typeVoltage;
	}
	public void setTypeVoltage(String typeVoltage) {
		this.typeVoltage = typeVoltage;
	}
    
}
