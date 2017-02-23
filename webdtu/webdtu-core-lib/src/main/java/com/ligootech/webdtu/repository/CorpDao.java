package com.ligootech.webdtu.repository;

import org.appdot.repository.BaseJpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.ligootech.webdtu.entity.core.Corp;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface CorpDao extends BaseJpaRepository<Corp, Long>, JpaSpecificationExecutor<Corp>{

    @Query("select c.id,c.corpName FROM Corp as c where c.id>1")
    public List<Corp> getCorpSimpList();

}
