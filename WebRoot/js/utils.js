
/**
 * 更新项目
 * @param title
 * @param data
 */
function update_project(title,data){
	$('#centerId').val(title);
	console.info(title);
//	$('#eastTree').tree('reload',data);
	
}

/**
 * 添加tab 页
 * @param title
 * @param url
 */
function layout_center_addTabFun(opts) {
		var t = $('#centerTab');
		if (t.tabs('exists', opts.title)) {
			t.tabs('select', opts.title);
		} else {
			t.tabs('add', opts);
		}
		console.info("打开页面："+opts.title);
}

$(function(){
	$('#eastTree').tree({
		onClick: function(node){
//			alert(node.text+","+node.url);  // alert node text property when clicked
			console.info("click:"+node.text);
			if(node.attributes.folder=='1'){// 父节点
				return ;
			}
			console.info("open url:"+node.attributes.url)	
			var url;
			if (node.attributes.url) {
				url = node.attributes.url;
			} else {
				url = '404.jsp';
			}
			console.info("open "+url);
			layout_center_addTabFun({
				title : node.text,
				closable : true,
				iconCls : node.iconCls,
				href : url
			});
		}
	});	
	

});