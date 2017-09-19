package br.edu.uni7.persistence;

import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;

@Entity
@Table(name = "TBL_TAREFA_RECORRENTE")
@DiscriminatorValue("TIPO")
@PrimaryKeyJoinColumn(name = "ID_FK")
public class TarefaRecorrente extends Tarefa{

	@Enumerated(EnumType.STRING)
	@Column(name = "NU_PERIODICIDADE")
	private Periodicidade periodicidade;

	public Periodicidade getPeriodicidade() {
		return periodicidade;
	}

	public void setPeriodicidade(Periodicidade periodicidade) {
		this.periodicidade = periodicidade;
	}
}
