package com.summer.dao.entity;

import lombok.Data;
import org.apache.ibatis.type.Alias;

import java.math.BigDecimal;

@Alias("SourceChannel")
@Data
public class SourceChannel extends BaseEntity {

    private static final long serialVersionUID = 1L;

    /**
     * 合作模式
     */
    public enum WaysOfCoperate {

        // 合作模式
        toPublic(0, "对公"),
        toPrivate(1, "对私");

        private Integer code;
        private String name;

        private WaysOfCoperate(int code, String name) {
            this.code = new Integer(code);
            this.name = name;
        }

        public Integer getCode() {
            return code;
        }

        public String getName() {
            return name;
        }
    }

    /**
     * 合作模式
     */
    public enum WaysOfPayment {

        // 合作模式
        daily(0, "日结"),
        monthly(1, "月结");

        private Integer code;
        private String name;

        private WaysOfPayment(int code, String name) {
            this.code = new Integer(code);
            this.name = name;
        }

        public Integer getCode() {
            return code;
        }

        public String getName() {
            return name;
        }
    }

    /**
     * 渠道商编号
     */
    private String code;

    /**
     * 渠道商名称
     */
    private String name;

    /**
     * 渠道商经理联系方式
     */
    private String managerPhone;

    /**
     * 渠道商经理名字
     */
    private String managerName;

    /**
     * 运营员名字
     */
    private String operatorName;

    /**
     * 运营员电话
     */
    private String operatorPhone;

    /**
     * 合作价格
     */
    private BigDecimal price;

    /**
     * 合作模式：对公,对私
     */
    private WaysOfCoperate waysOfCoperate;

    /**
     * 结算方式：日结,月结
     */
    private WaysOfPayment waysOfPayment;

    /**
     * 状态：禁用 0，启用 1
     */
    private Boolean enable;
    /**
     * 状态：0下线 1上线
     */
    private Boolean onlineFlag;

}
