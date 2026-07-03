package weaver.trq.util.report;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import weaver.conn.RecordSet;
import weaver.general.BaseBean;
import weaver.hrm.User;

/**
 * 查询考勤
 * 
 * @author FW
 * 
 */
public class MonthCheckUtil {

	public List<Map<String, String>> getMonth(User user,
			Map<String, String> otherparams, HttpServletRequest request,
			HttpServletResponse response) {

		BaseBean log = new BaseBean();
		RecordSet rs = new RecordSet();

		log.writeLog("进入查询考勤类");
		List<Map<String,String>> listMap = new ArrayList<Map<String,String>>();
		rs.execute("select * from uf_kqb");
		while (rs.next()) {
			Map<String, String> map = new HashMap<String, String>();
			map.put("bh", rs.getString("bh"));
			map.put("xm", rs.getString("xm"));
			map.put("bm", rs.getString("bm"));
			map.put("zb", rs.getString("zb"));
			map.put("rq", rs.getString("rq"));
			map.put("sw", rs.getString("sw"));
			map.put("xw", rs.getString("xw"));
			listMap.add(map);
		}

		return listMap;

	}
}
