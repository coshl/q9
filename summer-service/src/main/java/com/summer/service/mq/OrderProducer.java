package com.summer.service.mq;

import com.summer.dao.entity.OrderBorrow;
import com.summer.dao.entity.UserInfo;
import com.summer.dao.entity.UserMoneyRate;
import com.summer.dao.mapper.IBackConfigParamsDao;
import com.summer.dao.mapper.OrderBorrowMapper;
import com.summer.dao.mapper.UserMoneyRateMapper;
import com.summer.enums.YesOrNo;
import com.summer.queue.QueueConstans;
import com.summer.service.impl.UserInfoService;
import com.summer.util.DateUtil;
import com.summer.util.RedisUtil;
import com.summer.pojo.vo.BackConfigParamsVo;
import lombok.extern.slf4j.Slf4j;
import org.apache.activemq.ScheduledMessage;
import org.apache.activemq.command.ActiveMQQueue;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.jms.JmsProperties;
import org.springframework.jms.core.JmsMessagingTemplate;
import org.springframework.stereotype.Service;
import javax.annotation.Resource;
import javax.jms.*;
import java.math.BigDecimal;
import java.util.*;

/**
 * 订单生产者
 */
@Service("producer")
@Slf4j
public class OrderProducer {
    @Autowired
    private JmsMessagingTemplate jmsMessagingTemplate;
    @Resource
    private IBackConfigParamsDao backConfigParamsDao;
    @Resource
    UserInfoService userInfoService;
    @Resource
    UserMoneyRateMapper userMoneyRateMapper;
    @Resource
    OrderBorrowMapper orderBorrowMapper;


    private static String SEND_LOAN = "SEND_LOAN";
    private static String AUTO_BORROW = "AUTO_BORROW";
    @Resource
    private RedisUtil redisUtil;

