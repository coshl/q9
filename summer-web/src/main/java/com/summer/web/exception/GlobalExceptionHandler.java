
package com.summer.web.exception;

import com.summer.pojo.dto.ArgumentInvalidResult;
import com.summer.util.CallBackResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import java.util.*;

/**
 * 〈全局异常处理〉
 *
 * @author tl
 */
@ControllerAdvice
@ResponseBody
public class GlobalExceptionHandler {

    private static final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    /**
     * 所有异常报错
     *
     * @param request
     * @param exception
     * @return
     * @throws Exception
     */
    @ExceptionHandler(value = Exception.class)
    public String allExceptionHandler(HttpServletRequest request,
                                      Exception exception) {
        log.error(exception.getMessage(), request.getRequestURI(), exception);
        exception.printStackTrace();
        return CallBackResult.returnJson(999, exception.getMessage(), null);
    }

    /**
     * 全局JOSN请求参数校验异常捕获的方法
     * 注意点：1，该JSON请求参数对象必须为加@RequestBody @RequestBody GlobalParamValidatorDto globalParamValidatorDto）
     * 2，不能为直接为（GlobalParamValidatorDto globalParamValidatorDto)）
     * 否则校验错误的信息只会在后台控制台显示，返回给前端的时却会被全局异常捕获
     * 3，该方法可以返回：错误的信息、错误的字段、错误的值
     */
    @ExceptionHandler(value = MethodArgumentNotValidException.class)
    public Object MethodArgumentNotValidHandler(HttpServletRequest request,
                                                MethodArgumentNotValidException exception) {
        //按需重新封装需要返回的错误信息
        List<ArgumentInvalidResult> invalidArguments = new ArrayList<>();
        //解析原错误信息，封装后返回，此处返回非法的字段名称，原始值，错误信息
        for (FieldError error : exception.getBindingResult().getFieldErrors()) {
            ArgumentInvalidResult invalidArgument = new ArgumentInvalidResult();
            invalidArgument.setDefaultMessage(error.getDefaultMessage());
            invalidArgument.setField(error.getField());
            invalidArgument.setRejectedValue(error.getRejectedValue());
            invalidArguments.add(invalidArgument);
        }
        return CallBackResult.returnJson(CallBackResult.PARAM_IS_ERROR, CallBackResult.PARAM_IS_EXCEPTION_MSG, invalidArguments);
    }

    /**
     * 全局参数请求参数校验异常捕获的方法
     * 注意：1·使用该方法捕获时，@Validated注解需要放在controller层的类名上才会起作用，
     * 否则校验的信息只会在后台控制台显示，返回给前端的时却会被全局异常捕获
     * <p>
     * 2·适用校验在方法里的单个或几个参数使用，比如 ： public Object sendPhoneCaptcha(
     *
     * @param ex
     * @return
     * @NotBlank(message = "手机号不能为空")
     * @Pattern(regexp = Constant.PHONE_REGULAR,message = "手机号码非法")
     * String phoneNumber)throws Exception{
     * 3·该方法只会返回错误的信息
     */
    @ExceptionHandler(value = ConstraintViolationException.class)
    public Object ConstraintViolationExceptionHandler(ConstraintViolationException ex) {
        Set<ConstraintViolation<?>> constraintViolations = ex.getConstraintViolations();
        Iterator<ConstraintViolation<?>> iterator = constraintViolations.iterator();
        List<ArgumentInvalidResult> invalidArguments = new ArrayList<>();
        while (iterator.hasNext()) {
            ConstraintViolation<?> cvl = iterator.next();
            ArgumentInvalidResult invalidArgument = new ArgumentInvalidResult();
            invalidArgument.setDefaultMessage(cvl.getMessageTemplate());
            //invalidArgument.setField(cvl.getPropertyPath().toString());
            invalidArguments.add(invalidArgument);
        }
        return CallBackResult.returnJson(CallBackResult.PARAM_IS_ERROR, CallBackResult.PARAM_IS_EXCEPTION_MSG, invalidArguments);
    }
}
