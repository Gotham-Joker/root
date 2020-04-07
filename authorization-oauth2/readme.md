# oauth2.0协议
## 授权码模式获取access_token
###第一步，外部系统引导用户跳转到http://{授权服务器ip}/oauth/authorize并且需要携带以下五个参数：
* `client_id`: 外部系统的客户端id，需要事先在授权服务器注册获取
* `redirect_uri` : 外部系统的接口回调地址
* `scope` : 申请的权限，一般写`userInfo`就行，代表申请用户信息，如果要申请多个权限，请用`+`号分隔
* `response_type` : 因为是授权码模式，所以固定写`code`就行
* `state` : 外部系统自定义信息，之后会原样返回到`redirect_uri`中，一般用于抵制CSRF攻击
---
以下是请求的URL示例：
```
https://authorization-server.com/oauth/authorize
 ?client_id=29352915982374239857
 &redirect_uri=https%3A%2F%2Fexample-app.com%2Fcallback
 &scope=create+delete
 &response_type=code
 &state=xcoiv98y2kd22vusuye3kch
```
当用户同意授权以后授权服务器会跳转到外部系统提供的回调地址`redirect_uri`，并且携带认证中心生成的`code`以及外部系统提供的`state`，以下是授权服务器调用的回调地址示例：
```
https://example-app.com/Fcallback
 ?code=g0ZGZmNjVmOWIjNTk2NTk4ZTYyZGI3
 &state=xcoiv98y2kd22vusuye3kch
```
### 第二步，外部系统获取access_token，需要以POST请求访问http://{授权服务器ip}/oauth/token并携带以下参数：
* `grant_type`: 固定值，填`authorization_code`就行
* `code` : 第一步的回调地址中携带的`code`
* `client_id` : 外部系统的客户端id
* `client_secret` : 外部系统的客户端密码，需要事先在认证中心注册获取

以下是post请求示例
```
POST /oauth/token HTTP/1.1
Host: authorization-server.com

grant_type=authorization_code
&code=xxxxxxxxxxx
&client_id=xxxxxxxxxx
&client_secret=xxxxxxxxxx
```
调用成功以后会返回以下示例数据：
```
HTTP/1.1 200 OK
Content-Type: application/json
Cache-Control: no-store
Pragma: no-cache

{
  "access_token":"MTQ0NjJkZmQ5OTM2NDE1ZTZjNGZmZjI3",
  "token_type":"bearer",
  "expires_in":7200,
  "refresh_token":"IwOGYzYTlmM2YxOTQ5MGE3YmNmMDFkNTVk",
  "scope":"create delete"
}
```

### access_token 每两小时就会失效，失效之后需要用refresh_token换取新的access_token。所以，接下来的第三步看情况执行，如果失效了再执行这一步：
