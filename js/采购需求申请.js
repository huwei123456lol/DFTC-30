//项目编号不存在研发项目时隐藏明细11
WfForm.bindFieldChangeEvent("field1650534", function (obj, id, value) {
    if(value == "" || value == null){
        $('tr[name=ygjeqcjd]').hide();
    }else{
        $('tr[name=ygjeqcjd]').show();
    }
});

//历史流程中存在研发协同项目则显示在明细11  。。。创建表单上没有看见明细表11 ？？？
jQuery(document).ready(function(){

    var field1650534 = WfForm.getFieldValue("field1650534");

    console.log(field1650534);

    if(field1650534 == null || field1650534 == ""){
        $('tr[name=ygjeqcjd]').hide();
        return;
    }

    var rowArr = WfForm.getDetailAllRowIndexStr("detail_11").split(",");

    for(var j=0;j<field1650534.split(",").length; j++){
        var sdf = field1650534.split(",")[j];
        var boola = false;
        console.log(sdf);
        for(var i=0; i<rowArr.length; i++){
            var rowIndex = rowArr[i];
            if(rowIndex !== ""){
                var fieldMark = WfForm.getFieldValue("field1647056_"+rowIndex);
                // var fieldMark = "field1647056_"+rowIndex;    //遍历明细行字段
                console.log(fieldMark);

                if(fieldMark == sdf){
                    boola = true;
                    continue;
                }
            }
        }

        if(!boola){
            console.log("ssasdsdasdasdsad");
            WfForm.addDetailRow("detail_11",{field1647056:{
                    value:sdf,
                    specialobj:[
                        {id:sdf,name:sdf}
                    ]
                }});
        }
    }

});



//校验明细表11【预估金额（不含税，元）】的和<【预计采购不含税金额（元）】  2023-04-21  。。。创建表单上没有看见明细表11 ？？？
WfForm.registerCheckEvent(WfForm.OPER_SUBMIT, function(callback){
    var field332513 = WfForm.getFieldValue("field332513");//预计采购不含税金额
    var field1660606 = WfForm.getFieldValue("field1660606");//明细表11【预估金额（不含税，元）】的和
    if(field332513 != "" && field1660606 != ""){
        if(field1660606 < field332513){
            callback();
        }else{
            alert("协同项目预估金额必需小于【预计采购不含税金额】");
            return;
        }
    }
});

// WfForm.bindFieldChangeEvent("field199538", function(obj,id,value){

//     WfForm.triggerFieldAllLinkage("field199538");  //执行字段涉及的所有联动

//     // console.log("WfForm.bindFieldChangeEvent--",obj,id,value);
// });
//采购预算效验前端检测  2023-04-10
WfForm.registerCheckEvent(WfForm.OPER_SUBMIT, function(callback){
    var field237456 = WfForm.getFieldValue("field237456");  //采购类别
    var field237566 = WfForm.getFieldValue("field237566");  //合同类别
    var field199538 = WfForm.getBrowserShowName("field199538");   //项目编号
    var field30871 = WfForm.getFieldValue("field30871");   //采购项目分类
    var field30885 = WfForm.getFieldValue("field30885");   //采购业务分类
    var field204515 = WfForm.getFieldValue("field204515");   //采购需求部门
    var field30875 = WfForm.getFieldValue("field30875");

    if(field30875=='1'){
        callback();
        return;
    }
    if(field204515 == 25021 || field204515 == 25022 || field204515 == 25028 || field204515 == 25030 || field204515 == 34526 || field204515 == 34527 || field204515 == 40521 ||  field204515 == 24521){
        callback();
    }else{
        var value1 = field199538.split(",");
        if(value1.length > 1){
            if(field237456 == 1){
                callback();
            }else if(field237456 == 0){
                if(field237566 == 0){
                    callback();
                }else if(field237566 == 1){
                    if(field30871 == 5 || field30871 == 3){
                        callback();
                    }else if(field30871 == 0 || field30871 == 1 || field30871 == 2 || field30871 == 4){
                        if(field30885 == 5 || field30885 == 2 || field30885 == 7 || field30885 == 40){
                            callback();
                        }else if(field30885 == 0 || field30885 == 1 || field30885 == 3 || field30885 == 4 || field30885 == 6 || field30885 == 8){
                            alert("预算管控范围内的采购申请要求只能选择一个项目号，请重新填写项目号后再提交");
                            return;

                        }
                    }
                }
            }
        }else{
            callback();
        }
    }
});


// $('tr[name=bzkgys]').hide();
//预计采购含税金额(元)
WfForm.bindFieldChangeEvent("field30883", function (obj, id, value) {
    //万元以下采购业务逻辑控制
    checkCGFS();
});
//sunbb 20220310 去掉前台配置根据合同信息隐藏合同类型，在js中控制
WfForm.bindFieldChangeEvent("field244033", function (obj, id, value) {
    if(value == '0'){
        $('tr[name=htlxview]').hide();
    }else{
        $('tr[name=htlxview]').show();
    }
});
WfForm.bindFieldChangeEvent("field30885", function (obj, id, value) {
    debugger;
    //控制金额行否显示
    if(value == '2'){
        var cglx = WfForm.getFieldValue("field241014");
        if(cglx == '4'){
            $('tr[name=cgjeview]').hide();
            WfForm.changeFieldAttr("field30883", 2);
            WfForm.changeFieldAttr("field241017", 2);
        }else{
            $('tr[name=cgjeview]').show();
            WfForm.changeFieldAttr("field30883", 3);
            WfForm.changeFieldAttr("field241017", 3);
        }
    }else{
        $('tr[name=cgjeview]').show();
        WfForm.changeFieldAttr("field30883", 3);
        WfForm.changeFieldAttr("field241017", 3);
    }
});
//WfForm.changeFieldValue("field241017", {value: "" }); 2022-5-26标为注释

