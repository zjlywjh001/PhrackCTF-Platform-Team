$(document).ready(function(){
    $('#editcate').on("show.bs.modal",function (event) {
        var button = $(event.relatedTarget);
        var recipient = button.data('whatever');

        var modal = $(this);
        modal.find('.modal-body input#edit-cate-name').val(recipient);
    });
    $('#editip').on("show.bs.modal",function (event) {
        var button = $(event.relatedTarget);
        var recipient = button.data('whatever');

        var modal = $(this);
        modal.find('.modal-body input#edit-block_ip_addr').val(recipient);
    });
    $(".form_datetime").datetimepicker({
        format: 'yyyy-mm-dd hh:ii'
    });
});
function delnews(id) {
	$.confirm({
	    title: false,
	    content: 'Sure to delete?',
	    theme: 'bootstrap',
	    confirmButtonClass: 'btn-danger',
	    cancelButtonClass: 'btn-success',
	    confirm: function(){
	    	var csrftoken = $("input[name='pctf_csrf_token']").val();
	    	var postdata = {'id':id,'pctf_csrf_token':csrftoken};
	    	$.post('admin/delnews',postdata,function(data){
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
	    	},'json').error(function(){
	    		$.alert({
	        	    title: false,
	        	    content: "Something Wrong!!!",
	        	    theme: 'bootstrap',
	        	    confirmButtonClass: 'btn-danger',
	        	    confirm: function(){
	        	    	window.location.replace('admin/manage?func=news');
	       	        }
	        	});
	    	});
	    }
	});
}

function saverule() {
	var content = window.um.getContent();
	var csrftoken = $("input[name='pctf_csrf_token']").val();
	var postdata = {'content':content,'pctf_csrf_token':csrftoken};
	$.post('admin/saverule',postdata ,function(data){
		if (data.err==0) {
			$.alert({
        	    title: false,
        	    content: data.errmsg,
        	    theme: 'bootstrap',
        	    confirmButtonClass: 'btn-success',
        	    confirm: function(){
        	    	window.location.replace('admin/manage?func=rule');
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
       	    			window.location.replace('admin/manage?func=rule');
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
    	    	window.location.replace('admin/manage?func=rule');
   	        }
    	});
	});
}

function addcate() {
	var catename = $('#add-cate-name').val();
	var mark = $("input[name='addcatemark']:checked").val();
	var csrftoken = $("input[name='pctf_csrf_token']").val();
	if (mark==null) {
		$.alert({
    	    title: false,
    	    content: "Please choose a label style!!",
    	    theme: 'bootstrap',
    	    confirmButtonClass: 'btn-danger'
    	});
	}
	var postdata = {'name':catename,'markid':mark,'pctf_csrf_token':csrftoken};
	$.post('admin/addcate.json',postdata ,function(data){
		if (data.err==0) {
			$.alert({
        	    title: false,
        	    content: data.errmsg,
        	    theme: 'bootstrap',
        	    confirmButtonClass: 'btn-success',
        	    confirm: function(){
        	    	window.location.replace('admin/manage?func=categories');
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
       	    			window.location.replace('admin/manage?func=categories');
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
    	    	window.location.replace('admin/manage?func=categories');
   	        }
    	});
	});
	
	
}

function setidmark(id,mark) {
	$("#catemark-"+mark).attr('checked',true);
	$("input[name='cateid']").val(id);
}

function setrecordid(id) {
	$("input[name='ipaddrid']").val(id);
}

function editcate() {
	var id = $("input[name='cateid']").val();
	var catename = $('#edit-cate-name').val();
	var mark = $("input[name='editcatemark']:checked").val();
	var csrftoken = $("input[name='pctf_csrf_token']").val();
	if (mark==null) {
		$.alert({
    	    title: false,
    	    content: "Please choose a label style!!",
    	    theme: 'bootstrap',
    	    confirmButtonClass: 'btn-danger'
    	});
	}
	var postdata = {'id':id,'name':catename,'markid':mark,'pctf_csrf_token':csrftoken};
	$.post('admin/editcate.json',postdata ,function(data){
		if (data.err==0) {
			$.alert({
        	    title: false,
        	    content: data.errmsg,
        	    theme: 'bootstrap',
        	    confirmButtonClass: 'btn-success',
        	    confirm: function(){
        	    	window.location.replace('admin/manage?func=categories');
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
       	    			window.location.replace('admin/manage?func=categories');
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
    	    	window.location.replace('admin/manage?func=categories');
   	        }
    	});
	});
}

function delcate(id) {
	$.confirm({
	    title: false,
	    content: 'Warning !! Are you Sure? This operation will also delete all challenges and its submission records in this category.',
	    theme: 'bootstrap',
	    confirmButtonClass: 'btn-danger',
	    cancelButtonClass: 'btn-success',
	    confirm: function(){
	    	var csrftoken = $("input[name='pctf_csrf_token']").val();
	    	var postdata = {'id':id,'pctf_csrf_token':csrftoken};
	    	$.post('admin/delcate',postdata,function(data){
	    		if (data.err==0) {
	    			$.alert({
	    				title: false,
                	    content: data.errmsg,
                	    theme: 'bootstrap',
                	    confirmButtonClass: 'btn-success',
               	    	confirm: function(){
               	    		window.location.replace('admin/manage?func=categories');
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
               	    			window.location.replace('admin/manage?func=categories');
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
	        	    	window.location.replace('admin/manage?func=categories');
	       	        }
	        	});
	    	});
	    }
	});
}

function delchallenge(id) {
	$.confirm({
	    title: false,
	    content: 'Warning !! Are you Sure? This operation will also delete all submissions and hints of this challenge.',
	    theme: 'bootstrap',
	    confirmButtonClass: 'btn-danger',
	    cancelButtonClass: 'btn-success',
	    confirm: function(){
	    	var csrftoken = $("input[name='pctf_csrf_token']").val();
	    	var postdata = {'id':id,'pctf_csrf_token':csrftoken};
	    	$.post('admin/deltask',postdata,function(data){
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
               	    			window.location.replace('admin/manage?func=challenges');
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
	        	    	window.location.replace('admin/manage?func=challenges');
	       	        }
	        	});
	    	});
	    }
	});
}


function delhint(id) {
	$.confirm({
	    title: false,
	    content: 'Sure to delete?',
	    theme: 'bootstrap',
	    confirmButtonClass: 'btn-danger',
	    cancelButtonClass: 'btn-success',
	    confirm: function(){
	    	var csrftoken = $("input[name='pctf_csrf_token']").val();
	    	var postdata = {'id':id,'pctf_csrf_token':csrftoken};
	    	$.post('admin/delhint',postdata,function(data){
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
               	    			window.location.replace('admin/manage?func=hints');
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
	        	    	window.location.replace('admin/manage?func=hints');
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
	    			window.location.replace('admin/manage?func=submissions');
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
	           	    			window.location.replace('admin/manage?func=submissions');
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
	        	    	window.location.replace('admin/manage?func=submissions');
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
	    			window.location.replace('admin/manage?func=submissions');
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
	           	    			window.location.replace('admin/manage?func=submissions');
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
	        	    	window.location.replace('admin/manage?func=submissions');
	       	        }
	        	});
	    	});
	    }
	});
}

