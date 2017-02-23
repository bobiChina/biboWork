package com.ligootech.webdtu.repository;

import com.ligootech.webdtu.entity.core.ProductType;
import org.appdot.repository.BaseJpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/**
 * Created by wly on 2015/11/10 13:43.
 */
public interface ProductTypeDao extends BaseJpaRepository<ProductType, Long>, JpaSpecificationExecutor<ProductType> {
    @Query("select id,prodTypename from ProductType order by id desc")
    public List<Object[]> findSimpleProductList();

}
