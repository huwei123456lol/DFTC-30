package com.engine.dfxm.util;

import com.engine.dfxm.manager.PropertiesManager;
import com.fasterxml.jackson.core.JsonProcessingException;
import net.sf.json.JSON;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import net.sf.json.xml.XMLSerializer;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import weaver.conn.RecordSet;
import weaver.conn.RecordSetDataSource;
import weaver.docs.docs.util.DesUtils;
import weaver.general.Util;
import weaver.integration.logging.Logger;
import weaver.integration.logging.LoggerFactory;

import java.lang.reflect.Method;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 *@ClassName TransUtil
 *@Description 请说明该类的作用
 *@Author 86157
 *@Date 2023-11-14 15:47
 *@Version 1.0
 **/
public class TransUtil {
    private static Logger logger= LoggerFactory.getLogger(TransUtil.class);



    public static String transXmlToJson(String xmlString){
        try {
            int startIndex = xmlString.indexOf("<root>");
            String trimmedXmlString = xmlString.substring(startIndex);
            XMLSerializer xmlSerializer = new XMLSerializer();
            JSON json = xmlSerializer.read(trimmedXmlString);
            // 使用缩进格式化输出JSON字符串
            String str=json.toString(2);
            str=str.replaceAll("\\[\\]","\"\"");
            return  str;
        }catch (Exception e){
            logger.error("转换xml异常======"+e);
            return null;
        }
    }

    public static void main(String[] args) throws JsonProcessingException {
        String  str="[{\"a\":[]}]";
        String a= JSONArray.fromObject(str).toString().replaceAll("\\[\\]","\"\"");
        System.out.println(a);
    }

    /**
     * 自定义*/
    public static JSONObject startXMLToJSON(String xml){
        //1、定义JSON对象保存结果
        JSONObject result = new JSONObject();
        try {
            //2、使用DocumentHelper.parseText()转换为DOM文档对象
            Document document = DocumentHelper.parseText(xml);
            //3、获取DOM文档根节点
            Element rootElement = document.getRootElement();
            //4、调用自定义的方法转换为JSON数据格式
            parseJson(rootElement,result);
        } catch (DocumentException e) {
            e.printStackTrace();
        }
        return result;
    }

    public static void parseJson(Element element,JSONObject result){
        //1、获取子节点列表
        List<Element> elements = element.elements();
        //2、循环子节点列表获取数据
        for (Element e:elements) {
            //3、有数据则获取
            if (!e.elements().isEmpty() && !e.getText().isEmpty()){
                //4、定义另一个JSON对象保存子节点JSON数据
                JSONObject cjson = new JSONObject();
                //5、此处调用自身继续方法继续循环取值，知道遍历完所有字节点数据
                parseJson(e,cjson);
                if (!cjson.isEmpty()){
                    //6、添加到JSON对象
                    result.put(e.getName(),cjson);
                }
            }else {
                if (!e.getText().isEmpty()){
                    //6、添加到JSON对象
                    result.put(e.getName(),e.getText());
                }
            }
        }
    }

