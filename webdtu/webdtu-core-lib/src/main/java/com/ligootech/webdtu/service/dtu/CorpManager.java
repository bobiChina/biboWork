package com.ligootech.webdtu.service.dtu;

import org.appdot.service.GenericManager;

import com.ligootech.webdtu.entity.core.Corp;

import java.util.List;

public interface CorpManager  extends GenericManager<Corp, Long>{

    /**
     * 客户列表
     * @return
     */
    public List<Object[]> getCorpList();

    /**
     * 获取无账号的公司信息
     * @return
     */
    public List<Object[]> getCorpListNoUser();

    /**
     * 获取客户登录账号
     * @param corpId
     * @return
     */
    public List<Object[]> getCorpAccount(Long corpId);

    /**
     * 保存信息
     * @param corp
     */
    public void saveCorp(Corp corp, Long optUserId);

    /**
     * 删除公司信息
     * @param corpId
     * @param optUserId
     */
    public void delCorp(Long corpId, Long optUserId);

    /**
     * 验证客户名称是否重复
     * @param id
     * param type
     * @return
     */
    public int checkCorpName(String corpName, Long id);

}
