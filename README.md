WeUI App
==================

WeUI原本是微信官方推出的Web UI组件库，它专为移动端的网页和小程序设计，简洁优雅的设计风格是我选择模仿这个项目的主要原因，还有很重要的一点是在浏览器中打开 [weui.io](https://weui.io) 就能够通过检查元素轻松获取到各种设计参数，如颜色、字体大小、宽高和边距等。但是为了能够尽可能全面地接触到原生安卓开发的各个方面，我以WeUI为基础，扩展了一大批安卓端特有的硬件和系统层面的功能示例，以及一些特定功能的UI界面，主要目的就是希望尽可能快速全面地熟悉安卓开发的各个方面。

详细介绍：[https://juejin.cn/post/7353515388254142514](https://juejin.cn/post/7353515388254142514)

## 项目说明
- 尽最大可能减少外部依赖，自己造轮子，可以更好地理解底层原理、更好的灵活性控制、精简代码以及降低复杂性等
- 所有依赖尽可能用最新的版本，以便获得bug修复、性能提升以及新功能等
- 根据官方指引，项目采用模块化设计，实现关注点分离
- 所有界面均适配深色模式

## 源码&安装包
> 源码：[https://gitee.com/chengdongqing/weui](https://gitee.com/chengdongqing/weui)
>
> 安装包：[https://gitee.com/chengdongqing/weui/releases](https://gitee.com/chengdongqing/weui/releases)

## 功能介绍

基础组件
```
Badge（角标）
Loading（加载中）
LoadMore（加载更多）
Progress（进度条）
Steps（步骤条）
Swiper（轮播图）
RefreshView（下拉刷新）
TabView（选项卡视图）
SwipeAction（可滑动列表项）
Skeleton（骨架屏）
Tree（树型菜单）
```
表单组件
``` 
Button（按钮）
Checkbox（复选框）
Radio （单选框）
Switch（开关）
Slider（滑块）
Picker（滚动选择器）
Input（输入框）
Rate（评分）
```
媒体组件
``` 
Camera（相机，拍照+录视频）
MediaPicker（媒体选择器）
AudioRecorder（录音）
AudioPlayer（音频播放）
Gallery（相册）
ImageCropper（图片裁剪）
PanoramicImage（全景图片）
```
操作反馈
```
ActionSheet（弹出式菜单）
Dialog （对话框）
Popup（半屏弹窗）
Toast（提示框）
InformationBar（信息提示条）
ContextMenu（上下文菜单）
```
系统服务
```
Contacts（拨号、通讯录、通话记录）
Clipboard（剪贴板）
CalendarEvents （系统日历事件）
DeviceInfo（设备信息）
Downloader（系统下载）
Database（数据库示例）
SystemStatus（系统状态）
SMS（短信发送与读取）
InstalledApps（已安装的应用）
Keyboard（系统键盘）
Notification（发送通知）
```
网络服务
```
HttpRequest（网络请求）
FileUpload（文件上传）
FileDownload（文件下载）
WebSocket（双向通信）
```
硬件接口
```
Screen（屏幕）
Flashlight（闪光灯）
Vibration（震动）
WiFi（无线局域网）
Bluetooth（蓝牙）
GNSS（全球卫星导航系统）
Infrared（红外线）
Gyroscope（陀螺仪）
Compass（罗盘）
Accelerometer（加速度计）
Hygrothermograph（温湿度计）
Fingerprint（指纹识别）
```
图表组件
```
BarChart （柱状图）
LineChart（折线图）
PieChart（饼图、环形图）
```
二维码 
```
QrCodeScanner（二维码识别）
QrCodeGenerator（二维码生成）
```
地图组件 
```
LocationPreview（位置预览）
LocationPicker（位置选择）
```
扩展示例 
```
Calendar（日历）
Clock（时钟）
DropCard （滑走式卡片）
FileBrowser （文件浏览器）
Paint （画板）
IndexedList（索引列表）
Reorderable（拖拽排序）
DividingRule （刻度尺）
OrgTree（组织机构树）
DigitalRoller（数字滚轮）
DigitalKeyboard（数字键盘）
CubicBezier（贝塞尔曲线）
NotificationBar（通知栏）
VideoChannel（视频号）
SolarSystem（太阳系动画）
```