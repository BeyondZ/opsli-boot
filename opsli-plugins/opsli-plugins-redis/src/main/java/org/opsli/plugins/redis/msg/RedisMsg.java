package org.opsli.plugins.redis.msg;

import org.opsli.common.base.msg.BaseMsg;

/**
 * @BelongsProject: opsli-boot
 * @BelongsPackage: org.opsli.plugins.mail.msg
 * @Author: Parker
 * @CreateTime: 2020-09-13 19:54
 * @Description: Redis消息
 */
public enum RedisMsg implements BaseMsg {

    /** Redis异常 */
    EXCEPTION_KEY_NULL(90300,"Key不可为空！"),
    EXCEPTION_INCREMENT(90301,"递增值必须大于0！"),
    EXCEPTION_DECREMENT(90302,"递减值必须大于0！"),
    EXCEPTION_REFLEX(90303,"反射Redis脚本失败"),
    EXCEPTION_PUSH_SUB_NULL(90304,"发布消息体不可为空！"),
    ;


    private int code;
    private String message;

    RedisMsg(int code, String message){
        this.code = code;
        this.message = message;
    }

    @Override
    public Integer getCode() {
        return this.code;
    }

    @Override
    public String getMessage() {
        return this.message;
    }
}
