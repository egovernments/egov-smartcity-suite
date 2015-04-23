/**
 * TempPostRecords3.java

 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package org.egov.eb.webservice.tneb.corp;

public class TempPostRecords3  implements java.io.Serializable {
    private java.lang.String cuscode;

    private java.math.BigInteger amount;

    private java.lang.String duedate;

    private java.lang.String message;

    public TempPostRecords3() {
    }

    public TempPostRecords3(
           java.lang.String cuscode,
           java.math.BigInteger amount,
           java.lang.String duedate,
           java.lang.String message) {
           this.cuscode = cuscode;
           this.amount = amount;
           this.duedate = duedate;
           this.message = message;
    }


    /**
     * Gets the cuscode value for this TempPostRecords3.
     * 
     * @return cuscode
     */
    public java.lang.String getCuscode() {
        return cuscode;
    }


    /**
     * Sets the cuscode value for this TempPostRecords3.
     * 
     * @param cuscode
     */
    public void setCuscode(java.lang.String cuscode) {
        this.cuscode = cuscode;
    }


    /**
     * Gets the amount value for this TempPostRecords3.
     * 
     * @return amount
     */
    public java.math.BigInteger getAmount() {
        return amount;
    }


    /**
     * Sets the amount value for this TempPostRecords3.
     * 
     * @param amount
     */
    public void setAmount(java.math.BigInteger amount) {
        this.amount = amount;
    }


    /**
     * Gets the duedate value for this TempPostRecords3.
     * 
     * @return duedate
     */
    public java.lang.String getDuedate() {
        return duedate;
    }


    /**
     * Sets the duedate value for this TempPostRecords3.
     * 
     * @param duedate
     */
    public void setDuedate(java.lang.String duedate) {
        this.duedate = duedate;
    }


    /**
     * Gets the message value for this TempPostRecords3.
     * 
     * @return message
     */
    public java.lang.String getMessage() {
        return message;
    }


    /**
     * Sets the message value for this TempPostRecords3.
     * 
     * @param message
     */
    public void setMessage(java.lang.String message) {
        this.message = message;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof TempPostRecords3)) return false;
        TempPostRecords3 other = (TempPostRecords3) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.cuscode==null && other.getCuscode()==null) || 
             (this.cuscode!=null &&
              this.cuscode.equals(other.getCuscode()))) &&
            ((this.amount==null && other.getAmount()==null) || 
             (this.amount!=null &&
              this.amount.equals(other.getAmount()))) &&
            ((this.duedate==null && other.getDuedate()==null) || 
             (this.duedate!=null &&
              this.duedate.equals(other.getDuedate()))) &&
            ((this.message==null && other.getMessage()==null) || 
             (this.message!=null &&
              this.message.equals(other.getMessage())));
        __equalsCalc = null;
        return _equals;
    }

    private boolean __hashCodeCalc = false;
    public synchronized int hashCode() {
        if (__hashCodeCalc) {
            return 0;
        }
        __hashCodeCalc = true;
        int _hashCode = 1;
        if (getCuscode() != null) {
            _hashCode += getCuscode().hashCode();
        }
        if (getAmount() != null) {
            _hashCode += getAmount().hashCode();
        }
        if (getDuedate() != null) {
            _hashCode += getDuedate().hashCode();
        }
        if (getMessage() != null) {
            _hashCode += getMessage().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
  /*  private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(TempPostRecords3.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://corp.tneb.com/", "tempPostRecords3"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("cuscode");
        elemField.setXmlName(new javax.xml.namespace.QName("", "cuscode"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("amount");
        elemField.setXmlName(new javax.xml.namespace.QName("", "amount"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "integer"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("duedate");
        elemField.setXmlName(new javax.xml.namespace.QName("", "duedate"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("message");
        elemField.setXmlName(new javax.xml.namespace.QName("", "message"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
    }
*/
    /**
     * Return type metadata object
     */
    /*public static org.apache.axis.description.TypeDesc getTypeDesc() {
        return typeDesc;
    }
*/
    /**
     * Get Custom Serializer
     */
    /*public static org.apache.axis.encoding.Serializer getSerializer(
           java.lang.String mechType, 
           java.lang.Class _javaType,  
           javax.xml.namespace.QName _xmlType) {
        return 
          new  org.apache.axis.encoding.ser.BeanSerializer(
            _javaType, _xmlType, typeDesc);
    }

    *//**
     * Get Custom Deserializer
     *//*
    public static org.apache.axis.encoding.Deserializer getDeserializer(
           java.lang.String mechType, 
           java.lang.Class _javaType,  
           javax.xml.namespace.QName _xmlType) {
        return 
          new  org.apache.axis.encoding.ser.BeanDeserializer(
            _javaType, _xmlType, typeDesc);
    }
*/
	@Override
	public String toString() {
		return "TempPostRecords3 [cuscode=" + cuscode + ", amount=" + amount
				+ ", duedate=" + duedate + ", message=" + message + "]";
	}

}
