/**
 * 
 */
package com.ligootech.webdtu.service.task;

import java.util.Map;

import com.ligootech.webdtu.entity.account.Task;
import org.appdot.service.GenericManager;
import org.springframework.data.domain.Page;

/**
 * @author Lian
 *
 */
public interface TaskManager extends GenericManager<Task, Long> {

	public abstract Page<Task> getUserTask(Long userId, Map<String, Object> searchParams, int pageNumber, int pageSize, String sortType);

}
