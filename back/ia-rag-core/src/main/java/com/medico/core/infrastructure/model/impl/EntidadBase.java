package com.medico.core.infrastructure.model.impl;

import java.io.Serializable;
import com.medico.core.infrastructure.model.EntidadPersistible;
import jakarta.persistence.Column;
import jakarta.persistence.MappedSuperclass;


/**
 *
 * <p>Si se necesita crear una entidad basica sin campos de auditoria, se usa esta clase
 *
 * */
@MappedSuperclass
public abstract class EntidadBase<Id extends Serializable> implements EntidadPersistible<Id>, Serializable {
	private static final long serialVersionUID = 1L;

	protected Id id;
	protected String estado;
	
	public void setId(Id pId) {
		id = pId;
		
	}
	
	@Column(name = "ESTADO", length = 1)
	public String getEstado() {
		return estado;
	}

	public void setEstado(String estado) {
		this.estado = estado;
	}

	@Override
	public int hashCode() {
		int hash = 0;
		hash += (getId() != null ? getId().hashCode() : 0);
		return hash;
	}

}
