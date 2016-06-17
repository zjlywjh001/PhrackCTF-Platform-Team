<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
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
</head>

<body>
 	<%@ include file="nav.jsp"%>
    <div class="news-container">
        <div class="row">
        <h1 class="home-title">News</h1>
        <hr style="border:0;background-color:#d4d4d4;height:1px;" />
        </div>
    </div>
    
    <c:forEach items="${newslist}" var="news">
    <div class="news-container">
        <div class="row">
        <div class="span12">
            <div class="block news-main">
                <h2 class="news-title">${news.title}</h2>
                <p class="news-meta"><i class="glyphicon glyphicon-time" style="font-size:16px;"></i>&nbsp;&nbsp;<a href="${ctrlname}#"><fmt:formatDate value="${news.posttime}" pattern="yyyy-MM-dd HH:mm:ss"/></a></p>
                <hr>
                ${news.content}
            </div>
        </div>
        </div>
    </div>
    </c:forEach>

    <%@ include file="footer.jsp"%>
</body>

</html>