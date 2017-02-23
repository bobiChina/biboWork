package com.ligootech.webdtu.repository;

import com.ligootech.webdtu.entity.core.SnModule;
import org.appdot.repository.BaseJpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * 模块SN码信息
 */
public interface SnModuleDao extends BaseJpaRepository<SnModule, Long>, JpaSpecificationExecutor<SnModule>{

	@Query("from SnModule")
	public List<SnModule> gSnModuleMaps();

	@Query("select id,type,code from SnModule order by id desc")
	public List<Object[]> findSimpleSnModuleList();

	@Query("from SnModule smo where smo.code=:code")
	public SnModule findSnModuleByCode(@Param("code") String code);

}
