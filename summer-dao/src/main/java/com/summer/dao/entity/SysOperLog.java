/* https://github.com/12641561 */
package com.summer.dao.entity;

import java.util.Date;

public class SysOperLog {
    /**
     * 日志主键
     */
    private Long operId;

    /**
     * 模块标题
     */
    private String title;

    /**
     * 业务类型（0其它 1新增 2修改 3删除）  默认：0
     */
    private Integer businessType;

    /**
     * 方法名称
     */
    private String method;

    /**
     * 请求方式
     */
    private String requestMethod;

    /**
     * 操作类别（0其它 1后台用户 2手机端用户）  默认：0
     */
    private Integer operatorType;

    /**
     * 操作人员
     */
    private String operName;

    /**
     * 部门名称
     */
    private String deptName;

    /**
     * 请求URL
     */
    private String operUrl;

    /**
     * 主机地址
     */
    private String operIp;

    /**
     * 操作地点
     */
    private String operLocation;

    /**
     * 请求参数
     */
    private String operParam;

    /**
     * 返回参数
     */
    private String jsonResult;

    /**
     * 操作状态（0正常 1异常）  默认：0
     */
    private Integer status;

    /**
     * 错误消息
     */
    private String errorMsg;

    /**
     * 操作时间
     */
    private Date operTime;

    /**
     * 获取 日志主键 sys_oper_log.oper_id
     *
     * @return 日志主键
     */
    public Long getOperId() {
        return operId;
    }

    /**
     * 设置 日志主键 sys_oper_log.oper_id
     *
     * @param operId 日志主键
     */
    public void setOperId(Long operId) {
        this.operId = operId;
    }

    /**
     * 获取 模块标题 sys_oper_log.title
     *
     * @return 模块标题
     */
    public String getTitle() {
        return title;
    }

    /**
     * 设置 模块标题 sys_oper_log.title
     *
     * @param title 模块标题
     */
    public void setTitle(String title) {
        this.title = title == null ? null : title.trim();
    }

    /**
     * 获取 业务类型（0其它 1新增 2修改 3删除） sys_oper_log.business_type
     *
     * @return 业务类型（0其它 1新增 2修改 3删除）
     */
    public Integer getBusinessType() {
        return businessType;
    }

    /**
     * 设置 业务类型（0其它 1新增 2修改 3删除） sys_oper_log.business_type
     *
     * @param businessType 业务类型（0其它 1新增 2修改 3删除）
     */
    public void setBusinessType(Integer businessType) {
        this.businessType = businessType;
    }

    /**
     * 获取 方法名称 sys_oper_log.method
     *
     * @return 方法名称
     */
    public String getMethod() {
        return method;
    }

    /**
     * 设置 方法名称 sys_oper_log.method
     *
     * @param method 方法名称
     */
    public void setMethod(String method) {
        this.method = method == null ? null : method.trim();
    }

    /**
     * 获取 请求方式 sys_oper_log.request_method
     *
     * @return 请求方式
     */
    public String getRequestMethod() {
        return requestMethod;
    }

    /**
     * 设置 请求方式 sys_oper_log.request_method
     *
     * @param requestMethod 请求方式
     */
    public void setRequestMethod(String requestMethod) {
        this.requestMethod = requestMethod == null ? null : requestMethod.trim();
    }

    /**
     * 获取 操作类别（0其它 1后台用户 2手机端用户） sys_oper_log.operator_type
     *
     * @return 操作类别（0其它 1后台用户 2手机端用户）
     */
    public Integer getOperatorType() {
        return operatorType;
    }

    /**
     * 设置 操作类别（0其它 1后台用户 2手机端用户） sys_oper_log.operator_type
     *
     * @param operatorType 操作类别（0其它 1后台用户 2手机端用户）
     */
    public void setOperatorType(Integer operatorType) {
        this.operatorType = operatorType;
    }

