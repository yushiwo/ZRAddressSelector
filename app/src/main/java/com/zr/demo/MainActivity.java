package com.zr.demo;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;


import com.zr.addressselector.BottomSelectorDialog;
import com.zr.addressselector.listener.OnAddressSelectedListener;
import com.zr.addressselector.model.City;
import com.zr.addressselector.model.County;
import com.zr.addressselector.model.Province;
import com.zr.addressselector.model.Street;
import com.zr.addressselector.util.ToastUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class MainActivity extends AppCompatActivity implements OnAddressSelectedListener {

    BottomSelectorDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button buttonBottomDialog = (Button) findViewById(R.id.buttonBottomDialog);
        buttonBottomDialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog = new BottomSelectorDialog(MainActivity.this);
                dialog.setOnAddressSelectedListener(MainActivity.this);
                dialog.show();
                // TODO: 17/2/7 实时请求省份数据
                Province province = new Province();
                province.id = 1;
                province.name = "浙江省";
                dialog.getSelector().setProvinces(Collections.singletonList(province));
//                dialog.getSelector().setProvinces(null);
            }
        });

        Button hasMsgButton = (Button) findViewById(R.id.buttonBottomDialogWithMessage);
        hasMsgButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Province province = new Province();
                province.id = 1;
                province.name = "省份" + province.id;
                List<Province> provinces = Collections.singletonList(province);

                City city1 = new City();
                city1.province_id = province.id;
                city1.id = province.id * 100 + 1;
                city1.name = "城市" + city1.id;

                City city2 = new City();
                city2.province_id = province.id;
                city2.id = province.id * 100 + 2;
                city2.name = "城市" + city2.id;

                List<City> cities = new ArrayList<>();
                cities.add(city1);
                cities.add(city2);

                    long cityid = 101;
                    County county11 = new County();
                    county11.city_id = cityid;
                    county11.id = cityid * 100 + 1;
                    county11.name = "区县" + county11.id;

                    County county12 = new County();
                    county12.city_id = cityid;
                    county12.id = cityid * 100 + 2;
                    county12.name = "区县" + county12.id;

                    List<County> counties = new ArrayList<>();
                    counties.add(county11);
                    counties.add(county12);

                dialog = new BottomSelectorDialog(MainActivity.this);
                dialog.setOnAddressSelectedListener(MainActivity.this);
                dialog.show();

                dialog.getSelector().setAddressSelector(provinces, 0, cities, 0, counties, 0);
            }
        });
    }

    @Override
    public void onAddressSelected(Province province, City city, County county, Street street) {
        String s =
                (province == null ? "" : province.name) +
                        (city == null ? "" : "\n" + city.name) +
                        (county == null ? "" : "\n" + county.name) +
                        (street == null ? "" : "\n" + street.name);

        ToastUtils.showShort(MainActivity.this, s);
    }

    @Override
    public void onProvinceSelected(Province province) {
        System.out.println("onProvinceSelected");
//        ToastUtil.showToast("点击新省份,获取市数据");

        // TODO: 2017/2/5 请求城市数据
        City city1 = new City();
        city1.province_id = province.id;
        city1.id = province.id * 100 + 1;
        city1.name = "杭州市";

        City city2 = new City();
        city2.province_id = province.id;
        city2.id = province.id * 100 + 2;
        city2.name = "衢州市";

        List<City> list = new ArrayList<>();
        list.add(city1);
        list.add(city2);
        dialog.getSelector().setCities(list);
    }

    @Override
    public void onCitySelected(City city) {
        System.out.println("onCitySelected " + city.id);
//        ToastUtil.showToast("点击新城市,获取区县数据");

        // TODO: 2017/2/5 请求县乡数据
        if(city.id == 101){
            County county11 = new County();
            county11.city_id = city.id;
            county11.id = city.id * 100 + 1;
            county11.name = "西湖区";

            County county12 = new County();
            county12.city_id = city.id;
            county12.id = city.id * 100 + 2;
            county12.name = "滨江区";

            List<County> list = new ArrayList<>();
            list.add(county11);
            list.add(county12);
            dialog.getSelector().setCountries(list);
        }else if(city.id == 102) {
            County county21 = new County();
            county21.city_id = city.id;
            county21.id = city.id * 100 + 1;
            county21.name = "衢江区";

            County county22 = new County();
            county22.city_id = city.id;
            county22.id = city.id * 100 + 2;
            county22.name = "江山县";

            List<County> list2 = new ArrayList<>();
            list2.add(county21);
            list2.add(county22);
            dialog.getSelector().setCountries(list2);
        }

    }

    @Override
    public void onCountySelected(County county) {
        System.out.println("onCountySelected");
//        ToastUtil.showToast("点击新区县数据,获取街道数据");
        // TODO: 17/2/7 实时获取街道信息
        Street street = new Street();
        street.id = county.id * 100 + 1;
        street.county_id = county.id;
        street.name = "街道_" + street.id;

        dialog.getSelector().setStreets(Collections.singletonList(street));
    }

}

