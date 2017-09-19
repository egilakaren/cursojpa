package br.edu.uni7.teste.persistence;

import java.math.BigDecimal;

import javax.persistence.EntityManager;

import br.edu.uni7.persistence.Departamento;
import br.edu.uni7.persistence.Empregado;

public class Util {
	
	public static Empregado criarEmpregado() {
		Empregado empregado = new Empregado();
		empregado.setNome("Empregado " + ((int) Math.random()*1000));
		return empregado;
	}
	
	public static Departamento criarDepartamento() {
		Departamento departamento = new Departamento();
		departamento.setNome("Departamento " + Math.random()*1000);
		departamento.setOrcamento(new BigDecimal ((Math.random()*10000) * (Math.random()*10)));
		return departamento;
	}
	
	public static void gerarDepartamentosComEmpregados(EntityManager entityManager) {
		int maxDep = (int)(Math.random() * 15);
		
		for (int i = 0; i < maxDep; i++) {
			Departamento departamento = criarDepartamento();		
			entityManager.persist(departamento);
			
			int maxEmp = (int)(Math.random() * 40);
			for (int j = 0; j < maxEmp; j++) {
				Empregado emp = criarEmpregado();	
				emp.setDepartamento(departamento);
				entityManager.persist(emp);
			}						
		}
	}
}