    public static void setFieldValue(List list, JSONObject field, String v) {
        int transtype=field.containsKey("transtype")?field.getInt("transtype"):0;
        //字典表code
        String transcode=field.containsKey("transcode")?field.getString("transcode"):"";
        //通过字典表转换后的对象字段名
        String transname=field.containsKey("transname")?field.getString("transname"):"";
        boolean isnumber=field.containsKey("no");
        if(isnumber&&"".equals(v)){
            list.add(null);
            return ;
        }
        String defaultvalue=field.containsKey("defaultvalue")?field.getString("defaultvalue"):v;
        if(transtype==0){
            //原值传输，不需转换
            list.add(v);
        }else if(transtype==1){
            //无值时，取默认值并转整数
            //model.put("FID",Util.getIntValue(mainMap.get("fid"),0));
            list.add(Util.getIntValue(v,Integer.parseInt(defaultvalue)));
        }else if(transtype==2){
            //无值时，取默认值并转浮点数
            //model.put("FID",Util.getIntValue(mainMap.get("fid"),0));
            list.add(Util.getDoubleValue(v,Double.parseDouble(defaultvalue)));
        }else if(transtype==3){
            //字典表转换返回指定字段的对象
            //｛"transname":"FNumber","transcode":"MK000031","":""｝
            //FPOOrderFinance.put("FExchangeTypeId",getDataCodeObj("FNumber","MK000022",Util.null2String(mainMap.get("fexchangetypeid"))));
            list.add(getDataCodeObj(transname,transcode,v));
        }else if(transtype==4){
            //字典表转换直接返回
            //model.put("FBusinessType",getDataCode("MK000018",Util.null2String(mainMap.get("fbusinesstype"))));
            list.add(getDataCode(transcode,v));
        }else if(transtype==5){
            //不需通过字典表转换，直接封装为指定字段名的对象返回
            //model.put("F_NBDF_CGY",getDataCodeObj("FSTAFFNUMBER",Util.null2String(mainMap.get("fpurchaserid"))));
            list.add(getDataCodeObj(transname,v));
        }else if(transtype==6){
            //在字段值后边拼接固定内容
            //model.put("F_NBDF_CGY",getDataCodeObj("FSTAFFNUMBER",Util.null2String(mainMap.get("fpurchaserid"))));
            list.add(v+defaultvalue);
        }else if(transtype==7){
            //在字段值前边拼接固定内容
            //model.put("F_NBDF_CGY",getDataCodeObj("FSTAFFNUMBER",Util.null2String(mainMap.get("fpurchaserid"))));
            list.add(defaultvalue+v);
        }else if(transtype==8){
            //取默认值并转boolean
            //model.put("F_NBDF_CGY",false));
            list.add(Boolean.parseBoolean(defaultvalue));
        }else if(transtype==9){
            //直接设置字段为默认值，且默认值是字符串
            //model.put("F_NBDF_CGY","1111"));
            list.add(defaultvalue);
        }else if(transtype==10){
            //根据字段值判断，转换为boolean值传递
            JSONObject selectObj= JSONObject.fromObject(defaultvalue);
            list.add(selectObj.containsKey(v)?selectObj.getBoolean(v):false);
        }else if(transtype==11){
            //根据字典表做值转换为boolean
            list.add(Boolean.parseBoolean(getDataCode(transcode,v)));
        }else if(transtype==12){
            //根据转换sql做值转换
            list.add(getTransSqlValue(defaultvalue,v));
        }else if(transtype==13){
            //根据转换方法做值转换
            list.add(getTransMethodValue(defaultvalue,v));
        }else if(transtype==14||transtype==15||transtype==16||transtype==17||transtype==18||transtype==20||transtype==21||transtype==22){
            //14 时间戳yyyy-MM-dd HH:mm:ss
            //15 时间戳yyyy-MM-dd HH:mm
            //16 时间戳yyyy-MM-dd
            //17 秒级时间戳
            //18 毫秒级时间戳
            //20 时间戳 自定义
            //21 时间戳 HH:mm
            //22 时间戳 HH:mm:ss
            list.add(getTimeStameByString(transtype,defaultvalue));
        }else if(transtype>25&&transtype<34){
            //将字符串转为日期后再转换为指定类型的日期字符串
            //26 时间戳yyyy-MM-dd HH:mm:ss
            //27 时间戳yyyy-MM-dd HH:mm
            //28 时间戳yyyy-MM-dd
            //29 秒级时间戳
            //30 毫秒级时间戳
            //31 时间戳 自定义
            //32 时间戳 HH:mm
            //33 时间戳 HH:mm:ss
            String of=field.getString("of");
            list.add(getTimeStameByString(transtype,defaultvalue,v,of));
        }else if(transtype==19){
            //根据数据源做sql转换
            list.add(getTransSqlValueByDataSource(defaultvalue,v,field.getString("sourcecode")));
        }else if(transtype==23){
            //根据编号转人员id
            list.add(getUserIdByWorkcode(v,defaultvalue));
        }else if(transtype==24){
            //根据编号转人员id
            list.add(getDeptIdByDeptCode(v,defaultvalue));
        }else if(transtype==25){
            //根据编号转人员id
            list.add(getCompanyIdByCompanyCode(v,defaultvalue));
        }else if(transtype==34){
            //截取指定位数的字符串
            list.add(v.substring(0,Integer.parseInt(defaultvalue)));
        }else if(transtype==35){
            //截取指定位数的字符串
            int substart=Integer.parseInt(field.getString("substart"));
            list.add(v.substring(substart,Integer.parseInt(defaultvalue)));
        }else if(transtype==36){
            //替换字符串
            String repfrom=field.getString("repfrom");
            String repto=field.getString("repto");
            list.add(v.replace(repfrom,repto));
        }else if(transtype==37){
            //替换字符串
            String repfrom=field.getString("repfrom");
            String repto=field.getString("repto");
            list.add(v.replaceAll(repfrom,repto));
        }else if(transtype==38){
            //附件字段,目标字段to的值为a@b 根据字段值获取附件的名称和可下载地址给到字段@b@c,a是名称，b是下载地址，@是分隔符
            String from=field.containsKey("f")?field.getString("f"):"";
            String to=field.containsKey("t")?field.getString("t"):from;
            String[] toArr=to.split("@");
            if(!"".equals(v)){
                JSONObject docObj=getDocArr(v);
                if(toArr.length==1){
                    list.add(docObj.getString("fileName"));
                }else if(toArr.length==2){
                    list.add(docObj.getString("fileName"));
                    list.add(docObj.getString("downloadUrl"));
                }
            }else{
                if(toArr.length==1){
                    list.add("");
                }else if(toArr.length==2){
                    list.add("");
                    list.add("");
                }
            }
        }else if(transtype==40){
            //    sql转换如果没查到值，则返回默认值
            list.add(getTransSqlValue(defaultvalue,v,field.getString("dfv")));
        }else{
            list.add(v);
        }
    }


