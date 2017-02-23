package com.ligootech.webdtu.repository;

import org.appdot.repository.BaseJpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.ligootech.webdtu.entity.core.RunningCycle;

public interface RunningCycleDao extends BaseJpaRepository<RunningCycle, Long>, JpaSpecificationExecutor<RunningCycle> {

}