//万元以下相关业务逻辑
var checkCGFS = function () {
    //sunbb 20220310 去掉前台配置根据合同信息隐藏合同类型，在js中控制

    //合同信息 即合同变更类型
    var htxx = WfForm.getFieldValue("field244033");

    //合同重要信息变更
    if(htxx == '0'){

        //隐藏合同类型 即合同变更详细信息
        $('tr[name=htlxview]').hide();

    //合同一般事项变更
    }else{

        $('tr[name=htlxview]').show();

    }

    // 预计采购含税金额
    var _value = WfForm.getFieldValue("field30883");

    // 申请类型
    var _sqlx = WfForm.getFieldValue("field230327");


    //申请类型 为 合同变更/终止 。。。？？？该选项已经废弃
    if(_sqlx == '1') return;

    //申请类型 为 新增 && 预计采购金额不为空 && 预计采购金额小于1W
    if (_sqlx == '0'  && _value !== "" && _value * 1 < 10000) {

        $('tr[name=zjcgly]').hide();

    } else {

        $('tr[name=zjcgly]').show();

    }

    // 预计采购金额不为空 && 预计采购金额小于1W
    if (_value !== "" && _value * 1 < 10000 && _value * 1 > 0) {
        // console.log(_value);
        // console.log(_value*1);
        //WfForm.delDetailRow("detail_3", "all");


        //显示不在库供应商字段行
        $('tr[name=bzkgys]').show();


        //$('tr[name=zjcgly]').hide();
        //20220211 huhaibo确认 万元以下的建议供应商理由（不隐藏）
        //$('tr[name=jygysly]').hide();

        //隐藏采购立项来源文件
        $('tr[name=fjqy]').hide();

        //隐藏采购计划
        $('tr[name=cgjh]').hide();

        //锁定费用类
        WfForm.changeFieldValue("field30875", { value: 0 });
        WfForm.changeFieldAttr("field30875", 1);

        //议价方式 锁定 非电子交易平台
        WfForm.changeFieldValue("field30878", { value: 1 });
        WfForm.changeFieldAttr("field30878", 1);

        //采购方式 默认值置为 询价采购 可编辑
        WfForm.changeFieldValue("field30879", { value: 10 });
        WfForm.changeFieldAttr("field30879", 2);
        WfForm.controlSelectOption("field30879", '10');

        //评审方法锁定 最低投标价评审
        WfForm.changeFieldValue("field30886", { value: 0 });
        WfForm.changeFieldAttr("field30886", 1);

        //建议供应商理由置为 只读
        WfForm.changeFieldAttr("field30877", 1);

        //锁定评审方法为 最低投标叫评审  。。。与上文代码重复
        WfForm.changeFieldAttr("field30886", 1);

        //采购计划 置为 只读
        WfForm.changeFieldAttr("field199534", 1);

        //采购立项来源文件	置为只读
        WfForm.changeFieldAttr("field199552", 1);

        //采购需求评审会议纪要
        WfForm.changeFieldAttr("field199529", 1);
        // WfForm.changeFieldAttr("field78018", 1);
    } else {
        // $('tr[name=bzkgys]').hide();
        //$('tr[name=zjcgly]').show();
        $('tr[name=jygysly]').show();
        $('tr[name=fjqy]').show();   //suncs 20221104
        $('tr[name=cgjh]').show();
        WfForm.changeFieldAttr("field199534", 3);
        WfForm.changeFieldAttr("field30875", 3);
        WfForm.changeFieldAttr("field30878", 3);
        WfForm.changeFieldAttr("field30879", 3);
        WfForm.changeFieldAttr("field30886", 3);
        //WfForm.changeFieldAttr("field30877", 3);
        WfForm.changeFieldAttr("field30886", 3);
        if (WfForm.getFieldValue("field30878") == '') {
            WfForm.changeFieldValue("field30878", { value: 0 });
        }
        WfForm.changeFieldAttr("field199552", 3);
        WfForm.changeFieldAttr("field199529", 2);
        // WfForm.changeFieldAttr("field78018", 2);
        WfForm.controlSelectOption("field30879", "0,1,9,10,13,14,15");
        if (WfForm.getFieldValue("field30879") == '10') {
            WfForm.changeFieldValue("field30879", { value: '' });
        }
    }

    var cglx = WfForm.getFieldValue("field237456");
    if(cglx == '1'){
        WfForm.changeFieldAttr("field199534", 5);
        WfForm.changeFieldAttr("field230327",1);
        WfForm.changeFieldValue("field230327", { value: '0' });
        WfForm.changeFieldAttr("field237566",1);
        WfForm.changeFieldValue("field30878", { value: '1' });
        WfForm.changeFieldAttr("field30878", 1);
        WfForm.changeFieldValue("field30879", { value: '10' });
        WfForm.changeFieldAttr("field30879", 2);
        // WfForm.controlSelectOption("field30879", '10')
        WfForm.changeFieldValue("field30886", { value: '0' });
        WfForm.changeFieldAttr("field30886", 1);
    }else if(cglx == '0'){
        WfForm.changeFieldAttr("field237566",2);
        WfForm.changeFieldAttr("field230327",2);
    }

    var htlx = WfForm.getFieldValue("field230327");
    if(htlx == '2' || htlx == '3' ){
        WfForm.changeFieldValue("field30878", { value: 1 });
        WfForm.changeFieldAttr("field30878", 1);
        WfForm.changeFieldValue("field30879",{ value: 9 })
        WfForm.changeFieldAttr("field30879", 2);
        // var zjcgly = WfForm.getFieldValue("field30880");
        // alert(zjcgly);
        WfForm.changeFieldValue("field30880",{ value: 13 });
        WfForm.changeFieldAttr("field30880",1);
        // WfForm.changeFieldValue("field30886",{ value: "0" });
        WfForm.changeFieldAttr("field30886",5);
    }
    // else{
    //   WfForm.changeFieldAttr("field30878",2);
    //   WfForm.changeFieldAttr("field30879",2);
    //   WfForm.changeFieldAttr("field30886",2);
    //   WfForm.changeFieldAttr("field30880",2);
    // }

    var jjcg = WfForm.getFieldValue("field243515");
    if(jjcg == '2'){
        WfForm.changeFieldAttr("field199534", 5);
    }

    var sq = WfForm.getFieldValue("field230327");
    if(sq == '2'){
        WfForm.changeFieldAttr("field241014", 5);
    }

    var ywlx = WfForm.getFieldValue("field30885");
    var cglx2 = WfForm.getFieldValue("field241014");
    if(ywlx == '2' && cglx2 == '4'){
        $('tr[name=cgjeview]').hide();
        WfForm.changeFieldAttr("field30883", 2);
        WfForm.changeFieldAttr("field241017", 2);
    }else{
        $('tr[name=cgjeview]').show();
        WfForm.changeFieldAttr("field30883", 3);
        WfForm.changeFieldAttr("field241017", 3);
    }

};
checkCGFS();

//获取标工
function getBG01(cgfs, cgywlb, fl, yjfs, callback) {
    let bgDay = 0;
    $.ajax({
        url: '/api/public/browser/data/modeBrowserPreview',
        type: 'GET',
        data: {
            multiselectValue_con200069_value: cgfs + '',  //采购方式
            multiselectValue_con200072_value: cgywlb + '',  //采购业务类别
            multiselectValue_con200071_value: fl + '',  //分类
            multiselectValue_con200070_value: yjfs + '',  //议价方式
            customid: '30501'
        },
        async: false,
        success: function (result) {
            let _bg01Obj = JSON.parse(result).datas;
            if (_bg01Obj.length > 0) {
                if (_bg01Obj.length > 1) {
                }
                //if(window.console) console.log('@@@@',_bg01Obj[0].d_bzgs);
                bgDay = _bg01Obj[0].d_bzgs;
            }
            var bg1 = WfForm.convertFieldNameToId("bg1");
            WfForm.changeFieldAttr(bg1,3);
            WfForm.changeFieldValue(bg1, {
                value: bgDay
            });
            if(WfForm.getFieldValue(bg1)!=''){
                callback();
            }else{
                alert('未获取到标工1，请重新提交！');
                return;
            }
        }
    });
}

// 根据数据库名称转换fieldid
var bg1 = WfForm.convertFieldNameToId("bg1");
var bg2 = WfForm.convertFieldNameToId("bg2");
var cgfs = WfForm.convertFieldNameToId("cgfs"); //任务批分时间
var yjfs = WfForm.convertFieldNameToId("yjfs"); //招议标计划完成时间
var cgxmjelb = WfForm.convertFieldNameToId("cgxmjelb"); //技术任务书计划确认时间
var cgywfl = WfForm.convertFieldNameToId("cgywfl");


//suncs 2021-11-16 设置万元以下要添加采购明细
//var nn = 0;
var nn = WfForm.getDetailRowCount("detail_2");
WfForm.registerAction(WfForm.ACTION_ADDROW+"2", function(index){
    nn = WfForm.getDetailRowCount("detail_2");
});
WfForm.registerAction(WfForm.ACTION_DELROW+"2", function(arg){
    nn = WfForm.getDetailRowCount("detail_2");
});
//var  mm= 0;
var  mm = WfForm.getDetailRowCount("detail_4");
WfForm.registerAction(WfForm.ACTION_ADDROW+"4", function(index){
    mm = WfForm.getDetailRowCount("detail_4");
});
WfForm.registerAction(WfForm.ACTION_DELROW+"4", function(arg){
    mm = WfForm.getDetailRowCount("detail_4");
});

