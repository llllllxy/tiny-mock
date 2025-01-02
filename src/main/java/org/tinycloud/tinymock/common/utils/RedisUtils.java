package org.tinycloud.tinymock.common.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.core.*;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.*;
import java.util.concurrent.TimeUnit;

/**
 * Redis工具类
 *
 * @author liuxingyu01
 * @since 2021-09-13-13:39
 **/
public class RedisUtils {
    private final static Logger log = LoggerFactory.getLogger(RedisUtils.class);

    private final RedisTemplate<String, Object> redisTemplate;

    public RedisUtils(RedisTemplate<String, Object> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    /*============================Common Start=============================*/

    /**
     * 指定缓存失效时间
     *
     * @param key  键
     * @param time 时间(秒)
     * @return
     */
    public boolean expire(final String key, final long time) {
        if (StringUtils.isEmpty(key)) {
            return false;
        }
        try {
            if (time > 0) {
                redisTemplate.expire(key, time, TimeUnit.SECONDS);
            }
            return true;
        } catch (Exception e) {
            log.error("RedisUtil - expire - 设置缓存时间失败，Exception：{e}", e);
            return false;
        }
    }


    /**
     * 指定缓存失效时间
     *
     * @param key  键
     * @param time 时间
     * @param unit 时间类型
     * @return
     */
    public boolean expire(final String key, final long time, final TimeUnit unit) {
        if (StringUtils.isEmpty(key)) {
            return false;
        }
        try {
            if (time > 0) {
                redisTemplate.expire(key, time, unit);
            }
            return true;
        } catch (Exception e) {
            log.error("RedisUtil - expire - 设置缓存时间失败，Exception：{e}", e);
            return false;
        }
    }


    /**
     * 根据key 获取过期时间
     *
     * @param key 键 不能为null
     * @return 时间(秒) 返回0代表为永久有效
     */
    public long getExpire(final String key) {
        try {
            return redisTemplate.getExpire(key, TimeUnit.SECONDS);
        } catch (Exception e) {
            log.error("RedisUtil - getExpire - 获取过期时间失败，Exception：{e}", e);
            return 0;
        }
    }


    /**
     * 判断key是否存在
     *
     * @param key 键
     * @return true 存在 false不存在
     */
    public boolean hasKey(final String key) {
        try {
            return redisTemplate.hasKey(key);
        } catch (Exception e) {
            log.error("RedisUtil - hasKey - 判断key是否存在失败，Exception：{e}", e);
            return false;
        }
    }


    /**
     * 修改 key 的名称
     *
     * @param oldKey 旧名字
     * @param newKey 新名字
     */
    public void rename(final String oldKey, final String newKey) {
        redisTemplate.rename(oldKey, newKey);
    }


    /**
     * 删除缓存
     *
     * @param key 可以传一个值 或多个
     */
    public void del(String... key) {
        if (key != null && key.length > 0) {
            if (key.length == 1) {
                redisTemplate.delete(key[0]);
            } else {
                redisTemplate.delete(Arrays.asList(key));
            }
        }
    }


    /**
     * 模糊查询获取key值
     * keys命令可能会阻塞服务器，当数据库比较大时，不建议使用
     *
     * @param pattern 传入""查询所有key， 传入"shiro:test:" 查询所有这个开头的key
     * @return key列表
     */
    public Set<String> keys(final String pattern) {
        return redisTemplate.keys(pattern.concat("*"));
    }


    /**
     * scan 实现（分段获取 key，用于替代keys命令）
     *
     * @param pattern 传入""查询所有key， 传入"shiro:test:" 查询所有这个开头的key
     * @param limit   scan命令每次操作查询的数量
     * @return key列表
     */
    public Set<String> scan(String pattern, Long limit) {
        HashSet<String> set = new HashSet<>();
        if (limit == null) {
            limit = 1000L;
        }
        if (pattern == null || pattern.isEmpty()) {
            return set;
        } else {
            pattern = pattern.concat("*");
        }
        Cursor<String> cursor = this.scan(redisTemplate, pattern, limit);
        if (cursor != null) {
            while (cursor.hasNext()) {
                set.add(cursor.next());
            }
            try {
                cursor.close();
            } catch (Exception e) {
                log.error("关闭 redis cursor 失败");
            }
        }
        return set;
    }

    /**
     * 自定义 redis scan 操作
     *
     * @return Cursor
     */
    @SuppressWarnings("unchecked")
    private Cursor<String> scan(RedisTemplate<String, Object> redisTemplate, String pattern, Long limit) {
        ScanOptions options = ScanOptions.scanOptions().match(pattern).count(limit).build();
        RedisSerializer<String> redisSerializer = (RedisSerializer<String>) redisTemplate.getKeySerializer();
        return (Cursor<String>) redisTemplate.executeWithStickyConnection(new RedisCallback() {
            @Override
            public Object doInRedis(RedisConnection redisConnection)
                    throws org.springframework.dao.DataAccessException {
                return new ConvertingCursor<>(redisConnection.scan(options), redisSerializer::deserialize);
            }
        });
    }
    /*============================Common End=============================*/


    /*============================String Start=============================*/

    /**
     * 普通缓存获取
     *
     * @param key 键
     * @return 值
     */
    public Object get(final String key) {
        return key == null ? null : redisTemplate.opsForValue().get(key);
    }


    /**
     * 普通缓存获取（直接转字符串）
     *
     * @param key 键
     * @return 值，返回String
     */
    public String getStr(String key) {
        if (key == null) {
            return null;
        }
        Object obj = redisTemplate.opsForValue().get(key);
        return obj == null ? null : obj.toString();
    }


    /**
     * 批量获取缓存
     *
     * @param keys Collection<key>
     * @return List<Object>
     */
    public List<Object> multiGet(Collection<String> keys) {
        if (CollectionUtils.isEmpty(keys)) {
            return null;
        } else {
            return redisTemplate.opsForValue().multiGet(keys);
        }
    }


    /**
     * 普通缓存放入（永不失效）
     *
     * @param key   键
     * @param value 值
     * @return true成功 false失败
     */
    public boolean set(final String key, final Object value) {
        try {
            redisTemplate.opsForValue().set(key, value);
            return true;
        } catch (Exception e) {
            log.error("RedisUtil - set - 存入redis失败，Exception：{e}", e);
            return false;
        }
    }


    /**
     * 普通缓存放入并设置失效时间
     *
     * @param key   键
     * @param value 值
     * @param time  时间(秒) time要大于0 如果time小于等于0 将设置无限期
     * @return true成功 false 失败
     */
    public boolean set(String key, Object value, long time) {
        try {
            if (time > 0) {
                redisTemplate.opsForValue().set(key, value, time, TimeUnit.SECONDS);
            } else {
                set(key, value);
            }
            return true;
        } catch (Exception e) {
            log.error("RedisUtil - set - 存入redis失败，Exception：{e}", e);
            return false;
        }
    }


    /**
     * 普通缓存放入并设置时间
     *
     * @param key      键
     * @param value    值
     * @param time     时间 time要大于0 如果time小于等于0 将为无限期
     * @param timeUnit 时间单位TimeUnit
     *                 TimeUnit.DAYS          天
     *                 TimeUnit.HOURS         小时
     *                 TimeUnit.MINUTES       分钟
     *                 TimeUnit.SECONDS       秒
     *                 TimeUnit.MILLISECONDS  毫秒
     * @return true成功 false 失败
     */
    public boolean set(String key, Object value, long time, TimeUnit timeUnit) {
        try {
            if (time > 0) {
                redisTemplate.opsForValue().set(key, value, time, timeUnit);
            } else {
                set(key, value);
            }
            return true;
        } catch (Exception e) {
            log.error("RedisUtil - set - 存入redis失败，Exception：{e}", e);
            return false;
        }
    }

    /**
     * 批量写入数据
     *
     * @param map Map
     * @return true成功 false 失败
     */
    public boolean multiSet(Map<String, Object> map) {
        if (CollectionUtils.isEmpty(map)) {
            return false;
        }
        try {
            redisTemplate.opsForValue().multiSet(map);
            return true;
        } catch (Exception e) {
            log.error("RedisUtil - multiSet - 批量存入redis失败，Exception：{e}", e);
        }
        return false;
    }


    /**
     * 根据key更新数据
     *
     * @param key   键
     * @param value 值
     * @return true成功 false 失败
     */
    public boolean update(final String key, Object value) {
        if (StringUtils.isEmpty(key)) {
            return false;
        }
        try {
            redisTemplate.opsForValue().getAndSet(key, value);
            return true;
        } catch (Exception e) {
            log.error("RedisUtil - update - 更新redis失败，Exception：{e}", e);
        }
        return false;
    }


    /**
     * 递增（将key所储存的值加上增量 increment；如果key不存在，那么key的值会先被初始化为0）
     *
     * @param key   键
     * @param delta 要增加几(大于0)
     * @return 自增后的值
     */
    public long incr(String key, long delta) {
        if (delta < 0) {
            throw new RuntimeException("递增因子必须大于0");
        }
        return redisTemplate.opsForValue().increment(key, delta);
    }


    /**
     * 递减
     *
     * @param key   键
     * @param delta 要减少几
     * @return 自减后的值
     */
    public long decr(String key, long delta) {
        if (delta < 0) {
            throw new RuntimeException("递减因子必须大于0");
        }
        return redisTemplate.opsForValue().increment(key, -delta);
    }
    /*============================String end=============================*/


    /*================================Map==============================*/

    /**
     * HashGet
     *
     * @param key  键 不能为null
     * @param item 项 不能为null
     * @return 值
     */
    public Object hGet(String key, String item) {
        return redisTemplate.opsForHash().get(key, item);
    }

    /**
     * 获取hashKey对应的所有键值
     *
     * @param key 键
     * @return 对应的多个键值
     */
    public Map<Object, Object> hmGet(String key) {
        return redisTemplate.opsForHash().entries(key);
    }

    /**
     * HashSet
     *
     * @param key 键
     * @param map 对应多个键值
     * @return true 成功 false 失败
     */
    public boolean hmSet(String key, Map<String, Object> map) {
        try {
            redisTemplate.opsForHash().putAll(key, map);
            return true;
        } catch (Exception e) {
            log.error("RedisUtil - hmset - Exception：{e}", e);
            return false;
        }
    }

    /**
     * HashSet 并设置时间
     *
     * @param key  键
     * @param map  对应多个键值
     * @param time 时间(秒)
     * @return true成功 false失败
     */
    public boolean hmSet(String key, Map<String, Object> map, long time) {
        try {
            redisTemplate.opsForHash().putAll(key, map);
            if (time > 0) {
                expire(key, time);
            }
            return true;
        } catch (Exception e) {
            log.error("RedisUtil - hmset - Exception：{e}", e);
            return false;
        }
    }

    /**
     * 向一张hash表中放入数据,如果不存在将创建
     *
     * @param key     键
     * @param hashKey 项
     * @param value   值
     * @return true 成功 false失败
     */
    public boolean hSet(String key, String hashKey, Object value) {
        try {
            redisTemplate.opsForHash().put(key, hashKey, value);
            return true;
        } catch (Exception e) {
            log.error("RedisUtil - hset - Exception：{e}", e);
            return false;
        }
    }

    /**
     * 向一张hash表中放入数据,如果不存在将创建
     *
     * @param key     键
     * @param hashKey 项
     * @param value   值
     * @param time    时间(秒)  注意:如果已存在的hash表有时间,这里将会替换原有的时间
     * @return true 成功 false失败
     */
    public boolean hSet(String key, String hashKey, Object value, long time) {
        try {
            redisTemplate.opsForHash().put(key, hashKey, value);
            if (time > 0) {
                expire(key, time);
            }
            return true;
        } catch (Exception e) {
            log.error("RedisUtil - hset - Exception：{e}", e);
            return false;
        }
    }

    /**
     * 删除hash表中的值
     *
     * @param key      键 不能为null
     * @param hashKeys 项 可以使多个 不能为null
     */
    public void hDel(String key, Object... hashKeys) {
        redisTemplate.opsForHash().delete(key, hashKeys);
    }

    /**
     * 判断hash表中是否有该项的值
     *
     * @param key     键 不能为null
     * @param hashKey 项 不能为null
     * @return true 存在 false不存在
     */
    public boolean hHasKey(String key, String hashKey) {
        return redisTemplate.opsForHash().hasKey(key, hashKey);
    }

    /**
     * hash递增 如果不存在,就会创建一个 并把新增后的值返回
     *
     * @param key  键
     * @param item 项
     * @param by   要增加几(大于0)
     * @return 自增后的值
     */
    public double hIncr(String key, String item, long by) {
        return redisTemplate.opsForHash().increment(key, item, by);
    }

    /**
     * hash递减
     *
     * @param key  键
     * @param item 项
     * @param by   要减少几
     * @return 自减后的值
     */
    public double hDecr(String key, String item, long by) {
        return redisTemplate.opsForHash().increment(key, item, -by);
    }
    /*================================Map  end====================*/


    /*============================set=============================*/

    /**
     * 根据key获取Set中的所有值
     *
     * @param key 键
     * @return
     */
    public Set<Object> sGet(String key) {
        try {
            return redisTemplate.opsForSet().members(key);
        } catch (Exception e) {
            log.error("RedisUtil - sGet - Exception：{e}", e);
            return null;
        }
    }

    /**
     * 根据value从一个set中查询,是否存在
     *
     * @param key   键
     * @param value 值
     * @return true 存在 false不存在
     */
    public boolean sHasKey(String key, Object value) {
        try {
            return Boolean.TRUE.equals(redisTemplate.opsForSet().isMember(key, value));
        } catch (Exception e) {
            log.error("RedisUtil - sHasKey - Exception：{e}", e);
            return false;
        }
    }

    /**
     * 将数据放入set缓存
     *
     * @param key    键
     * @param values 值 可以是多个
     * @return 成功个数
     */
    public long sSet(String key, Object... values) {
        try {
            return redisTemplate.opsForSet().add(key, values);
        } catch (Exception e) {
            log.error("RedisUtil - sSet - Exception：{e}", e);
            return 0;
        }
    }

    /**
     * 将set数据放入缓存
     *
     * @param key    键
     * @param time   时间(秒)
     * @param values 值 可以是多个
     * @return 成功个数
     */
    public long sSetAndTime(String key, long time, Object... values) {
        try {
            Long count = redisTemplate.opsForSet().add(key, values);
            if (time > 0) {
                expire(key, time);
            }
            return count;
        } catch (Exception e) {
            log.error("RedisUtil - sSetAndTime - Exception：{e}", e);
            return 0;
        }
    }

    /**
     * 获取set缓存的长度
     *
     * @param key 键
     * @return
     */
    public long sGetSetSize(String key) {
        try {
            return redisTemplate.opsForSet().size(key);
        } catch (Exception e) {
            log.error("RedisUtil - sGetSetSize - Exception：{e}", e);
            return 0;
        }
    }

    /**
     * 移除值为value的
     *
     * @param key    键
     * @param values 值 可以是多个
     * @return 移除的个数
     */
    public long setRemove(String key, Object... values) {
        try {
            return redisTemplate.opsForSet().remove(key, values);
        } catch (Exception e) {
            log.error("RedisUtil - setRemove - Exception：{e}", e);
            return 0;
        }
    }
    /*============================set end=============================*/


    /*===============================list=============================*/

    /**
     * 获取list缓存的内容
     *
     * @param key   键
     * @param start 开始
     * @param end   结束  0 到 -1代表所有值
     * @return
     */
    public List<Object> lGet(String key, long start, long end) {
        try {
            return redisTemplate.opsForList().range(key, start, end);
        } catch (Exception e) {
            log.error("RedisUtil - lGet - Exception：{e}", e);
            return null;
        }
    }

    /**
     * 获取list缓存的长度
     *
     * @param key 键
     * @return
     */
    public long lGetListSize(String key) {
        try {
            return redisTemplate.opsForList().size(key);
        } catch (Exception e) {
            log.error("RedisUtil - lGetListSize - Exception：{e}", e);
            return 0;
        }
    }

    /**
     * 通过索引 获取list中的值
     *
     * @param key   键
     * @param index 索引  index>=0时， 0 表头，1 第二个元素，依次类推；index<0时，-1，表尾，-2倒数第二个元素，依次类推
     * @return
     */
    public Object lGetIndex(String key, long index) {
        try {
            return redisTemplate.opsForList().index(key, index);
        } catch (Exception e) {
            log.error("RedisUtil - lGetIndex - Exception：{e}", e);
            return null;
        }
    }


    /**
     * 根据索引修改list中的某条数据
     *
     * @param key   键
     * @param index 索引
     * @param value 值
     * @return
     */
    public boolean lUpdateIndex(String key, long index, Object value) {
        try {
            redisTemplate.opsForList().set(key, index, value);
            return true;
        } catch (Exception e) {
            log.error("RedisUtil - lUpdateIndex - Exception：{e}", e);
            return false;
        }
    }


    /**
     * 将list放入缓存
     *
     * @param key   键
     * @param value 值
     * @return
     */
    public boolean lRightPush(String key, Object value) {
        try {
            redisTemplate.opsForList().rightPush(key, value);
            return true;
        } catch (Exception e) {
            log.error("RedisUtil - lRightPush - Exception：{e}", e);
            return false;
        }
    }

    /**
     * 将list放入缓存
     *
     * @param key   键
     * @param value 值
     * @param time  时间(秒)
     * @return
     */
    public boolean lRightPush(String key, Object value, long time) {
        try {
            redisTemplate.opsForList().rightPush(key, value);
            if (time > 0) {
                expire(key, time);
            }
            return true;
        } catch (Exception e) {
            log.error("RedisUtil - lRightPush - Exception：{e}", e);
            return false;
        }
    }

    /**
     * 将list放入缓存
     *
     * @param key   键
     * @param value 值
     * @return
     */
    public boolean lRightPushAll(String key, List<Object> value) {
        try {
            redisTemplate.opsForList().rightPushAll(key, value);
            return true;
        } catch (Exception e) {
            log.error("RedisUtil - lRightPushAll - Exception：{e}", e);
            return false;
        }
    }

    /**
     * 将list放入缓存
     *
     * @param key   键
     * @param value 值
     * @param time  时间(秒)
     * @return
     */
    public boolean lRightPushAll(String key, List<Object> value, long time) {
        try {
            redisTemplate.opsForList().rightPushAll(key, value);
            if (time > 0) {
                expire(key, time);
            }
            return true;
        } catch (Exception e) {
            log.error("RedisUtil - lRightPushAll - Exception：{e}", e);
            return false;
        }
    }


    /**
     * 将指定的值插入存储在键的列表的头部。
     * 如果键不存在，则在执行推送操作之前将其创建为空列表。（从左边插入）
     *
     * @param key   键
     * @param value 值
     * @return Long 返回的结果为推送操作后的列表的长度
     */
    public Long lLeftPush(String key, Object value) {
        try {
            return redisTemplate.opsForList().leftPush(key, value);
        } catch (Exception e) {
            log.error("RedisUtil - lLeftPush - Exception：{e}", e);
            return 0L;
        }
    }

    /**
     * 将指定的值插入存储在键的列表的头部。
     * 如果键不存在，则在执行推送操作之前将其创建为空列表。（从左边插入）
     *
     * @param key   键
     * @param value 值
     * @param time  过期时间(秒)
     * @return Long 返回的结果为推送操作后的列表的长度
     */
    public Long lLeftPush(String key, Object value, long time) {
        try {
            Long num = redisTemplate.opsForList().leftPush(key, value);
            if (time > 0) {
                expire(key, time);
            }
            return num;
        } catch (Exception e) {
            log.error("RedisUtil - lLeftPush - Exception：{e}", e);
            return 0L;
        }
    }


    /**
     * 将所有指定的值插入存储在键的列表的头部。
     * 如果键不存在，则在执行推送操作之前将其创建为空列表。（从左边插入）
     *
     * @param key   键
     * @param value 值
     * @return Long 返回的结果为推送操作后的列表的长度
     */
    public Long lLeftPushAll(String key, List<Object> value) {
        try {
            return redisTemplate.opsForList().leftPushAll(key, value);
        } catch (Exception e) {
            log.error("RedisUtil - lLeftPushAll - Exception：{e}", e);
            return 0L;
        }
    }

    /**
     * 将所有指定的值插入存储在键的列表的头部。
     * 如果键不存在，则在执行推送操作之前将其创建为空列表。（从左边插入）
     *
     * @param key   键
     * @param value 值
     * @param time  过期时间(秒)
     * @return Long 返回的结果为推送操作后的列表的长度
     */
    public Long lLeftPushAll(String key, List<Object> value, long time) {
        try {
            Long num = redisTemplate.opsForList().leftPushAll(key, value);
            if (time > 0) {
                expire(key, time);
            }
            return num;
        } catch (Exception e) {
            log.error("RedisUtil - lLeftPushAll - Exception：{e}", e);
            return 0L;
        }
    }


    /**
     * 移除N个值为value
     *
     * @param key   键
     * @param count 移除多少个
     * @param value 值
     * @return 移除的个数
     */
    public long lRemove(String key, long count, Object value) {
        try {
            return redisTemplate.opsForList().remove(key, count, value);
        } catch (Exception e) {
            log.error("RedisUtil - lRemove - Exception：{e}", e);
            return 0;
        }
    }

    /**
     * 弹出最左边的元素并返回，弹出之后该值在列表中将不复存在
     *
     * @param key 键
     * @return Object 弹出的元素
     */
    public Object lLeftPop(String key) {
        try {
            return redisTemplate.opsForList().leftPop(key);
        } catch (Exception e) {
            log.error("RedisUtils - lLeftPop - Exception：{e}", e);
            return null;
        }
    }

    /**
     * 弹出最右边的元素并返回，弹出之后该值在列表中将不复存在
     *
     * @param key 键
     * @return Object 弹出的元素
     */
    public Object lRightPop(String key) {
        try {
            return redisTemplate.opsForList().rightPop(key);
        } catch (Exception e) {
            log.error("RedisUtils - lRightPop - Exception：{e}", e);
            return null;
        }
    }

    /*===============================list  end=============================*/


    /**
     * 使用Redis的消息队列
     *
     * @param channel
     * @param message 消息内容
     */
    public void convertAndSend(String channel, Object message) {
        redisTemplate.convertAndSend(channel, message);
    }


    //=========BoundListOperations 用法 start============

    /**
     * 将数据添加到Redis的list中（从右边添加）
     *
     * @param listKey
     * @param time     过期时间
     * @param timeUnit 时间单位
     * @param values   待添加的数据
     */
    public void addToListRight(String listKey, Long time, TimeUnit timeUnit, Object... values) {
        //绑定操作
        BoundListOperations<String, Object> boundValueOperations = redisTemplate.boundListOps(listKey);
        //插入数据
        boundValueOperations.rightPushAll(values);
        //设置过期时间
        boundValueOperations.expire(time, timeUnit);
    }

    /**
     * 根据起始结束序号遍历Redis中的list
     *
     * @param listKey
     * @param start   起始序号
     * @param end     结束序号
     * @return
     */
    public List<Object> rangeList(String listKey, long start, long end) {
        //绑定操作
        BoundListOperations<String, Object> boundValueOperations = redisTemplate.boundListOps(listKey);
        //查询数据
        return boundValueOperations.range(start, end);
    }

    /**
     * 弹出右边的值 --- 并且移除这个值
     *
     * @param listKey
     */
    public Object rifhtPop(String listKey) {
        //绑定操作
        BoundListOperations<String, Object> boundValueOperations = redisTemplate.boundListOps(listKey);
        return boundValueOperations.rightPop();
    }

    //=========BoundListOperations 用法 End============
}