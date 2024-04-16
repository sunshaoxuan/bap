package jp.co.bobb.common.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

/**
 * 流程枚举
 */
@Getter
@AllArgsConstructor
public enum ProcessKeyEnums {
    TRUSTBANKMATCH_A("trustBankMatch_A", "不动产托管银行流水匹配(BoBB・不動産委託)"),
    TRUST_SETTLE("trust_settle", "委託契約を決算"),
    LONG_RENT_ORDER_OVERDUE_RENT_NOTIFY("long_rent_order_overdue_rent_notify", "长租租约过期提醒"),
    BANK_MATCH_ROLLBACK("bank_match_rollback", "不動産委託.银行明细回滚流程"),
    TRUST_ORDER_CLEAN("trust_order_clean", "委託契約清算"),
    BANK_TRADE_MATCH_FINANCIAL_NOTICE("bank_trade_match_financial_notice", "银行交易数据匹配财务通知"),
    ESCROW_DATA_FINANCIAL_NOTICE("escrow_data_financial_notice", "托管数据财务通知"),
    RENT_RENEW_NOTIFY("rent_renew_notify", "賃貸契約更新通告"),
    KOUZA_REPAYMENT("kouza_repayment", "不動産投資収益お支払财务提醒"),
    TRUST_ORDER_OVERDUE_NOTIFY("trust_order_overdue_notify", "託管到期提醒(委託契約更新)"),
    HOUSEOWNER_CERTIFICATION_JUDGE("houseowner_certification_judge", "居住者身份认证"),
    PROFIT_INTENTION("profit_intention", "電子契約"),
    MOCHINUSHIENOSHIHARAI_FEE("mochinushienoshiharai_fee", "家主仮払金"),
    RYOUSYOUSYO("ryousyousyo", "領収書"),
    TRUST_SETTLEMENT_TAX("trust_settlement_tax", "所得税確定申告書"),
    KEY_MANAGERMENT("key_management", "钥匙管理"),
    NEW_MOCHINUSHIENOSHIHARAI("new_mochinushienoshiharai", "新家主仮払金"),
    TRUST_PAYMENT("trustPayment", "委託支払"),
    AUTONOMOUS_ACCESS("autonomous_access", "外部委託接続"),
    LEGAL_PERSON_APPLY("legal_person_apply", "法人認証");

    /**
     * 全部枚举信息
     * 前端要求按顺序输出
     */
    public static List<String> list = new ArrayList();

    static {
        ProcessKeyEnums[] vakues = ProcessKeyEnums.values();
        for (ProcessKeyEnums e : vakues) {
            list.add(e.getKey());
        }
    }

    private String key;
    private String name;

    public static ProcessKeyEnums getByKey(String key) {
        for (ProcessKeyEnums keyEnums : values()) {
            if (keyEnums.getKey().equals(key)) {
                return keyEnums;
            }
        }
        return null;
    }
}
