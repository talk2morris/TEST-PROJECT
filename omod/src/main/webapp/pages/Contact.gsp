<style>
    /* Style the tab */
    .tab {
    overflow: hidden;
    border: 1px solid #ccc;
    background-color: #f1f1f1;
    }

    /* Style the buttons that are used to open the tab content */
    .tab button {
    background-color: inherit;
    float: left;
    border: none;
    outline: none;
    cursor: pointer;
    padding: 14px 16px;
    transition: 0.3s;
    }

    /* Change background color of buttons on hover */
    .tab button:hover {
    background-color: #ddd;
    }

    /* Create an active/current tablink class */
    .tab button.active {
    background-color: #ccc;
    }

    /* Style the tab content */
    .tabcontent {
    display: none;
    padding: 6px 12px;
    border: 1px solid #ccc;
    border-top: none;
    animation: fadeEffect 1s; /* Fading effect takes 1 second */
    }

    /*.input-w label, .input-w input {
    float: none; !* if you had floats before? otherwise inline-block will behave differently *!
    display: inline-block;
    vertical-align: middle;
    }*/

    /* Go from zero to full opacity */
    @keyframes fadeEffect {
    from {opacity: 0;}
    to {opacity: 1;}
    }
</style>

<link href="https://cdnjs.cloudflare.com/ajax/libs/select2/4.0.10/css/select2.min.css" rel="stylesheet" />
<script src="https://cdnjs.cloudflare.com/ajax/libs/select2/4.0.10/js/select2.min.js"></script>

<!-- Tab links -->
<div class="tab">
        
    <button class="tablinks" onclick="openTab(event, 'Contacts_Listing')" id="defaultOpen">Contacts Listing</button>

    <button class="tablinks" onclick="openTab(event, 'Create_Contacts')">Create Contacts</button>
    
    <button class="tablinks" onclick="openTab(event, 'Assign_Contacts')">Assign Contacts</button>

    <button class="tablinks" onclick="openTab(event, 'Data_Visualization')">Data Visualization</button>

</div>

<!-- Tab content -->
<div id="Create_Contacts" class="tabcontent">

    <% ui.decorateWith("appui", "standardEmrPage") %>

    <%= ui.resourceLinks() %>

    <% ui.includeJavascript("patientindextracing", "lga.js") %>

    <style>

        table, tr, td {
        border: none;
        }

    </style>

    <script>

        var globalPatientID = "";
        function getUrlVars()
        {
            let vars = [], hash;
            let hashes = window.location.href.slice(window.location.href.indexOf('?') + 1).split('&');
            console.log(hashes);
            for(let i = 0; i < hashes.length; i++)
            {
                hash = hashes[i].split('=');
                vars.push(hash[0]);
                vars[hash[0]] = hash[1];
            }
            console.log(vars);
            return vars;
        }


        let patientId;
        patientId = getUrlVars()["patientId"];
        globalPatientID = getUrlVars()["patientId"];
       
        
      
    

    </script>

    <div class="row wrapper  white-bg page-heading"  style="">

        <h4 style="text-align: center">
                    
                    Create Contacts 
                    
        </h4>
    </div>

<br>


    <div class="container" style="padding-top: 10px;">
        <form method="post">
            <fieldset>
                <div>
