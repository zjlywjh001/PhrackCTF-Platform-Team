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
            <h1 class="home-title">All Users</h1>
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
                                <div class="text-muted bootstrap-admin-box-title"><strong>User List</strong></div>
                            </div>
                            <div class="bootstrap-admin-panel-content">
                                <div class="table-responsive">
                                    <table class="table table-striped table-hover">
                                        <thead>
                                            <tr>
                                                <th>Username</th>
                                                <th>Email</th>
                                                <th>Reg time</th>
                                                <th>Last Active</th>
                                                <th>Class</th>
                                                <th>Enabled</th>
                                                <th>IPs</th>
                                                <th>Operation</th>
                                            </tr>
                                        </thead>
                                        <tbody>
                                       		<c:forEach items="${userlist}" var="user">
                                            <tr>
                                                <td><img src="images/flags/${user.countrycode}.png" data-placement="right" alt="cn" title="${ user.countryname }" />&nbsp;&nbsp;${ user.username }</td>
                                                <td>${ user.email }</td>
                                                <td><fmt:formatDate value="${ user.regtime }" pattern="yyyy-MM-dd HH:mm" /></td>
                                                <td><fmt:formatDate value="${ user.lastactive }" pattern="yyyy-MM-dd HH:mm" /></td>
                                                <td>${ user.role }</td>
                                                <td>
                                                <c:if test="${ user.isenabled }">
                                                <span class="glyphicon glyphicon-ok" style="color:#00ff00;" title="Enable"></span>
                                                </c:if>
                                                <c:if test="${ !user.isenabled }">
                                                <span class="glyphicon glyphicon-remove" style="color:#ff0000;" title="Banned"></span>
                                                </c:if>
                                                </td>
                                                <td>${ user.ips }</td>
                                                <td><a class="btn btn-primary btn-xs" href="admin/mails?target=${ user.id }" role="button" target="_blank">MailTo</a>&nbsp;<a class="btn btn-default btn-xs" href="admin/edituser/${ user.id }" role="button" target="_blank">Edit</a></td>
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