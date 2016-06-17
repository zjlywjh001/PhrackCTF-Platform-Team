$(function(){
    $('#Container').mixItUp();
});

function taskviewed(id,solved){
	if ($('#panel-task'+id).hasClass("panel-danger")) {
		if (solved==true) {
			$('#panel-task'+id).removeClass("panel-danger").addClass("panel-success");
		} else {
			$('#panel-task'+id).removeClass("panel-danger").addClass("panel-default");
		}
		
		$('#new-'+id).text('');
		$.get('challenges?taskid='+id,function(data){
			var newtoken = $(data).find("input[name='pctf_csrf_token']").val();
			$("input[name='pctf_csrf_token']").val(newtoken);
		});
	}
}

function submitflag(id) {
	var flag = $('#flag-task'+id).val();
	if (flag=='') {
		return ;
	}
	var csrftoken = $("input[name='pctf_csrf_token']").val()
	var data = {'taskid':id,'flag':flag,'pctf_csrf_token':csrftoken};
	var result = 0;
	$.post('submitanswer.json',data,function (data) {
		if (data.err==0) {
			$('#result'+id).addClass("alert").addClass("alert-success").text(data.errmsg);
			$('#solves-task'+id).text(data.solves);
			$('#userscore').text(data.newscore);
			$('#userrank').text(data.newrank);
			result = 1;
		} else {
			if (data.err==-5) {
				$('#result'+id).addClass("alert").addClass("alert-warning").text(data.errmsg);
				$('#solves-task'+id).text(data.solves);
				$('#userscore').text(data.newscore);
				$('#userrank').text(data.newrank);
				result = 2;
			} else {
				$('#result'+id).addClass("alert").addClass("alert-danger").text(data.errmsg);
			}
		}
	},"json").error(function(jqXHR, textStatus, errorMsg) {
		if (jqXHR.status==403) {
			$('#result'+id).addClass("alert").addClass("alert-danger").text("Session time out!! Redirecting to login page..");
			setTimeout(function(){
				window.location.replace("login");
			},2000);
		} else {
			$('#result'+id).addClass("alert").addClass("alert-danger").text("Something wrong!!");
			setTimeout(function(){
				window.location.reload();
			},1500);
		}
		
	});
	$.get('challenges',function (data){
		var newtoken = $(data).find("input[name='pctf_csrf_token']").val();
		$("input[name='pctf_csrf_token']").val(newtoken);
	});
	setTimeout(function(){
		if (result>=1) {
			$('#panel-task'+id).removeClass("panel-default").addClass("panel-success");
			$('#flag-task'+id).parent().remove();
			$('#task'+id+'-btn').remove();
			if (result>=2) {
				$('#result'+id).removeClass("alert-warning").addClass("alert-success");
			} 
			$('#result'+id).text('Your Team have solved this Challenge!');
		} else {
			$('#result'+id).text('');
			$('#result'+id).attr('class','');
		}
	},3000);
	
}