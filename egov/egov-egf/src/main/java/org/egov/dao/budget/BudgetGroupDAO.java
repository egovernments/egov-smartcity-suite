/*
 * Created on Jan 17, 2006
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package org.egov.dao.budget;

/**
 * @author Administrator
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
import java.util.List; 

import org.egov.commons.CChartOfAccounts;
import org.egov.infstr.ValidationException;
import org.egov.infstr.dao.*;
import org.egov.model.budget.BudgetGroup;

public interface BudgetGroupDAO extends GenericDAO{
	public List<BudgetGroup> getBudgetGroupList()throws ValidationException;
	public List<BudgetGroup> getBudgetHeadByDateAndFunction(String functionCode,java.util.Date date) throws ValidationException;
	public BudgetGroup getBudgetHeadById(Long id) throws ValidationException;
	public List<BudgetGroup> getBudgetHeadByFunction(String string);
	public List<BudgetGroup> getBudgetHeadByCOAandFunction(String functionCode,List<CChartOfAccounts> chartOfAccountsList) throws ValidationException;
} 
