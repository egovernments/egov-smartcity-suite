package com.exilant.exility.dataservice;
public class SQLParameter {
	// name of the variable in the DataColelction that cintains value for this parameter
	public String dataSource;
	// is this parameter a must? 
	// if true: Exility generates an error if the value is not found. You should not put any braces
	//			around this parameter. (as there is no question of deleting anything.
	// if false: this field is optional. You must provide braces ({ }) around this parameter.
	//			Exility removes these braces after substituting the values if found.
	//			If value is not found, the string within braces, including the braces is removed
	public boolean isRequired;
	// if the field is not present in DC, do you want Exility to take a default value? This woudl be helpful
	// in scenarios where the field is optional, but removing text is not possible. You can have a default value.
	public String defaultValue;
	// is the parameter a list (and not a single value) example is for in operator.
	// WHERE customerCode in ['a', 'b'.....]
	// if this is 'true', exility looks for value in ValueList. If it is not found, it checks in Values.
	// In any case, one or more values found are made into a 'list'. List is a,b,c or just a.
	// exampe custoomerCode in [@1] will translate to customerCode in ['a','b','c'] or simply ['a'].
	public boolean isList = false;
	// this is applicable only for list. while making teh list, exility uss this flag to put single quote
	// around the values.
	public boolean listRequiresQuotes = false;

//Default Constructor
	public SQLParameter() {
		super();
	}

}