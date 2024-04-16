package jp.co.bobb.common.util;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import java.util.Date;

/**
 * describe:
 *
 * @author Parker Sun
 * @date 2019/07/09
 */
@Slf4j
public class DateTimeDeserializers extends JsonDeserializer<Date> {
    @Override
    public Date deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) {
        try {
            if (StringUtils.isNotEmpty(jsonParser.getText())) {
                return DateUtil.parse(jsonParser.getText());
            } else {
                return null;
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Class<?> handledType() {
        return Date.class;
    }

}
