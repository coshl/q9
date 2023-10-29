/* https://github.com/12641561 */
package com.summer.dao.entity;

public class WhiteUser {
    /**
     * 自增ID
     */
    private Integer id;

    /**
     * 手机号码
     */
    private String phone;

    /**
     * 获取 自增ID white_user.id
     *
     * @return 自增ID
     */
    public Integer getId() {
        return id;
    }

    /**
     * 设置 自增ID white_user.id
     *
     * @param id 自增ID
     */
    public void setId(Integer id) {
        this.id = id;
    }

    /**
     * 获取 手机号码 white_user.phone
     *
     * @return 手机号码
     */
    public String getPhone() {
        return phone;
    }

    /**
     * 设置 手机号码 white_user.phone
     *
     * @param phone 手机号码
     */
    public void setPhone(String phone) {
        this.phone = phone == null ? null : phone.trim();
    }
}