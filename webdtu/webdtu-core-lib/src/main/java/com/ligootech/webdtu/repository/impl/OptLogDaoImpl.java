package com.ligootech.webdtu.repository.impl;

import com.ligootech.webdtu.repository.OptLogDao;
import org.springframework.stereotype.Component;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

/**
 * Created by wly on 2015/9/22 16:09.
 */
@Component
public class OptLogDaoImpl implements OptLogDao {
    @PersistenceContext
    private EntityManager em;

    @Override
    public int optLog(Long userId, String opt_type, String content) {
        StringBuffer sql = new StringBuffer("insert into t_operation_log(opt_userid, opt_type, opt_time, content) values(?, ?, NOW(), ?)");
        Query query = em.createNativeQuery(sql.toString()).setParameter(1, userId).setParameter(2, opt_type).setParameter(3, content);
        int rs = query.executeUpdate();
        /*if (rs > -1){
            sql = new StringBuffer("SELECT MAX(id) FROM vehicle_model");
            Query querySearch = em.createNativeQuery(sql.toString());
            List rsList = querySearch.getResultList();
            if (null != rsList && rsList.size() > 0){
                java.math.BigInteger bi = (java.math.BigInteger) rsList.get(0);
            }
        }*/
        return rs;
    }
}
