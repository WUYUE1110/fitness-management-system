package com.work.reservationsystem.service;

import com.work.reservationsystem.constant.PageConstant;
import com.work.reservationsystem.dao.EquipmentCategoryDao;
import com.work.reservationsystem.entity.EquipmentCategory;

import java.util.List;

public class EquipmentCategoryService {
    private final EquipmentCategoryDao equipmentCategoryDao = new EquipmentCategoryDao();

    public int count() {
        return equipmentCategoryDao.count();
    }

    public List<EquipmentCategory> findPage(int pageNum, int pageSize) {
        if (pageNum < 1) {
            pageNum = PageConstant.DEFAULT_PAGE_NUM;
        }
        if (pageSize < 1) {
            pageSize = PageConstant.DEFAULT_PAGE_SIZE;
        }
        
        int offset = (pageNum - 1) * pageSize;
        return equipmentCategoryDao.findPage(offset, pageSize);
    }

    public void add(EquipmentCategory category) {
        equipmentCategoryDao.insert(category);
    }

    public EquipmentCategory findById(Integer id) {
        return equipmentCategoryDao.findById(id);
    }

    public void update(EquipmentCategory category) {
        equipmentCategoryDao.update(category);
    }

    public void delete(Integer id) {
        equipmentCategoryDao.delete(id);
    }

    public List<EquipmentCategory> findAll() {
        return equipmentCategoryDao.findAll();
    }
} 