<!--                    <legend> Index Client Patient Id</legend>-->
                    <input type="hidden" id="index_patient_id" name="index_patient_id" value="" class="form-control" />

                </div>

                <legend> Relationship</legend>
                <div class="form-row">

                    <label class="radio-inline" style="padding-right: 70px"><input type="radio" name="relationship" value="Sexual Partner" checked>Sexual Partner</label>
                    <label class="radio-inline" style="padding-right: 70px"><input type="radio" name="relationship" value="Biological Children" >Biological Children</label>
                    <label class="radio-inline" style="padding-right: 70px"><input type="radio" name="relationship" value="PWID" >PWID</label>
                    <label class="radio-inline" style="padding-right: 70px"><input type="radio" name="relationship" value="Siblings">Siblings</label>
                    <label class="radio-inline" style="padding-right: 70px"><input type="radio" name="relationship" value="Biological Mother" >Biological Mother</label>

                </div>
            </fieldset>



            <table cellspacing="0" cellpadding="0">

                <tr>
                    <td>
                        <label for="firstname">Firstname</label><input type="text" class="form-control" name="firstname" id="firstname" placeholder="firstname"></td>

                    <td>
                        <label for="lastname">Lastname</label><input type="text" class="form-control" name="lastname" id="lastname" placeholder="lastname"></td>

                    <td>
                        <label for="age">Age</label><input type="text" class="form-control" name="age" id="age" placeholder="Age"></td>

                </tr>
                <br/>

                <tr>


                    <td><label for="sex">Sex</label>
                        <select id="sex" name="sex" class="form-control">
                            <option selected>Choose...</option>
                            <option>Male</option>
                            <option>Female</option>
                        </select> </td>

                    <td>
                        <label for="state">State</label>
                        <select id="state" name="state" class="form-control">
                            <option value="" selected="selected" >- Select -</option>
                            <option value='Abia'>Abia</option>
                            <option value='Adamawa'>Adamawa</option>
                            <option value='AkwaIbom'>AkwaIbom</option>
                            <option value='Anambra'>Anambra</option>
                            <option value='Bauchi'>Bauchi</option>
                            <option value='Bayelsa'>Bayelsa</option>
                            <option value='Benue'>Benue</option>
                            <option value='Borno'>Borno</option>
                            <option value='Cross River'>Cross River</option>
                            <option value='Delta'>Delta</option>
                            <option value='Ebonyi'>Ebonyi</option>
                            <option value='Edo'>Edo</option>
                            <option value='Ekiti'>Ekiti</option>
                            <option value='Enugu'>Enugu</option>
                            <option value='FCT'>FCT</option>
                            <option value='Gombe'>Gombe</option>
                            <option value='Imo'>Imo</option>
                            <option value='Jigawa'>Jigawa</option>
                            <option value='Kaduna'>Kaduna</option>
                            <option value='Kano'>Kano</option>
                            <option value='Katsina'>Katsina</option>
                            <option value='Kebbi'>Kebbi</option>
                            <option value='Kogi'>Kogi</option>
                            <option value='Kwara'>Kwara</option>
                            <option value='Lagos'>Lagos</option>
                            <option value='Nasarawa'>Nasarawa</option>
                            <option value='Niger'>Niger</option>
                            <option value='Ogun'>Ogun</option>
                            <option value='Ondo'>Ondo</option>
                            <option value='Osun'>Osun</option>
                            <option value='Oyo'>Oyo</option>
                            <option value='Plateau'>Plateau</option>
                            <option value='Rivers'>Rivers</option>
                            <option value='Sokoto'>Sokoto</option>
                            <option value='Taraba'>Taraba</option>
                            <option value='Yobe'>Yobe</option>
                            <option value='Zamfara'>Zamafara</option>
                        </select></td>

                    <td><label for="lga">LGA</label>
                        <select name="lga" id="lga" class="form-control" required>
                            <option selected>Choose...</option>

                        </select></td>
                </tr>
                <br/>


                <tr>


                    <td><label for="town">Town</label>
                        <input type="text" class="form-control" id="town" name="town" > </td>

                    <td>
                        <label for="village">Village</label>
                        <input type="text" class="form-control" id="village" name="village" placeholder="village of origin"></td>

                    <td><label for="preferred_testing_location">Contacts preferred testing location</label>
                        <select id="preferred_testing_location" name="preferred_testing_location" class="form-control">
                            <option selected>Choose...</option>
                            <option>Facility</option>
                            <option>Community level</option>
                            <option>Other location</option>
                        </select> </td>
                </tr>
                <tr>
                 <td><label for="phone_number">Phone Number</label>
                        <input type="text" class="form-control" id="phone_number" name="phone_number" > </td>
                </tr>


            </table>
            <br>
            <div>
                <fieldset>
                    <legend>Relationship situations</legend>

                    <div class="form-control">
                        <div class="form-group">
                            <div class="row">
                                <div class="col-md-8">
                                    <label  for="physically_abused">Physical abuse (hit, kicked, slapped or phisically hurt) within the last year</label>
                                </div>

                                <div class="col-md-4">
                                    <select id="physically_abused" name="physically_abused" class="form-control col-md-4">
                                        <option value="No" selected>No</option>
                                        <option value="Yes" >Yes</option>
                                    </select>

                                </div>

                            </div>
                        </div>

                        <div class="row">
                            <div class="form-group">
                                <label  class="col-md-8" for="forced_sexually">Forced sexual activities within the last year</label>
                                <select id="forced_sexually" name="forced_sexually" class="form-control col-md-4">
                                    <option value="No" selected>No</option>
                                    <option value="Yes" >Yes</option>

                                </select>
                            </div>
                        </div>

                        <div class="row">
                            <div class="form-group">
                                <label class="col-md-8" for="fear_their_partner">Fear their partner</label>
                                <select id="fear_their_partner" name="fear_their_partner" class="form-control col-md-4">
                                    <option value="No" selected>No</option>
                                    <option value="Yes" >Yes</option>

                                </select></div>

                        </div>
                    </div>
                </fieldset>
            </div>


            <br>

            <fieldset>
                <legend>
                    Notification method
                </legend>
                <div class="form-group">

                    <div class="radio">
                        <label><input type="radio" name="notification_method" value="Cleint referral" ><span class="" style="padding-left: 10px">Cleint referral</span></label>
                    </div>
                    <div class="radio">
                        <label><input type="radio" name="notification_method" value="provider referral" ><span class="" style="padding-left: 10px">provider referral</span></label>
                    </div>
                    <div class="radio">
                        <label><input type="radio" name="notification_method" value="Contact referral" ><span class="" style="padding-left: 10px">Contact referral</span></label>
                    </div>
                    <div class="radio">
                        <label><input type="radio" name="notification_method" value="Dual referral" ><span class="" style="padding-left: 10px">Dual referral</span></label>
                    </div>
                </div>
            </fieldset>
            
                <br><br>

            <fieldset>
                <legend>More Information</legend>
                <textarea rows="2"  cols="100"  name="more_information" ></textarea>
            </fieldset>
            
            <br><br>

            <fieldset>
                <legend>
                    Assign to Community Tester
                </legend>
                <div class="form-group">

                    <div class="radio">
                        <label><input type="radio" name="assign_contact_to_cec" value="Yes" onclick="showTester()" ><span class="form-control" style="padding-left: 10px"  >Yes</span></label>
                    </div>
                    <div class="radio">
                        <label><input type="radio" name="assign_contact_to_cec" value="No" onclick="hideTester()"><span class="form-control" style="padding-left: 10px"   >No</span></label>
                    </div>

                </div>



            </fieldset>




            <br>

            <div id="community_tester" class="form-control" style="display: none">

                <fieldset>
                    <legend>
                        Choose Community Tester
                    </legend>
                    <div >
                        <select id="testers_list" name="community_tester_guid" class="form-control js-example-basic-single" style="width: 60%" class="form-control js-example-basic-single" style="width: 50%">
                            <option selected>Choose...</option>
                            <% testers.each { %>
                            <option value="${ui.format(it.community_tester_guid)}">${ui.format(it.username)}</option>
                            <% } %>
                        </select>

                            <input type="hidden" id="community_tester_name_input" name="community_tester_name" />
                    </div>
                </fieldset>
            </div>
            <br>
            <div>
                <input type="submit" value="Add contact" class="btn btn-primary" />
            </div>
            <br>
        </form>
    </div>



