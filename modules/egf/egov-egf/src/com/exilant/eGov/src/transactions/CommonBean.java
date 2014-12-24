/*
 * Created on Aug 28, 2005
 * @author chiranjeevi
 */
package com.exilant.eGov.src.transactions;

import org.apache.log4j.Logger;

public class CommonBean 
{
	private String billType;
	private String contraType;
	private String journalType;
	private String showMode;
	private String paymentType;
	
	private String inputField1;
	private String inputField2;
	private String inputFieldC;
	private String inputField4;
	private String inputField5;
	private String inputField6;
	private String inputField7;
	private String inputField8;
	private String inputField9; 
	private String inputField10;
	private String inputField11;
	private String inputField12;
		
	private String field1;
	private String field2;
	private String field3;
	private String field4;
	private String field5;
	private String field6;
	private String field7;
	private String field8;
	private String field9; 
	private String field10;
	private String field11;
	private String field12;
	private String field13;
	private String field14;
	private String field15;
	private static final Logger LOGGER=Logger.getLogger(CommonBean.class);
 	public CommonBean()
    {//LOGGER.info("inside commonbean");  
 		
    }
 	 
 	//input fields
 	/**
	 * @return Returns the billType.
	 */
 	public String getBillType()
    {	LOGGER.info("inside billType:"+billType);
        return billType;
    }
 	/**
	 * @param billType The billType.
	 */
    public void setBillType(String billTypeTT)
    {LOGGER.info("inside billType:"+billTypeTT);
        this.billType = billTypeTT;
    }
 	
 	/**
	 * @return Returns the journalType.
	 */
 	public String getJournalType()
    {	LOGGER.info("inside get journalType:"+journalType);
        return journalType;
    }
 	/**
	 * @param contraType The contraType.
	 */
    public void setJournalType(String journalType)
    {LOGGER.info("inside set journalType:"+journalType);
        this.journalType = journalType;
    }
    
    /**
	 * @return Returns the contraType.
	 */
 	public String getContraType()
    {	LOGGER.info("inside get contraType:"+contraType);
        return contraType;
    }
 	/**
	 * @param contraType The contraType.
	 */
    public void setContraType(String contraTypeTT)
    {LOGGER.info("inside set contraType:"+contraType);
        this.contraType = contraTypeTT;
    }
    
    /**
	 * @return Returns the showMode.
	 */
 	public String getShowMode()
    {	LOGGER.info("inside get showMode:"+showMode);
        return showMode;
    }
 	/**
	 * @param showMode The showMode.
	 */
    public void setShowMode(String showMode)
    {LOGGER.info("inside set showMode:"+showMode);
        this.showMode = showMode;
    }
    /**
	 * @return Returns the paymentType.
	 */
 	public String getPaymentType()
    {	LOGGER.info("inside get paymentType:"+paymentType);
        return paymentType;
    }
 	/**
	 * @param paymentType The paymentType.
	 */
    public void setPaymentType(String paymentType)
    {LOGGER.info("inside set paymentType:"+paymentType);
        this.paymentType = paymentType;
    }
    
    
 	/**
	 * @return Returns the inputField1.
	 */
 	public String getInputField1()
    {	LOGGER.info("inside get inputField1:"+inputField1);
        return inputField1;
    }
 	/**
	 * @param inputField1 The inputField1.
	 */
    public void setInputField1(String inputField1t)
    {LOGGER.info("inside inputField1:"+inputField1t);
        this.inputField1 = inputField1t;
    }
    /**
	 * @return Returns the inputField.
	 */
 	public String getInputField2()
    {LOGGER.info("inside get inputField2:"+inputField2); 
        return inputField2;
    }
 	/**
	 * @param inputField2 The inputField2t.
	 */
    public void setInputField2(String inputField2t)
    {LOGGER.info("inside set inputField2t:"+inputField2t);
        this.inputField2 = inputField2t;
    }
    /**
	 * @return Returns the inputField3t.
	 */
 	public String getInputFieldC()
    {
        return inputFieldC;
    }
 	/**
	 * @param inputField3 The inputField3.
	 */
    public void setInputFieldC(String inputField3t)
    {	LOGGER.info("inside set field 3:"+inputField3t);
        this.inputFieldC = inputField3t;
    }
    /**
	 * @return Returns the inputField4.
	 */
 	public String getInputField4()
    {
        return inputField4;
    }
 	/**
	 * @param inputField4 The inputField4.
	 */
    public void setInputField4(String inputField4t)
    {
        this.inputField4 = inputField4t;
    }
    /**
	 * @return Returns the inputField.
	 */
 	public String getInputField5()
    {
        return inputField5;
    }
 	/**
	 * @param inputField5 The inputField5.
	 */
    public void setInputField5(String inputField5t)
    {
        this.inputField5 = inputField5t;
    }
    /**
	 * @return Returns the inputField.
	 */
 	public String getInputField6()
    {LOGGER.info("inside get inputField6t:"+inputField6);
        return inputField6;
    }
 	/**
	 * @param inputField6 The inputField6.
	 */
    public void setInputField6(String inputField6t)
    {LOGGER.info("inside set inputField6t:"+inputField6t);  
        this.inputField6 = inputField6t;
    }
    /**
	 * @return Returns the inputField.
	 */
 	public String getInputField7()
    {
        return inputField7;
    }
 	/**
	 * @param inputField7 The inputField7.
	 */
    public void setInputField7(String inputField7t)
    {
        this.inputField7 = inputField7t;
    }
    /**
	 * @return Returns the inputField.
	 */
 	public String getInputField8()
    {
        return inputField8;
    }
 	/**
	 * @param inputField8 The inputField8.
	 */
    public void setInputField8(String inputField8t)
    {	LOGGER.info("inside set inputField8:"+inputField8t);  
        this.inputField8 = inputField8t;
    }
    /**
	 * @return Returns the inputField9.
	 */
 	public String getInputField9()
    {
        return inputField9;
    }
 	/**
	 * @param inputField9 The inputField9.
	 */
    public void setInputField9(String inputField9t)
    {
        this.inputField9 = inputField9t;
    }
    /**
	 * @return Returns the inputField10.
	 */
 	public String getInputField10()
    {
        return inputField10;
    }
 	/**
	 * @param inputField10 The inputField10.
	 */
    public void setInputField10(String inputField10t) 
    {
        this.inputField10 = inputField10t;
    }
    /**
	 * @return Returns the inputField11.
	 */
 	public String getInputField11()
    {
        return inputField11;
    }
 	/**
	 * @param inputField11 The inputField11.
	 */
    public void setInputField11(String inputField11) 
    {
        this.inputField11 = inputField11;
    }
    /**
	 * @return Returns the inputField12.
	 */
 	public String getInputField12()
    {
        return inputField12;
    }
 	/**
	 * @param inputField12 The inputField12.
	 */
    public void setInputField12(String inputField12) 
    {
        this.inputField12 = inputField12;
    }
 	
