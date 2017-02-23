package com.ligootech.webdtu.service.sim;

import com.ligootech.webdtu.service.account.ShiroUser;
import com.ligootech.webdtu.util.EasyUiUtil;

/**
 * Created by wly on 2015/10/15 14:34.
 */
public interface SimManager {

    /**
     * 获取SIM列表信息,分页
     * @param rows 每页数据量
     * @param page 开始页码位置
     * @param sort 排序字段
     * @param order 升降序标识 DESC asc
     * @param keywords 搜索关键字
     * @return
     */
    public EasyUiUtil.PageForData findSIMList(int rows, int page, String sort, String order, String keywords);

    /**
     * 删除
     * @param ids
     * @param optUser
     * @return
     */
    public int delSimCard(String ids, ShiroUser optUser);

    /**
     * 导入
     * @param fileName
     * @param optUser
     * @return
     */
    public int saveSimCard(String fileName, ShiroUser optUser)  throws Exception;

}
