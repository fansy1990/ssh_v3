$(function(){
	
	// 文件上传
	$('#dg_hdfsManager_file_upload_btn').bind('click', function(){
//		var file_=$('#dg_hdfsManager_file_upload').filebox('getValue');
//		console.info("file:"+file_);
//		
//		// ajax 异步提交任务
//		var result = callByAJax('hdfs/hdfsManager_upload.action',{file:file_});
//		if("true" == result.flag){
//			$.messager.alert('信息','目录删除成功!','info');
//		}else if("false" == result.flag){
//			$.messager.alert('信息','目录删除失败，'+result.msg,'info');
//		}
		// submit the form    
		$('#dg_hdfsManager_file_upload_ff').submit(); 
		
		
	});
	// 文件上传 
	$('#dg_hdfsManager_file_upload_ff').form({    
	    url:'hdfs/hdfsManager_upload.action',    
	    onSubmit: function(){    
	    	var file_=$('#dg_hdfsManager_file_upload_input').filebox('getValue');
	         if(file_=="") {
	        	 $.messager.alert('警告',"上传文件不能为空！",'warning');
	        	 return false;
	         }
	         if(checkTextBoxEmpty('dg_hdfsManager_file_upload_folder','上传目录不能为空，请输入！')) return false;
	         
	         // 权限及目录
	         return checkExistAndAuth($('#dg_hdfsManager_file_upload_folder').val(),'rwx');
	         
	         
	    },    
	    success:function(data){    
	        alert(data) ;   
	    }    
	});    
	
	
});