    /**
     * 发送新生成的订单进入消息队列
     *
     * @param message
     */
    public void sendNewOrder(final String message) {
        log.info("初审队列，开启事务,msg:{}", message);
        Destination destination = new ActiveMQQueue(QueueConstans.newOrderQueue);

        Connection connection = null;
        Session session = null;
        MessageProducer producer = null;
        // 获取连接工厂
        ConnectionFactory connectionFactory = jmsMessagingTemplate.getConnectionFactory();
        try {
            // 获取连接
            connection = connectionFactory.createConnection();
            connection.start();
            // 获取session，true开启事务，false关闭事务
            session = connection.createSession(Boolean.TRUE, Session.AUTO_ACKNOWLEDGE);
            // 创建一个消息队列
            producer = session.createProducer(destination);
            producer.setDeliveryMode(JmsProperties.DeliveryMode.PERSISTENT.getValue());
            ObjectMessage txMessage = session.createObjectMessage(message);
            //设置延迟时间
            txMessage.setLongProperty(ScheduledMessage.AMQ_SCHEDULED_DELAY, 0);
            // 发送消息
            producer.send(txMessage);
            session.commit();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (producer != null) {
                    producer.close();
                }
                if (session != null) {
                    session.close();
                }
                if (connection != null) {
                    connection.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }

    /**
     * 提供给复审
     *
     * @param message
     */
    public void sendRisk(final String message) {
        log.info("复审队列，开启事务----------order={}", message);
        Destination destination = new ActiveMQQueue(QueueConstans.riskQueue);
        Connection connection = null;
        Session session = null;
        MessageProducer producer = null;
        // 获取连接工厂
        ConnectionFactory connectionFactory = jmsMessagingTemplate.getConnectionFactory();
        try {
            // 获取连接
            connection = connectionFactory.createConnection();
            connection.start();
            // 获取session，true开启事务，false关闭事务
            session = connection.createSession(Boolean.TRUE, Session.AUTO_ACKNOWLEDGE);
            // 创建一个消息队列
            producer = session.createProducer(destination);
            producer.setDeliveryMode(JmsProperties.DeliveryMode.PERSISTENT.getValue());
            ObjectMessage txMessage = session.createObjectMessage(message);
            //设置延迟时间
            txMessage.setLongProperty(ScheduledMessage.AMQ_SCHEDULED_DELAY, 20000L);
            // 发送消息
            producer.send(txMessage);
            session.commit();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (producer != null) {
                    producer.close();
                }
                if (session != null) {
                    session.close();
                }
                if (connection != null) {
                    connection.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
    //TODO 00自动申请
    public void autoBorrowQueue(final String message) {
        log.info("自动申请----------order={}", message);
        String autoBorrowConfigTime = backConfigParamsDao.findStrValue("auto_borrow");
        Long autoBorrowConfigTimeLong = new BigDecimal(autoBorrowConfigTime).multiply(new BigDecimal(1000*60)).longValue();
        String dateFormatMin = DateUtil.getDateFormat(new Date(), "yyyy-MM-dd HH:mm");
        String minkey = AUTO_BORROW + dateFormatMin + message;
        long minIn = redisUtil.incr(minkey, 1);
        int timeIn = 60;
        boolean expireIn = redisUtil.expire(minkey, timeIn);
        if (!expireIn) {
            boolean expire1 = redisUtil.expire(minkey, timeIn);
            if (!expire1) {
                log.error("PRODUCE autoBorrowQueue expireIn error id=" + message);
            }
        }
        if (minIn > 1) {
            log.error("PRODUCE autoBorrowQueue FAIL,id={},minIn={}", message, minIn);
            return;
        }

        Destination destination = new ActiveMQQueue(QueueConstans.autoBorrowQueue);
        Connection connection = null;
        Session session = null;
        MessageProducer producer = null;
        // 获取连接工厂
        ConnectionFactory connectionFactory = jmsMessagingTemplate.getConnectionFactory();
        try {
            // 获取连接
            connection = connectionFactory.createConnection();
            connection.start();
            // 获取session，true开启事务，false关闭事务
            session = connection.createSession(Boolean.TRUE, Session.AUTO_ACKNOWLEDGE);
            // 创建一个消息队列
            producer = session.createProducer(destination);
            producer.setDeliveryMode(JmsProperties.DeliveryMode.PERSISTENT.getValue());
            ObjectMessage txMessage = session.createObjectMessage(message);
            //设置延迟时间
            log.info("自动下单延迟时间：{}",autoBorrowConfigTimeLong);
            txMessage.setLongProperty(ScheduledMessage.AMQ_SCHEDULED_DELAY, autoBorrowConfigTimeLong);
            // 发送消息
            producer.send(txMessage);
            session.commit();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (producer != null) {
                    producer.close();
                }
                if (session != null) {
                    session.close();
                }
                if (connection != null) {
                    connection.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }


        /*Destination destination = new ActiveMQQueue(QueueConstans.autoBorrowQueue);
        jmsMessagingTemplate.convertAndSend(destination, message);*/
    }

    //老客自动申请
    public void oldAuto(Integer userId)
    {
        BackConfigParamsVo oldAuto = backConfigParamsDao.findBySysKey("old_auto");
        if (oldAuto.getStrValue().equals(YesOrNo.YES.getStrValue()))//老客自动申请开关
        {
            UserInfo userInfo =   userInfoService.selectByPrimaryKey(userId);
            if (Objects.nonNull(userInfo))
            {
                UserMoneyRate userMoneyRate = userMoneyRateMapper.findByUserId(userInfo.getId());
                Map<String,Object> param = new HashMap<>();
                param.put("userId",userInfo.getId());
                param.put("status",10);//还款
                List<OrderBorrow> list = orderBorrowMapper.selectByParams(param);
                if (userMoneyRate.getRepetitionTimes() == 1 && list.size() == 1)//老客第一次还款以后,只有一个全额还款订单
                {
                    log.info("老客自动下单userId:{}",userInfo.getId());
                    autoBorrowQueue(userInfo.getId()+"");
                }
            }
        }

    }

    /**
     * 提供给放款
     */
    public void sendLoan(final String data, Long time) {
        log.info("sendLoan borrowId=" + data);
        String dateFormatMin = DateUtil.getDateFormat(new Date(), "yyyy-MM-dd HH:mm");
        String minkey = SEND_LOAN + dateFormatMin + data;
        long minIn = redisUtil.incr(minkey, 1);
        int timeIn = 60;
        boolean expireIn = redisUtil.expire(minkey, timeIn);
        if (!expireIn) {
            boolean expire1 = redisUtil.expire(minkey, timeIn);
            if (!expire1) {
                log.error("PRODUCE SEND_LOAN expireIn error id=" + data);
            }
        }
        if (minIn > 1) {
            log.error("PRODUCE SEND_LOAN FAIL,id={},minIn={}", data, minIn);
            return;
        }
        log.info("PRODUCE SEND_LOAN SUC,id={},minIn={}", data, minIn);
        boolean noDelay = false;
        if (null == time) {
            Integer late_min = 10;
            BackConfigParamsVo scoreType = backConfigParamsDao.findBySysKey("late_min");
            if (null != scoreType && scoreType.getSysValue() >= 0) {
                late_min = scoreType.getSysValue();
                if (scoreType.getSysValue() == 0) {
                    noDelay = true;
                }
            }
            time = Long.valueOf(late_min * 60000);
            if (noDelay) {
                time = 10L;
            }
        }
        Destination destination = new ActiveMQQueue(QueueConstans.loanQueues);
        Connection connection = null;
        Session session = null;
        MessageProducer producer = null;
        // 获取连接工厂
        ConnectionFactory connectionFactory = jmsMessagingTemplate.getConnectionFactory();
        try {
            // 获取连接
            connection = connectionFactory.createConnection();
            connection.start();
            // 获取session，true开启事务，false关闭事务
            session = connection.createSession(Boolean.TRUE, Session.AUTO_ACKNOWLEDGE);
            // 创建一个消息队列
            producer = session.createProducer(destination);
            producer.setDeliveryMode(JmsProperties.DeliveryMode.PERSISTENT.getValue());
            ObjectMessage message = session.createObjectMessage(data);
            //设置延迟时间
            message.setLongProperty(ScheduledMessage.AMQ_SCHEDULED_DELAY, time);
            // 发送消息
            producer.send(message);
            session.commit();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (producer != null) {
                    producer.close();
                }
                if (session != null) {
                    session.close();
                }
                if (connection != null) {
                    connection.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * otc用户确认
     */
    public void sendConfirm(final String data, Long time) {
        log.info("sendConfirm borrowId=" + data);

        Destination destination = new ActiveMQQueue("otcConfirmQueue");
        Connection connection = null;
        Session session = null;
        MessageProducer producer = null;
        // 获取连接工厂
        ConnectionFactory connectionFactory = jmsMessagingTemplate.getConnectionFactory();
        try {
            // 获取连接
            connection = connectionFactory.createConnection();
            connection.start();
            // 获取session，true开启事务，false关闭事务
            session = connection.createSession(Boolean.TRUE, Session.AUTO_ACKNOWLEDGE);
            // 创建一个消息队列
            producer = session.createProducer(destination);
            producer.setDeliveryMode(JmsProperties.DeliveryMode.PERSISTENT.getValue());
            ObjectMessage message = session.createObjectMessage(data);
            //设置延迟时间
            message.setLongProperty(ScheduledMessage.AMQ_SCHEDULED_DELAY, time);
            // 发送消息
            producer.send(message);
            session.commit();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (producer != null) {
                    producer.close();
                }
                if (session != null) {
                    session.close();
                }
                if (connection != null) {
                    connection.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }


    /**
     * 发送短信验证码
     *
     * */
    public void createSendSmsCodeTask(String data)
    {
        log.info("添加发送短信验证码队列任务:{}",data);
        Destination destination = new ActiveMQQueue(QueueConstans.sendSmsCodeQueue);
        jmsMessagingTemplate.convertAndSend(destination, data);
    }

    public void sendNotify(final String data) {
        log.info("sendNotify borrowId=" + data);
        Integer late_min = 10;

        Long time = Long.valueOf(late_min * 60000);
        Destination destination = new ActiveMQQueue("notifyQueue");
        Connection connection = null;
        Session session = null;
        MessageProducer producer = null;
        // 获取连接工厂
        ConnectionFactory connectionFactory = jmsMessagingTemplate.getConnectionFactory();
        try {
            // 获取连接
            connection = connectionFactory.createConnection();
            connection.start();
            // 获取session，true开启事务，false关闭事务
            session = connection.createSession(Boolean.TRUE, Session.AUTO_ACKNOWLEDGE);
            // 创建一个消息队列
            producer = session.createProducer(destination);
            producer.setDeliveryMode(JmsProperties.DeliveryMode.PERSISTENT.getValue());
            ObjectMessage message = session.createObjectMessage(data);
            //设置延迟时间
            message.setLongProperty(ScheduledMessage.AMQ_SCHEDULED_DELAY, time);
            // 发送消息
            producer.send(message);
            session.commit();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (producer != null) {
                    producer.close();
                }
                if (session != null) {
                    session.close();
                }
                if (connection != null) {
                    connection.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 发送新生成的订单进入消息队列
     *
     * @param message
     */
    public void sendmanualLoan(final String message) {
        Destination destination = new ActiveMQQueue("sendManualLoan");
        jmsMessagingTemplate.convertAndSend(destination, message);
    }
}
