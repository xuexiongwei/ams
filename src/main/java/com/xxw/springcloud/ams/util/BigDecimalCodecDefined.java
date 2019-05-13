package com.xxw.springcloud.ams.util;

import java.io.IOException;
import java.lang.reflect.Type;
import java.math.BigDecimal;
import java.text.DecimalFormat;

import com.alibaba.fastjson.serializer.BigDecimalCodec;
import com.alibaba.fastjson.serializer.JSONSerializer;
import com.alibaba.fastjson.serializer.SerializeWriter;
import com.alibaba.fastjson.serializer.SerializerFeature;

public class BigDecimalCodecDefined extends BigDecimalCodec {

	private static DecimalFormat format;

	public BigDecimalCodecDefined(){
		format = new DecimalFormat();
		format.setMinimumFractionDigits(1);
		format.setMaximumFractionDigits(Integer.MAX_VALUE);
		format.setGroupingUsed(false);
		format.setParseBigDecimal(true);
	}

	@Override
	public void write(JSONSerializer serializer, Object object, Object fieldName, Type fieldType, int features)
			throws IOException {
		SerializeWriter out = serializer.out;

		if (object == null) {
			if (((1 & SerializerFeature.WriteNullNumberAsZero.mask) == 1 //
					|| (features & SerializerFeature.WriteNullNumberAsZero.mask) == 1)
					&& ((1 & SerializerFeature.WriteBigDecimalAsPlain.mask) == 1
							|| (features & SerializerFeature.WriteBigDecimalAsPlain.mask) == 1)) {
				out.write(format.format(BigDecimal.ZERO));
			} else {
				out.writeNull(SerializerFeature.WriteNullNumberAsZero);
			}
		} else {
			BigDecimal val = (BigDecimal) object;

			String outText;
			if (out.isEnabled(SerializerFeature.WriteBigDecimalAsPlain)) {
				outText = format.format(val);
			} else {
				outText = val.toPlainString();
			}
			out.write(outText);

			if (out.isEnabled(SerializerFeature.WriteClassName) && fieldType != BigDecimal.class && val.scale() == 0) {
				out.write('.');
			}
		}
	}

}
