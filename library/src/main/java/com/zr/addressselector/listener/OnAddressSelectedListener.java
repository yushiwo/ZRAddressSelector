package com.zr.addressselector.listener;


import com.zr.addressselector.model.City;
import com.zr.addressselector.model.County;
import com.zr.addressselector.model.Province;
import com.zr.addressselector.model.Street;

public interface OnAddressSelectedListener {
    // 获取地址完成回调
    void onAddressSelected(Province province, City city, County county, Street street);
    // 选取省份完成回调
    void onProvinceSelected(Province province);
    // 选取城市完成回调
    void onCitySelected(City city);
    // 选取区/县完成回调
    void onCountySelected(County county);
}
