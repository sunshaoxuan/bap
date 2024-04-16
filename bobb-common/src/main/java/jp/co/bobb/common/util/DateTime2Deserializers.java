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
 * @date 2019/10/02
 */
@Slf4j
public class DateTime2Deserializers extends JsonDeserializer<Date> {
    @Override
    public Date deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) {
        try {

            if (StringUtils.isNotBlank(jsonParser.getText())) {
                return DateUtil.parse1(jsonParser.getText());
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
