# Keys-For-Command
 为了便于使用命令检测客户端的按键而创建的模组

## 我如何使用它
 + 首先, 需要为双端安装此模组
 + 进入世界, 输入以下命令
 ```
# 这意味着玩家客户端不会受到服务端的限制, 将会监听全部的按键并由服务端发送键位计分板名称
 /gamerule keyPressDebug true
 /reload
```
 + 根据你想要监听哪些键位, 创建对应名称的计分板 (需要为可改变分数的准则, 注意大小写)
 + 当你已经创建完键位计分板之后需要 *输入 `/reload`* 或 *重新加入世界*
 + 如果你了解完键位对应的计分板名称后, 输入 `/gamerule keyPressDebug false` 即可禁用显示

 ## 原理
 客户端将会对键盘及鼠标的`按下/重复`操作进行监听, 当玩家加入到世界时, 服务端会向玩家发送已注册键位的包

 当客户端收到包之后, 将包内注册的键位进行载入, 在世界规则 `keyPressDebug` 为 `false` 的时候, 玩家按下已注册的键位, 才会向服务端发送键位包

 而世界规则 `keyPressDebug` 为 `true` 的时候, 只要按下某个键位就会向服务端发送键位包

 当你创建键位计分板或更改世界规则 `keyPressDebug` 后, 服务端都需要重新向玩家发送注册键位包, 但考虑到性能, 不能每刻向玩家发送

 这也就是为什么需要*输入 `/reload`* 或 *重新加入世界* 的原因

 

 
