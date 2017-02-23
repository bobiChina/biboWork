package com.ligootech.webdtu.entity.data;

import com.ligootech.webdtu.entity.account.Task;
import org.springside.modules.test.data.RandomData;


/**
 * Task相关实体测试数据生成.
 * 
 * @author liand
 */
public class TaskData {

	public static Task randomTask() {
		Task task = new Task();
		task.setTitle(randomTitle());
		return task;
	}

	public static String randomTitle() {
		return RandomData.randomName("Task");
	}

}
