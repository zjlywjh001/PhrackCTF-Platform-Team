<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="shiro" uri="http://shiro.apache.org/tags"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort()
			+ path + "/";
%>
<!DOCTYPE html>
<html lang="zh-CN">
<head>
<base href="<%=basePath%>">
<meta charset="UTF-8">
<%@ include file="top.jsp"%>
</head>

<body>
	<%@ include file="nav.jsp"%>
	<div class="news-container">
		<div class="row">
			<h1 class="home-title">Scoreboard</h1>
			<hr style="border: 0; background-color: #d4d4d4; height: 1px;" />
		</div>
	</div>
	<!--
    <div class="news-container">
        <div class="row">
        <div class="span12">
            <div class="block news-main">
                <h2 class="news-title">Trend</h2>
                <p class="news-meta">Last update time:&nbsp;&nbsp;<a href="#">2016-03-24 15:30</a></p>
                <hr>
                <div style="height:500px;border:1px solid #ccc;padding:10px;" id="score-graph"></div>
            </div>
        </div>
        </div>
</div>-->
	<div class="news-container">
		<div class="row">
			<div class="span12">
				<div class="block">
					<div style="height: 500px; border: 1px solid #ccc; padding: 10px; width:100%"
						id="score-graph"></div>
					<p class="news-meta">
						<font size="4px">Last update time:&nbsp;&nbsp;<a
							href="${ ctrlname }#"><span id="lastuptime"><fmt:formatDate value="${updatetime}"
									pattern="yyyy-MM-dd HH:mm:ss" /></span></a> (Update Every 5 minutes)</font>
					</p>
				</div>
			</div>
		</div>
	</div>

	<div class="news-container">
		<div class="row">
			<div class="span12">
				<div class="block">
					<div class="panel panel-default">
						<div class="panel-heading">
							<div class="text-muted bootstrap-admin-box-title">
								<strong>Ranklist<a href="scoretrend.json" style="text-decoration:none;">
								<img src="images/json.png" data-placement="right" title="JSON Ranklist" style="height:18px;width:18px;"/>
								</a> | <a href="personalrank" style="text-decoration:none;">Show Personal Ranklist</a></strong>
							</div>
						</div>
						<div class="bootstrap-admin-panel-content">
							<div class="table-responsive">
								<table class="table table-striped table-hover">
									<thead>
										<tr>
											<th>Rank</th>
											<th>Team</th>
											<th>Progress</th>
											<th>Score</th>
										</tr>
									</thead>
									<tbody>
										<c:forEach items="${ scorelist }" var="sb">
											<tr
												<c:if test="${ thisteam==sb.userid }">class="info"</c:if>>
												<td><c:if test="${ sb.rank==1 }">
														<img src="images/award_star_gold_3.png"
															data-placement="right" title="Gold" />
													</c:if> <c:if test="${ sb.rank==2 }">
														<img src="images/award_star_silver_3.png"
															data-placement="right" title="Silver" />
													</c:if> <c:if test="${ sb.rank==3 }">
														<img src="images/award_star_bronze_3.png"
															data-placement="right" title="Bronze" />
													</c:if> ${ sb.rank }</td>
												<td><a class="text-muted"
													style="text-decoration: none;" href="teaminfo/${ sb.userid }">${ sb.username }</a>&nbsp;&nbsp;<a href="country/${ sb.countrycode }" style="text-decoration:none;">
													<img src="images/flags/${ sb.countrycode }.png" data-placement="right" alt="${ sb.countrycode }" title="${ sb.countryname }" /></a></td>
												<td>
													<ul class="scoreboard-progress">
														<c:forEach items="${ sb.solvestat }" var="stat">
															<li><a href="challenges?taskid=${ stat.taskid }" class="process-bucket ${ stat.solvestr }" title="${ stat.tasktitle }"></a></li>
														</c:forEach>
													</ul>
												</td>
												<td>${ sb.score }</td>
											</tr>
										</c:forEach>
									</tbody>
								</table>
							</div>
						</div>
					</div>
				</div>
			</div>
		</div>
	</div>

	<%@ include file="footer.jsp"%>
	<script type="text/javascript" src="vendors/echarts.min.js"></script>
	<script type="text/javascript"
		src="vendors/echarts/theme/infographic.js"></script>
	<script type="text/javascript">
		function showtrend() {
			$.get('scoretrend.json?r='+ Math.floor(Math.random() * 100),function (data){
				var scores = data.scorelist;
				userdata = [];
				scoredata = [];
				count = 0;
				scores.forEach(function(e){
					if (count>=10)
						return false;
					userdata.push(e.username);
					aseries = {
							name: e.username,
							type: 'line',
							data: function(){
								list = [];
								for (var i=0;i<e.taskarr.length;i++) {
									list.push([new Date(e.timepoint[i]),
									           e.scorearr[i],
									           e.taskarr[i],
									]);
								}
								return list;
							}(),
							showAllSymbol : true,
							symbolSize : 8
					};
					scoredata.push(aseries);
					count ++;
				}); 
				var lasttime = new Date(data.lastupdate);
				var fmtlasttime = lasttime.getFullYear() + '-' + (lasttime.getMonth()<9?'0'+(lasttime.getMonth()+1):lasttime.getMonth()+1)
									+ '-' + (lasttime.getDate()<10?'0'+lasttime.getDate():lasttime.getDate())
									+ ' ' + lasttime.getHours()
									+ ':' + (lasttime.getMinutes()<10?'0'+lasttime.getMinutes():lasttime.getMinutes())
									+ ':' + (lasttime.getSeconds()<10?'0'+lasttime.getSeconds():lasttime.getSeconds());
				$('#lastuptime').text(fmtlasttime);
				var myChart = echarts.init(document.getElementById('score-graph'),
				'infographic');
				option = {
					title : {
						text : 'Trend'
					},
					tooltip : {
						trigger : 'item',
						formatter : function(params) {
							var date = new Date(params.value[0]);
							data = date.getFullYear() + '-' + (date.getMonth() + 1)
									+ '-' + date.getDate() + ' ' + date.getHours()
									+ ':' + (date.getMinutes()<10?'0'+date.getMinutes():date.getMinutes());
							return data + '<br/>' + params.value[1]+','+params.value[2];
						}
					},
					legend : {
						data : userdata
					},
					toolbox : {
						show : true,
						feature : {
							mark : {
								show : true
							},
							saveAsImage : {
								show : true
							}
		
						}
					},
					calculable : true,
					dataZoom : {
						show : true,
						start : 0
					},
					xAxis : [ {
						type : 'time',
						spiltNumber : 10
					} ],
					yAxis : [ {
						type : 'value'
					} ],
					series : scoredata
				};
				myChart.setOption(option);
			});
		}
		
		function update() {
			showtrend();
		}
		
		setInterval(update,300000);
		showtrend();
	</script>
</body>

</html>