    public static void setFieldValue(List list, JSONObject field, JSONObject rowData) {
       try {
           int transtype=field.containsKey("transtype")?field.getInt("transtype"):0;
           //字典表code
           String transcode=field.containsKey("transcode")?field.getString("transcode"):"";
           //通过字典表转换后的对象字段名
           String transname=field.containsKey("transname")?field.getString("transname"):"";
           boolean isnumber=field.containsKey("no");
           String f=field.getString("f");
           String v=rowData.containsKey(f)?rowData.getString(f):"";
           if(isnumber&&"".equals(v)){
               list.add(null);
               return ;
           }
           String defaultvalue=field.containsKey("defaultvalue")?field.getString("defaultvalue"):v;
           if(transtype==0){
               //原值传输，不需转换
               list.add(v);
           }else if(transtype==1){
               //无值时，取默认值并转整数
               //model.put("FID",Util.getIntValue(mainMap.get("fid"),0));
               list.add(Util.getIntValue(v,Integer.parseInt(defaultvalue)));
           }else if(transtype==2){
               //无值时，取默认值并转浮点数
               //model.put("FID",Util.getIntValue(mainMap.get("fid"),0));
               list.add(Util.getDoubleValue(v,Double.parseDouble(defaultvalue)));
           }else if(transtype==3){
               //字典表转换返回指定字段的对象
               //｛"transname":"FNumber","transcode":"MK000031","":""｝
               //FPOOrderFinance.put("FExchangeTypeId",getDataCodeObj("FNumber","MK000022",Util.null2String(mainMap.get("fexchangetypeid"))));
               list.add(getDataCodeObj(transname,transcode,v));
           }else if(transtype==4){
               //字典表转换直接返回
               //model.put("FBusinessType",getDataCode("MK000018",Util.null2String(mainMap.get("fbusinesstype"))));
               list.add(getDataCode(transcode,v));
           }else if(transtype==5){
               //不需通过字典表转换，直接封装为指定字段名的对象返回
               //model.put("F_NBDF_CGY",getDataCodeObj("FSTAFFNUMBER",Util.null2String(mainMap.get("fpurchaserid"))));
               list.add(getDataCodeObj(transname,v));
           }else if(transtype==6){
               //在字段值后边拼接固定内容
               //model.put("F_NBDF_CGY",getDataCodeObj("FSTAFFNUMBER",Util.null2String(mainMap.get("fpurchaserid"))));
               list.add(v+defaultvalue);
           }else if(transtype==7){
               //在字段值前边拼接固定内容
               //model.put("F_NBDF_CGY",getDataCodeObj("FSTAFFNUMBER",Util.null2String(mainMap.get("fpurchaserid"))));
               list.add(defaultvalue+v);
           }else if(transtype==8){
               //取默认值并转boolean
               //model.put("F_NBDF_CGY",false));
               list.add(Boolean.parseBoolean(defaultvalue));
           }else if(transtype==9){
               //直接设置字段为默认值，且默认值是字符串
               //model.put("F_NBDF_CGY","1111"));
               list.add(defaultvalue);
           }else if(transtype==10){
               //根据字段值判断，转换为boolean值传递
               JSONObject selectObj= JSONObject.fromObject(defaultvalue);
               list.add(selectObj.containsKey(v)?selectObj.getBoolean(v):false);
           }else if(transtype==11){
               //根据字典表做值转换为boolean
               list.add(Boolean.parseBoolean(getDataCode(transcode,v)));
           }else if(transtype==12){
               //根据转换sql做值转换
               list.add(getTransSqlValue(defaultvalue,v));
           }else if(transtype==13){
               //根据转换方法做值转换
               list.add(getTransMethodValue(defaultvalue,v));
           }else if(transtype==14||transtype==15||transtype==16||transtype==17||transtype==18||transtype==20||transtype==21||transtype==22){
               //14 时间戳yyyy-MM-dd HH:mm:ss
               //15 时间戳yyyy-MM-dd HH:mm
               //16 时间戳yyyy-MM-dd
               //17 秒级时间戳
               //18 毫秒级时间戳
               //20 时间戳 自定义
               //21 时间戳 HH:mm
               //22 时间戳 HH:mm:ss
               list.add(getTimeStameByString(transtype,defaultvalue));
           }else if(transtype>25&&transtype<34){
               //将字符串转为日期后再转换为指定类型的日期字符串
               //26 时间戳yyyy-MM-dd HH:mm:ss
               //27 时间戳yyyy-MM-dd HH:mm
               //28 时间戳yyyy-MM-dd
               //29 秒级时间戳
               //30 毫秒级时间戳
               //31 时间戳 自定义
               //32 时间戳 HH:mm
               //33 时间戳 HH:mm:ss
               String of=field.getString("of");
               list.add(getTimeStameByString(transtype,defaultvalue,v,of));
           }else if(transtype==19){
               //根据数据源做sql转换
               list.add(getTransSqlValueByDataSource(defaultvalue,v,field.getString("sourcecode")));
           }else if(transtype==23){
               //根据编号转人员id
               list.add(getUserIdByWorkcode(v,defaultvalue));
           }else if(transtype==24){
               //根据编号转人员id
               list.add(getDeptIdByDeptCode(v,defaultvalue));
           }else if(transtype==25){
               //根据编号转人员id
               list.add(getCompanyIdByCompanyCode(v,defaultvalue));
           }else if(transtype==34){
               //截取指定位数的字符串
               list.add(v.substring(0,Integer.parseInt(defaultvalue)));
           }else if(transtype==35){
               //截取指定位数的字符串
               int substart=Integer.parseInt(field.getString("substart"));
               list.add(v.substring(substart,Integer.parseInt(defaultvalue)));
           }else if(transtype==36){
               //替换字符串
               String repfrom=field.getString("repfrom");
               String repto=field.getString("repto");
               list.add(v.replace(repfrom,repto));
           }else if(transtype==37){
               //替换字符串
               String repfrom=field.getString("repfrom");
               String repto=field.getString("repto");
               list.add(v.replaceAll(repfrom,repto));
           }else if(transtype==38){
               //附件字段,目标字段to的值为a@b 根据字段值获取附件的名称和可下载地址给到字段@b@c,a是名称，b是下载地址，@是分隔符
               String from=field.containsKey("f")?field.getString("f"):"";
               String to=field.containsKey("t")?field.getString("t"):from;
               String[] toArr=to.split("@");
               if(!"".equals(v)){
                   JSONObject docObj=getDocArr(v);
                   if(toArr.length==1){
                       list.add(docObj.getString("fileName"));
                   }else if(toArr.length==2){
                       list.add(docObj.getString("fileName"));
                       list.add(docObj.getString("downloadUrl"));
                   }
               }else{
                   if(toArr.length==1){
                       list.add("");
                   }else if(toArr.length==2){
                       list.add("");
                       list.add("");
                   }
               }
           }else if(transtype==39){
               //字段需要计算
               JSONArray o=field.getJSONArray("o");
               double newv=Util.getDoubleValue(v,0d);
               for (int i = 0; i <o.size(); i++) {
                   JSONObject oRow=o.getJSONObject(i);
                   //计算类型，暂时只支持加减 0 是加 1是减
                   int ot=oRow.getInt("ot");
                   String of=oRow.getString("of");
                   double v1=Util.getDoubleValue(rowData.getString(of),0d);
                   if(ot==0){
                       newv+=v1;
                   }else if(ot==1){
                       newv-=v1;
                   }
               }
               list.add(newv);
           }else if(transtype==40){
               //    sql转换如果没查到值，则返回默认值
               list.add(getTransSqlValue(defaultvalue,v,field.getString("dfv")));
           }else if(transtype==41){
               //    下拉选转换
               //    list.add(getTransSelectValue(defaultvalue,v,field.getString("dfv")));
           }else{
               list.add(v);
           }
       }catch (Exception e){
           logger.error("字段转换异常"+e);
       }
    }

