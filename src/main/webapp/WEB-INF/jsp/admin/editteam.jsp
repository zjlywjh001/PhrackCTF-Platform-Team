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
    <script type="text/javascript" src="//cdn.bootcss.com/jquery/2.0.3/jquery.js"></script>
    <link rel="stylesheet" href="vendors/jquery-confirm/jquery-confirm.min.css" media="screen">
</head>

<body>
    <%@ include file="../nav.jsp"%>
    <div class="news-container">
        <div class="row">
            <h1 class="home-title">Edit Team: ${ teamname }</h1>
            <hr style="border:0;background-color:#d4d4d4;height:1px;" />
        </div>
    </div>
    <div class="news-container">
        <div class="row">
            <div class="span12">
                <form class="form-horizontal">
                    <fieldset>
                        <h3>Score: ${ teamscore }, Rank: ${ teamrank }</h3>
                        <c:forEach items="${ teamstat }" var="ctp">
                        <strong>${ ctp.name }: ${ ctp.proc }/${ ctp.total }(<fmt:formatNumber type="number" value="${ ctp.percent }" maxFractionDigits="1"/>%)</strong>
                        <div class="progress">
                            <div class="progress-bar progress-bar-${ ctp.style } progress-bar-striped" role="progressbar" aria-valuenow="<fmt:formatNumber type="number" value="${ ctp.percent }" maxFractionDigits="1"/>" aria-valuemin="0" aria-valuemax="100" style="min-width: 2em;width: <fmt:formatNumber type="number" value="${ ctp.percent }" maxFractionDigits="1"/>%;">
                                <fmt:formatNumber type="number" value="${ ctp.percent }" maxFractionDigits="1"/>%
                            </div>
                        </div>
                        </c:forEach>
                        <br/>
                        <h3>Total: ${ teamscore }/${ totalscore }(${ totalpercent }%)</h3>
                        <div class="progress">
                        <c:forEach items="${ teamstat }" var="ctp">
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
                                                <th>Solved by</th>
                                                <th>Time</th>
                                            </tr>
                                        </thead>
                                        <tbody>
                                        <c:forEach items="${ passtask }" var="ch">
                                            <tr>
                                                <td><a class="text-muted" style="text-decoration: none;" href="challenges?taskid=${ ch.id }" target="_blank">${ ch.title }</a></td>
                                                <td><span class="label label-${ ch.catemark } label-as-badge">${ ch.catename }</span></td>
                                                <td>${ ch.score }</td>
                                                <td>${ ch.solvedby }</td>
                                                <td><fmt:formatDate value="${ ch.passtime }" pattern="yyyy-MM-dd HH:mm"/></td>
                                            </tr>
                                        </c:forEach>
                                        </tbody>
                                    </table>
                                </div>
                            </div>
                        </div>
                        <div class="panel panel-default">
                            <div class="panel-heading">
                                <div class="text-muted bootstrap-admin-box-title"><strong>Team Members</strong></div>
                            </div>
                            <div class="bootstrap-admin-panel-content">
                                <div class="table-responsive">
                                    <table class="table table-striped table-hover">
                                        <thead>
                                            <tr>
                                                <th>Username</th>
                                                <th>Score</th>
                                                <th>Solved Tasks</th>
                                                <th>Operation</th>
                                            </tr>
                                        </thead>
                                        <tbody>
                                        <c:forEach items="${ members }" var="m">
                                            <tr <c:if test="${ m.role=='leader' }">class="warning"</c:if>>
                                                <td><a class="text-muted" style="text-decoration: none;" href="profile/${ m.id }" target="_blank">${ m.username }</a><c:if test="${ m.role=='leader' }"><span style="font-weight:bold">(Creator)</span></c:if></td>
                                                <td>${ m.score }</td>
                                                <td>${ m.solved }</td>
                                                <td>
                                                <c:if test="${ m.role!='leader' }">
                                                <a class="btn btn-danger btn-xs" href="javascript:kick('${ m.username }',${ m.id },${ thisteam.id });" role="button">KICK OUT</a>
                                                </c:if>
                                                <c:if test="${ m.role=='leader' }">
                                                <span class="text-muted" style="font-weight:bold;">No option</span>
                                                </c:if>
                                                </td>
                                            </tr>
                                        </c:forEach>
                                        </tbody>
                                    </table>
                                </div>
                            </div>
                        </div>
                        <h3>IP address usage&nbsp;|&nbsp;<a href="admin/teamips/${ thisteam.id }">Show All</a></h3>
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
                                                <th>Used By</th>
                                                <th>First Used</th>
                                                <th>Last Used</th>
                                                <th>Times used</th>
                                            </tr>
                                        </thead>
                                        <tbody>
                                        <c:forEach items="${ ipused }" var="ip">
                                            <tr>
                                                <td>${ ip.ipaddr }</td>
                                                <td>${ ip.usedby }</td>
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
                        <h3>Submissions&nbsp;|&nbsp;<a href="admin/teamsubmit/${ thisteam.id }">Show All</a></h3>
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
                                                <th>Team</th>
                                                <th>Submitted By</th>
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
                                                <td>${ sb.teamname }</td>
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
                <form class="form-horizontal" onsubmit="javascript: return false;">
                    <fieldset>
                        <h3>Team Settings&nbsp;|&nbsp;<a href="teaminfo/${ thisteam.id }">View public Team Info</a></h3>
                        <div class="form-group">
                            <label class="col-lg-2 control-label" for="typeahead">Team Name</label>
                            <div class="col-lg-10">
                                <input type="text" class="form-control col-md-6" name="teamname" value="${ thisteam.name }">
                            </div>
                        </div>
                        <div class="form-group">
                            <label class="col-lg-2 control-label" for="organize">Organization/College</label>
                            <div class="col-lg-10">
                                <input type="text" class="form-control col-md-6" name="organize" value="${ thisteam.organization }">
                            </div>
                        </div>
                        <div class="form-group">
                            <label class="col-lg-2 control-label" for="select01">Country</label>
                            <div class="col-lg-10">
                                <select id="select01" class="form-control" style="width: 300px" name="country">
                                <c:forEach items="${countrylist}" var="cts">
                                    <option value="${ cts.id }" <c:if test="${ cts.id==thisteam.countryid }">selected</c:if>>${ cts.countryname }</option>
                                </c:forEach>
                                </select>
                            </div>
                        </div>
                        <div class="form-group">
                            <label class="col-lg-2 control-label" for="textarea-wysihtml5">Team Description</label>
                            <div class="col-lg-10">
                                <textarea id="desc" class="form-control textarea-wysihtml5" placeholder="Enter text..." style="width: 100%; height: 200px">${ thisteam.description }</textarea>
                            </div>
                        </div>
                        <div class="checkbox">
                            <label>
                                <input name="isenabled" type="checkbox" <c:if test="${ thisteam.isenabled }">checked="checked"</c:if>><font size="3px"><strong>Enabled</strong></font>
                            </label>
                        </div>
                        <br/>
                        <button id="saveprofile" type="submit" class="btn btn-primary" onclick="saveteaminfo(${ thisteam.id });">Save changes</button>
                    </fieldset>
                </form>
            </div>
        </div>
    </div>
    <br/>
    <br/>
    <div class="news-container">
        <div class="row">
            <div class="span12">
                <form class="form-horizontal" onsubmit="javascript: return false;">
                    <fieldset>
                        <h3>Team token</h3>
                        <div class="well">
                        	<table style="table-layout:fixed;word-break:break-all">
						    	<thead>
						        <tr>
						            <th>
						                <span style="font-weight:normal;">${ thisteam.teamtoken }</span>
						            </th>
						        </tr>
						    	</thead>
							</table>
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
                <form class="form-horizontal" onsubmit="javascript:return false;">
                    <fieldset>
                        <h3>Delete</h3>
                        <button type="submit" class="btn btn-danger" onclick="deleteteam(${ thisteam.id });">Delete</button>
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
    	function saveteaminfo(id){
    		var organize = $("input[name='organize']").val();
        	var countryinput = $('#select01 option:selected').val();
        	var descriptioninput = $('#desc').val();
        	var isenable = $("input[name='isenabled']").is(':checked');
        	var csrftoken = $("input[name='pctf_csrf_token']").val();
        	var name = $("input[name='teamname']").val();
        	var postdata={'id':id ,'teamname':name,'organization':organize,'countryid':countryinput,'description':descriptioninput,'enable':isenable,'pctf_csrf_token':csrftoken};
        	$.post('admin/saveteaminfo.json',postdata ,function(data){
	    		if (data.err==0) {
	    			$.alert({
	            	    title: false,
	            	    content: data.errmsg,
	            	    theme: 'bootstrap',
	            	    confirmButtonClass: 'btn-success',
	            	    confirm: function(){
	            	    	window.location.reload();
	           	        }
	            	});
	    		} else {
	    			$.alert({
	    				title: false,
	            	    content: data.errmsg,
	            	    theme: 'bootstrap',
	            	    confirmButtonClass: 'btn-danger',
	            	    confirm: function(){
	           	    		if (data.err==-99) {
	           	    			window.location.replace("login");
	           	    		} else {
	           	    			window.location.reload();
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
	        	    	window.location.reload();
	       	        }
	        	});
	    	});
    	}
    	function kick(membername,memberid,teamid){
    		$.confirm({
    		    title: false,
    		    content: 'Sure to kick member '+membername+'?',
    		    theme: 'bootstrap',
    		    confirmButtonClass: 'btn-danger',
    		    cancelButtonClass: 'btn-success',
    		    confirm: function(){
    		    	var csrftoken = $("input[name='pctf_csrf_token']").val();
    	        	var postdata={'teamid':teamid,'memberid':memberid ,'pctf_csrf_token':csrftoken};
    	        	$.post('admin/delmember.json',postdata ,function(data){
    		    		if (data.err==0) {
    		    			$.alert({
    		            	    title: false,
    		            	    content: data.errmsg,
    		            	    theme: 'bootstrap',
    		            	    confirmButtonClass: 'btn-success',
    		            	    confirm: function(){
    		            	    	window.location.reload();
    		           	        }
    		            	});
    		    		} else {
    		    			$.alert({
    		    				title: false,
    		            	    content: data.errmsg,
    		            	    theme: 'bootstrap',
    		            	    confirmButtonClass: 'btn-danger',
    		            	    confirm: function(){
    		           	    		if (data.err==-99) {
    		           	    			window.location.replace("login");
    		           	    		} else {
    		           	    			window.location.reload();
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
    		        	    	window.location.reload();
    		       	        }
    		        	});
    		    	});
    		    }
    		});
    	}
    	function deleteteam(id) {
    		$.confirm({
    		    title: false,
    		    content: 'Are you sure to delete this team?',
    		    theme: 'bootstrap',
    		    confirmButtonClass: 'btn-danger',
    		    cancelButtonClass: 'btn-success',
    		    confirm: function(){
    		    	var csrftoken = $("input[name='pctf_csrf_token']").val();
    	        	var postdata={'teamid':id,'pctf_csrf_token':csrftoken};
    	        	$.post('admin/delteam.json',postdata ,function(data){
    		    		if (data.err==0) {
    		    			$.alert({
    		            	    title: false,
    		            	    content: data.errmsg,
    		            	    theme: 'bootstrap',
    		            	    confirmButtonClass: 'btn-success',
    		            	    confirm: function(){
    		            	    	window.location.replace('admin/manage?func=teams');
    		           	        }
    		            	});
    		    		} else {
    		    			$.alert({
    		    				title: false,
    		            	    content: data.errmsg,
    		            	    theme: 'bootstrap',
    		            	    confirmButtonClass: 'btn-danger',
    		           	    	confirm: function(){
    		           	    		if (data.err==-99) {
    		           	    			window.location.replace("login");
    		           	    		} else {
    		           	    			window.location.replace('admin/manage?func=teams');
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
    		        	    	window.location.replace('admin/manage?func=teams');
    		       	        }
    		        	});
    		    	});
    		    }
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
        	    			window.location.reload();
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
        	           	    			window.location.reload();
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
        	        	    	window.location.reload();
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
        	    			window.location.reload();
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
        	           	    			window.location.reload();
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
        	        	    	window.location.reload();
        	       	        }
        	        	});
        	    	});
        	    }
        	});
        }
    </script>
    <form:form></form:form>
 
</body>

</html>