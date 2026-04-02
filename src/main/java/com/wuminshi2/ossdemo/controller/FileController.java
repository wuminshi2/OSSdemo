package com.wuminshi2.ossdemo.controller;

import com.wuminshi2.ossdemo.Mapper.FileMapper;
import com.wuminshi2.ossdemo.entity.File;
import com.wuminshi2.ossdemo.utils.AliOssUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

import static com.baomidou.mybatisplus.extension.toolkit.Db.save;


@RestController
public class FileController {
    @Autowired
    private AliOssUtil aliOssUtil;
    @Autowired
    private FileMapper fileMapper;

    @PostMapping("/upload")
    public File upload(MultipartFile multipartfile){
        String originalFilename = null;
        String url = null;
        try {
            originalFilename = multipartfile.getOriginalFilename();
            String extension = originalFilename.substring(originalFilename.lastIndexOf("."));
            String objectName = "images/" + UUID.randomUUID().toString() + extension;
            url = aliOssUtil.upload(multipartfile.getBytes(), objectName);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        File file = new File();
        file.setFileName(originalFilename);
        file.setOssUrl(url);
        save(file);
        return file;
    }

    @GetMapping("/list")
    public List<File> list(){
        return fileMapper.selectList(null);
    }
}
