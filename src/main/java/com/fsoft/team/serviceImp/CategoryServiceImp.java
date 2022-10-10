package com.fsoft.team.serviceImp;

import com.fsoft.team.entity.Category;
import com.fsoft.team.repository.CategoryRepository;
import com.fsoft.team.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CategoryServiceImp implements CategoryService {

    @Autowired
    CategoryRepository categoryRepository;

    @Override
    public List<Category> getAllCategory() {
        return categoryRepository.findAll();
    }

    @Override
    public Category getCateById(Long id) {
        return categoryRepository.findById(id).get();
    }

    @Override
    public Category addCategory(String nameCategory) {
        Category category = new Category();
        category.setCategoryName(nameCategory.substring(0, nameCategory.length() - 16));
        return categoryRepository.save(category);
    }

    @Override
    public Category save(Category entity) {
        return categoryRepository.save(entity);
    }

    @Override
    public Category getbyId(long l) {
        return categoryRepository.findById(l).get();
    }

    @Override
    public List<Category> findAll() {
        return (List<Category>) categoryRepository.findAll();
    }

    @Override
    public void deleteById(long l) {
        categoryRepository.deleteById(l);
    }
}