    public static void setFieldValue(JSONObject dataJSON, JSONObject field, JSONObject rowData) {
        int transtype=field.containsKey("transtype")?field.getInt("transtype"):0;
        //字典表code
        String transcode=field.containsKey("transcode")?field.getString("transcode"):"";
        //通过字典表转换后的对象字段名
        String transname=field.containsKey("transname")?field.getString("transname"):"";
        String f=field.getString("f");
        String v=rowData.containsKey(f)?rowData.getString(f):"";
        String defaultvalue=field.containsKey("defaultvalue")?field.getString("defaultvalue"):v;
        String from=field.containsKey("f")?field.getString("f"):"";
        String to=field.containsKey("t")?field.getString("t"):from;
        boolean isnumber=field.containsKey("no");
        if(isnumber&&"".equals(v)){
            dataJSON.put(to,null);
            return ;
        }
        if(transtype==0){
            //原值传输，不需转换
            dataJSON.put(to,v);
        }else if(transtype==1){
            //无值时，取默认值并转整数
            //model.put("FID",Util.getIntValue(mainMap.get("fid"),0));
            dataJSON.put(to,Util.getIntValue(v,Integer.parseInt(defaultvalue)));
        }else if(transtype==2){
            //无值时，取默认值并转浮点数
            //model.put("FID",Util.getIntValue(mainMap.get("fid"),0));
            dataJSON.put(to,Util.getDoubleValue(v,Double.parseDouble(defaultvalue)));
        }else if(transtype==3){
            //字典表转换返回指定字段的对象
            //｛"transname":"FNumber","transcode":"MK000031","":""｝
            //FPOOrderFinance.put("FExchangeTypeId",getDataCodeObj("FNumber","MK000022",Util.null2String(mainMap.get("fexchangetypeid"))));
            dataJSON.put(to,getDataCodeObj(transname,transcode,v));
        }else if(transtype==4){
            //字典表转换直接返回
            //model.put("FBusinessType",getDataCode("MK000018",Util.null2String(mainMap.get("fbusinesstype"))));
            dataJSON.put(to,getDataCode(transcode,v));
        }else if(transtype==5){
            //不需通过字典表转换，直接封装为指定字段名的对象返回
            //model.put("F_NBDF_CGY",getDataCodeObj("FSTAFFNUMBER",Util.null2String(mainMap.get("fpurchaserid"))));
            dataJSON.put(to,getDataCodeObj(transname,v));
        }else if(transtype==6){
            //在字段值后边拼接固定内容
            //model.put("F_NBDF_CGY",getDataCodeObj("FSTAFFNUMBER",Util.null2String(mainMap.get("fpurchaserid"))));
            dataJSON.put(to,v+defaultvalue);
        }else if(transtype==7){
            //在字段值前边拼接固定内容
            //model.put("F_NBDF_CGY",getDataCodeObj("FSTAFFNUMBER",Util.null2String(mainMap.get("fpurchaserid"))));
            dataJSON.put(to,defaultvalue+v);
        }else if(transtype==8){
            //取默认值并转boolean
            //model.put("F_NBDF_CGY",false));
            dataJSON.put(to,Boolean.parseBoolean(defaultvalue));
        }else if(transtype==9){
            //直接设置字段为默认值，且默认值是字符串
            //model.put("F_NBDF_CGY","1111"));
            dataJSON.put(to,defaultvalue);
        }else if(transtype==10){
            //根据字段值判断，转换为boolean值传递
            JSONObject selectObj= JSONObject.fromObject(defaultvalue);
            dataJSON.put(to,selectObj.containsKey(v)?selectObj.getBoolean(v):false);
        }else if(transtype==11){
            //根据字典表做值转换为boolean
            dataJSON.put(to,Boolean.parseBoolean(getDataCode(transcode,v)));
        }else if(transtype==12){
            //根据转换sql做值转换
            dataJSON.put(to,getTransSqlValue(defaultvalue,v));
        }else if(transtype==13){
            //根据转换方法做值转换
            dataJSON.put(to,getTransMethodValue(defaultvalue,v));
        }else if(transtype==14||transtype==15||transtype==16||transtype==17||transtype==18||transtype==20||transtype==21||transtype==22){
            //14 时间戳yyyy-MM-dd HH:mm:ss
            //15 时间戳yyyy-MM-dd HH:mm
            //16 时间戳yyyy-MM-dd
            //17 秒级时间戳
            //18 毫秒级时间戳
            //20 时间戳 自定义
            //21 时间戳 HH:mm
            //22 时间戳 HH:mm:ss
            dataJSON.put(to,getTimeStameByString(transtype,defaultvalue));
        }else if(transtype>25&&transtype<34){
            String of=field.getString("of");
            dataJSON.put(to,getTimeStameByString(transtype,defaultvalue,v,of));
        }else if(transtype==19){
            //根据数据源做sql转换
            dataJSON.put(to,getTransSqlValueByDataSource(defaultvalue,v,field.getString("sourcecode")));
        }else if(transtype==23){
            //根据编号转人员id
            dataJSON.put(to,getUserIdByWorkcode(v,defaultvalue));
        }else if(transtype==24){
            //根据编号转人员id
            dataJSON.put(to,getDeptIdByDeptCode(v,defaultvalue));
        }else if(transtype==25){
            //根据编号转人员id
            dataJSON.put(to,getCompanyIdByCompanyCode(v,defaultvalue));
        }else if(transtype==34){
            //截取指定位数的字符串
            dataJSON.put(to,v.substring(0,Integer.parseInt(defaultvalue)));
        }else if(transtype==35){
            //截取指定位数的字符串
            int substart=Integer.parseInt(field.getString("substart"));
            dataJSON.put(to,v.substring(substart,Integer.parseInt(defaultvalue)));
        }else if(transtype==36){
            //替换字符串
            String repfrom=field.getString("repfrom");
            String repto=field.getString("repto");
            dataJSON.put(to,v.replace(repfrom,repto));
        }else if(transtype==37){
            //替换字符串
            String repfrom=field.getString("repfrom");
            String repto=field.getString("repto");
            dataJSON.put(to,v.replaceAll(repfrom,repto));
        }else if(transtype==38){
            //附件字段,目标字段to的值为a@b 根据字段值获取附件的名称和可下载地址给到字段@b@c,a是名称，b是下载地址，@是分隔符
            String[] toArr=to.split("@");
            if(!"".equals(v)){
                JSONObject docObj=getDocArr(v);
                if(toArr.length==1){
                    dataJSON.put(toArr[0],docObj.getString("fileName"));
                }else if(toArr.length==2){
                    dataJSON.put(toArr[0],docObj.getString("fileName"));
                    dataJSON.put(toArr[1],docObj.getString("downloadUrl"));
                }
            }else{
                if(toArr.length==1){
                    dataJSON.put(toArr[0],"");
                }else if(toArr.length==2){
                    dataJSON.put(toArr[0],"");
                    dataJSON.put(toArr[1],"");
                }
            }
        }else if(transtype==39){
            //字段需要计算
            JSONArray o=field.getJSONArray("o");
            double newv=Util.getDoubleValue(v,0d);
            for (int i = 0; i <o.size(); i++) {
                JSONObject oRow=o.getJSONObject(i);
                //计算类型，暂时只支持加减 0 是加 1是减
                int ot=oRow.getInt("ot");
                String of=oRow.getString("of");
                double v1=Util.getDoubleValue(rowData.getString(of),0d);
                if(ot==0){
                    newv+=v1;
                }else if(ot==1){
                    newv-=v1;
                }
            }
            dataJSON.put(to,newv);
        }else if(transtype==40){
            //    sql转换如果没查到值，则返回默认值
            dataJSON.put(to,getTransSqlValue(defaultvalue,v,field.getString("dfv")));
        }else{
            dataJSON.put(to,v);
        }
    }


