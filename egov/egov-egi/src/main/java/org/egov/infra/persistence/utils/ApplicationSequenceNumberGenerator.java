package org.egov.infra.persistence.utils;

import java.io.Serializable;
import java.sql.SQLException;

import org.egov.infra.exception.ApplicationRuntimeException;
import org.hibernate.exception.SQLGrammarException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
@Service
public class ApplicationSequenceNumberGenerator {

	public static String SEQUENCE_NAME_PREFIX = "SQ_";
	public static String WORD_SEPARATOR_FOR_NAME = "_";
	private static final String DISALLOWED_CHARACTERS = "[\\/ -]";

	@Autowired
	private DBSequenceGenerator dbSequenceGenerator;

	@Autowired
	private SequenceNumberGenerator sequenceNumberGenerator;
	
	public Serializable getNextSequence(String sequenceName)
	{
		sequenceName=replaceBadChars(sequenceName);

		Serializable sequenceNumber;
		try{
			try {
				sequenceNumber = sequenceNumberGenerator.getNextSequence(sequenceName);
			} catch (final SQLGrammarException e) {
				sequenceNumber = dbSequenceGenerator.createAndGetNextSequence(sequenceName);
			}
		}catch (final SQLException e) {
			throw new ApplicationRuntimeException("Error occurred while generating Application Number", e);
		}

		return sequenceNumber;  
	}

	public static String replaceBadChars(String sequenceNameWithBadChars) {
		return sequenceNameWithBadChars.replaceAll(
				DISALLOWED_CHARACTERS, WORD_SEPARATOR_FOR_NAME);
	}
}
