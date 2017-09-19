package br.edu.uni7.teste.persistence;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import org.junit.Assert;
import org.junit.Test;

public class ConnectionTest {

	@Test
	public void testConnectionWithDB() {
		EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("aulajpa");
		EntityManager entityManager = entityManagerFactory.createEntityManager();
		Assert.assertNotNull(entityManager);
	}
}
