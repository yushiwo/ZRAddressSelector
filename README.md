## 前言
我们知道，地址选择器是一个通用组件，网上的实现方式也有很多。那么为什么还会有这篇文章呢？因为我在调研过程中发现，虽然都是地址选择器，但是实现的方式却各不相同。以下是调研地址选择器的一些总结和思考实践。希望对大家有帮助，大家有什么更好的想法，也要告知我哦～

## 地址选择器实现方式介绍

+ **本地存放area.db文件**  
大多数App都是此种实现
    - 优点：启动快，不受网络影响
    - 缺点：
        + 不能实时更新，数据更新依赖发布新版本。
        + 各端（服务器、前端、移动端）需要维护一份相同的地址信息表。增加了修改出错的概率。
        + 地址信息表本地保存，增大安装包体积

+ **一次性从服务端拉取所有地址信息**  
在App启动时候，一次性拉取所有地址信息。
    - 优点：数据可配置、请求少
    - 缺点：
        + 每次启动都请求服务器，大部分是无用请求，浪费了服务器资源。
        + 请求数据量大
    - 优化方案：点击选择地址的时候才去一次性请求所有数据。但是还是不能避免请求数据量过大问题。

+ **实时获取省、市、区信息，选中上一级才去获取对应的下一级数据**  
如：**京东**
    - 优点：数据可配置，请求数据量小
    - 缺点：请求较多，需要处理的异常case较多
    - 思考优化方案：每次唤起地址选择器时，缓存获取的地址信息历史数据。显示地址信息的时候，只有本地缓存没有当前数据，才向服务端发送请求。

因为用户基本是在有网络的情况下才会使用，所以选择联网获取地址信息也是合理的。又考虑到网络请求的大小，服务端性能影响，安装包大小以及地址信息可配置性等因素，实时获取地址信息是一个不错的方式。下面介绍地址选择器实现一些关键点。

## 实时获取信息的地址选择器设计
先来看效果，如下图所示。唤起地址选择器会有一次省份的网络请求，之后每一级数据都是实时去获取。在当此地址选择器唤起状态，获取之后就将历史数据保存在本地，下一次就不再发送网络请求了。
![地址选择器效果图](https://github.com/yushiwo/images/blob/master/addressselector/test.gif?raw=true)

### 数据结构设计
当前的地址信息按照省、市、区/县、街道四级划分，前一级总是和后一级相关联。

+ **Province**

```
public class Province {
    public long id;
    public String name;
}
```
+ **City**

```
public class City  {
    public long id;
    public long province_id;
    public String name;
}
```
+ **County**

```
public class County {
    public long id;
    public long city_id;
    public String name;
}
```
+ **Street**

```
public class Street {
    public long id;
    public long county_id;
    public String name;
}
```

### 回调接口设计
主要提供了4个回调方法：

```
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
```
这里着重说一下`onAddressSelected`回调方法，其是在地址选择完成的时候调用。那么是如何判断地址选择已经完成呢。这个在`AddressSelector`中有如下一个机制。  
每次一个级别选择完成后，会获取下一个级别的数据（网络请求或者缓存获取）进行显示。显示的时候有这么一个逻辑，当前级别有数据，则正常显示；若没有，则说明地址选择已经完成，此时调用`onAddressSelected`方法。 

### 缓存设计
**我们这里默认服务端每个地址的id都是唯一的**。缓存机制比较简单，可以看下如下的流程图。  
![缓存机制流程图](https://github.com/yushiwo/images/blob/master/addressselector/cache.png?raw=true)
1. 建立三个缓存map，分别缓存`省－市`、`市－区`和`区－街道`。

```
/** 缓存数据:省-市 */
private ArrayMap<Long, List<City>> province2city = new ArrayMap<>();
/** 缓存数据:市-区 */
private ArrayMap<Long, List<County>> city2county = new ArrayMap<>();
/** 缓存数据:区-街道 */
private ArrayMap<Long, List<Street>> county2street = new ArrayMap<>();
```
2. 选择省、市、区某一级时，先查看是否有缓存数据，若有则使用缓存数据；若没有，则向服务端发送网络请求。

```
// 有缓存则直接使用缓存,否则去重新请求
if(province2city.containsKey(province.id)){
    setCities(province2city.get(province.id));
} else {
    progressBar.setVisibility(View.VISIBLE);
    listener.onProvinceSelected(province);
}
```
3、每次获取的历史数据，存储在相应的缓存map中。

```
province2city.put(provinceId, cities);
```

## 控件使用
1. gradle导入控件

```
compile 'com.zr.addressselector:library:1.0.1'
```

2. Activity实现`OnAddressSelectedListener`接口

```
public class MainActivity extends AppCompatActivity implements OnAddressSelectedListener
```

3. 展现地址选择器

```
dialog = new BottomSelectorDialog(MainActivity.this);
dialog.setOnAddressSelectedListener(MainActivity.this);
dialog.show();
```
4. 根据网络返回，设置数据

```
dialog.getSelector().setProvinces(Collections.singletonList(province));
```

5. 控件提供的几个设置方法

```
/**
     * 设置回调接口
     * @param listener
     */
    public void setOnAddressSelectedListener(OnAddressSelectedListener listener) {
        this.listener = listener;
    }

    /**
     * 设置省列表
     * @param provinces 省份列表
     */
    public void setProvinces(List<Province> provinces){
        handler.sendMessage(Message.obtain(handler, WHAT_PROVINCES_PROVIDED, provinces));
    }

    /**
     * 设置市列表
     * @param cities 城市列表
     */
    public void setCities(List<City> cities){
        handler.sendMessage(Message.obtain(handler, WHAT_CITIES_PROVIDED, cities));
    }

    /**
     * 设置区列表
     * @param countries 区/县列表
     */
    public void setCountries(List<County> countries){
        handler.sendMessage(Message.obtain(handler, WHAT_COUNTIES_PROVIDED, countries));
    }

    /**
     * 设置街道列表
     * @param streets 街道列表
     */
    public void setStreets(List<Street> streets){
        handler.sendMessage(Message.obtain(handler, WHAT_STREETS_PROVIDED, streets));
    }
```


## 写在最后
虽然提供了gradle compile导入的方式，但是整个控件的源码其实是很简单的。如果有定制需求，可以到这里下载源码。 

项目源码下载：[ZRAddressSelector](https://github.com/yushiwo/ZRAddressSelector.git)

## 特别感谢 
[JDAddressSelector](https://github.com/chihane/JDAddressSelector)，本组件设计也是受次开源项目启发，在此感谢～