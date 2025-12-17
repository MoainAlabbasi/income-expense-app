# Add project specific ProGuard rules here.
-keepattributes *Annotation*
-keepattributes SourceFile,LineNumberTable
-keep public class * extends java.lang.Exception

# Keep data classes
-keep class com.moain.incomeexpense.MainActivity$Transaction { *; }
