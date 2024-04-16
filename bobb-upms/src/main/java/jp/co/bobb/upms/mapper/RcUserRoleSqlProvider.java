package jp.co.bobb.upms.mapper;

import org.springframework.util.CollectionUtils;

import java.util.Set;

/**
 * describe:
 *
 * @author Parker Sun
 * @date 2019/07/16
 */
public class RcUserRoleSqlProvider {
    private String deleteBatchByMenuIds(Set<String> pIdSet) {
        StringBuilder sb = new StringBuilder();
        sb.append("delete from rc_privilege where");
        if (!CollectionUtils.isEmpty(pIdSet)) {
            sb.append(" menu_id in (");
            pIdSet.forEach(sb::append);
            sb.append(" )");
        }
        return sb.toString();
    }
}
