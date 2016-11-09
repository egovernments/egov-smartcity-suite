package org.egov.council.autonumber;


import org.egov.council.entity.CouncilPreamble;
import org.springframework.stereotype.Service;

@Service
public interface PreambleNumberGenerator {

	public String getNextNumber(CouncilPreamble councilpreamble);

}
