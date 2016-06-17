<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="shiro" uri="http://shiro.apache.org/tags"%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form" %>  

<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://"
			+ request.getServerName() + ":" + request.getServerPort()
			+ path + "/";
%>
<!DOCTYPE html>
<html lang="zh-CN">
<head>
	<base href="<%=basePath%>">
    <meta charset="UTF-8">
    <%@ include file="top.jsp"%>
    <style type="text/css">
    	.alert{
    		max-width:400px;
    		margin:0 auto 20px;
    	}
    </style>
</head>

<body>
    <%@ include file="nav.jsp"%>
    
    <div class="news-container">
        <div class="row">
        <div class="span12">
            <div class="login">
                    <c:forEach items="${stat}" var="s">
                    <div class="alert alert-${s.type}">
                        <a class="close" data-dismiss="alert" href="#">&times;</a>
                        ${s.msg}
                    </div>
                    </c:forEach>
                    <form:form method="post" action="resetpass" class="bootstrap-admin-login-form">
                        <h1>Reset Password</h1>
                        <br>
                        <div class="form-group">
                            <input class="form-control" type="text" name="email" placeholder="E-mail">
                        </div>
                        <div class="form-group">
                            <input class="form-control" style="width:233px;float:left;" type="text" name="captcha" placeholder="Captcha"><img id="capimg" style="display:inline;" src="captcha.jpg" />
                        </div>
                        <hr>
                        
                        <button class="btn btn-lg btn-default" type="submit">Submit</button>
                    </form:form>
            </div>
        </div>
        </div>
    </div>
    

    <%@ include file="footer.jsp"%>
    <script type="text/javascript">
    $(document).ready(  
    		  function (){  
    		      $('#capimg').click(  
    		          function (){  
    		                $(this).attr('src', "captcha.jpg"+'?'+Math.floor(Math.random() * 100));
    		          }  
    		      );  
    		        
    		  }  
    		);      
    </script>
</body>

</html>