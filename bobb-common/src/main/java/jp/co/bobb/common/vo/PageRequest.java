package jp.co.bobb.common.vo;

import lombok.Data;

import java.io.Serializable;

/**
 * describe:
 *
 * @author Parker Sun
 * @date 2019/04/17
 */
@Data
public class PageRequest implements Serializable {
    private Integer startPage = 1;
    private Integer limit = 10;
}
