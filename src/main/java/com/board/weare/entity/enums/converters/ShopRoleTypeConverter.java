package com.board.weare.entity.enums.converters;

import com.jsol.mcall.entity.enums.AbstractBaseEnumConverter;
import com.jsol.mcall.entity.enums.ShopRoleType;

import javax.persistence.Converter;

// autoApply 적용하면 글로벌하게 적용해서 OpenUsimType을 쓰는 모든 엔티티에 적용됨.
@Converter(autoApply = true)
public class ShopRoleTypeConverter extends AbstractBaseEnumConverter<ShopRoleType, String> {

    @Override
    protected ShopRoleType[] getValueList() {
        return ShopRoleType.values();
    }
}