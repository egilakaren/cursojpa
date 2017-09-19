package br.edu.uni7.teste.persistence;

import java.util.Date;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.LockModeType;
import javax.persistence.OptimisticLockException;
import javax.persistence.Persistence;

import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import br.edu.uni7.persistence.Departamento;
import br.edu.uni7.persistence.Documento;
import br.edu.uni7.persistence.Empregado;
import br.edu.uni7.persistence.Endereco;
import br.edu.uni7.persistence.Periodicidade;
import br.edu.uni7.persistence.Projeto;
import br.edu.uni7.persistence.Tarefa;
import br.edu.uni7.persistence.TarefaRecorrente;
import br.edu.uni7.persistence.TarefaUnica;

public class EmpregadoTest {

	private static EntityManager entityManager;
	
	@BeforeClass
	public static void startup() {
		EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("aulajpa");
		entityManager = entityManagerFactory.createEntityManager();
	}
	
	@Test
	public void testInsert(){
		entityManager.getTransaction().begin();
		
		Empregado empregado = Util.criarEmpregado();
		entityManager.persist(empregado);
		entityManager.getTransaction().commit();
		
		Assert.assertNotNull(empregado);
	}
	
	@Test
	public void testUpdate(){
		entityManager.getTransaction().begin();
		
		Empregado empregado = Util.criarEmpregado();
		entityManager.persist(empregado);
		entityManager.getTransaction().commit();
		
		empregado.setNome("Novo nome ");
		entityManager.getTransaction().begin();
		Empregado novoEmpregado = entityManager.merge(empregado);
		entityManager.getTransaction().commit();
		
		entityManager.clear();
		
		Assert.assertEquals("Novo nome ", novoEmpregado.getNome());
	}

	@Test
	public void testRemove(){
		entityManager.getTransaction().begin();
		
		Empregado empregado = Util.criarEmpregado();
		entityManager.persist(empregado);
		entityManager.remove(empregado);
		entityManager.getTransaction().commit();
		
		Assert.assertNull(entityManager.find(Empregado.class, empregado.getId()));
	}
	
	@Test
	public void testDetach(){
		entityManager.getTransaction().begin();
		
		Empregado empregado = Util.criarEmpregado();
		entityManager.persist(empregado);
		
		String nomeAntesDaMudanca = empregado.getNome();
		
		entityManager.detach(empregado);
		empregado.setNome("Novo nome");
		
		entityManager.getTransaction().commit();
		entityManager.close();
		
		Empregado empregadoDB = entityManager.find(Empregado.class, empregado.getId());
		
		Assert.assertEquals(nomeAntesDaMudanca, empregadoDB.getNome());
	}
	
	@Test
	public void testAssociarDepartamento(){
		entityManager.getTransaction().begin();
		
		Empregado empregado = Util.criarEmpregado();
		Departamento departamento = Util.criarDepartamento();
		entityManager.persist(departamento);	

		empregado.setDepartamento(departamento);
		entityManager.persist(empregado);
		entityManager.getTransaction().commit();
		entityManager.clear(); 
		
		Empregado empregadoDB = entityManager.find(Empregado.class, empregado.getId());
		Assert.assertNotNull(empregadoDB.getDepartamento());
		Assert.assertNotNull(empregadoDB.getDepartamento().getId());
	}
	
	@Test
	public void testAssociarProjetoEmpregado(){
		entityManager.getTransaction().begin();
		
		Empregado empregado = Util.criarEmpregado();
		
		Projeto projeto = new Projeto();
		projeto.setNome("MARS");
		entityManager.persist(projeto);
		
		empregado.getProjetos().add(projeto);
		entityManager.persist(empregado);
		
		entityManager.getTransaction().commit();
		entityManager.clear();
		
		Empregado empregadoDB = entityManager.find(Empregado.class, empregado.getId());
		Assert.assertTrue(empregadoDB.getProjetos().size() == 1);		
		Assert.assertTrue(empregadoDB.getProjetos().get(0).getEmpregados().size() == 1);
	}
	
	@Test
	public void testAssociarEmpregadoProjeto(){
		entityManager.getTransaction().begin();
		
		Empregado empregado = Util.criarEmpregado();
		entityManager.persist(empregado);
		
		Projeto projeto = new Projeto();
		projeto.setNome("MARS");
		projeto.getEmpregados().add(empregado);
		entityManager.persist(projeto);
		
		entityManager.getTransaction().commit();
		entityManager.clear();
		
		Empregado empregadoDB = entityManager.find(Empregado.class, empregado.getId());
		Assert.assertTrue(empregadoDB.getProjetos().size() == 0);
	}
	
