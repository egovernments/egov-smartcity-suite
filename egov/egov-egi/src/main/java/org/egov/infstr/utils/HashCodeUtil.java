/**
 * eGov suite of products aim to improve the internal efficiency,transparency, 
   accountability and the service delivery of the government  organizations.

    Copyright (C) <2015>  eGovernments Foundation

    The updated version of eGov suite of products as by eGovernments Foundation 
    is available at http://www.egovernments.org

    This program is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    any later version.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with this program. If not, see http://www.gnu.org/licenses/ or 
    http://www.gnu.org/licenses/gpl.html .

    In addition to the terms of the GPL license to be adhered to in using this
    program, the following additional terms are to be complied with:

	1) All versions of this program, verbatim or modified must carry this 
	   Legal Notice.

	2) Any misrepresentation of the origin of the material is prohibited. It 
	   is required that all modified versions of this material be marked in 
	   reasonable ways as different from the original version.

	3) This license does not grant any rights to any user of the program 
	   with regards to rights under trademark law for use of the trade names 
	   or trademarks of eGovernments Foundation.

  In case of any queries, you can reach eGovernments Foundation at contact@egovernments.org.
 */

/*
 * HashCodeUtil.java Created on Nov 8, 2005
 *
 * Copyright 2005 eGovernments Foundation. All rights reserved.
 * EGOVERNMENTS PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package org.egov.infstr.utils;

/**
 * TODO Brief Description of the puprpose of the class/interface
 * 
 * @author Administrator
 * @version 1.00 
 * @see	    
 * @see	    
 * @since   1.00
 */
import java.lang.reflect.Array;

import org.apache.log4j.Logger;

/**
* Collected methods which allow easy implementation of <code>hashCode</code>.
* http://www.javapractices.com/Topic28.cjp. Based on the recommendations of Effective Java, by Joshua Bloch. 
*
* Example use case:
* <pre>
*  public int hashCode(){
*    int result = HashCodeUtil.SEED;
*    //collect the contributions of various fields
*    result = HashCodeUtil.hash(result, fPrimitive);
*    result = HashCodeUtil.hash(result, fObject);
*    result = HashCodeUtil.hash(result, fArray);
*    return result;
*  }
* </pre>
*/
public final class HashCodeUtil {

  /**
  * An initial value for a <code>hashCode</code>, to which is added contributions
  * from fields. Using a non-zero value decreases collisons of <code>hashCode</code>
  * values.
  */
  public static final int SEED = 23;
  private static Logger logger = Logger.getLogger(HashCodeUtil.class);

  /**
  * booleans.
  */
  public static int hash( int aSeed, boolean aBoolean ) {
    logger.debug("boolean...");
    return firstTerm( aSeed ) + ( aBoolean ? 1 : 0 );
  }

  /**
  * chars.
  */
  public static int hash( int aSeed, char aChar ) {
    logger.debug("char...");
    return firstTerm( aSeed ) + (int)aChar;
  }

  /**
  * ints.
  */
  public static int hash( int aSeed , int aInt ) {
    /*
    * Implementation Note
    * Note that byte and short are handled by this method, through
    * implicit conversion.
    */
    logger.debug("int...");
    return firstTerm( aSeed ) + aInt;
  }

  /**
  * longs.
  */
  public static int hash( int aSeed , long aLong ) {
    logger.debug("long...");
    return firstTerm(aSeed)  + (int)( aLong ^ (aLong >>> 32) );
  }

  /**
  * floats.
  */
  public static int hash( int aSeed , float aFloat ) {
    return hash( aSeed, Float.floatToIntBits(aFloat) );
  }

  /**
  * doubles.
  */
  public static int hash( int aSeed , double aDouble ) {
    return hash( aSeed, Double.doubleToLongBits(aDouble) );
  }

  /**
  * <code>aObject</code> is a possibly-null object field, and possibly an array.
  *
  * If <code>aObject</code> is an array, then each element may be a primitive
  * or a possibly-null object.
  */
  public static int hash( int aSeed , Object aObject ) {
    int result = aSeed;
    if ( aObject == null) {
      result = hash(result, 0);
    }
    else if ( ! isArray(aObject) ) {
      result = hash(result, aObject.hashCode());
    }
    else {
      int length = Array.getLength(aObject);
      for ( int idx = 0; idx < length; ++idx ) {
        Object item = Array.get(aObject, idx);
        //recursive call!
        result = hash(result, item);
      }
    }
    return result;
  }


  /// PRIVATE ///
  private static final int fODD_PRIME_NUMBER = 37;

  private static int firstTerm( int aSeed ){
    return fODD_PRIME_NUMBER * aSeed;
  }

  private static boolean isArray(Object aObject){
    return aObject.getClass().isArray();
  }
} 