function partIn(id, id4) {
    const urlStr = window.location.href;
    const urlObj = {}
    if (urlStr.indexOf('?') === -1) return null
    const index = urlStr.indexOf('?') // index = 31
    const dataStr = urlStr.substr(index + 1) // dataStr = a=1&b=2&c=&d=xxx&e
    const dataArr = dataStr.split('&') // ['a=1', 'b=2', 'c=', 'd=xxx', 'e']
    dataArr.forEach(str => {
        if (str.indexOf('=') === -1) {
            urlObj[str] = undefined // { e: undefined }
        } else {
            // 如果有 '='
            // 通过 '=' 将此字符串截取成两段字符串（不推荐使用 split 分割, 因为数据中可能携带多个 '=' ）
            const innerArrIndex = str.indexOf('=')
            const key = str.substring(0, innerArrIndex)
            const value = str.substr(innerArrIndex + 1)
            // 以截取后的两段字符串作为对象的键值对
            urlObj[key] = value // {a: '1', b: '2', c: '', d: 'xxx'}
        }
    })

    /*var btns = new Array();
      $('.ant-tabs-tab').each(function(key,value){
          btns[key] = $(this);      //如果是其他标签 用 html();
      });
     var ecid = btns[1].attr('ecid')
    var n=ecid .lastIndexOf("@");
    var length = ecid.length
    var newecid = ecid.substring(n+1, length)
    */
    var paramsObj = ModeList.getListUrlInfo();

    ModeForm.showConfirm("您知悉、同意并且承诺在参与寻源时不存在以下行为，若有违反愿意承担相应责任", function () {

        if (id != null && id != "") {

            var url = "/api/srm/rfq/insert/partInQuotedPrice?id=" + id4;

            jQuery.ajax({
                type: 'GET',
                url: url,
                async: false,
                dataType: "json",
                success: function (datas) {
                    var nowDate = new Date()
                    var now = nowDate.getTime();
                    var bjjzsjDate = new Date(datas.bjjzsj)
                    var time = bjjzsjDate.getTime();
                    var bjkssjDate = new Date(datas.bjkssj)
                    var time2 = bjkssjDate.getTime();
                    var secend = now - time;
                    var secend2 = now - time2;
                    if (secend < 0 && secend2 > 0) {
                        //根据报价行跳转不同报价页面
                        window.open("/spa/cube/index.html#/main/cube/card?type=2&modeId=339&formId=-164&opentype=2&customid=201&viewfrom=fromsearchlist&billid=" + datas.id)
                    } else if (secend > 0) {
                        ModeList.showMessage("报价截止时间已过");

                    } else if (secend2 < 0) {
                        ModeList.showMessage("报价时间未开始");

                    }
                    if (urlObj.fromProt) {
                        window.opener.location.reload()
                    }
                    //window.onChangeBjtTabs(newecid);
                    ModeList.reloadTableAll();
                }
            });
        }

    }, function () {
        return;
    }, {
        title: "寻源事项说明", //弹确认框的title，仅PC端有效
        okText: "确认参与", //自定义确认按钮名称
        cancelText: "取消" //自定义取消按钮名称
    })
}