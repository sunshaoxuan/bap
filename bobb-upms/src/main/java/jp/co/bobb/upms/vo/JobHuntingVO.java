package jp.co.bobb.upms.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * describe:
 *
 * @author Parker Sun
 * @date 2019/04/23
 */
@ApiModel(value = "情報", description = "情報信息")
@Data
public class JobHuntingVO implements Serializable {
    private static final long serialVersionUID = 8639980504285419013L;
    @ApiModelProperty(value = "职位", name = "title", example = "FX")
    private String title;
    @ApiModelProperty(value = "姓名", name = "name", example = "FX")
    private String name;
    @ApiModelProperty(value = "片假名", name = "nameJP", example = "FX")
    private String nameJP;
    @ApiModelProperty(value = "性别", name = "sex", example = "FX")
    private String sex;
    @ApiModelProperty(value = "联系地址", name = "address", example = "FX")
    private String address;
    @ApiModelProperty(value = "手机 ", name = "phone", example = "FX")
    private String phone;
    @ApiModelProperty(value = "邮箱", name = "mail", example = "FX")
    private String mail;
    @ApiModelProperty(value = "联系时间段", name = "lineTime", example = "FX")
    private String lineTime;
}
