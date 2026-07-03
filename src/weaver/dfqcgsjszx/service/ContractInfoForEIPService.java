package weaver.dfqcgsjszx.service;

/**
 * 合同信息接口（供给EIP系统调用)
 *
 * @author Alex.Du
 */
public interface ContractInfoForEIPService {
    /**
     * 查询合同信息
     *
     * @param modifyDate 更新日期
     * @return
     */
    public String searchContractInfo(String modifyDate);

    /**
     * 传递合同支付信息
     *
     * @param htbh 合同编号
     * @param fksqr 付款申请人
     * @param fkrq 付款日期
     * @param fkje 付款金额
     * @param fksy 付款事由
     * @param lcmc 流程名称
     * @param lcdh 流程单号
     * @return
     */
    public String toContractPayInfo(String htbh, String fksqr, String fkrq, float fkje, String fksy, String lcmc, String lcdh);
}
