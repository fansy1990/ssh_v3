




function import2hbase(){
	console.info("import to hbase");
	// TODO 检查HDFS数据路径是否存在，如果不存在给出提示，并返回
	var hdfs = $('#import2hbase_input').val();
	if(hdfs == null || hdfs == ""){
		$.messager.alert('提示',"目录为空，请重新输入!",'info');
		return ;
	}
	var ret = callByAJax("hdfs/hdfsManager_checkExist.action", {folder:hdfs})
	if(ret.flag == "false"){
		$.messager.alert('提示',"目录不存在，请重新输入!",'info');
		return ;
	}
	// 检查HBase表是否存在，表列簇是否对应，如果不存在则给出提示，如果列簇不对应也给出提示，和列描述对应起来
	var table = $('#import2hbase_table').val();
	if(table == null || table == ""){
		$.messager.alert('提示',"表名为空，请重新输入!",'info');
		return ;
	}
	var colDescription = $('#import2hbase_columnDescription').val();
	if(colDescription == null || colDescription == ""){
		$.messager.alert('提示',"列描述为空，请重新输入!",'info');
		return ;
	}
	ret = callByAJax("hbase/hbaseCommand_checkExistAndFamily.action", {tableName:table,cfs:colDescription})
	if(ret.flag == "false"){
		$.messager.alert('提示',ret.msg,'info');
		return ;
	}
	// 提交导入任务，返回正常后，弹出进度条，进度条使用真实MR进度条  
	
	 $.messager.progress({
	     title:'提示',
	     msg:'导入数据中...',
	     interval:0    //disable auto update progress value
	 });
	
	var jobId="abcdefg";
	if(typeof(EventSource)!=="undefined")
	  {
	  var source=new EventSource("hadoop/hadoop_getMRProgress.action"+"?jobId="+ jobId );
	  source.onmessage=function(event)
	    {
		  console.info(event.data);
		  var bar = $.messager.progress('bar');
		  bar.progressbar('setValue',  event.data);
		  
		  
		  if(event.data=="100"){
			  source.close();
			  $.messager.progress('close');
		  }
		  
	    };
	    /*source.onopen=function(event){
	    	console.info("start-");
	    	
	    };*/
	    
	    /*source.onerror=function(event){
	    	console.info("error");
	    	console.info(event);
	    	console.info(event.data);
	    };*/
	  }
	else
	  {
	  console.info("Sorry, your browser does not support server-sent events...");
	  }
}

function init_import2hbase(){
	console.info("init import to hbase")
}