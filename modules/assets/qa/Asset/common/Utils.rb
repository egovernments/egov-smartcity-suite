require 'win32ole'
#~ require 'thread'
require 'roo'


def start_browser(url_name)
    browser = Watir::Browser.start(url_name) 
    browser.maximize
    return browser
end


def get_input_data(tc, sheet_name,input_excel=INPUT_EXCEL)

    excel= WIN32OLE::new('excel.Application') 
    workbook=excel.Workbooks.Open("#{input_excel}")
    worksheet=workbook.Worksheets("#{sheet_name}") 
    worksheet.Select 
    #~ excel ['Visible'] 
    
    header_row = 1
    row = 1
    column = 'A'
    title_cell = column + header_row.to_s
    row = row + 1
    data_cell = column + row.to_s
  
    #find the row that has data for this test case
    while  worksheet.Range(data_cell) ['Value'] != tc
      row += 1
      data_cell = column + row.to_s
    end
      
    test_data = { } #create a new hash
   
    data_cell = column + row.to_s
    
    #Populate the hash
    while worksheet.Range(data_cell) ['Value'] != END_OF_DATA
    
      data = worksheet.Range(title_cell) ['Value'] #this is the key of the hash
      value = worksheet.Range(data_cell) ['Value'] #this is the value of the hash
      test_data[data] = value
      column = column.next
      data_cell = column + row.to_s
      title_cell = column + header_row.to_s
      
    end
    workbook.close
    return test_data
    
  ensure  
  excel.Quit
  end #def end
  
  def get_input_data_oo(tc, sheet_name,input_oo)
      oo = Openoffice.new(input_oo)
      oo.default_sheet = sheet_name
      header_row = oo.first_row  # gives the number of the first row
      column = oo.first_column
      row = header_row + 1
    
    #find the row that has data for this test case
    
    while  oo.cell(row,column) != tc
      row += 1
    end
      
    test_data = { } #create a new hash
   
    #Populate the hash
    while  column <=(oo.last_column)
      data = oo.cell(header_row, column)  #this is the key of the hash
      value = oo.cell(row,column)  #this is the value of the hash
      test_data[data] = value
      column = column.next
    end
    return test_data
  end
  
  #This method gives two hashes. One with Title and attributes and the other is title and values. This method is useful when we have same attribute value  repeating in a DOM page
  def get_input_data_hasharray(tc, sheet_name,input_excel=INPUT_EXCEL)

    excel= WIN32OLE::new('excel.Application') 
    workbook=excel.Workbooks.Open("#{input_excel}")
    worksheet=workbook.Worksheets("#{sheet_name}") 
    worksheet.Select 
    #~ excel ['Visible'] 
    
    header_row = 1
    attribute_row = header_row + 1
    row = 1
    column = 'A'
    title_cell = column + header_row.to_s
    
    
    row = row + 1  #to get to the attributes
    attribute_cell = column + attribute_row.to_s
    row = row + 1  # to get to the data values
    
    
    attribute_hash = {} # create attribute hash - this will have Title and attributes
    #Populate the hash
    while worksheet.Range(attribute_cell) ['Value'] != END_OF_DATA
    
      title = worksheet.Range(title_cell) ['Value'] #this is the key of the hash
      attribute = worksheet.Range(attribute_cell) ['Value'] #this is the value of the hash
      attribute_hash[title] = attribute
      column = column.next
      attribute_cell = column + attribute_row.to_s
      title_cell = column + header_row.to_s
    end
   row = 1
    column = 'A'
   data_cell = column + row.to_s
    #find the row that has data for this test case
    while  worksheet.Range(data_cell) ['Value'] != tc
      row += 1
      data_cell = column + row.to_s
    end
      
    test_data = { } #create a new hash -This will have title and values
   
    #Populate the hash
    while worksheet.Range(data_cell) ['Value'] != END_OF_DATA
    
      data = worksheet.Range(title_cell) ['Value'] #this is the key of the hash
      value = worksheet.Range(data_cell) ['Value'] #this is the value of the hash
      test_data[data] = value
      column = column.next
      data_cell = column + row.to_s
      title_cell = column + header_row.to_s
      
    end
    workbook.close
    hash_array = Array.new
    hash_array[1] = attribute_hash
    hash_array[2] = test_data
    #~ puts hash_array
    return hash_array
    
  ensure  
  excel.Quit
end #def end

#This method gives two hashes. One with Title and attributes and the other is title and values. This method is useful when we have same attribute value  repeating in a DOM page
def get_input_data_oo_hasharray(tc, sheet_name,input_oo)

      oo = Openoffice.new(input_oo)
      oo.default_sheet = sheet_name
      header_row = oo.first_row  # gives the number of the first row
      column = oo.first_column
      attribute_row = header_row + 1 #next row should be the attributes row
      
    attribute_hash = {} # create attribute hash
    
    # find row that has values for attributes
    while column<=(oo.last_column)
      title = oo.cell(header_row, column)  #this is the key of the hash
      attribute = oo.cell(attribute_row,column)  #this is the value of the hash
      attribute_hash[title] = attribute
      column = column.next
    end
    
    #reset for test_data
      row = oo.first_row
      column = oo.first_column
    while  oo.cell(row,column) != tc  #11
      row += 1
    end
      
     test_data = { } #create a new hash
   
    #Populate the hash
    while  column <=(oo.last_column)
      data = oo.cell(header_row, column)  #this is the key of the hash
      value = oo.cell(row,column)  #this is the value of the hash
      test_data[data] = value
      column = column.next
    end
    
    hash_array = Array.new
    hash_array[1] = attribute_hash
    hash_array[2] = test_data
    return hash_array
    
  end #def end
  
  
  
  #~ def check_for_popups 
    
    #~ autoit = WIN32OLE.new('AutoItX3.Control') 
  
    #~ loop do 
      #~ # Look for window with given title. Give up after 1 second. 
      #~ ret = autoit.WinWait('Windows Internet Explorer', '', 1) 
      #~ ret = autoit.WinWait('', '', 1) 
          
      #~ if (ret==1) then
        #~ autoit.Send('{enter}') 
      #~ end 
    
      #~ sleep(3) 
    #~ end 

  #~ end 
