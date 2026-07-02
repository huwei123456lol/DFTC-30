//重复采购新增 2022.12.16
WfForm.bindFieldChangeEvent("field243013_0", function (obj, id, value) {
    var field228863 = WfForm.getFieldValue("field243013_0");
    if (field228863 == 9) {
        WfForm.changeFieldValue("field228850_0", {value: "9"});  //采购方式
        WfForm.changeFieldValue("field228849_0", {value: "1"});  //议价方式
        WfForm.changeFieldValue("field228852_0", {value: "13"});  //直接采购理由
        // WfForm.changeFieldValue("field228851",{value:""});  //评审方法
        WfForm.changeFieldAttr("field228850_0", 1);
        WfForm.changeFieldAttr("field228849_0", 1);
        WfForm.changeFieldAttr("field228852_0", 1);
        WfForm.changeFieldAttr("field230341_0", 3);
        WfForm.changeFieldAttr("field228865_0", 3);
        WfForm.changeFieldAttr("field228866_0", 3);
        WfForm.changeFieldAttr("field230340_0", 3);
        // WfForm.changeFieldAttr("field228851",1);
    } else {
        WfForm.changeFieldValue("field228850_0", {value: ""});  //采购方式
        // WfForm.changeFieldValue("field228849_0",{value:""});  //议价方式
        WfForm.changeFieldValue("field228852_0", {value: ""});  //直接采购理由
        // WfForm.changeFieldValue("field228851_0",{value:""});
        WfForm.changeFieldAttr("field228850_0", 3);
        WfForm.changeFieldAttr("field228849_0", 1);
        WfForm.changeFieldAttr("field228851_0", 1);
        WfForm.changeFieldAttr("field230341_0", 2);
        WfForm.changeFieldAttr("field228865_0", 2);
        WfForm.changeFieldAttr("field228866_0", 2);
        WfForm.changeFieldAttr("field230340_0", 2);
    }
    WfForm.registerAction(WfForm.ACTION_ADDROW + "1", function (index) {
        var mxb1 = WfForm.getDetailRowCount("detail_1");
        WfForm.bindFieldChangeEvent("field243013_" + index, function (obj, id, value) {
            for (var i = 1; i <= mxb1; i++) {
                var field228863 = WfForm.getFieldValue("field243013_" + i);
                if (field228863 == 9) {
                    WfForm.changeFieldValue("field228850_" + i, {value: "9"});  //采购方式
                    WfForm.changeFieldValue("field228849_" + i, {value: "1"});  //议价方式
                    WfForm.changeFieldValue("field228852_" + i, {value: "13"});  //直接采购理由
                    // WfForm.changeFieldValue("field228851",{value:""});  //评审方法
                    WfForm.changeFieldAttr("field228850_" + i, 1);
                    WfForm.changeFieldAttr("field228849_" + i, 1);
                    WfForm.changeFieldAttr("field228852_" + i, 1);
                    WfForm.changeFieldAttr("field230341_" + i, 3);
                    WfForm.changeFieldAttr("field228865_" + i, 3);
                    WfForm.changeFieldAttr("field228866_" + i, 3);
                    WfForm.changeFieldAttr("field230340_" + i, 3);
                    // WfForm.changeFieldAttr("field228851",1);
                } else {
                    WfForm.changeFieldValue("field228850_" + i, {value: ""});  //采购方式
                    // WfForm.changeFieldValue("field228849_"+i,{value:""});  //议价方式
                    WfForm.changeFieldValue("field228852_" + i, {value: ""});  //直接采购理由
                    // WfForm.changeFieldValue("field228851_"+i,{value:""});
                    WfForm.changeFieldAttr("field228850_" + i, 3);
                    WfForm.changeFieldAttr("field228849_" + i, 1);
                    // WfForm.changeFieldAttr("field228851_"+i,1);
                    WfForm.changeFieldAttr("field230341_" + i, 2);
                    WfForm.changeFieldAttr("field228865_" + i, 2);
                    WfForm.changeFieldAttr("field228866_" + i, 2);
                    WfForm.changeFieldAttr("field230340_" + i, 2);
                }
            }
        });
    });
});
//移除明细行首行采购方式选项公开竞争性谈判、邀请竞争性谈判、询价采购 ...选项已封存 ？？？
WfForm.removeSelectOption("field228850_0", "2,3,10");

//过滤明细行首行采购计划号选项内容 已被评审会议纪要引用的数据
WfForm.appendBrowserDataUrlParam("field228845_0", {"con199533_value": "1"})

//对添加的做选项移除和浏览按钮数据处理 效果同首行
WfForm.registerAction(WfForm.ACTION_ADDROW + "1", function (index) {
    //alert("添加行下标是"+index);

    //移除采购方式选项询价采购...选项已封存 ？？？
    WfForm.removeSelectOption("field228850_" + index, "10");

    //过滤采购计划号选项内容 已被评审会议纪要引用的数据
    WfForm.appendBrowserDataUrlParam("field228845_" + index, {"con199533_value": "1"})
});


