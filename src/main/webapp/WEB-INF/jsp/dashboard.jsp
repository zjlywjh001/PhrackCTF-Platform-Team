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
                        <h3>Score: ${ userscore }, Rank: ${ userrank } (<a href="personalrank">View Personal Ranklist</a>)</h3>
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
                        <h3>Profile&nbsp;|&nbsp;<a href="profile/${ currentuser.id }">View public profile</a></h3>
                        <div class="form-group">
                            <label class="col-lg-2 control-label" for="typeahead">Email </label>
                            <div class="col-lg-10">
                                <input type="text" class="form-control col-md-6" name="email" autocomplete="off" data-provide="typeahead" value="${ currentuser.email }" disabled>
                            </div>
                        </div>
                        <div class="form-group">
                            <label class="col-lg-2 control-label" for="username">Username</label>
                            <div class="col-lg-10">
                                <input type="text" class="form-control col-md-6" name="username" value="${ currentuser.username }" disabled>
                            </div>
                        </div>
                        <div class="form-group">
                            <label class="col-lg-2 control-label" for="phone">Mobile Phone</label>
                            <div class="col-lg-10">
                                <input type="text" class="form-control col-md-6" name="phone" value="${ currentuser.phone }">
                            </div>
                        </div>
                        <div class="form-group">
                            <label class="col-lg-2 control-label" for="organize">Organization/College</label>
                            <div class="col-lg-10">
                                <input type="text" class="form-control col-md-6" name="organize" value="${ currentuser.organization }">
                            </div>
                        </div>
                        <div class="form-group">
                            <label class="col-lg-2 control-label" for="select01">Country</label>
                            <div class="col-lg-10">
                                <select id="select01" class="form-control" style="width: 300px" name="country">
                                <c:forEach items="${countrylist}" var="cts">
                                    <option value="${ cts.id }" <c:if test="${ cts.id==currentuser.countryid }">selected</c:if>>${ cts.countryname }</option>
                                </c:forEach>
                                </select>
                            </div>
                        </div>
                        <div class="form-group">
                            <label class="col-lg-2 control-label" for="textarea-wysihtml5">Personal Description</label>
                            <div class="col-lg-10">
                                <textarea id="desc" class="form-control textarea-wysihtml5" placeholder="Enter text..." style="width: 100%; height: 200px">${ currentuser.description }</textarea>
                            </div>
                        </div>
                        <button id="saveprofile" type="submit" class="btn btn-primary">Save changes</button>
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
                        <h3>Change Password</h3>
                        <div class="form-group">
                            <label class="col-lg-2 control-label" for="oldpass">Old Password</label>
                            <div class="col-lg-10">
                                <input type="password" class="form-control col-md-6" name="oldpass" autocomplete="off" data-provide="typeahead">
                            </div>
                        </div>
                        <div class="form-group">
                            <label class="col-lg-2 control-label" for="newpass">New Password</label>
                            <div class="col-lg-10">
                                <input type="password" class="form-control col-md-6" name="newpass">
                            </div>
                        </div>
                        <div class="form-group">
                            <label class="col-lg-2 control-label" for="repeatpass">Confirm New Password</label>
                            <div class="col-lg-10">
                                <input type="password" class="form-control col-md-6" name="repeatpass">
                            </div>
                        </div>
                        <div class="form-group">
                            <label class="col-lg-2 control-label" for="repeatpass">Captcha</label>
                            <div class="col-lg-10">
                                <input type="text" class="form-control col-md-6" style="width: 150px;display:inline;" name="captcha"><img id="capimg" style="display:inline;" src="captcha.jpg" />
                            </div>
                        </div>
                        <button id="changepass" type="submit" class="btn btn-primary">Save Changes</button>
                    </fieldset>
                </form>
            </div>
        </div>
    </div>
    <br/>
    <br/>


    <%@ include file="footer.jsp"%>
    <script type="text/javascript" src="vendors/jquery-confirm/jquery-confirm.min.js"></script>
    <form:form></form:form>
    <script type="text/javascript">
    $(document).ready(  
    		  function (){  
    		      $('#capimg').click(  
    		          function (){  
    		                $(this).attr('src', "captcha.jpg"+'?'+Math.floor(Math.random() * 100));
    		          }  
    		      );  
    		        
    		  }  
    		); 
    $('#saveprofile').click(function (){
    	var phoneinput = $("input[name='phone']").val();
    	var organizeinput = $("input[name='organize']").val();
    	var countryinput = $('#select01 option:selected').val();
    	var descriptioninput = $('#desc').val();
    	var csrftoken = $("input[name='pctf_csrf_token']").val();
    	var data={'phone':phoneinput,'organize':organizeinput,'country':countryinput,'description':descriptioninput,'pctf_csrf_token':csrftoken};
    	$.post('saveprofile.json',data,function(ret) {
    		if (ret.err==0) {
    			$.alert({
            	    title: false,
            	    content: ret.errmsg,
            	    theme: 'bootstrap',
            	    confirmButtonClass: 'btn-success',
            	    confirm: function(){
            	    	window.location.reload();
           	        }
            	});
    		} else {
    			if (ret.err==-98) {
    				$.alert({
                	    title: false,
                	    content: "Session time out, please login again!!",
                	    theme: 'bootstrap',
                	    confirmButtonClass: 'btn-danger',
               	    	confirm: function(){
               	    		window.location.replace("login");
               	        }
                	});
    			} else {
	    			$.alert({
	            	    title: false,
	            	    content: ret.errmsg,
	            	    theme: 'bootstrap',
	            	    confirmButtonClass: 'btn-danger',
	            	    confirm: function(){
	            	    	window.location.reload();
	           	        }
	            	}); 
    			}
    		}
    	},"json").error(function() {
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
    });
    
    $('#changepass').click(function (){
    	var oldpass = $("input[name='oldpass']").val();
    	var newpass = $("input[name='newpass']").val();
    	var repeatpass = $("input[name='repeatpass']").val();
    	var captcha = $("input[name='captcha']").val();
    	var csrftoken = $("input[name='pctf_csrf_token']").val();
    	var data={'oldpass':oldpass,'newpass':newpass,'confirm':repeatpass,'captcha':captcha,'pctf_csrf_token':csrftoken};
    	$.post('changepass.json',data,function(ret) {
    		if (ret.err==0) {
    			$.alert({
            	    title: false,
            	    content: ret.errmsg,
            	    theme: 'bootstrap',
            	    confirmButtonClass: 'btn-success',
            	    confirm: function(){
           	    		window.location.reload();
           	        }
            	});
    		} else {
    			if (ret.err==-98) {
    				$.alert({
                	    title: false,
                	    content: "Session time out, please login again!!",
                	    theme: 'bootstrap',
                	    confirmButtonClass: 'btn-danger',
               	    	confirm: function(){
               	    		window.location.replace("login");
               	        }
                	});
    			} else {
	    			$.alert({
	            	    title: false,
	            	    content: ret.errmsg,
	            	    theme: 'bootstrap',
	            	    confirmButtonClass: 'btn-danger',
	            	    confirm: function(){
	            	    	window.location.reload();
	           	        }
	            	});
    			}
    		}
    	},"json").error(function() {
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
    });
    </script>
</body>

</html>