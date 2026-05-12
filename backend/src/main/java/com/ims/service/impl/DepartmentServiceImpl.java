package com.ims.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ims.entity.Department;
import com.ims.mapper.DepartmentMapper;
import com.ims.service.DepartmentService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DepartmentServiceImpl extends ServiceImpl<DepartmentMapper, Department> implements DepartmentService {

    @Override
    public List<Department> listDepartments() {
        return list(new LambdaQueryWrapper<Department>()
                .orderByAsc(Department::getDeptId));
    }
}
