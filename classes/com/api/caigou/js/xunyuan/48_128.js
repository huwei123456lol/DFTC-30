jQuery(document).ready(function(){
  var tableArray = new Array();
    $('.ant-spin-nested-loading').each(function(key,value){
        tableArray[key] = $(this);      //如果是其他标签 用 html();
    });

  var dt2Count= ModeForm.getDetailAllRowIndexStr("detail_2");
  if(dt2Count =='' || dt2Count==0){
    tableArray[1].parents('tr').css('display','none');
    $('#detailTitleTble1').parents('tr').css('display','none');
    
  }
  
});
//空白样式

