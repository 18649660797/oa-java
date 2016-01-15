/**
 * Copyright (c) 2016 云智盛世
 * Created with PermissionServiceTests.
 */
package top.gabin.oa.web.service;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import top.gabin.oa.BaseTest;
import top.gabin.oa.web.dto.SimpleTreeDTO;
import top.gabin.oa.web.entity.Permission;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author linjiabin  on  16/1/9
 */
public class PermissionServiceTests extends BaseTest {
    protected static final Logger logger = LoggerFactory.getLogger(PermissionServiceTests.class);
    @Resource(name = "permissionService")
    private PermissionService permissionService;

    @Test
    public void findAll() {
        List<Permission> all = permissionService.findAll();
        assert !all.isEmpty();
    }

    @Test
    public void getPermissionTreeData() {
        List<SimpleTreeDTO> treeData = permissionService.getPermissionTreeData(null);
        assert !treeData.isEmpty();
    }


}
