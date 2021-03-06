package org.opsli.core.cache.pushsub.handler;

import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.opsli.api.wrapper.system.dict.DictModel;
import org.opsli.common.constants.CacheConstants;
import org.opsli.common.constants.DictConstants;
import org.opsli.core.cache.local.CacheUtil;
import org.opsli.core.cache.pushsub.enums.CacheType;
import org.opsli.core.cache.pushsub.enums.DictModelType;
import org.opsli.core.cache.pushsub.enums.MsgArgsType;
import org.opsli.core.cache.pushsub.enums.PushSubType;
import org.opsli.plugins.cache.EhCachePlugin;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Collection;
import java.util.List;

/**
 * @BelongsProject: opsli-boot
 * @BelongsPackage: org.opsli.core.cache.pushsub.handler
 * @Author: Parker
 * @CreateTime: 2020-09-15 16:24
 * @Description: 字典消息处理
 */
@Slf4j
public class DictHandler implements RedisPushSubHandler{

    @Autowired
    EhCachePlugin ehCachePlugin;

    @Override
    public PushSubType getType() {
        return PushSubType.DICT;
    }

    @Override
    public void handler(JSONObject msgJson) {
        DictModelType dictModelType = DictModelType.valueOf((String) msgJson.get(MsgArgsType.DICT_MODEL_TYPE.toString()));
        CacheType type = CacheType.valueOf((String) msgJson.get(MsgArgsType.DICT_TYPE.toString()));

        if(DictModelType.COLLECTION == dictModelType){
            Collection<Object> dicts = (Collection<Object>) msgJson.get(MsgArgsType.DICT_MODELS.toString());
            for (Object dictObj : dicts) {
                JSONObject jsonObject = (JSONObject) dictObj;
                DictModel dictModel = JSONObject.toJavaObject(jsonObject, DictModel.class);
                this.handler(dictModel, type);
            }
        } else if(DictModelType.OBJECT == dictModelType){
            Object dictObj = msgJson.get(MsgArgsType.DICT_MODEL.toString());
            JSONObject jsonObject = (JSONObject) dictObj;
            DictModel dictModel = JSONObject.toJavaObject(jsonObject, DictModel.class);
            this.handler(dictModel, type);
        }
    }


    /**
     * 真正处理 - 只是处理自己本地的缓存
     * @param dictModel
     * @param type
     */
    private void handler(DictModel dictModel, CacheType type){

        // 解析 key
        String ehKeyByName = CacheUtil.handleKey(CacheConstants.EDEN_HASH_DATA,
                DictConstants.CACHE_PREFIX_NAME + dictModel.getTypeCode() + ":" + dictModel.getDictName());
        String ehKeyByValue = CacheUtil.handleKey(CacheConstants.EDEN_HASH_DATA,
                DictConstants.CACHE_PREFIX_VALUE + dictModel.getTypeCode() + ":" + dictModel.getDictValue());

        // 缓存更新
        if(CacheType.UPDATE == type){
            ehCachePlugin.delete(CacheConstants.HOT_DATA, ehKeyByName);
            ehCachePlugin.delete(CacheConstants.HOT_DATA, ehKeyByValue);

            // 统一转换为 JSONObject
            String jsonStr = JSONObject.toJSONString(dictModel.getModel());
            JSONObject value = JSONObject.parseObject(jsonStr);
            ehCachePlugin.put(CacheConstants.HOT_DATA, ehKeyByName, value);
            ehCachePlugin.put(CacheConstants.HOT_DATA, ehKeyByValue, value);
        }
        // 缓存删除
        else if(CacheType.DELETE == type){
            ehCachePlugin.delete(CacheConstants.HOT_DATA, ehKeyByName);
            ehCachePlugin.delete(CacheConstants.HOT_DATA, ehKeyByValue);
        }
    }

}
