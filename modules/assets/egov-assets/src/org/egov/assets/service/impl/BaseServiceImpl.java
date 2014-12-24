package org.egov.assets.service.impl;

import java.io.Serializable;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import org.egov.commons.CFinancialYear;
import org.egov.commons.EgwStatus;
import org.egov.infstr.ValidationError;
import org.egov.infstr.ValidationException;
import org.egov.infstr.services.Page;
import org.egov.infstr.services.PersistenceService;
import org.egov.assets.service.BaseService;
import org.hibernate.Query;

public class BaseServiceImpl<T, ID extends Serializable> implements BaseService<T, ID>{
	
	protected PersistenceService<T, ID> persistenceService;
	protected PersistenceService 			genericService; 
	
	/**	
     * Public constructor for creating a new BaseServiceImpl.
	 * @param genericDAO
	 */
	public BaseServiceImpl(PersistenceService<T, ID> persistenceService) {
		super();
		this.persistenceService = persistenceService;
	}

	/**
     * Generic method to get an object based on class and identifier. An
     * ObjectRetrievalFailureException Runtime Exception is thrown if
     * nothing is found.
	 * 
	 * @param id
	 * @param lock
	 * @return
	 */
	public T findById(ID id, boolean lock){
		return persistenceService.findById(id,lock);
	}

	/**
     * Generic method used to get all objects of a particular type. This
     * is the same as lookup up all rows in a table.
	 * @return list of objects.
	 */
	public List<T> findAll(){
		return persistenceService.findAll();
	}

    /**
     * Generic method to search similar objects.
     * @param exampleT
     * @return
     */
    public List<T> findByExample(T exampleT){
    	return persistenceService.findByExample(exampleT);
    }

    /**
     * Generic method to save an object.
     * 
     * @param entity
     * @return the created
     */
    public T create(T entity){
    	return persistenceService.create(entity);
    }
    

    /**
     * Generic method to validate and save/update the entity
     * @param entity
     * @return
     */
    public T persist(T entity){
    	return persistenceService.persist(entity);
    }
    

    /**
     * Generic method to merge entity
     * @param model
     * @return
     */
    public T merge(T model){
    	return persistenceService.merge(model);
    }

    /**
     * Generic method to delete an object based on class and id
     * @param entity
     */
    public void delete(T entity){
    	persistenceService.delete(entity);
    }
    
    /**
     * Generic method to update an object.
     * @param entity
     * @return the updated object
     */
    public T update(T entity){
    	return persistenceService.update(entity);
    }
    
    /**
     * Find object by query and parameter.
     * @param query
     * @param params
     * @return object.
     */
    public T find(String query, Object... params){
    	return persistenceService.find(query, params);
    }
    
    /**
     * Find object by query.
     * @param query
     * @return object.
     */
    public T find(String query){
    	return persistenceService.find(query);
    }
    
    /**
     * Find list of object by query and parameter.
     * @param query
     * @param params
     * @return List of object
     */
    public List<T> findAllBy(String query, Object... params){
    	return persistenceService.findAllBy(query, params);
    }
    
    /**
     * Find list of object by query and parameter.
     * @param namedQuery
     * @param params
     * @return List of object
     */
    public List<T> findAllByNamedQuery(String namedQuery,Object... params){
    	return persistenceService.findAllByNamedQuery(namedQuery,params);
    }
    
    /**
     * find object by named query and parameter.
     * @param namedQuery
     * @param params
     * @return object.
     */
    public T findByNamedQuery(String namedQuery, Object... params){
    	return persistenceService.findByNamedQuery(namedQuery, params);
    }
    
    
    public List<T> findAll(String... orderByFields) {
    	return persistenceService.findAll(orderByFields);
    }
    
    public Page findPageBy(String query,Integer pageNumber, Integer pageSize, Object... params)  {
    	return persistenceService.findPageBy(query,pageNumber,pageSize,params);
    }
    
    /**
     * search list of object by query and offset range.
     * @param queryString
     * @param pageNumber
     * @param pageSize
     * @return list of object.
     */
//    public List<T> search(String queryString,int pageNumber,int pageSize){
//    	return persistenceService.search(queryString, pageNumber, pageSize);
//    }
	
    /**
     * Search list of objects by query.
     * @param queryString
     * @return list of objects
     */
//    public List<T> search(String queryString){
//    	return persistenceService.search(queryString);
//    }


	public void setGenericService(PersistenceService genericService) {
		this.genericService = genericService;
	}
	
	/**
	 * This method retrieves the <code>CFinancialYear</code> for the given date.
	 * 
	 * @param date an instance of <code>Date</code> for which the financial year is to 
	 * be retrieved.
	 * @return 
	 */
	public CFinancialYear getCurrentFinancialYear(Date date) {
		List<CFinancialYear> financialYear =null;
		financialYear= persistenceService.getSession().createQuery("from CFinancialYear cfinancialyear where ? between " +
				"cfinancialyear.startingDate and cfinancialyear.endingDate").setDate(0, date).list();
		
		if(financialYear == null || (financialYear!=null && financialYear.isEmpty())) {
			throw new ValidationException(Arrays.asList(new ValidationError("financialyear","financialyear.invalid")));
		}
		else {
			return (CFinancialYear)financialYear.get(0);		
		}
	}
	
	/**
	 * Return <code>EgwStatus</code> for given code from ASSET module 
	 * @param statusCode
	 *            Status code
	 * @return EgwStatus object for given module type and status code
	 */
	public EgwStatus getAssetStatusByCode(String code) {
		Query qry = genericService.getSession().createQuery(
						"from EgwStatus S where S.moduletype ='ASSET' and S.code =:code");
		qry.setString("code", code);
		return (EgwStatus) qry.uniqueResult();
	}

}
