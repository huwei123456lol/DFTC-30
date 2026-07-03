package weaver.dfqcgsjszx.quartz;

import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

import com.weaver.general.BaseBean;

import weaver.conn.RecordSet;
import weaver.dfqcgsjszx.util.RSAUtil;
import weaver.interfaces.schedule.BaseCronJob;

/**
 * 创建登陆加密的令牌
 *
 * @author Alex.Du
 */
public class CreateLoginKeyQuartz extends BaseCronJob {
    public void execute() {
        new BaseBean().writeLog("开始创建登陆加密的令牌");

        //获取所有的客户系统，循环每个系统，为其创建加密的令牌（公钥、私钥）
        RecordSet rs = new RecordSet();
        rs.execute("select * from uf_unify_login_sys");
        while (rs.next()) {
            // 获取当前系统最大的秘钥版本号
            int key_version = 0;

            RecordSet rs2 = new RecordSet();
            rs2.execute("select key_version from uf_unify_login_key where sys_name='" + rs.getString("sys_name") + "' order by key_version desc");
            if (rs2.next()) {
                new BaseBean().writeLog("查询到的最大秘钥版本为："
                        + rs2.getInt("key_version"));
                key_version = rs2.getInt("key_version");
            }
            //对新生产的秘钥版本加1
            key_version++;
            new BaseBean().writeLog("计算的新秘钥版本为：" + key_version);

            Map<Integer, String> keyMap = null;
            try {
                keyMap = RSAUtil.genKeyPair();
            } catch (NoSuchAlgorithmException e) {
                e.printStackTrace();
                new BaseBean().writeLog(rs.getString("sys_name") + "创建登陆加密的令牌出现异常：" + key_version);
                continue;
            }

            new BaseBean().writeLog("创建新的秘钥：insert into uf_unify_login_key(sys_name,key_version,public_key,private_key,create_date,create_time) values('"
                    + rs.getString("sys_name")
                    + "','"
                    + key_version
                    + "','"
                    + keyMap.get(0)
                    + "','"
                    + keyMap.get(1)
                    + "','"
                    + new SimpleDateFormat("yyyy-MM-dd").format(new Date())
                    + "','"
                    + new SimpleDateFormat("HH:mm:ss").format(new Date())
                    + "')");

            rs2.execute("insert into uf_unify_login_key(sys_name,key_version,public_key,private_key,create_date,create_time) values('"
                    + rs.getString("sys_name")
                    + "','"
                    + key_version
                    + "','"
                    + keyMap.get(0)
                    + "','"
                    + keyMap.get(1)
                    + "','"
                    + new SimpleDateFormat("yyyy-MM-dd").format(new Date())
                    + "','"
                    + new SimpleDateFormat("HH:mm:ss").format(new Date())
                    + "')");
            new BaseBean().writeLog(rs.getString("sys_name")+"创建登陆加密的令牌完毕");
        }
        new BaseBean().writeLog("所有系统创建登陆加密的令牌完毕");
    }
}
