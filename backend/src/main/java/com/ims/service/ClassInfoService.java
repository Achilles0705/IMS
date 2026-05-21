package com.ims.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.ims.dto.ClassCreateRequest;
import com.ims.dto.ClassUpdateRequest;
import com.ims.entity.ClassInfo;

import java.util.List;

public interface ClassInfoService extends IService<ClassInfo> {

    List<ClassInfo> listClasses(String semester, String courseId, String staffId);

    ClassInfo createClass(ClassCreateRequest request);

    ClassInfo updateClass(String semester, String courseId, String staffId, ClassUpdateRequest request);
}
