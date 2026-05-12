package com.ims.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName("department")
public class Department {

    @TableId(value = "dept_id", type = IdType.INPUT)
    private String deptId;

    private String deptName;

    private String address;

    private String phoneCode;
}
