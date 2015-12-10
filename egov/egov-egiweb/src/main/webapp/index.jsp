<!DOCTYPE html>
<html lang="en">
  <head>
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <!-- The above 3 meta tags *must* come first in the head; any other head content must come *after* these tags -->
    <title>Chennai Rains</title>

    <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/font-awesome/4.5.0/css/font-awesome.min.css">
    <!-- Bootstrap -->
    <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.6/css/bootstrap.min.css" integrity="sha384-1q8mTJOASx8j1Au+a5WDVnPi2lkFfwwEAa8hDDdjZlpLegxhjVME1fgjWPGmkzs7" crossorigin="anonymous">

      <style>
        .navbar-fixed-top {
            min-height: 80px;
        }

        .navbar-nav > li > a {
            padding-top: 0px;
            padding-bottom: 0px;
            line-height: 80px;
        }
          
        .navbar-header a.navbar-brand
        {
            margin-left: 0px !important;
            height: 80px;
            line-height: 50px;
            color:white;
        }
          
        .navbar-header a.navbar-brand:hover, .navbar-header a.navbar-brand:focus, .navbar-header a.navbar-brand:active
          {
              color: white;
          }
        
        .navbar-default .navbar-nav > .active > a, .navbar-default .navbar-nav > .active >   
         a:hover, .navbar-default .navbar-nav > .active > a:focus {
        color: white; /*BACKGROUND color for active*/
        background-color: #344E6F;
        }

          .navbar-default {
            background-color: #344E6F;
            border-color: #030033;
        }
          
        .padding-top
          {
              padding-top: 20%;
          }
          
          /* Sticky footer styles
            -------------------------------------------------- */
            html {
              position: relative;
              min-height: 100%;
              background-color: #f5f5f5;
            }
            body {
              /* Margin bottom by footer height */
              margin-bottom: 60px;
              background-color: #f5f5f5;
            }
            .footer {
              position: absolute;
              bottom: 0;
              width: 100%;
              /* Set the fixed height of the footer here */
              height: 60px;
              background-color: #eee;
            }


            /* Custom page CSS
            -------------------------------------------------- */
            /* Not required for template or sticky footer method. */

            body > .container {
               height:100%;
            }
            .container .text-muted {
              margin: 20px 0;
            }

            .footer > .container {
              padding-right: 15px;
              padding-left: 15px;
            }

            code {
              font-size: 80%;
            }
          
           .imp-link .btn.btn-default
           {
              margin-top: 20px;
              font-size: 1.4em;  
              width: 100%;
              padding: 8px 12px;
           }
          
           .imp-link.red i
          {
              color: red;
          }
          
          .imp-link.blue i
          {
              color: blue;
          }
          
          .egov-logo-div
          {
              height: 80px;
          }
          
          .egov-logo-div img
          {
              width: 160px;vertical-align: bottom;margin-top: 24px;
          }
    </style>
      
    <!-- HTML5 shim and Respond.js for IE8 support of HTML5 elements and media queries -->
    <!-- WARNING: Respond.js doesn't work if you view the page via file:// -->
    <!--[if lt IE 9]>
      <script src="https://oss.maxcdn.com/html5shiv/3.7.2/html5shiv.min.js"></script>
      <script src="https://oss.maxcdn.com/respond/1.4.2/respond.min.js"></script>
    <![endif]-->
  </head>
    
  <body>
      
   <div class="main-content">
   
       <header>
               <nav class="navbar navbar-default navbar-fixed-top">
               <div class="container">
                <div class="navbar-header">
                  <img src="/egi/images/chennailogo.jpg" style="width: 80px;" class="pull-left"/>
                  <a class="navbar-brand" href="#">Chennai Rains</a>
                </div>
                   
                <div class="egov-logo-div pull-right">
                    <a target="_blank" href="http://egovernments.org">
                       <img src="/egi/images/egov-logo.png">
                    </a>
                </div>
              </div>
            </nav>
       </header>
       
       <div class="container padding-top">
       
           <div class="row">
             <div class="col-xs-12 col-sm-4 col-sm-offset-2 text-center imp-link red"><a href="/pgr/complaint/citizen/anonymous/show-reg-form" target="_blank" class="btn btn-default"><i class="fa fa-medkit"></i>  &nbsp;Ask for aid</a></div>
             <div class="col-xs-12 col-sm-4 text-center imp-link blue"><a href="/pgr/complaint/citizen/anonymous/search" target="_blank" class="btn btn-default"><i class="fa fa-search"></i> &nbsp;Search for aid</a></div>
           </div>
           
           
       </div>
       
       
        <footer class="footer">
          <div class="container">
            <p class="text-muted">Powered by <a href="http://egovernments.org" target="_blank">eGovernments Foundation</a></p>
          </div>
       </footer>
       
   </div>

    <!-- jQuery (necessary for Bootstrap's JavaScript plugins) -->
    <script src="https://ajax.googleapis.com/ajax/libs/jquery/1.11.3/jquery.min.js"></script>
    <!-- Include all compiled plugins (below), or include individual files as needed -->
    <script src="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.6/js/bootstrap.min.js" integrity="sha384-0mSbJDEHialfmuBBQP6A4Qrprq5OVfW37PRR3j5ELqxss1yVqOtnepnHVP9aJ7xS" crossorigin="anonymous"></script>
  </body>
</html>