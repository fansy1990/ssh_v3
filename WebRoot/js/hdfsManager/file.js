$(function(){
	// 文件上传
	$('#dg_hdfsManager_file_upload_btn').bind('click', function(){
		var file_=$('#dg_hdfsManager_file_upload').textbox('getValue');
		console.info("file:"+file_);
		
//		// ajax 异步提交任务
//		var result = callByAJax('hdfs/hdfsManager_deleteFolder.action',{folder:folder_,recursive:recursive_});
//		if("true" == result.flag){
//			$.messager.alert('信息','目录删除成功!','info');
//		}else if("false" == result.flag){
//			$.messager.alert('信息','目录删除失败，'+result.msg,'info');
//		}
		
	});
	
});