</div>




<div id="Contacts_Listing" class="tabcontent">
    ${ ui.includeFragment("patientindextracing", "contactlist") }

</div>


<div id="Assign_Contacts" class="tabcontent">
    
    <h4 style="text-align: center">
                    
                    Assign/Re-assign Contacts 
                    
    </h4><br>
    
    
    ${ ui.includeFragment("patientindextracing", "reassignContact") }

<!--    <form method="post">
           <table cellspacing="0" cellpadding="0">

        

                <tr>


                    <td><label for="contacts">Contacts</label>
                        <select id="contats" name="contacts" class="form-control">
                            <option selected>Choose...</option>
                            <option>Male</option>
                            <option>Female</option>
                        </select> </td>

                    <td>
                        <label for="testers">Testers</label>
                        <select id="testers" name="testers" class="form-control">
                            <option value="" selected="selected" >- Select -</option>
                            <option value='Abia'>Abia</option>
                            <option value='Adamawa'>Adamawa</option>
                            <option value='AkwaIbom'>AkwaIbom</option>
                           
                        </select></td>

                </tr>
          </table>
          
            <br>
            <div>
                <input type="submit" value="Assign" class="btn btn-primary" />
            </div>

            <br><br>
    </form>-->
    
</div>


<div id="Data_Visualization" class="tabcontent">
    ${ ui.includeFragment("patientindextracing", "visualization") }

</div>

</div>

<script>
    
    jQuery(document).ready(function() {
    jQuery('.js-example-basic-single').select2();
});
</script>


<script type="text/javascript">
    jQuery('#testers_list').on('change', function() {
    var communityTesterName = jQuery('#testers_list option:selected').text();
    console.log('tester is '+communityTesterName)
    jQuery('#community_tester_name_input').val(communityTesterName);

    });

    function showTester(){
    jQuery('#community_tester').show(500);

    }

    function hideTester(){
    jQuery('#community_tester').hide(500);

    }


</script>

<script>

    // Get the element with id="defaultOpen" and click on it
    document.getElementById("defaultOpen").click();

    function openTab(evt, tabName) {
    // Declare all variables
    var i, tabcontent, tablinks;

    // Get all elements with class="tabcontent" and hide them
    tabcontent = document.getElementsByClassName("tabcontent");
    for (i = 0; i < tabcontent.length; i++) {
    tabcontent[i].style.display = "none";
    }

    // Get all elements with class="tablinks" and remove the class "active"
    tablinks = document.getElementsByClassName("tablinks");
    for (i = 0; i < tablinks.length; i++) {
    tablinks[i].className = tablinks[i].className.replace(" active", "");
    }

    // Show the current tab, and add an "active" class to the button that opened the tab
    document.getElementById(tabName).style.display = "block";
    evt.currentTarget.className += " active";
    }

</script>

<script>
//set patient uuid
jQuery('#index_patient_id').val(globalPatientID);

</script>