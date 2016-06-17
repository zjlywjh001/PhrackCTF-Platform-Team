<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="shiro" uri="http://shiro.apache.org/tags"%>
<% 	String queryString = request.getQueryString(); %>
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
                    <c:if test="${ctrlname=='admin/manage'}">
                    class="active"
                    </c:if>
                    >
                        <a href="admin/manage">Manage
                                </a>
                    </li>
                    </shiro:hasRole>

                </ul>
                <shiro:guest>
                <ul class="nav navbar-nav navbar-right navbar-collapse">
                    <li>
                        <a href="login"> Sign in
                                </a>
                    </li>

                    <li>
                        <a href="register"> Sign up
                                </a>
                    </li>
                </ul>
                </shiro:guest>
                <shiro:user>
                <ul class="nav navbar-nav navbar-right navbar-collapse">
                    <li class="dropdown">
                    	<shiro:hasRole name="admin">
                    	<a href="${ctrlname}<%=(queryString!=null?("?"+queryString):"")%>#" class="dropdown-toggle" data-toggle="dropdown" role="button" aria-haspopup="true" aria-expanded="false"><i class="icon-user"></i> ${username_top} <strong>(Score:<span id="userscore">${score}</span> Rank:<span id="userrank">${rank}</span>)</strong><span class="caret"></span></a>
                    	</shiro:hasRole>
                    	<shiro:hasAnyRoles name="user,leader">
                        <a href="${ctrlname}<%=(queryString!=null?("?"+queryString):"")%>#" class="dropdown-toggle" data-toggle="dropdown" role="button" aria-haspopup="true" aria-expanded="false"><i class="icon-${ usericon }"></i> ${username_top} <strong>(Score:<span id="userscore">${score}</span> Rank:<span id="userrank">${rank}</span>)</strong><span class="caret"></span></a>
                        </shiro:hasAnyRoles>
                        <ul class="dropdown-menu">
                        	<shiro:hasAnyRoles name="user,leader">
                        	<li><a href="myteam">My Team</a></li>
                        	</shiro:hasAnyRoles>
                            <li><a href="dashboard">Dashboard</a></li>
                            <li role="separator" class="divider"></li>
                            <li><a href="logout">Logout</a></li>
                        </ul>

                    </li>
                </ul>
                </shiro:user>
            </div>
            <!--/.nav-collapse -->

        </div>
    </nav>
 