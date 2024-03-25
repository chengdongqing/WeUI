# Add project specific ProGuard rules here.
# You can control the set of applied configuration files using the
# proguardFiles setting in build.gradle.
#
# For more details, see
#   http://developer.android.com/guide/developing/tools/proguard.html

# If your project uses WebView with JS, uncomment the following
# and specify the fully qualified class name to the JavaScript interface
# class:
#-keepclassmembers class fqcn.of.javascript.interface.for.webview {
#   public *;
#}

# Uncomment this to preserve the line number information for
# debugging stack traces.
#-keepattributes SourceFile,LineNumberTable

# If you keep the line number information, uncomment this to
# hide the original source file name.
#-renamesourcefileattribute SourceFile

# -keep class <类名> { *; }：保持指定类和其所有成员不被混淆。
# -keep class <包名>.** { *; }：保持指定包下所有类和成员不被混淆。
# -keepclasseswithmembers class <类名> { <成员规则>; }：仅当类中包含指定成员时，才保持该类不被混淆。

# allowoptimization：可以对匹配的类或成员应用优化操作。包括但不限于移除无用代码、合并相似的代码段、简化条件判断等，旨在提高应用的运行效率和减少应用大小。
# allowobfuscation：尽管保持了这些类，但允许R8更改它们的名称（进行混淆）。这有助于保护代码免受逆向工程的攻击。
# allowshrinking：允许R8移除这些类中未使用的部分，即使类本身被保留下来。

# { *; }是一种通配符语法，用于匹配类中的所有成员，包括字段、方法、构造函数等。
# 当你在保持规则（-keep）后面使用{ *; }时，意味着你希望保留指定类的所有成员不被混淆或删除。这通常用于确保特定的类完全保持其原有的结构和名称，以避免运行时错误或者其他由于混淆引起的问题。