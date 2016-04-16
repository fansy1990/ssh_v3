$(function(){
	
	// folder参数
	var folder_=$('#hdfsManager_list_folder').val();
	
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
					width : '50'
				},{
					field : 'group',
					title : '组名',
					width : '70'
				}
				 ]]
		    }); 
	
	// recomend
});