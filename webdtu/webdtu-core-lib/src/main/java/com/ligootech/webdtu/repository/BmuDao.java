package com.ligootech.webdtu.repository;

import java.util.List;

import org.appdot.repository.BaseJpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.ligootech.webdtu.entity.core.Bmu;

public interface BmuDao extends BaseJpaRepository<Bmu, Long>, JpaSpecificationExecutor<Bmu>{
	
	@Query("from Bmu as bmu where bmu.dtu.id = :id ") // 去掉箱体温感不为空的限制 AND bmu.boxTemperList IS NOT NULL
	public List<Bmu> getBmusByDtuId(@Param("id") Long id);
}