 	//fields
 	/**
	 * @return Returns the field1.
	 */
 	public String getField1()
    {
        return field1;
    }
 	/**
	 * @param field1 The field1.
	 */
    public void setField1(String field1)
    {
        this.field1 = field1;
    }
    /**
	 * @return Returns the field.
	 */
 	public String getField2()
    {
        return field2;
    }
 	/**
	 * @param field2 The field2.
	 */
    public void setField2(String field2)
    {
        this.field2 = field2;
    }
    /**
	 * @return Returns the field3.
	 */
 	public String getField3()
    {
        return field3;
    }
 	/**
	 * @param field3 The field3.
	 */
    public void setField3(String field3)
    {
        this.field3 = field3;
    }
    /**
	 * @return Returns the field4.
	 */
 	public String getField4()
    {
        return field4;
    }
 	/**
	 * @param field4 The field4.
	 */
    public void setField4(String field4)
    {
        this.field4 = field4;
    }
    /**
	 * @return Returns the field.
	 */
 	public String getField5()
    {
        return field5;
    }
 	/**
	 * @param field5 The field5.
	 */
    public void setField5(String field5)
    {
        this.field5 = field5;
    }
    /**
	 * @return Returns the field.
	 */
 	public String getField6()
    {
        return field6;
    }
 	/**
	 * @param field6 The field6.
	 */
    public void setField6(String field6)
    {
        this.field6 = field6;
    }
    /**
	 * @return Returns the field.
	 */
 	public String getField7()
    {
        return field7;
    }
 	/**
	 * @param field7 The field7.
	 */
    public void setField7(String field7)
    {
        this.field7 = field7;
    }
    /**
	 * @return Returns the field.
	 */
 	public String getField8()
    {
        return field8;
    }
 	/**
	 * @param field8 The field8.
	 */
    public void setField8(String field8)
    {
        this.field8 = field8;
    }
    /**
	 * @return Returns the field9.
	 */
 	public String getField9()
    {
        return field9;
    }
 	/**
	 * @param field9 The field9.
	 */
    public void setField9(String field9)
    {
        this.field9 = field9;
    }
    /**
	 * @return Returns the field10.
	 */
 	public String getField10()
    {
        return field10;
    }
 	/**
	 * @param field10 The field10.
	 */
    public void setField10(String field10)
    {
        this.field10 = field10;
    }
    /**
	 * @return Returns the field11.
	 */
 	public String getField11()
    {
        return field11;
    }
 	/**
	 * @param field11 The field11.
	 */
    public void setField11(String field11)
    {
        this.field11 = field11;
    }
    /**
	 * @return Returns the field12.
	 */
 	public String getField12()
    {
        return field12;
    }
 	/**
	 * @param field12 The field12.
	 */
    public void setField12(String field12)
    {
        this.field12 = field12;
    }
    /**
	 * @return Returns the field13.
	 */
 	public String getField13()
    {
        return field13;
    }
 	/**
	 * @param field13 The field13.
	 */
    public void setField13(String field13)
    {
        this.field13 = field13;
    }
    /**
	 * @return Returns the field14.
	 */
 	public String getField14()
    {
        return field14;
    }
 	/**
	 * @param field14 The field14.
	 */
    public void setField14(String field14)
    {
        this.field14 = field14;
    }
    /**
	 * @return Returns the field15.
	 */
 	public String getField15()
    {
        return field15;
    }
 	/**
	 * @param field15 The field15.
	 */
    public void setField15(String field15)
    {
        this.field15 = field15;
    }
  
}