	@Test
	public void testAdicionarDoc(){
		entityManager.getTransaction().begin();
		
		Empregado emp = Util.criarEmpregado();
		
		Documento doc = new Documento();
		doc.setNumero(1L);
		entityManager.persist(doc);
		
		emp.setDocumento(doc);
		entityManager.persist(emp);
		
		entityManager.getTransaction().commit();
		entityManager.clear();
		
		Empregado empregadoDB = entityManager.find(Empregado.class, emp.getId());
		Assert.assertNotNull(empregadoDB);
		Assert.assertNotNull(empregadoDB.getDocumento().getId());
	}
	
	@Test
	public void testRemoveDoc(){
		entityManager.getTransaction().begin();
		
		Empregado emp = Util.criarEmpregado();
		
		Documento doc = new Documento();
		doc.setNumero(1L);
		entityManager.persist(doc);
		
		emp.setDocumento(doc);
		entityManager.persist(emp);
		
		entityManager.remove(emp);
		
		entityManager.getTransaction().commit();
		entityManager.clear();
		
		Documento documentoDB = entityManager.find(Documento.class, emp.getDocumento().getId());
		Assert.assertNull(documentoDB);
	}
	
	@Test
	public void testAdicionarEndereco(){
		entityManager.getTransaction().begin();
		
		Empregado emp = Util.criarEmpregado();
		Endereco end = new Endereco();
		end.setRua("Rua A");
		end.setCidade("Fortaleza");
		end.setCep(45L);
		emp.setEndereco(end);
		
		entityManager.persist(emp);
		entityManager.getTransaction().commit();
		
		Assert.assertNotNull(emp.getId());		
		Assert.assertNotNull(emp.getEndereco());		
	}
	
	@Test
	public void testEmpregadoFindByCidade(){
		entityManager.getTransaction().begin();
		
		Empregado emp = Util.criarEmpregado();
		Endereco end = new Endereco();
		end.setRua("Rua A");
		end.setCidade("Fortaleza");
		end.setCep(45L);
		emp.setEndereco(end);
		
		entityManager.persist(emp);
		entityManager.getTransaction().commit();
		entityManager.clear();
		
		List<Empregado> empregados = entityManager.createNamedQuery("Empregado.findByCidade", Empregado.class)
				.setParameter("cidade", emp.getEndereco().getCidade())
				.getResultList();
		
		Assert.assertEquals(1, empregados.size());
	}
	
	@Test
	public void testEmpregadoFindByName(){
		entityManager.getTransaction().begin();
		
		Empregado emp = Util.criarEmpregado();
		entityManager.persist(emp);
		entityManager.getTransaction().commit();
		entityManager.clear();
		
		List<Empregado> empregados = entityManager.createNamedQuery("Empregado.findByName", Empregado.class)
				.setParameter("nome", emp.getNome())
				.getResultList();
		
		Assert.assertEquals(1, empregados.size());
	}

	@Test
	public void testEmpregadoMaiorOrcPorDep(){
		entityManager.getTransaction().begin();
		
		Util.gerarDepartamentosComEmpregados(entityManager);

		entityManager.getTransaction().commit();
		entityManager.clear();
		
		List<Object[]> resultList = entityManager.createNamedQuery("Departamento.quantoEmpregadosMaiorOrcamento", Object[].class)
				.getResultList();
		
		Assert.assertTrue(!resultList.isEmpty());
	}
	
	@Test
	public void testEmpregadoSemProjeto(){
		entityManager.getTransaction().begin();
		
		Util.gerarDepartamentosComEmpregados(entityManager);

		entityManager.getTransaction().commit();
		entityManager.clear();
		
		List<Departamento> departamento = entityManager.createNamedQuery("Departamento.semEmpregadosEmProjeto", Departamento.class)
				.getResultList();
		
		Assert.assertTrue(departamento.size() > 0);
	}
	
