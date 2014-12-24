package org.egov.tender.model;

import java.util.Collections;
import java.util.Set;

import org.egov.infstr.models.StateAware;
import org.egov.tender.interfaces.TenderFile;
import org.egov.tender.interfaces.Tenderable;
import org.egov.tender.interfaces.TenderableGroup;

/**
 * 
 * An Abstract TenderFile that implementations can extend.
 * 
 */
public abstract class TenderFileImpl  extends StateAware implements TenderFile{

	public  Set<TenderableGroup> tenderGroups(){
		return Collections.EMPTY_SET;
	}
	public  Set<Tenderable> tenderEntities(){
		return Collections.EMPTY_SET;
	}
}
