/**
 * 检查目录是否存在或权限错误
 * @param folder_
 * @param auth_
 * @returns {Boolean}
 */
function checkExistAndAuth(folder_,auth_){
	var flag =false;
	var result = callByAJax('hdfs/hdfsManager_checkExistAndAuth.action',{folder:folder_,auth:auth_});
	console.info("result.flag:"+result.flag);
	if("false" == result.flag){
		flag=false;
		console.info("目录没有权限或后台错误！");
		$.messager.alert('警告',result.msg,'warning');
	}else if("nodir" == result.flag){
		flag=false;
		console.info("目录不存在");
		$.messager.alert('警告','目录不存在，请重新输入!','warning');
	}else if("true" == result.flag){
		flag =true;
	}
//	$.ajax({
//		url : 'hdfs/hdfsManager_checkExistAndAuth.action',
//		data: {folder:param.folder},
//		async:true,
//		dataType:"json",
//		async: false,
//		context : document.body,
//		success : function(data) {
//			console.info("data:"+data.flag);
//			var retMsg;
//			if("noauth" == data.flag){
//				flag=false;
//				console.info("目录没有权限");
//				$.messager.alert('警告','目录没有权限，请重新输入!','warning');
//			}else if("nodir" == data.flag){
//				flag=false;
//				console.info("目录不存在");
//				$.messager.alert('警告','目录不存在，请重新输入!','warning');
//			}else if("true" == data.flag){
//				flag =true;
//			}
//		}
//	});
//	console.info("flag:"+flag);
	
	return flag;
}


$(function(){
	// 权限验证
	$('#authCheckId').bind('click', function(){
		if(checkTextBoxEmpty('hdfsUser','用户不能为空，请重新输入!')) return ;
		if(checkTextBoxEmpty('hdfsPassword','密码不能为空，请重新输入!')) return ;
	      
		var hUser=$('#hdfsUser').val();
		var hPassword=$('#hdfsPassword').val();
		// ajax 异步提交任务
		var result = callByAJax('user/hdfsUser_authCheck.action',{hdfsUserName:hUser,hdfsPassword:hPassword});
		if("true" == result.flag){
			$.messager.alert('信息','HDFS用户验证成功!','info');
		}else if("false" == result.flag){
			$.messager.alert('信息','HDFS用户验证失败，'+result.msg,'info');
		}
		
	});
	
	// 权限更新
	$('#authUpdateId').bind('click', function(){
		if(checkTextBoxEmpty('hdfsUser','用户不能为空，请重新输入!')) return ;
		if(checkTextBoxEmpty('hdfsPassword','密码不能为空，请重新输入!')) return ;
	      
		var hUser=$('#hdfsUser').val();
		var hPassword=$('#hdfsPassword').val();
		// ajax 异步提交任务
		var result = callByAJax('user/hdfsUser_authUpdate.action',{hadoopUserName:hUser,hadoopPassword:hPassword});
		if("true" == result.flag){
			$.messager.alert('信息','HDFS用户更新成功!','info');
		}else if("false" == result.flag){
			$.messager.alert('信息','HDFS用户更新失败，'+result.msg,'info');
		}
		
	});

});