//提交校验
WfForm.registerCheckEvent(WfForm.OPER_SUBMIT, function (callback) {

    //获取明细游标
    var rowArr = WfForm.getDetailAllRowIndexStr("detail_1").split(",");

    //合同上限临时变量
    var tmp = 0;

    //遍历明细行，获取合同金额上限最大值存入tmp
    for (var i = 0; i < rowArr.length; i++) {
        var rowIndex = rowArr[i];
        if (rowIndex != "") {

            //获取合同金额上限
            var value = WfForm.getFieldValue("field228855_" + rowIndex);

            //取较大值赋予tmp
            if (parseFloat(value) > parseFloat(tmp)) {
                tmp = value;
            }
        }
    }

    // const pwr = wfform.getFieldValue("field236091");  //field228719

    // 获取评委名单数据置入Array
    const pwr = wfform.getFieldValue("field228719");
    var pwrarr = pwr.split(",");

    //获取评委数量
    var num = pwrarr.length;

    // 小于50万评委数量判断
    if (parseFloat(tmp) < parseFloat(500000)) {
        if (!(num >= 3 && num <= 5)) {
            alert('不足50万元的采购项目,需3~5名评委（至少有1名主任级及以上职级评委）')
        } else {
            callback();
        }
    }

    // 50万含-至-500万不含  评委数量判断
    if (parseFloat(tmp) >= parseFloat(500000) && parseFloat(tmp) < parseFloat(5000000)) {
        if (!(num >= 5 && num <= 7)) {
            alert('50万元以上且不足500万元的采购项目,需5~7名评委（至少有3名主任级及以上职级评委）')
        } else {
            callback();
        }
    }
    // 500万以上含  评委数量判断
    if (parseFloat(tmp) >= parseFloat(5000000)) {
        if (num < 7) {
            alert('500万元以上的采购项目7名及以上评委（至少有3名主任级评委和1名副总师及以上职级评委）')
        } else {
            callback();
        }
    }
});

//绑定明细表 申请类型、采购方式 变更事件
WfForm.bindDetailFieldChangeEvent("field228863,`field228850`", function (id, rowIndex, value) {

    //申请类型变更触发动作
    if (id == 'field228863') {

        //采购方式为 合同变更/终止 、合同变更
        if (value == '2' || value == '3') {
            //议价方式 赋值 非电子交易平台
            WfForm.changeFieldValue("field228849_" + rowIndex, {value: "1"});  //field228849_议价方式

            //采购方式 赋值 直接采购
            WfForm.changeFieldValue("field228850_" + rowIndex, {value: "9"});  //field228850_采购方式

            //直接采购理由 赋值 关联采购
            WfForm.changeFieldValue("field228852_" + rowIndex, {value: "13"});  //field228852_直接采购理由

            //评审方法 赋值 最低投标价评审
            WfForm.changeFieldValue("field228851_" + rowIndex, {value: "0"});   //field228851_评审方法

            //相关字段只读处理
            WfForm.changeFieldAttr("field228849_" + rowIndex, 1);
            WfForm.changeFieldAttr("field228850_" + rowIndex, 1);
            WfForm.changeFieldAttr("field228852_" + rowIndex, 1);
            WfForm.changeFieldAttr("field228851_" + rowIndex, 1);

        } else {
            // WfForm.changeFieldValue("field228849_" + rowIndex, { value: "" });
            WfForm.changeFieldValue("field228850_" + rowIndex, {value: ""});
            WfForm.changeFieldValue("field228852_" + rowIndex, {value: ""});
            // WfForm.changeFieldValue("field228851_" + rowIndex, { value: "" });
            WfForm.changeFieldAttr("field228849_" + rowIndex, 1);
            WfForm.changeFieldAttr("field228850_" + rowIndex, 3);
            //WfForm.changeFieldAttr("field228852_" + rowIndex, 3);
            WfForm.changeFieldAttr("field228852_" + rowIndex, 1);////2022-5-7添加
            // WfForm.changeFieldAttr("field228851_" + rowIndex, 1);
        }
    }

    //采购方式触发变更事件
    if (id == "field228850") {

        //获取申请类型
        const sqlx = wfform.getFieldValue("field228863_" + rowIndex)

        //判断申请方式 不为合同变更
        if (sqlx != '1') {

            //采购方式为直接采购
            if (value == '9') {
                // WfForm.changeFieldValue("field228851_" + rowIndex, { value: "0" });  //field228851_评审方法
                // WfForm.changeFieldAttr("field228851_" + rowIndex, 1);

                //直接采购理由 只读
                WfForm.changeFieldAttr("field228852_" + rowIndex, 1);////2022-5-7添加
            } else {
                // WfForm.changeFieldAttr("field228851_" + rowIndex, 1);

                //直接采购理由 置为空值 并且 只读
                WfForm.changeFieldValue("field228852_" + rowIndex, {value: ""});////2022-5-7添加
                WfForm.changeFieldAttr("field228852_" + rowIndex, 1);////2022-5-7添加
            }

            //采购方式属于 公开招标、邀请招标、公开竞争性谈判
            if (['0', '1', '2'].indexOf(value) != -1) {

                //议价方式 置为 电子交易平台 并且 只读
                WfForm.changeFieldValue("field228849_" + rowIndex, {value: "0"});  //field228849_议价方式
                WfForm.changeFieldAttr("field228849_" + rowIndex, 1);

            } else {

                ////议价方式 只读  。。。
                WfForm.changeFieldAttr("field228849_" + rowIndex, 1);

            }
        }
    }
});


