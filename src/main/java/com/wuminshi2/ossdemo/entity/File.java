package com.wuminshi2.ossdemo.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

@Data
public class File {
    @TableId(type = IdType.AUTO)
    private String id;
    private String fileName;
    private String ossUrl;
}