    public static void setFieldValue(JSONObject dataJSON, JSONObject field, String v) {
        int transtype=field.containsKey("transtype")?field.getInt("transtype"):0;
        //字典表code
        String transcode=field.containsKey("transcode")?field.getString("transcode"):"";
        //通过字典表转换后的对象字段名
        String transname=field.containsKey("transname")?field.getString("transname"):"";
        String defaultvalue=field.containsKey("defaultvalue")?field.getString("defaultvalue"):v;
        String from=field.containsKey("f")?field.getString("f"):"";
        String to=field.containsKey("t")?field.getString("t"):from;
        boolean isnumber=field.containsKey("no");
        if(isnumber&&"".equals(v)){
            dataJSON.put(to,null);
            return ;
        }
        if(transtype==0){
            //原值传输，不需转换
            dataJSON.put(to,v);
        }else if(transtype==1){
            //无值时，取默认值并转整数
            dataJSON.put(to,Util.getIntValue(v,Integer.parseInt(defaultvalue)));
        }else if(transtype==2){
            //无值时，取默认值并转浮点数
            dataJSON.put(to,Util.getDoubleValue(v,Double.parseDouble(defaultvalue)));
        }else if(transtype==3){
            //字典表转换返回指定字段的对象
            //｛"transname":"FNumber","transcode":"MK000031","":""｝
            dataJSON.put(to,getDataCodeObj(transname,transcode,v));
        }else if(transtype==4){
            //字典表转换直接返回
            dataJSON.put(to,getDataCode(transcode,v));
        }else if(transtype==5){
            //不需通过字典表转换，直接封装为指定字段名的对象返回
            dataJSON.put(to,getDataCodeObj(transname,v));
        }else if(transtype==6){
            //在字段值后边拼接固定内容
            dataJSON.put(to,v+defaultvalue);
        }else if(transtype==7){
            //在字段值前边拼接固定内容
            dataJSON.put(to,defaultvalue+v);
        }else if(transtype==8){
            //取默认值并转boolean
            dataJSON.put(to,Boolean.parseBoolean(defaultvalue));
        }else if(transtype==9){
            //直接设置字段为默认值，且默认值是字符串
            dataJSON.put(to,defaultvalue);
        }else if(transtype==10){
            //根据字段值判断，转换为boolean值传递
            JSONObject selectObj= JSONObject.fromObject(defaultvalue);
            dataJSON.put(to,selectObj.containsKey(v)?selectObj.getBoolean(v):false);
        }else if(transtype==11){
            //根据字典表做值转换为boolean
            dataJSON.put(to,Boolean.parseBoolean(getDataCode(transcode,v)));
        }else if(transtype==12){
            //根据转换sql做值转换
            dataJSON.put(to,getTransSqlValue(defaultvalue,v));
        }else if(transtype==13){
            //根据转换方法做值转换
            dataJSON.put(to,getTransMethodValue(defaultvalue,v));
        }else if(transtype==14||transtype==15||transtype==16||transtype==17||transtype==18||transtype==20||transtype==21||transtype==22){
            //14 时间戳yyyy-MM-dd HH:mm:ss
            //15 时间戳yyyy-MM-dd HH:mm
            //16 时间戳yyyy-MM-dd
            //17 秒级时间戳
            //18 毫秒级时间戳
            //20 时间戳 自定义
            //21 时间戳 HH:mm
            //22 时间戳 HH:mm:ss
            dataJSON.put(to,getTimeStameByString(transtype,defaultvalue));
        }else if(transtype>25&&transtype<34){
            String of=field.getString("of");
            dataJSON.put(to,getTimeStameByString(transtype,defaultvalue,v,of));
        }else if(transtype==19){
            //根据数据源做sql转换
            dataJSON.put(to,getTransSqlValueByDataSource(defaultvalue,v,field.getString("sourcecode")));
        }else if(transtype==23){
            //根据编号转人员id
            dataJSON.put(to,getUserIdByWorkcode(v,defaultvalue));
        }else if(transtype==24){
            //根据编号转人员id
            dataJSON.put(to,getDeptIdByDeptCode(v,defaultvalue));
        }else if(transtype==25){
            //根据编号转人员id
            dataJSON.put(to,getCompanyIdByCompanyCode(v,defaultvalue));
        }else if(transtype==34){
            //截取指定位数的字符串
            dataJSON.put(to,v.substring(0,Integer.parseInt(defaultvalue)));
        }else if(transtype==35){
            //截取指定位数的字符串
            int substart=Integer.parseInt(field.getString("substart"));
            dataJSON.put(to,v.substring(substart,Integer.parseInt(defaultvalue)));
        }else if(transtype==36){
            //替换字符串
            String repfrom=field.getString("repfrom");
            String repto=field.getString("repto");
            dataJSON.put(to,v.replace(repfrom,repto));
        }else if(transtype==37){
            //替换字符串
            String repfrom=field.getString("repfrom");
            String repto=field.getString("repto");
            dataJSON.put(to,v.replaceAll(repfrom,repto));
        }else if(transtype==38){
            //附件字段,目标字段to的值为a@b 根据字段值获取附件的名称和可下载地址给到字段@b@c,a是名称，b是下载地址，@是分隔符
            String[] toArr=to.split("@");
            if(!"".equals(v)){
                JSONObject docObj=getDocArr(v);
                if(toArr.length==1){
                    dataJSON.put(toArr[0],docObj.getString("fileName"));
                }else if(toArr.length==2){
                    dataJSON.put(toArr[0],docObj.getString("fileName"));
                    dataJSON.put(toArr[1],docObj.getString("downloadUrl"));
                }
            }else{
                if(toArr.length==1){
                    dataJSON.put(toArr[0],"");
                }else if(toArr.length==2){
                    dataJSON.put(toArr[0],"");
                    dataJSON.put(toArr[1],"");
                }
            }
        }else if(transtype==39){

        }else if(transtype==40){
            //    sql转换如果没查到值，则返回默认值
            dataJSON.put(to,getTransSqlValue(defaultvalue,v,field.getString("dfv")));
        }else{
            dataJSON.put(to,v);
        }
    }