jQuery().ready(function () {

//页面载入时处理相关逻辑，用户保存后重新打开流程时 相关逻辑正确执行
    var indexArr = WfForm.getDetailAllRowIndexStr("detail_1").split(',');
    for (var i = 0; i < indexArr.length; i++) {
        var rowIndex = indexArr[i]
        var value1 = WfForm.getFieldValue("field228863_" + rowIndex);
        if (value1 == '1') { //申请类型为合同变更

            WfForm.changeFieldValue("field228849_" + rowIndex, {value: "1"});  //field228849_议价方式
            WfForm.changeFieldValue("field228850_" + rowIndex, {value: "9"});  //field228850_采购方式
            WfForm.changeFieldValue("field228852_" + rowIndex, {value: "13"});  //field228852_直接采购理由
            WfForm.changeFieldValue("field228851_" + rowIndex, {value: "0"});   //field228851_评审方法
            WfForm.changeFieldAttr("field228849_" + rowIndex, 1);
            WfForm.changeFieldAttr("field228850_" + rowIndex, 1);
            WfForm.changeFieldAttr("field228852_" + rowIndex, 1);
            WfForm.changeFieldAttr("field228851_" + rowIndex, 1);

        } else {

            WfForm.changeFieldAttr("field228849_" + rowIndex, 1);
            WfForm.changeFieldAttr("field228850_" + rowIndex, 3);
            //WfForm.changeFieldAttr("field228852_" + rowIndex, 3);
            //WfForm.changeFieldValue("field228852_" + rowIndex, { value: "" });////2022-5-7添加////2022-5-12标记为注释
            WfForm.changeFieldAttr("field228852_" + rowIndex, 1);////2022-5-7添加
            // WfForm.changeFieldAttr("field228851_" + rowIndex, 1);
        }
    }


    //隐藏非电子平台理由
    (function () {
        setInterval(function () {
            let tot = jQuery('[data-fieldMark^=field228861_]')
            for (let i = 0; i < tot.length; i++) {
                jQuery(tot[i]).parent().parent().css('display', 'none')
            }
            delete tot
        }, 1000)
    })()


})

//2021.12.13 pw
//绑定申请类型
WfForm.bindDetailFieldChangeEvent("field228863", function (id, rowIndex, value) {

    //申请类型为 新增
    if (value == '0' || value == '') {

        //调整采购类型选项 常规采购、年度延续性采购、年度框架采购、电商采购、重复采购
        WfForm.controlSelectOption("field243013_" + rowIndex, "0,1,7,8,9");

        // ？？？没找到这个字段
        WfForm.controlSelectOption("field307051_" + rowIndex, "0,1");

    } else {

        // ？？？没找到这个字段
        WfForm.controlSelectOption("field307051_" + rowIndex, "");

        //调整采购类型选项
        WfForm.controlSelectOption("field243013_" + rowIndex, "");

        // WfForm.changeFieldValue("field228851_"+rowIndex, {value:""});
        // WfForm.changeFieldAttr("field228851_"+rowIndex, 1);
    }

    //申请类型为 合同变更、合同终止
    if (value == '2' || value == '3') {
        //field228849  field228849_议价方式  field228850 采购方式  field228852 采购理由 field228851 评审方法

        //调整 议价方式 为 非电子交易平台 并且只读
        WfForm.changeFieldValue("field228849_" + rowIndex, {value: "1"});
        WfForm.changeFieldAttr("field228849_" + rowIndex, 1);

        //调整 采购方式 为 直接采购 并且只读
        WfForm.changeFieldValue("field228850_" + rowIndex, {value: "9"});
        WfForm.changeFieldAttr("field228850_" + rowIndex, 1);

        //调整 采购理由 为 关联采购 并且只读
        WfForm.changeFieldValue("field228852_" + rowIndex, {value: "13"});
        WfForm.changeFieldAttr("field228852_" + rowIndex, 1);


        // WfForm.changeFieldValue("field228851_"+rowIndex, {value:""});
        // WfForm.changeFieldAttr("field228851_"+rowIndex, 1);
    } else {

        //议价方式 只读
        WfForm.changeFieldAttr("field228849_" + rowIndex, 1);////2022-5-7添加

        //采购方式 必填
        WfForm.changeFieldAttr("field228850_" + rowIndex, 3);////2022-5-7添加

        //WfForm.changeFieldValue("field228852_" + rowIndex, { value: "" });////2022-5-7添加//2022-5-12标记为注释

        //直接采购理由 只读
        WfForm.changeFieldAttr("field228852_" + rowIndex, 1);////2022-5-7添加
        // WfForm.changeFieldAttr("field228851_" + rowIndex, 1);////2022-5-7添加
    }
});

