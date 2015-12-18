<!DOCTYPE html>
<html lang="en">
   <head>
      <meta charset="utf-8">
      <title>Insurance Java sample app</title>
      <meta name="viewport" content="width=device-width, initial-scale=1.0">
      <meta name="description" content="">
      <meta name="author" content="">
      <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.6/css/bootstrap.min.css" integrity="sha384-1q8mTJOASx8j1Au+a5WDVnPi2lkFfwwEAa8hDDdjZlpLegxhjVME1fgjWPGmkzs7" crossorigin="anonymous">
      <link href="style/sample-container.css" rel="stylesheet">
      <link rel="stylesheet" type="text/css" href="//cdn.datatables.net/1.10.10/css/jquery.dataTables.css">
	  <!-- HTML5 shim and Respond.js for IE8 support of HTML5 elements and media queries -->
	  <!--[if lt IE 9]>
	    <script src="https://oss.maxcdn.com/html5shiv/3.7.2/html5shiv.min.js"></script>
	    <script src="https://oss.maxcdn.com/respond/1.4.2/respond.min.js"></script>
	  <![endif]-->

      <script src="https://ajax.googleapis.com/ajax/libs/jquery/1.11.3/jquery.min.js"></script>
      <script src="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.6/js/bootstrap.min.js" integrity="sha384-0mSbJDEHialfmuBBQP6A4Qrprq5OVfW37PRR3j5ELqxss1yVqOtnepnHVP9aJ7xS" crossorigin="anonymous"></script>
      <script type="text/javascript" src="js/scripts.js"></script>
      <script type="text/javascript" charset="utf8" src="//cdn.datatables.net/1.10.10/js/jquery.dataTables.js"></script>
      
      <%@page import="com.ibm.twa.bluemix.samples.managers.WorkloadSchedulerManager"%>

		<%!
			//Session key to register in the session
			private static final String WORKLOAD_APP_SESSION_KEY = "INSURANCE_APP";
		%>
		<%
			WorkloadSchedulerManager wsApp;
			if (session.getAttribute(WORKLOAD_APP_SESSION_KEY)!=null){
				wsApp = (WorkloadSchedulerManager) session.getAttribute(WORKLOAD_APP_SESSION_KEY);
			}else{
				wsApp = new WorkloadSchedulerManager();
				session.setAttribute(WORKLOAD_APP_SESSION_KEY,wsApp);
			}
		%>
   </head>
    <body>
      <div class="container">
         <div class="row clearfix">
            <div class="col-md-12 column">
               <nav class="navbar navbar-default navbar-fixed-top" role="navigation">
                  <div class="navbar-header">
                     <span class="application-title"> Workload Scheduler sample</span>
                     <button type="button" class="navbar-toggle collapsed" data-toggle="collapse" data-target="#bs-example-navbar-collapse-1"> <span class="sr-only">Toggle navigation</span><span class="icon-bar"></span><span class="icon-bar"></span><span class="icon-bar"></span></button> <a class="navbar-brand" href="#"><img class="image-logo" src="http://www.graphicalweb.org/2013/assets/ibm_logo_small.png" /></a>
                  </div>
                  <div class="collapse navbar-collapse" id="bs-example-navbar-collapse-1">
                     <ul class="nav navbar-nav">
                        <li class="active" onclick="selectProvisioning()" id="provisioningButton">
                           <a href="#" >Provisioning</a>
                        </li>
                        <li onclick="selectMonitoring()" id="monitoringButton">
                           <a href="#" >Monitor</a>
                        </li>
                     </ul>
                  </div>
               </nav>
               <div class="jumbotron well description">
                  <h1>
                     Java sample
                  </h1>
                  <p>
                   	Insurance app
                  </p>
                </div>
            </div>
         </div>
         <div class="row clearfix" id="provisioning-container">
            <div class="col-md-4 column provisioning-column">
               <form role="form" name="start" method="post">
                  <div class="form-group">
                     <label for="email">Email</label><input type="text" class="form-control" id="email" name="email" placeholder="insert your email">
                  	 <label for="hp">HP(horse power)</label><input type="text" class="form-control" id="hp" name="hp" placeholder="insert your car hp">
                  	 <label for="birthyear">Birth year</label><input type="text" class="form-control" id="birthyear" name="birthyear" placeholder="insert your birth year">
                  	 <input type="hidden" id="formSubmitted" name="formSubmitted" value="" />
                  	 <button type="submit" class="btn btn-primary" style="margin-top: 30px;" formaction="index.jsp">Start</button>
                  </div>
               </form>
            </div>
         </div>
         
         <div class="row clearfix submit-button-container">
            <div class="col-md-12 column">
               <div class="modal fade" id="actionSubmitted" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
                  <div class="modal-dialog">
                     <div class="modal-content">
                        <div class="modal-header">
                           <!--button type="button" class="close" data-dismiss="modal" aria-hidden="true">×</button-->
                           <h4 class="modal-title" id="myModalLabel">
                              Success
                           </h4>
                        </div>
                        <div class="modal-body">
                           Action submitted successfully
                        </div>
                        <div class="modal-footer">
                           <button type="button" class="btn btn-primary" data-dismiss="modal">Close</button> 
                        </div>
                     </div>
                  </div>
               </div>
            </div>
         </div>
         
         <div class="row clearfix">
            <div class="col-md-12 column">
               <div id="monitoring-container" style="display: none">
                  <table id="datatable" class="display" cellspacing="0" width="100%">
                     <thead>
                        <tr>
                           <th>Email</th>
                           <th>Subject</th>
                           <th>Status</th>
                           <th>Start date</th>
                        </tr>
                     </thead>
                     <tfoot>
                        <tr>
                           <th>Email</th>
                           <th>Subject</th>
                           <th>Status</th>
                           <th>Start date</th>
                        </tr>
                     </tfoot>
                     <tbody id ="monitoringTableBody">
                     </tbody>
                  </table>
               </div>
            </div>
         </div>
      </div>
      
      <div class="loading-hide" id="loadingDiv"> 
         Loading data...
      </div>
      
      <div class="loading-hide" id="loadingSubmitDiv"> 
         Submitting request...
      </div>
      
      	<%
      	if(request.getParameter("formSubmitted") != null){
      		request.removeAttribute("formSubmitted");
	      	wsApp.initConnection();
			if (wsApp.isConnected()) {
				String email = request.getParameter("email");
				int hp = Integer.parseInt(request.getParameter("hp"));
				int birthyear = Integer.parseInt(request.getParameter("birthyear"));
				wsApp.runProcess(email, hp, birthyear);
				if (wsApp.isSubmitted()){ 
					%>
					<script>
						jQuery('#actionSubmitted').modal('show');
						jQuery("#provisioning-container input").blur();
					</script>
					<%
				} else {
					%>
					<script>
					 	alert("Error during the submission (process doesn't exists)");
					</script>
					<%
				}
	 		}
			else {
				%>
				<script>
				 	alert("Error during the submission (app not connected)");
				</script>
				<%
			}
      	}
	%>
	
   </body>
