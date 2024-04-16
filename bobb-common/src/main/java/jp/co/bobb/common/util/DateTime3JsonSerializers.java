package jp.co.bobb.common.util;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

import java.io.IOException;
import java.time.LocalDate;

/**
 * describe:
 *
 * @author Parker Sun
 * @date 2019/11/13
 */
public class DateTime3JsonSerializers extends JsonSerializer<LocalDate> {
    @Override
    public void serialize(LocalDate date, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {
        jsonGenerator.writeString(date.format(DateUtil.dtf1));
    }
}
