<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<title>批量上传</title>
<link rel="Stylesheet" href="css/uploadify.css" />
<script type="text/javascript" src="script/jquery-1.8.3.min.js"></script>
<script type="text/javascript" src="script/jquery.uploadify3.js"></script>
<style type="text/css">
body {
	font-size: 14px;
	font-family: 微软雅黑;
}

#custom-demo .uploadifyQueueItem {
	background-color: #FFFFFF;
	border: none;
	border-bottom: 1px solid #E5E5E5;
	font: 11px Verdana, Geneva, sans-serif;
	height: 20px;
	margin-top: 0;
	padding: 10px;
	width: 350px;
}

#custom-demo #custom-queue {
	border: 1px solid #E5E5E5;
	margin-bottom: 10px;
	width: 370px;
}

#custom-demo object {
	float: left;
}

.button {
	float: right;
	width: 120px;
	height: 30px;
	background-color: #525252;
	color: #fff;
	border: 0;
	font-size: 14px;
	font-family: 微软雅黑;
	cursor: pointer;
	margin-right: 10px;
	_margin-right: 7px;
}
</style>
<script type="text/javascript">
	$(function() {
		$('#custom_file_upload')
				.uploadify(
						{
							// 是否自动上传
							'auto' : true,
							'fileObjName' : 'imageObj',
							'buttonText' : '选择照片',
							// flash
							'swf' : "script/uploadify.swf",
							// 文件选择后的容器ID
							'queueID' : 'uploadfileQueue',
							'uploader' : '/consult/common/uploadcomponent',
							'multi' : false,
							'fileTypeDesc' : '支持的格式：',
							'fileTypeExts' : '*.jpg; *.png; *.jpeg',
							'fileSizeLimit' : '5MB',
							// 返回一个错误，选择文件的时候触发
							'onSelectError' : function(file, errorCode,
									errorMsg) {
								if (errorCode == SWFUpload.QUEUE_ERROR.FILE_EXCEEDS_SIZE_LIMIT) {
									$(".prompt").text("上传文件不能超过5M").css(
											"color", "red").show();
								} else if (errorCode == SWFUpload.QUEUE_ERROR.INVALID_FILETYPE) {
									$(".prompt").text("请上传jpg,jpeg,png格式的图片")
											.css("color", "red").show();
								} else {
									$(".prompt").text("上传文件出错,请稍候重试").css(
											"color", "red").show();
								}
							},
							// 检测FLASH失败调用
							'onFallback' : function() {
								alert("您未安装FLASH控件，无法上传图片！请安装FLASH控件后再试。");
							},
							// 上传到服务器，服务器返回相应信息到data里
							'onUploadSuccess' : function(file, data, response) {
								var result = $.parseJSON(data);
								$("#img0span").hide();
								$("#img0").attr("src", result.picUrl);
								$("#img0").val(result.picPath);
							}
						});

		$("#Up").click(function() {
			if ($("#custom-queue").html() == "") {
				alert("请选择要上传的文件");
			} else {
				window.parent.document.getElementById('').value = "";
				$('#custom_file_upload').uploadifyUpload(null, false); //一个一个传
				//$('#custom_file_upload').uploadifyUpload(null, true);//同时传
			}
		});

		$("#Clear").click(function() {
			$('#custom_file_upload').uploadifyClearQueue();
		});

	});
</script>

</head>
<body>
	<div id="custom-demo" class="demo">
		<div class="demo-box">
			<div id="status-message">请选择要上传的文件:</div>
			<div id="custom-queue"></div>
			<input id="custom_file_upload" type="file" name="Filedata"
				class="button" /> <input type="button" id="Up" name="Up" value="上传"
				class="button" /> <input type="button" id="Clear" name="Clear"
				value="清空" class="button" />
		</div>
	</div>
</body>
</html>