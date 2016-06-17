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
            <h1 class="home-title">${ countryname }&nbsp;<img src="images/flags/${ countrycode }.png" style="vertical-align:bottom;" data-placement="right" alt="${ countrycode }" title="${ countryname }" /></h1>
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
                                <div class="text-muted bootstrap-admin-box-title"><strong>Members</strong></div>
                            </div>
                            <div class="bootstrap-admin-panel-content">
                                <div class="table-responsive">
                                    <table class="table table-striped table-hover">
                                        <thead>
                                            <tr>
                                                <th>Username</th>
                                                <th>Country</th>
                                                <th>Score</th>
                                                <th>Rank</th>

                                            </tr>
                                        </thead>
                                        <tbody>
                                        	<c:forEach items="${ country_users }" var="u">
                                            <tr>
                                                <td><a class="text-muted" style="text-decoration: none;" href="teaminfo/${ u.id }">${ u.name }</a></td>
                                                <td><img src="images/flags/${ countrycode }.png" style="vertical-align:bottom;" data-placement="right" alt="${ countrycode }" title="${ countryname }" /></td>
                                                <td>${ u.score }</td>
                                                <td>${ u.rank }</td>
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
    <br/>


    <%@ include file="footer.jsp"%>
</body>

</html>