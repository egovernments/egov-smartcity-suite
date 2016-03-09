/**
 * 
 */
package org.egov.infstr.models;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.egov.commons.Accountdetailtype;
import org.egov.commons.utils.EntityType;
import org.egov.infra.validation.exception.ValidationError;
import org.egov.infra.validation.exception.ValidationException;
import org.egov.infstr.services.PersistenceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;

/**
 * @author manoranjan
 *
 */
public class ServiceSubledgerInfo {

	private Long id;
	
	private Accountdetailtype detailType;
	
	private Integer detailKeyId;
	
	private BigDecimal amount = BigDecimal.ZERO;
	
	private ServiceAccountDetails serviceAccountDetail;
	
	private EntityType entity;
	
	@Autowired
	private ApplicationContext context;

	/**
	 * @return the detailType
	 */
	public Accountdetailtype getDetailType() {
		return detailType;
	}

	/**
	 * @param detailType the detailType to set
	 */
	public void setDetailType(Accountdetailtype detailType) {
		this.detailType = detailType;
	}

	/**
	 * @return the detailKey
	 */
	public Integer getDetailKeyId() {
		return detailKeyId;
	}

	/**
	 * @param detailKey the detailKey to set
	 */
	public void setDetailKeyId(Integer detailKeyId) {
		this.detailKeyId = detailKeyId;
	}

	/**
	 * @return the amount
	 */
	public BigDecimal getAmount() {
		return null != this.amount ?this.amount.setScale(2, BigDecimal.ROUND_HALF_EVEN):null;
	}

	/**
	 * @param amount the amount to set
	 */
	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}

	/**
	 * @return the serviceAccountDetail
	 */
	public ServiceAccountDetails getServiceAccountDetail() {
		return serviceAccountDetail;
	}

	/**
	 * @param serviceAccountDetail the serviceAccountDetail to set
	 */
	public void setServiceAccountDetail(ServiceAccountDetails serviceAccountDetail) {
		this.serviceAccountDetail = serviceAccountDetail;
	}

	/**
	 * @return the id
	 */
	public Long getId() {
		return id;
	}

	/**
	 * @param id the id to set
	 */
	public void setId(Long id) {
		this.id = id;
	}

	public String getDetailCode() {
		entity = getEntityInfo();
		return null != this.entity ? entity.getCode() : null;
	}
	public String getDetailKey() {
		
		return null != this.entity ? entity.getName() : null;
	}

	
	@SuppressWarnings("unchecked")
	private EntityType getEntityInfo() throws ValidationException
	{	
		EntityType entity = null;
		if(null == this.getDetailType() || null == this.detailType.getId() || this.detailType.getId().equals(0) ||this.detailType.getId().equals(-1)
			|| null == this.detailKeyId || this.detailKeyId.equals(0) || this.detailKeyId.equals(-1)){
			
			return null;
		}
		try {
				Class<?> service = Class.forName(this.detailType.getFullQualifiedName());
				// getting the entity type service.
				String detailTypeName = service.getSimpleName();
				String detailTypeService =  detailTypeName.substring(0,1).toLowerCase()+detailTypeName.substring(1)+"Service";
				PersistenceService entityPersistenceService=(PersistenceService)context.getBean(detailTypeService);
				String dataType = "";
				// required to know data type of the id of the detail  type object.
				java.lang.reflect.Method method = service.getMethod("getId");
				dataType = method.getReturnType().getSimpleName();
				if ( dataType.equals("Long") ){
					entity=(EntityType)entityPersistenceService.findById(Long.valueOf(this.detailKeyId.toString()),false);
				}else{
					entity=(EntityType)entityPersistenceService.findById(detailKeyId,false);
				}
		} catch (Exception e) {
			 List<ValidationError> errors=new ArrayList<ValidationError>();
			 errors.add(new ValidationError("exp",e.getMessage()));
			 throw new ValidationException(errors);
		}
		
		return entity;
		
	}
}
