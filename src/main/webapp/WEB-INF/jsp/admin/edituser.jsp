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
            <h1 class="home-title">Edit User: ${ username }</h1>
            <hr style="border:0;background-color:#d4d4d4;height:1px;" />
        </div>
    </div>
    <div class="news-container">
        <div class="row">
            <div class="span12">
                <form class="form-horizontal">
                    <fieldset>
                        <legend>Score: ${ userscore }, Rank: ${ userrank }</legend>
                        <c:forEach items="${ userstat }" var="ctp">
                        <strong>${ ctp.name }: ${ ctp.proc }/${ ctp.total }(<fmt:formatNumber type="number" value="${ ctp.percent }" maxFractionDigits="1"/>%)</strong>
                        <div class="progress">
                            <div class="progress-bar progress-bar-${ ctp.style } progress-bar-striped" role="progressbar" aria-valuenow="<fmt:formatNumber type="number" value="${ ctp.percent }" maxFractionDigits="1"/>" aria-valuemin="0" aria-valuemax="100" style="min-width: 2em;width: <fmt:formatNumber type="number" value="${ ctp.percent }" maxFractionDigits="1"/>%;">
                                <fmt:formatNumber type="number" value="${ ctp.percent }" maxFractionDigits="1"/>%
                            </div>
                        </div>
                        </c:forEach>
                        <br/>
                        <legend>Total: ${ userscore }/${ totalscore }(${ totalpercent }%)</legend>
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
                    <legend>IP address usage&nbsp;|&nbsp;<a href="admin/userips/${ currentuser.id }">Show All</a></legend>
                        <div class="panel panel-default">
                            <div class="panel-heading">
                                <div class="text-muted bootstrap-admin-box-title"><strong>IPs(Limited to 5 results)</strong></div>
                            </div>
                            <div class="bootstrap-admin-panel-content">
                                <div class="table-responsive">
                                    <table class="table table-striped table-hover">
                                        <thead>
                                            <tr>
                                                <th>IP</th>
                                                <th>First Used</th>
                                                <th>Last Used</th>
                                                <th>Times used</th>
                                            </tr>
                                        </thead>
                                        <tbody>
                                        <c:forEach items="${ ipused }" var="ip">
                                            <tr>
                                                <td>${ ip.ipaddr }</td>
                                                <td><fmt:formatDate value="${ ip.added }" pattern="yyyy-MM-dd HH:mm:ss" /></td>
                                                <td><fmt:formatDate value="${ ip.lastused }" pattern="yyyy-MM-dd HH:mm:ss" /></td>
                                                <td>${ ip.timesused }</td>
                                            </tr>
                                        </c:forEach>
                                        </tbody>
                                    </table>
                                </div>
                            </div>
                        </div>
                        <legend>Submissions&nbsp;|&nbsp;<a href="admin/usersubmit/${ currentuser.id }">Show All</a></legend>
                        <div class="panel panel-default">
                            <div class="panel-heading">
                                <div class="text-muted bootstrap-admin-box-title"><strong>Submissions(Limited to 5 results)</strong></div>
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
                </form>
            </div>
        </div>
    </div>
    <br/>
    <div class="news-container">
        <div class="row">
            <div class="span12">
                <form:form class="form-horizontal" onsubmit="javascript:return false;">
                    <fieldset>
                        <legend>Edit Profile</legend>
                        <div class="form-group">
                            <label class="col-lg-2 control-label" for="typeahead">Email </label>
                            <div class="col-lg-10">
                                <input type="text" class="form-control col-md-6" name="email" autocomplete="off" data-provide="typeahead" value="${ currentuser.email }" disabled>
                            </div>
                        </div>
                        <div class="form-group">
                            <label class="col-lg-2 control-label" for="username">Username</label>
                            <div class="col-lg-10">
                                <input type="text" class="form-control col-md-6" id="username" value="${ currentuser.username }">
                            </div>
                        </div>
                        <div class="form-group">
                            <label class="col-lg-2 control-label" for="phone">Mobile Phone</label>
                            <div class="col-lg-10">
                                <input type="text" class="form-control col-md-6" id="phone" value="${ currentuser.phone }">
                            </div>
                        </div>
                        <div class="form-group">
                            <label class="col-lg-2 control-label" for="date01">Organization/College</label>
                            <div class="col-lg-10">
                                <input type="text" class="form-control col-md-6" id="oganize" value="${ currentuser.organization }">
                            </div>
                        </div>
                        <div class="form-group">
                            <label class="col-lg-2 control-label" for="select01">Country</label>
                            <div class="col-lg-10">
                                <select id="select01" class="form-control" style="width: 300px">
                                <c:forEach items="${countrylist}" var="cts">
                                    <option value="${ cts.id }" <c:if test="${ cts.id==currentuser.countryid }">selected</c:if>>${ cts.countryname }</option>
                                </c:forEach>
                                </select>
                            </div>
                        </div>
                        <div class="form-group">
                            <label class="col-lg-2 control-label" for="select01">Role</label>
                            <div class="col-lg-10">
                                <select id="roleselector" class="form-control" style="width: 300px">
                                    <option value="admin" <c:if test="${ currentuser.role=='admin' }">selected</c:if>>admin</option>
                                    <option value="user" <c:if test="${ currentuser.role=='user' }">selected</c:if>>user</option>
                                </select>
                            </div>
                        </div>
                        <div class="form-group">
                            <label class="col-lg-2 control-label" for="date01">Score</label>
                            <div class="col-lg-10">
                                <input type="text" class="form-control col-md-6" style="width: 120px" name="userscore" value="${ currentuser.score }">
                            </div>
                        </div>
                        <div class="form-group">
                            <label class="col-lg-2 control-label" for="textarea-wysihtml5">Personal Description</label>
                            <div class="col-lg-10">
                                <textarea id="description" class="form-control textarea-wysihtml5" placeholder="Enter text..." style="width: 100%; height: 200px">${ currentuser.description }</textarea>
                            </div>
                        </div>
                        <div class="checkbox">
                            <label>
                                <input name="isenabled" type="checkbox" <c:if test="${ currentuser.isenabled }">checked="checked"</c:if>><font size="3px"><strong>Enabled</strong></font>
                            </label>
                        </div>
                        <br/>
                        <button type="submit" class="btn btn-primary" onclick="savechange(${ currentuser.id });">Save changes</button>
                    </fieldset>
                </form:form>
            </div>
        </div>
    </div>
    <br/>
    <br/>
    <div class="news-container">
        <div class="row">
            <div class="span12">
                <form class="form-horizontal" onsubmit="javascript:return false;">
                    <fieldset>
                        <legend>Reset Password</legend>
                        <button type="submit" class="btn btn-warning" onclick="resetpass(${ currentuser.id });">Reset Password</button>
                    </fieldset>
                </form>
            </div>
        </div>
    </div>
    <br/>
    <div class="news-container">
        <div class="row">
            <div class="span12">
                <form class="form-horizontal" onsubmit="javascript:return false;">
                    <fieldset>
                        <legend>Delete This User</legend>
                        <button type="submit" class="btn btn-danger" onclick="deluser(${ currentuser.id });">Delete</button>
                    </fieldset>
                </form>
            </div>
        </div>
    </div>
    <br/>
    <br/>


    <%@ include file="footer-admin.jsp"%>
    <script type="text/javascript" src="vendors/jquery-confirm/jquery-confirm.min.js"></script>
    <script type="text/javascript">
    function savechange(id) {
    	var username = $("#username").val();
    	var phoneinput = $("#phone").val();
    	var organizeinput = $("#oganize").val();
    	var countryinput = $('#select01 option:selected').val();
    	var roleinput = $('#roleselector option:selected').val();
    	var scoreinput = $("input[name='userscore']").val();
    	var descriptioninput = $('#description').val();
    	var isenable = $("input[name='isenabled']").is(':checked');
    	var csrftoken = $("input[name='pctf_csrf_token']").val();
    	var postdata={
    			'username':username,
    			'phone':phoneinput,
    			'organize':organizeinput,
    			'country':countryinput,
    			'role':roleinput,
    			'score':scoreinput,
    			'description':descriptioninput,
    			'enable':isenable,
    			'pctf_csrf_token':csrftoken
    	};
    	$.post('admin/saveuser/'+id,postdata ,function(data){
    		if (data.err==0) {
    			$.alert({
            	    title: false,
            	    content: data.errmsg,
            	    theme: 'bootstrap',
            	    confirmButtonClass: 'btn-success',
            	    confirm: function(){
            	    	window.location.replace('admin/edituser/${ currentuser.id }');
           	        }
            	});
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
           	    			window.location.replace('admin/edituser/${ currentuser.id }');
           	    		}
           	    		
           	        }
    			}); 
    			
    		}
    	},'json').error(function() {
    		$.alert({
        	    title: false,
        	    content: "Something Wrong!!!",
        	    theme: 'bootstrap',
        	    confirmButtonClass: 'btn-danger',
        	    confirm: function(){
        	    	window.location.replace('admin/edituser/${ currentuser.id }');
       	        }
        	});
    	});
    }
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
    	    			window.location.replace('admin/edituser/${ currentuser.id }');
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
    	           	    			window.location.replace('admin/edituser/${ currentuser.id }');
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
    	        	    	window.location.replace('admin/edituser/${ currentuser.id }');
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
    	    			window.location.replace('admin/edituser/${ currentuser.id }');
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
    	           	    			window.location.replace('admin/edituser/${ currentuser.id }');
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
    	        	    	window.location.replace('admin/edituser/${ currentuser.id }');
    	       	        }
    	        	});
    	    	});
    	    }
    	});
    }
    
    function resetpass(id){
    	$.confirm({
    	    title: false,
    	    content: 'Sure to reset password for user:'+'${currentuser.username}'+'?',
    	    theme: 'bootstrap',
    	    confirmButtonClass: 'btn-danger',
    	    cancelButtonClass: 'btn-success',
    	    confirm: function(){
    	    	var csrftoken = $("input[name='pctf_csrf_token']").val();
    	    	var postdata = {'id':id,'pctf_csrf_token':csrftoken};
    	    	$.post('admin/resetuserpass',postdata,function(data){
    	    		if (data.err==0) {
    	    			$.alert({
    	    				title: false,
                    	    content: data.errmsg,
                    	    theme: 'bootstrap',
                    	    confirmButtonClass: 'btn-success',
                   	    	confirm: function(){
                   	    		window.location.replace('admin/edituser/${ currentuser.id }');
                   	        }
    	    			});
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
                   	    			window.location.replace('admin/edituser/${ currentuser.id }');
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
    	        	    	window.location.replace('admin/edituser/${ currentuser.id }');
    	       	        }
    	        	});
    	    	});
    	    }
    	});
    }
    
    function deluser(id) {
    	$.confirm({
    	    title: false,
    	    content: 'Warning!! Delete user will also delete all submissions & logs of this user.Continue?',
    	    theme: 'bootstrap',
    	    confirmButtonClass: 'btn-danger',
    	    cancelButtonClass: 'btn-success',
    	    confirm: function(){
    	    	var csrftoken = $("input[name='pctf_csrf_token']").val();
    	    	var postdata = {'id':id,'pctf_csrf_token':csrftoken};
    	    	$.post('admin/deluser',postdata,function(data){
    	    		if (data.err==0) {
    	    			$.alert({
    	    				title: false,
                    	    content: data.errmsg,
                    	    theme: 'bootstrap',
                    	    confirmButtonClass: 'btn-success',
                   	    	confirm: function(){
                   	    		window.location.replace('admin/manage?func=users');
                   	        }
    	    			});
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
                   	    			window.location.replace('admin/manage?func=users');
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
    	        	    	window.location.replace('admin/manage?func=users');
    	       	        }
    	        	});
    	    	});
    	    }
    	});
    }
    
    </script>
</body>

</html>