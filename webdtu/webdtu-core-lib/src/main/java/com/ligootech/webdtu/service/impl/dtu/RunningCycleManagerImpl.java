package com.ligootech.webdtu.service.impl.dtu;

import org.appdot.service.impl.GenericManagerImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ligootech.webdtu.entity.core.RunningCycle;
import com.ligootech.webdtu.repository.RunningCycleDao;
import com.ligootech.webdtu.service.dtu.RunningCycleManager;

@Service("runningCycleManager")
public class RunningCycleManagerImpl extends GenericManagerImpl<RunningCycle, Long> implements RunningCycleManager {
	@Autowired
	private RunningCycleDao runningCycleDao;

	@Autowired
	public void setCorpDao(RunningCycleDao runningCycleDao) {
		super.dao = runningCycleDao;
		this.runningCycleDao = runningCycleDao;
	}
}
