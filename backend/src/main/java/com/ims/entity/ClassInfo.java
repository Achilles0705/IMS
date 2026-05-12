package com.ims.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName("class")
public class ClassInfo {

    private String semester;

    private String courseId;

    private String staffId;

    private String classTime;
}
