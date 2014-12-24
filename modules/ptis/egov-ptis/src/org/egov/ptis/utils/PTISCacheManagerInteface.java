package org.egov.ptis.utils;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.egov.lib.address.model.Address;
import org.egov.lib.admbndry.Boundary;
import org.egov.ptis.domain.entity.property.BasicProperty;
import org.egov.ptis.domain.entity.property.ConstructionTypeImpl;
import org.egov.ptis.domain.entity.property.PropertyCreationReason;
import org.egov.ptis.domain.entity.property.PropertyOccupation;
import org.egov.ptis.domain.entity.property.PropertySource;
import org.egov.ptis.domain.entity.property.PropertyStatus;
import org.egov.ptis.domain.entity.property.PropertyUsage;
import org.egov.ptis.domain.entity.property.StructureClassification;


/**
 *
 *  @author deepak YN
 *
 */


public interface PTISCacheManagerInteface
{
	public java.util.List getPropertyCreationReasons();
	public List getAllStructuralFactors();
	public java.util.List getAllConstructionTypes();
	public java.util.List getAllPropUsage();
	public java.util.List getAllPropOccTypes();
	public java.util.List getAllPropertyStatus();
	public java.util.List getAllPropertySource();
	public PropertySource getPropertySourceById(Integer propSrcId);
	public PropertyStatus getPropertyStatusById(Integer propStatusId);
	public PropertyUsage getPropertyUsageById(Integer propUsageId);
	public PropertyOccupation getPropertyOccupationById(Integer propOccId);
	public StructureClassification getStructureClassificationById(Integer strucClsfnId);
	public ConstructionTypeImpl getConstructionTypeById(Integer ConstTypeId);
	public PropertyCreationReason getReasonById(Integer reasonId);
	public Boundary getBoundary(Integer bndryId);
	public java.util.List getAllTaxRates();
	public java.util.List getAllDepreciationRates();
	public List getAllCategories();
	public String buildAddress(BasicProperty basicProperty);
	public String buildOwnerFullName(Set ownerSet);
    public Map getTxPercWithUsg(List lstTaxRates);
    public String  buildOwnerFullName(BasicProperty basicProp);
    public String buildAddressFromAddress(Address add);
    public String buildAddressByImplemetation(Address address);


}
