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

        <div class="panel panel-flat" id="auth_form" style="display: none">

            <div class="panel-heading" style="padding:10px 20px;">

                
                <h4 style="margin-left: 38%; margin-bottom: 10px">
                    
                    Facility Authentication Form 
                    
                </h4>

              
                    
                         <h5 style="margin-left: 32%; width: 40%; height: 90%; background-color: #00463f; border-radius: 10px; ">
                        <br/> <br/> 


                        <label style="font-size: 16px; padding: 12px 20px 12px 40px; margin-bottom: 12px; color: #fff; margin-top: 20px">Username: </label><br>

                        <label style="margin-left: 20px; width: 70%;font-size: 16px; padding: 12px 20px 12px 40px; border: 1px solid #ddd; margin-bottom: 12px; border-radius: 15px 50px; color: #fff" class="heading-text pull-left" id="username" placeholder="" required="required"> </label><br>

    <!--                    <input style="margin-left: 20px; width: 70%;font-size: 16px; padding: 12px 20px 12px 40px; border: 1px solid #ddd; margin-bottom: 12px; border-radius: 15px 50px;" class="heading-text pull-left" type="text" id="username" placeholder="" required="required">-->

                        <br/>

                        <div id="old_password_div" style="display:none">

                                    <label style="font-size: 16px; padding: 12px 20px 12px 40px; margin-bottom: 12px; color: #fff">Old Password: </label><br>

                                    <input style="margin-left: 20px; width: 70%;font-size: 16px; padding: 12px 20px 12px 40px; border: 1px solid #ddd; margin-bottom: 12px; border-radius: 15px 50px;" class="heading-text pull-left" type="password" id="old_password" autocomplete="off" placeholder="" required="required">

                                    <br/><br/><br/> 


                        </div>

                        <label style="font-size: 16px; padding: 12px 20px 12px 40px; margin-bottom: 12px; color: #fff">Password: </label><br>

                        <input style="margin-left: 20px; width: 70%;font-size: 16px; padding: 12px 20px 12px 40px; border: 1px solid #ddd; margin-bottom: 12px; border-radius: 15px 50px;" class="heading-text pull-left" type="password" id="password" autocomplete="off" placeholder="" required="required">

                      <br/><br/><br/> 

                       <label style="font-size: 16px; padding: 12px 20px 12px 40px; margin-bottom: 12px; color: #fff">Confirm Password: </label><br>

                       <input style="margin-left: 20px; width: 70%;font-size: 16px; padding: 12px 20px 12px 40px; border: 1px solid #ddd; margin-bottom: 12px; border-radius: 15px 50px;" class="heading-text pull-left" type="password" id="c_password" autocomplete="off" placeholder="" required="required">

                      <br/><br/><br/> 

                      <label style="font-size: 16px; padding: 12px 20px 12px 40px; margin-bottom: 12px; color: #fff">Mac Address </label><br>

                      <label style="margin-left: 20px; width: 70%;font-size: 16px; padding: 12px 20px 12px 40px; border: 1px solid #ddd; margin-bottom: 12px; border-radius: 15px 50px; color: #fff" class="heading-text pull-left" id="mac_address"> </label><br>

    <!--                     <input style="margin-left: 20px; width: 70%;font-size: 16px; padding: 12px 20px 12px 40px; border: 1px solid #ddd; margin-bottom: 12px; border-radius: 15px 50px;" class="heading-text pull-left" type="text" id="mac_address" placeholder="" required>-->

                        <br/><br/>

                        <input style="background-color: #E8F0FE; margin-left: 45px; width: 70%; height: 45px; border-radius: 25px; margin-top: 15px" type="button" value="Change Password" class="btn btn-primary" onclick="Authenticate()" />

                      <br/><br/>


                    </h5>
                        
                        
                    
           
               

            </div>

        </div>

    </div>
</div>

<script>
    
    jQuery(document).ready(function() {
   
     jq = jQuery;
     var facility = '';
     var username = '';
     var mac_address = '';
     var passwordExist = '';
     jq.ajax({
            url: "${ ui.actionLink("patientindextracing", "ndr", "retrieveSystemUserDetails") }",
            dataType: "json",


            }).success(function(data) {
            jq('#gen-wait').hide();
            console.log(data);

             facility = jq.parseJSON(data);
             
             console.log(facility.username);
             console.log(facility.mac);
             console.log(facility.passwordExist);
             
             username = facility.username;
             mac = facility.mac;
             passwordExist = facility.passwordExist;
            
            if(username != '')
            {
                if(passwordExist === true)
                {
                    
                    jq('#old_password_div').show();
                    
                }
               jq('#username').text(username);
               jq('#mac_address').text(mac);
               jq('#auth_form').show();
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
    var GlobalClient = "";
    
    function checkconnection() 
    {
        var status = navigator.onLine;
        return status;
    }

    function Authenticate()
    {
 
       var internet_status = checkconnection();
             
       if (internet_status) 
       {
                
                jq('#wait').hide();
    
                jq('#gen-wait').show();

                var username = jQuery('#username').text();
                var mac_address = jQuery('#mac_address').text();
                var password = jQuery('#password').val();
                var c_password = jQuery('#c_password').val();
                var old_password = jQuery('#old_password').val();

                 if(password !="")
                 {
                    if(password == c_password)
                    {               
                         console.log(username+' '+password);

                         var data = JSON.stringify({username: username, mac: mac_address, password: password, oldPassword: old_password });
                         console.log(data);
                         var obj = jq.parseJSON(data);
                         console.log(obj);
                         
                         jq.ajax({
                                url: "${ ui.actionLink("patientindextracing", "ndr", "saveSystemUser") }",
                            dataType: "json",
                            data: {
                            'userModel': data
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
                    else
                    {
                       alert('The passwords you entered do not match, please check and try again');
                    }

                 }

                 else
                 {
                    alert('Password and Confirm Password cannot be empty');
                 }

    
       } 
       else 
       {
                alert('No internet Connectivity, please check your internet connection and try again.');
       }
 
    }

</script>