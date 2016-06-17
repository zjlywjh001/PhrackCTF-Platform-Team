<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form" %>  
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
    <%@ include file="../top.jsp"%>
    <script type="text/javascript" src="//code.jquery.com/jquery-2.0.3.min.js"></script>
    <link rel="stylesheet" href="vendors/jquery-confirm/jquery-confirm.min.css" media="screen">
</head>

<body>
    <%@ include file="../nav.jsp"%>

    <div class="news-container">
        <div class="row">
            <h1 class="home-title">All Teams</h1>
            <hr style="border:0;background-color:#d4d4d4;height:1px;" />
        </div>
    </div>

    <div class="news-container">
        <div class="row">
            <div class="span12">
                <form:form class="form-horizontal">
                    <fieldset>
                         <div class="panel panel-default">
                            <div class="panel-heading">
                                <div class="text-muted bootstrap-admin-box-title"><strong>Team List</strong></div>
                            </div>
                            <div class="bootstrap-admin-panel-content">
                                <div class="table-responsive">
                                    <table class="table table-striped table-hover">
                                        <thead>
                                            <tr>
                                                <th>Teamname</th>
                                                <th>Creator</th>
                                                <th>Create time</th>
                                                <th>Last Solve</th>
                                                <th>Enabled</th>
                                                <th>IPs</th>
                                                <th>Operation</th>
                                            </tr>
                                        </thead>
                                        <tbody>
                                       		<c:forEach items="${teamlist}" var="team">
                                            <tr>
                                                <td><img src="images/flags/${team.countrycode}.png" data-placement="right" alt="cn" title="${ team.countryname }" />&nbsp;&nbsp;${ team.name }</td>
                                                <td>${ team.creator }</td>
                                                <td><fmt:formatDate value="${ team.createtime }" pattern="yyyy-MM-dd HH:mm" /></td>
                                                <td><fmt:formatDate value="${ team.getLastSubmit() }" pattern="yyyy-MM-dd HH:mm" /></td>
                                                <td>
                                                <c:if test="${ team.isenabled }">
                                                <span class="glyphicon glyphicon-ok" style="color:#00ff00;" title="Enable"></span>
                                                </c:if>
                                                <c:if test="${ !team.isenabled }">
                                                <span class="glyphicon glyphicon-remove" style="color:#ff0000;" title="Banned"></span>
                                                </c:if>
                                                </td>
                                                <td>${ team.ips }</td>
                                                <td><a class="btn btn-default btn-xs" href="admin/editteam/${ team.id }" role="button" target="_blank">Edit</a></td>
                                            </tr>
                                            </c:forEach>
                                        </tbody>
                                    </table>
                                </div>
                            </div>
                        </div>        
                    </fieldset>
                </form:form>
            </div>
        </div>
    </div>
    <br/>


    <br/>

    <%@ include file="footer-admin.jsp"%>
    <script type="text/javascript" src="vendors/jquery-confirm/jquery-confirm.min.js"></script>
</body>

</html>