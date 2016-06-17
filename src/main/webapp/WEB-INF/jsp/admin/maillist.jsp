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
</head>

<body>
    <div class="news-container">
        <div class="row">
            <div class="span12">
                <form class="form-horizontal">
                    <fieldset>
                        <div class="panel panel-default">
                            <div class="panel-heading">
                                <div class="text-muted bootstrap-admin-box-title"><strong>Select Receiver |&nbsp;<a class="btn btn-primary btn-xs" href="javascript:selectdone();" role="button">OK</a></strong></div>
                            </div>
                            <div class="bootstrap-admin-panel-content">
                                <div class="table-responsive">
                                    <table class="table table-striped table-hover" style="table-layout:fixed;word-break:break-all">
                                        <thead>
                                            <tr>
                                            	<th>Select</th>
                                            	<th>id</th>
                                                <th>Username</th>
                                                <th>Email Address</th>
                                            </tr>
                                        </thead>
                                        <tbody>
                                        	<tr>
                                        		<td><input type="checkbox" name="checkall"></td>
                                        		<td></td>
                                        		<td></td>
                                        		<td>Select All</td>
                                        	<tr>
                                            <c:forEach items="${ userlist }" var="u">
                                            <tr>
                                            	<td><input name="emailcheck" type="checkbox" value="${ u.email }"></td>
                                            	<td>${ u.id }</td>
                                                <td>${ u.username }</td>
                                                <td>${ u.email }</td>
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
    
    <script type="text/javascript">
    	$(document).ready(function (){
    		$("input[name='checkall']").click(function (){
    			var ischeck = $(this).is(':checked');
    			if (ischeck) {
    				$("input[name='emailcheck']").each(function(){
    					$(this).prop("checked",true); 
    				});
    			} else {
    				$("input[name='emailcheck']").each(function(){
    					$(this).prop("checked",false); 
    				});
    			}
    		});
    	}); 
    	function selectdone(){
    		var maillist = '';
    		$("input[name='emailcheck']").each(function(){
    			if ($(this).is(':checked')) {
    				if (maillist=='') {
        				maillist = $(this).val();
        			} else {
        				maillist = maillist+ ',' + $(this).val();
        			}
    			}	
			});
    		$('#receiver',window.opener.document).val(maillist);
    		window.close();
    	}
    </script>
</body>

</html>