WfForm.bindDetailFieldChangeEvent("field228847", function (id, rowIndex, value) {//绑定采购业务分类
    if (value == '5') {
        // WfForm.changeFieldValue("field228851_"+rowIndex, {value:"0"});
        // WfForm.changeFieldAttr("field228851_"+rowIndex, 1);
    }
});

//绑定合同勒边变更事件
WfForm.bindDetailFieldChangeEvent("field237459", function (id, rowIndex, value) {

    //开口合同
    if (value == '0') {
        // WfForm.controlDetailRowDisplay("detail_1", "9", false);

        //物资目标价清单 必填
        WfForm.changeFieldAttr("field245515_" + rowIndex, 3);

    } else {  //闭口
        // WfForm.controlDetailRowDisplay("detail_1", "9", true);

        //物资目标价清单 必填
        WfForm.changeFieldAttr("field245515_" + rowIndex, 1);
        console.log(1)
    }
});

//页面载入时处理相关逻辑，用户保存后重新打开流程时 相关逻辑正确执行
var rowArr = WfForm.getDetailAllRowIndexStr("detail_1").split(",");
for (var i = 0; i < rowArr.length; i++) {

    var rowIndex = rowArr[i];
    if (rowIndex !== "") {

        //申请类型
        var value = WfForm.getFieldValue("field228863_" + rowIndex);

        //采购业务分类
        var value1 = WfForm.getFieldValue("field228847_" + rowIndex);

        //申请类型 为 新增
        if (value == '0') {

            //调整采购类型选项为 常规采购、年度延续性采购、电商采购、重复采购
            WfForm.controlSelectOption("field243013_" + rowIndex, "0,1,8,9");  //重复采购新增展示

            //。。。？？？ 字段已删除
            WfForm.controlSelectOption("field307051_" + rowIndex, "0,1");
        } else {

            //。。。？？？ 字段已删除
            WfForm.controlSelectOption("field307051_" + rowIndex, "");

            //调整采购类型选项为 空
            WfForm.controlSelectOption("field243013_" + rowIndex, "");

            // WfForm.changeFieldValue("field228851_"+rowIndex, {value:""});
            // WfForm.changeFieldAttr("field228851_"+rowIndex, 1);
        }

        //申请类型 为 合同变更、合同终止
        if (value == '2' || value == '3') {

            //field228849  field228849_议价方式  field228850 采购方式  field228852 采购理由 field228851 评审方法
            //调整 议价方式 为 非电子交易平台 并且只读
            WfForm.changeFieldValue("field228849_" + rowIndex, {value: "1"});
            WfForm.changeFieldAttr("field228849_" + rowIndex, 1);

            //调整 采购方式 为 直接采购 并且只读
            WfForm.changeFieldValue("field228850_" + rowIndex, {value: "9"});
            WfForm.changeFieldAttr("field228850_" + rowIndex, 1);

            //调整 采购理由 为 关联采购 并且只读
            WfForm.changeFieldValue("field228852_" + rowIndex, {value: "13"});
            WfForm.changeFieldAttr("field228852_" + rowIndex, 1);


            // WfForm.changeFieldValue("field228851_"+rowIndex, {value:""});

            //调整 评审方法 为 只读
            WfForm.changeFieldAttr("field228851_" + rowIndex, 1);

        } else {

            //调整 议价方式 为 只读
            WfForm.changeFieldAttr("field228849_" + rowIndex, 1);////2022-5-7添加

            //调整 采购方式 为 必填
            WfForm.changeFieldAttr("field228850_" + rowIndex, 3);////2022-5-7添加

            //WfForm.changeFieldValue("field228852_" + rowIndex, { value: "" });////2022-5-7添加//2022-5-12标记为注释

            //调整 采购理由 为 只读
            WfForm.changeFieldAttr("field228852_" + rowIndex, 1);////2022-5-7添加
            // WfForm.changeFieldAttr("field228851_" + rowIndex, 1);////2022-5-7添加

        }

        //采购业务分类 为 设备类
        if (value1 == '5') {
            // WfForm.changeFieldValue("field228851_"+rowIndex, {value:"0"});
            // WfForm.changeFieldAttr("field228851_"+rowIndex, 1);
        }
    }
}

