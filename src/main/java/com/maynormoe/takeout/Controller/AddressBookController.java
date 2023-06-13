package com.maynormoe.takeout.Controller;

import com.alibaba.fastjson.serializer.AdderSerializer;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.maynormoe.takeout.common.Results;
import com.maynormoe.takeout.entity.AddressBook;
import com.maynormoe.takeout.entity.User;
import com.maynormoe.takeout.service.AddressBookService;
import com.maynormoe.takeout.utils.BaseContext;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.Arrays;
import java.util.List;

/**
 * @author Maynormoe
 */

@Slf4j
@RestController
@RequestMapping("/addressBook")
public class AddressBookController {

    @Resource
    private AddressBookService addressBookService;

    /**
     * 查询用户地址薄
     *
     * @param addressBook 地址薄
     * @return Results<List < AddressBook>>
     */
    @GetMapping("/list")
    public Results<List<AddressBook>> list(AddressBook addressBook) {
        // 获取用户当前id
        addressBook.setUserId(BaseContext.getCurrentId());
        // 条件构造器，根据用户id查询当前用户地址信息
        LambdaQueryWrapper<AddressBook> addressBookLambdaQueryWrapper = new LambdaQueryWrapper<AddressBook>();
        addressBookLambdaQueryWrapper.eq(addressBook.getUserId() != null, AddressBook::getUserId, addressBook.getUserId());
        addressBookLambdaQueryWrapper.orderByDesc(AddressBook::getUpdateTime);

        List<AddressBook> list = addressBookService.list(addressBookLambdaQueryWrapper);
        return Results.success(list);
    }

    /**
     * 新增地址
     *
     * @param addressBook 地址
     * @return Results<AddressBook>
     */
    @PostMapping
    public Results<AddressBook> save(@RequestBody AddressBook addressBook) {
        log.info("新增地址{}", addressBook);
        // 获取用户当前id
        addressBook.setUserId(BaseContext.getCurrentId());

        addressBookService.save(addressBook);

        return Results.success(null);
    }


    /**
     * 设置默认地址
     *
     * @param addressBook 地址
     * @return Results<AddressBook>
     */
    @PutMapping("/default")
    public Results<AddressBook> setDefault(@RequestBody AddressBook addressBook) {
        // 将用户地址状态默认改为0
        LambdaUpdateWrapper<AddressBook> addressBookLambdaUpdateWrapper = new LambdaUpdateWrapper<AddressBook>();
        addressBookLambdaUpdateWrapper.eq(AddressBook::getUserId, BaseContext.getCurrentId());
        addressBookLambdaUpdateWrapper.set(AddressBook::getIsDefault, 0);

        addressBookService.update(addressBookLambdaUpdateWrapper);

        // 设置用户默认地址
        addressBook.setIsDefault(1);

        LambdaQueryWrapper<AddressBook> addressBookLambdaQueryWrapper = new LambdaQueryWrapper<AddressBook>();

        addressBookService.updateById(addressBook);

        return Results.success(addressBook);
    }

    /**
     * 查询默认地址
     *
     * @return Results<AddressBook>
     */
    @GetMapping("/default")
    public Results<AddressBook> getDefault() {
        LambdaQueryWrapper<AddressBook> addressBookLambdaQueryWrapper = new LambdaQueryWrapper<>();
        addressBookLambdaQueryWrapper.eq(AddressBook::getUserId, BaseContext.getCurrentId());
        addressBookLambdaQueryWrapper.eq(AddressBook::getIsDefault, 1);

        AddressBook addressBook = addressBookService.getOne(addressBookLambdaQueryWrapper);
        if (addressBook == null) {
            return Results.error("没有找到");
        }
        return Results.success(addressBook);
    }

    /**
     * 根据id查询地址信息
     *
     * @return Results<AddressBook>
     */
    @GetMapping("/{id}")
    public Results<AddressBook> getById(@PathVariable Long id) {
        AddressBook addressBook = addressBookService.getById(id);
        return Results.success(addressBook);
    }

    /**
     * 根据id删除地址信息
     *
     * @param id 地址id
     * @return Results<AddressBook>
     */
    @DeleteMapping
    public Results<AddressBook> delete(@RequestParam("ids") Long id) {
        log.info("删除id为{}的地址信息", id);
        LambdaQueryWrapper<AddressBook> addressBookLambdaQueryWrapper = new LambdaQueryWrapper<>();
        addressBookLambdaQueryWrapper.eq(AddressBook::getUserId, BaseContext.getCurrentId())
                .eq(AddressBook::getId, id);

        addressBookService.remove(addressBookLambdaQueryWrapper);
        return Results.success(null);
    }

    /**
     * @return Results<AddressBook>
     */
    @PutMapping
    public Results<AddressBook> delete(@RequestBody AddressBook addressBook) {
        log.info("id为{}的用户修改地址信息{}", BaseContext.getCurrentId(), addressBook);
        LambdaQueryWrapper<AddressBook> addressBookLambdaQueryWrapper = new LambdaQueryWrapper<AddressBook>();
        addressBookLambdaQueryWrapper.eq(AddressBook::getUserId, BaseContext.getCurrentId())
                .eq(AddressBook::getId, addressBook.getId());
        addressBookService.update(addressBook, addressBookLambdaQueryWrapper);
        return Results.success(null);
    }
}
