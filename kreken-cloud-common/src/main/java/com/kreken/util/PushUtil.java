package com.kreken.util;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

/**
 * 消息推送
 * @author kreken
 * @date 2018-04-18
 * @company xiliangmen
 */
public class PushUtil {

    /**
     * 发送post请求(application/xml)
     * @param reqURL 发送地址
     * @param data 数据
     * @param encodeCharset 编码
     * @return
     */
    public static String sendPostRequest(String reqURL, String data, String encodeCharset){
        HttpPost httpPost = new HttpPost(reqURL);
        HttpClient client = new DefaultHttpClient();
        HttpResponse response = null;
        String result = null;
        try {
            StringEntity entity = new StringEntity(data, encodeCharset);
            entity.setContentType("application/xml;utf8");
            httpPost.setEntity(entity);
            httpPost.setHeader("MsgType","XML");//约定
            httpPost.setHeader("SourceCode","ELK");//约定
            response = client.execute(httpPost);
            result = EntityUtils.toString(response.getEntity(), encodeCharset);
        } catch (Exception e) {
            e.printStackTrace();
        }finally{
            client.getConnectionManager().shutdown();
        }
        return result;
    }
}
