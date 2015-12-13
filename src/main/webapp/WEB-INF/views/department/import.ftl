<!DOCTYPE html>
<html>
<head>
    <meta charset="utf-8"/>
<#include "../include/resource.ftl"/>
</head>
<body>
<form id="saveForm" action="/department/import" method="post" enctype="multipart/form-data" class="form-horizontal">
    <div class="tips tips-small tips-info">
        <span class="x-icon x-icon-small x-icon-info"><i class="icon icon-white icon-info"></i></span>
        <div class="tips-content">用初始的考勤文件做导入</div>
    </div>
    <div class="control-group">
        <div class="controls">
            <input type="file" name="csvFile" id="file" data-rules="{required:true}" /><br>
        </div>
    </div>
</form>
<script>
    (function($) {
        $(function() {
            var Form = BUI.Form;
            new Form.Form({
                srcNode : '#saveForm',
            }).render();
        });
    } (jQuery));
</script>
</body>
</html>