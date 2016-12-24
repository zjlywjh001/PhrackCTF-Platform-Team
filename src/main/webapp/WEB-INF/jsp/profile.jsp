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
            <h1 class="home-title">${ username }&nbsp;<a href="country/${ countrycode }" style="text-decoration:none;"><img src="images/flags/${ countrycode }.png" style="vertical-align:bottom;" data-placement="right" alt="${ countrycode }" title="${ country }" /></a></h1>
            <hr style="border:0;background-color:#d4d4d4;height:1px;" />
        </div>
    </div>
    <div class="news-container">
        <div class="row">
            <div class="span12">
                <form class="form-horizontal">
                    <fieldset>
                        <h3>Score: ${ userscore }, Rank: ${ userrank }</h3>
                        <c:forEach items="${ userstat }" var="ctp">
                        <strong>${ ctp.name }: ${ ctp.proc }/${ ctp.total }(<fmt:formatNumber type="number" value="${ ctp.percent }" maxFractionDigits="1"/>%)</strong>
                        <div class="progress">
                            <div class="progress-bar progress-bar-${ ctp.style } progress-bar-striped" role="progressbar" aria-valuenow="<fmt:formatNumber type="number" value="${ ctp.percent }" maxFractionDigits="1"/>" aria-valuemin="0" aria-valuemax="100" style="min-width: 2em;width: <fmt:formatNumber type="number" value="${ ctp.percent }" maxFractionDigits="1"/>%;">
                                <fmt:formatNumber type="number" value="${ ctp.percent }" maxFractionDigits="1"/>%
                            </div>
                        </div>
                        </c:forEach>
                        <br/>
                        <h3>Total: ${ userscore }/${ totalscore }(${ totalpercent }%)</h3>
                        <div class="progress">
                        <c:forEach items="${ userstat }" var="ctp">
                            <div class="progress-bar progress-bar-${ctp.style} progress-bar-striped"  style="width: <fmt:formatNumber type="number" value="${ctp.percentall}" maxFractionDigits="3"/>%">
                                <span class="sr-only"></span>
                            </div>
                        </c:forEach>
                        </div>
                        <div class="panel panel-default">
                            <div class="panel-heading">
                                <div class="text-muted bootstrap-admin-box-title"><strong>Solved Challenges</strong></div>
                            </div>
                            <div class="bootstrap-admin-panel-content">
                                <div class="table-responsive">
                                    <table class="table table-striped table-hover">
                                        <thead>
                                            <tr>
                                                <th>Challenge</th>
                                                <th>Category</th>
                                                <th>Score</th>
                                                <th>Time</th>
                                            </tr>
                                        </thead>
                                        <tbody>
                                        <c:forEach items="${ passtask }" var="ch">
                                            <tr>
                                                <td><a class="text-muted" style="text-decoration: none;" href="challenges?taskid=${ ch.id }" target="_blank">${ ch.title }</a></td>
                                                <td><span class="label label-${ ch.catemark } label-as-badge">${ ch.catename }</span></td>
                                                <td>${ ch.score }</td>
                                                <td><fmt:formatDate value="${ ch.passtime }" pattern="yyyy-MM-dd HH:mm"/></td>
                                            </tr>
                                        </c:forEach>
                                        </tbody>
                                    </table>
                                </div>
                            </div>
                        </div>
                        <h3>Organization</h3>
                        <div>
                            <p>${ organize }</p>
                        </div>
                        <h3>Personal Description</h3>
                        <div>
                            <p>${ userdec }</p>
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