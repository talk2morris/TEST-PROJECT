<%
def id = config.id
%>
<%= ui.resourceLinks() %>
<style>
    
.warning{
background-color: #f4da07;
width: 80%;
text-align: center;
}    

.success{
background-color: #5cb85c;
color: #fff;
width: 80%;
text-align: center;
} 


/* The Modal (background) */
.modal {
  display: none; /* Hidden by default */
  position: fixed; /* Stay in place */
  z-index: 1; /* Sit on top */
  left: 0;
  top: 30%;
  width: 100%; /* Full width */
  height: 50%; /* Full height */
  overflow: auto; /* Enable scroll if needed */
  background-color: rgb(0,0,0); /* Fallback color */
  background-color: rgba(0,0,0,0.4); /* Black w/ opacity */
}

/* The Close Button */
.close {
  color: #fff;
  float: right;
  font-size: 28px;
  font-weight: bold;
}

.close:hover,
.close:focus {
  color: black;
  text-decoration: none;
  cursor: pointer;
}

/* Modal Header */
.modal-header {
  padding: 2px 16px;
  background-color: #00463f;
  color: #fff;
}

/* Modal Body */
.modal-body {padding: 2px 16px;}

/* Modal Footer */
.modal-footer {
  padding: 2px 16px;
  background-color: #5cb85c;
  color: #fff;
}

/* Modal Content */
.modal-content {
  position: relative;
  background-color: #fefefe;
  margin: auto;
  padding: 0;
  border: 1px solid #888;
  width: 80%;
  box-shadow: 0 4px 8px 0 rgba(0,0,0,0.2),0 6px 20px 0 rgba(0,0,0,0.19);
  animation-name: animatetop;
  animation-duration: 0.4s
}

/* Add Animation */
@keyframes animatetop {
  from {top: -300px; opacity: 0}
  to {top: 0; opacity: 1}
}

    
    
</style>

<div class="row wrapper  white-bg page-heading"  style="">
    <div class="col-lg-8 offset-lg-2">

        <div class="panel panel-flat">

            <div class="panel-heading" style="padding:10px 20px">

                
                <h4 style="text-align: center">
                    
                    Contacts List
                    
                </h4>

                <h5>
                    <br/>
                    <input style="width: 40%;font-size: 16px; padding: 12px 20px 12px 40px; border: 1px solid #ddd; margin-bottom: 12px;" class="heading-text pull-left" type="text" id="nameSearch" onkeyup="myFunction()" placeholder="Search..">

                    <br/><br/>
                </h5>

            </div>

        </div>
        <div class="table-responsive">
            <table class="table table-striped table-bordered  table-hover" id="tb_commtester">
                <thead>
                    <tr>
                        <th>${ ui.message("First Name") }</th>
                        <th>${ ui.message("Last Name") }</th>
                        <th>${ ui.message("Gender") }</th>
                        <th>${ ui.message("State") }</th>
                        <th>${ ui.message("Trace Status") }</th>
                        <th>${ ui.message("Assigned To") }</th>
                        <th>${ ui.message("Actions") }</th>
                    </tr>
                </thead>
                <tbody id="TableBody">




                </tbody>
            </table>

        </div>

    </div>
</div>

<script>
    
    console.log('this is patient uuid, remember to convert to id at the backend');
    console.log(patientId);
    
    
    function myFunction() {
    // Declare variables 
    var input, filter, table, tr, td, i, txtValue;
    input = document.getElementById("nameSearch");
    filter = input.value.toUpperCase();
    table = document.getElementById("tb_commtester");
    tr = table.getElementsByTagName("tr");

    // Loop through all table rows, and hide those who don't match the search query
    for (i = 0; i < tr.length; i++) {
    td = tr[i].getElementsByTagName("td");

    for (var j = 0; j < td.length; j++) {
    cell = tr[i].getElementsByTagName("td")[j];
    if (cell) {
        if (cell.innerHTML.toUpperCase().indexOf(filter) > -1) {
    tr[i].style.display = "";
    break;
    }
    else {
    tr[i].style.display = "none";
    }
    }
    } 

    }
    }
</script>

