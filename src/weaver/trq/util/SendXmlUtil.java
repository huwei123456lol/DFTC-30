package weaver.trq.util;

import java.util.List;
import java.util.Map;

import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;

import weaver.conn.RecordSet;

public class SendXmlUtil {

	public static String createCEAXML(String ceaNo, String projectName,
			String fsxbgcxmh, String dept, String fszxbm, String orgbh,
			String user, String username, String money, String fsxbgcxmll,
			String place, String state, String fsWorkNo, String ceatype,
			String fsxbdq, String fsxbyjgckgsj, String fsxbyjgcjgsj,
			String tqsj, String gzzsr, String fsxbgcl1, String fsxbgcxmgs1,
			String fsxbgcjafje, String fsxbgcclfje, String fsxbgcsjfje,
			String fsxbgcjlfje, String fsxbgcbcfje, String fsxbgckcfje,
			String fsxbgfje, String fsxbqtje, String fsxbbkyjfyje,
			String lcstate, String isgczcb) {
		Document document = DocumentHelper.createDocument();

		Element xmlinfoElement = document.addElement("xmlinfo");

		Element pkGroupElement = xmlinfoElement.addElement("ceaNo");
		pkGroupElement.setText(ceaNo);

		Element pk_orgElement = xmlinfoElement.addElement("projectName");
		pk_orgElement.setText(projectName);

		Element pkorgElement = xmlinfoElement.addElement("fsxbgcxmh");
		pkorgElement.setText(fsxbgcxmh);

		Element pk_org_vElement = xmlinfoElement.addElement("dept");
		pk_org_vElement.setText(dept);

		Element totalElement = xmlinfoElement.addElement("fszxbm");
		totalElement.setText(fszxbm);

		Element totElement = xmlinfoElement.addElement("fsxbgcxmll");
		totElement.setText(fsxbgcxmll);

		Element fydwbmElement = xmlinfoElement.addElement("orgbh");
		fydwbmElement.setText(orgbh);

		Element fydwbm_vElement = xmlinfoElement.addElement("user");
		fydwbm_vElement.setText(user);

		Element fydeptidElement = xmlinfoElement.addElement("username");
		fydeptidElement.setText(username);

		Element fydeptid_vElement = xmlinfoElement.addElement("money");
		fydeptid_vElement.setText(money);

		Element dwbmElement = xmlinfoElement.addElement("place");
		dwbmElement.setText(place);

		Element dwbm_vElement = xmlinfoElement.addElement("state");
		dwbm_vElement.setText(state);

		Element deptidElement = xmlinfoElement.addElement("fsWorkNo");
		deptidElement.setText(fsWorkNo);

		Element deptid_vElement = xmlinfoElement.addElement("ceatype");
		deptid_vElement.setText(ceatype);

		Element pk_payorgElement = xmlinfoElement.addElement("fsxbdq");
		pk_payorgElement.setText(fsxbdq);

		Element djdlElement = xmlinfoElement.addElement("fsxbyjgckgsj");
		djdlElement.setText(fsxbyjgckgsj);

		Element fsxbyjgcjgsjElement = xmlinfoElement.addElement("fsxbyjgcjgsj");
		fsxbyjgcjgsjElement.setText(fsxbyjgcjgsj);

		Element djlxbmElement = xmlinfoElement.addElement("tqsj");
		djlxbmElement.setText(tqsj);

		Element djrqElement = xmlinfoElement.addElement("gctzje");
		djrqElement.setText(money);

		Element payflagElement = xmlinfoElement.addElement("gzzsr");
		payflagElement.setText(gzzsr);

		Element paytargetElement = xmlinfoElement.addElement("fsxbgcl");
		paytargetElement.setText(fsxbgcl1);

		Element receiverElement = xmlinfoElement.addElement("fsxbgcxmgs");
		receiverElement.setText(fsxbgcxmgs1);

		Element customerElement = xmlinfoElement.addElement("fsxbgcjafje");
		customerElement.setText(fsxbgcjafje);

		Element jkbxrElement = xmlinfoElement.addElement("fsxbgcclfje");
		jkbxrElement.setText(fsxbgcclfje);

		Element operatorElement = xmlinfoElement.addElement("fsxbgcsjfje");
		operatorElement.setText(fsxbgcsjfje);

		Element jsfsElement = xmlinfoElement.addElement("fsxbgcjlfje");
		jsfsElement.setText(fsxbgcjlfje);

		Element fkyhzhElement = xmlinfoElement.addElement("fsxbgcbcfje");
		fkyhzhElement.setText(fsxbgcbcfje);

		Element zfybjeElement = xmlinfoElement.addElement("fsxbgckcfje");
		zfybjeElement.setText(fsxbgckcfje);

		Element zfbbjeElement = xmlinfoElement.addElement("fsxbgfje");
		zfbbjeElement.setText(fsxbgfje);

		Element szxmidElement = xmlinfoElement.addElement("fsxbqtje");
		szxmidElement.setText(fsxbqtje);

		Element bzbmElement = xmlinfoElement.addElement("fsxbbkyjfyje");
		bzbmElement.setText(fsxbbkyjfyje);

		Element bbhlElement = xmlinfoElement.addElement("lcstate");
		bbhlElement.setText(lcstate);

		Element ybjeElement = xmlinfoElement.addElement("isgczcb");
		ybjeElement.setText(isgczcb);

		xmlinfoElement.addElement("fsqt");
		xmlinfoElement.addElement("fsxbsghtno");
		xmlinfoElement.addElement("fsxbsgdw");
		xmlinfoElement.addElement("fsxbgchtzj");
		xmlinfoElement.addElement("fsxbjldw");
		xmlinfoElement.addElement("fsxbjlgcs");
		xmlinfoElement.addElement("fsxbsghtgs");

		return document.asXML();

	}

