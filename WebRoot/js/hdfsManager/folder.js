$(function(){
	
	// folder参数
	var folder_=$('#hdfsManager_list_folder').val();
	// 目录查看
	$('#dg_hdfsManager_list').datagrid({
		border : false,
		fitColumns : false,
		singleSelect : true,
		width : 600,
		height : 250,
		nowrap : false,
		fit : true,
		pagination : true,// 分页控件
		pageSize : 4, // 每页记录数，需要和pageList保持倍数关系
		pageList : [ 4, 8, 12 ],
		rownumbers : true,// 行号
		pagePosition : 'top',
		url : 'hdfs/hdfsManager_listFolder.action',
//		url:'',
		queryParams: {
			folder: folder_
		},
		onLoadError:function(){
			console.info("list load error!");
			 $.messager.alert('信息','加载错误，请联系管理员!','info');
		},
		onBeforeLoad:function(param){
			return checkExistAndAuth(param.folder,'rx');
		},
		idField:'id',
		columns :[[
				{
					field : 'name',
					title : '文件名',
					width : '120'
				},{
					field : 'type',
					title : '类型',
					width : '50'
				},{
					field : 'size',
					title : '大小',
					width : '100'
				},{
					field : 'replication',
					title : '副本数',
					width : '50',
				},{
					field : 'blockSize',
					title : '块大小',
					width : '100'
				},{
					field : 'modificationTime',
					title : '修改时间',
					width : '200'
				},{
					field : 'permission',
					title : '权限',
					width : '150'
				},{
					field : 'owner',
					title : '所有者',
					width : '100'
				},{
					field : 'group',
					title : '组名',
					width : '100'
				}
				 ]]
		    }); 
	
	// 目录检索
	$('#dg_hdfsManager_search').datagrid({
		border : false,
		fitColumns : false,
		singleSelect : true,
		width : 600,
		height : 250,
		nowrap : false,
		fit : true,
		pagination : true,// 分页控件
		pageSize : 4, // 每页记录数，需要和pageList保持倍数关系
		pageList : [ 4, 8, 12 ],
		rownumbers : true,// 行号
		pagePosition : 'top',
		url : 'hdfs/hdfsManager_searchFolder.action',
		queryParams: {
			folder: "/",
			name:"",
			nameOp:"no",
			owner:"",
			ownerOp:"no"
		},
		onLoadError:function(){
			console.info("load error!");
			 $.messager.alert('警告','读取错误，请联系管理员!','warning');
		},
		onBeforeLoad:function(param){
			return checkExistAndAuth(param.folder,'rx');
		},
		onLoadSuccess:function(data ){
			console.info("success,data:"+data);
		},
		idField:'id',
		columns :[[
				{
					field : 'name',
					title : '文件名',
					width : '120',
					formatter: function(value,row,index){
//						var ret = "<a href='javascript:void(0);' onclick='refreshDir(";
//						ret = ret + "'" + value +"','"+row.type+"')"
//						var v1 = value;
//						var v2 = row.type;
						// 使用转义即可解决单引号、双引号不够用的问题
						return "<a href='javascript:void(0);' onclick='refreshDir(\""+value+"\",\""+row.type+"\")"+"'>"+value+"</a>";
//						return "<a href='javascript:void(0);' onclick='refreshDir(v1,v2)"+"'>"+value+"</a>";
						
//						var ref_href = $("<a href='javascript:void(0);'>"+value+"</a>");
////						ref_href.live('click',function(v1,v2){
////							console.info("name:"+v1);
////							console.info("type:"+v2);
////						});
//						console.info("v1:"+v1);
//						ref_href.bind('click',function(v1,v2){
//							console.info("name:"+v1);
//							console.info("type:"+v2);
//							alter.show('a');
//						});
//						return ref_href[0].outerHTML;
						
					}

				},{
					field : 'type',
					title : '类型',
					width : '50'
				},{
					field : 'size',
					title : '大小',
					width : '100'
				},{
					field : 'replication',
					title : '副本数',
					width : '50',
				},{
					field : 'blockSize',
					title : '块大小',
					width : '100'
				},{
					field : 'modificationTime',
					title : '修改时间',
					width : '200'
				},{
					field : 'permission',
					title : '权限',
					width : '150'
				},{
					field : 'owner',
					title : '所有者',
					width : '100'
				},{
					field : 'group',
					title : '组名',
					width : '100'
				}
				 ]]
		    });
	
	
	// 目录新建
	$('#dg_hdfsManager_folder_add_btn').bind('click', function(){
		var folder_=$('#dg_hdfsManager_folder_add').val();
		
		// ajax 异步提交任务
		var result = callByAJax('hdfs/hdfsManager_createFolder.action',{folder:folder_});
		if("true" == result.flag){
			$.messager.alert('信息','目录创建成功!','info');
		}else if("false" == result.flag){
			$.messager.alert('信息','目录创建失败，'+result.msg,'info');
		}else if("hasdir" == result.flag){
			$.messager.alert('warning','目录已经存在!','warning');
		}
		
	});
	
	// 目录删除
	$('#dg_hdfsManager_folder_delete_btn').bind('click', function(){
		var folder_=$('#dg_hdfsManager_folder_delete').val();
		var recursive_ =$('#dg_hdfsManager_folder_delete_cc').combobox('getValue');
		// ajax 异步提交任务
		var result = callByAJax('hdfs/hdfsManager_deleteFolder.action',{folder:folder_,recursive:recursive_});
		if("true" == result.flag){
			$.messager.alert('信息','目录删除成功!','info');
		}else if("false" == result.flag){
			$.messager.alert('信息','目录删除失败，'+result.msg,'info');
		}
		
	});
	
});

