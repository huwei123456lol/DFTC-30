let enable = true;
let isrun = false


const runScript = () => {

    ecodeSDK.load({
        id: '${appId}',
        noCss: true,
        cb: function () {

            let queueRender = [];

            WfForm.bindDetailFieldChangeEvent('field1692604', function (id, rowIndex, value) {
                    console.log("WfForm.bindDetailFieldChangeEvent--", id, rowIndex, value);
                    const sArr_gysid = value.split(',');
                    const jArr_dtGysinfo = [];
                    for (let i = 0; i < sArr_gysid.length; i++) {

                        let json_GysInfo = {};
                        json_GysInfo.gysid = sArr_gysid[i];
                        json_GysInfo.gysmc = WfForm.getBrowserShowName("field1692604_" + rowIndex).split(',')[i];
                        json_GysInfo.dt1id = rowIndex;

                        jArr_dtGysinfo.push(json_GysInfo)
                    }


                    showDevDt(rowIndex)
                    detail3Update(rowIndex, jArr_dtGysinfo);


                }
            )


            WfForm.registerAction(WfForm.ACTION_ADDROW + "3", function (index) {
                //alert("添加行下标是"+index);


                forceRender()

                const dt1Index = WfForm.getFieldValue('field1708579_' + index)
                jQuery('#devDtinDtTb_gys_' + dt1Index).append($('tr[name="dt3Data"][data-rowindex="' + index + '"]'))


            });

            WfForm.registerAction(WfForm.ACTION_DELROW + "3", function (index) {
                //alert("添加行下标是"+index);

                forceRender()

                const dt1Index = WfForm.getFieldValue('field1708579_' + index)
                jQuery('#devDtinDtTb_gys_' + dt1Index).append($('tr[name="dt3Data"][data-rowindex="' + index + '"]'))


            });

            const detail3Update = (dt1Index, jArr_updateGysinfo) => {

                //获取明细4所有行下标
                const sArr_dt3Index = WfForm.getDetailAllRowIndexStr("detail_3").split(",");

                //初始化删除后需要重新渲染的原数据
                queueRender = [...sArr_dt3Index]

                console.log('dt3arr', sArr_dt3Index)
                console.log('xminfoArr', jArr_updateGysinfo)

                //存放需要删除的明细行下标
                const delIndex = [];

                //初始化需要新增的明细行 ，后续会进行数据过滤
                let jArr_addGysinfo = [...jArr_updateGysinfo]

                //遍历明细行字段，筛选出需要删除的数据
                for (let i = 0; i < sArr_dt3Index.length; i++) {

                    // 明细行3下标
                    const rowIndex = sArr_dt3Index[i];

                    if (rowIndex !== "") {

                        //获取明细3中对 主明细1ID 值
                        const fieldMark_dt1ID = "field1708579_" + rowIndex;
                        const value_dy1ID = WfForm.getFieldValue(fieldMark_dt1ID);

                        //获取明细3中 供应商id 值
                        const fieldMark_Gysid = "field1692651_" + rowIndex;
                        const value_Gysid = WfForm.getFieldValue(fieldMark_Gysid);

                        //通过上述字段 判断明细4行中的数据是否需要删除
                        //dt1行下标判断
                        if(value_dy1ID === dt1Index){

                            //删除标记
                            let delTemp = true

                            jArr_updateGysinfo.forEach((value) => {

                                console.log("filterXminfo" , value)
                                const { gysid, dt1id } = value;

                                if(dt1id !== dt1Index){

                                    console.log('与dt1下标不一致,为新数据 数据保留',dt1id)
                                    delTemp = false

                                }else{

                                    if(value_Gysid === gysid){

                                        console.log('与dt1下标一致,xmid想同 数据保留',xmid)
                                        delTemp = false

                                        //对需新增的数据进行修剪
                                        jArr_addGysinfo = jArr_addGysinfo.filter(temp => {
                                            console.log('temp', temp)
                                            return temp.gysid !== gysid
                                        })

                                        console.log('修建后数据' ,jArr_addGysinfo)
                                    }

                                }
                            })

                            if ( delTemp ) {
                                delIndex.push(rowIndex);
                            }

                        }

                    }
                }

                console.log('queueRender1', queueRender)

                console.log('delIndex', delIndex)

                //从需要重新渲染的原行中剔除已删除的数据
                queueRender = queueRender.filter(el => !delIndex.includes(el))
                console.log('queueRender2', queueRender)

                //执行删除明细操作前 需要将所有明细行dom元素还原到原始位置
                const dt3data = $('tr[name="dt4Data"]')
                for (t = 0; t < dt3data.length; t++) {
                    jQuery('#oTable2').children()[0].append(dt3data[t])
                }


                //删除对应明细
                WfForm.delDetailRow("detail_3", delIndex.join(','));

                console.log('addxminfdo',jArr_addGysinfo)

                for (let s = 0; s < jArr_addGysinfo.length; s++) {

                    const {gysmc, gysid, dt1id} = jArr_addGysinfo[s];

                    WfForm.addDetailRow("detail_3", {

                            field1692651: {
                                value: gysid,
                                specialobj: [
                                    {id: gysid, name: gysmc},
                                ]
                            },
                            field1708579: {
                                value: dt1id
                            }
                        },
                    );

                }

            }

            //重新渲染明细4数据到 对应的明细1锚点div中
            const forceRender = () => {

                //('重新渲染')
                //const rowArr_new = WfForm.getDetailAllRowIndexStr("detail_4").split(",");
                for (i = 0; i < queueRender.length; i++) {

                    const index = queueRender[i]
                    const dt1Index = WfForm.getFieldValue('field1708579_' + index)
                    jQuery('#devDtinDtTb_gys_' + dt1Index).append($('tr[name="dt3Data"][data-rowindex="' + index + '"]'))

                }


            }

            const showDevDt = (rowIndex) => {

                WfForm.proxyFieldContentComp("1708580", function (info, compFn) {

                    // console.log("字段id：", info.fieldid);
                    // console.log("明细行号：", info.rowIndex);
                    // console.log("字段只读必填属性：", info.viewAttr);
                    // console.log("字段值：", info.fieldValue);

                    const Com = ecodeSDK.getCom('${appId}', 'MyReqInnerCom');

                    //返回自定义渲染的组件
                    return <Com dtid={info.rowIndex}/>

                });

                //如果此接口调用在代码块、custompage等(非模块加载前调用)，需强制渲染字段一次
                WfForm.forceRenderField("field1708580_" + rowIndex);
                //WfForm.forceRenderField("field1693142");


            }

            const titleShowDev = (dt1Index) => {

                if (jQuery('#devDtinDtTb_gys_' + dt1Index + ' #dt3Title').length < 1) {

                    var elementToCopy = $('#dt3Title').clone();
                    jQuery('#devDtinDtTb_gys_' + dt1Index).append(elementToCopy)

                }

            }

            //明细1新增行后 渲染table ，并增加表头
            WfForm.registerAction(WfForm.ACTION_ADDROW + "1", function (index) {

                showDevDt(index)
                titleShowDev(index)

            });

            //由于DT1首行明细为默认增加 ，此处特殊处理
            //showDevDt(0)



            const initDtShow = () => {

                const sArr_dt1Index = WfForm.getDetailAllRowIndexStr("detail_1").split(",");
                console.log('sArr_dt1Index',sArr_dt1Index)
                for (let i = 0; i < sArr_dt1Index.length; i++) {
                    const sIndex =  sArr_dt1Index[i]
                    console.log('sindex',sIndex)
                    showDevDt(sIndex)
                    titleShowDev(sIndex)
                }

                const sArr_dt4Index = WfForm.getDetailAllRowIndexStr("detail_3").split(",");
                queueRender = [...sArr_dt4Index];
                forceRender()

            }

            //非创建模式 已有行项目数据进行处理
            //initDtShow()
            // jQuery(document).ready(function (){
            //     console.log('init')
            //     initDtShow()

            // })

            let ss = setInterval(() => {

                if (window.initDtst) {
                    initDtShow()
                    clearInterval(ss)
                }

            }, 1000)

            console.log(' window.initDtst', window.initDtst)
            isrun = true
        }
    });


}


ecodeSDK.overwritePropsFnQueueMapSet('WeaReqTop', {
    fn: (newProps) => {
        if (!enable) return;
        if (!window.location.hash.startsWith('#/main/workflow/req')) return;
        if (isrun) return;
        console.log('tui!!!')
        runScript();
    }
});