package org.tinycloud.tinymock.common.utils;

import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.DefaultResourceLoader;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;
import java.io.*;
import java.util.Map;


/**
 * <p>
 * 统一业务异常封装
 * </p>
 *
 * @author liuxingyu01
 * @since 2023-05-30 13:26
 */
@Slf4j
public class DataMockUtils {

    private static ScriptEngine MOCK_JS_ENGINE;

    static {
        // 读取resource路径下的js
        String path = "static/lib/mock/mock-min.js";
        ResourceLoader resourceLoader = new DefaultResourceLoader();
        Resource resource = resourceLoader.getResource("classpath:" + path);
        try (InputStream inputStream = resource.getInputStream(); BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream))) {
            MOCK_JS_ENGINE = new ScriptEngineManager().getEngineByName("nashorn");
            MOCK_JS_ENGINE.eval(reader);
        } catch (ScriptException | IOException e) {
            log.error("加载Mock.Js错误", e);
        }
    }


    /**
     * 调用mock.js执行方法生成模拟数据
     *
     * @param script js脚本命令
     */
    public static String invoke(String script) {
        try {
            // 调用 JavaScript 中的方法
            return MOCK_JS_ENGINE.eval("JSON.stringify(Mock." + script + ")").toString();
        } catch (ScriptException e) {
            e.printStackTrace();
        }
        return null;
    }


    /**
     * 方法描述：根据数据模板生成模拟数据。
     * 创建时间：2019-06-14 22:42:12
     *
     * @param template 表示数据模板，可以是对象或字符串。例如 { 'data|1-10':[{}] }、'@EMAIL'。
     */
    public static Map<String, Object> mock(String template) throws ScriptException {
        String format = "mock(" + template + ")";
        String random = invoke(format);
        return JsonUtils.readMap(random);
    }

    public static void main(String[] args) throws ScriptException, UnsupportedEncodingException {

        Map map = mock("{\"code\":\"0000\",\"data\":{\"list|20\":[{\"name\":\"@name\",\"age\":\"@integer(10, 30)\"}],\"url\":\"@email\"},\"desc\":\"成功\"}");

        System.out.println(JsonUtils.toJsonString(map));

    }
}