    private static JSONObject getDocArr(String v) {
        String[] vArr=v.split(",");
        String sql="select imagefileid ,imagefilename  from DocImageFile  where docid="+vArr[vArr.length-1]+" order by versionId  desc";
        RecordSet rs=new RecordSet();
        rs.executeQuery(sql);
        JSONObject fileObj=new JSONObject();
        fileObj.put("fileName","");
        fileObj.put("downloadUrl","");
        if(rs.next()){
            fileObj.put("fileName",rs.getString("imagefilename"));
            String downloadUrl="";
            try{
                DesUtils des = new DesUtils();
                String ddcode = des.encrypt(1+ "_" + rs.getString("imagefileid"));
                PropertiesManager pm=new PropertiesManager("ECOLOGY",1);
                JSONObject pmObj=pm.getFieldValueObj();
                downloadUrl=pmObj.getString("url")+"/weaver/weaver.file.FileDownload?fileid="+rs.getString("imagefileid")+"&download=1&ddcode="+ddcode;
            }catch(Exception e){
            }
            fileObj.put("downloadUrl",downloadUrl);
        }
        return fileObj;
    }


    private static Object getTimeStameByString(int transtype, String myformtStr, String v,String oldformatStr) {
        SimpleDateFormat dateFormat = new SimpleDateFormat(oldformatStr);
        Date date = null;
        try {
            date = dateFormat.parse(v);
            System.out.println(date);
            String formatStr;
            switch (transtype){
                case 26:
                    formatStr="yyyy-MM-dd HH:mm:ss";
                    break;
                case 27:
                    formatStr="yyyy-MM-dd HH:mm";
                    break;
                case 28:
                    formatStr="yyyy-MM-dd";
                    break;
                case 29:
                    String timestamp = String.valueOf(date.getTime()/1000);
                    return Integer.valueOf(timestamp)+"";
                case 30:
                    return System.currentTimeMillis()+"";
                case 31:
                    formatStr=myformtStr;
                    break;
                case 32:
                    formatStr="HH:mm";
                    break;
                case 33:
                    formatStr="HH:mm:ss";
                    break;
                default:
                    formatStr="yyyy-MM-dd HH:mm:ss";
                    break;
            }
            SimpleDateFormat sdf = new SimpleDateFormat(formatStr);
            return sdf.format(date);
        } catch (ParseException e) {
            return v;
        }
    }

