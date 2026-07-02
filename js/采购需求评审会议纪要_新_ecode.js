let enable = true;
let isrun = false


const runScript = () => {

    ecodeSDK.load({
        id: '${appId}',
        noCss: true,
        cb: function () {

            let queueRender = [];

            WfForm.bindDetailFieldChangeEvent('field1692605', function (id, rowIndex, value) {
                    console.log("WfForm.bindDetailFieldChangeEvent--", id, rowIndex, value);
                    const sArr_xmids = value.split(',');
                    const jArr_DTXMiNFO = [];
                    for (let i = 0; i < sArr_xmids.length; i++) {

                        let json_XmInfo = {};
                        json_XmInfo.xmid = sArr_xmids[i];
                        json_XmInfo.xmmc = WfForm.getBrowserShowName("field1692605_" + rowIndex).split(',')[i];
                        json_XmInfo.dt1id = rowIndex;

                        jArr_DTXMiNFO.push(json_XmInfo)
                    }


                    showDevDt(rowIndex)
                    detail4Update(rowIndex, jArr_DTXMiNFO);


                }
            )


            WfForm.registerAction(WfForm.ACTION_ADDROW + "4", function (index) {
                //alert("添加行下标是"+index);


                forceRender()

                const dt1Index = WfForm.getFieldValue('field1692695_' + index)
                jQuery('#devDtinDtTb_' + dt1Index).append($('tr[name="dt4Data"][data-rowindex="' + index + '"]'))


            });

            WfForm.registerAction(WfForm.ACTION_DELROW + "4", function (index) {
                //alert("添加行下标是"+index);

                forceRender()

                const dt1Index = WfForm.getFieldValue('field1692695_' + index)
                jQuery('#devDtinDtTb_' + dt1Index).append($('tr[name="dt4Data"][data-rowindex="' + index + '"]'))


            });

            const detail4Update = (dt1Index, jArr_updateXminfo) => {

                //获取明细4所有行下标
                const sArr_dt4Index = WfForm.getDetailAllRowIndexStr("detail_4").split(",");

                //初始化删除后需要重新渲染的原数据
                queueRender = [...sArr_dt4Index]

                console.log('dt4arr', sArr_dt4Index)
                console.log('xminfoArr', jArr_updateXminfo)

                //存放需要删除的明细行下标
                const delIndex = [];

                //初始化需要新增的明细行 ，后续会进行数据过滤
                let jArr_addXminfo = [...jArr_updateXminfo]

                //遍历明细行字段，筛选出需要删除的数据
                for (let i = 0; i < sArr_dt4Index.length; i++) {

                    // 明细行4下标
                    const rowIndex = sArr_dt4Index[i];

                    if (rowIndex !== "") {

                        //获取明细4中对 应明细1ID 值
                        const fieldMark_dt1ID = "field1692695_" + rowIndex;
                        const value_dy1ID = WfForm.getFieldValue(fieldMark_dt1ID);

                        //获取明细4中 项目id 值
                        const fieldMark_xmid = "field1692689_" + rowIndex;
                        const value_xmid = WfForm.getFieldValue(fieldMark_xmid);

                        //通过上述字段 判断明细4行中的数据是否需要删除
                        //dt1行下标判断
                        if(value_dy1ID === dt1Index){

                            //删除标记
                            let delTemp = true

                            jArr_updateXminfo.forEach((value) => {

                                console.log("filterXminfo" , value)
                                const { xmid, dt1id } = value;

                                if(dt1id !== dt1Index){

                                    console.log('与dt1下标不一致,为新数据 数据保留',dt1id)
                                    delTemp = false

                                }else{

                                    if(value_xmid === xmid){

                                        console.log('与dt1下标一致,xmid想同 数据保留',xmid)
                                        delTemp = false

                                        //对需新增的数据进行修剪
                                        jArr_addXminfo = jArr_addXminfo.filter(temp => {
                                            console.log('temp', temp)
                                            return temp.xmid !== xmid
                                        })

                                        console.log('修建后数据' ,jArr_addXminfo)
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
                const dt4data = $('tr[name="dt4Data"]')
                for (t = 0; t < dt4data.length; t++) {
                    jQuery('#oTable3').children()[0].append(dt4data[t])
                }


                //删除对应明细
                WfForm.delDetailRow("detail_4", delIndex.join(','));

                console.log('addxminfdo',jArr_addXminfo)

                for (let s = 0; s < jArr_addXminfo.length; s++) {

                    const {xmmc, xmid, dt1id} = jArr_addXminfo[s];

                    WfForm.addDetailRow("detail_4", {

                            field1692689: {
                                value: xmid,
                                specialobj: [
                                    {id: xmid, name: xmmc},
                                ]
                            },
                            field1692695: {
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
                    const dt1Index = WfForm.getFieldValue('field1692695_' + index)
                    jQuery('#devDtinDtTb_' + dt1Index).append($('tr[name="dt4Data"][data-rowindex="' + index + '"]'))

                }


            }

            const showDevDt = (rowIndex) => {

                WfForm.proxyFieldContentComp("1693142", function (info, compFn) {

                    // console.log("字段id：", info.fieldid);
                    // console.log("明细行号：", info.rowIndex);
                    // console.log("字段只读必填属性：", info.viewAttr);
                    // console.log("字段值：", info.fieldValue);

                    const Com = ecodeSDK.getCom('${appId}', 'MyReqInnerCom');

                    //返回自定义渲染的组件
                    return <Com dtid={info.rowIndex}/>

                });

                //如果此接口调用在代码块、custompage等(非模块加载前调用)，需强制渲染字段一次
                WfForm.forceRenderField("field1693142_" + rowIndex);
                //WfForm.forceRenderField("field1693142");


            }

            const titleShowDev = (dt1Index) => {

                if (jQuery('#devDtinDtTb_' + dt1Index + ' #dt4Title').length < 1) {

                    var elementToCopy = $('#dt4Title').clone();
                    jQuery('#devDtinDtTb_' + dt1Index).append(elementToCopy)

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

                const sArr_dt4Index = WfForm.getDetailAllRowIndexStr("detail_4").split(",");
                queueRender = [...sArr_dt4Index];
                forceRender()

            }

            //非创建模式 已有行项目数据进行处理
            initDtShow()
            // jQuery(document).ready(function (){
            //     console.log('init')
            //     initDtShow()

            // })

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

