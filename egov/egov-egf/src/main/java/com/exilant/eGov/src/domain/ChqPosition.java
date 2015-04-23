 /*
   * ChqPosition.java Created on July 17, 2007
   *
   * Copyright 2005 eGovernments Foundation. All rights reserved.
   * EGOVERNMENTS PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */


package com.exilant.eGov.src.domain;
/**
 * 
 * @author Mani
 * This Class represents one row of the chequeformat 
 * for example Date field in the ChequePrinting will have
 * 1.Length 2.X value 3.Y value
 *
 */
public class ChqPosition
{
	public float x,y,l=6;
	/**
	 * @return the l
	 */

	/**
	 * @return the l
	 */
	public float getL() {
		return l;
	}

	/**
	 * @param l the l to set
	 */
	public void setL(float l) {
		this.l = l;
	}

	/**
	 * @return the x
	 */
	public float getX() {
		return x;
	}

	/**
	 * @param x the x to set
	 */
	public void setX(float x) {
		this.x = x;
	}

	/**
	 * @return the y
	 */
	public float getY() {
		return y;
	}

	/**
	 * @param y the y to set
	 */
	public void setY(float y) {
		this.y = y;
	}

}