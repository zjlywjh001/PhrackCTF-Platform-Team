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
            <h1 class="home-title">User Submissions</h1>
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
                                <div class="text-muted bootstrap-admin-box-title"><strong>All Submissions of ${ currentuser.username }</strong></div>
                            </div>
                            <div class="bootstrap-admin-panel-content">
                                <div class="table-responsive">
                                    <table class="table table-striped table-hover" style="table-layout:fixed;word-break:break-all">
                                        <thead>
                                            <tr>
                                                <th>Challenge</th>
                                                <th>Username</th>
                                                <th>Submit Time</th>
                                                <th>Content</th>
                                                <th>Correct</th>
                                                <th>Manage</th>
                                            </tr>
                                        </thead>
                                        <tbody>
                                            <c:forEach items="${submits}" var="sb">
                                            <tr>
                                                <td>${ sb.taskname }</td>
                                                <td>${ sb.user }</td>
                                                <td><fmt:formatDate value="${ sb.submitTime }" pattern="yyyy-MM-dd HH:mm" /></td>
                                                <td>${ sb.content }</td>
                                                <td>
                                                <c:if test="${ sb.correct }">
                                                <span class="glyphicon glyphicon-ok" style="color:#00ff00;" title="Correct"></span>
                                                </c:if>
                                                <c:if test="${ !sb.correct }">
                                                <span class="glyphicon glyphicon-remove" style="color:#ff0000;" title="Wrong"></span>
                                                </c:if>
                                                </td>
                                                <td>
                                                <c:if test="${ sb.correct }">
                                                <a class="btn btn-warning btn-xs" href="javascript:markas(${ sb.id },0);" role="button">Mark incorrect</a>
                                                </c:if>
                                                <c:if test="${ !sb.correct }">
                                                <a class="btn btn-success btn-xs" href="javascript:markas(${ sb.id },1);" role="button">Mark correct</a>
                                                </c:if>
                                                &nbsp;<a class="btn btn-danger btn-xs" href="javascript:delsub(${ sb.id });" role="button">Delete</a>
                                                </td>                                         
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
     <script type="text/javascript">
     function markas(id,stat) {
    		$.confirm({
    		    title: false,
    		    content: 'Sure to mark as '+(stat==1?'Correct?':'Incorrect?'),
    		    theme: 'bootstrap',
    		    confirmButtonClass: 'btn-danger',
    		    cancelButtonClass: 'btn-success',
    		    confirm: function(){
    		    	var csrftoken = $("input[name='pctf_csrf_token']").val();
    		    	var postdata = {'id':id,'newstat':stat,'pctf_csrf_token':csrftoken};
    		    	$.post('admin/editsubmit',postdata,function(data){
    		    		if (data.err==0) {
    		    			window.location.replace('admin/usersubmit/${ currentuser.id }');
    		    		} else {
    		    			$.alert({
    		    				title: false,
    		            	    content: data.errmsg,
    		            	    theme: 'bootstrap',
    		            	    confirmButtonClass: 'btn-danger',
    		           	    	confirm: function(){
    		           	    		if (data.err==-99 || data.err==-98) {
    		           	    			window.location.replace("login");
    		           	    		} else {
    		           	    			window.location.replace('admin/usersubmit/${ currentuser.id }');
    		           	    		}
    		           	    		
    		           	        }
    		    			});
    		    		}
    		    	},'json').error(function(){
    		    		$.alert({
    		        	    title: false,
    		        	    content: "Something Wrong!!!",
    		        	    theme: 'bootstrap',
    		        	    confirmButtonClass: 'btn-danger',
    		        	    confirm: function(){
    		        	    	window.location.replace('admin/usersubmit/${ currentuser.id }');
    		       	        }
    		        	});
    		    	});
    		    }
    		});
    	}

    	function delsub(id) {
    		$.confirm({
    		    title: false,
    		    content: 'Sure to delete?',
    		    theme: 'bootstrap',
    		    confirmButtonClass: 'btn-danger',
    		    cancelButtonClass: 'btn-success',
    		    confirm: function(){
    		    	var csrftoken = $("input[name='pctf_csrf_token']").val();
    		    	var postdata = {'id':id,'pctf_csrf_token':csrftoken};
    		    	$.post('admin/delsubmit',postdata,function(data){
    		    		if (data.err==0) {
    		    			window.location.replace('admin/usersubmit/${ currentuser.id }');
    		    		} else {
    		    			$.alert({
    		    				title: false,
    		            	    content: data.errmsg,
    		            	    theme: 'bootstrap',
    		            	    confirmButtonClass: 'btn-danger',
    		           	    	confirm: function(){
    		           	    		if (data.err==-99 || data.err==-98) {
    		           	    			window.location.replace("login");
    		           	    		} else {
    		           	    			window.location.replace('admin/usersubmit/${ currentuser.id }');
    		           	    		}
    		           	    		
    		           	        }
    		    			});
    		    		}
    		    	},'json').error(function(){
    		    		$.alert({
    		        	    title: false,
    		        	    content: "Something Wrong!!!",
    		        	    theme: 'bootstrap',
    		        	    confirmButtonClass: 'btn-danger',
    		        	    confirm: function(){
    		        	    	window.location.replace('admin/usersubmit/${ currentuser.id }');
    		       	        }
    		        	});
    		    	});
    		    }
    		});
    	}
     
     </script>
</body>

</html>