package com.ligootech.webdtu.repository;

/**
 * Created by wly on 2015/9/22 16:05.
 */
public interface OptLogDao {
    /**
     * 日志记录
     * @param userId
     * @param opt_type
     * @param content
     * @return
     */
    public int optLog(Long userId, String opt_type, String content);
}
