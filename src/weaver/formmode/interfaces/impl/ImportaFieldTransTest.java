package weaver.formmode.interfaces.impl;

import java.util.Map;
import weaver.conn.RecordSet;
import weaver.file.ExcelParseForPOI;
import weaver.formmode.interfaces.ImportFieldTransActionPOI;
import weaver.general.BaseBean;
import weaver.general.Util;
import weaver.hrm.User;

/**
 * 说明
 * 修改时
 * 类名要与文件名保持一致
 * class文件存放位置与路径保持一致。
 * 请把编译后的class文件，放在对应的目录中才能生效
 * 注意 同一路径下java名不能相同。
 * @author Administrator
 *
 */
public class ImportaFieldTransTest implements ImportFieldTransActionPOI{

	@Override
	public String getTransValue(Map<String, Object> param, User user, ExcelParseForPOI excelParse, int row, int col) {
		// 获取模块ID
        Integer modeId = Util.getIntValue(param.get("modeid").toString());
        //表单id
        Integer formId = Util.getIntValue(param.get("formid").toString());
        //当前字段id
        String fieldid = Util.null2String(param.get("fieldid"));
        //当前字段名(明细表字段名为 d明细表顺序_明细表字段名 如  d1_mx1wb )
        String fieldname = Util.null2String(param.get("fieldname"));
        //excel sheet顺序
        String sheetindex = Util.null2String(param.get("sheetindex"));
        // 获取当前登录人员ID
        Integer userId = user.getUID();
        //获取第 sheetindex 个sheet的第row行第col列的单元格的值 (下标都是从1开始)
        String value = excelParse.getValue(sheetindex, row, col);
        //日期类型
//        String valuedate =excelParse.getDateValue(sheetindex, row, col);
        //时间类型
//        String valuetime =excelParse.getTimeValue(sheetindex, row, col);
        //日期时间类型
//        String valuedatetime =excelParse.getDateTimeValue(sheetindex, row, col);
//        Cell cell = excelParse.getCell(sheetindex, row, col);



        //打印当前行的数据
        int rowCount = 3;
        for (int i = 1; i <= rowCount; i++) {
            String rowValue = excelParse.getValue(sheetindex, row, i);
            printRowValue(col,i,rowValue);
        }

        //捕获人员字段 **注意** 开启自定义转换接口后 人员字段需要通过该接口转换人人员id 否则无法导入
        if ("ry".equals(fieldname)) {

            //获取第二列 人员信息字段人员id查询分部
            value = getUserId(value);

        }

        //捕获分部字段 **注意** 该字段必须有值 否则无法被捕获
        if ("fb".equals(fieldname)) {

            //获取第二列 人员信息字段人员id查询分部
            String lastname = excelParse.getValue(sheetindex, row, 2);

            new BaseBean().writeLog("[ImportaFieldTransTest] 当前行人员名称 = " + lastname);

            //赋值用户分部
            value = subcompanyID(lastname);
        }

        return value;
	}

	private void printRowValue(int colIndex ,int rowIndex , String value){
        new BaseBean().writeLog("[ImportaFieldTransTest] 当前第 " + colIndex + " 行 ，第 " + rowIndex + " 列 ，VALUE = " + value);
    }


    private String getUserId(String lastname){
        String result = "0";
        RecordSet rs = new RecordSet();
        rs.execute("select id from hrmresource where lastname like '" + lastname + "'");
        if (rs.next()){
            result = rs.getString("id");
        }
	    return result;
    }


    private String subcompanyID(String lastname){
        String result = "0";
        RecordSet rs = new RecordSet();
        rs.execute("select subcompanyid1 from hrmresource where lastname like '" + lastname + "'");
        if (rs.next()){
            result = rs.getString("subcompanyid1");
        }
        return result;
    }


}
