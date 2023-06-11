package com.maynormoe.takeout.common;

import lombok.extern.slf4j.Slf4j;
import net.bytebuddy.dynamic.scaffold.inline.MethodNameTransformer;
import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.nio.file.Path;
import java.util.UUID;

/**
 * @author Maynormoe
 */

@Slf4j
@RestController
@RequestMapping("/common")
public class CommonController {

    @Value("${takeout.path}")
    private String basePath;

    /**
     * 文件上传
     *
     * @param file 媒体文件
     * @return Results<String>
     */
    @PostMapping("/upload")
    public Results<String> upload(MultipartFile file) {
        log.info("上传文件{}", file.getOriginalFilename());

        // 获取原始文件名
        String originalFilename = file.getOriginalFilename();
        String suffix = null;
        if (originalFilename != null) {
            suffix = originalFilename.substring(originalFilename.lastIndexOf("."));
        }
        String fileName = UUID.randomUUID().toString() + suffix;
        // 创建一个目录对象
        File dir = new File(basePath);
        if (!dir.exists()) {
            //目录不存在，创建目录
            dir.mkdirs();
        }
        try {
            file.transferTo(new File(basePath + fileName));
        } catch (Exception exception) {
            exception.printStackTrace();
        }

        return Results.success(fileName);
    }

    /**
     * 下载文件
     *
     * @param name 文件名称
     * @return Results<String>
     */
    @GetMapping("/download")
    public Results<String> download(String name, HttpServletResponse response) {

        try {
            // 通过输入流读取文件内容
            FileInputStream fileInputStream = new FileInputStream(new File(basePath + name));
            // 输出流返回给客户端
            ServletOutputStream servletOutputStream = response.getOutputStream();

            //  使用 IOUtils 将文件内容直接复制到输出流：
            response.setHeader("Content-Disposition", "inline; filename=" + name);
            IOUtils.copy(fileInputStream, servletOutputStream);
            servletOutputStream.close();
            fileInputStream.close();
        } catch (Exception exception) {
            exception.printStackTrace();
        }
        return Results.success(null);
    }
}
