package com.kuradeon.freemarker.templateloader;

import freemarker.cache.URLTemplateLoader;

import java.net.URL;

/**
 * 自定义远程模板加载器
 * @author kuradeon
 * @date 2019-01-10 16:08
 */
public class RemoteTemplateLoader extends URLTemplateLoader {

    @Override
    protected URL getURL(String name) {
        // 自己实现远程模板URL加载
        return null;
    }
}
