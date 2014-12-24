package org.egov.tender.interfaces;

import java.util.Date;
import java.util.Set;

import org.egov.commons.EgwStatus;
import org.egov.lib.rjbac.dept.Department;
/**
 * This class represents the file that will be used to create Tender Notice. Each tender file
 * will have one Tender Notice associated with it.
 * A TenderFile can either have groups of tenderable entities or a set of tenderable entities.
 * NOTE: If the TenderFile contains tenderGroups then tenderEntities will be ignored even if present.
 */
public interface TenderFile {
	
	
	public Long getId();
	
	/**
	 * Returns the set of TenderableGroups that needs to be tendered out.
	 * TenderableGroup is equivalent to a unit detail in the TenderNotice.
	 * Eg: Estimate/Indent Rate Contract in works and indent in stores . 
	 * Estimate, indent, etc should implement TenderableGroup interface. 
	 * Each TenderableGroup group contain set of Tenderable entities. 
	 * @return Set of TenderableGroup
	 */
	public  Set<TenderableGroup> getTenderGroups();
	
	/**
	 * Returns the set of Tenderable entities that need to be tendered out. This is only in
	 * cases where entities are not grouped together for a tender.
	 * Eg: Shop in landestate module. 
	 * Tender Group will be blank in land estate module. Set of shops will be returned as entities from land estate module.
	 * @return Set of Tenderable 
	 */
	public  Set<Tenderable> getTenderEntities();
	
	/**
	 * Date of Tender 
	 * @return Date
	 */
	public  Date getTenderDate();
	
	/**
	 * Tender file number generate by individual module.
	 * @return String
	 */
	public String getFileNumber();
	/**
	 * Description about tender. This Description will be showed in tender notice.
	 * @return String
	 */
	public String getDescription();
	/**
	 * Department Interface.
	 * @return Department
	 */
	public Department getDepartment();
	/**
	 * Tender file status.
	 * @return status object
	 */
	public EgwStatus getStatus();
	
	/**
	 * If TRUE, TenderableGroup are combined into a single unit in tender Notice.
	 * If this field is FALSE, a separate Unit will be created for each tenderable group.  
	 */
	public Boolean combineTenderableGroups();
}