//保存时处理标工1数据
WfForm.registerCheckEvent(WfForm.OPER_SAVE, function (callback) {

    var cgfs = WfForm.getFieldValue("field30879");
    var yjfsValue = WfForm.getFieldValue(yjfs);
    var cgxmjelbValue = WfForm.getFieldValue(cgxmjelb);
    var cgywflValue = WfForm.getFieldValue(cgywfl);

    var bg1value = 0;

    if(cgfs === '2'){
        WfForm.changeFieldValue('field30878',{value:0});
    }

    $.ajax({
        url: '/api/public/browser/data/modeBrowserPreview',
        type: 'GET',
        data: {
            multiselectValue_con200069_value: cgfs + '',  //采购方式
            multiselectValue_con200072_value: cgywflValue + '',  //采购业务类别
            multiselectValue_con200071_value: cgxmjelbValue + '',  //分类
            multiselectValue_con200070_value: yjfsValue + '',  //议价方式
            customid: '30501'
        },
        async: false,
        success: function (result) {
            let _bg01Obj = JSON.parse(result).datas;
            if (_bg01Obj.length > 0) {
                if (_bg01Obj.length > 1) {
                }
                //if(window.console) console.log('@@@@',_bg01Obj[0].d_bzgs);
                bg1value = _bg01Obj[0].d_bzgs;
            }
            var bg1 = WfForm.convertFieldNameToId("bg1");
            WfForm.changeFieldValue(bg1, {
                value: bg1value
            });

            var cgjhh = WfForm.getFieldValue("field199534");
            var bginfo = cgjhh + ',' + bg1value;
            $.ajax({
                url: '/api/SupplierInfo/updateBgOne/' + bginfo,
                type: 'GET',
                async: false,
                success: function (result) {
                    callback();
                }
            });
        }
    });
});

//字段必填控制及提示
WfForm.registerCheckEvent(WfForm.OPER_SUBMIT, function (callback) {
    var zt = WfForm.getFieldValue("field346013");
    if(zt == '1' || zt == '2'){
        alert("采购需求废止或暂缓，不能提交");
        return;
    }

    //suncs 20221104
    var cglx = WfForm.convertFieldNameToId("cglx"); //采购类型
    var cglxvalue = WfForm.getFieldValue(cglx);
    var cgywfl = WfForm.convertFieldNameToId("cgywfl"); //采购业务分类
    var cgywflvalue = WfForm.getFieldValue(cgywfl);
    var jsrwslc = WfForm.convertFieldNameToId("jsrwslc"); //技术任务书流程
    var jsrwslcvalue = WfForm.getFieldValue(jsrwslc);
    var jsxy = WfForm.convertFieldNameToId("jsxy"); //技术协议
    var jsxyvalue = WfForm.getFieldValue(jsxy);
    if(cglxvalue=='4'&& cgywfl=='0'){
        if(jsrwslcvalue==null && jsrwslcvalue=="" && jsxyvalue==null && jsxyvalue=="" ){
            alert("采购类型为“量产一体化采购” 和采购业务分类为“设计开发类” 时“技术任务书流程”及“技术协议”不能同时为空");
            return ;
        }
    }



    //设置采购启动申请“今年”、“明年”、“以后年度”付款比率要进行合计=100%的校验
    //2022-10-31
    var jn = WfForm.getFieldValue("field332514");
    var mn = WfForm.getFieldValue("field332515");
    var sy = WfForm.getFieldValue("field332516");
    let hj = Number(jn)+Number(mn)+Number(sy);
    if(hj>100){
        alert("预计执行比例总和超过100%");
        return;
    }


    //获取采购方式
    var cgfs = WfForm.getFieldValue("field30879");
    var yjfsValue = WfForm.getFieldValue(yjfs);
    var cgxmjelbValue = WfForm.getFieldValue(cgxmjelb);
    var cgywflValue = WfForm.getFieldValue(cgywfl);
    if (window.console) console.log(cgfs, cgywflValue, cgxmjelbValue, yjfsValue)

    //suncs 2021-11-16 设置万元以下要添加采购明细
    var fieldvalue = WfForm.getFieldValue("field237456");
    if(fieldvalue==1){
        if(nn==0){
            alert('请填写采购明细！！');
            return;
        }
        if(mm==0){
            alert('请填写供应商明细！！');
            return;
        }
    }
    //采购方式为 公开竞争性谈判
    if(cgfs === '2'){
        //设置议价方式为 电子交易平台
        WfForm.changeFieldValue('field30878',{value:0});
    }

    //如果万元以上直采理由是集团控股，那么供应商必须都是集团控股有一个不是就提交不了，战略供应商也是一样
    if(WfForm.getFieldValue("field30883")*1 > 10000){
        var zjcgly = WfForm.getFieldValue("field30880");
        var flag = true;
        if(zjcgly == '12'){//如果万元以上直采理由是集团控股
            var rowArr = WfForm.getDetailAllRowIndexStr("detail_3").split(",");
            for(var i=0; i<rowArr.length; i++){
                var rowIndex = rowArr[i];
                if(rowIndex !== ""){
                    var value = WfForm.getFieldValue("field265561_"+rowIndex);
                    if(value == '1'||value==''){
                        alert('直接采购理由是集团控股，请选择集团控股供应商');
                        return;
                    }
                }
            }
        }
        if(zjcgly == '14'){//如果万元以上直采理由是战略供应商
            var rowArr = WfForm.getDetailAllRowIndexStr("detail_3").split(",");
            for(var i=0; i<rowArr.length; i++){
                var rowIndex = rowArr[i];
                if(rowIndex !== ""){
                    var value = WfForm.getFieldValue("field262515_"+rowIndex);
                    if(value == '1'||value==''){
                        alert('直接采购理由是战略/框架合作股，请选择战略供应商应商');
                        return;
                    }
                }
            }
        }
    }

    //采购类型
    var vcg=WfForm.getFieldValue('field241014');
    //采购类型属于 试制类量产零件采购、试制类追加采购、	一体化采购
    if(vcg=='2' || vcg=='3' || vcg=='4'){
        callback();
        return;
    }
    if (cgfs!==''&&(cgfs !== '0' && cgfs !== '2'  && cgfs !== '14' && cgfs !== '10') && WfForm.getDetailAllRowIndexStr("detail_3").split(',')[0] == '') {
        alert('请填写供应商明细！')
    }else if(WfForm.getFieldValue('field237456') == '0' && WfForm.getFieldValue('field230327') == '2' && WfForm.getFieldValue('field244033') == '1'){
        callback();
    } else if(WfForm.getFieldValue('field230948') == '' && WfForm.getFieldValue(cgxmjelb) === '0' &&   WfForm.getFieldValue("field30883")*1 > 10000){
        alert('采购计划未关联采购需求！ 请先完成采购需求评审会议纪要！')
    }else {
        getBG01(cgfs, cgywflValue, cgxmjelbValue, yjfsValue, callback)
    }
})
//不良供应商链接
jQuery('#blgysLink').bind('click', function () {
    window.open('/spa/cube/index.html#/main/cube/search?customid=103016')
})


WfForm.bindFieldChangeEvent(cgfs + ',' + yjfs + ',' + cgxmjelb + ',' + cgywfl, function (obj, id, value) {
    //if(window.console) console.log("WfForm.bindFieldChangeEvent--",obj,id,value);
    var cgfsValue = WfForm.getFieldValue(cgfs);
    var yjfsValue = WfForm.getFieldValue(yjfs);
    var cgxmjelbValue = WfForm.getFieldValue(cgxmjelb);
    var cgywflValue = WfForm.getFieldValue(cgywfl);
    if (cgfsValue === '9' || cgfsValue === '10') {
        WfForm.changeFieldValue("field30886", { value: 0 });
        WfForm.changeFieldAttr("field30886", 1);
    } else {
        //suncs  2021-11-16 之前是放开的，现把此处屏蔽
        //WfForm.changeFieldAttr("field30886", 3);   //评审方法
    }
    if ((cgfsValue == '0') ) {

        WfForm.delDetailRow("detail_3", "all");
        $('tr[name=bzkgys]').hide();
        $('tr[name=gys]').hide();
    }else if(WfForm.getFieldValue("field30883")*1 < 10000){
        $('tr[name=bzkgys]').show();
        $('tr[name=gys]').show();
    }else{
        $('tr[name=gys]').show();
    }
    if ((cgfsValue == '0' || cgfsValue == '1' ) ) {

        WfForm.changeFieldValue("field30878", { value: 0 });
        WfForm.changeFieldAttr("field30878", 1);
        WfForm.changeFieldAttr("field30877", 1);

    }else if(cgfsValue == '2'){

        WfForm.changeFieldAttr("field30877", 3);
        //suncs  2021-11-16 之前是放开的，现把此处屏蔽
        //WfForm.changeFieldAttr("field30878", 3);    //议价方式
        //WfForm.changeFieldAttr("field30877", 3);

    }

    //suncs 2021-10-10
    //投资类 隐藏采购需求评审会议纪要行
    if(cgxmjelbValue === '1'){

        $('tr[name=cgpshyjy]').hide();

    //投资类 显示采购需求评审会议纪要行
    }else if(cgxmjelbValue === '0'){

        $('tr[name=cgpshyjy]').show();

    }


});

