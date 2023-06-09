package com.maynormoe.takeout.common;

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import com.maynormoe.takeout.utils.BaseContext;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.reflection.MetaObject;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

/**
 * @author Maynormoe
 */

@Slf4j
@Component
public class MyMetaObjectHandler implements MetaObjectHandler {
    /**
     * 插入自动填充
     * @param metaObject 元信息对象
     */
    @Override
    public void insertFill(MetaObject metaObject) {
        log.info("公共字段填充[insert]");
        log.info(metaObject.toString());
        metaObject.setValue("createTime", LocalDateTime.now());
        metaObject.setValue("updateTime", LocalDateTime.now());
        metaObject.setValue("createUser", BaseContext.getCurrentId());
        metaObject.setValue("updateUser", BaseContext.getCurrentId());
    }

    /**
     * 更新时填充
     * @param metaObject 元信息对象
     */
    @Override
    public void updateFill(MetaObject metaObject) {
        log.info("公共字段填充[update]");
        log.info(metaObject.toString());
        metaObject.setValue("updateTime", LocalDateTime.now());
        metaObject.setValue("updateUser", BaseContext.getCurrentId());
    }
}
