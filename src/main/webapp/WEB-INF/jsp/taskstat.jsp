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
    <%@ include file="top.jsp"%>
</head>

<body>
    <%@ include file="nav.jsp"%>

    <div class="news-container">
        <div class="row">
            <h1 class="home-title">Solve Status of ${ taskname }</h1>
            <hr style="border:0;background-color:#d4d4d4;height:1px;" />
        </div>
    </div>

    <div class="news-container">
        <div class="row">
            <div class="span12">
                <form class="form-horizontal">
                    <fieldset>
                        <div class="panel panel-default">
                            <div class="panel-heading">
                                <div class="text-muted bootstrap-admin-box-title"><strong>Solver List</strong></div>
                            </div>
                            <div class="bootstrap-admin-panel-content">
                                <div class="table-responsive">
                                    <table class="table table-striped table-hover" style="table-layout:fixed;word-break:break-all">
                                        <thead>
                                            <tr>
                                                <th>Teamname</th>
                                                <th>Submits</th>
                                                <th>Time</th>
                                            </tr>
                                        </thead>
                                        <tbody>
                                            <c:forEach items="${ passtable }" var="pt">
                                            <tr <c:if test="${ pt.order==1 }">class="danger"</c:if>>
                                            	<td><c:if test="${ pt.order==1 }"><img src="images/fb.png" title="First Blood" width="20" height="20"/>&nbsp;&nbsp;&nbsp;</c:if><a class="text-muted"
													style="text-decoration: none;" href="teaminfo/${ pt.solverid }" target="_blank">${ pt.solvername }</a>&nbsp;&nbsp;<a target="_blank" href="country/${ pt.countrycode }" style="text-decoration:none;">
													<img src="images/flags/${ pt.countrycode }.png" data-placement="right" alt="${ pt.countrycode }" title="${ pt.countryname }" /></a></td>
                                                <td>${ pt.submits }</td>
                                                <td><fmt:formatDate value="${ pt.time }" pattern="yyyy-MM-dd HH:mm:ss" /></td>
                                            </tr>
                                        	</c:forEach>
                                        </tbody>
                                    </table>
                                </div>
                            </div>
                        </div>        
                    </fieldset>
                </form>
            </div>
        </div>
    </div>
    <br/>


    <br/>

    <%@ include file="footer.jsp"%>
</body>

</html>