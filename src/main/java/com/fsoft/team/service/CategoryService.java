package com.fsoft.team.service;

import com.fsoft.team.entity.Category;

import java.util.List;

public interface CategoryService {

    public List<Category> getAllCategory();

    public Category getCateById(Long id);

    public Category addCategory(String nameCate);

    Category save(Category entity);

    Category getbyId(long l);

    List<Category> findAll();

    void deleteById(long l);
}
