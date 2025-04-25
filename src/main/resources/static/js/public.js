var CommonConfig = {
    // 密码长度为8-20位且必须包含字母、数字、特殊符号（如：@#$%^&*()_+-=）等三种字符
    passwordRegex: /^(?=.*[a-zA-Z])(?=.*\d)(?=.*[@#$%^&*()_+\-=])[a-zA-Z\d@#$%^&*()_+\-=]{8,20}$/,

    passwordSalt: "W%o$gxG6Nbbg+S4@W5&9VQeud&2O1^V8&We^YwhX-1N+_m7&KQ#JJ@a+UX9Rr5Bk"
};

/**
 * 判断对象是否为空
 * @param obj
 * @returns {boolean}
 */
function isEmpty(obj) {
    if (typeof obj === 'undefined' || obj == null || obj === '') {
        return true;
    } else {
        return false;
    }
}


/**
 * 判断对象是否不为空
 * @param obj
 * @returns {boolean}
 */
function isNotEmpty(obj) {
    return !isEmpty(obj);
}


/**
 * 下划线转换驼峰
 * @param name
 * @returns {*}
 */
function toHump(name) {
    return name.replace(/\_(\w)/g, function (all, letter) {
        return letter.toUpperCase();
    });
}


/**
 * 驼峰转换下划线
 * @param name
 * @returns {string}
 */
function toLine(name) {
    return name.replace(/([A-Z])/g, "_$1").toLowerCase();
}


/**
 * 获取当前日期 yyyyMMdd
 * @returns {string}
 */
function getCurrentDate() {
    let date = new Date();
    let month = date.getMonth() + 1;
    let strDate = date.getDate();
    if (month >= 1 && month <= 9) {
        month = "0" + month;
    }
    if (strDate >= 0 && strDate <= 9) {
        strDate = "0" + strDate;
    }
    month = month + "";
    strDate = strDate + "";
    return date.getFullYear() + month + strDate;
}


/**
 * 获取当前时间yyyyMMddHHmmss
 * @returns {string}
 */
function getCurrentTime() {
    let date = new Date();
    let month = date.getMonth() + 1;
    let strDate = date.getDate();
    if (month >= 1 && month <= 9) {
        month = "0" + month;
    }
    if (strDate >= 0 && strDate <= 9) {
        strDate = "0" + strDate;
    }
    let strHour = date.getHours();
    if (strHour >= 0 && strHour <= 9) {
        strHour = "0" + strHour;
    }
    let strMinute = date.getMinutes();
    if (strMinute >= 0 && strMinute <= 9) {
        strMinute = "0" + strMinute;
    }
    let strSecond = date.getSeconds();
    if (strSecond >= 0 && strSecond <= 9) {
        strSecond = "0" + strSecond;
    }
    month = month + "";
    strDate = strDate + "";
    strHour = strHour + "";
    strMinute = strMinute + "";
    strSecond = strSecond + "";
    return date.getFullYear() + month + strDate
        + strHour + strMinute + strSecond;
}


/**
 * 转化时间戳或日期对象为日期格式字符
 * time：可以是日期对象，也可以是毫秒数
 * format：日期字符格式（默认：yyyy-MM-dd HH:mm:ss），可随意定义，如：yyyy年MM月dd日
 */
function toDateString(time, format) {
    //若 null 或空字符，则返回空字符
    if (time === null || time === '') return '';
    var that = this
        , date = new Date(function () {
        if (!time) return;
        return isNaN(time) ? time : (typeof time === 'string' ? parseInt(time) : time)
    }() || new Date())
        , ymd = [
        that.digit(date.getFullYear(), 4)
        , that.digit(date.getMonth() + 1)
        , that.digit(date.getDate())
    ]
        , hms = [
        that.digit(date.getHours())
        , that.digit(date.getMinutes())
        , that.digit(date.getSeconds())
    ];

    if (!date.getDate()) {
        console.error('Invalid Msec for "util.toDateString(Msec)');
        return '';
    }

    format = format || 'yyyy-MM-dd HH:mm:ss';
    return format.replace(/yyyy/g, ymd[0])
        .replace(/MM/g, ymd[1])
        .replace(/dd/g, ymd[2])
        .replace(/HH/g, hms[0])
        .replace(/mm/g, hms[1])
        .replace(/ss/g, hms[2]);
}

/**
 * 数字前置补零(配合toDateString使用)
 * @param num
 * @param length
 * @returns {string}
 */
function digit(num, length) {
    var str = '';
    num = String(num);
    length = length || 2;
    for (var i = num.length; i < length; i++) {
        str += '0';
    }
    return num < Math.pow(10, length) ? str + (num | 0) : num;
}


/**
 * 去掉字符串的HTML标签
 * @param str
 * @returns {string}
 */
function filterHTMLTag(str) {
    let msg = str.replace(/<\/?[^>]*>/g, '');
    msg = msg.replace(/[|]*\n/, '');
    msg = msg.replace(/&nbsp;/ig, '');
    return msg;
}


/**
 * 保留小数位数
 * @param n 原数字
 * @param s 保存位数
 * @returns {string}
 */
function toFixed(n, s) {
    if (s > 6) {
        s = 6;
    }
    var changenum;
    if (n > 0) {
        changenum = (parseInt(n * Math.pow(10, s) + 0.5) / Math.pow(10, s)).toString();
    } else {
        changenum = (parseInt(n * Math.pow(10, s) - 0.5) / Math.pow(10, s)).toString();
    }

    var index = changenum.indexOf(".");
    if (index < 0 && s > 0) {
        changenum = changenum + ".";
        for (var i = 0; i < s; i++) {
            changenum = changenum + "0";
        }
    } else {
        index = changenum.length - index;
        for (var i = 0; i < (s - index) + 1; i++) {
            changenum = changenum + "0";
        }
    }
    return changenum;
}


/**
 * 校验电话号码是否正确
 * @param val
 * @returns {boolean}
 */
function checkTel(val) {
    var isPhone = /^([0-9]{3,4}-)?[0-9]{7,8}$/;
    var isMob = /^(?:(?:\+|00)86)?1(?:(?:3[\d])|(?:4[5-7|9])|(?:5[0-3|5-9])|(?:6[5-7])|(?:7[0-8])|(?:8[\d])|(?:9[1|8|9]))\d{8}$/;
    if (isMob.test(val) || isPhone.test(val)) {
        return true;
    } else {
        return false;
    }
}


/**
 * 数字转大写金额
 * @param n 数字 9101.11
 * @returns {string} 大写金额 玖千壹百零壹元壹角壹分
 */
function numToDX(n) {
    if (!/^(0|[1-9]\d*)(\.\d+)?$/.test(n))
        return "数据非法";
    let unit = "千百拾亿千百拾万千百拾元角分", str = "";
    n += "00";
    let p = n.indexOf('.');
    if (p >= 0)
        n = n.substring(0, p) + n.substr(p + 1, 2);
    unit = unit.substr(unit.length - n.length);
    for (let i = 0; i < n.length; i++)
        str += '零壹贰叁肆伍陆柒捌玖'.charAt(n.charAt(i)) + unit.charAt(i);
    return str.replace(/零(千|百|拾|角)/g, "零").replace(/(零)+/g, "零").replace(/零(万|亿|元)/g, "$1").replace(/(亿)万|壹(拾)/g, "$1$2").replace(/^元零?|零分/g, "").replace(/元$/g, "元整");
}


/**
 * 数字每三位加逗号
 * @param number 9999
 * @returns {string} 999,9
 */
function toThousands(number) {
    var num = (number || 0).toString(), result = '';
    while (num.length > 3) {
        result = ',' + num.slice(-3) + result;
        num = num.slice(0, num.length - 3);
    }
    if (num) {
        result = num + result;
    }
    return result;
}

/**
 * 根据参数名称获取url中参数值(会自动解码)
 * @param url https://translate.google.cn/?sl=zh-CN&tl=en&text=%E8%A1%A5%E8%B4%B4&op=translate
 * @param key text
 * @returns {string|null} 补贴
 */
function getUrlParamValue(url, key) {
    var reg = new RegExp("(^|&)" + key + "=([^&]*)(&|$)", "i");
    var r = url.match(reg);
    if (r != null) {
        return decodeURIComponent(r[2]);
    } else {
        return null;
    }
}

/**
 * 根据参数名称获取当前url：location.href中参数值(会自动解码)
 * @param key 参数键
 * @returns {string | null}
 */
function getUrlKeyValue(key) {
    return decodeURIComponent((new RegExp('[?|&]' + key + '=' + '([^&;]+?)(&|#|;|$)').exec(location.href) || [, ""])[1].replace(/\+/g, '%20')) || null
}

/**
 * 获取UUID
 * @returns {string}
 */
function uuid() {
    var s = [];
    var hexDigits = "0123456789abcdef";
    for (var i = 0; i < 36; i++) {
        s[i] = hexDigits.substr(Math.floor(Math.random() * 0x10), 1);
    }
    s[14] = "4";  // bits 12-15 of the time_hi_and_version field to 0010
    s[19] = hexDigits.substr((s[19] & 0x3) | 0x8, 1);  // bits 6-7 of the clock_seq_hi_and_reserved to 01
    s[8] = s[13] = s[18] = s[23] = "-";

    var uuid = s.join("");
    return uuid;
}


/**
 * table cell 中状态标识转为字典文本显示
 * @param dictCode
 * @param dictKey
 * @returns {string}
 */
function showDictValue(dictCode, dictKey) {
    const dictData = JSON.parse(sessionStorage.getItem("dictData"));
    const dict = dictData[dictCode];
    if (dict && dict.map && dict.map[dictKey]) {
        const {dictValue, background} = dict.map[dictKey];
        const style = background ? `layui-badge layui-bg-${background}` : "layui-badge-rim";
        return `<span class="${style}">${dictValue}</span>`;
    }
    return "";
}

/**
 * 根据 dictCode 动态生成 select 元素的 option 内容
 * @param dictCode
 * @returns {string}
 */
function generateSelectOptions(dictCode) {
    const dictData = JSON.parse(sessionStorage.getItem("dictData"));
    const dict = dictData[dictCode];
    if (!dict || !dict.list) {
        return '';
    }
    let options = '<option value="">请选择</option>';
    dict.list.forEach(item => {
        options += `<option value="${item.dictKey}">${item.dictValue}</option>`;
    });
    return options;
}

/**
 * 根据 dictCode 动态生成 select 元素的 option 内容
 * @param dictCode
 * @param target jQuery对象
 * @param hasEmpty 是否添加空选项，默认为 false
 */
function generateSelect(dictCode, target, hasEmpty = false) {
    const dictData = JSON.parse(sessionStorage.getItem("dictData"));
    const dict = dictData[dictCode];
    if (!dict || !dict.list) {
        return;
    }
    let options = '';
    if (hasEmpty) {
        options = '<option value="">请选择</option>';
    }
    dict.list.forEach(item => {
        options += `<option value="${item.dictKey}">${item.dictValue}</option>`;
    });
    target.append(options);
}


/**
 * 根据 dictCode 更新已有开关按钮的 value 和 title 属性
 * @param dictCode
 * @param inputElement jQuery 对象，要更新属性的 input 元素
 */
function updateSwitchAttributes(dictCode, inputElement) {
    const dictData = JSON.parse(sessionStorage.getItem("dictData"));
    const dict = dictData[dictCode];
    if (!dict || !dict.list || dict.list.length < 2) {
        return;
    }
    const firstItem = dict.list[0];
    const secondItem = dict.list[1];
    inputElement.attr({
        value: firstItem.dictKey,
        title: `${firstItem.dictValue}|${secondItem.dictValue}`
    });
}