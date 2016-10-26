# Android-annotation-lib
处理Android M运行时权限的库

本库使用注解方式去检查、申请和处理Android M的权限问题
注解的方式是基于Annotation Processor的编译时注解，减少yun xing shi运行时注解对性能的损耗

## 引入 
##### 还没发布到jcenter,以下都是以moudule方式引入


project's build.gradle

```
buildscript {
dependencies {
classpath 'com.neenbedankt.gradle.plugins:android-apt:1.8'
}
}
```

module's buid.gradle

```
apply plugin: 'android-apt'

dependencies {
compile project(':premissionutils')
provided project(':lib-annotation')
apt project(':lib-annotation')
}
```

## 使用
* 检查权限
```java
PermissionUtils.checkPermission(this,Manifest.permission.READ_EXTERNAL_STORAGE)

```

* 申请权限

```java
PermissionUtils.requestPermission(this,
new String[]{Manifest.permission.READ_EXTERNAL_STORAGE,
Manifest.permission.WRITE_EXTERNAL_STORAGE},
code)
```

* 处理权限回调（处理都一样，可写在基类的`onRequestPermissionsResult1`里）

```java
PermissionUtils.onRequestPermissionsResult(this,requestCode,permissions,grantResults);
```

* 然后就可以通过注解写具体业务逻辑

```
//权限申请成功回调，code是指明申请哪个权限成果了
@GrantPermissionSucc(code)
public void XXX(){

}

//权限申请失败回调，code是指明申请哪个权限成果了
@GrantPermissionFail(code)
public void XXX(){

}
```




