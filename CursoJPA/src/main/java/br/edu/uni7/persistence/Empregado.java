package br.edu.uni7.persistence;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.AttributeOverride;
import javax.persistence.AttributeOverrides;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.NamedNativeQuery;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Version;

@Entity
@Table(name = "TBL_EMPREGADOS")
@NamedQueries({
	@NamedQuery(name = "Empregado.findByCidade", query = "select e from Empregado e where e.endereco.cidade = :cidade"),
	@NamedQuery(name = "Empregado.findByName", query = "select e from Empregado e where e.nome = :nome")
})
@NamedNativeQuery(name = "Empregado.findByNumDoc", query = "select e.* from TBL_EMPREGADOS e "
												+ "join TBL_DOCUMENTOS doc on e.FK_DOC = doc.PK_DOC "
												+ "where doc.NU_NUMERO  = ?1",
										resultClass = Empregado.class)
public class Empregado {

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name = "PK_EMP")
	private Long id;
	
	@Column(name = "NM_NOME")
	private String nome;
	
	@Column(name = "NM_EMAIL")
	private String email;
	
	@Column(name = "DT_NASC")
	private Date dataNascimento;
	
	@Column(name = "NU_SALARIO")
	private BigDecimal salario;
	
	@Embedded
	@AttributeOverrides(value = { 
			@AttributeOverride(name = "cep", column = @Column(name = "NU_COD_POSTAL"))})
	private Endereco endereco;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "FK_DEP")
	private Departamento departamento;
	
	@OneToOne(cascade = { CascadeType.REMOVE, CascadeType.PERSIST }, fetch = FetchType.LAZY)
	@JoinColumn(name = "FK_DOC")
	private Documento documento;
	
	@ManyToMany
	@JoinTable(name = "TBL_EMP_PRJS",
				joinColumns = { @JoinColumn(name = "FK_EMP")},
				inverseJoinColumns = { @JoinColumn(name = "FK_PROJ")})
	private List<Projeto> projetos = new ArrayList<Projeto>();
	
	@Version
	@Column(name="NU_VERSAO")
	private Long versao;
	
	// GETTERS AND SETTERS
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public Date getDataNascimento() {
		return dataNascimento;
	}

	public void setDataNascimento(Date dataNascimento) {
		this.dataNascimento = dataNascimento;
	}

	public BigDecimal getSalario() {
		return salario;
	}

	public void setSalario(BigDecimal salario) {
		this.salario = salario;
	}

	public Departamento getDepartamento() {
		return departamento;
	}

	public void setDepartamento(Departamento departamento) {
		this.departamento = departamento;
	}

	public List<Projeto> getProjetos() {
		return projetos;
	}

	public void setProjetos(List<Projeto> projetos) {
		this.projetos = projetos;
	}

	public Documento getDocumento() {
		return documento;
	}

	public void setDocumento(Documento documento) {
		this.documento = documento;
	}

	public Endereco getEndereco() {
		return endereco;
	}

	public void setEndereco(Endereco endereco) {
		this.endereco = endereco;
	}

	public Long getVersao() {
		return versao;
	}

	public void setVersao(Long versao) {
		this.versao = versao;
	}
	
	// HASHCODE AND EQUALS

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Empregado other = (Empregado) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}
}
