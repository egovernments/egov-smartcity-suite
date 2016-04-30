package org.egov.ptis.web.controller.masters.usage;

import org.egov.ptis.domain.dao.property.PropertyUsageDAO;
import org.egov.ptis.domain.entity.property.PropertyUsage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Controller to list out all the existing Usage Masters
 * 
 * @author nayeem
 *
 */

@RestController
@RequestMapping(value = "/usage/list")
public class ViewUsageController {

    private final PropertyUsageDAO propertyUsageHibernateDAO;

    @Autowired
    public ViewUsageController(final PropertyUsageDAO propertyUsageHibernateDAO) {
        this.propertyUsageHibernateDAO = propertyUsageHibernateDAO;
    }

    @RequestMapping(method = RequestMethod.GET)
    public Map<String, List<PropertyUsage>> viewUsages() {
        final Map<String, List<PropertyUsage>> result = new HashMap<String, List<PropertyUsage>>();
        result.put("data", propertyUsageHibernateDAO.getAllActivePropertyUsage());
        return result;
    }
}
