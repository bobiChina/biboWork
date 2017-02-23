package com.ligootech.webdtu.service.impl.dtu;

import java.util.List;

import org.appdot.service.impl.GenericManagerImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ligootech.webdtu.entity.core.Bmu;
import com.ligootech.webdtu.repository.BmuDao;
import com.ligootech.webdtu.service.dtu.BmuManager;
@Service("bmuManager")
public class BmuManagerImpl extends GenericManagerImpl<Bmu,Long> implements BmuManager{

	@Autowired
	private BmuDao bmuDao;
	@Autowired
	public void setBmuDao(BmuDao bmuDao) {
		super.dao = bmuDao;
		this.bmuDao = bmuDao;
	}
	
	@Override
	public List<Bmu> getBmusByDtuId(Long id) {
		return bmuDao.getBmusByDtuId(id);
	}
	
	

}
