package com.ligootech.webdtu.repository;

import org.appdot.repository.BaseJpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.ligootech.webdtu.entity.core.DtuUser;

public interface DtuUserDao extends BaseJpaRepository<DtuUser, Long>, JpaSpecificationExecutor<DtuUser> {
	
	@Query("from DtuUser as du where du.userName = :userName")
	public DtuUser getUserByName(@Param("userName") String userName);

	@Query("from DtuUser as du where du.userName = :userName and du.isAdmin < :isAdmin")
	public DtuUser getLoginUserByName(@Param("userName") String userName, @Param("isAdmin") int isAdmin);

	@Query("from DtuUser as du where du.userName = :userName and du.isAdmin=2")
	public DtuUser findBackenLoginByUserName(@Param("userName") String userName);
	
	@Query("from DtuUser du where du.id = :id")
	public DtuUser getit(@Param("id") Long id);
	
}
