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
	    	console.info(file_);
	         if(file_=="") {
	        	 $.messager.alert('警告',"上传文件不能为空！",'warning');
	        	 return false;
	         }
	         if(checkTextBoxEmpty('dg_hdfsManager_file_upload_folder','上传目录不能为空，请输入！')) return false;
	         
	         // 权限及目录
	         var flag = checkExistAndAuth($('#dg_hdfsManager_file_upload_folder').val(),'rwx');
	         
	         if(flag) popupProgressbar('数据上传','数据上传中...',1000);
	         

	         return flag;
	         
	    },    
	    success:function(data){    
	    	closeProgressbar();
	    	$.messager.alert('信息','文件上传完成！','info');
	    }    
	});    
	
	// 隐藏upload 中fileName属性
//	$('#dg_hdfsManager_file_upload_fileName').hide();
	
	// 文件删除
	$('#dg_hdfsManager_file_delete_btn').bind('click', function(){
		if(checkTextBoxEmpty('dg_hdfsManager_file_delete','文件路径不能为空，请重新输入!')) return ;
		
		// 权限及目录
        var flag = checkExistAndAuth($('#dg_hdfsManager_file_delete').val(),'x');
        if(!flag) return;
        
		var file_=$('#dg_hdfsManager_file_delete').val();
		// ajax 异步提交任务
		var result = callByAJax('hdfs/hdfsManager_deleteFile.action',{fileName:file_});
		if("true" == result.flag){
			$.messager.alert('信息','文件删除成功!','info');
		}else if("false" == result.flag){
			$.messager.alert('信息','文件删除失败，'+result.msg,'info');
		}
		
	});
	
	// 文件下载
	$('#dg_hdfsManager_file_download_btn').bind('click', function(){
		if(checkTextBoxEmpty('dg_hdfsManager_file_download_src','HDFS文件路径不能为空，请重新输入!')) return ;
		if(checkTextBoxEmpty('dg_hdfsManager_file_download_des','本地文件路径不能为空，请重新输入!')) return ;
		// 权限及目录
        var flag = checkExistAndAuth($('#dg_hdfsManager_file_download_src').val(),'r');
        if(!flag) return;
        
		var file_=$('#dg_hdfsManager_file_download_src').val();
		var des_=$('#dg_hdfsManager_file_download_des').val();
		// ajax 异步提交任务
		var result = callByAJax('hdfs/hdfsManager_download.action',{fileName:file_,localFile:des_});
		if("true" == result.flag){
			$.messager.alert('信息','文件下载成功!','info');
		}else if("false" == result.flag){
			$.messager.alert('信息','文件下载失败，'+result.msg,'info');
		}
		
	});
	
	// 文件读取
	$('#dg_hdfsManager_file_read_btn').bind('click', function(){
		if(checkTextBoxEmpty('dg_hdfsManager_file_read_file','文件路径不能为空，请重新输入!')) return ;
		
		// 权限及目录
        var flag = checkExistAndAuth($('#dg_hdfsManager_file_read_file').val(),'r');
        if(!flag) return;
        
		var file_=$('#dg_hdfsManager_file_read_file').val();
		var text_seq= $('#dg_hdfsManager_file_read_text_seq').combobox('getValue');
		var records_= $('#dg_hdfsManager_file_read_records').combobox('getValue');
		// ajax 异步提交任务
		var result = callByAJax('hdfs/hdfsManager_read.action',{fileName:file_,textSeq:text_seq,records:records_});
		if("true" == result.flag){
//			$.messager.alert('信息','文件删除成功!','info');
			// 文件读取结果展示；
			$('#retId').show();
			$('#retId').html("<hr>"+result.data+"<hr>");
		}else if("false" == result.flag){
//			$('#retId').html(result.data);
			$('#retId').hide();
			$.messager.alert('信息','文件读取失败，'+result.msg,'info');
		}
		
	});
	
});