	@Test
	public void testEmpregadoFindByNumDoc() {
		entityManager.getTransaction().begin();
		
		Empregado emp = Util.criarEmpregado();
		Documento doc = new Documento();
		doc.setNumero((long) (Math.random()*1000));
		entityManager.persist(doc);
		
		emp.setDocumento(doc);
		entityManager.persist(emp);
		entityManager.getTransaction().commit();
		entityManager.clear();
		
		List<Empregado> emps = entityManager.createNamedQuery("Empregado.findByNumDoc", Empregado.class)
				.setParameter(1, doc.getNumero())
				.getResultList();
		
		Assert.assertTrue(emps.size() > 0);
	}
	
	@Test
	public void testCriarEAssociarTarefa() {
		entityManager.getTransaction().begin();
		
		Empregado emp = Util.criarEmpregado();
		
		Projeto proj = new Projeto();
		proj.setNome("Projeto 1");
		entityManager.persist(proj);
		
		emp.getProjetos().add(proj);
		entityManager.persist(emp);
		
		TarefaUnica tarefaUnica = new TarefaUnica();
		tarefaUnica.setNome("Tarefa Unica 1");
		tarefaUnica.setDataCriacao(new Date());
		tarefaUnica.setEmpregado(emp);
		tarefaUnica.setProjeto(proj);
		entityManager.persist(tarefaUnica);
		
		TarefaRecorrente tarefaRecorrente = new TarefaRecorrente();
		tarefaRecorrente.setNome("Tarefa Recorrente 1");
		tarefaRecorrente.setDataCriacao(new Date());
		tarefaRecorrente.setEmpregado(emp);
		tarefaRecorrente.setProjeto(proj);
		tarefaRecorrente.setPeriodicidade(Periodicidade.DIARIO);
		entityManager.persist(tarefaRecorrente);
		
		entityManager.getTransaction().commit();
		entityManager.clear();
		
		Projeto projDB = entityManager.find(Projeto.class, proj.getId());
		List<Tarefa> tarefas = projDB.getTarefas();
		for(int i = 0; i < tarefas.size(); i++){
			Tarefa tarefa = tarefas.get(i);
			System.out.println(String.format("Tarefa %d -> %s", i+1, tarefa.getNome()));
		}
		
		Assert.assertTrue(projDB.getTarefas().size() == 2);
	}
	
	@Test
	public void testCriarEmpregadoVersao(){
		entityManager.getTransaction().begin();
		
		Empregado emp = Util.criarEmpregado();
		entityManager.persist(emp);
		entityManager.getTransaction().commit();
		entityManager.clear();
		
		Assert.assertNotNull(emp.getVersao());
	}
	
	@Test(expected=javax.persistence.OptimisticLockException.class)
	public void testModificarEmpregadoComVersaoAntiga(){
		entityManager.getTransaction().begin();
		
		Empregado emp = Util.criarEmpregado();
		entityManager.persist(emp);
		entityManager.getTransaction().commit();
		entityManager.clear();
		
		entityManager.getTransaction().begin();
		Empregado empregado = entityManager.find(Empregado.class, emp.getId());
		empregado.setNome("Novo Nome");
		entityManager.merge(empregado);
		entityManager.getTransaction().commit();
		entityManager.clear();
		
		entityManager.getTransaction().begin();
		try {
			emp.setNome("Novo Nome 2");
			entityManager.merge(emp);
			entityManager.getTransaction().commit();
		} catch (OptimisticLockException e) {
			entityManager.getTransaction().rollback();
			throw e;
		}
	}
	
	@Test
	public void testModificarDocumentoDoEmpregado(){
		entityManager.getTransaction().begin();
		
		Empregado emp = Util.criarEmpregado();
		
		Documento doc = new Documento();
		doc.setNumero(454512L);
		
		emp.setDocumento(doc);
		
		entityManager.persist(emp);
		entityManager.getTransaction().commit();
		entityManager.clear();
		
		entityManager.getTransaction().begin();
		
		Empregado empregado = entityManager.find(Empregado.class, emp.getId());
		entityManager.lock(empregado, LockModeType.OPTIMISTIC_FORCE_INCREMENT);
		empregado.getDocumento().setNumero(55121L);
		
		Long versaoAntiga = empregado.getVersao();
		
		entityManager.getTransaction().commit();
		entityManager.clear();
		
		Empregado empregadoNovaVersao = entityManager.find(Empregado.class, empregado.getId());
		
		Assert.assertNotEquals(versaoAntiga, empregadoNovaVersao.getVersao());
	}
}
