
var prefix=new Array()

for (var i = 0; i < 26; i++) {
	String.fromCharCode("65")
    prefix[i]='AAA'+String.fromCharCode(65+i);
	prefix[i+26]='AAB'+String.fromCharCode(65+i);
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
		document.getElementById("check_result_id").innerHTML = "";
		return ;
	}
	
	document.getElementById("check_result_id").innerHTML = "存在的冠字号为:"+"<br>"+
	ret.exist+"<br>"+"疑似伪钞冠字号为:"+"<br>"+ret.notExist;
}