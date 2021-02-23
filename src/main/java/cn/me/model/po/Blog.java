package cn.me.model.po;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import java.time.LocalDateTime;
import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * 博客表
 *
 * @author Giskard
 * @since 2021-02-18
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("t_blog")
public class Blog implements Serializable
{
    private static final long serialVersionUID = 1L;

    /**
     * 博客主键ID
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 用户ID
     */
    private Integer userId;

    /**
     * 博客标题
     */
    private String title;

    /**
     * 博客描述
     */
    private String description;

    /**
     * 博客内容
     */
    private String content;

    /**
     * 状态：0-停用，1-正常
     */
    private String status;

    /**
     * 创建时间，由于IPage<Blog> pageResult只能是Blog类型，不得已在Blog加了JsonFormat
     */
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone="GMT+8")
    private LocalDateTime createTime;
}
