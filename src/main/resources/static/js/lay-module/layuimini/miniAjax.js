/**
 * date:2023/06/18
 * author:liuxingyu01
 * version:1.0
 * description:layuimini ajax框架扩展
 */
layui.define(["jquery"], function (exports) {
    var $ = layui.jquery;

    /**
     * 定义后端服务的地址（绝对地址，前后端分离的时候会用到这个）
     */
    var baseURL = "";

    /**
     * 定义后端服务的地址（反向代理的地址）
     */
    // var baseURL = "/back";

    /**
     * 封装AJAX，设置AJAX的全局默认选项，
     * 当AJAX请求会话过期时，跳转到登录页面
     */
    var _ajax = $.ajax;
    $.ajax = function (opt) {
        var fn = {
            error: function (XMLHttpRequest, textStatus, errorThrown) {
            },
            success: function (data, textStatus) {
            },
            beforeSend: function (XHR) {
            },
            complete: function (XHR, TS) {
            }
        };
        if (opt.error) {
            fn.error = opt.error;
        }
        if (opt.success) {
            fn.success = opt.success;
        }
        if (opt.beforeSend) {
            fn.beforeSend = opt.beforeSend;
        }
        if (opt.complete) {
            fn.complete = opt.complete;
        }
        //扩展增强处理
        var _opt = $.extend(opt, {
            error: function (XMLHttpRequest, textStatus, errorThrown) {
                // 错误方法增强处理
                fn.error(XMLHttpRequest, textStatus, errorThrown);
            },
            // 只有 HTTP 状态码为 200（包括 200-299 范围内）的 Ajax 请求才会触发 success 回调函数
            // 其他状态码将触发 error 回调函数
            success: function (res, textStatus) {
                if (res.code === 3001 || res.code === '3001') {
                    layer.alert('会话已过期，请重新登录', function (index) {
                        layer.close(index);
                        window.location.href = "/page/login.html";
                    });
                } else {
                    // 其他返回码不是401的请求，都由各页面自行处理
                    // 成功回调方法增强处理
                    fn.success(res, textStatus);
                }
            },
            beforeSend: function (XHR, settings) {
                // 只有访问后端的ajax请求才需要拼接baseURL
                if (settings.dataType !== "html" && settings.dataType !== "script") {
                    settings.url = baseURL + settings.url; // 修改url
                }
                // 设置全局超时时间为20秒
                settings.timeout = 20000;

                // 设置token
                if (sessionStorage.getItem('token')) {
                    XHR.setRequestHeader("token", sessionStorage.getItem('token'));
                }
                XHR.setRequestHeader("Powered-By", 'TINYCLOUD');
                // 提交前回调方法
                fn.beforeSend(XHR);
            },
            complete: function (XHR, textStatus) {
                // 完成后回调方法
                fn.complete(XHR, textStatus);
            }
        });
        return _ajax(_opt);
    };

    /**
     * Ajax请求封装
     */
    var miniAjax = {

        /**
         * GET 请求
         */
        get: function (options) {
            if (!options.url) {
                alert('请求错误，url不可为空!');
                return false;
            }
            options.type = 'GET';
            options.timeout = options.timeout || 20000; // 设置本地的请求超时时间（以毫秒计）
            options.async = options.async !== false; // 布尔值，表示请求是否异步处理。默认是 true
            options.cache = options.cache !== true; // 布尔值，表示浏览器是否缓存被请求页面，默认是false
            options.dataType = options.dataType || 'json';
            $.ajax({
                url: options.url,
                type: options.type,
                timeout: options.timeout,
                async: options.async,
                cache: options.cache,
                dataType: options.dataType,
                success: function (data, textStatus, jqXHR) {
                    // 成功回调
                    options.success(data);
                },
                error: function (XMLHttpRequest, textStatus, errorThrown) {
                    // 错误回调
                    options.error("错误提示： " + XMLHttpRequest.status + " " + XMLHttpRequest.statusText);
                }
            });
        },


        /**
         * POST 请求
         */
        post: function (options) {
            if (!options.url) {
                alert('请求错误，url不可为空!');
                return false;
            }
            options.type = 'POST';
            options.timeout = options.timeout || 20000;
            options.async = options.async !== false;
            options.cache = options.cache !== true;
            options.dataType = options.dataType || 'json';
            options.contentType = options.contentType || 'application/x-www-form-urlencoded';
            options.data = options.data || {};
            $.ajax({
                url: options.url,
                type: options.type,
                timeout: options.timeout,
                async: options.async,
                cache: options.cache,
                dataType: options.dataType,
                data: options.data,
                contentType: options.contentType, // 发送数据到服务器时所使用的内容类型。默认是："application/x-www-form-urlencoded"
                success: function (data, textStatus, jqXHR) {
                    // 成功回调
                    options.success(data);
                },
                error: function (XMLHttpRequest, textStatus, errorThrown) {
                    // 错误回调
                    options.error("错误提示： " + XMLHttpRequest.status + " " + XMLHttpRequest.statusText);
                }
            });
        },


        /**
         * POST-JSON 请求
         */
        postJSON: function (options) {
            if (!options.url) {
                alert('请求错误，url不可为空!');
                return false;
            }
            options.type = 'POST';
            options.timeout = options.timeout || 20000;
            options.async = options.async !== false;
            options.cache = options.cache !== true;
            options.dataType = options.dataType || 'json';
            options.contentType = options.contentType || 'application/json';
            options.data = options.data || '';
            $.ajax({
                url: options.url,
                type: options.type,
                timeout: options.timeout,
                async: options.async,
                cache: options.cache,
                dataType: options.dataType,
                data: options.data,
                contentType: options.contentType, // 发送数据到服务器时所使用的内容类型。默认是："application/x-www-form-urlencoded"
                success: function (data, textStatus, jqXHR) {
                    // 成功回调
                    options.success(data);
                },
                error: function (XMLHttpRequest, textStatus, errorThrown) {
                    // 错误回调
                    options.error("错误提示： " + XMLHttpRequest.status + " " + XMLHttpRequest.statusText);
                }
            });
        },

        /**
         * upload 文件上传 请求
         */
        upload: function (options) {
            if (!options.url) {
                alert('请求错误，url不可为空!');
                return false;
            }
            options.type = 'POST';
            options.timeout = options.timeout || 20000;
            options.async = options.async !== false;
            options.cache = options.cache !== true;
            options.dataType = options.dataType || 'json';
            options.contentType = options.contentType || false;
            options.processData = options.processData || false;
            options.data = options.data || new FormData();
            $.ajax({
                url: options.url,
                type: options.type,
                timeout: options.timeout,
                async: options.async,
                cache: options.cache,
                dataType: options.dataType,
                processData: options.processData,
                data: options.data,
                contentType: options.contentType,
                success: function (data, textStatus, jqXHR) {
                    // 成功回调
                    options.success(data);
                },
                error: function (XMLHttpRequest, textStatus, errorThrown) {
                    // 错误回调
                    options.error("错误提示： " + XMLHttpRequest.status + " " + XMLHttpRequest.statusText);
                }
            });
        },
        /**
         * download 文件上下载 请求
         * 改为ajax下载文件，这样方便设置header，适配前后端分离的情况（$.ajax不支持blob类型，所以这里用原生的XMLHttpRequest）
         */
        download: function (options) {
            if (!options.url) {
                alert('请求错误，url不可为空!');
                return false;
            }
            let xhr = new XMLHttpRequest();
            xhr.open('GET', options.url, true);    // 也可以使用POST方式，根据接口
            xhr.responseType = 'blob';    // 返回类型blob
            xhr.setRequestHeader("token", sessionStorage.getItem('token'));
            // 定义请求完成的处理函数，请求前也可以增加加载框/禁用下载按钮逻辑
            xhr.onload = function () {
                // 请求完成
                if (this.status === 200) {
                    // 返回200
                    let blob = this.response;
                    let temp = xhr.getResponseHeader("content-disposition").split(";")[1].split("=")[1];
                    let fileName = decodeURIComponent(temp);
                    console.log("fileName = " + fileName);
                    let reader = new FileReader();
                    reader.readAsDataURL(blob);
                    reader.onload = function (e) {
                        // 转换完成，创建一个a标签用于下载
                        let a = document.createElement('a');
                        a.download = fileName;
                        a.href = e.target.result;
                        $("body").append(a);    // 修复firefox中无法触发click
                        a.click();
                        $(a).remove();
                    }
                }
            };
            // 发送ajax请求
            xhr.send()
        }
    };

    exports("miniAjax", miniAjax);
});