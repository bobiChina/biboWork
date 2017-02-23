package com.ligootech.webdtu.web;

import com.ligootech.webdtu.entity.account.Role;
import com.ligootech.webdtu.entity.core.clientForm.SystemJoint;
import com.ligootech.webdtu.entity.core.clientForm.SystemJointInfo;
import com.ligootech.webdtu.service.account.AccountManager;
import com.ligootech.webdtu.util.ExcelReaderUtil;
import com.ligootech.webdtu.web.account.RoleDetailController;
import net.sf.json.JSONObject;
import org.appdot.test.spring.BaseControllerTestCase;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.mvc.support.RedirectAttributesModelMap;
import org.springside.modules.test.security.shiro.ShiroTestUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 
 */

/**
 * @author Lian
 *
 */
public class RoleDetailControllerTest extends BaseControllerTestCase {

	@Autowired
	private RoleDetailController c = null;

	@Autowired
	private AccountManager accountManager;

	@Before
	public void setUp() {
		ShiroTestUtils.mockSubject("foo");
	}

	@After
	public void tearDown() {
		ShiroTestUtils.clearSubject();
	}

	@Test
	public void testSaveRole() {
		String storeName = "asdfghjkl.xml";
		storeName = storeName.substring(storeName.lastIndexOf(".")+1);

		System.out.println("截取测试=" + storeName);




		logger.debug("test save role");
		Role role = applicationContext.getBean(AccountManager.class).getRole(-2L);
		logger.debug("old role is [" + role + "]");
		role.setName("changedByMe");
		c.save(role, new RedirectAttributesModelMap());

		Role newRole = accountManager.getRole(-2L);
		logger.debug("updated role == [" + newRole + "]");
		Assert.assertEquals("newRole name is changedByMe", "changedByMe", newRole.getName());

		try {
			List<List<String>> rsList = ExcelReaderUtil.readExcel("E:\\ceshi.xls");
			System.out.println("SIZE=" + rsList.size());
			if (rsList != null && rsList.size() > 0) {
				for (int i = 0; i < rsList.size(); i++) {
					System.out.println("--------------------------------------------------");
					for (int j = 0; j < rsList.get(i).size(); j++) {
						System.out.print(rsList.get(i).get(j) + "==");
					}
					System.out.println();
				}
			}
			rsList.clear();

		} catch (Exception e) {
			e.printStackTrace();
		}

		//JSON对象转换
		SystemJointInfo sji2 = new SystemJointInfo();
		sji2.setUuid("123456780");
		sji2.setSingle_result("success");
		sji2.setSn("123456780");
		sji2.setType("BM51A");
		sji2.setName("BC52B");

		Map<String, String> detail2 = new HashMap<String, String>();
		detail2.put("firm_version", "uncheck");
		detail2.put("cfg", "success");
		detail2.put("csf", "uncheck");
		sji2.setDetail(detail2);

		SystemJointInfo sji1 = new SystemJointInfo();
		sji1.setUuid("123456789");
		sji1.setSingle_result("success");
		sji1.setSn("123456789");
		sji1.setType("bcu");
		sji1.setName("BC52B");

		Map<String, String> detail1 = new HashMap<String, String>();
		detail1.put("firm_version", "success");
		detail1.put("cfg", "success");
		detail1.put("csf", "success");
		sji1.setDetail(detail1);

		SystemJoint sj = new SystemJoint();
		sj.setUser_id("4014");
		sj.setTotal_result("ok");

		List<SystemJointInfo> lists = new ArrayList<SystemJointInfo>();
		lists.add(sji1);
		lists.add(sji2);

		sj.setLists(lists);

		JSONObject str = JSONObject.fromObject(sj);

		System.out.println("json=" + str.toString());


		String jsonStr = "{\"lists\":[{\"detail\":{\"csf\":\"success\",\"cfg\":\"success\",\"firm_version\":\"success\"},\"name\":\"BC52B\",\"single_result\":\"success\",\"sn\":\"123456789\",\"type\":\"bcu\",\"uuid\":\"123456789\"},{\"detail\":{\"csf\":\"uncheck\",\"cfg\":\"success\",\"firm_version\":\"uncheck\"},\"name\":\"BC52B\",\"single_result\":\"success\",\"sn\":\"123456780\",\"type\":\"BM51A\",\"uuid\":\"123456780\"}],\"total_result\":\"ok\",\"user_id\":\"4014\"}";

		JSONObject jsonObject = JSONObject.fromObject(jsonStr);
		//先定义好内部的对象
		Map<String, Class> classMap = new HashMap<String, Class>();
		classMap.put("lists", SystemJointInfo.class);
		classMap.put("detail", Map.class);

		SystemJoint obj;
		obj = (SystemJoint) JSONObject.toBean(jsonObject, SystemJoint.class, classMap);

		System.out.println("-------------------------");
		System.out.println(obj.getUser_id());
		System.out.println(obj.getTotal_result());
		System.out.println("-------------------------");
		SystemJointInfo sl = obj.getLists().get(0);
		System.out.println(obj.getLists().size());
		System.out.println(sl.getName());
		System.out.println(sl.getDetail());
		System.out.println(sl.getDetail().get("csf"));

	}
}
