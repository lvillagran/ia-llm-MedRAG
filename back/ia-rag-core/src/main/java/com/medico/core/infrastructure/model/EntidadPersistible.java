package com.medico.core.infrastructure.model;

import java.io.Serializable;

public interface EntidadPersistible<Id extends Serializable>
{
	public Id getId();
	public void setId(Id id);
	public void setEstado(String estado);
	public String getEstado();
	
}
