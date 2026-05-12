package com.ims.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.ims.entity.ClassInfo;

import java.util.List;

public interface ClassInfoService extends IService<ClassInfo> {

    List<ClassInfo> listClasses(String semester, String courseId, String staffId);
}
