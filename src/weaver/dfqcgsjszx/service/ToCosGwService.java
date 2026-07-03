package weaver.dfqcgsjszx.service;
/**
 * 获取cos公文信息
 * @author peixuan
 *
 */
public interface ToCosGwService {
	/**
	 * 获取公文基础信息
	 * @param message  公文基础信息字符串（json格式）
	 * @return
	 */
	public String toCosGwMessage(String message);
	
	/**
	 * 获取公文附件
	 * @param  message  公文附件字符串（json格式）
	 * @return
	 */
	public String toCosGwFj(String message);

}