    /**
     * 获取 操作人员 sys_oper_log.oper_name
     *
     * @return 操作人员
     */
    public String getOperName() {
        return operName;
    }

    /**
     * 设置 操作人员 sys_oper_log.oper_name
     *
     * @param operName 操作人员
     */
    public void setOperName(String operName) {
        this.operName = operName == null ? null : operName.trim();
    }

    /**
     * 获取 部门名称 sys_oper_log.dept_name
     *
     * @return 部门名称
     */
    public String getDeptName() {
        return deptName;
    }

    /**
     * 设置 部门名称 sys_oper_log.dept_name
     *
     * @param deptName 部门名称
     */
    public void setDeptName(String deptName) {
        this.deptName = deptName == null ? null : deptName.trim();
    }

    /**
     * 获取 请求URL sys_oper_log.oper_url
     *
     * @return 请求URL
     */
    public String getOperUrl() {
        return operUrl;
    }

    /**
     * 设置 请求URL sys_oper_log.oper_url
     *
     * @param operUrl 请求URL
     */
    public void setOperUrl(String operUrl) {
        this.operUrl = operUrl == null ? null : operUrl.trim();
    }

    /**
     * 获取 主机地址 sys_oper_log.oper_ip
     *
     * @return 主机地址
     */
    public String getOperIp() {
        return operIp;
    }

    /**
     * 设置 主机地址 sys_oper_log.oper_ip
     *
     * @param operIp 主机地址
     */
    public void setOperIp(String operIp) {
        this.operIp = operIp == null ? null : operIp.trim();
    }

    /**
     * 获取 操作地点 sys_oper_log.oper_location
     *
     * @return 操作地点
     */
    public String getOperLocation() {
        return operLocation;
    }

    /**
     * 设置 操作地点 sys_oper_log.oper_location
     *
     * @param operLocation 操作地点
     */
    public void setOperLocation(String operLocation) {
        this.operLocation = operLocation == null ? null : operLocation.trim();
    }

    /**
     * 获取 请求参数 sys_oper_log.oper_param
     *
     * @return 请求参数
     */
    public String getOperParam() {
        return operParam;
    }

    /**
     * 设置 请求参数 sys_oper_log.oper_param
     *
     * @param operParam 请求参数
     */
    public void setOperParam(String operParam) {
        this.operParam = operParam == null ? null : operParam.trim();
    }

    /**
     * 获取 返回参数 sys_oper_log.json_result
     *
     * @return 返回参数
     */
    public String getJsonResult() {
        return jsonResult;
    }

    /**
     * 设置 返回参数 sys_oper_log.json_result
     *
     * @param jsonResult 返回参数
     */
    public void setJsonResult(String jsonResult) {
        this.jsonResult = jsonResult == null ? null : jsonResult.trim();
    }

    /**
     * 获取 操作状态（0正常 1异常） sys_oper_log.status
     *
     * @return 操作状态（0正常 1异常）
     */
    public Integer getStatus() {
        return status;
    }

    /**
     * 设置 操作状态（0正常 1异常） sys_oper_log.status
     *
     * @param status 操作状态（0正常 1异常）
     */
    public void setStatus(Integer status) {
        this.status = status;
    }

    /**
     * 获取 错误消息 sys_oper_log.error_msg
     *
     * @return 错误消息
     */
    public String getErrorMsg() {
        return errorMsg;
    }

    /**
     * 设置 错误消息 sys_oper_log.error_msg
     *
     * @param errorMsg 错误消息
     */
    public void setErrorMsg(String errorMsg) {
        this.errorMsg = errorMsg == null ? null : errorMsg.trim();
    }

    /**
     * 获取 操作时间 sys_oper_log.oper_time
     *
     * @return 操作时间
     */
    public Date getOperTime() {
        return operTime;
    }

    /**
     * 设置 操作时间 sys_oper_log.oper_time
     *
     * @param operTime 操作时间
     */
    public void setOperTime(Date operTime) {
        this.operTime = operTime;
    }
}