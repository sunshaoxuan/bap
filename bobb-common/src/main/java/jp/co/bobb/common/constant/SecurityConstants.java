package jp.co.bobb.common.constant;

/**
 * describe:
 *
 * @author Parker Sun
 * @date 2018/10/22
 */
public interface SecurityConstants {
    /**
     * 默认生成图形验证码宽度
     */
    String DEFAULT_IMAGE_WIDTH = "100";

    /**
     * 默认生成图像验证码高度
     */
    String DEFAULT_IMAGE_HEIGHT = "40";

    /**
     * 默认生成图形验证码长度
     */
    String DEFAULT_IMAGE_LENGTH = "4";

    /**
     * 默认生成图形验证码过期时间
     */
    int DEFAULT_IMAGE_EXPIRE = 60;

    /**
     * 登录验证码过期时间
     */
    int DEFAULT_LOGIN_VAL_CODE_EXPIRE = 5;

    int DEFAULT_MEETING_VAL_CODE_EXPIRE = 10;
    /**
     * 边框颜色，合法值： r,g,b (and optional alpha) 或者 white,black,blue.
     */
    String DEFAULT_COLOR_FONT = "black";

    /**
     * 图片边框
     */
    String DEFAULT_IMAGE_BORDER = "no";
    /**
     * 默认图片间隔
     */
    String DEFAULT_CHAR_SPACE = "5";

    /**
     * 默认保存code的前缀
     */
    String DEFAULT_CODE_KEY = "DEFAULT_CODE_KEY";


    String LOGIN_MOBILE_CODE_KEY = "login.code.{0}";

    String MEETING_FREE_MOBILE_CODE = "meeting.free.mobile.code.{0}";

    /**
     * 验证码文字大小
     */
    String DEFAULT_IMAGE_FONT_SIZE = "30";


    /**
     * 用户状态，1：正常，0：禁用，-1锁定
     */
    String USER_STATUS_ENABLED = "1";
    String USER_STATUS_DISABLED = "0";
    String USER_STATUS_LOCKED = "-1";


    Integer ROLE_STATUS_ENABLED = 1;
    Integer ROLE_STATUS_DISABLED = 0;

    /**
     * 默认国隙区号 86 中国
     */
    String DEFAULT_BIZ = "86";

    String HTTP_SERVER_DOMAIN_NAME = "HTTP_SERVER_DOMAIN_NAME";
}