setInterval(function () {
    var rowArr = WfForm.getDetailAllRowIndexStr("detail_1").split(",");
    for (var i = 0; i < rowArr.length; i++) {
        var rowIndex = rowArr[i];
        if (rowIndex !== "") {

            //采购类型字段fieldid
            var fieldMark0 = "field243013_" + rowIndex;

            //议价方式字段fieldid
            var fieldMark1 = "field228849_" + rowIndex;

            //采购方式字段fieldid
            var fieldMark2 = "field228850_" + rowIndex;

            //直接采购理由字段fieldid
            var fieldMark3 = "field228852_" + rowIndex;

            //评审方法字段fieldid
            var fieldMark4 = "field228851_" + rowIndex;

            //申请类型字段fieldid
            var fieldMark5 = "field228863_" + rowIndex;

            //采购类型值 为 电商采购
            if (WfForm.getFieldValue(fieldMark0) === '8') {

                // WfForm.changeSingleField(fieldMark1, {value:""}, {viewAttr:"1"});

                //修改采购方式值 为 空 显示属性为 只读
                WfForm.changeSingleField(fieldMark2, {value: ""}, {viewAttr: "1"});

                //修改直接采购理由 为 空 显示属性为 只读
                WfForm.changeSingleField(fieldMark3, {value: ""}, {viewAttr: "1"});

                // WfForm.changeSingleField(fieldMark4, {value:""}, {viewAttr:"1"});

                //添加电商采购提示文本
                var selectorStr = 'div[data-fieldmark=field228852_' + rowIndex + ']>span>span'
                jQuery(selectorStr).text('电商采购本行不需填写').css('color', 'red')

                //电商采购推荐供应商 无需填写提示
                WfForm.changeFieldValue("field386014_" + rowIndex, {value: "电商采购本行不需填写"});
                $('tr[name=dscgbhbxytx]').show();
                var selectStr = 'div[data-fieldmark=field228854_' + rowIndex + ']>span>span'
                jQuery(selectStr).text('电商采购本行不需填写').css('color', 'red')

            } else {

                //电商采购推荐供应商 无需填写提示清除
                WfForm.changeFieldValue("field386014_" + rowIndex, {value: ""});
                var selectorStr = 'div[data-fieldmark=field228852_' + rowIndex + ']>span>span'
                if (jQuery(selectorStr).text === '电商采购本行不需填写') {
                    jQuery(selectorStr).text('').css('color', 'black')
                }


                // WfForm.changeFieldAttr(fieldMark1, 3);

                //采购方式为必填
                WfForm.changeFieldAttr(fieldMark2, 3);

                //采购方式值 为 直接采购
                if (WfForm.getFieldValue(fieldMark2) === '9') {

                    //直接采购理由 必填
                    WfForm.changeFieldAttr(fieldMark3, 3);

                } else {

                    //直接采购理由 只读
                    WfForm.changeFieldAttr(fieldMark3, 1);

                }
                // WfForm.changeFieldAttr(fieldMark4, 1);
            }

            //申请类型为合同变更或终止时采购方式和直接采购理由只读
            if (WfForm.getFieldValue(fieldMark5) === '2' || WfForm.getFieldValue(fieldMark5) === '3') {
                WfForm.changeFieldAttr(fieldMark2, 1);
                WfForm.changeFieldAttr(fieldMark3, 1);
            }
        }


        let cgfsValue = WfForm.getFieldValue("field228850_" + rowIndex);

        // if(cgfsValue == 0 || cgfsValue == 1 || cgfsValue == 14)

        //采购方式值 为 公开询比招标
        if (cgfsValue == 14) {
            // console.log(999999);
            // WfForm.changeFieldValue("field228849_"+rowIndex, {value:"0"});
            // WfForm.changeFieldAttr("field228849_"+rowIndex, 1);
            // WfForm.changeFieldAttr("field228851_"+rowIndex, 2);

            //推荐供应商置为 只读
            WfForm.changeFieldAttr('field230641_' + rowIndex, 1);

            // console.log(999999);
        }


    }
}, 500)


//增加预计执行之和不能大于100  2022-11-27
WfForm.registerCheckEvent(WfForm.OPER_SUBMIT, function (callback) {

    var value = WfForm.getDetailRowCount("detail_1");
    for (var i = 0; i < value; i++) {

        //预计执行比例（本年度%）
        var field1401535 = WfForm.getFieldValue("field332517" + "_" + i);

        //预计执行比例（明年%）
        var field1401536 = WfForm.getFieldValue("field332518" + "_" + i);

        //预计执行比例（剩余年度%）
        var field1401537 = WfForm.getFieldValue("field332519" + "_" + i);


        //预计金额含税>=预计不含税金额
        //预估费用（含税，元）
        var field228855 = WfForm.getFieldValue("field228855" + "_" + i);

        //预估费用（不含税，元）
        var field1401531 = WfForm.getFieldValue("field350014" + "_" + i);

        //合同类别
        var htlb = WfForm.getFieldValue("field237459" + "_" + i);

        var mc1 = "";
        var mc2 = "";

        //开口合同
        if (htlb == '0') {

            mc1 = "合同上限含税";
            mc2 = "合同上限不含税";

        //闭口合同
        } else if (htlb == '1') {

            mc1 = "闭口合同含税";
            mc2 = "闭口合同不含税";

        }

        //判断合同含税金额需大于不含税金额
        if (Number(field228855) < Number(field1401531)) {
            alert("明细" + (i + 1) + ":" + mc1 + "需大于等于" + mc2)
            return;
        }

        //合同执行比例合计
        let hj = Number(field1401535) + Number(field1401536) + Number(field1401537);

        //合同类别
        var htlb = WfForm.getFieldValue("field237459" + "_" + i);

        //闭口合同执行比例合计100%判断
        if (hj != 100 && htlb == '1') {

            alert("明细" + (i + 1) + ":预计执行比例之和不为100%")
            return;

        } else if (htlb == '0') {

            //开口合同执行比例合计判断
            if (hj == 100 || hj == 0) {

            } else {

                alert("明细" + (i + 1) + "开口合同:预计执行比例之和应为100%或0%")
                return;
            }


        }

    }

    callback();

});

