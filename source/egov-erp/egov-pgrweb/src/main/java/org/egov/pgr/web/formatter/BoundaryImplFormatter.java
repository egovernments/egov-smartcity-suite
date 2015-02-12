package org.egov.pgr.web.formatter;

import java.text.ParseException;
import java.util.Locale;

import org.egov.lib.admbndry.BoundaryDAO;
import org.egov.lib.admbndry.BoundaryImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.Formatter;
import org.springframework.stereotype.Component;

@Component
public class BoundaryImplFormatter implements Formatter<BoundaryImpl> {

    private final BoundaryDAO boundaryDAO;

    @Autowired
    public BoundaryImplFormatter(final BoundaryDAO boundaryDAO) {
        this.boundaryDAO = boundaryDAO;
    }

    @Override
    public BoundaryImpl parse(final String location, final Locale locale) throws ParseException {
    	return boundaryDAO.load(Integer.valueOf(location));
    }

    @Override
    public String print(final BoundaryImpl status, final Locale locale) {
        return status.getName();
    }

}
