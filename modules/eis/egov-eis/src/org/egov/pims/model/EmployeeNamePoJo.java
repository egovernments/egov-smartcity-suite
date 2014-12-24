package org.egov.pims.model;

public class EmployeeNamePoJo implements java.io.Serializable {
	String firstName;
    String middleName;
    String lastName;

    public EmployeeNamePoJo(String firstName, String middleName, String lastName)
    {
        this.firstName = "";
        this.middleName = "";
        this.lastName = "";
        this.firstName = firstName;
        this.middleName = middleName;
        this.lastName = lastName;
    }

    public String getFirstName()
    {
        return firstName;
    }

    public void setFirstName(String firstName)
    {
        this.firstName = firstName;
    }

    public String getLastName()
    {
        return lastName;
    }

    public void setLastName(String lastName)
    {
        this.lastName = lastName;
    }

    public String getMiddleName()
    {
        return middleName;
    }

    public void setMiddleName(String middleName)
    {
        this.middleName = middleName;
    }
}
