### ueditor富文本编辑器模块
---
### 1. 优化

#### 1.1 解决图片跨域上传成功但是依然提示"上传错误"的bug
 
下载ueditor官方源码包（没错，是源码），在ueditor.all.js中找到
`domUtils.on(iframe, 'load', callback)`
这一行，做以下更改：
```
                // domUtils.on(iframe, 'load', callback);
                //form.action = utils.formatUrl(imageActionUrl + (imageActionUrl.indexOf('?') == -1 ? '?':'&') + params);
                //form.submit();

                var formdata = new FormData(form);

                var xhr = new XMLHttpRequest();
                xhr.open("POST", me.getOpt('serverUrl') + '?action=uploadimage', true);
                xhr.send(formdata);

                xhr.onreadystatechange = function () {
                    if (xhr.readyState == 4) {
                        var response = JSON.parse(xhr.responseText);
                        if (response.state) {
                            loader = me.document.getElementById(loadingId);
                            loader.setAttribute('src', response.url);
                            loader.setAttribute('_src', response.url);
                            loader.setAttribute('title', response.title || '');
                            loader.setAttribute('alt', response.original || '');
                            loader.removeAttribute('id');
                            domUtils.removeClasses(loader, 'loadingclass');
                        } else {
                            showErrorLoader && showErrorLoader(response.state);
                        }
                    }
                }
```

#### 1.2 下载文件时，文件名采用上传时的名字
由于文件上传经过后端处理后，会被重命名(普通上传采用uuid,
oss上传采用文件哈希值(sha1)命名)，这样导致我们在下载文件时
会下载到被重命名的文件，所以需要特殊处理一下：
在ueditor.config.js找到
`<a style="font-size:12px; color:#0066cc;"`这一行，添加
`download="'+title+'"`这个属性()，然后在ueditor.config.js找到`whitList`，
在a标签的数组中添加'download'这个值，这样下载的文件的名字就是上传时的文件名了。

完了以后重新打包，安装nodejs，命令行cd到ueditor源码位置(package.json文件所在目录)
执行以下命令
```
npm install
npm install -g grunt-cli
grunt --encode=utf-8 --server=jsp
```
打包完了，会生成dist文件夹，里面的东西就是我们要用的代码了

### 2. 后端(java)使用
#### 2.1 普通上传
导入ueditor-starter这个jar包，默认http://{domain}/{contextPath}/api/ueditor就是接口地址。
上传后的文件存放目录
是webapp环境下的/ueditor。

#### 2.2 oss上传

oss上传还需要引入oss-starter这个jar包，配置好
com.ht.oss的各种配置即可。

#### 2.3 修改默认配置

```
com:
  ht:
    ueditor:
      # 文件保存目录，oss上传同样生效
      upload-dir: /ueditor
      # 普通上传将返回完整url地址而不是绝对路径，eg: http://localhost:8080
      # oss上传会忽略该属性
      server-url: 
      # ueditor接口地址
      api: /api/ueditor
      # 是否启用oss上传，默认true
      enable-oss: true
spring:
  servlet:
    multipart:
      # 修改spring boot允许上传的文件大小
      max-request-size: 500MB
      max-file-size: 500MB
```

### 3. vue环境使用ueditor
安装`npm i vue-ueditor-wrap`

使用：
```
<template>
  <div>
    <VueUeditorWrap v-model="content" :config="ueConfig" />
    <p>
      <el-button type="primary" @click="showOne()">获取编辑器内容</el-button>
    </p>
  </div>
</template>
<script>
import VueUeditorWrap from "vue-ueditor-wrap";
export default {
  name: "about",
  components: {
    VueUeditorWrap
  },
  data() {
    return {
      content: "",
      ueConfig: {
        UEDITOR_HOME_URL: "/UEditor/",
        // 初始容器高度
        initialFrameHeight: 540,
        // 初始容器宽度
        initialFrameWidth: "100%",
        // 后端api地址，后端要注意处理跨域问题
        serverUrl: "http://192.168.0.103:8080/api/ueditor"
      }
    };
  },
  methods: {
    showOne() {
      window.console.log(this.content);
    }
  }
};
</script>
```

### 4. angular使用

