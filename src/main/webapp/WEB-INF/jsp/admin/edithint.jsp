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
	<!-- jQuery (necessary for Bootstrap's JavaScript plugins) -->
	<script type="text/javascript" src="//code.jquery.com/jquery-2.0.3.min.js"></script>
    <link rel="stylesheet" href="vendors/jquery-confirm/jquery-confirm.min.css" media="screen">
</head>

<body>
    <%@ include file="../nav.jsp"%>

    <div class="news-container">
        <div class="row">
            <h1 class="home-title">Edit Hint</h1>
            <hr style="border:0;background-color:#d4d4d4;height:1px;" />
        </div>
    </div>

    <div class="news-container">
        <div class="row">
            <div class="span12">
                <form:form class="form-horizontal" onsubmit="javascript:return false;">
                    <fieldset>
                        <div class="form-group">
                            <div class="col-lg-10">
                                <h3>Challenge</h3>
                                <select id="select01" class="form-control" style="width: 300px">
                                    <option disabled selected>-- Select A Challenge --</option>
                            		<c:forEach items="${allchalls}" var="ch">
                            			<option value="${ch.id}" <c:if test="${ thishint.challengeid==ch.id }">selected</c:if>>${ch.title}</option>
                            		</c:forEach>
                                </select>
                            </div>
                        </div>
                        <div class="form-group col-lg-12">
                            <h3>Hint Content</h3>
                            <textarea class="form-control" rows="10" style="width:100%" id="hintcontent">${ thishint.content }</textarea>
                        </div>
                        <br/>
                        
                        <button type="submit" class="btn btn-primary" onclick="doedithint(${ thishint.id });">Save</button>
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
    	function doedithint(id){
    		var challid = $('#select01 option:selected').val();
    		var content = $('#hintcontent').val();
    		var csrftoken = $("input[name='pctf_csrf_token']").val();
    		var postdata = {'challengeid':challid,'hint':content,'pctf_csrf_token':csrftoken};
    		$.post('admin/edithint/'+id,postdata ,function(data){
        		if (data.err==0) {
        			$.alert({
                	    title: false,
                	    content: data.errmsg,
                	    theme: 'bootstrap',
                	    confirmButtonClass: 'btn-success',
                	    confirm: function(){
                	    	window.location.replace('admin/manage?func=hints');
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