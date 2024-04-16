package jp.co.bobb.common.vo;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * describe:
 *
 * @author Parker Sun
 * @date 2019/04/17
 */
@Data
public class PageResult<T> implements Serializable {
    private static final long serialVersionUID = -7966556456559989166L;
    private int total;
    private int limit;
    private int currentPage;
    private List<T> rows;
}