function savesysconfig() {
	var membernum = $("input[name='teammembers']").val();
	var starttime = $("input[name='starttime']").val();
	var endtime = $("input[name='endtime']").val();
	var csrftoken = $("input[name='pctf_csrf_token']").val();
	var postdata = {'maxmem':membernum,'starttime':starttime,'endtime':endtime,'pctf_csrf_token':csrftoken};
	$.post('admin/saveconfig.json',postdata ,function(data){
		if (data.err==0) {
			$.alert({
        	    title: false,
        	    content: data.errmsg,
        	    theme: 'bootstrap',
        	    confirmButtonClass: 'btn-success',
        	    confirm: function(){
        	    	window.location.replace('admin/manage?func=config');
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
       	    			window.location.replace('admin/manage?func=config');
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
    	    	window.location.replace('admin/manage?func=config');
   	        }
    	});
	});
}


function addblockip() {
	var ip_addr = $('#block_ip_addr').val();
	var csrftoken = $("input[name='pctf_csrf_token']").val();
	var postdata = {'ip_addr':ip_addr,'pctf_csrf_token':csrftoken};
	$.post('admin/addblockip.json',postdata ,function(data){
		if (data.err==0) {
			$.alert({
        	    title: false,
        	    content: data.errmsg,
        	    theme: 'bootstrap',
        	    confirmButtonClass: 'btn-success',
        	    confirm: function(){
        	    	window.location.replace('admin/manage?func=blacklist');
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
       	    			window.location.replace('admin/manage?func=blacklist');
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
    	    	window.location.replace('admin/manage?func=blacklist');
   	        }
    	});
	});
	
	
}

function editblockip() {
	var id = $("input[name='ipaddrid']").val();
	var ip_addr = $('#edit-block_ip_addr').val();
	var csrftoken = $("input[name='pctf_csrf_token']").val();
	var postdata = {'id':id,'ip_addr':ip_addr,'pctf_csrf_token':csrftoken};
	$.post('admin/editblockip.json',postdata ,function(data){
		if (data.err==0) {
			$.alert({
        	    title: false,
        	    content: data.errmsg,
        	    theme: 'bootstrap',
        	    confirmButtonClass: 'btn-success',
        	    confirm: function(){
        	    	window.location.replace('admin/manage?func=blacklist');
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
       	    			window.location.replace('admin/manage?func=blacklist');
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
    	    	window.location.replace('admin/manage?func=blacklist');
   	        }
    	});
	});
	
	
}

function delblackip(id,addr) {
	$.confirm({
	    title: false,
	    content: 'Sure to delete record '+addr+'?',
	    theme: 'bootstrap',
	    confirmButtonClass: 'btn-danger',
	    cancelButtonClass: 'btn-success',
	    confirm: function(){
	    	var csrftoken = $("input[name='pctf_csrf_token']").val();
	    	var postdata = {'id':id,'pctf_csrf_token':csrftoken};
	    	$.post('admin/delblockip.json',postdata,function(data){
	    		if (data.err==0) {
	    			$.alert({
	    				title: false,
                	    content: data.errmsg,
                	    theme: 'bootstrap',
                	    confirmButtonClass: 'btn-success',
               	    	confirm: function(){
               	    		window.location.replace('admin/manage?func=blacklist');
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
               	    			window.location.replace('admin/manage?func=blacklist');
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
	        	    	window.location.replace('admin/manage?func=blacklist');
	       	        }
	        	});
	    	});
	    }
	});
}

