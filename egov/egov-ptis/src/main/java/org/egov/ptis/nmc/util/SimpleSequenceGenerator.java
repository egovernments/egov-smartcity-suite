package org.egov.ptis.nmc.util;

/**
 * Gives the sequence with prefix runningNumber postfix
 *
 * @author nayeem
 *
 */
public class SimpleSequenceGenerator {

	private static final String STR_SEPARATOR = "_";

	String prefix;
	Integer runningNumber;
	String postfix;

	public SimpleSequenceGenerator() {

	}

    public SimpleSequenceGenerator(String prefix, Integer runningNumber, String postfix) {
    	this.prefix = prefix;
    	this.runningNumber = runningNumber;
    	this.postfix = postfix;
	}

	public String getPrefix() {
		return prefix;
	}

	public void setPrefix(String prefix) {
		this.prefix = prefix;
	}

	public Integer getRunningNumber() {
		return runningNumber;
	}

	public void setRunningNumber(Integer runningNumber) {
		this.runningNumber = runningNumber;
	}

	public String getPostfix() {
		return postfix;
	}

	public void setPostfix(String postfix) {
		this.postfix = postfix;
	}

	public String generateSequence() {
		return new StringBuilder().append(getPrefix()).append(STR_SEPARATOR).append(getRunningNumber())
				.append(STR_SEPARATOR).append(getPostfix()).toString();
	}
}
