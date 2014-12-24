require 'path_helper'
#~ require path_to_file("Utils.rb")
require path_to_file("stores_constants.rb")

module Stores_common_methods

include Stores_constants
include Test::Unit::Assertions

def login_to_stores(username=STORES_USER_NAME, password=STORES_PASSWORD)
    
    $browser = start_browser(STORES_URL)
    #~ puts $browser.text
    assert_not_nil($browser.text_field(:id, STORES_LOGIN_USERNAME_FLD), ("Couldn't find user name text field."))
    assert_not_nil($browser.text_field(:id, STORES_LOGIN_PASSWORD_FLD), ("Couldn't find password input field."))
   
    $browser.text_field(:id, STORES_LOGIN_USERNAME_FLD).set username
    $browser.text_field(:id, STORES_LOGIN_PASSWORD_FLD).set password
    
    $browser.button(:value, STORES_LOGIN_BUTTON).click

    assert($browser.contains_text(STORES_LOGIN_SUCCESS_MSG), (". Couldn't find text: " + STORES_LOGIN_SUCCESS_MSG + ". Login Failed."))
    
    #~ code to check the inventory management link    
    
    #~ Watir::Waiter.wait_until{$browser.cell(:xpath, "//[contains(text(),'Inventory Management')]").exists?}
    Watir::Waiter.wait_until{$browser.dt(:text,'Inventory Management').exists?}
    #~ $browser.cell(:xpath, "//[contains(text(),'Inventory Management')]").click
    $browser.dt(:text,'Inventory Management').click
    
    #~ ie_inventory_management = Watir::IE.attach(:title, /eGovernance/)    
    assert($browser.contains_text(INVENTORY_MANAGEMENT_SUCCESS_MESG), "Couldn't open Inventory Management Home page")

end
  
  
def logout_stores
  
    $browser.link(:text, STORES_LOGOUT_BTN).flash  
    $browser.link(:text, STORES_LOGOUT_BTN).click

  ensure
    $browser.close

end


def close_all_windows
  
    loop do
      begin
        Watir::IE.attach(:title, //).close
      rescue Watir::Exception::NoMatchingWindowFoundException
        break
      rescue
        retry
      end
    end
    
end




end