<script>
 
    jq = jQuery;
    var GlobalClients = "";
    jq('#wait').hide();
    jq(function() {
       
    jq('#gen-wait').show();

    jq.ajax({
        url: "${ ui.actionLink("patientindextracing", "ndr", "getIndexContacts") }",
    dataType: "json",
    data: {
    'indexClientId': patientId
    }
    
    }).success(function(data) {
    jq('#gen-wait').hide();
    console.log(data);
     
    var obj = jq.parseJSON(data);
    GlobalClients = obj;
    console.log(obj.length);
    console.log(obj);
    
    console.log(GlobalClients);
 
    var assingmentNullCheck =  '';
    var moreButton = '';
    var userID = '';

    
    if(obj !="")
    {
    
        for(var i=0;i<obj.length;i++)
        {
        
            userID = obj[i].id+'';
            console.log(userID);
            
             moreButton = '<button type="" class="btn btn-primary heading-text" style="width: 80%;" onclick="showMore(' + userID + ')">'+"Show More Data"+'</button>';

            
            assignmentNullCheck = obj[i].community_tester_name+'';
            if(assignmentNullCheck == 'null')
            {
                assignmentNullCheck = 'Unassigned';
            }
           
        
            if(obj[i].trace_status == 'pending' || obj[i].trace_status == 'Pending')
            {
                jq('#TableBody').append("<tr><td>"+obj[i].lastname+"</td><td>"+obj[i].firstname+"</td><td>"+obj[i].sex+"</td><td>"+obj[i].state+"</td><td><div class='warning'>"+obj[i].trace_status+"</div></td><td>"+assignmentNullCheck+"</td><td>"+moreButton+"</td></tr>");
            }
            else
            {
            
                jq('#TableBody').append("<tr><td>"+obj[i].lastname+"</td><td>"+obj[i].firstname+"</td><td>"+obj[i].sex+"</td><td>"+obj[i].state+"</td><td><div class='success'>"+obj[i].trace_status+"</div></td><td>"+assignmentNullCheck+"</td><td>"+moreButton+"</td></tr>");

            }
            

        }
    
    }
    
    })
    .error(function(xhr, status, err) {
    jq('#gen-wait').hide();
    alert('An error occured');

    }); 

    });

</script>



<script type="text/javascript">
    jq = jQuery;
  

    function showMore(clientID)
    {
         
               jq('#wait').hide();
    
               jq('#gen-wait').show();
               
               var selector = jq(this).data('selector');
                
               console.log(clientID);
               
               var result = jQuery.grep(GlobalClients, function(e){ return e.id == clientID; });
    
                console.log(result[0]);
              

                var jsonResult =  JSON.stringify(result[0]);

                console.log(jsonResult);
                
                // Get the modal
                var modal = document.getElementById("myModal");
                
                jq('#modalText').append('Name: '+result[0].firstname+' '+result[0].lastname+'<br>'+'Phone Number: '+result[0].phone_number+'<br>'+'Sex: '+result[0].sex+'<br>'+'Preferred Testing Location: '+result[0].preferred_testing_location+', '+result[0].lga+', '+result[0].state+'<br>'+'Physically Abused: '+result[0].physically_abused+'<br>'+'Forced Sexually: '+result[0].forced_sexually+'<br>'+'Fear Their Partner: '+result[0].fear_their_partner+'<br>'+'Notification Method: '+result[0].notification_method)
                modal.style.display = "block"; 

                
 
    }

</script>




<!-- The Modal -->
<!--<div id="myModal" class="modal">

   Modal content 
  <div class="modal-content">
      <span class="close" onclick="modalOff()">&times;</span>
    <p id="modalText"></p>
  </div>

</div>-->


<!-- Modal content -->
 <div id="myModal" class="modal">
<div class="modal-content">
  <div class="modal-header">
    <span class="close" onclick="modalOff()">&times;</span>
    <h2 style="color: #fff">Contacts Listing - More Details</h2>
  </div>
  <div class="modal-body">
   <p id="modalText"></p>
  </div>

</div>
</div>

<script type="text/javascript">
   
         // When the user clicks anywhere outside of the modal, close it
        window.onclick = function(event) {
        if (event.target == modal) {
              modal.style.display = "none";
        } 
    }
</script>


<script type="text/javascript">
    function modalOff()
    {
    
       jq = jQuery;
       
       // Get the modal
       var modal = document.getElementById("myModal");
        modal.style.display = "none";
        
        jq("#modalText").html("");
    }
    
</script>
