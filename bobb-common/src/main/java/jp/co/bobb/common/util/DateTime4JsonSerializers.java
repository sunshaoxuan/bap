package jp.co.bobb.common.util;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

import java.io.IOException;
import java.util.Date;
import java.util.Optional;

/**
 * describe:
 *
 * @author Parker Sun
 * @date 2020/06/22
 */
public class DateTime4JsonSerializers extends JsonSerializer<Date> {
    @Override
    public void serialize(Date date, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {
        jsonGenerator.writeString(Optional.ofNullable(date)
                .map(d -> DateUtil.format(d, DateUtil.dtf6))
                .orElse(""));
    }
}
