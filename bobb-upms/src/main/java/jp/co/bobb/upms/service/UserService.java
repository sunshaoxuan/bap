package jp.co.bobb.upms.service;

import jp.co.bobb.upms.dto.PageDTO;
import jp.co.bobb.upms.entity.MemberInfo;
import jp.co.bobb.upms.entity.RcUser;
import jp.co.bobb.common.util.R;

import javax.servlet.http.HttpServletRequest;

/**
 * @author Parker Sun
 */
public interface UserService {
    RcUser findByUsername(String username);

    R<Boolean> sendSmsCode(String biz, String mobile);

    RcUser findByMobile(String mobile);

    /**
     * 更新rc_user信息
     *
     * @return
     */
    R<Integer> updateUserInfo(RcUser user);

    /**
     * 新增会员
     *
     * @param user
     */
    void insertUser(RcUser user);

    /**
     * 根据member_id查询user
     *
     * @param memberId
     * @return
     */
    RcUser selectUserByMemberId(Integer memberId);

    PageDTO findInsideUser(int pageNum, int pageSize);

    PageDTO findOutsideUser(int pageNum, int pageSize);

    /**
     * 会員をチェックする
     *
     * @param mobile
     * @return
     */
    R<Boolean> queryMemberByMobile(String mobile);

    /**
     * 自動登録
     *
     * @param mobile
     * @param code   区号 86、81 等
     * @return
     */
    RcUser register(String mobile, Short code);

    /**
     * 查询会员级别
     *
     * @param id
     * @return
     */
    MemberInfo selectMemberMemberInfoByUserId(Integer id);

    /**
     * @param userId
     * @param openId
     * @param unionId
     */
    void updateMemberWxInfo(Integer userId, String openId, String unionId);

    Boolean updateUser(String mobile, Short code);

    RcUser findByEmail(String email);

    R<Boolean> sendEmailCode(String email, HttpServletRequest request);

}