    private static String getUserIdByWorkcode(String v,String defaultvalue) {
        String sql="select id from hrmresource where workcode='"+v+"'";
        RecordSet rs=new RecordSet();
        return (rs.executeQuery(sql)&&rs.next())? rs.getString("id"):"0";
    }

    private static String getDeptIdByDeptCode(String v,String defaultvalue) {
        String sql="select id from hrmdepartment where departmentcode='"+v+"'";
        RecordSet rs=new RecordSet();
        return (rs.executeQuery(sql)&&rs.next())? rs.getString("id"):("".equals(defaultvalue)?"0":defaultvalue);
    }

    private static String getCompanyIdByCompanyCode(String v,String defaultvalue) {
        String sql="select id from hrmsubcompany where subcompanycode='"+v+"'";
        RecordSet rs=new RecordSet();
        return (rs.executeQuery(sql)&&rs.next())? rs.getString("id"):("".equals(defaultvalue)?"0":defaultvalue);
    }

    /**
     * 根据转换编码、字段值和，转换后，封装为指定key名字的json对象返回
     * @author wangkun
     * @date 2023-10-20 17:24
     * @param fieldname
     * @param code
     * @param v
     * @return com.alibaba.fastjson.JSONObject
     */
    public static com.alibaba.fastjson.JSONObject getDataCodeObj(String fieldname, String code , String v) {
        String sql="select zdbm from uf_DataWarehouse t left join uf_DataWarehouse_dt1 t1 on t.id=t1.mainid where t.wybm='"+code+"' and t1.oadyz='"+v+"'";
        RecordSet rs=new RecordSet();
        String fnumber= rs.executeQuery(sql)&&rs.next()?rs.getString("zdbm"):"";
        com.alibaba.fastjson.JSONObject ret=new com.alibaba.fastjson.JSONObject();
        ret.put(fieldname,fnumber);
        return ret;
    }

