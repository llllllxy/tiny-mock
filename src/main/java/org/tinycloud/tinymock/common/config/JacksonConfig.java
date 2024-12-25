package org.tinycloud.tinymock.common.config;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalTimeSerializer;
import org.springframework.boot.autoconfigure.jackson.Jackson2ObjectMapperBuilderCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.TimeZone;

/**
 * <p>
 * </p>
 *
 * @author liuxingyu01
 * @since 2024-12-25 09:55
 */
@Configuration
public class JacksonConfig {

    public static final String DEFAULT_DATE_FORMAT = "yyyy-MM-dd";
    public static final String DEFAULT_DATE_TIME_FORMAT = "yyyy-MM-dd HH:mm:ss";
    public static final String DATE_TIME_FORMAT_NO_SECOND = "yyyy-MM-dd HH:mm";
    public static final String DEFAULT_TIME_FORMAT = "HH:mm:ss";

    @Bean
    public Jackson2ObjectMapperBuilderCustomizer customizer() {
        return builder ->
                builder.simpleDateFormat(DEFAULT_DATE_TIME_FORMAT)
                        .timeZone(TimeZone.getTimeZone(ZoneId.systemDefault()))
                        // long类型转string， 前端处理Long类型，数值过大会丢失精度
                        .serializerByType(Long.class, ToStringSerializer.instance)
                        .serializerByType(Long.TYPE, ToStringSerializer.instance)
                        .serializerByType(BigInteger.class, ToStringSerializer.instance)
                        .serializerByType(BigDecimal.class, ToStringSerializer.instance)
                        // 若POJO对象的属性值为null，序列化时不进行显示（选配），
                        // .serializationInclusion(JsonInclude.Include.NON_NULL)
                        // 若POJO对象的属性值为""，序列化时不进行显示（选配）
                        //  .serializationInclusion(JsonInclude.Include.NON_EMPTY)
                        // 收到未知属性时不报异常
                        .failOnUnknownProperties(false)
                        // 忽略空对象，直接序列化为空的 {}
                        .failOnEmptyBeans(false)
                        // 允许单引号
                        .featuresToEnable(JsonParser.Feature.ALLOW_SINGLE_QUOTES)

                        // 指定反序列化类型，也可以使用@JsonFormat(pattern = "yyyy-MM-dd")替代。主要是mvc接收日期时使用
                        .deserializerByType(LocalTime.class, new LocalDateTimeDeserializer(DateTimeFormatter.ofPattern(DEFAULT_TIME_FORMAT)))
                        .deserializerByType(LocalDate.class, new LocalDateDeserializer(DateTimeFormatter.ofPattern(DEFAULT_DATE_FORMAT)))
                        .deserializerByType(LocalDateTime.class, new LocalDateTimeDeserializer(DateTimeFormatter.ofPattern(DEFAULT_DATE_TIME_FORMAT)))
                        // 日期序列化，主要返回数据时使用
                        .serializerByType(LocalTime.class, new LocalTimeSerializer(DateTimeFormatter.ofPattern(DEFAULT_TIME_FORMAT)))
                        .serializerByType(LocalDate.class, new LocalDateSerializer(DateTimeFormatter.ofPattern(DEFAULT_DATE_FORMAT)))
                        .serializerByType(LocalDateTime.class, new LocalDateTimeSerializer(DateTimeFormatter.ofPattern(DEFAULT_DATE_TIME_FORMAT)));
    }
}
