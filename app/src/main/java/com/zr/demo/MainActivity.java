package com.zr.demo;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.zr.addressselector.BottomDialog;
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

    BottomDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button buttonBottomDialog = (Button) findViewById(R.id.buttonBottomDialog);
        assert buttonBottomDialog != null;
        buttonBottomDialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog = new BottomDialog(MainActivity.this);
                dialog.setOnAddressSelectedListener(MainActivity.this);
                dialog.show();
                // TODO: 17/2/7 实时请求省份数据
                Province province = new Province();
                province.id = 1;
                province.name = "省份";
                dialog.getSelector().setProvinces(Collections.singletonList(province));
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

        ToastUtils.showShort(this, s);
    }

    @Override
    public void onProvinceSelected(Province province) {
        System.out.println("onProvinceSelected");

        // TODO: 2017/2/5 请求城市数据
        City city1 = new City();
        city1.province_id = province.id;
        city1.id = 1;
        city1.name = "城市1";

        City city2 = new City();
        city2.province_id = province.id;
        city2.id = 2;
        city2.name = "城市2";

        List<City> list = new ArrayList<>();
        list.add(city1);
        list.add(city2);
        dialog.getSelector().setCities(list);
    }

    @Override
    public void onCitySelected(City city) {
        System.out.println("onCitySelected");

        // TODO: 2017/2/5 请求县乡数据
        switch (city.id){
            case 1:
                County county11 = new County();
                county11.city_id = city.id;
                county11.id = 3;
                county11.name = "乡镇11";

                County county12 = new County();
                county12.city_id = city.id;
                county12.id = 4;
                county12.name = "乡镇12";

                List<County> list = new ArrayList<>();
                list.add(county11);
                list.add(county12);
                dialog.getSelector().setCountries(list);
                break;

            case 2:
                County county21 = new County();
                county21.city_id = city.id;
                county21.id = 3;
                county21.name = "乡镇21";

                County county22 = new County();
                county22.city_id = city.id;
                county22.id = 4;
                county22.name = "乡镇22";

                List<County> list2 = new ArrayList<>();
                list2.add(county21);
                list2.add(county22);
                dialog.getSelector().setCountries(list2);
                break;
        }

    }

    @Override
    public void onCountySelected(County county) {
        System.out.println("onCountySelected");
        // TODO: 17/2/7 实时获取街道信息
        Street street = new Street();
        street.id = 4;
        street.county_id = county.id;
        street.name = "街道";

        dialog.getSelector().setStreets(Collections.singletonList(street));
    }

}