//绑定申请类型字段 变更时间
WfForm.bindFieldChangeEvent("field230327", function(obj,id,value){
    //console.log("WfForm.bindFieldChangeEvent--",obj,id,value);
    //申请类型为 合同变更/终止
    if(value == '1'){

        WfForm.changeFieldValue("field30878", { value: "1" });  //field228849_议价方式
        WfForm.changeFieldValue("field30879", { value: "9" });  //field228850_采购方式
        WfForm.changeFieldValue("field30880", { value: "13" });  //field228852_直接采购理由
        WfForm.changeFieldValue("field30886", { value: "0" });   //field228851_评审方法
        WfForm.changeFieldAttr("field30878" , 1);
        WfForm.changeFieldAttr("field30879" , 2);
        WfForm.changeFieldAttr("field30880" , 1);
        WfForm.changeFieldAttr("field30886" , 1);
    }
    //申请类型为 新增
    if(value=='0'){

        //获取采购类别
        var lb = WfForm.getFieldValue("field237456");

        //采购类别 为万元以下
        if(lb=='1'){
            //WfForm.changeFieldAttr("field110", 4);
            WfForm.changeFieldAttr("field241014", 5);
        }

    }
});


//根据采购类别，万元以下申请类型默认新增。采购类别field237456；申请类型：field230327
//万元以下预计采购含税金额(元）、议价方式field30878、采购方式field30879、直接采购理由field30880、评审方法field30886只读
//原先代码：WfForm.bindFieldChangeEvent("field240152", function(obj,id,value){
//更改与2022/05/13 ，找不到字段id-field240152，现跟改为field237456。
WfForm.bindFieldChangeEvent("field237456", function(obj,id,value){
    var fieldvalue = WfForm.getFieldValue("field237456");
    //alert(fieldvalue);
    if(fieldvalue=="1"){

        WfForm.controlSelectOption("field230327", "0");//万元以下默认新增
        WfForm.changeFieldValue("field230327", { value: "0" });

        //suncs 2021-11-12 万元以下的设置议价方式默认非电子交易
        WfForm.controlSelectOption("field30878", "1");
        WfForm.changeFieldValue("field30878", { value: "1" });

        //suncs 2021-11-19 控制采购业务分类
        WfForm.controlSelectOption("field30885", "0,1,2,3,4,5,6,7,8");


        WfForm.changeFieldAttr("field30883", 3)
        WfForm.changeFieldAttr("field30878", 3)
        WfForm.changeFieldAttr("field30879", 3)
        WfForm.changeFieldAttr("field30880", 2)
        WfForm.changeFieldAttr("field30886", 3)
    }else{
        WfForm.controlSelectOption("field230327", "0,2,3");

        //suncs 2021-11-12 万元以上
        WfForm.controlSelectOption("field30878", "0,1");
        WfForm.changeFieldValue("field30878", { value: "" });

        //suncs 2021-11-19 控制采购业务分类 ,去掉 样件邮寄费、协会费、党建、团委、样车加油费、资料打印费类
        WfForm.controlSelectOption("field30885", "0,1,2,3,4,5,6,7");

        //ZLL 2021-12-1万元以上属于投资类时，以下逻辑必填，暂时先注释
        //  WfForm.changeFieldAttr("field30883", 1)
        //  WfForm.changeFieldAttr("field30878", 1)
        // WfForm.changeFieldAttr("field30879", 1)
        // WfForm.changeFieldAttr("field30880", 1)
        // WfForm.changeFieldAttr("field30886", 1)

    }
});
//申请类型默认新增。申请类型：field230327
//万元以下预计采购含税金额(元）、议价方式field30878、采购方式field30879、直接采购理由field30880、评审方法field30886只读
// WfForm.bindFieldChangeEvent("field230327", function(obj,id,value){
//     var fieldvalue1 = WfForm.getFieldValue("field230327");
//           //alert(fieldvalue);
//       if(fieldvalue1=="1"){
//         $('tr[name=yht]').hide();
//         $('tr[name=yhtje]').hide();
//         $('tr[name=yhtsmj]').hide();

//     }else{
//     if(fieldvalue1=="1"){
//         $('tr[name=yht]').show();
//         $('tr[name=yhtje]').show();
//         $('tr[name=yhtsmj]').show();

//     }
// });
//pw 2021-12 采购类型变化采购方式
WfForm.bindFieldChangeEvent("field241014", function(obj,id,value){
    //试制类量产零件采购       //field30878 议价方式   field30879  采购方式  field30880直接采购理由      field30886 评审方法
    if(value=='2' || value=='4'){
        WfForm.changeSingleField("field30878", {value:1}, {viewAttr:"1"});
        WfForm.changeSingleField("field30879", {value:13}, {viewAttr:"2"});
        $('tr[name=zjcgly]').hide()
        $('tr[name=zcgfs3]').show()
        WfForm.changeFieldAttr('field242013',3)
        WfForm.changeSingleField("field30880", {value:18}, {viewAttr:"1"});
        WfForm.changeSingleField("field30886", {value:0}, {viewAttr:"1"});
        //试制类追加采购
    }else if(value=='3'){
        WfForm.changeSingleField("field30878", {value:1}, {viewAttr:"1"});
        WfForm.changeSingleField("field30879", {value:9}, {viewAttr:"2"});
        WfForm.changeSingleField("field30880", {value:13}, {viewAttr:"1"});
        WfForm.changeSingleField("field30886", {value:0}, {viewAttr:"1"});
        $('tr[name=zjcgly]').show()
        $('tr[name=zcgfs3]').show()
        WfForm.changeFieldAttr('field242013',2)
    }else if(value==='8'){
        $('tr[name=zcgfs3]').hide()
        $('tr[name= zjcgly]').hide()
        WfForm.changeFieldAttr('field30878',2)
        WfForm.changeFieldAttr('field30879',2)
        WfForm.changeFieldAttr('field30886',2)
        // $('tr[name=zjcgly]').show()
        WfForm.changeFieldAttr('field242013',2)
        //电商采购新增 2022.12.16
        // WfForm.changeFieldAttr("field78018",2);
        //重复采购新增
    }else if(value = '9'){
        WfForm.changeSingleField("field30878",{value:1}, {viewAttr:"1"});
        WfForm.changeSingleField("field30879", {value:9}, {viewAttr:"2"});
        WfForm.changeSingleField("field30880", {value:13},{viewAttr:"1"});
        WfForm.changeSingleField("field30886", {value:0}, {viewAttr:"1"});
        // WfForm.changeFieldAttr("field78018",3);
    }
    else{
        WfForm.changeSingleField("field30878",{value:''}, {viewAttr:"2"});
        WfForm.changeSingleField("field30879", {value:''}, {viewAttr:"2"});
        WfForm.changeSingleField("field30880", {value:''},{viewAttr:"2"});
        WfForm.changeSingleField("field30886", {value:''}, {viewAttr:"2"});
        $('tr[name=zcgfs3]').show()
        $('tr[name=zjcgly]').show()
        WfForm.changeFieldAttr('field242013',2)
    }

    //控制金额行否显示
    if(value == '4'){
        var ywlx = WfForm.getFieldValue("field30885");
        if(ywlx == '2'){

            $('tr[name=cgjeview]').hide();
            WfForm.changeFieldAttr("field30883", 2);
            WfForm.changeFieldAttr("field241017", 2);

        }else{

            $('tr[name=cgjeview]').show();
            WfForm.changeFieldAttr("field30883", 3);
            WfForm.changeFieldAttr("field241017", 3);

        }
    }else{

        $('tr[name=cgjeview]').show();
        WfForm.changeFieldAttr("field30883", 3);
        WfForm.changeFieldAttr("field241017", 3);

    }
});
/**  var fieldvalue1 = WfForm.getFieldValue("field241014");
 if(fieldvalue1=='2' || fieldvalue1=='4'){
      WfForm.changeSingleField("field30878", {value:1}, {viewAttr:"1"});
      WfForm.changeSingleField("field30879", {value:9}, {viewAttr:"1"});
      WfForm.changeSingleField("field30880", {value:18}, {viewAttr:"1"});
      WfForm.changeSingleField("field30886", {value:0}, {viewAttr:"1"});
  }else if(fieldvalue1=='3'){
      WfForm.changeSingleField("field30878", {value:1}, {viewAttr:"1"});
      WfForm.changeSingleField("field30879", {value:9}, {viewAttr:"1"});
      WfForm.changeSingleField("field30880", {value:13}, {viewAttr:"1"});
      WfForm.changeSingleField("field30886", {value:0}, {viewAttr:"1"});
  }else{
      WfForm.changeSingleField("field30878",{value:''}, {viewAttr:"2"});
      WfForm.changeSingleField("field30879", {value:''}, {viewAttr:"2"});
      WfForm.changeSingleField("field30880", {value:''},{viewAttr:"2"});
      WfForm.changeSingleField("field30886", {value:''}, {viewAttr:"2"});
  }**/

