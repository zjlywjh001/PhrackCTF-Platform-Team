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
            window.um = UM.getEditor('mailcontent');
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
            <h1 class="home-title">Mail Panel</h1>
            <hr style="border:0;background-color:#d4d4d4;height:1px;" />
        </div>
    </div>
    
    <div class="news-container">
        <div class="row">
            <div class="span12">
                <form:form class="form-horizontal">
                    <fieldset>
                    	<div class="form-group">
                            <div class="col-lg-12">
                                <legend>Recipients</legend>
                                <input type="text" class="form-control col-md-6" id="receiver" autocomplete="off" data-provide="typeahead" value="${ target }">
                                <a href="javascript:recvselect();"><strong>Select Receiver</strong></a><br/>
                                <span class="text-muted" style="font-size:15px">(Separate by "," if more than one)</span>
                            </div>
                        </div>
                        <div class="form-group">
                            <div class="col-lg-12">
                                <legend>Title</legend>
                                <input type="text" class="form-control col-md-6" id="mailtitle" autocomplete="off" data-provide="typeahead" placeholder="Please input title">
                            
                            </div>
                        </div>
                        <div class="form-group col-lg-12">
                            <legend>Content</legend>
                            <script id="mailcontent" name="content" type="text/plain" style="height:500px;">
								<p></p>
                            </script>
                        </div>
                        <button id="sendmail" type="submit" class="btn btn-primary" onclick="javascript:return false;">Send</button>
                    </fieldset>
                </form:form>
            </div>
        </div>
    </div>
    <br/>
    
    
    <%@ include file="footer-admin.jsp"%>
    <script type="text/javascript" src="vendors/jquery-confirm/jquery-confirm.min.js"></script>
    <script type="text/javascript">
    	function recvselect() {
    		window.open('admin/maillist','','status=no,scrollbars=yes,resizable=yes');
    	}
    	$('#sendmail').click(function (){
    		var recv = $('#receiver').val();
    		var title = $('#mailtitle').val();
    		var content = window.um.getContent();
    		var csrftoken = $("input[name='pctf_csrf_token']").val();
    		var postdata = {'maillist':recv,'title':title,'content':content,'pctf_csrf_token':csrftoken};
    		$.post('admin/maillist',postdata,function(ret){
    			if (ret.err==0) {
        			$.alert({
                	    title: false,
                	    content: ret.errmsg,
                	    theme: 'bootstrap',
                	    confirmButtonClass: 'btn-success',
                	    confirm: function(){
                	    	window.close();
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
    					if (ret.err==-99) {
            				$.alert({
                        	    title: false,
                        	    content: ret.errmsg,
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
    	});
    </script>
</body>

</html>