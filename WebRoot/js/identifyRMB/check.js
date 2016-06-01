
$(function(){
	// check dg :
	$('#check_detail_dg').datagrid({
		border : false,
		fitColumns : false,
		singleSelect : true,
		width : 950,
		height : 220,
		nowrap : false,
		fit : false,
		pagination : true,// 分页控件
		pageSize : 4, // 每页记录数，需要和pageList保持倍数关系
		pageList : [ 4,8 ,12],
		rownumbers : true,// 行号
		pagePosition : 'top',
		url : 'hbase/hbaseCommand_getTableCertainRowKeyData.action',
		queryParams: {
			tableName: '',
			cfs: ''
		},
		onLoadError:function(){
			console.info("list load error!");
			 $.messager.alert('信息','加载错误，请联系管理员!','info');
		},
		onBeforeLoad:function(param){
			return true;
		},
		idField:'id',
		columns :[[
				{
					field : 'rowKey',
					title : 'RowKey',
					width : '200'
				},{
					field : 'column',
					title : '列簇:列名',
					width : '300'
				},{
					field : 'timestamp',
					title : '时间戳',
					width : '200'
				},{
					field : 'value',
					title : '值',
					width : '200',
				}
				 ]]
		    }); 
	
	// == check dg 
	
	
	
});



var prefix=new Array()

for (var i = 0; i < 26; i++) {
	String.fromCharCode("65")
    prefix[i]='AAA'+String.fromCharCode(65+i);
	prefix[i+26]='AAB'+String.fromCharCode(65+i);
}


var UIDPrefixes = new Array("4113281989XXXX","4113281990XXXX","4113281991XXXX","4113281992XXXX");


var banks = new Array(
		// 中国银行
		"BKCHCNBJ",
		//工商银行
		"ICBKCNBJ",
		//建设银行
		"PCBCCNBJ",
		//农业银行
		"ABOCCNBJ",
		//招商银行
		"CMBCCNBS",
//		交通银行
		"COMMCN",
//		中信银行
		"CIBKCNBJ",
//		兴业银行
		"FJIBCNBA",
//		民生银行：
		"MSBCCNBJ",
//		华夏银行：
		"HXBKCN",
//		浦发银行：
		"SPDBCNSH",
//		汇丰银行：
		"HSBCCNSH",
//		渣打银行：
		"SCBLCNSX",
//		花旗银行：
		"CITICNSX",
//		德意志银行：
		"DEUTCNSH",
//		瑞士银行：
		"UBSWCNBJ",
//		荷兰银行：
		"ABNACNSH",
//		香港汇丰：
		"BLICHKHK",
//		香港花旗：
		"CITIHK",
//		香港东亚银行：
		"BEASCNSH",
//		恒生银行：
		"HASECNSHBEJ",
//		厦门银行：
		"CBXMCNBA"		
);
/**
 * 产生随机用户ID
 * @returns {String}
 */
function generateRandomUID(){
	return UIDPrefixes[randomNumber(4)]+formatNumber(randomNumber(10000));
}
/**
 * 产生随机银行ID
 */
function generateRandomBank(){
	return banks[randomNumber(22)];
}

/**
 * 根据个数随机生成stumber id
 */
function generateRandom(num){
	var r = prefix[randomNumber(52)]+formatNumber(randomNumber(10000));
	for (var i = 0; i < num-1; i++) {
	    r=r+','+prefix[randomNumber(52)]+formatNumber(randomNumber(10000));
	}
//	alert(document.getElementById("num_id").value);
	return r;
}
function formatNumber(num){
	var str = "" + num;
	var pad = "0000";
	var ans = pad.substring(0, pad.length - str.length) + str;
	return ans;
}

function randomNumber(max){
	return Math.floor(Math.random()*max);
}


/**
 * check.jsp
 * 随机生成stumber
 */
function generateRandomStumber(){
	var num = $('#check_num').combobox('getValue');
	var info = generateRandom(parseInt(num));
	$('#check_input').textbox("setValue",info);
}
/**
 * 检查输入的stumber 中疑似的冠字号
 */
function check_stumbers(){
	if(checkTextBoxEmpty('check_input','冠字号不能为空，请重新输入，每个冠字号使用逗号结尾!')) return ;
	
	var ret = callByAJax("identify/identify_checkStumberExistOrNot.action", {stumbers:$('#check_input').val()})
	
	if(ret.flag == "false"){
		$.messager.alert('提示',ret.msg,'info');
		$("#check_show_or_not_id").hide();
		document.getElementById("check_result_id").innerHTML = "";
		return ;
	}
	$("#check_show_or_not_id").show();
	document.getElementById("check_result_id").innerHTML = "存在的冠字号为:"+"<br>"+
	ret.exist+"<br>"+"疑似伪钞冠字号为:"+"<br>"+ret.notExist;
}
/**
 * 读取给定rowkey以及版本的表数据
 */
function check_detail(){
	
	$('#check_detail_dg').datagrid('load',{
		tableName: 'records',
		cfs: 'info',
		rowkey: $('#check_input').val(),
		versions:$('#check_versions_num').combobox("getValue")
	});
}

/**
 * save.jsp
 * 随机生成用户id，银行id，冠字号id
 */
function generateRandomSave(){
	// uid
	$('#save_user').textbox("setValue",generateRandomUID());
	// bank
	$('#save_bank').textbox("setValue",generateRandomBank());
	
	// stumbers 
	var num = $('#save_num').combobox('getValue');
	var info = generateRandom(parseInt(num));
	$('#save_stumbers').textbox("setValue",info);
}
/**
 * save.jsp
 * 存款
 */
function save_RMB(){
	if(checkTextBoxEmpty('save_user','用户不能为空，请重新输入!')) return ;
	if(checkTextBoxEmpty('save_bank','银行不能为空，请重新输入!')) return ;
	if(checkTextBoxEmpty('save_stumbers','冠字号不能为空，请重新输入，每个冠字号使用逗号结尾!')) return ;
	
	var ret = callByAJax("identify/identify_save.action", 
			{stumbers:$('#save_stumbers').val(),userId:$('#save_user').val(),
			bank:$('#save_bank').val()})
	
	if(ret.flag == "false"){
		$.messager.alert('提示',ret.msg,'info');
		document.getElementById("save_result_id").innerHTML = "";
		return ;
	}
	document.getElementById("save_result_id").innerHTML = "已存储的冠字号为:"+"<br>"+
	ret.saved+"<br>"+"疑似伪钞冠字号为:"+"<br>"+ret.notSaved;
}

/**
 * retrieve.jsp
 * 随机生成用户id，银行id
 */
function generateRandomRetrieve(){
	// uid
	$('#retrieve_user').textbox("setValue",generateRandomUID());
	// bank
	$('#retrieve_bank').textbox("setValue",generateRandomBank());
}

function retrieve_RMB(){
	if(checkTextBoxEmpty('retrieve_user','用户不能为空，请重新输入!')) return ;
	if(checkTextBoxEmpty('retrieve_bank','银行不能为空，请重新输入!')) return ;
	
	var ret = callByAJax("identify/identify_retrieve.action", 
			{num:$('#retrieve_num').combobox("getValue"),userId:$('#retrieve_user').val(),
			bank:$('#retrieve_bank').val()})
	
	if(ret.flag == "false"){
		$.messager.alert('提示',ret.msg,'info');
		document.getElementById("retrieve_result_id").innerHTML = "";
		return ;
	}
	document.getElementById("retrieve_result_id").innerHTML = "取款的冠字号为:"+"<br>"+
	ret.retrieved+"<br>";
}
