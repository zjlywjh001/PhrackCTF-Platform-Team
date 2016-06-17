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
	String queryString = request.getQueryString();
%>
<!DOCTYPE html>
<html lang="zh-CN">
<head>
	<base href="<%=basePath%>">
    <meta charset="UTF-8">
    <%@ include file="top.jsp"%>
    <meta http-equiv="Refresh" content="5; url=<%=basePath%>${nextpage}" />
    <style type="text/css">
    	.alert{
    		max-width:70%;
    		margin:0 auto 20px;
    	}
    </style>
</head>

<body>
    <nav class="navbar navbar-default navbar-fixed-top">
        <div class="container-fluid">
            <div class="navbar-header">
                <button type="button" class="navbar-toggle collapsed" data-toggle="collapse" data-target="#navigator" aria-expanded="false">
                    <span class="sr-only">Toggle navigation</span>
                    <span class="icon-bar"></span>
                    <span class="icon-bar"></span>
                    <span class="icon-bar"></span>
                </button>
                <a class="navbar-brand" href="${ctrlname}<%=(queryString!=null?("?"+queryString):"")%>#">phrackCTF</a>
            </div>

            <div class="collapse navbar-collapse" id="navigator">
                <ul class="nav navbar-nav">
                	
                    <li 
                    <c:if test="${ctrlname=='home'}">
                    class="active"
                    </c:if>
                    >
                        <a href="home">Home <span class="sr-only">(current)</span></a>
                    </li>
                    <shiro:user>
                    <li 
                    <c:if test="${ctrlname=='challenges'}">
                    class="active"
                    </c:if>
                    >
                        <a href="challenges">Challenges

                                </a>
                    </li>
                    </shiro:user>
                    <li 
                    <c:if test="${ctrlname=='rules'}">
                    class="active"
                    </c:if>
                    >
                        <a href="rules">Rules</a>
                    </li>
                    <li 
                    <c:if test="${ctrlname=='scoreboard'}">
                    class="active"
                    </c:if>
                    >
                        <a href="scoreboard">Scoreboard
                                </a>
                    </li>
                    <shiro:hasRole name="admin">
                    <li 
                    <c:if test="${ctrlname=='manage'}">
                    class="active"
                    </c:if>
                    >
                        <a href="admin/manage">Manage
                                </a>
                    </li>
                    </shiro:hasRole>

                </ul>
            </div>
            <!--/.nav-collapse -->

        </div>
    </nav>
    
    <div class="news-container">
        <div class="row">
        <div class="span12">
            <div class="login">
            <c:forEach items="${stat}" var="s">
            	<div class="alert alert-${s.type}">
                	${s.msg}
            	</div>
            </c:forEach>
            </div>
        </div>
        </div>
    </div>
    

    <%@ include file="footer.jsp"%>
</body>

</html>