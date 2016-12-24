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
    <link rel="stylesheet" media="screen" href="vendors/bootstrap-datetimepicker/css/bootstrap-datetimepicker.min.css">
    <!-- 样式文件 -->
    <link rel="stylesheet" href="umeditor/themes/default/css/umeditor.css">
    <!-- 引用jquery -->
    <script src="umeditor/third-party/jquery.min.js"></script>
    <!-- 配置文件 -->
    <script type="text/javascript" src="umeditor/umeditor.config.js"></script>
    <!-- 编辑器源码文件 -->
    <script type="text/javascript" src="umeditor/umeditor.js"></script>
    <!-- 语言包文件 -->
    <script type="text/javascript" src="umeditor/lang/en/en.js"></script>
    <!-- 实例化编辑器代码 -->
    <script type="text/javascript">
        $(function () {
            window.um = UM.getEditor('ruleeditor');
            um.setWidth("100%");
            $(".edui-body-container").css("width", "98%");
        });
    </script>
    <link rel="stylesheet" href="vendors/jquery-confirm/jquery-confirm.min.css" media="screen">
</head>

<body>
    <%@ include file="../nav.jsp"%>
    
    <div class="news-container">
        <div class="row">
            <h1 class="home-title">Control Panel</h1>
        </div>
    </div>
    <br/>

    <div class="news-container">
        <div class="row">
            <div class="span12">
                <ul id="myTab" class="nav nav-tabs">
                    <li <c:if test='${ param.func=="news"||param.func==null }'>class="active"</c:if>>
                        <a href="${ ctrlname }#news" data-toggle="tab">News</a>
                    </li>
                    <li <c:if test='${ param.func=="rule" }'>class="active"</c:if>>
                        <a href="${ ctrlname }#rule" data-toggle="tab">Rule</a>
                    </li>
                    <li <c:if test='${ param.func=="categories" }'>class="active"</c:if>>
                        <a href="${ ctrlname }#categories" data-toggle="tab">Categories</a>
                    </li>
                    <li <c:if test='${ param.func=="challenges" }'>class="active"</c:if>>
                        <a href="${ ctrlname }#challenges" data-toggle="tab">Challenges</a>
                    </li>
                    <li <c:if test='${ param.func=="submissions" }'>class="active"</c:if>>
                        <a href="${ ctrlname }#submissions" data-toggle="tab">Submissions</a>
                    </li>
                    <li <c:if test='${ param.func=="users" }'>class="active"</c:if>>
                        <a href="${ ctrlname }#users" data-toggle="tab">Users</a>
                    </li>
                    <li <c:if test='${ param.func=="teams" }'>class="active"</c:if>>
                        <a href="${ ctrlname }#teams" data-toggle="tab">Teams</a>
                    </li>
                    <li <c:if test='${ param.func=="hints" }'>class="active"</c:if>>
                        <a href="${ ctrlname }#hints" data-toggle="tab">Hints</a>
                    </li>
                    <li <c:if test='${ param.func=="mails" }'>class="active"</c:if>>
                        <a href="${ ctrlname }#mails" data-toggle="tab">Mails</a>
                    </li>
                    <li <c:if test='${ param.func=="logs" }'>class="active"</c:if>>
                        <a href="${ ctrlname }#logs" data-toggle="tab">Admin Logs</a>
                    </li>
                    <li <c:if test='${ param.func=="config" }'>class="active"</c:if>>
                        <a href="${ ctrlname }#config" data-toggle="tab">System Config</a>
                    </li>
                     <li <c:if test='${ param.func=="blacklist" }'>class="active"</c:if>>
                        <a href="${ ctrlname }#blacklist" data-toggle="tab">IP Blacklist</a>
                    </li>
                </ul>
                <div id="myTabContent" class="tab-content">
                    <div class="tab-pane fade <c:if test='${ param.func=="news"||param.func==null }'> in active</c:if>" id="news">
                        <h2>News List</h2>
                        <hr style="border:0;background-color:#d4d4d4;height:1px;" />
                        <div class="panel panel-default">
                            <div class="panel-heading">
                                <div class="text-muted bootstrap-admin-box-title"><strong>News |</strong>&nbsp;<a class="btn btn-primary btn-xs" href="admin/addnews" role="button">Add</a></div>
                            </div>
                            <div class="bootstrap-admin-panel-content">
                                <div class="table-responsive">
                                    <table class="table table-striped table-hover">
                                        <thead>
                                            <tr>
                                                <th>Title</th>
                                                <th>Post time</th>
                                                <th>Operation</th>
                                            </tr>
                                        </thead>
                                        <tbody>
                                        	<c:forEach items="${newslist}" var="news">
                                            <tr>
                                                <td>${ news.title }</td>
                                                <td><fmt:formatDate value="${news.posttime}" pattern="yyyy-MM-dd HH:mm:ss"/></td>
												<td><a class="btn btn-default btn-xs" href="admin/editnews/${ news.id }" role="button">Edit</a>&nbsp;<a class="btn btn-danger btn-xs" onclick="delnews(${ news.id });" role="button">Delete</a></td>
                                            </tr>
                                            </c:forEach>
                                        </tbody>
                                    </table>
                                </div>
                            </div>
                        </div>

                    </div>
                    <div class="tab-pane fade <c:if test='${ param.func=="rule" }'> in active</c:if>" id="rule">
                        <h2>Edit Rules</h2>
                        <hr style="border:0;background-color:#d4d4d4;height:1px;" />
                        <!-- 加载编辑器的容器 -->
                        <script id="ruleeditor" type="text/plain" style="height:500px;">
                            <c:if test="${fn:length(rulecontent)==0}">
								<p></p>
							</c:if>
							${ rulecontent }
                        </script>

                        <br>
                        <br>
                        <button type="submit" class="btn btn-primary" onclick="saverule();">Save Changes</button>

                    </div>
                    <div class="tab-pane fade <c:if test='${ param.func=="categories" }'> in active</c:if>" id="categories">
                        <h2>Categories List</h2>
                        <hr style="border:0;background-color:#d4d4d4;height:1px;" />
                        <div class="panel panel-default">
                            <div class="panel-heading">
                                <div class="text-muted bootstrap-admin-box-title"><strong>Categories |</strong>&nbsp;
                                    <button class="btn btn-primary btn-xs" data-toggle="modal" data-target="#addcate">Add</button>
                                    <div class="modal fade" id="addcate" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
                                        <div class="modal-dialog modal-lg">
                                            <div class="modal-content">
                                                <div class="modal-header">
                                                    <button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>
                                                    <h4 class="modal-title" id="myModalLabel">Add Category</h4>
                                                </div>
                                                <div class="modal-body">
                                                    <form onsubmit="javascript:return false;">
                                                        <div class="form-group">
                                                            <label for="catgory-name" class="control-label">Category Name:</label>
                                                            <input type="text" class="form-control" id="add-cate-name">
                                                        </div>
                                                        <div class="form-group">
                                                            <label for="message-text" class="control-label">Label style:</label>
                                                            <br>
                                                            <label class="radio-inline">
                                                                <input type="radio" name="addcatemark" id="style1" value="1"> <span class="label label-default label-as-badge">Catgory</span>
                                                            </label>
                                                            <label class="radio-inline">
                                                                <input type="radio" name="addcatemark" id="style2" value="2"> <span class="label label-primary label-as-badge">Catgory</span>
                                                            </label>
                                                            <label class="radio-inline">
                                                                <input type="radio" name="addcatemark" id="style3" value="3"> <span class="label label-success label-as-badge">Catgory</span>
                                                            </label>
                                                            <label class="radio-inline">
                                                                <input type="radio" name="addcatemark" id="style4" value="4"> <span class="label label-info label-as-badge">Catgory</span>
                                                            </label>
                                                            <label class="radio-inline">
                                                                <input type="radio" name="addcatemark" id="style5" value="5"> <span class="label label-warning label-as-badge">Catgory</span>
                                                            </label>
                                                            <label class="radio-inline">
                                                                <input type="radio" name="addcatemark" id="style6" value="6"> <span class="label label-danger label-as-badge">Catgory</span>
                                                            </label>

                                                        </div>
                                                    </form>
                                                </div>
                                                <div class="modal-footer">
                                                    <button type="button" class="btn btn-primary" data-dismiss="modal" onclick="addcate();">Save changes</button>
                                                    <button type="button" class="btn btn-default" data-dismiss="modal">Close</button>
                                                </div>
                                            </div>
                                        </div>
                                    </div>
                                </div>
                            </div>
                            <div class="bootstrap-admin-panel-content">
                                <div class="table-responsive">
                                    <table class="table table-striped table-hover">
                                        <thead>
                                            <tr>
                                                <th>Name</th>
                                                <th>Mark</th>
                                                <th>Operation</th>
                                            </tr>
                                        </thead>
                                        <tbody>
                                        	<c:forEach items="${ cates }" var="c">
                                            <tr>
                                                <td>${ c.name }</td>
                                                <td><span class="label label-${ c.mark } label-as-badge">${ c.name }</span></td>
												<td>
                                                    <button class="btn btn-default btn-xs" data-toggle="modal" data-target="#editcate" data-whatever="${ c.name }" onclick='setidmark(${ c.id },"${ c.mark }");'>Edit</button>
                                                    <button class="btn btn-danger btn-xs" role="button" onclick="delcate(${ c.id });" >Delete</button>
                                               	</td>
                                            </tr>
                                            </c:forEach>
                                        </tbody>
                                    </table>
                                </div>
                                <div class="modal fade" style="text-align:left;" id="editcate" tabindex="-1" role="dialog" aria-labelledby="editcateLabel" aria-hidden="true">
                                          <div class="modal-dialog modal-lg">
                                              <div class="modal-content">
                                                  <div class="modal-header">
                                                      <button type="button" class="close" data-dismiss="modal" aria-hidden="true" >&times;</button>
                                                      <h4 class="modal-title" id="editcateLabel">Edit Category</h4>
                                                  </div>
                                                  <div class="modal-body">
                                                      <form onsubmit="javascript:return false;">
                                                          <div class="form-group">
                                                              <label for="category-name" class="control-label">Category Name:</label>
                                                              <input type="text" class="form-control" id="edit-cate-name">
                                                          </div>
                                                          <div class="form-group">
                                                              <label for="message-text" class="control-label">Label style:</label>
                                                              <br>
                                                              <label class="radio-inline">
                                                                  <input type="radio" name="editcatemark" id="catemark-default" value="1"> <span class="label label-default label-as-badge">Catgory</span>
                                                              </label>
                                                              <label class="radio-inline">
                                                                  <input type="radio" name="editcatemark" id="catemark-primary" value="2"> <span class="label label-primary label-as-badge">Catgory</span>
                                                              </label>
                                                              <label class="radio-inline">
                                                                  <input type="radio" name="editcatemark" id="catemark-success" value="3"> <span class="label label-success label-as-badge">Catgory</span>
                                                              </label>
                                                              <label class="radio-inline">
                                                                  <input type="radio" name="editcatemark" id="catemark-info" value="4"> <span class="label label-info label-as-badge">Catgory</span>
                                                              </label>
                                                              <label class="radio-inline">
                                                                  <input type="radio" name="editcatemark" id="catemark-warning" value="5"> <span class="label label-warning label-as-badge">Catgory</span>
                                                              </label>
                                                              <label class="radio-inline">
                                                                  <input type="radio" name="editcatemark" id="catemark-danger" value="6"> <span class="label label-danger label-as-badge">Catgory</span>
                                                              </label>
																  <input type="hidden" name="cateid" value=""/>
                                                          </div>
                                                      </form>
                                                  </div>
                                                  <div class="modal-footer">
                                                      <button type="button" class="btn btn-primary" onclick="editcate();">Save changes</button>
                                                      <button type="button" class="btn btn-default" data-dismiss="modal">Close</button>
                                                  </div>
                                              </div>
                                          </div>
                                      </div>
                            </div>
                        </div>
                    </div>
                    <div class="tab-pane fade <c:if test='${ param.func=="challenges" }'> in active</c:if>" id="challenges">
                        <h2>Challenges List</h2>
                        <hr style="border:0;background-color:#d4d4d4;height:1px;" />
                        <div class="panel panel-default">
                            <div class="panel-heading">
                                <div class="text-muted bootstrap-admin-box-title"><strong>Challenges |</strong>&nbsp;<a class="btn btn-primary btn-xs" href="admin/addprob" role="button">Add</a></div>
                            </div>
                            <div class="bootstrap-admin-panel-content">
                                <div class="table-responsive">
                                    <table class="table table-striped table-hover">
                                        <thead>
                                            <tr>
                                                <th>Name</th>
                                                <th>Category</th>
                                                <th>Score</th>
                                                <th>Available</th>
                                                <th>Invalidate</th>
                                                <th>Operation</th>
                                            </tr>
                                        </thead>
                                        <tbody>
                                        	<c:forEach items="${challengelist}" var="ch">
                                            <tr>
                                                <td>${ ch.title }</td>
                                                <td><span class="label label-${ ch.catemark } label-as-badge">${ ch.catename }</span></td>
                                                <td>${ ch.score }</td>
                                                <td><fmt:formatDate value="${ ch.available }" pattern="yyyy-MM-dd HH:mm:ss" /></td>
                                                <td><fmt:formatDate value="${ ch.invalidate }" pattern="yyyy-MM-dd HH:mm:ss" /></td>
                                                <td><a class="btn btn-default btn-xs" href="admin/editprob/${ ch.id }" role="button">Edit</a>&nbsp;<a class="btn btn-default btn-xs" href="admin/addhint?taskid=${ ch.id }" role="button">Hint</a>&nbsp;<a class="btn btn-danger btn-xs" href="javascript:delchallenge(${ ch.id });" role="button">Delete</a></td>
                                            </tr>
                                            </c:forEach>
                                        </tbody>
                                    </table>
                                </div>
                            </div>
                        </div>
                    </div>
                    <div class="tab-pane fade <c:if test='${ param.func=="submissions" }'> in active</c:if>" id="submissions">
                        <h2>All Submissions</h2>
                        <hr style="border:0;background-color:#d4d4d4;height:1px;" />
                        <div class="panel panel-default">
                            <div class="panel-heading">
                                <div class="text-muted bootstrap-admin-box-title"><strong>Submissions (Limited to 50 records) |</strong>&nbsp;<a class="btn btn-primary btn-xs" href="admin/submitlist" role="button" target="_blank">SHOW All</a></div>
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
                    </div>
                    <div class="tab-pane fade <c:if test='${ param.func=="users" }'> in active</c:if>" id="users">
                        <h2>Users</h2>
                        <hr style="border:0;background-color:#d4d4d4;height:1px;" />
                        <div class="panel panel-default">
                            <div class="panel-heading">
                                <div class="text-muted bootstrap-admin-box-title"><strong>User List (Limited to 50 records) |</strong>&nbsp;<a class="btn btn-primary btn-xs" href="admin/userlist" role="button" target="_blank">SHOW All</a></div>
                            </div>
                            <div class="bootstrap-admin-panel-content">
                                <div class="table-responsive">
                                    <table class="table table-striped table-hover">
                                        <thead>
                                            <tr>
                                                <th>Username</th>
                                                <th>Email</th>
                                                <th>Reg time</th>
                                                <th>Last Active</th>
                                                <th>Class</th>
                                                <th>Enabled</th>
                                                <th>IPs</th>
                                                <th>Operation</th>
                                            </tr>
                                        </thead>
                                        <tbody>
                                       		<c:forEach items="${userlist}" var="user">
                                            <tr>
                                                <td><img src="images/flags/${user.countrycode}.png" data-placement="right" alt="cn" title="${ user.countryname }" />&nbsp;&nbsp;${ user.username }</td>
                                                <td>${ user.email }</td>
                                                <td><fmt:formatDate value="${ user.regtime }" pattern="yyyy-MM-dd HH:mm" /></td>
                                                <td><fmt:formatDate value="${ user.lastactive }" pattern="yyyy-MM-dd HH:mm" /></td>
                                                <td>${ user.role }</td>
                                                <td>
                                                <c:if test="${ user.isenabled }">
                                                <span class="glyphicon glyphicon-ok" style="color:#00ff00;" title="Enable"></span>
                                                </c:if>
                                                <c:if test="${ !user.isenabled }">
                                                <span class="glyphicon glyphicon-remove" style="color:#ff0000;" title="Banned"></span>
                                                </c:if>
                                                </td>
                                                <td>${ user.ips }</td>
                                                <td><a class="btn btn-primary btn-xs" href="admin/mails?target=${ user.id }" role="button" target="_blank">MailTo</a>&nbsp;<a class="btn btn-default btn-xs" href="admin/edituser/${ user.id }" role="button" target="_blank">Edit</a></td>
                                            </tr>
                                            </c:forEach>
                                        </tbody>
                                    </table>
                                </div>
                            </div>
                        </div>
                    </div>
                    <div class="tab-pane fade <c:if test='${ param.func=="teams" }'> in active</c:if>" id="teams">
                        <h2>Teams</h2>
                        <hr style="border:0;background-color:#d4d4d4;height:1px;" />
                        <div class="panel panel-default">
                            <div class="panel-heading">
                                <div class="text-muted bootstrap-admin-box-title"><strong>Team List (Limited to 50 records) |</strong>&nbsp;<a class="btn btn-primary btn-xs" href="admin/teamlist" role="button" target="_blank">SHOW All</a></div>
                            </div>
                            <div class="bootstrap-admin-panel-content">
                                <div class="table-responsive">
                                    <table class="table table-striped table-hover">
                                        <thead>
                                            <tr>
                                                <th>Teamname</th>
                                                <th>Creator</th>
                                                <th>Create time</th>
                                                <th>Last Solve</th>
                                                <th>Enabled</th>
                                                <th>IPs</th>
                                                <th>Operation</th>
                                            </tr>
                                        </thead>
                                        <tbody>
                                       		<c:forEach items="${teamlist}" var="team">
                                            <tr>
                                                <td><img src="images/flags/${team.countrycode}.png" data-placement="right" alt="cn" title="${ team.countryname }" />&nbsp;&nbsp;${ team.name }</td>
                                                <td>${ team.creator }</td>
                                                <td><fmt:formatDate value="${ team.createtime }" pattern="yyyy-MM-dd HH:mm" /></td>
                                                <td><fmt:formatDate value="${ team.getLastSubmit() }" pattern="yyyy-MM-dd HH:mm" /></td>
                                                <td>
                                                <c:if test="${ team.isenabled }">
                                                <span class="glyphicon glyphicon-ok" style="color:#00ff00;" title="Enable"></span>
                                                </c:if>
                                                <c:if test="${ !team.isenabled }">
                                                <span class="glyphicon glyphicon-remove" style="color:#ff0000;" title="Banned"></span>
                                                </c:if>
                                                </td>
                                                <td>${ team.ips }</td>
                                                <td><a class="btn btn-default btn-xs" href="admin/editteam/${ team.id }" role="button" target="_blank">Edit</a></td>
                                            </tr>
                                            </c:forEach>
                                        </tbody>
                                    </table>
                                </div>
                            </div>
                        </div>
                    </div>
                    <div class="tab-pane fade <c:if test='${ param.func=="hints" }'> in active</c:if>" id="hints">
                        <h2>Hints</h2>
                        <hr style="border:0;background-color:#d4d4d4;height:1px;" />
                        <div class="panel panel-default">
                            <div class="panel-heading">
                                <div class="text-muted bootstrap-admin-box-title"><strong>Hints List |</strong>&nbsp;<a class="btn btn-primary btn-xs" href="admin/addhint" role="button">Add</a></div>
                            </div>
                            <div class="bootstrap-admin-panel-content">
                                <div class="table-responsive">
                                    <table class="table table-striped table-hover">
                                        <thead>
                                            <tr>
                                                <th>Challenge</th>
                                                <th>Added Time</th>
                                                <th>Hint</th>
                                                <th>Operation</th>
                                            </tr>
                                        </thead>
                                        <tbody>
                                        	<c:forEach items="${ hintsdisp }" var="hi">
                                            <tr>
                                                <td>${ hi.challname }</td>
                                                <td><fmt:formatDate value="${ hi.adddate }" pattern="yyyy-MM-dd HH:mm:ss" /></td>
                                                <td>${ hi.content }</td>
                                                <td><a class="btn btn-default btn-xs" href="admin/edithint/${ hi.id }" role="button">Edit</a>&nbsp;<a class="btn btn-danger btn-xs" href="javascript:delhint(${ hi.id });" role="button">Delete</a></td>
                                            </tr>
                                            </c:forEach>
                                        </tbody>
                                    </table>
                                </div>
                            </div>
                        </div>
                    </div>
                    <div class="tab-pane fade <c:if test='${ param.func=="mails" }'> in active</c:if>" id="mails">
                        <h2>Mail System</h2>
                        <hr style="border:0;background-color:#d4d4d4;height:1px;" />
                        <a class="btn btn-primary" role="button" href="admin/mails" target="_blank">OPEN MAIL PANEL</a>
                        
                    </div>
                    <div class="tab-pane fade <c:if test='${ param.func=="logs" }'> in active</c:if>" id="logs">
                        <h2>Admin Operation Logs</h2>
                        <hr style="border:0;background-color:#d4d4d4;height:1px;" />
                        <a class="btn btn-primary" role="button" href="admin/oplogs" target="_blank">CLICK TO SHOW ALL</a>
                        
                    </div>
                    <div class="tab-pane fade <c:if test='${ param.func=="config" }'> in active</c:if>" id="config">
                        <h2>System Config</h2>
                        <hr style="border:0;background-color:#d4d4d4;height:1px;" />
                        <form class="form-horizontal" onsubmit="javascript:return false;">
                        	<fieldset>
		                        <div class="form-group">
		                    		<div class="col-lg-12">
		                        		<h3>Max team members:</h3>
		                        		<input type="text" value="${ max_teammembers }" class="form-control" name="teammembers" style="width:30%">
		                    		</div>
		                		</div>
		                        <div class="form-group">
		                    		<div class="col-lg-12">
		                        		<h3>Competition start time:</h3>
		                        		<input type="text" value="<fmt:formatDate value="${ compstart }" pattern="yyyy-MM-dd HH:mm"/>" class="form-control form_datetime col-md-6" name="starttime" data-date-format="yyyy-mm-dd hh:ii" style="width:30%" placeholder="Leave it blank if do not want to set." > 
		                    		</div>
		                		</div>
		                		<div class="form-group">
		                    		<div class="col-lg-12">
		                        		<h3>Competition end time:</h3>
		                        		<input type="text" value="<fmt:formatDate value="${ compend }" pattern="yyyy-MM-dd HH:mm"/>" class="form-control form_datetime col-md-6" name="endtime" data-date-format="yyyy-mm-dd hh:ii" style="width:30%" placeholder="Leave it blank if do not want to set." >
		                    		</div>
		                		</div>
	                            <button type="submit" class="btn btn-primary" onclick="savesysconfig();">Save Config</button>
                            </fieldset>
                        </form>
                        
                    </div>
                    <div class="tab-pane fade <c:if test='${ param.func=="blacklist" }'> in active</c:if>" id="blacklist">
                        <h2>IP Blacklist</h2>
                        <hr style="border:0;background-color:#d4d4d4;height:1px;" />
                        <div class="panel panel-default">
                            <div class="panel-heading">
                                <div class="text-muted bootstrap-admin-box-title"><strong>Blocked IPs |</strong>&nbsp;
                                    <button class="btn btn-primary btn-xs" data-toggle="modal" data-target="#addip">Add</button>
                                    <div class="modal fade" id="addip" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
                                        <div class="modal-dialog modal-lg">
                                            <div class="modal-content">
                                                <div class="modal-header">
                                                    <button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>
                                                    <h4 class="modal-title" id="myModalLabel">Add IP to Blacklist</h4>
                                                </div>
                                                <div class="modal-body">
                                                    <form onsubmit="javascript:return false;">
                                                        <div class="form-group">
                                                            <label for="catgory-name" class="control-label">IP Address:</label>
                                                            <input type="text" class="form-control" id="block_ip_addr">
                                                        </div>
                                                    </form>
                                                </div>
                                                <div class="modal-footer">
                                                    <button type="button" class="btn btn-primary" data-dismiss="modal" onclick="addblockip();">Add</button>
                                                    <button type="button" class="btn btn-default" data-dismiss="modal">Close</button>
                                                </div>
                                            </div>
                                        </div>
                                    </div>
                                </div>
                            </div>
                            <div class="bootstrap-admin-panel-content">
                                <div class="table-responsive">
                                    <table class="table table-striped table-hover">
                                        <thead>
                                            <tr>
                                                <th>IP Address</th>
                                                <th>Operation</th>
                                            </tr>
                                        </thead>
                                        <tbody>
                                        	<c:forEach items="${ blackips }" var="ip">
                                            <tr>
                                                <td>${ ip.ipAddr }</td>
												<td>
                                                    <button class="btn btn-default btn-xs" data-toggle="modal" data-target="#editip" data-whatever="${ ip.ipAddr }" onclick="setrecordid(${ ip.id });">Edit</button>
                                                    <button class="btn btn-danger btn-xs" role="button" onclick="delblackip(${ ip.id },'${ ip.ipAddr }');" >Delete</button>
                                               	</td>
                                            </tr>
                                            </c:forEach>
                                        </tbody>
                                    </table>
                                </div>
                                <div class="modal fade" style="text-align:left;" id="editip" tabindex="-1" role="dialog" aria-labelledby="editcateLabel" aria-hidden="true">
	                                <div class="modal-dialog modal-lg">
	                                    <div class="modal-content">
	                                        <div class="modal-header">
	                                            <button type="button" class="close" data-dismiss="modal" aria-hidden="true" >&times;</button>
	                                            <h4 class="modal-title" id="editcateLabel">Edit IP Address</h4>
	                                        </div>
	                                        <div class="modal-body">
	                                            <form onsubmit="javascript:return false;">
	                                                <div class="form-group">
	                                                    <label for="category-name" class="control-label">IP Address:</label>
	                                                    <input type="text" class="form-control" id="edit-block_ip_addr">
	                                                </div>
	                                                <input type="hidden" name="ipaddrid" value=""/>
	                                            </form>
	                                        </div>
	                                        <div class="modal-footer">
	                                            <button type="button" class="btn btn-primary" onclick="editblockip();">Save changes</button>
	                                            <button type="button" class="btn btn-default" data-dismiss="modal">Close</button>
	                                        </div>
	                                    </div>
	                                </div>
	                            </div>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>
    <br/>
    
    
    <%@ include file="footer-admin.jsp"%>
    <form:form></form:form>
    <script type="text/javascript" src="vendors/jquery-confirm/jquery-confirm.min.js"></script>
    <script type="text/javascript" src="vendors/bootstrap-datetimepicker/js/bootstrap-datetimepicker.min.js"></script>
    <script type="text/javascript" src="assets/manage.js"></script>
    
</body>

</html>