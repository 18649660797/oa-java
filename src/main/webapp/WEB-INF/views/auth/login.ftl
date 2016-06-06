<!DOCTYPE html>
<html>
<head>
    <title>OA Beta</title>
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <!-- 新 Bootstrap 核心 CSS 文件 -->
    <link rel="stylesheet" href="//cdn.bootcss.com/bootstrap/3.3.5/css/bootstrap.min.css">

    <!-- 可选的Bootstrap主题文件（一般不用引入） -->
    <link rel="stylesheet" href="//cdn.bootcss.com/bootstrap/3.3.5/css/bootstrap-theme.min.css">

    <!-- jQuery文件。务必在bootstrap.min.js 之前引入 -->
    <script src="//cdn.bootcss.com/jquery/1.11.3/jquery.min.js"></script>

    <!-- 最新的 Bootstrap 核心 JavaScript 文件 -->
    <script src="//cdn.bootcss.com/bootstrap/3.3.5/js/bootstrap.min.js"></script>

</head>
<body>

<div class="container">

    <form class="form-signin" action="/j_admin_login" method="post">
        <h2 class="form-signin-heading">OA Beta</h2>
        <label for="inputEmail" class="sr-only">Email address</label>
        <input type="text" id="inputPassword" name="j_username" class="form-control" placeholder="管理员名称/考勤号" required="true" autofocus="">
        <label for="inputPassword" class="sr-only">Password</label>
        <input type="password" id="inputPassword" class="form-control" placeholder="管理员密码/考勤号" required="true" name="j_password" data-rules="{required:true}">
        <div class="checkbox">
            <label>
                <input checked type="checkbox" name="_spring_security_remember_me"> Remember me
            </label>
        </div>
        <button class="btn btn-lg btn-primary btn-block" type="submit">登录</button>
    </form>

</div> <!-- /container -->
</body>
<script>

</script>
</body>
</html>
