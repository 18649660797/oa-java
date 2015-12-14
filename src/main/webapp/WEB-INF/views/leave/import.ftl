<!DOCTYPE html>
<html>
<head>
<#include "../include/resource.ftl"/>
</head>
<body>
<form id="J_Form" action="/leave/import" method="post" enctype="multipart/form-data" class="form-horizontal">
    <div class="control-group">
        <div class="controls">
            <input type="file" name="file" id="file" data-rules="{required:true}" /><br>
        </div>
    </div>
</form>
<script>
    (function($) {
        $(function() {
            var Form = BUI.Form;
            new Form.Form({
                srcNode : '#J_Form',
            }).render();
        });
    } (jQuery));
</script>
</body>
</html>