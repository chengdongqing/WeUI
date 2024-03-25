-keep class kotlin.coroutines.Continuation
-if interface * { @retrofit2.http.* public *** *(...); }
-keep class <3>
-keep class retrofit2.Response

-keep, allowobfuscation class top.chengdongqing.weui.feature.network.screens.request.data.model.** { *; }
-keep, allowobfuscation class top.chengdongqing.weui.feature.network.screens.upload.data.model.** { *; }