//pw 万元下下隐藏常规采购 年度采购 紧急采购
WfForm.bindFieldChangeEvent("field237456", function(obj,id,value){
    if(value=='0'){
        WfForm.controlSelectOption("field241014", "0,1,2,3,4,5,7,8,9");
    }else{
        WfForm.controlSelectOption("field241014", "2,3,4");
        WfForm.changeFieldAttr("field241014", 5);
    }
});
var fieldvalue2 = WfForm.getFieldValue("field237456");
if(fieldvalue2=='0'){
    WfForm.controlSelectOption("field241014", "0,1,2,3,4,5,7,8,9");
}else{
    WfForm.controlSelectOption("field241014", "2,3,4");
}

jQuery(document).ready(function(){
    kbk();
    jQuery("#field244034").bindPropertyChange(function(obj,id,value){
        if(value.indexOf('0')>=0 && value.indexOf('1')>=0){
            alert("不能同时选择1,2")
            $("#field244034").val("");
        }else if(value.indexOf('2')>=0 && value.indexOf('3')>=0){
            alert("不能同时选择3,4")
            $("#field244034").val("");
        }else if(value.indexOf('0')>=0 && value.indexOf('3')>=0){
            alert("不能同时选择1,4")
            $("#field244034").val("");
        }
    });
});


WfForm.bindFieldChangeEvent("field230327", function(obj,id,value){
    if(value == '2' || value == '3' ){
        WfForm.changeFieldValue("field30878", { value: 1 });
        WfForm.changeFieldAttr("field30878", 1);
        WfForm.changeFieldValue("field30879",{ value: 9 })
        WfForm.changeFieldAttr("field30879", 2);
        // var zjcgly = WfForm.getFieldValue("field30880");
        // alert(zjcgly);
        WfForm.changeFieldValue("field30880",{ value: 13 });
        WfForm.changeFieldAttr("field30880",1);
        // WfForm.changeFieldValue("field30886",{ value: "0" });
        WfForm.changeFieldAttr("field30886",5);
    }else{
        WfForm.changeFieldAttr("field30878",2);
        WfForm.changeFieldAttr("field30879",2);
        WfForm.changeFieldAttr("field30886",2);
        WfForm.changeFieldAttr("field30880",2);
    }
});
// var _value = WfForm.getFieldValue("field30883");
WfForm.bindFieldChangeEvent("field237456", function(obj,id,value){
    if(value == '1'){
        WfForm.changeFieldAttr("field230327",1);
        WfForm.changeFieldAttr("field199534", 5);
        WfForm.changeFieldValue("field230327", { value: '0' });
        WfForm.changeFieldAttr("field237566",1);
        // WfForm.changeFieldValue("field30878", { value: '1' });
        // WfForm.changeFieldAttr("field30878", 1);
        // WfForm.changeFieldValue("field30879", { value: '10' });
        // WfForm.changeFieldAttr("field30879", 1);
        // WfForm.changeFieldValue("field30886", { value: '0' });
        // WfForm.changeFieldAttr("field30886", 1);
    }else{
        WfForm.changeFieldAttr("field30886", 1);
        // WfForm.changeFieldAttr("field78018",2);
        WfForm.changeFieldAttr("field237566",3);
        WfForm.changeFieldAttr("field230327",2);
    }
});

WfForm.bindFieldChangeEvent("field30875", function(obj,id,value){
    if(value == '2'){
        WfForm.changeFieldAttr("field199534", 5);
    }
});

// WfForm.bindFieldChangeEvent("field30883", function(obj,id,value){
//   if(value !== "" && value * 1 < 10000){
//       WfForm.changeFieldValue("field30878", { value: 1 });
//       WfForm.changeFieldAttr("field30878", 1);
//       WfForm.changeFieldValue("field30879", { value: 10 });
//       WfForm.changeFieldAttr("field30879", 1);
//       // WfForm.controlSelectOption("field30879", '10')
//       WfForm.changeFieldValue("field30886", { value: 0 });
//       WfForm.changeFieldAttr("field30886", 1);
//   }
// });

//2022-05-26添加↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓
WfForm.bindFieldChangeEvent("field30879", function(obj,id,value){
    if(value!=9){//采购方式不属于直接采购，清空直接采购理由赋值
        //  WfForm.changeFieldValue("field30880", {value:""});
    }
});

WfForm.bindFieldChangeEvent("field230327", function(obj,id,value){
    if(value==0){//申请类型属于新增，清空合同类型、原合同、原合同名称、原合同金额、原合同供应商名称、原合同扫描件赋值
        WfForm.changeFieldValue("field244034", {value:""});
        WfForm.changeFieldValue("field230328", {value:""});
        WfForm.changeFieldValue("field230329", {value:""});
        WfForm.changeFieldValue("field230330", {value:""});
        WfForm.changeFieldValue("field230331", {value:""});
        WfForm.changeFieldValue("field230952", {value:""});
    }
});

WfForm.bindFieldChangeEvent("field230327", function(obj,id,value){
    if(value!=2){//申请类型不属于合同变更，清空合同信息赋值
        WfForm.changeFieldValue("field244033", {value:""});
    }
});

WfForm.bindFieldChangeEvent("field230327", function(obj,id,value){
    if(value==2){//申请类型属于合同变更，清空财务预算听证含税金额（元）、财务听证会议纪要、采购类型赋值
        WfForm.changeFieldValue("field235856", {value:""});
        WfForm.changeFieldValue("field231454", {value:""});
        WfForm.changeFieldValue("field241014", {value:""});
    }
});

WfForm.bindFieldChangeEvent("field230327", function(obj,id,value){
    if(value==3){//申请类型属于合同终止，清空财务预算听证含税金额（元）、财务听证会议纪要、采购类型、合同类型赋值
        WfForm.changeFieldValue("field235856", {value:""});
        WfForm.changeFieldValue("field231454", {value:""});
        WfForm.changeFieldValue("field241014", {value:""});
        WfForm.changeFieldValue("field244034", {value:""});
    }
});

WfForm.bindFieldChangeEvent("field237456", function(obj,id,value){
    if(value==1){//采购类别属于万元以下，清空建议供应商理由、采购类型、万元以上供应商明细、采购需求评审会议纪要流程、不在库供应商(……)赋值
        WfForm.changeFieldValue("field30877", {value:""});
        WfForm.changeFieldValue("field241014", {value:""});
        WfForm.changeFieldValue("field239365", {value:""});
        WfForm.changeFieldValue("field230948", {value:""});
        WfForm.changeFieldValue("field230950", {value:""});
    }
});

WfForm.bindFieldChangeEvent("field237456", function(obj,id,value){
    if(value==0){//采购类别属于万元以上，清空万元以下供应商明细、是否京东慧采赋值
        WfForm.changeFieldValue("field238767", {value:""});
        WfForm.changeFieldValue("field237454", {value:""});
    }
});

