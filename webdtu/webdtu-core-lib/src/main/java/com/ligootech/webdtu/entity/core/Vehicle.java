package com.ligootech.webdtu.entity.core;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;

@Entity
@Table(name = "vehicle")
public class Vehicle {
    @Id
    @Column(name = "id", insertable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(name = "uuid")
    private String uuid;
    
    @Column(name = "vehicleNumber")
    private String vehicleNumber;
    
    @ManyToOne
    @JoinColumn(name = "vehicleTypeId")
    private VehicleType vehicleType;
    
    @ManyToOne
    @JoinColumn(name = "vehicleModelId")
    private VehicleModel vehicleModel;
    
    private Date createTime;
    
    @ManyToOne
    @JoinColumn(name = "dtuUserId")
    private DtuUser dtuUser;
    
    @OneToOne
    @JoinColumn(name = "dtuId")
    private Dtu dtu;
    
    

	public Dtu getDtu() {
		return dtu;
	}

	public void setDtu(Dtu dtu) {
		this.dtu = dtu;
	}

	public String getUuid() {
		return uuid;
	}

	public void setUuid(String uuid) {
		this.uuid = uuid;
	}

	public DtuUser getDtuUser() {
		return dtuUser;
	}

	public void setDtuUser(DtuUser dtuUser) {
		this.dtuUser = dtuUser;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}


	public String getVehicleNumber() {
		return vehicleNumber;
	}

	public void setVehicleNumber(String vehicleNumber) {
		this.vehicleNumber = vehicleNumber;
	}

	public VehicleType getVehicleType() {
		return vehicleType;
	}

	public void setVehicleType(VehicleType vehicleType) {
		this.vehicleType = vehicleType;
	}

	public VehicleModel getVehicleModel() {
		return vehicleModel;
	}

	public void setVehicleModel(VehicleModel vehicleModel) {
		this.vehicleModel = vehicleModel;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
    
}
