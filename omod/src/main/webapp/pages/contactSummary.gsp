<% ui.decorateWith("appui", "standardEmrPage") %>

<%= ui.resourceLinks() %>
<% ui.includeJavascript("patientindextracing", "series-label.js") %>

<script type="text/javascript">
    jq = jQuery.noConflict();
</script>

<div class="row wrapper  white-bg page-heading"  style="">
    <div class="col-lg-8 offset-lg-2">

        <div class="panel panel-flat">

            <div class="panel-heading" style="padding:10px 20px">

 ${ ui.includeFragment("patientindextracing", "contactSummaryViz") }          
            </div>

        </div>
        

    </div>
</div>