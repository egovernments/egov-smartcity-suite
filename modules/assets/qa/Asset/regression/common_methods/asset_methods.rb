require path_to_file("asset_constants.rb")
 


module Asset_methods

include Asset_constants
include Test::Unit::Assertions

      

      def asset_user_login(username=ASSET_USER_NAME, password=ASSET_PASSWORD)
        
            $browser = start_browser(ASSET_URL)
	    @window= $browser.ie.Document.parentWindow
            assert_not_nil($browser.text_field(:id, ASSET_LOGIN_USERNAME_FLD), ("Couldn't find user name text field."))
            assert_not_nil($browser.text_field(:id, ASSET_LOGIN_PASSWORD_FLD), ("Couldn't find password input field."))

            $browser.text_field(:id, ASSET_LOGIN_USERNAME_FLD).set username
            $browser.text_field(:id, ASSET_LOGIN_PASSWORD_FLD).set password

            $browser.button(:value, ASSET_LOGIN_BUTTON).click
	    
            assert($browser.contains_text(ASSET_LOGIN_SUCCESS_MSG), (".Couldn't find text: " + ASSET_LOGIN_SUCCESS_MSG + ". Login Failed."))
           
            #~ $browser.cell(:xpath, "//[contains(text(),'#{ASSET_LINK}')]").click
             $browser.dt(:text,/#{ASSET_LINK}/).click
             assert_not_nil($browser.contains_text(ASSET_LOGIN_SUCCESS_MSG), (". Couldn't find text: " + ASSET_LOGIN_SUCCESS_MSG + ".login failed."))

      end

      def logout_asset
            sleep(2)
            $browser.link(:text, ASSET_SIGN_OUT_LINK).click
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