// 绑定合同类别字段变更事件
WfForm.bindDetailFieldChangeEvent("field237459", function (id, rowIndex, value) {
    console.log(rowIndex);

    if (value == '0') { //开口
        //jQuery('div.etype_2_swapDiv[data-cellmark=detail_1_7_1] span').text("合同上限（含税，元）");
        //jQuery('div.etype_2_swapDiv[data-cellmark=detail_1_7_5] span').text("合同上限（不含税，元）");

        //开口合同单价置为 可编辑
        WfForm.changeFieldAttr("field350017_" + rowIndex, 2);

        //预计执行比例（本年度%）置为 可编辑
        WfForm.changeFieldAttr("field332517_" + rowIndex, 2);

        //预计执行比例（明年%） 置为可编辑
        WfForm.changeFieldAttr("field332518_" + rowIndex, 2);

        //预计执行比例（剩余年度%） 置为可编辑
        WfForm.changeFieldAttr("field332519_" + rowIndex, 2);
        //  WfForm.changeFieldAttr("field1401534_"+rowIndex,3);

        //更改字段显示名
        //预估费用（含税，元） 显示名 改为 合同上限（含税，元）
        jQuery('div.field228855_' + rowIndex + '_swapDiv[data-cellmark=detail_1_7_2]').parent().prev().find('span').text("合同上限（含税，元）");

        //预估费用（不含税，元） 显示名改为 合同上限（不含税，元）
        jQuery('div.field350014_' + rowIndex + '_swapDiv[data-cellmark=detail_1_7_7]').parent().prev().find('span').text("合同上限（不含税，元）");

    } else {  //闭口

        //  jQuery('div.etype_2_swapDiv[data-cellmark=detail_1_7_1] span').text("闭口合同（含税，元）");
        //  jQuery('div.etype_2_swapDiv[data-cellmark=detail_1_7_5] span').text("闭口合同（不含税，元）");
        jQuery('div.field228855_' + rowIndex + '_swapDiv[data-cellmark=detail_1_7_2]').parent().prev().find('span').text("闭口合同（含税，元）");
        jQuery('div.field350014_' + rowIndex + '_swapDiv[data-cellmark=detail_1_7_7]').parent().prev().find('span').text("闭口合同（不含税，元）");
        WfForm.changeFieldAttr("field350017_" + rowIndex, 1);
        WfForm.changeFieldAttr("field332517_" + rowIndex, 3);
        WfForm.changeFieldAttr("field332518_" + rowIndex, 3);
        WfForm.changeFieldAttr("field332519_" + rowIndex, 3);
    }
});

//绑定资金渠道改变事件
WfForm.bindDetailFieldChangeEvent("field228867", function (id, rowIndex, value) {

    //费用类时 成本主管、项目负责人必填
    if (value == '0') {
        WfForm.changeFieldAttr("field237455_" + rowIndex, 3);
        WfForm.changeFieldAttr("field228859_" + rowIndex, 3);

    //投资类 成本主管、项目负责人可编辑
    } else if (value == '1') {
        WfForm.changeFieldAttr("field237455_" + rowIndex, 2);
        WfForm.changeFieldAttr("field228859_" + rowIndex, 2);
    }

});


