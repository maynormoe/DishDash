package com.maynormoe.takeout.Controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.maynormoe.takeout.common.Results;
import com.maynormoe.takeout.entity.Category;
import com.maynormoe.takeout.service.CategoryService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * @author Maynormoe
 */

@Slf4j
@RestController
@RequestMapping("/category")
public class CategoryController {

    @Resource
    private CategoryService categoryService;

    /**
     * 新增分类
     *
     * @param category 分类
     * @return Results<Category>
     */
    @PostMapping
    public Results<Category> add(@RequestBody Category category) {
        log.info("新增菜品分类{}", category.toString());
        categoryService.save(category);
        log.info("新增分类成功");
        return Results.success(null);
    }

    /**
     * 添加分页查询
     *
     * @return Page<Category>
     */
    @GetMapping("/page")
    public Results<Page<Category>> page(Integer page, Integer pageSize) {
        // 分页构造器
        Page<Category> pageInfo = new Page<Category>(page, pageSize);
        // 排序构造器
        LambdaQueryWrapper<Category> categoryLambdaQueryWrapper = new LambdaQueryWrapper<>();
        categoryLambdaQueryWrapper.orderByAsc(Category::getSort);
        // 调用mapper
        categoryService.page(pageInfo, categoryLambdaQueryWrapper);

        return Results.success(pageInfo);
    }

    @DeleteMapping
    public Results<Category> delete(Long ids) throws Exception {
        log.info("删除id为{}的数据", ids);
        categoryService.remove(ids);
        log.info("删除成功");
        return Results.success(null);
    }


    @PutMapping
    public Results<Category> update(@RequestBody Category category) {
        log.info("修改id为{}的分类数据,修改为{}", category.getId().toString(), category);
        categoryService.updateById(category);
        log.info("修改成功");
        return Results.success(null);
    }
}
