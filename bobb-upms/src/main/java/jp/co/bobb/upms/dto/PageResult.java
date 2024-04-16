package jp.co.bobb.upms.dto;

import com.google.common.collect.Lists;
import lombok.Data;

import java.util.List;

/**
 * @ClassName PageResult
 * @Description 分页返回数据
 * @Author wenyang
 * @Date 2019/4/3 10:37
 **/
@Data
public class PageResult<T> {

    /**
     * 待返回的有效信息
     */
    private List<T> info = Lists.newArrayList();

    /**
     * 总页数
     */
    private Integer pageCount;

    /**
     * 每页数据条数
     */
    private Integer pageSize;

    /**
     * 页码
     */
    private Integer pageNum;

    /**
     * 总数
     */
    private Integer total;

}
