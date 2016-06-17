<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="shiro" uri="http://shiro.apache.org/tags"%>
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
            <h1 class="home-title">Challenges</h1>
        </div>
    </div>
    <br/>

    <div class="news-container">
        <div class="row">
            <div class="span12">
                <ul class="nav nav-tabs">
                	<li role="presentation" class="filter" data-filter="all"><a href="${ ctrlname }${ nowtask }#">All</a></li>
                	<c:forEach items="${cates}" var="c">
                    <li role="presentation" class="filter" data-filter=".${c.name}"><a href="${ ctrlname }${ nowtask }#">${c.name}</a></li>
                    </c:forEach>
                </ul>
            </div>
        </div>
    </div>
    <br/>
    <div class="news-container">
        <div class="row">
            <div id="Container" class="mix-container">
                <div class="panel-group" id="accordion" role="tablist" aria-multiselectable="false">
                		<c:forEach items="${tasklist}" var="task">
                        <div id="panel-task${ task.id }" class="panel panel-${task.stat} bootstrap-admin-no-table-panel mix ${task.cate}" data-category="${task.cate}" >
                            <div class="panel-heading" id="heading${ task.id }">
                                <a role="button" data-toggle="collapse" data-parent="#accordion" href="#task-${ task.id }" 
                                aria-expanded="false" class="text-muted bootstrap-admin-box-title"
                                <c:if test="${task.isnew}">
                         			onclick="taskviewed(${ task.id },${ task.solved });"    
                        		</c:if>
                                >
                                <strong>${ task.title }</strong>
                                <c:if test="${task.isnew}">
                                <font id="new-${ task.id }" color="#d80000">New!</font>
                                </c:if>
                                </a>
                                <div class="pull-right "><span class="badge">${ task.score }</span>&nbsp;&nbsp;&nbsp;<span class="label label-${task.catestyle} label-as-badge">${task.cate}</span></div>
                            </div>
                            <div id="task-${ task.id }" class="panel-collapse collapse ${ task.in }" role="tabpanel" aria-labelledby="heading${ task.id }">
                                <div class="panel-body">
                                    <p>
                                        <strong>Solved: <span id="solves-task${ task.id }">${ task.solvenum }</span></strong>&nbsp;&nbsp;(<a href="taskstat/${ task.id }" style="text-muted" target="_blank">Show Solvers List</a>)
                                    </p>
                                    <p>
                                        ${ task.content }
                                    </p>
                                    <c:forEach items="${ task.hints }" var="hint">
                                    <p>
                                        Hint${ hint.label }: ${ hint.content }
                                    </p>
                                    </c:forEach>
                                    <c:forEach items="${task.attach}" var="att">
                                    <p><a href="${ att.url }">${ att.name }</a></p>
                                    </c:forEach>
                                    <hr>
                                    <div id="result${ task.id }"></div>
                                    <c:if test="${ !task.fin }">
                                    <c:if test="${ task.solved!=true }">
                                    <div class="input-group input-float">
                                        <input class="form-control" id="flag-task${ task.id }" type="text" placeholder="FLAG" />&nbsp;
                                    </div>
                                    <button id="task${ task.id }-btn" type="button" class="btn btn-default" onclick="submitflag(${ task.id });">Submit</button>
                                    </c:if>
                                    <c:if test="${ task.solved }">
                                    	<div class="alert alert-success" role="alert">Your Team have solved this Challenge!</div>
                                    </c:if>
                                    </c:if>
                                    <c:if test="${ task.fin }">
                                    	<div class="alert alert-warning" role="alert">The CTF is over.</div>
                                    </c:if>
                                </div>
                            </div>
                        </div>
                        </c:forEach>
                </div>
            </div>
        </div>
    </div>

    <%@ include file="footer.jsp"%>
    <script src="assets/scripts.js"></script>
    <form:form></form:form>
</body>

</html>