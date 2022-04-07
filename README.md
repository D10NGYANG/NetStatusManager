# NetStatusManager

 网络状态监听框架，从1.3版本开始支持***LiveData***监听

[![](https://jitpack.io/v/D10NGYANG/NetStatusManager.svg)](https://jitpack.io/#D10NGYANG/NetStatusManager)



## 添加依赖

1、在根目录的`build.gradle`里插入

```kotlin
allprojects {
  repositories {
    ...
    maven { url 'https://jitpack.io' }
  }
}
```

2、在app的`build.gradle`里插入

```kotlin
dependencies {
    implementation 'com.github.D10NGYANG:NetStatusManager:1.5'
}
```

## 使用代码

### LiveData监听

推荐在Kotlin项目中使用这种方法。

```kotlin
DLNetManager.getInstance(this.application).getNetTypeLiveData().observe(this, Observer { str ->
    // 网络状态改变
    Log.e("测试", "Main网络状态改变：${str}")
    //binding.netType = str
    if (str == NetType.WIFI) {
        if (NetUtils.is5GWifiConnected(this)) {
            Log.e("测试", "这是5G WI-FI")
        } else{
            Log.e("测试", "这是2.4G WI-FI")
        }
        Log.e("测试", "WI-FI名：${NetUtils.getConnectedWifiSSID(this)}")
    }
})
```

### 传统注册监听

1、注册监听

```kotlin
override fun onStart() {
    super.onStart()
    DLNetManager.getInstance(this.application).register(this)
}
```

2、取消监听

```kotlin
override fun onStop() {
    super.onStop()
    DLNetManager.getInstance(this.application).unRegister(this)
}
```

3、取消监听所有

```kotlin
override fun onDestroy() {
    DLNetManager.getInstance(this.application).unRegisterAll()
    super.onDestroy()
}
```

4、监听状态变化

```kotlin
@DLNet
fun onNetStatusChange(str: @NetType String) {
    // 网络状态改变
    Log.e("测试", "Main网络状态改变：${str}")
    //binding.netType = str
    if (str == NetType.WIFI) {
        if (NetUtils.is5GWifiConnected(this)) {
            Log.e("测试", "这是5G WI-FI")
        } else{
            Log.e("测试", "这是2.4G WI-FI")
        }
        Log.e("测试", "WI-FI名：${NetUtils.getConnectedWifiSSID(this)}")
    }
}
```

## 混淆规则

```kotlin
-keep class com.dlong.netstatus.** {*;}
-dontwarn com.dlong.netstatus.**
-keepclassmembers class * {
    @com.dlong.netstatus.annotation.DLNet <methods>;
    @com.dlong.netstatus.annotation.NetType <fields>;
}
```
