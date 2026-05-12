package com.ims.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.ims.entity.Department;

import java.util.List;

public interface DepartmentService extends IService<Department> {

    List<Department> listDepartments();
}