    /**
     * 将字段值封装为指定key名字的json对象返回
     * @author wangkun
     * @date 2023-10-20 17:23
     * @param fieldname
     * @param v
     * @return com.alibaba.fastjson.JSONObject
     */
    public static com.alibaba.fastjson.JSONObject getDataCodeObj(String fieldname, String v) {
        com.alibaba.fastjson.JSONObject ret=new com.alibaba.fastjson.JSONObject();
        if(!"".equals(fieldname)){
            ret.put(fieldname,v);
        }
        return ret;
    }

    /**
     * 根据转换编码code和字段值，转换后返回值
     * @author wangkun
     * @date 2023-10-20 17:22
     * @param code
     * @param v
     * @return java.lang.String
     */
    public static String getDataCode(String code ,String v) {
        String sql="select zdbm from uf_DataWarehouse t left join uf_DataWarehouse_dt1 t1 on t.id=t1.mainid where t.wybm='"+code+"' and t1.oadyz='"+v+"'";
        RecordSet rs=new RecordSet();
        return rs.executeQuery(sql)&&rs.next()?rs.getString("zdbm"):"";
    }

    /**
     * 获取到转换sql的查询结果，将{currentvalue}占位字符串替换为当前字段值
     * @author wangkun
     * @date 2022/1/15 20:31
     * @param zhsql
     * @param lybzdvalue
     * @return java.lang.String
     */
    public static String getTransSqlValue(String zhsql, String lybzdvalue) {
        try {
            if(zhsql.indexOf("{currentvalue}")>-1){
                zhsql=zhsql.replaceAll("\\{currentvalue}",lybzdvalue);
            }
            RecordSet rs=new RecordSet();
            rs.executeQuery(zhsql);
            if(rs.next()){
                return rs.getString(1);
            }
            return lybzdvalue;
        }catch (Exception e){
            return lybzdvalue;
        }
    }

    /**
     * 获取到转换sql的查询结果，将{currentvalue}占位字符串替换为当前字段值
     * @author wangkun
     * @date 2022/1/15 20:31
     * @param zhsql
     * @param lybzdvalue
     * @return java.lang.String
     */
    public static String getTransSqlValue(String zhsql, String lybzdvalue,String defaultV) {
        try {
            if(zhsql.indexOf("{currentvalue}")>-1){
                zhsql=zhsql.replaceAll("\\{currentvalue}",lybzdvalue);
            }
            RecordSet rs=new RecordSet();
            rs.executeQuery(zhsql);
            if(rs.next()){
                return rs.getString(1);
            }else{
                return defaultV;
            }
        }catch (Exception e){
            return defaultV;
        }
    }

    /**
     * 获取到转换sql的查询结果，将{currentvalue}占位字符串替换为当前字段值
     * @author wangkun
     * @date 2022/1/15 20:31
     * @param zhsql
     * @param lybzdvalue
     * @return java.lang.String
     */
    public static String getTransSqlValueByDataSource(String zhsql, String lybzdvalue,String sourcecode) {
        try {
            if(zhsql.indexOf("{currentvalue}")>-1){
                zhsql=zhsql.replaceAll("\\{currentvalue}",lybzdvalue);
            }
            RecordSetDataSource rs=new RecordSetDataSource(sourcecode);
            rs.execute(zhsql);
            if(rs.next()){
                return rs.getString(1);
            }
            return lybzdvalue;
        }catch (Exception e){
            return lybzdvalue;
        }
    }

    /**
     * 获取到转换类的查询结果值，通过反射调用方法，将当前字段值传入方法获取方法返回值
     * @author wangkun
     * @date 2022/1/15 20:32
     * @param zhff
     * @param lybzd
     * @return java.lang.String
     */
    public static String getTransMethodValue(String zhff, String lybzd) {
        try {
            // 注意此字符串必须是真实路径，就是带包名的类路径，包名.类名
            String[] zhffArr=zhff.split("#");
            String classPath = zhffArr[0];
            Class stuClass = Class.forName(classPath);
            Object obj = stuClass.newInstance();
            Method md = stuClass.getDeclaredMethod(zhffArr[1], String.class);
            return (String) md.invoke(obj, lybzd);
        } catch (Exception e) {
            return lybzd;
        }
    }

    /**
     * 根据类型值获取不同时间戳类型
     * @author wangkun
     * @date 2022/1/15 20:34
     * @param transtype
     * @param myformtStr
     * @return java.lang.String
     */
    public static String getTimeStameByString(int transtype, String myformtStr) {
        Date date = new Date();
        String formatStr;
        switch (transtype){
            case 14:
                formatStr="yyyy-MM-dd HH:mm:ss";
                break;
            case 15:
                formatStr="yyyy-MM-dd HH:mm";
                break;
            case 16:
                formatStr="yyyy-MM-dd";
                break;
            case 17:
                String timestamp = String.valueOf(date.getTime()/1000);
                return Integer.valueOf(timestamp)+"";
            case 18:
                return System.currentTimeMillis()+"";
            case 20:
                formatStr=myformtStr;
                break;
            case 21:
                formatStr="HH:mm";
                break;
            case 22:
                formatStr="HH:mm:ss";
                break;
            default:
                formatStr="yyyy-MM-dd HH:mm:ss";
                break;
        }
        SimpleDateFormat sdf = new SimpleDateFormat(formatStr);
        return sdf.format(date);
    }
}
