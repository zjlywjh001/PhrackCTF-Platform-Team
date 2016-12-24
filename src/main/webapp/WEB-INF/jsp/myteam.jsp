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
    <link rel="stylesheet" href="vendors/jquery-confirm/jquery-confirm.min.css" media="screen">
</head>

<body>
    <%@ include file="nav.jsp"%>

    <c:if test="${ thisuser.teamid==null }">
    	<div class="news-container">
        	<div class="row">
            	<h1 class="home-title">My Team</h1>
           		<hr style="border:0;background-color:#d4d4d4;height:1px;" />
        	</div>
    	</div>
    	<div class="news-container">
        <div class="row">
            <div class="span12">
            	<h3>You are not belong to any team.</h3>
            	<a class="btn btn-primary" role="button" data-toggle="modal" data-target="#addteam">CREATE A TEAM</a> or <a class="btn btn-primary" role="button" data-toggle="modal" data-target="#jointeam">JOIN A TEAM</a>
      			<div class="modal fade" id="addteam" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
	                <div class="modal-dialog modal-lg">
	                    <div class="modal-content">
	                        <div class="modal-header">
	                            <button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>
	                            <h4 class="modal-title" id="myModalLabel">Create Team</h4>
	                        </div>
	                        <div class="modal-body">
	                            <form onsubmit="javascript:return false;">
	                                <div class="form-group">
	                                    <label for="team-name" class="control-label">Team Name:</label>
	                                    <input type="text" class="form-control" id="team-name">
	                                </div>
	                                <div class="form-group">
	                                    <label for="team-organize" class="control-label">Organization:</label>
	                                    <input type="text" class="form-control" id="team-organize">
	                                </div>
	                                <div class="form-group">
	                                    <label for="team-organize" class="control-label">Country:</label>
	                                    <select id="countryid" class="form-control" >
		                            		<option disabled selected>-- Select Your Country --</option>
		                            			<c:forEach items="${country}" var="cts">
		                            				<option value="${cts.getId()}">${cts.getCountryname()}</option>
		                            			</c:forEach>
                       		 			</select>
	                                </div>
	                                <div class="form-group">
	                                    <label for="team-describe" class="control-label">Description:</label>
	                                    <textarea class="form-control" rows="3" id="team-describe"></textarea>
	                                </div>
	                            </form>
	                        </div>
	                        <div class="modal-footer">
	                            <button type="button" class="btn btn-primary" data-dismiss="modal" onclick="addteam();">Save</button>
	                            <button type="button" class="btn btn-default" data-dismiss="modal">Cancel</button>
	                        </div>
	                    </div>
	                </div>
            	</div>
            	<div class="modal fade" id="jointeam" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
	                <div class="modal-dialog modal-lg">
	                    <div class="modal-content">
	                        <div class="modal-header">
	                            <button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>
	                            <h4 class="modal-title" id="myModalLabel">Join Team</h4>
	                        </div>
	                        <div class="modal-body">
	                            <form onsubmit="javascript:return false;">
	                                <div class="form-group">
	                                    <label for="team-token" class="control-label">Team Token:</label>
	                                    <input type="text" class="form-control" id="team-token">
	                                </div>
	                            </form>
	                        </div>
	                        <div class="modal-footer">
	                            <button type="button" class="btn btn-primary" data-dismiss="modal" onclick="jointeam();">Join</button>
	                            <button type="button" class="btn btn-default" data-dismiss="modal">Cancel</button>
	                        </div>
	                    </div>
	                </div>
            	</div>
            </div>
        </div>
    </div>
    <script type="text/javascript">
    function addteam(){
    	var teamname = $('#team-name').val();
    	var teamorgan = $('#team-organize').val();
    	var countryid = $("#countryid option:selected").val();
    	var teamdescript = $('#team-describe').val();
    	var csrftoken = $("input[name='pctf_csrf_token']").val();
    	var postdata = {'teamname':teamname,'organization':teamorgan,'countryid':countryid,'description':teamdescript,'pctf_csrf_token':csrftoken}
    	$.post('addteam.json',postdata ,function(data){
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
    
    function jointeam(){
    	var token = $('#team-token').val();
    	var csrftoken = $("input[name='pctf_csrf_token']").val();
    	var postdata = {'token':token,'pctf_csrf_token':csrftoken}
    	$.post('jointeam.json',postdata ,function(data){
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
    </script>
    </c:if>

	<c:if test="${ thisuser.teamid!=null }">
    <div class="news-container">
        <div class="row">
            <h1 class="home-title">${ teamname }&nbsp;<a href="country/${ countrycode }" style="text-decoration:none;"><img src="images/flags/${ countrycode }.png" style="vertical-align:bottom;" data-placement="right" alt="${ countrycode }" title="${ country }" /></a></h1>
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
                                                <shiro:hasRole name="leader">
                                                <th>Operation</th>
                                                </shiro:hasRole>
                                            </tr>
                                        </thead>
                                        <tbody>
                                        <c:forEach items="${ members }" var="m">
                                            <tr <c:if test="${ m.role=='leader' }">class="warning"</c:if>>
                                                <td><a class="text-muted" style="text-decoration: none;" href="profile/${ m.id }" target="_blank">${ m.username }</a><c:if test="${ m.role=='leader' }"><span style="font-weight:bold">(Creator)</span></c:if></td>
                                                <td>${ m.score }</td>
                                                <td>${ m.solved }</td>
                                                <shiro:hasRole name="leader">
                                                <td>
                                                <c:if test="${ m.role!='leader' }">
                                                <a class="btn btn-danger btn-xs" href="javascript:kick('${ m.username }',${ m.id },${ thisteam.id });" role="button">KICK OUT</a>
                                                </c:if>
                                                <c:if test="${ m.role=='leader' }">
                                                <span class="text-muted" style="font-weight:bold;">Yourself</span>
                                                </c:if>
                                                </td>
                                                </shiro:hasRole>
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
    <shiro:hasRole name="user">
    <div class="news-container">
        <div class="row">
        	<div class="span12">
                <form class="form-horizontal">
                    <fieldset>
                    	<h3>Token</h3>
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
    					<h3>Organization</h3>
   		 				<div>
        					<p>${ thisteam.organization }</p>
   						</div>
   	 					<h3>Team Description</h3>
    					<div>
        					<p>${ thisteam.description }</p>
    					</div>
    				</fieldset>
    			</form>
    		</div>
    	</div>
    </div>
    <div class="news-container">
        <div class="row">
            <div class="span12">
                <form class="form-horizontal" onsubmit="javascript:return false;">
                    <fieldset>
                        <h3>Quit this team</h3>
                        <button type="submit" class="btn btn-danger" onclick="quitteam(${ thisteam.id });">Quit</button>
                    </fieldset>
                </form>
            </div>
        </div>
    </div>
    <script type="text/javascript">
    function quitteam(id){
    	$.confirm({
    	    title: false,
    	    content: 'Are you sure to quit team ${thisteam.name}?',
    	    theme: 'bootstrap',
    	    confirmButtonClass: 'btn-danger',
    	    cancelButtonClass: 'btn-success',
    	    confirm: function(){
    	    	var csrftoken = $("input[name='pctf_csrf_token']").val();
    	    	var postdata = {'teamid':id,'pctf_csrf_token':csrftoken}
    	    	$.post('quitteam.json',postdata ,function(data){
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
    </script>
    </shiro:hasRole>
    <shiro:hasRole name="leader">
    <div class="news-container">
        <div class="row">
            <div class="span12">
                <form class="form-horizontal" onsubmit="javascript: return false;">
                    <fieldset>
                        <h3>Team Settings&nbsp;|&nbsp;<a href="teaminfo/${ thisteam.id }">View public Team Info</a></h3>
                        <div class="form-group">
                            <label class="col-lg-2 control-label" for="typeahead">Team Name</label>
                            <div class="col-lg-10">
                                <input type="text" class="form-control col-md-6" name="teamname" autocomplete="off" data-provide="typeahead" value="${ thisteam.name }" disabled>
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
                        <button id="changepass" type="submit" class="btn btn-warning" onclick="resettoken(${ thisteam.id });">Reset token</button>
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
                        <h3>Dismiss your team</h3>
                        <button type="submit" class="btn btn-danger" onclick="dismissteam(${ thisteam.id });">Dismiss</button>
                    </fieldset>
                </form>
            </div>
        </div>
    </div>
    <script type="text/javascript">
    	function saveteaminfo(id){
    		var organize = $("input[name='organize']").val();
        	var countryinput = $('#select01 option:selected').val();
        	var descriptioninput = $('#desc').val();
        	var csrftoken = $("input[name='pctf_csrf_token']").val();
        	var postdata={'id':id ,'organization':organize,'countryid':countryinput,'description':descriptioninput,'pctf_csrf_token':csrftoken};
        	$.post('saveteam.json',postdata ,function(data){
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
    	function resettoken(id){
    		$.confirm({
    		    title: false,
    		    content: 'Sure to reset team token?',
    		    theme: 'bootstrap',
    		    confirmButtonClass: 'btn-danger',
    		    cancelButtonClass: 'btn-success',
    		    confirm: function(){
    		    	var csrftoken = $("input[name='pctf_csrf_token']").val();
    	        	var postdata={'id':id ,'pctf_csrf_token':csrftoken};
    	        	$.post('resettoken.json',postdata ,function(data){
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
    	        	$.post('kickmember.json',postdata ,function(data){
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
    	function dismissteam(id) {
    		$.confirm({
    		    title: false,
    		    content: 'Are you sure to dismiss your team?',
    		    theme: 'bootstrap',
    		    confirmButtonClass: 'btn-danger',
    		    cancelButtonClass: 'btn-success',
    		    confirm: function(){
    		    	var csrftoken = $("input[name='pctf_csrf_token']").val();
    	        	var postdata={'teamid':id,'pctf_csrf_token':csrftoken};
    	        	$.post('dismissteam.json',postdata ,function(data){
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
    </script>
    </shiro:hasRole>
    </c:if>
    <br/>
    <br/>


    <%@ include file="footer.jsp"%>
    <script type="text/javascript" src="vendors/jquery-confirm/jquery-confirm.min.js"></script>
    <form:form></form:form>
 
</body>

</html>