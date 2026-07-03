
var mkmcid = ModeForm.convertFieldNameToId("mkmc"); 
var bjid = ModeForm.convertFieldNameToId("bj"); 
var fieldid = ModeForm.convertFieldNameToId("fieldid", 'detail_1'); 
ModeForm.bindFieldChangeEvent(mkmcid,function(obj,id,value){
  ModeForm.delDetailRow("detail_1", "all");
});
ModeForm.bindFieldChangeEvent(bjid, function(obj,id,value){
  ModeForm.delDetailRow("detail_1", "all");
});

function _customAddFun1(addIndexStr){ 
    //明细1新增成功后触发事件，addIndexStr即刚新增的行标示， 添加多行为(1,2,3) 
    console.log("新增的行标示："+addIndexStr); 
    var mkmcid =  ModeForm.convertFieldNameToId("mkmc");
    var mkmcValue = ModeForm.getFieldValue(mkmcid);
    var bjid = ModeForm.convertFieldNameToId("bj");
    var bjValue = ModeForm.getFieldValue(bjid);
    console.log("bjValue" +bjValue);

    let where = {
      "modeid" : mkmcValue,
      "bj" : bjValue
    };
    console.log("modeid:" + mkmcValue);
    ModeForm.appendBrowserDataUrlParam(fieldid+"_"+addIndexStr,where);
}

