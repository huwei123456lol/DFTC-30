
var zdmcFieldId = ModeForm.convertFieldNameToId("zdmc","detail_1");
var cxmcid =  ModeForm.convertFieldNameToId("cxmc");
var cxid = ModeForm.convertFieldNameToId("cx", "detail_1");
ModeForm.bindFieldChangeEvent(cxmcid ,function(obj,id,value){
  console.log("cxmcid" +cxmcid);
  //删除所有明细表  只用删除所有明细行 传入的条件会自动刷新
  ModeForm.delDetailRow("detail_1", "all");
});

function _customAddFun1(addIndexStr){ 
    //明细1新增成功后触发事件，addIndexStr即刚新增的行标示， 添加多行为(1,2,3) 
    console.log("新增的行标示："+addIndexStr); 
    var cxmcid =  ModeForm.convertFieldNameToId("cxmc");
    var cxmcValue = ModeForm.getFieldValue(cxmcid);
    console.log("cxmcValue" +cxmcValue);
  // ModeForm.bindFieldChangeEvent(cxmcid, function(obj,id,value){
    // console.log("ModeForm.bindFieldChangeEvent--",obj,id,value);
    let where = {
      "cxmc" : cxmcValue,
    };
    console.log("zdmcFieldId" + zdmcFieldId);
    ModeForm.appendBrowserDataUrlParam(zdmcFieldId+"_"+addIndexStr,where);
  // });
}

