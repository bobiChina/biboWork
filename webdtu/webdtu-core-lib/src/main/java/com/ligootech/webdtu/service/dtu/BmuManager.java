package com.ligootech.webdtu.service.dtu;

import java.util.List;

import org.appdot.service.GenericManager;
import org.springframework.data.repository.query.Param;

import com.ligootech.webdtu.entity.core.Bmu;

public interface BmuManager extends GenericManager<Bmu, Long>{
	public List<Bmu> getBmusByDtuId(@Param("id") Long id);
}