WfForm.bindFieldChangeEvent("field241014", function(obj,id,value){
    if(value!=1){//采购类型不属于年度延续性采购，清空上年度合同编号(年度延续性采购)、上年度合同截止日期(年度延续性采购)赋值
        WfForm.changeFieldValue("field241018", {value:""});
        WfForm.changeFieldValue("field241019", {value:""});
    }
});

WfForm.bindFieldChangeEvent("field241014", function(obj,id,value){
    if(value==2){//采购类型属于试制类量产零件采购，清空采购需求评审会议纪要流程赋值
        WfForm.changeFieldValue("field230948", {value:""});
    }
});

WfForm.bindFieldChangeEvent("field241014", function(obj,id,value){
    if(value==3){//采购类型属于试制类追加采购，清空采购需求评审会议纪要流程赋值
        WfForm.changeFieldValue("field230948", {value:""});
    }
});

WfForm.bindFieldChangeEvent("field241014", function(obj,id,value){
    if(value==4){//采购类型属于量产一体化采购，清空采购需求评审会议纪要流程赋值
        WfForm.changeFieldValue("field230948", {value:""});
    }
});

WfForm.bindFieldChangeEvent("field241014", function(obj,id,value){
    if(value!=4){//采购类型不属于量产一体化采购，清空量产供应商(附件)赋值
        WfForm.changeFieldValue("field242013", {value:""});//不能清除附件
    }
});

WfForm.bindFieldChangeEvent("field241014", function(obj,id,value){
    if(value==5){//采购类型属于紧急特殊采购，清空采购需求评审会议纪要流程赋值
        WfForm.changeFieldValue("field230948", {value:""});
    }
});

WfForm.bindFieldChangeEvent("field241014", function(obj,id,value){
    if(value!=5){//采购类型不属于紧急特殊采购，清空项目背景及申请原因、节点要求、紧急特殊采购申请类型赋值
        WfForm.changeFieldValue("field243513", {value:""});
        WfForm.changeFieldValue("field243514", {value:""});
        WfForm.changeFieldValue("field243515", {value:""});
    }
});

WfForm.bindFieldChangeEvent("field243515", function(obj,id,value){
    if(value==2){//紧急特殊采购申请类型属于紧急特殊项目，清空采购计划赋值
        WfForm.changeFieldValue("field199534", {value:""});
    }
});

WfForm.bindFieldChangeEvent("field243515", function(obj,id,value){
    if(value!=2){//紧急特殊采购申请类型不属于紧急特殊项目，清空办公会批准会议纪要附件赋值
        WfForm.changeFieldValue("field243516", {value:""});//不能清除附件
    }
});

WfForm.bindFieldChangeEvent("field244033", function(obj,id,value){
    if(value==1){//合同信息属于合同一般事项变更，清空采购需求评审会议纪要流程赋值
        WfForm.changeFieldValue("field230948", {value:""});
    }
});

WfForm.bindFieldChangeEvent("field30875", function(obj,id,value){
    if(value==1){//采购项目资金渠道属于投资类，清空财务预算听证含税金额（元）、财务听证会议纪要赋值
        WfForm.changeFieldValue("field235856", {value:""});
        WfForm.changeFieldValue("field231454", {value:""});
    }
});

WfForm.bindFieldChangeEvent("field234577",function(obj,id,value){
    if(value==1){//预计金额是否大于50万属于否，清空财务预算听证含税金额（元）、财务听证会议纪要赋值
        WfForm.changeFieldValue("field235856", {value:""});
        WfForm.changeFieldValue("field231454", {value:""});
    }
});

WfForm.bindFieldChangeEvent("field199548", function(obj,id,value){
    if(value!=0){//类型不属于决策会议，清空决策会议类型赋值
        WfForm.changeFieldValue("field199551", {value:""});
    }
});

WfForm.bindFieldChangeEvent("field30885", function(obj,id,value){
    if(value!=2){//采购业务分类不属于试制类（含外制件、内制件），清空试制报价参考清单赋值
        WfForm.changeFieldValue("field230334", {value:""});
    }
});

WfForm.bindFieldChangeEvent("field30886", function(obj,id,value){
    if(value!=2){//评审方法不属于综合评审，清空综合评审表赋值
        WfForm.changeFieldValue("field230333", {value:""});
    }
});

WfForm.bindFieldChangeEvent("field280515", function(obj,id,value){
    if(value===0){//年度采购框架类别属于年度框架合同，清空预计采购含税金额（元）、议价方式、采购方式、评审方法赋值
        WfForm.changeFieldValue("field30883", {value:""});
        WfForm.changeFieldValue("field30878", {value:""});
        WfForm.changeFieldValue("field30879", {value:""});
        WfForm.changeFieldValue("field30886", {value:""});
    }
});

WfForm.bindFieldChangeEvent("field30880", function(obj,id,value){
    if(value!=11){//直接采购理由不属于技术选型，清空技术选型会议纪要附件赋值
        WfForm.changeFieldValue("field282513", {value:""});//不能清除附件
    }
});

WfForm.bindFieldChangeEvent("field30880", function(obj,id,value){
    if(value!=18){//直接采购理由不属于量产供应商，清空量产供应商(附件)赋值
        WfForm.changeFieldValue("field242013", {value:""});//不能清除附件
    }
});
//2022-05-26添加↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑
//   WfForm.bindFieldChangeEvent("field230327,field244033", function(obj,id,value){
//     if(WfForm.getFieldValue("field230327") === '2'  && WfForm.getFieldValue('field244033') === '1'){
//       WfForm.changeFieldAttr("field244034", 3);
//     }else{
//       WfForm.changeFieldAttr("field244034", 5);
//     }
// });
WfForm.bindFieldChangeEvent("field30879", function(obj,id,value){
    if(WfForm.getFieldValue("field30879") === '2'){
        WfForm.changeFieldValue("field30878",{value:0});
        WfForm.changeFieldAttr("field30878",1);
    }
    if(WfForm.getFieldValue("field30879") === '3' || WfForm.getFieldValue("field30879") === '9' || WfForm.getFieldValue("field30879") === '10'|| WfForm.getFieldValue("field30879") === '11'){
        WfForm.changeFieldAttr("field30878",3);
    }
});
if(WfForm.getFieldValue('field30886')===''){
    WfForm.changeFieldValue('field30886',{
        value:0
    })
}
if(WfForm.getFieldValue('field241014')==='4' && WfForm.getFieldValue('field30883')==='0'){
    WfForm.changeFieldValue('field30883',{
        value:null
    })
}

// WfForm.bindFieldChangeEvent("field237456,field241014,field237566", function(obj,id,value){
//     if(WfForm.getFieldValue('field237456')==='0'&&WfForm.getFieldValue('field241014')!=='4'&&WfForm.getFieldValue('field237566')==='1'){
//   WfForm.changeFieldAttr("field332513", 3);
//   WfForm.changeFieldAttr("field332514", 3);
//   WfForm.changeFieldAttr("field332515", 3);
//   WfForm.changeFieldAttr("field332516", 3);
// }else{
//   WfForm.changeFieldAttr("field332513", 2);
//   WfForm.changeFieldAttr("field332514", 2);
//   WfForm.changeFieldAttr("field332515", 2);
//   WfForm.changeFieldAttr("field332516", 2);
// }
// });
setInterval(function(){  if(WfForm.getFieldValue('field237456')==='0'&&WfForm.getFieldValue('field241014')!=='4'&&WfForm.getFieldValue('field237566')==='1'){
    WfForm.changeFieldAttr("field332513", 3);
    WfForm.changeFieldAttr("field332514", 3);
    WfForm.changeFieldAttr("field332515", 3);
    WfForm.changeFieldAttr("field332516", 3);
}else{
    WfForm.changeFieldAttr("field332513", 2);
    WfForm.changeFieldAttr("field332514", 2);
    WfForm.changeFieldAttr("field332515", 2);
    WfForm.changeFieldAttr("field332516", 2);
}},500);

