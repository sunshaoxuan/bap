package jp.co.bobb.upms.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * describe:質問
 *
 * @author Parker Sun
 * @date 2019/04/23
 */
@ApiModel(value = "質問对象", description = "質問信息")
@Data
public class InquiryVO implements Serializable {
    private static final long serialVersionUID = 8232042801744465431L;
    @ApiModelProperty(value = "邮箱代码", name = "mailCode", example = "FX")
    private String mailCode;
    @ApiModelProperty(value = "内容", name = "content", example = "FX")
    private String content;
    @ApiModelProperty(value = "姓名", name = "name", example = "FX")
    private String name;
    @ApiModelProperty(value = "日文姓名", name = "nameJP", example = "FX")
    private String nameJP;
    @ApiModelProperty(value = "邮箱", name = "email", example = "FX")
    private String email;
    @ApiModelProperty(value = "电话", name = "phone", example = "FX")
    private String phone;
}
