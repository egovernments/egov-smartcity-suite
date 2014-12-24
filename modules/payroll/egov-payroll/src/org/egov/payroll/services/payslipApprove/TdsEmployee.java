package org.egov.payroll.services.payslipApprove;

import org.egov.commons.utils.EntityType;
import org.egov.model.recoveries.Recovery;

public class TdsEmployee {
	
	private Recovery tds;
	private EntityType entity;
	
		
	public EntityType getEntity() {
		return entity;
	}
	public void setEntity(EntityType entity) {
		this.entity = entity;
	}
	public Recovery getTds() {
		return tds;
	}
	public void setTds(Recovery tds) {
		this.tds = tds;
	}
	
	public boolean equals(Object o) {
		if ((o instanceof TdsEmployee) && (((TdsEmployee)o).getEntity() == (this.getEntity())) &&
				((TdsEmployee)o).getTds() == this.getTds()) {
		return true;
		} else {
		return false;
		}
	}
	
	public int hashCode() {
		Integer hash = 0;
		if(tds != null)
			{
			hash = hash + tds.getId().intValue();
			}
		if(entity != null)
			{
			hash = hash + entity.getEntityId();	
			}
		return hash;
	}
	
	
}