//电商采购采购方式显示隐藏
class dianshangcaigou{
    constructor(){
        this.cglx = WfForm.getFieldValue('field241014')
        this.yjfsyz={
            value:'',
            attr:3
        }
        this.cgfsyz={
            value:'',
            attr:3
        }
        this.psffyz={
            value:'',
            attr:3
        }
        this.yjfsStr='field30878'
        this.cgfsStr='field30879'
        this.psffStr='field30886'
        this.cgfsmk= jQuery('tr[name=zcgfs3]')
        // this.doOriginal = true
        this.doShowed=false
        this.doHided=false
    }

    getCglx(){
        this.cglx = WfForm.getFieldValue('field241014')
    }

    saveOriginalValue(){

        this.yjfsyz.value= WfForm.getFieldValue(this.yjfsStr) || ''
        this.cgfsyz.value= WfForm.getFieldValue(this.cgfsStr)|| ''
        this.psffyz.value= WfForm.getFieldValue(this.psffStr)|| ''

        this.yjfsyz.attr = WfForm.getFieldCurViewAttr(this.yjfsStr)
        this.cgfsyz.attr = WfForm.getFieldCurViewAttr(this.cgfsStr)
        this.psffyz.attr = WfForm.getFieldCurViewAttr(this.psffStr)
        // this.doOriginal=false

    }

    doSetOriginalValue(){

        WfForm.changeFieldValue(this.yjfsStr,{
            value:this.yjfsyz.value
        })
        WfForm.changeFieldValue(this.cgfsStr,{
            value:this.cgfsyz.value
        })
        WfForm.changeFieldValue(this.psffStr,{
            value:this.psffyz.value
        })

        WfForm.changeFieldAttr(this.yjfsStr,this.yjfsyz.attr)
        WfForm.changeFieldAttr(this.cgfsStr,this.cgfsyz.attr)
        WfForm.changeFieldAttr(this.psffStr,this.psffyz.attr)
        // this.doOriginal=true   getFieldCurViewAttr

    }


    doHide(){
        [this.yjfsStr,this.cgfsStr,this.psffStr].map(x=>{
            WfForm.changeFieldValue(x,{
                value:''
            })
            WfForm.changeFieldAttr(x,2)
        })
        this.cgfsmk.hide()
        this.doShowed=false
        this.doHided=true
        // this.doOriginal=false
    }

    doShow(){
        let that = this
        that.cgfsmk.show()
        setTimeout(function(){
            [that.yjfsStr,that.cgfsStr,that.psffStr].map(x=>{
                WfForm.changeFieldAttr(x,3)
            },50)
        })
    }

    run(t){
        let that = this
        setInterval(function(){
            that.getCglx()
            if(that.cglx==='8'){
                if(that.doHided===false){
                    console.log('电商采购隐藏采购方式')
                    that.doHide()
                    that.doShowed=false
                }
            }else {
                that.saveOriginalValue()
                if(!that.doShowed&& that.doHided){
                    console.log('非电商采购还原采购方式')
                    that.doShowed=true
                    that.doHided=false
                    that.doShow()
                    that.doSetOriginalValue()
                }
            }
        },t)
    }
}
var mmdscg = new dianshangcaigou()
mmdscg.run(1000)
//2022-11-27
WfForm.bindFieldChangeEvent("field237566", function(obj,id,value){
    if(value == '1'){
        jQuery('div.etype_2_swapDiv[data-cellmark=main_35_2] span').text("闭口合同（含税，元）");
        jQuery('div.etype_2_swapDiv[data-cellmark=main_36_2] span').text("闭口合同（不含税，元）");
        WfForm.changeFieldAttr("field332514", 3);
        WfForm.changeFieldAttr("field332515", 3);
        WfForm.changeFieldAttr("field332516", 3);
    }else if(value == '0'){
        jQuery('div.etype_2_swapDiv[data-cellmark=main_35_2] span').text("合同上限（含税，元）");
        jQuery('div.etype_2_swapDiv[data-cellmark=main_36_2] span').text("合同上限（不含税，元）");
        WfForm.changeFieldAttr("field332514", 2);
        WfForm.changeFieldAttr("field332515", 2);
        WfForm.changeFieldAttr("field332516", 2);
    }
});


