package com.we.weblog.web.controller.admin;

import com.vue.adminlte4j.model.UIModel;
import com.we.weblog.domain.Select;
import com.we.weblog.service.ContextService;
import com.we.weblog.service.TagService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import java.util.*;

/**
 * 分类标签控制器
 * created by tangwei at 2018/3/19
 */
@Controller
@RequestMapping("/admin/category")
public class CategoryController {

    private ContextService contextService;
    private TagService tagService;

    @Autowired
    public CategoryController(ContextService contextService,TagService tagService){
        this.contextService = contextService;
        this.tagService = tagService;
    }

    /**
     *  添加博客分类显示
     * @return
     */
    @GetMapping("/index")
    @ResponseBody
    public List<Select> getAllKindCategories(){
//        {code:'1' ,  label:'男' ,checked:false},
        List<Select> lists = tagService.getCategories();
        return  lists;
    }
    /**
     * 删除类别
     */
    @GetMapping("/delete/{name}")
    @ResponseBody
    public UIModel deleteCategory(@PathVariable("name") String categoryName){

          if (categoryName.equals("")) {
            return UIModel.fail().msg("删除的类别为空");
          }
          int result = contextService.deleteCatories(categoryName);
          tagService.deleteMetas(categoryName);
          if(result >= 0) {
              return UIModel.success().msg("删除成功");
          } else {
              return UIModel.fail().msg("删除失败");
          }
    }

    /**
     * 分类标签管理页面
     */
    @GetMapping("/manage")
    @ResponseBody
    public Map<String,Object> manageCategoryAndTag() {
        Map<String,Object> maps  = new HashMap<>();
        List<String> cates = tagService.getMates();
        List<String> tags = contextService.getAllKindTags();

        maps.put("categories",cates);
        maps.put("tags",tags);
        return maps;
    }

    /**
     * 保存新分类
     * @return
     */
    @GetMapping("/save/{name}")
    @ResponseBody
    public UIModel newCategory(@PathVariable("name")  String name) {
        //这里全是空格 全是数字 null 都要检查
        if (name.equals("")) {
            return UIModel.fail().msg("请输入类别");
        } else if(name.length()> 25) {
            return  UIModel.fail().msg("您输入的类别过长");
        }

        List<String> tagName = tagService.getMates();
        if (tagName.contains(name)) {
            return UIModel.fail().msg("该分类已存在");
        }
        int result = tagService.addCategory(name);

        if (result > 0)
            return UIModel.success().msg("添加成功");
        else
            return UIModel.fail().msg("添加失败");

    }

}
