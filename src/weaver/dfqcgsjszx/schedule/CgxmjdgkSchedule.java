//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package weaver.dfqcgsjszx.schedule;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

import com.api.formmode.service.FormmodeService;
import com.api.formmode.util.FormmodeDbUtil;
import com.api.formmode.util.FormmodeUtil;
import com.api.formmode.web.FormmodeFormAction;
import com.weaver.formmodel.data.dao.FormModelInfoDao;
import weaver.conn.RecordSet;
import weaver.formmode.customjavacode.ICustomJavaCode;
import weaver.general.BaseBean;
import weaver.general.TimeUtil;
import weaver.hrm.User;
import weaver.hrm.report.schedulediff.HrmScheduleDiffUtil;

public class CgxmjdgkSchedule implements ICustomJavaCode {
    BaseBean log = new BaseBean();

    public Object execute(Map<String, Object> param) {
        Date d = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String s = sdf.format(d);
        RecordSet rs = new RecordSet();
        RecordSet rs2 = new RecordSet();
        rs.execute("select * from uf_cggl_cgxmjdgkb where xjd in (0,1,2,3,4,5)");
        while(rs.next()) {
            String billid = rs.getString("id");   //获取表单数据ID
            String infoState = rs.getString("xjd");  //获取数据当前进度
            String rwpfsj = rs.getString("rwpfsj"); // 获取采购需求任务批分时间
            int dqBG = 0;
            String zybwcsj = rs.getString("zybwcsj"); //招议标完成时间
            String httjwcsj = rs.getString("httjwcsj");  //合同提交完成时间
            String htspwcsj = rs.getString("htspwcsj");  //合同审批完成时间
            int xmjdcxts = 0;
            boolean status  = true; //判断当前状态是需要计算的状态
            switch(infoState) {
                case "1": //
                    this.log.writeLog("状态为" + infoState);
                    xmjdcxts = this.compWorkDateDiff(rwpfsj, s);
                    dqBG = rs.getInt("bg1");
                    break;
                case "2":
                    this.log.writeLog("状态为" + infoState);
                    xmjdcxts = this.compWorkDateDiff(zybwcsj, s);
                    dqBG = rs.getInt("bg3");
                    break;
                case "3":
                    this.log.writeLog("状态为" + infoState);
                    xmjdcxts = this.compWorkDateDiff(httjwcsj, s);
                    dqBG = rs.getInt("bg5");
                    break;
                case "4":
                    this.log.writeLog("状态为" + infoState);
                    xmjdcxts = this.compWorkDateDiff(htspwcsj, s);
                    dqBG = rs.getInt("bg6");
                    break;
                default:
                    this.log.writeLog("状态为" + infoState);
                    status = false; 
            }

            this.log.writeLog("项目持续时间" + xmjdcxts);
            this.log.writeLog("当前标工" + dqBG);
            if(!status){
                rs2.execute("update uf_cggl_cgxmjdgkb set xjdcxts = null, dqzt = null where id = " + billid);
            }else {
                rs2.execute("update uf_cggl_cgxmjdgkb set xjdcxts = '" + xmjdcxts + "', dqzt = '" + this.getState(dqBG, xmjdcxts) + "' where id = " + billid);
            }
        }
        return null;
    }

    private String getState(int bg, int ycxts) {
        double check = 1.5D;
        String res;
        if (ycxts <= bg) {
            res = "正常";
        } else if ((double)ycxts < (double)bg * check) {
            res = "超时";
        } else {
            res = "严重超时";
        }

    return res;
    }

    private int compWorkDateDiff(String fromdate, String todate) {
        int cx = 0;

        try {
            HrmScheduleDiffUtil hrmScheduleDiffUtil = new HrmScheduleDiffUtil();
            hrmScheduleDiffUtil.setUser(new User(6296));
            String nextDate_t = fromdate;
            int addtype = TimeUtil.dateInterval(fromdate, todate);
            int day0;
            if (addtype > 0) {
                day0 = 1;
            } else {
                day0 = -1;
            }

            while(!nextDate_t.equals(todate)) {
                nextDate_t = TimeUtil.dateAdd(nextDate_t, day0);
                boolean isworkday = hrmScheduleDiffUtil.getIsWorkday(nextDate_t);
                if (isworkday) {
                    cx += day0;
                }
            }
        } catch (Exception var9) {
            (new BaseBean()).writeLog(var9);
        }
        return cx;
    }
}