//线上线下方式对应规则 2022-12-28 cx
jQuery(document).ready(function () {
    WfForm.bindDetailFieldChangeEvent("field243013,field228850,field228851,field228847,field228855,field228849,field237459,field228852", function (id, rowIndex, value) {   //明细值变更触发
        // alert("22222");
        var cglxValue = WfForm.getFieldValue("field243013_" + rowIndex);//采购类型
        var cgfsValue = WfForm.getFieldValue("field228850_" + rowIndex);//采购方式
        var psffValue = WfForm.getFieldValue("field228851_" + rowIndex);//评审方法
        var cgywflValue = WfForm.getFieldValue("field228847_" + rowIndex);//采购业务分类
        var ygfyhsyValue = WfForm.getFieldValue("field228855_" + rowIndex);//预估费用（含税，元）
        var yjfsValue = WfForm.getFieldValue("field228849_" + rowIndex);//议价方式
        var htlbValue = WfForm.getFieldValue("field237459_" + rowIndex);//合同类别
        var zjcglyValue = WfForm.getFieldValue("field228852_" + rowIndex);//直接采购理由
        var dscgbhbxytxValue = WfForm.getFieldValue("field386014_" + rowIndex);//电商采购本行不需填写
        var tjgysjlyValue = WfForm.getFieldValue("field228854_" + rowIndex);//推荐供应商及理由

        if (cglxValue == 9) {//采购类型为 重复采购时  议价方式为非电子交易
            WfForm.changeFieldValue("field228849_" + rowIndex, {value: "1"});//议价方式 	非电子交易平台
            WfForm.changeFieldAttr("field228849_" + rowIndex, 1);
        } else if (cglxValue == 8) {//采购类型为电商采购
            WfForm.changeFieldValue("field228849_" + rowIndex, {value: "1"});//议价方式 	非电子交易平台
            WfForm.changeFieldAttr("field228849_" + rowIndex, 1);
            // WfForm.changeFieldValue("field386014_"+rowIndex, {value:"电商采购本行不需填写"});
            //   $('tr[name=dscgbhbxytx]').show();
            // var selectStr = 'div[data-fieldmark=field228854_'+rowIndex+']>span>span'
            // jQuery(selectStr).text('电商采购本行不需填写').css('color','red')
        } else if (cglxValue == 0 || cglxValue == 1) {//常规采购或年度延续性采购

            if (cgfsValue == '0' || cgfsValue == '1' || cgfsValue == '14') {//公开招标 邀请招标 公开询比招标
                // console.log(999999);
                WfForm.changeFieldValue("field228849_" + rowIndex, {value: "0"});
                WfForm.changeFieldAttr("field228849_" + rowIndex, 1);
                WfForm.changeFieldAttr("field228851_" + rowIndex, 2);
                console.log(999999);
            } else if (cgfsValue == 15) {//邀请询比采购
                if (psffValue == 1) {//评审方法为综合评审
                    console.log(88888);
                    WfForm.changeFieldValue("field228849_" + rowIndex, {value: "0"});
                    // WfForm.changeFieldAttr("field228849_"+rowIndex, 2);
                } else if (psffValue == 0) { //评审方法为最低评审
                    if (cgywflValue == 2) {//采购业务分类为试制类
                        if (ygfyhsyValue < 500000) {//预估费用（含税，元）小于500000元非电子交易平台
                            WfForm.changeFieldValue("field228849_" + rowIndex, {value: "1"});//议价方式 	非电子交易平台
                        } else if (ygfyhsyValue >= 500000) {//预估费用（含税，元） 大于500000元时 议价方式 	电子交易平台
                            WfForm.changeFieldValue("field228849_" + rowIndex, {value: "0"});
                        }
                    } else if (cgywflValue != 2) {
                        WfForm.changeFieldValue("field228849_" + rowIndex, {value: "0"});
                    }
                }
            } else if (cgfsValue == 13) {//客户指定（含量产供应商）
                WfForm.changeFieldValue("field228849_" + rowIndex, {value: "1"});//议价方式 	非电子交易平台
                WfForm.changeFieldValue("field228851_" + rowIndex, {value: "0"}); //评审方法为最低评审
                WfForm.changeFieldAttr("field228851_" + rowIndex, 1);
                WfForm.changeFieldAttr("field228849_" + rowIndex, 1);
            } else if (cgfsValue == 9) {//直接采购
                if (htlbValue == 0) {//合同类别为开口时
                    // console.log(zjcglyValue);
                    WfForm.changeFieldValue("field228849_" + rowIndex, {value: "1"});//议价方式 	非电子交易平台
                    WfForm.changeFieldValue("field228851_" + rowIndex, {value: "0"}); //评审方法为最低评审
                    WfForm.changeFieldAttr("field228851_" + rowIndex, 1);
                    WfForm.changeFieldAttr("field228849_" + rowIndex, 1);
                } else if (htlbValue == 1) {//合同类别为闭口时
                    // console.log(zjcglyValue);
                    if (cgywflValue != 2) {//采购业务分类为非试制类
                        if (zjcglyValue == 13 || zjcglyValue == 19) {
                            WfForm.changeFieldValue("field228849_" + rowIndex, {value: "1"});//议价方式 	非电子交易平台
                            WfForm.changeFieldValue("field228851_" + rowIndex, {value: "0"}); //评审方法为最低评审
                            WfForm.changeFieldAttr("field228851_" + rowIndex, 1);
                            WfForm.changeFieldAttr("field228849_" + rowIndex, 1);
                        } else {
                            // alert(1111);
                            WfForm.changeFieldValue("field228849_" + rowIndex, {value: "0"}); //议价方式 	电子交易平台
                            WfForm.changeFieldAttr("field228849_" + rowIndex, 1);
                            WfForm.changeFieldAttr("field228851_" + rowIndex, 1);

                        }
                    } else if (cgywflValue == 2) {//采购业务分类为试制类
                        if (zjcglyValue == 13 || zjcglyValue == 19) {
                            WfForm.changeFieldValue("field228849_" + rowIndex, {value: "1"});//议价方式 	非电子交易平台
                            WfForm.changeFieldValue("field228851_" + rowIndex, {value: "0"}); //评审方法为最低评审
                            WfForm.changeFieldAttr("field228851_" + rowIndex, 1);
                            WfForm.changeFieldAttr("field228849_" + rowIndex, 1);
                        } else if (ygfyhsyValue < 500000) {
                            WfForm.changeFieldValue("field228849_" + rowIndex, {value: "1"});//议价方式 	非电子交易平台
                            WfForm.changeFieldValue("field228851_" + rowIndex, {value: "0"}); //评审方法为最低评审
                            WfForm.changeFieldAttr("field228851_" + rowIndex, 1);
                            WfForm.changeFieldAttr("field228849_" + rowIndex, 1);
                        } else if (ygfyhsyValue >= 500000)
                            WfForm.changeFieldValue("field228849_" + rowIndex, {value: "0"}); //议价方式 	电子交易平台
                        WfForm.changeFieldValue("field228851_" + rowIndex, {value: "0"}); //评审方法为最低评审
                        WfForm.changeFieldAttr("field228851_" + rowIndex, 1);
                        WfForm.changeFieldAttr("field228849_" + rowIndex, 1);
                    }
                }

            }
        }
    });
});


