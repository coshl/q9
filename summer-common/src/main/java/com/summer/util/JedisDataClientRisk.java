package com.summer.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

/**
 * redis操作
 *
 * @author gaoyuhai
 */
public class JedisDataClientRisk {
    Logger logger = LoggerFactory.getLogger(getClass());
    private static JedisPool poolMaster;
    public static JedisDataClientRisk jedisDataClientRisk = new JedisDataClientRisk();

    private JedisDataClientRisk() {
        String redisHostMaster = null;
        int redisPort;
        int timeout;
        redisHostMaster = RedisConfigConstantRisk.getConstant("spring.redis.host").trim();
        redisPort = Integer.parseInt(RedisConfigConstantRisk.getConstant("spring.redis.port").trim());
        timeout = Integer.parseInt(RedisConfigConstantRisk.getConstant("spring.redis.timeout").trim());
        logger.info(">>>>>>>>>redisHostMaster ip:" + redisHostMaster);
        logger.info(">>>>>>>>>redis port:" + redisPort);
        logger.info(">>>>>>>>>redis timeout:" + timeout);
        JedisPoolConfig configMaster = new JedisPoolConfig();
        configMaster.setMaxTotal(30000);// 设置最大连接数
        configMaster.setMaxIdle(100); // 设置最大空闲数
        configMaster.setMinIdle(10);
        configMaster.setMaxWaitMillis(10000);// 设置超时时间
        configMaster.setTestWhileIdle(true);
        configMaster.setTestOnBorrow(false);
        poolMaster = new JedisPool(configMaster, redisHostMaster, redisPort, timeout);

    }

    public synchronized Jedis getMasterJedis() {
        Jedis jedis = null;
        if (poolMaster != null) {
            jedis = poolMaster.getResource();
            jedis.auth(RedisConfigConstantRisk.getConstant("spring.redis.password").trim());
        }
        return jedis;
    }

    public String rPop(String key) {
        String value = null;
        Jedis jedis = null;
        try {
            jedis = getMasterJedis();
            value = jedis.rpop(key);
        } catch (Exception e) {
            logger.error("rPop error = " + key, e);
        } finally {
            returnResource(jedis);
            return value;
        }
    }

    public String get(String key) {
        String value = null;
        Jedis jedis = null;
        try {
            jedis = getMasterJedis();
            value = jedis.get(key);
        } catch (Exception e) {
            logger.error("rPop error = " + key, e);
        } finally {
            returnResource(jedis);
            return value;
        }

    }

    public void lPush(String key, String values) {
        Jedis jedis = null;
        try {
            jedis = getMasterJedis();
            jedis.lpush(key, values);
        } catch (Exception e) {
            logger.error("rPop error = " + key, e);
        } finally {
            returnResource(jedis);
        }
    }

    private void returnResource(Jedis jedis) {
        if (jedis != null && poolMaster != null) {
            jedis.disconnect();
            poolMaster.returnResourceObject(jedis);
        }
    }

}
