<link rel="stylesheet" href="/static/css/error.css" type="text/css"/>
<div id="error-wrapper">
    <div id="error-message">
        ${message}
    </div>
</div>
<div id="normal-wrapper">
    <div id="normal-message">
        ${message}
    </div>
</div>
<script type="text/javascript">
    $(function(){callError("Sample Error Message");})
    $(function(){callNormal("Sample Normal Message");})
</script>

        