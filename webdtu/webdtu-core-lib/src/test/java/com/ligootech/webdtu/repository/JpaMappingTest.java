package com.ligootech.webdtu.repository;

import javax.persistence.metamodel.EntityType;
import javax.persistence.metamodel.Metamodel;

import org.appdot.test.dao.BaseDaoJpaTestCase;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class JpaMappingTest extends BaseDaoJpaTestCase {

	private static Logger logger = LoggerFactory.getLogger(JpaMappingTest.class);

	@Test
	public void allClassMapping() throws Exception {
		Metamodel model = entityManager.getEntityManagerFactory().getMetamodel();
		for (EntityType entityType : model.getEntities()) {
			String entityName = entityType.getName();
			entityManager.createQuery("select o from " + entityName + " o").getFirstResult();
			logger.info("ok: " + entityName);
		}
	}
}
