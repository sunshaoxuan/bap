package jp.co.bobb.upms.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * describe:
 *
 * @author Parker Sun
 * @date 2019/05/15
 */
@ApiModel(value = "認可对象", description = "認可信息")
@Data
public class WarrantVO implements Serializable {
    @ApiModelProperty(value = "roleId", name = "roleId", example = "123311")
    private Long roleId;

    @ApiModelProperty(value = "menuId", name = "menuId", example = "123311")
    private List<String> menuId;

    @ApiModelProperty(value = "roleCode", name = "roleCode", example = "123311")
    private String roleCode;

    @ApiModelProperty(value = "userId", name = "userId", example = "123311")
    private Integer userId;

}
