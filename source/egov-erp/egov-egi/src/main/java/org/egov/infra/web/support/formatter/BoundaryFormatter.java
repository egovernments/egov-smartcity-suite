package org.egov.infra.web.support.formatter;

import java.text.ParseException;
import java.util.Locale;

import org.egov.lib.admbndry.BoundaryImpl;
import org.egov.lib.admbndry.ejb.api.BoundaryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.Formatter;
import org.springframework.stereotype.Component;

@Component
public class BoundaryFormatter implements Formatter<BoundaryImpl> {

    private final BoundaryService boundaryService;

    @Autowired
    public BoundaryFormatter(final BoundaryService boundaryService) {
        this.boundaryService = boundaryService;
    }

    @Override
    public BoundaryImpl parse(final String boundaryId, final Locale locale) throws ParseException {
        return boundaryId.isEmpty() ? null : (BoundaryImpl) boundaryService.getBoundary(Integer.valueOf(boundaryId));
    }

    @Override
    public String print(final BoundaryImpl boundary, final Locale locale) {
        return boundary.getName();
    }

}
