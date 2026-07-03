
//空白样式
debugger;
var lcidid = ModeForm.convertFieldNameToId("lcid");
var jmidid = ModeForm.convertFieldNameToId("jmid");
var lcidvalue = ModeForm.getFieldValue(lcidid);
var jmidvalue = ModeForm.getFieldValue(jmidid);
var lcjdxxid = ModeForm.convertFieldNameToId("lcjdxx"); 
var jmtxjkdzid = ModeForm.convertFieldNameToId("jmtxjkdz");
ModeForm.appendBrowserDataUrlParam(lcjdxxid,{"lcid":lcidvalue}); 
ModeForm.appendBrowserDataUrlParam(jmtxjkdzid,{"jmid":jmidvalue});

ModeForm.bindFieldChangeEvent(lcidid,function(obj,id,value){   
  debugger;
  var lcjdxxid = ModeForm.convertFieldNameToId("lcjdxx"); 
ModeForm.appendBrowserDataUrlParam(lcjdxxid,{"lcid":value}); 
});

ModeForm.bindFieldChangeEvent(jmidid,function(obj,id,value){   
  debugger;
  var jmtxjkdzid = ModeForm.convertFieldNameToId("jmtxjkdz"); 
ModeForm.appendBrowserDataUrlParam(jmtxjkdzid,{"jmid":value}); 
});


