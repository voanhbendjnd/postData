package com.djnd.post_data.config;

import org.springframework.format.FormatterRegistry;
import org.springframework.format.datetime.standard.DateTimeFormatterRegistrar;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.lang.NonNull;

// phân tích chuẩn giờ iso
public class DateTimeFormatConfig implements WebMvcConfigurer {
    @Override
    public void addFormatters(@NonNull FormatterRegistry registry) {
        DateTimeFormatterRegistrar registar = new DateTimeFormatterRegistrar();
        registar.setUseIsoFormat(true);
        registar.registerFormatters(registry);
    }
}
