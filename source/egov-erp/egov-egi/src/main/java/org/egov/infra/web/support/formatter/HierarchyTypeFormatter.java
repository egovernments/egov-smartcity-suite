package org.egov.infra.web.support.formatter;

import org.egov.infra.admin.master.entity.HierarchyType;
import org.egov.infra.admin.master.service.HierarchyTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.Formatter;
import org.springframework.stereotype.Component;

import java.text.ParseException;
import java.util.Locale;

@Component
public class HierarchyTypeFormatter implements Formatter<HierarchyType> {


    private HierarchyTypeService hierarchyTypeService;

    @Autowired
    public HierarchyTypeFormatter(HierarchyTypeService hierarchyTypeService) {
        this.hierarchyTypeService = hierarchyTypeService;
    }

    @Override
    public HierarchyType parse(String hierarchyName, Locale locale) throws ParseException {
        return (HierarchyType) hierarchyTypeService.getHierarchyTypeByName(hierarchyName);
    }

    @Override
    public String print(HierarchyType hierarchyType, Locale locale) {
        return hierarchyType.getName();
    }

}
