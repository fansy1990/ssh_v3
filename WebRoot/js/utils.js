
/**
 * 更新项目
 * @param title_
 * @param data
 */
function update_project(title_,data){
//	更新中间显示
	$('#centerTitleId').panel({ title: title_ });
	document.getElementById("project_about_id").innerHTML=title_;
	removePanel();
	// 更新east 树导航
	$('#eastTree').tree({'url':data});
			
	
}

/**
 * check if textbox is empty ，empty: true
 * @param id_
 * @param msg
 * @returns {Boolean}
 */
function checkTextBoxEmpty(id_,msg){
	var folder_ = $('#'+id_).val();
	if(folder_=="") {
		$.messager.alert('警告',msg,'warning');
		return true;
	}
	return false;
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
/**
 * 移除所有tab页，除了第一个
 */
function removePanel(){
//    var tab = $('#centerTab').tabs('tabs');
//    console.info(tab.length);
//    var index=0;
//    for(var i=0;i<tab.length;i++){
//    	 index= $('#centerTab').tabs('getTabIndex',tab[i]);
//    	console.info(index);
//    }
    
	$('#centerTab ul.tabs li').each(function(index, v) {
	    if(index!=0){
	    	
	    	var elTitle = $('.tabs-title', v);
		    var title = elTitle.html();
	    	console.info('Tab: ' + title + ' ,index: ' + index+' closing');
	    	$("#centerTab").tabs("close", title);
	    }
	});
}


//调用ajax同步提交
//任务返回成功，则提示成功，否则提示失败的信息
function callByAJax(url,data_){
	var result;
	$.ajax({
		url : url,
		data: data_,
		async:false,
		dataType:"json",
		context : document.body,
		success : function(data) {
			result=data;
		}
	});
//	console.info("result:"+result);
	return result;
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