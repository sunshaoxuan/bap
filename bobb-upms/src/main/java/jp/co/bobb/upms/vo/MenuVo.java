package jp.co.bobb.upms.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.List;

/**
 * describe:
 *
 * @author Parker Sun
 * @date 2019/03/19
 */
@Data
@ApiModel(value = "メニューの相手", description = "メニゆーの情報")
public class MenuVo implements Serializable {
    @ApiModelProperty(value = "主键ID", name = "id", example = "123311")
    private String id;
    @NotNull(message = "メニューのコードは必須です")
    @ApiModelProperty(value = "菜单CODE", name = "code", example = "123311", required = true)
    private String code;

    @ApiModelProperty(value = "上级菜单CODE", name = "pCode", example = "123311")
    private String pCode;
    @ApiModelProperty(value = "上级菜单ID", name = "pId", example = "123311")
    private String pId;

    @ApiModelProperty(value = "菜单名称", name = "name", example = "123311", required = true)
    @NotNull(message = "メニューの名前は必須です")
    private String name;

    @ApiModelProperty(value = "是否是菜单有可能是目录 0 不显示目录 1 是目录  2 菜单  3 接口", name = "isMenu", example = "123311", required = true)
    @NotNull(message = "メニューの属性は必須です")
    private Integer isMenu;

    @ApiModelProperty(value = "菜单地址", name = "url", example = "123311")
    private String url;

    @ApiModelProperty(value = "页面路由", name = "pageUrl", example = "123311")
    private String pageUrl;

    @ApiModelProperty(value = "所属系统 tanimachikun participator", name = "system", example = "123311")
    private String system;

    @ApiModelProperty(value = "层级", name = "level", example = "123311", required = true)
    @NotNull(message = "メニューのレベルは必須です")
    private Integer level;

    @ApiModelProperty(value = "排序", name = "sort", example = "123311")
    private Integer sort;

    @NotNull(message = "メニューの州は必須です")
    @ApiModelProperty(value = "状态 1 有效 0 无效", name = "status", example = "123311", required = true)
    private Integer status;

    @NotNull(message = "所属域名")
    @ApiModelProperty(value = "所属域名", name = "domain", example = "briconbric.com", required = true)
    private String domain;

    @NotNull(message = "菜单图标")
    @ApiModelProperty(value = "菜单图标", name = "icon", example = "", required = true)
    private String icon;


    private List<MenuVo> children;
}