	public String createContractXML(String fsContractName,
			String fscreateDateTime, String fsksDateTime, String fsjsDateTime,
			String gysncpk, String fsSqDeptId, String user, String fsHtbh,
			String httype, String orgbh, List<Map<String, String>> cljgList) {
		Document document = DocumentHelper.createDocument();
		Element xmlInfoElement = document.addElement("xmlInfo");

		Element fsContractNameElement = xmlInfoElement
				.addElement("fsContractName");
		fsContractNameElement.addText(fsContractName);

		Element fscreateDateTimeElement = xmlInfoElement
				.addElement("fscreateDateTime");
		fscreateDateTimeElement.addText(fscreateDateTime);

		Element fsksDateTimeElement = xmlInfoElement.addElement("fsksDateTime");
		fsksDateTimeElement.addText(fsksDateTime);

		Element fsjsDateTimeElement = xmlInfoElement.addElement("fsjsDateTime");
		fsjsDateTimeElement.addText(fsjsDateTime);

		Element gysncpkElement = xmlInfoElement.addElement("gysncpk");
		gysncpkElement.addText(gysncpk);

		Element fsSqDeptIdElement = xmlInfoElement.addElement("fsSqDeptId");
		fsSqDeptIdElement.addText(fsSqDeptId);

		Element userElement = xmlInfoElement.addElement("user");
		userElement.addText(user);

		Element fsHtbhElement = xmlInfoElement.addElement("fsHtbh");
		fsHtbhElement.addText(fsHtbh);

		Element httypeElement = xmlInfoElement.addElement("httype");
		httypeElement.addText(httype);

		Element orgbhElement = xmlInfoElement.addElement("orgbh");
		orgbhElement.addText(orgbh);

		Element fslistElement = xmlInfoElement.addElement("fslist");

		for (int i = 0; i < cljgList.size(); i++) {
			Map<String, String> attrMap = cljgList.get(i);

			Element cljgElement = fslistElement.addElement("cljg");
			cljgElement.addAttribute("fsZbId", attrMap.get("fsZbId"));
			cljgElement.addAttribute("ffCpNum", attrMap.get("ffCpNum"));
			cljgElement.addAttribute("fsdj", attrMap.get("fsdj"));
			cljgElement.addAttribute("fsprice", attrMap.get("fsprice"));
			cljgElement.addAttribute("fsshuilv", attrMap.get("fsshuilv"));
		}

		return document.asXML();
	}

}
