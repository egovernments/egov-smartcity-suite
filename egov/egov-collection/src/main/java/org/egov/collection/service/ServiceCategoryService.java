package org.egov.collection.service;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import javax.validation.ConstraintViolation;
import javax.validation.Path.Node;

import org.egov.collection.repository.ServiceCategoryRepository;
import org.egov.infstr.ValidationError;
import org.egov.infstr.ValidationException;
import org.egov.infstr.models.ServiceCategory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;

@Service
@Transactional(readOnly = true)
public class ServiceCategoryService{
	private final ServiceCategoryRepository serviceCategoryRepository;
	@Autowired
    @Qualifier("entityValidator") 
    private LocalValidatorFactoryBean entityValidator;
    
	  
    @Autowired
    public ServiceCategoryService(final ServiceCategoryRepository serviceCategoryRepository) {

        this.serviceCategoryRepository = serviceCategoryRepository;
    }
    
    @Transactional
    public void create(final ServiceCategory serviceCategory) {
        serviceCategoryRepository.save(serviceCategory);
    }

    @Transactional
    public void update(final ServiceCategory serviceCategory) {
        serviceCategoryRepository.save(serviceCategory);
    }
    
    public List<ServiceCategory> getAllServiceCategoriesOrderByCode(){
		return serviceCategoryRepository.findAllByOrderByCodeAsc();
	}
    
    public ServiceCategory findByCode(String code) {
    	return serviceCategoryRepository.findByCode(code);
    }
    
    public List<ServiceCategory> getAllActiveServiceCategories() {
    	return serviceCategoryRepository.findAllActiveServiceCategories();    	
    }
    
    public void validate(final ServiceCategory model) {
        final List<ValidationError> errors = this.validateModel(model);
        if (!errors.isEmpty())
            throw new ValidationException(errors);
    }

    public List<ValidationError> validateModel(final ServiceCategory model) {
        final List<ValidationError> errors = new ArrayList<ValidationError>();
        if (model == null) {
            errors.add(new ValidationError("", "model.null"));
            return errors;
        }
        final Set<ConstraintViolation<ServiceCategory>> constraintViolations = entityValidator.validate(model);
        for (final ConstraintViolation<ServiceCategory> constraintViolation : constraintViolations) {
            final Iterator<Node> nodes = constraintViolation.getPropertyPath().iterator();
            while (nodes.hasNext())
                errors.add(new ValidationError(nodes.next().getName(), constraintViolation.getMessage()));
        }
       /* if (model instanceof BaseModel) {
            final BaseModel basemodel = (BaseModel) model;
            final List<ValidationError> dependentValMessages = basemodel.validate();
            if (dependentValMessages != null)
                errors.addAll(dependentValMessages);
        }*/
        return errors;
    }
    
    
}