//2023-02-16  投资类和间接可控，间接不可空等费用号,成本主管和项目负责人改为只读
WfForm.bindDetailFieldChangeEvent("field228867", function (id, rowIndex, value) {
    var zjlx = WfForm.getFieldValue('field228867_' + rowIndex);
    var xmfl = WfForm.getFieldValue('field350013_' + rowIndex);
    if (zjlx == '1' || (zjlx == '0' && xmfl == '0')) {
        WfForm.changeFieldAttr("field237455_" + rowIndex, 1);
        WfForm.changeFieldAttr("field228859_" + rowIndex, 1);
    } else {
        WfForm.changeFieldAttr("field237455_" + rowIndex, 3);
        WfForm.changeFieldAttr("field228859_" + rowIndex, 3);
    }

});


WfForm.bindDetailFieldChangeEvent("field350013", function (id, rowIndex, value) {
    var zjlx = WfForm.getFieldValue('field228867_' + rowIndex);
    var xmfl = WfForm.getFieldValue('field350013_' + rowIndex);
    if (zjlx == '1' || (zjlx == '0' && xmfl == '0')) {
        WfForm.changeFieldAttr("field237455_" + rowIndex, 1);
        WfForm.changeFieldAttr("field228859_" + rowIndex, 1);
    } else {
        WfForm.changeFieldAttr("field237455_" + rowIndex, 3);
        WfForm.changeFieldAttr("field228859_" + rowIndex, 3);
    }

});

//采购业务分类为设备类评审方法默认锁死；最低投标价评审
WfForm.bindDetailFieldChangeEvent("field228847", function (id, rowIndex, value) {
    if (value == '5') {

        WfForm.changeSingleField("field228851_" + rowIndex, {value: "0"}, {viewAttr: "1"});
    }
});

//如果万元以上直采理由是集团控股，那么供应商必须都是集团控股有一个不是就提交不了，战略供应商也是一样
WfForm.registerCheckEvent(WfForm.OPER_SUBMIT, function (callback) {

    var rowArr = WfForm.getDetailAllRowIndexStr("detail_1").split(",");
    for (var i = 0; i < rowArr.length; i++) {
        var rowIndex = rowArr[i];
        if (rowIndex != "") {
            var ygfy = WfForm.getFieldValue("field228855_" + rowIndex);//预估费用含税
            var zjcgly = WfForm.getFieldValue("field228850_" + rowIndex);//直接采购理由  9
            var cgfs = WfForm.getFieldValue("field228852_" + rowIndex);//采购方式  12-集团 14-战略
            var fzlgys = WfForm.getFieldValue("field1656407_" + rowIndex);//非战略供应商
            var fjtkggys = WfForm.getFieldValue("field1656408_" + rowIndex);//非集团控股供应商
            if (Number(ygfy) > 10000) {
                if (cgfs == '12') {
                    if (fjtkggys != '') {
                        alert('明细' + (i + 1) + ':直接采购理由是集团控股，请选择集团控股供应商');
                        return;
                    }
                }

                if (cgfs == '14') {
                    if (fzlgys != '') {
                        alert('明细' + (i + 1) + ':直接采购理由是战略/框架合作股，请选择战略供应商应商');
                        return;
                    }

                }

            }


        }
    }


    callback();

});

