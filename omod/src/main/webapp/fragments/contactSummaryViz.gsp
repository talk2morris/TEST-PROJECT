<%
def id = config.id
%>
<%= ui.resourceLinks() %>
<style>
.my-card
{
    position:absolute;
    left:40%;
    top:-20px;
    border-radius:50%;
}
  #container {
    min-width: 320px;
    max-width: 600px;
    margin: 0 auto;
}
    
</style>
<% ui.includeCss("patientindextracing", "bootstrap.min.css") %>
<% ui.includeJavascript("patientindextracing", "bootstrap.min.js") %>
<% ui.includeJavascript("patientindextracing", "jquery.js") %>
<% ui.decorateWith("appui", "standardEmrPage") %>
<% ui.includeJavascript("patientindextracing", "highcharts.js") %>
<% ui.includeJavascript("patientindextracing", "export-data.js") %>
<% ui.includeJavascript("patientindextracing", "exporting.js") %>
<% ui.includeJavascript("patientindextracing", "series-label.js") %>
<% ui.includeJavascript("patientindextracing", "jquery.js") %>


<div class="jumbotron" style="height: 80%; width: 150%; margin-left: -150px; margin-top: 30px">
<div class="row w-100" style="width: 100%">
        <div class="col-md-4" style="max-width: 100%">
            <div class="card border-info mx-sm-1 p-3">
                <div class="card border-info shadow text-info p-3 my-card" ><span class="icon-user" aria-hidden="true"></span></div>
                <div class="text-info text-center mt-3"><h4>Total Patients</h4></div>
                <div class="text-info text-center mt-2" id="total-patient"><h1>0</h1></div>
            </div>
        </div>
        <div class="col-md-4" style="max-width: 100%">
            <div class="card border-success mx-sm-1 p-3">
                <div class="card border-success shadow text-success p-3 my-card"><span class="icon-group" aria-hidden="true"></span></div>
                <div class="text-success text-center mt-3"><h4>Total Contacts</h4></div>
                <div class="text-success text-center mt-2" id="total-contact"><h1>0</h1></div>
            </div>
        </div>
        <div class="col-md-4" style="max-width: 100%">
            <div class="card border-danger mx-sm-1 p-3">
                <div class="card border-danger shadow text-danger p-3 my-card" ><span class="icon-retweet" aria-hidden="true"></span></div>
                <div class="text-danger text-center mt-3"><h4>Total Contacts Traced</h4></div>
                <div class="text-danger text-center mt-2" id="total-contact-traced"><h1>0</h1></div>
            </div>
        </div>     
     </div>
</div>

<div id="container"></div>
<button id="plain">Plain</button>
<button id="inverted">Inverted</button>
<button id="polar">Polar</button>
<br>

<div id="container-3d-pie" style="height: 400px"></div>
<br>

<div id="container-grouped" style="min-width: 310px; height: 400px; margin: 0 auto"></div>

    
    <script type="text/javascript">
        
      jq.ajax({
        url: "${ ui.actionLink("patientindextracing", "ndr", "getContactSummary") }",
    dataType: "json"
    
    }).success(function(data) {
    
    console.log(data);
     
    var result = jq.parseJSON(data);
    loadContactSummary(result);
    
    })
    .error(function(xhr, status, err) {
    alert('An error occured retrieving yield per location');

    });     
   
    
    // yield per location
    
    jq.ajax({
        url: "${ ui.actionLink("patientindextracing", "ndr", "visualizeAllYieldPerLocation") }",
    dataType: "json"

    }).success(function(data) {
    
    console.log(data);
     
    var result = jq.parseJSON(data);
    loadYieldPerLocation(result);
    
    })
    .error(function(xhr, status, err) {
    alert('An error occured retrieving yield per location');

    }); 

 

</script>
    
 <script type="text/javascript">
   
    jq.ajax({
        url: "${ ui.actionLink("patientindextracing", "ndr", "visualizeAllYieldPerSex") }",
    dataType: "json"
    
    

    }).success(function(data) {
    
    console.log(data);
     
    var result = jq.parseJSON(data);
    loadSexAggregate(result);
    
    })
    .error(function(xhr, status, err) {
    alert('An error occured sex aggregate chart');

    }); 

 

