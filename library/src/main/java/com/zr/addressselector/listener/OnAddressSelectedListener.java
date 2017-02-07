package com.zr.addressselector.listener;


import com.zr.addressselector.model.City;
import com.zr.addressselector.model.County;
import com.zr.addressselector.model.Province;
import com.zr.addressselector.model.Street;

public interface OnAddressSelectedListener {
    void onAddressSelected(Province province, City city, County county, Street street);
    void onProvinceSelected(Province province);
    void onCitySelected(City city);
    void onCountySelected(County county);
}
