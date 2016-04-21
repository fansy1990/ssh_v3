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