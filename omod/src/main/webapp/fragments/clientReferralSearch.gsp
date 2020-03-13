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
    
    
</style>

<div class="row wrapper  white-bg page-heading"  style="">
    <div class="col-lg-8 offset-lg-2">

        <div class="panel panel-flat">

            <div class="panel-heading" style="padding:10px 20px">

                
                <h4 style="text-align: center">
                    
                    Patient Search Form 
                    
                </h4>

                <h5>
                    <br/>
                    <input style="width: 40%;font-size: 16px; padding: 12px 20px 12px 40px; border: 1px solid #ddd; margin-bottom: 12px;" class="heading-text pull-left" type="text" id="referralSearch" placeholder="Enter Referral Code" required>

                    <input style="margin-left: 40px; width: 20%; height: 45px" type="button" value="Search" class="btn btn-primary" onclick="processReferralSearch()" />

                    <br/><br/>
                </h5>

            </div>

        </div>
        <div class="table-responsive">
            <table class="table table-striped table-bordered  table-hover" id="tb_patientsearch" style="display: none">
                <thead>
                    <tr>
                        <th>${ ui.message("First Name") }</th>
                        <th>${ ui.message("Last Name") }</th>
                        <th>${ ui.message("Gender") }</th>
                        <th>${ ui.message("Address") }</th>
                        <th>${ ui.message("State") }</th>
                        <th>${ ui.message("Marital Status") }</th>
                    </tr>
                </thead>
                <tbody id="TableBody">




                </tbody>
            </table>
            <br><br>
            
            <input type="button" value="Continue & Register Patient" class="btn btn-primary" onclick="processReferralSubmit()" />


        </div>

    </div>
</div>


<script type="text/javascript">

    var GlobalClient = "";
    function processReferralSearch(){

    jq = jQuery;
    jq('#wait').hide();
    
    jq('#gen-wait').show();
    
    var refID = jQuery('#referralSearch').val();
    
    
     if(refID !="")
     {
            
            jq.ajax({
            url: "${ ui.actionLink("patientindextracing", "ndr", "retrieveClientByID") }",
            dataType: "json",
            data: {
            'referralCode': refID
            }


            }).success(function(data) {
            jq('#gen-wait').hide();
            console.log(data);
             GlobalClient = data;
            console.log(data.address);
            
            if(!data.address)
            {
                   alert('ID Not Found');
            }
            else
            {
                    jq('#TableBody').append("<tr><td>"+data.firstName+"</td><td>"+data.surname+"</td><td>"+data.sex+"</td><td>"+data.address+"</td><td>"+data.clientState+"</td><td>"+data.maritalStatus+"</td></tr>");
            }
            
           
           

            })
            .error(function(xhr, status, err) {
            jq('#gen-wait').hide();
            alert('An error occured');

            }); 

            jQuery('#tb_patientsearch').show(500);

     }
     
     else
     {
        alert('Referral code cannot be empty');
     }

    

   
    }

</script>


<script type="text/javascript">

    function processReferralSubmit(){

    jq = jQuery;
    jq('#wait').hide();
    
    jq('#gen-wait').show();
    
    
            var jsonclientData =  JSON.stringify(GlobalClient);
            
            jq.ajax({
            url: "${ ui.actionLink("patientindextracing", "ndr", "retrieveClient") }",
            dataType: "json",
            data: {
            'clientData': jsonclientData
            }


            }).success(function(data) {
            jq('#gen-wait').hide();
            console.log(data);
            
                alert(data);

            })
            .error(function(xhr, status, err) {
            jq('#gen-wait').hide();
            alert('An error occured');

            }); 

    }

</script>