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
@Table(name = "alarmHistory")
public class AlarmHistory {
	    @Id
	    @Column(name = "id", insertable = false)
	    @GeneratedValue(strategy = GenerationType.IDENTITY)
	    private Long id;
	    
	    @ManyToOne
	    @JoinColumn(name = "dtuId")
	    private Dtu dtu;
	    
	    @Column(name = "alarmType")
	    private int alarmType;
	    
	    @Column(name = "alarmStartTime")
	    private Date alarmStartTime;
	    
	    @Column(name = "alarmEndTime")
	    private Date alarmEndTime;

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

		public int getAlarmType() {
			return alarmType;
		}

		public void setAlarmType(int alarmType) {
			this.alarmType = alarmType;
		}

		public Date getAlarmStartTime() {
			return alarmStartTime;
		}

		public void setAlarmStartTime(Date alarmStartTime) {
			this.alarmStartTime = alarmStartTime;
		}

		public Date getAlarmEndTime() {
			return alarmEndTime;
		}

		public void setAlarmEndTime(Date alarmEndTime) {
			this.alarmEndTime = alarmEndTime;
		}
	    
}
