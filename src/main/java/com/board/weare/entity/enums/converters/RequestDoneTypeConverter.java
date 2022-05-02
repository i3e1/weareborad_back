package com.board.weare.entity.enums.converters;

import com.jsol.mcall.entity.enums.AbstractBaseEnumConverter;
import com.jsol.mcall.entity.enums.RequestDoneType;

import javax.persistence.Converter;

// autoApply 적용하면 글로벌하게 적용해서 OpenUsimType을 쓰는 모든 엔티티에 적용됨.
@Converter(autoApply = true)
public class RequestDoneTypeConverter extends AbstractBaseEnumConverter<RequestDoneType, String> {

    @Override
    protected RequestDoneType[] getValueList() {
        return RequestDoneType.values();
    }
}