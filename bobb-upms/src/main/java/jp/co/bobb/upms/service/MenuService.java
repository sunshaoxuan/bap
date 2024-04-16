package jp.co.bobb.upms.service;

import jp.co.bobb.upms.dto.MenuInfoDTO;
import jp.co.bobb.common.vo.Result;

/**
 * describe:
 *
 * @author Parker Sun
 * @date 2019/03/19
 */
public interface MenuService {
    /**
     * 查询菜单
     *
     * @param pId    上一级菜单id
     * @param userId 当前用户id
     * @param system 所属系统
     * @param domain 所属域名
     * @return
     */
    Result queryMenus(String pId, Integer userId, String system, String domain);

    /**
     * @param menuDTO
     * @return
     */
    Result newMenu(MenuInfoDTO menuDTO);

    /**
     * @param menuDTO
     * @return
     */
    Result updateMenu(MenuInfoDTO menuDTO);

    /**
     * メニューを問い合わせする
     *
     * @param id
     * @return
     */
    Result queryMenu(String id);

    /**
     * メニューを削除する
     *
     * @param id
     * @return
     */
    Result deleteMenu(String id);
}
