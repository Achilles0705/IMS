package com.ims.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ims.entity.ClassInfo;
import com.ims.mapper.ClassInfoMapper;
import com.ims.service.ClassInfoService;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;

@Service
public class ClassInfoServiceImpl extends ServiceImpl<ClassInfoMapper, ClassInfo> implements ClassInfoService {

    @Override
    public List<ClassInfo> listClasses(String semester, String courseId, String staffId) {
        LambdaQueryWrapper<ClassInfo> wrapper = new LambdaQueryWrapper<ClassInfo>()
                .eq(StringUtils.hasText(semester), ClassInfo::getSemester, semester)
                .eq(StringUtils.hasText(courseId), ClassInfo::getCourseId, courseId)
                .eq(StringUtils.hasText(staffId), ClassInfo::getStaffId, staffId)
                .orderByDesc(ClassInfo::getSemester)
                .orderByAsc(ClassInfo::getCourseId)
                .orderByAsc(ClassInfo::getStaffId);
        return list(wrapper);
    }
}
