<style>
#container {
    min-width: 320px;
    max-width: 600px;
    margin: 0 auto;
}

</style>
<%= ui.resourceLinks() %>
<% ui.includeJavascript("patientindextracing", "highcharts.js") %>
<% ui.includeJavascript("patientindextracing", "export-data.js") %>
<% ui.includeJavascript("patientindextracing", "exporting.js") %>
<% ui.includeJavascript("patientindextracing", "series-label.js") %>

<div id="container">
<button id="plain">Plain</button>
<button id="inverted">Inverted</button>
<button id="polar">Polar</button>
<br>
<br>
</div>

<div id="container-3d-pie" style="height: 400px"></div>
<br>
<br>
<div id="container-line" style="min-width: 310px; height: 400px; margin: 0 auto"></div>
<br>
<br>
<div id="container-grouped" style="min-width: 310px; height: 400px; margin: 0 auto"></div>

    
    <script type="text/javascript">

    jq.ajax({
        url: "${ ui.actionLink("patientindextracing", "ndr", "visualizeYieldPerLocation") }",
    dataType: "json",
    data: {
    'patientUUID': patientId
    }
    

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
        url: "${ ui.actionLink("patientindextracing", "ndr", "visualizeYieldPerSex") }",
    dataType: "json",
    data: {
    'patientUUID': patientId
    }
    

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
        url: "${ ui.actionLink("patientindextracing", "ndr", "visualizeYieldPerAssign") }",
    dataType: "json",
    data: {
    'patientUUID': patientId
    }
    

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

        series: [{
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