package com.engine.dfxm.manager;

import net.sf.json.JSONObject;
import weaver.conn.RecordSet;

/**
 *@ClassName PropertiesManager
 *@Description 获取uf_properties配置表的数据
 *@Author 86157
 *@Date 2022/12/13 14:47
 *@Version 1.0
 **/
public class PropertiesManager {

    private String fieldkey;
    private String fieldvalue;
    private String pzms;
    private int sfqy;
    private JSONObject fieldValueObj;

    public PropertiesManager(String fieldkey){
        String sql="select fieldvalue,pzms,sfqy from uf_properties where fieldkey=?";
        RecordSet rs=new RecordSet();
        rs.executeQuery(sql,fieldkey);
        if(rs.next()){
            this.fieldkey=fieldkey;
            fieldvalue=rs.getString("fieldvalue");
            pzms=rs.getString("pzms");
            sfqy=rs.getInt("sfqy");
        }
    }

    /**
     * 根据类型获取配置信息，如果传1，则会将配置值转换为json对象赋值给fieldValueObj
     * 注意：配置的时候是以json字符串形式配置的值才可以转换成功，否则fieldValueObj返回null
     * @author wangkun
     * @date 2022/12/15 16:41
     * @param fieldkey
     * @param type
     * @return
     */
    public PropertiesManager(String fieldkey, int type){
        String sql="select fieldvalue,pzms,sfqy from uf_properties where fieldkey=? and sfqy=1";
        RecordSet rs=new RecordSet();
        rs.executeQuery(sql,fieldkey);
        fieldValueObj=null;
        if(rs.next()){
            this.fieldkey=fieldkey;
            fieldvalue=rs.getString("fieldvalue");
            pzms=rs.getString("pzms");
            sfqy=rs.getInt("sfqy");
            if(type==1){
                try {
                    fieldValueObj= JSONObject.fromObject(fieldvalue);
                }catch (Exception e){
                }
            }
        }
    }

    public String getFieldkey() {
        return fieldkey;
    }

    public void setFieldkey(String fieldkey) {
        this.fieldkey = fieldkey;
    }

    public String getFieldvalue() {
        return fieldvalue;
    }

    public void setFieldvalue(String fieldvalue) {
        this.fieldvalue = fieldvalue;
    }

    public String getPzms() {
        return pzms;
    }

    public void setPzms(String pzms) {
        this.pzms = pzms;
    }

    public int getSfqy() {
        return sfqy;
    }

    public void setSfqy(int sfqy) {
        this.sfqy = sfqy;
    }

    public JSONObject getFieldValueObj() {
        return fieldValueObj;
    }

    public void setFieldValueObj(JSONObject fieldValueObj) {
        this.fieldValueObj = fieldValueObj;
    }
}
