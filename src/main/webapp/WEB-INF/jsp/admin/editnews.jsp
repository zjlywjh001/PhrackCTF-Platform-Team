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
        	window.UMEDITOR_HOME_URL = "umeditor"
            window.um = UM.getEditor('newscontent');
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
            <h1 class="home-title">Edit News</h1>
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
                                <h3>News Title</h3>
                                <input type="text" class="form-control col-md-6" id="newstitle" autocomplete="off" data-provide="typeahead" value="${ news.title }">
                            </div>
                        </div>
                        <div class="form-group col-lg-12">
                            <h3>Content</h3>
                            <script id="newscontent" name="content" type="text/plain" style="height:500px;">
                            	<c:if test="${fn:length(news.content)==0}">
									<p></p>
								</c:if>
								${ news.content }
                            </script>
                        </div>
                        <button id="savenews" type="submit" class="btn btn-primary">Save changes</button>
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
    	$('#savenews').click(function (){
    		var title = $('#newstitle').val();
    		var content = window.um.getContent();
    		var csrftoken = $("input[name='pctf_csrf_token']").val();
    		var postdata = {'title':title,'content':content,'pctf_csrf_token':csrftoken};
    		$.post('admin/editnews/${ news.id }',postdata ,function(data){
    			if (data.err==0) {
        			$.alert({
                	    title: false,
                	    content: data.errmsg,
                	    theme: 'bootstrap',
                	    confirmButtonClass: 'btn-success',
                	    confirm: function(){
                	    	window.location.replace('admin/manage?func=news');
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
               	    			window.location.replace('admin/manage?func=news');
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
    	});
    </script>
</body>

</html>