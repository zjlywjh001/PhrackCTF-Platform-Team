<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form" %>  
<%@page import="java.util.Date"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://"
			+ request.getServerName() + ":" + request.getServerPort()
			+ path + "/";
	long nowtime = new Date().getTime();
%>
<!DOCTYPE html>
<html lang="zh-CN">

<head>
    <base href="<%=basePath%>">
    <meta charset="UTF-8">
    <%@ include file="../top.jsp"%>
	<link rel="stylesheet" media="screen" href="vendors/bootstrap-datetimepicker/css/bootstrap-datetimepicker.min.css">
    <link rel="stylesheet" href="vendors/jquery-confirm/jquery-confirm.min.css" media="screen">
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
    <link href="vendors/bootstrap-fileinput/css/fileinput.min.css" media="all" rel="stylesheet" type="text/css" />
	<script src="vendors/bootstrap-fileinput/js/fileinput.min.js"></script>
	<!-- bootstrap.js below is only needed if you wish to the feature of viewing details
     of text file preview via modal dialog -->
     
    <!-- 实例化编辑器代码 -->
    <script type="text/javascript">
        $(function () {
            window.um = UM.getEditor('probcontent');
            um.setWidth("100%");
            $(".edui-body-container").css("width", "98%");
            $("#file_upload").fileinput({
            	uploadUrl: 'admin/fileUpload', //上传的地址
            	showPreview:false,
            	maxFileCount:100,
            	enctype: 'multipart/form-data',
            	msgFilesTooMany: '100 files once only!!'
            });
        });
    </script>
</head>

<body>
    <%@ include file="../nav.jsp"%>

    <div class="news-container">
        <div class="row">
            <h1 class="home-title">Add Challenge</h1>
            <hr style="border:0;background-color:#d4d4d4;height:1px;" />
        </div>
    </div>

    <div class="news-container">
        <div class="row">
            <div class="span12">
                <form:form class="form-horizontal" onsubmit="javascript:return false;">
                    <fieldset>
                        <div class="form-group">
                            <div class="col-lg-12">
                                <legend>Title</legend>
                                <input type="text" class="form-control col-md-6" id="tasktitle" autocomplete="off" data-provide="typeahead" style="width:50%">
                            </div>
                        </div>
                        <div class="form-group">
                            <div class="col-lg-12">
                                <legend>Score</legend>
                                <input type="text" class="form-control col-md-6" id="taskscore" autocomplete="off" data-provide="typeahead" style="width:100px">
                            </div>
                        </div>
                        <div class="form-group">
                            <div class="col-lg-12">
                                <legend>Category</legend>
                                <select id="select01" name="cateselector" class="form-control" style="width: 300px">
                                	<option disabled selected>-- Select A Category --</option>
                            		<c:forEach items="${allcates}" var="ct">
                            			<option value="${ct.id}">${ct.name}</option>
                            		</c:forEach>
                                </select>
                            </div>
                        </div>
                        <div class="form-group col-lg-12">
                            <legend>Description</legend>
                            <script id="probcontent" name="content" type="text/plain" style="height:500px;">
                                <p></p>
                            </script>
                        </div>
                        <br/>
                        <div class="form-group">
                        	<div class="col-lg-12" id="attachdiv">
                            	<legend>Attachment</legend>
                            	<input type="file" name="file_upload" id="file_upload">
                            </div>
                        </div>
                        <div class="form-group">
                            <div class="col-lg-12">
                                <legend>FLAG</legend>
                                <input type="text" class="form-control col-md-6" id="flag"  autocomplete="off" data-provide="typeahead" place>
                            </div>
                        </div>
                        <div class="checkbox">
                            <label>
                                <input name="isexposed" type="checkbox" checked="checked"><font size="3px"><strong>Exposed</strong></font>
                            </label>
                        </div>
                        <br/>
                        <div class="form-group">
                            <div class="col-lg-12">
                                <legend>Available From</legend>
                                <input type="text" value="<fmt:formatDate value="<%=new Date(nowtime) %>" pattern="yyyy-MM-dd HH:mm"/>" class="form-control form_datetime col-md-6" name="availfrom" data-date-format="yyyy-mm-dd hh:ii">
                            </div>
                        </div>
                        <div class="form-group">
                            <div class="col-lg-12">
                                <legend>Available Until</legend>
                                <input type="text" value="<fmt:formatDate value="<%=new Date(nowtime+86400000) %>" pattern="yyyy-MM-dd HH:mm"/>" class="form-control form_datetime col-md-6" name="availuntil" data-date-format="yyyy-mm-dd hh:ii">
                            </div>
                        </div>
                        <button type="submit" class="btn btn-primary" onclick="doaddchallenge();">Save</button>
                    </fieldset>
                </form:form>
            </div>
        </div>
    </div>
    <br/>


    <br/>
    <%@ include file="footer-admin.jsp"%>
    <script type="text/javascript" src="vendors/bootstrap-datetimepicker/js/bootstrap-datetimepicker.min.js"></script>
    <script type="text/javascript" src="vendors/jquery-confirm/jquery-confirm.min.js"></script>
    <script type="text/javascript">
    	var att = $('#attachdiv');
        $(".form_datetime").datetimepicker({
            format: 'yyyy-mm-dd hh:ii'
        });
        $('#file_upload').on('fileuploaded', function(event, data, previewId, index) {
        	var resp = data.response;
        	var filehtml="<a id='file"+resp.fileid+"' href='"+resp.index+"' style='text-decoration:none;'><font size='3'>"+resp.filename+"</font>"
        	+"<input type='hidden' name='attachlist' value='"+resp.fileid+"' /></a>&nbsp;&nbsp;&nbsp;"
        	+"<a id='filerm"+resp.fileid+"' href='javascript:removeattach("+resp.fileid+");' style='text-decoration:none;'><span class='glyphicon glyphicon-remove' style='color:#ff0000;' title='Remove'>"+
        	"</span><br/></a>";
        	att.append(filehtml);
    	});
        function removeattach(id) {
        	$('#file'+id).remove();
        	$('#filerm'+id).remove();
        }
        function doaddchallenge(){
        	var title = $('#tasktitle').val();
        	var score = $('#taskscore').val();
        	var category = $('#select01 option:selected').val();
        	var descript = window.um.getContent();
        	var attachlist = '';
        	$("input[name='attachlist']").each(function(){
        		if (attachlist==''){
        			attachlist = attachlist + $(this).val();
        		} else {
        			attachlist = attachlist +'|'+ $(this).val();
        		}
        	});
        	var flag=$('#flag').val();
        	var isexposed = $("input[name='isexposed']").is(':checked');
        	var availtime = $("input[name='availfrom']").val();
        	var availuntil = $("input[name='availuntil']").val();
        	var csrftoken = $("input[name='pctf_csrf_token']").val();
        	var postdata={
        		'title':title,
        		'score':score,
        		'category':category,
        		'description':descript,
        		'attaches':attachlist,
        		'flag':flag,
        		'isexposed':isexposed,
        		'available':availtime,
        		'invalidate':availuntil,
        		'pctf_csrf_token':csrftoken
        	};
        	$.post('admin/addchallenge.json',postdata ,function(data){
        		if (data.err==0) {
        			$.alert({
                	    title: false,
                	    content: data.errmsg,
                	    theme: 'bootstrap',
                	    confirmButtonClass: 'btn-success',
                	    confirm: function(){
                	    	window.location.replace('admin/manage?func=challenges');
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
</body>

</html>