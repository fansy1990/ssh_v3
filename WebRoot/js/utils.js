
$(function(){
	// 绑定登录form表单
	$('#login_ff').form({    
	    url:'user/hdfsUser_login.action',      
	    success:function(data){    
	    	var jsonData = JSON.parse(data);
	    	if("true" == jsonData.flag){
	    		$('#login_window').window('close');
//	    		window.self.location = "main.jsp";
	    		 window.location.href="main.jsp";
//	    		 window.navigate("main.jsp");
//	    		 window.location.replace("main.jsp");
	    	}else{
	    		$.messager.alert('提示',jsonData.msg,'info');
	    	}
	    }    
	});    
	
	// 绑定注册form表单
	$('#register_ff').form({    
	    url:'user/hdfsUser_register.action',
	    onSubmit: function(){    
	    	var email_ = $('#register_eamil').val();
	    	var result = callByAJax('user/hdfsUser_registerCheck.action',{email:email_});
	    	if(result.flag == "false"){
	    		$.messager.alert('提示','用户Email已存在!','warning');
	    		return false;
	    	}else{
	    		return true;
	    	}
	         
	    },
	    success:function(data){    
	    	console.info(data);
	    	var jsonData = JSON.parse(data);
	    	if("true" == jsonData.flag){
	    		$.messager.confirm('提示', '注册成功，转到登录界面？', function(r){
	    			if (r){
	    				window.location.href="login.jsp";
	    			}
	    		});
	    	}else{
	    		$.messager.alert('提示',jsonData.msg,'info');
	    	}
	    }    
	});  
	
	// 绑定注册按钮
	
	$('#registerBtn').bind('click', function(){
		// submit the form    
		$('#register_ff').submit(); 
		
		
	});
});


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


// 登录
function login(){
	$('#login_ff').submit(); 
}
// 注销
function logout(){
	$.messager.confirm('提示', '您想要退出该系统吗？', function(r){
		if (r){
		    // 退出操作;
			var result = callByAJax('user/hdfsUser_logout.action',{});
			if("true" == result.flag){
//				$.messager.alert('信息','文件下载成功!','info');
				window.location.href="login.jsp";
			}else if("false" == result.flag){
				$.messager.alert('信息',result.msg,'info');
			}
		}
	});


}
// 注册
function register(){
	window.location.href="register.jsp";
}

//弹出progressBar
//
function popupProgressbar(titleStr,msgStr,intervalStr){
	var win = $.messager.progress({
     title:titleStr,
     msg:msgStr,
     interval:intervalStr    //设置时间间隔较长 
 });
}
//关闭progressBar
function closeProgressbar(){
	$.messager.progress('close');
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
 * check if combobox value is empty
 * @param id_
 * @param msg
 * @returns {Boolean}
 */
function checkComboboxEmpty(id_,msg){
	var folder_ = $('#'+id_).combobox('getValue');
//	console.info(":"+folder_);
	if(folder_==""|| folder_==undefined) {
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
			// 增加权限验证
			
			if(opts.title == '权限管理'){
				var ret = callByAJax('user/hdfsUser_getSessionValue.action',{sessionProperty:'authority'});
				if(ret.authority!=0){
					$.messager.alert('信息','没有权限打开此页面，请使用管理员账户登录!','info');
					return ;
				}
			}
			t.tabs('add', opts);
		}
//		console.info("打开页面："+opts.title);
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
//	    	console.info('Tab: ' + title + ' ,index: ' + index+' closing');
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
//			console.info("click:"+node.text);
			if(node.attributes.folder=='1'){// 父节点
				return ;
			}
//			console.info("open url:"+node.attributes.url)	;
			var url;
			if (node.attributes.url) {
				url = node.attributes.url;
			} else {
				url = '404.jsp';
			}
//			console.info("open "+url);
			layout_center_addTabFun({
				title : node.text,
				closable : true,
				iconCls : node.iconCls,
				href : url
			});
		}
	});	
	

});