function kbk(){
    var value = WfForm.getFieldValue("field237566");
    if(value == '1'){
        jQuery('div.etype_2_swapDiv[data-cellmark=main_35_2] span').text("闭口合同（含税，元）");
        jQuery('div.etype_2_swapDiv[data-cellmark=main_36_2] span').text("闭口合同（不含税，元）");
        WfForm.changeFieldAttr("field332514", 3);
        WfForm.changeFieldAttr("field332515", 3);
        WfForm.changeFieldAttr("field332516", 3);
    }else if(value == '0'){
        jQuery('div.etype_2_swapDiv[data-cellmark=main_35_2] span').text("合同上限（含税，元）");
        jQuery('div.etype_2_swapDiv[data-cellmark=main_36_2] span').text("合同上限（不含税，元）");
        WfForm.changeFieldAttr("field332514", 2);
        WfForm.changeFieldAttr("field332515", 2);
        WfForm.changeFieldAttr("field332516", 2);
    }


}
//线上线下议价方式选择规则 cx 2022.12.28
jQuery(document).ready(function(){
    WfForm.bindFieldChangeEvent("field237456,field241014,field30879,field30878,field30886,field30883,field237566,field30885,field30880",function(obj,id,value){
        var cgjelbValue = WfForm.getFieldValue("field237456");//采购类别
        var cglxValue = WfForm.getFieldValue("field241014");//采购类型
        var cgfsValue = WfForm.getFieldValue("field30879");//采购方式
        var psffValue = WfForm.getFieldValue("field30886");//评审方法
        var cgywflValue = WfForm.getFieldValue("field30885");//采购业务分类
        var xmzeyghsfyyValue = WfForm.getFieldValue("field30883");//预计采购含税金额(元)
        var yjfsValue = WfForm.getFieldValue("field30878");//议价方式
        var htlbValue = WfForm.getFieldValue("field237566");//合同类别
        var zjcglyValue = WfForm.getFieldValue("field30880");//直接采购理由
        var sqlxValue = WfForm.getFieldValue("field230327");//申请类型属于新增
        if(cgjelbValue==0){//采购类别万元以上
//  WfForm.controlSelectOption("field30879", "0,1,3,9,13,14");
            WfForm.changeFieldAttr("field30878", 1);
            if(sqlxValue == 0){//申请类型属于新增
                if(cglxValue == 4 ||  cglxValue == 9){//采购类型为一体化采购和 重复采购 电商采购时
                    console.log(111111);
                    WfForm.changeFieldValue("field30878", {value:"1"});//议价方式 	非电子交易平台
                    WfForm.changeFieldAttr("field30879", 1);
                }else if(cglxValue == 8){//采购类型为 电商采购时
                    WfForm.changeFieldValue("field30878", {value:"1"});//议价方式 	非电子交易平台
                    WfForm.changeFieldAttr("field30879", 1);
                    $('tr[name=gys]').hide();
                    $('tr[name=bzkgys]').hide();

                }else if(cglxValue == 0 || cglxValue == 1){//采购类型为 常规采购 和 框架合同时
                    if(cgfsValue == 0 ||cgfsValue == 1 ||cgfsValue == 14){//议价方式为 公开招标 邀请招标 公开询比采购时
                        WfForm.changeFieldValue("field30878", {value:"0"}); //议价方式 	电子交易平台
                        $('tr[name=zjcgly]').hide();
                        WfForm.changeFieldValue("field30880", {value:""});
                        WfForm.changeFieldAttr("field30878", 1);
                        WfForm.changeFieldAttr("field30886", 2);
                    }else if (cgfsValue == 15){//邀请询比采购时
                        //console.log(cgfsValue);
                        if(psffValue == 1){//评审方法为综合评审
                            WfForm.changeFieldValue("field30878", {value:"0"});
                            WfForm.changeFieldAttr("field30878", 1);
                            $('tr[name=zjcgly]').hide();
                            WfForm.changeFieldValue("field30880", {value:""});
                        }else if(psffValue == 0){ //评审方法为最低评审
                            if(cgywflValue == 2 ){//采购业务分类为试制类
                                if(xmzeyghsfyyValue < 500000){//预估费用（含税，元）小于500000元非电子交易平台
                                    WfForm.changeFieldValue("field30878", {value:"1"});//议价方式 	非电子交易平台
                                    WfForm.changeFieldAttr("field30878", 1);
                                }else if(xmzeyghsfyyValue >= 500000){//预估费用（含税，元） 大于500000元时 议价方式 	电子交易平台
                                    WfForm.changeFieldValue("field30878", {value:"0"});
                                    WfForm.changeFieldAttr("field30878", 1);
                                }
                            }else if(cgywflValue !=2){
                                WfForm.changeFieldValue("field30878", {value:"0"});
                                WfForm.changeFieldAttr("field30878", 1);
                            }
                        }
                    }else if(cgfsValue == 9) {//直接采购
                        $('tr[name=zjcgly]').show();
                        WfForm.changeFieldAttr("field30880",3);
                        if(htlbValue == 0 ){//合同类别为开口时

                            WfForm.changeFieldValue("field30878", {value:"1"});//议价方式 	非电子交易平台
                            WfForm.changeFieldValue("field30886", {value:"0"}); //评审方法为最低评审
                            WfForm.changeFieldAttr("field30886", 1);
                            WfForm.changeFieldAttr("field30878", 1);
                        }else if(htlbValue == 1){//合同类别为闭口时
                            // console.log(zjcglyValue);
                            if(cgywflValue != 2){//采购业务分类为非试制类
                                if(zjcglyValue == 13 || zjcglyValue == 19){
                                    WfForm.changeFieldValue("field30878", {value:"1"});//议价方式 	非电子交易平台
                                    WfForm.changeFieldValue("field30886", {value:"0"}); //评审方法为最低评审
                                    WfForm.changeFieldAttr("field30886", 1);
                                    WfForm.changeFieldAttr("field30878", 1);
                                }else {
                                    // alert(1111);
                                    WfForm.changeFieldValue("field30878", {value:"0"}); //议价方式 	电子交易平台
                                    WfForm.changeFieldAttr("field30886", 1);
                                    WfForm.changeFieldAttr("field30878", 1);
                                }
                            }else if (cgywflValue == 2){//采购业务分类为试制类
                                if(zjcglyValue == 13 || zjcglyValue == 19){
                                    WfForm.changeFieldValue("field30878", {value:"1"});//议价方式 	非电子交易平台
                                    WfForm.changeFieldValue("field30886", {value:"0"}); //评审方法为最低评审
                                    WfForm.changeFieldAttr("field30886", 1);
                                    WfForm.changeFieldAttr("field30878", 1);
                                }else if(xmzeyghsfyyValue < 500000){
                                    WfForm.changeFieldValue("field30878", {value:"1"});//议价方式 	非电子交易平台
                                    WfForm.changeFieldValue("field30886", {value:"0"}); //评审方法为最低评审
                                    WfForm.changeFieldAttr("field30886", 1);
                                    WfForm.changeFieldAttr("field30878", 1);
                                }else if(xmzeyghsfyyValue >= 500000)
                                    WfForm.changeFieldValue("field30878", {value:"0"}); //议价方式 	电子交易平台
                                WfForm.changeFieldValue("field30886", {value:"0"}); //评审方法为最低评审
                                WfForm.changeFieldAttr("field30886", 1);
                                WfForm.changeFieldAttr("field30878", 1);
                            }
                        }
                    }else if(cgfsValue == 13){//客户指定（含量产供应商）
                        console.log(cgfsValue);
                        WfForm.changeFieldValue("field30878", {value:"1"});//议价方式 	非电子交易平台
                        WfForm.changeFieldValue("field30886", {value:"0"}); //评审方法为最低评审
                        $('tr[name=zjcgly]').hide();
                        WfForm.changeFieldValue("field30880", {value:""});
                        WfForm.changeFieldAttr("field30886", 1);
                        WfForm.changeFieldAttr("field30878", 1);
                    }
                }
            }

        }
    });
});
WfForm.bindFieldChangeEvent("field237456,field241014,field230327,field199534", function(obj,id,value){
    var cglxValue = WfForm.getFieldValue("field241014");//采购类型
    var cgjelbValue = WfForm.getFieldValue("field237456");//采购类别
    var sqlxValue = WfForm.getFieldValue("field230327");//申请类型属于新增
    var cgjhValue = WfForm.getFieldValue("field199534");//采购计划
    var cgxmzjqdValue = WfForm.getFieldValue("field30875");//采购项目资金渠道
    var cgxmzjqdValue = WfForm.getFieldValue("field230948");//采购项目资金渠道
    var htxxValue = WfForm.getFieldValue("field244033");//合同信息

    console.log(cglxValue)
    if(cgjelbValue==0){//采购类别万元以上
        if(sqlxValue == 0){//申请类型属于新增
            if(cgjhValue==0 ){//常规采购
                if(cgxmzjqdValue  ==0){//采购项目资金渠道属于投资类
                    WfForm.changeFieldAttr("field199534", 3);
                    $('tr[name=cgpshyjy]').show();
                }else if(cgxmzjqdValue  ==1){//采购项目资金渠道属于费用类
                    WfForm.changeFieldAttr("field199534", 3);
                    $('tr[name=cgpshyjy]').hide();
                }
            }else if (cgjhValue==1){//年度延续性采购
                $('tr[name=cgpshyjy]').show();
                WfForm.changeFieldAttr("field199534", 3);
            }else if(cgjhValue==4){//量产一体化采购
                WfForm.changeFieldAttr("field199534", 3);
                $('tr[name=cgpshyjy]').hide();
            }else if(cgjhValue==5){//紧急特殊采购
                $('tr[name=cgpshyjy]').hide();
                WfForm.changeFieldAttr("field199534", 1);
            }else if(cgjhValue==9){//重复采购
                $('tr[name=cgpshyjy]').show();
                WfForm.changeFieldAttr("field199534", 3);
            }else if(cgjhValue==8){//电商采购
                $('tr[name=cgpshyjy]').show();
                WfForm.changeFieldAttr("field199534", 3);
            }
        }else if(sqlxValue == 2){//申请类型属于合同变更
            if(htxxValue== 0 ){
                WfForm.changeFieldAttr("field199534", 3);
            }
        }else if(sqlxValue == 3){
            $('tr[name=cgpshyjy]').show();
            WfForm.changeFieldAttr("field199534", 3);
        }
    }
});


setInterval(function(){if(new Date(WfForm.getFieldValue('field30932')) >= new Date('2022-12-22') &&wfform.getFieldValue('field237456')==='1' && WfForm.getFieldValue('field230327')==='0'){jQuery('td.mainTd_19_2 span').text('是否电商采购');}else{jQuery('td.mainTd_19_2 span').text('是否京京东慧采')}},100)
//2023-03-24
WfForm.bindFieldChangeEvent("field30875", function (obj, id, value) {
    var cgxmjelb = WfForm.getFieldValue("field30875");//采购资金渠道
    var cgjh     = WfForm.getFieldValue("field199534"); //采购计划
    //alert(cgxmjelb);
    //alert(cgjh);
    if(cgxmjelb==0){
        WfForm.delDetailRow("detail_10", "all");
        $('tr[name=96tr]').hide();
        $('tr[name=97tr]').hide();
        $('tr[name=98tr]').hide();
        $('tr[name=99tr]').hide();
    }

    if(cgxmjelb==1){
        WfForm.delDetailRow("detail_10", "all");
        $('tr[name=96tr]').show();
        $('tr[name=97tr]').show();
        $('tr[name=98tr]').show();
        $('tr[name=99tr]').show();
    }

});
WfForm.bindFieldChangeEvent("field30883", function(obj,id,value){
    var sz =0;
    if(value.indexOf(".")){
        sz = (Number(value*100)/1000000).toFixed(2);
    }else{
        sz = (Number(value)/10000).toFixed(2);
    }

    WfForm.changeFieldValue("field1645485",{value:sz});


});