</script>

 <script type="text/javascript">
   
    jq.ajax({
        url: "${ ui.actionLink("patientindextracing", "ndr", "visualizeAllYieldPerAssign") }",
    dataType: "json"
 
    }).success(function(data) {
    
    console.log(data);
     
    var result = jq.parseJSON(data);
    loadAssignMixChart(result);
    
    })
    .error(function(xhr, status, err) {
    alert('An error occured sex aggregate chart');

    }); 

</script>
    
<script type="text/javascript">
    
    
    function loadContactSummary(summaryResponse){
        jq('#total-patient').text(summaryResponse.totalPatient);
        jq('#total-contact').text(summaryResponse.totalPatientContacts);
         jq('#total-contact-traced').text(summaryResponse.totalContactTraced);
    }
    
    
    
    function loadYieldPerLocation(resultData){
  var chart =  Highcharts.chart('container', {

        title: {
            text: 'Yield by Location'
        },

        subtitle: {
            text: 'LGA'
        },

        xAxis: {
            categories: resultData.locations
        },
  tooltip: {
        headerFormat: '<span style="font-size:11px">{series.name}</span><br>',
        pointFormat: '<span style="color:{point.color}">{point.name}</span>: <b>{point.y}</b>'
    },
        series: [{
            name:'LGA',
            type: 'column',
            colorByPoint: true,
            data: resultData.yieldSum,
            showInLegend: false
        }]

    });
    
    
    jQuery('#plain').click(function () {
        chart.update({
            chart: {
                inverted: false,
                polar: false
            },
            subtitle: {
                text: 'Plain'
            }
        });
    });

    jQuery('#inverted').click(function () {
        chart.update({
            chart: {
                inverted: true,
                polar: false
            },
            subtitle: {
                text: 'Inverted'
            }
        });
    });

    jQuery('#polar').click(function () {
        chart.update({
            chart: {
                inverted: false,
                polar: true
            },
            subtitle: {
                text: 'Polar'
            }
        });
    });
    
    }


function loadSexAggregate(resultData){
       Highcharts.chart('container-3d-pie', {
        chart: {
            type: 'pie',
            options3d: {
                enabled: true,
                alpha: 45,
                beta: 0
            }
        },
        title: {
            text: 'Yield by Sex'
        },
        tooltip: {
            pointFormat: '{series.name}: <b>{point.percentage:.1f}%</b>'
        },
        plotOptions: {
            pie: {
                allowPointSelect: true,
                cursor: 'pointer',
                depth: 35,
                dataLabels: {
                    enabled: true,
                    format: '{point.name}'
                }
            }
        },
        series: [{
            type: 'pie',
            name: 'Sex share',
            data: [
                resultData.maleList,
                resultData.femaleList
            ]
        }]
    });
}


function loadAssignMixChart(resultData){

   Highcharts.chart('container-grouped', {

        chart: {
            type: 'column'
        },

        title: {
            text: 'Number (Assigned, Traced) by sex and location'
        },

        xAxis: {
            categories: ['Male', 'Female']
        },

        yAxis: {
            allowDecimals: false,
            min: 0,
            title: {
                text: 'Number of Contact'
            }
        },

        tooltip: {
            formatter: function () {
                return '<b>' + this.x + '</b><br/>' +
                    this.series.name + ': ' + this.y + '<br/>' +
                    'Total: ' + this.point.stackTotal;
            }
        },

        plotOptions: {
            column: {
                stacking: 'normal'
            }
        },

        series: [{
            name: 'Assigned',
            data: resultData.assignYieldBySex,
            stack: 'male'
        }, {
            name: 'Unassigned',
            data: resultData.unassignYieldBySex,
            stack: 'male'
        }, {
            name: 'Traced',
            data: resultData.tracedYieldBySex,
            stack: 'female'
        }, {
            name: 'Untraced',
            data: resultData.untracedYieldBySex,
            stack: 'female'
        }]

    });

}
</script>