function refreshDir(name,type){
	if(type != 'dir'){
		$.messager.alert('信息','您点击的是非目录！','info');
		return ;
	}
	var curr =$('#hdfsManager_search_folder').val();
	if(curr == '/'){
		$('#hdfsManager_search_folder').val(curr+name);
	}else{
		$('#hdfsManager_search_folder').val(curr+'/'+name);
	}
	
	search_data();
}

/**
 * 重新加载数据
 */
function list_data(){
//	var folder_ = $('#hdfsManager_list_folder').val();
//	if(folder_=="") {
//		$.messager.alert('警告','目录为空，请重新输入!','warning');
//		return ;
//	}
	if(checkTextBoxEmpty('hdfsManager_list_folder','输入目录不能为空，请重新输入!')) return ;
//	if() // 加输入框验证
    $('#dg_hdfsManager_list').datagrid('load',{
        folder: $('#hdfsManager_list_folder').val()
    });
}

/**
 * 过滤加载数据
 */
function search_data(){
	 // 加输入框验证
	if(checkTextBoxEmpty('hdfsManager_search_folder','输入目录不能为空，请重新输入!')) return ;
    $('#dg_hdfsManager_search').datagrid('load',{
        folder: $('#hdfsManager_search_folder').val(),
        name:$('#hdfsManager_search_folder_name').val(),
        nameOp:$('#hdfsManager_search_folder_name_op').combobox('getValue'),
        owner:$('#hdfsManager_search_owner').val(),
        ownerOp:$('#hdfsManager_search_owner_op').combobox('getValue')
    });
}
/**
 * 返回上一级
 */
function back_to_parent(){
	var curr =$('#hdfsManager_search_folder').val();
	if(curr == '/'){ return ;}
	
	var index = curr.lastIndexOf('/');
	if(index == 0){
		back_to_dir('/');
		return ;
	}
	back_to_dir(curr.slice(0,index));
//	console.info("index:"+);
}
/**
 * 返回dir目录
 *  如果是根目录，并且flag= true，则直接返回
 *  如果flag =false，则执行一次
 */
function back_to_dir(dir){
	var curr =$('#hdfsManager_search_folder').val();
	if(curr == '/'){ return ;}
	$('#hdfsManager_search_folder').val(dir);
	search_data();
}

