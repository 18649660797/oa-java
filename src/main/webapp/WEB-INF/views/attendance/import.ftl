<!DOCTYPE html>
<html>
<head>
<#include "../include/resource.ftl"/>
</head>
<body>
<form id="J_Form" action="/index.php/home/attendance/import" method="post" enctype="multipart/form-data" class="form-horizontal">
    <div class="control-group">
        <label class="control-label">考勤初始文件：</label>
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
                srcNode : '#J_Form'
            }).render();
        });
    } (jQuery));
</script>
</body>
</html>