/*
 * DepreciationMasterDao.java Created on Nov 24, 2005
 *
 * Copyright 2005 eGovernments Foundation. All rights reserved.
 * EGOVERNMENTS PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package org.egov.demand.dao;

import java.util.List;

import org.egov.commons.Installment;
import org.egov.demand.model.DepreciationMaster;
import org.egov.infstr.commons.Module;
import org.egov.infstr.dao.GenericDAO;

/**
 * TODO Brief Description of the puprpose of the class/interface
 * 
 * @author Administrator
 * @version 1.00 
 * @see	    
 * @see	    
 * @since   1.00
 */
public interface DepreciationMasterDao extends GenericDAO
{
	public DepreciationMaster getDepreciationMaster(Module mod, Integer year);
	public List getDepreciationsForModule(Module mod);
	public List getDepreciationsForModulebyHistory(Module mod);
	public Float getDepreciationPercent(Integer year);
    /**
     * getAllNonHistoryDepreciationRates
     * @return list
     */
    public List getAllNonHistoryDepreciationRates();

    /**
	 * Added By Rajalakshmi D.N. on 07/05/2007
	 * Description : Returns the Non-History Depreciation for the Given Module, Year and Installment 
	 * @param Module,Year and Installment 
	 * @return DepreciationMaster record
	 */	
	public DepreciationMaster getNonHistDepMasterByModuleInsYr(Module mod,Integer year,Installment insYear);
	
	
	 public Float getLeastDepreciationPercent(Integer year);

}
