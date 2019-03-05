# Add project specific ProGuard rules here.
# By default, the flags in this file are appended to flags specified
# in C:\Users\AmeerHamza\AppData\Local\Android\Sdk/tools/proguard/proguard-android.txt
# You can edit the include path and order by changing the proguardFiles
# directive in build.gradle.
#
# For more details, see
#   http://developer.android.com/guide/developing/tools/proguard.html
-dontwarn com.fasterxml.**
-dontwarn okio.**
-dontwarn retrofit2.**
-keep class org.apache.http.** { *; }
-dontwarn org.apache.http.**
-dontwarn android.net.**
-keep class com.squareup.okhttp.** { *;}
-keep public class * implements com.bumptech.glide.module.GlideModule
-keep public class * extends com.bumptech.glide.module.AppGlideModule
-keep public enum com.bumptech.glide.load.resource.bitmap.ImageHeaderParser$** {
  **[] $VALUES;
  public *;
}
# Glide
-keep public class * implements com.bumptech.glide.module.GlideModule
-keep public enum com.bumptech.glide.load.resource.bitmap.ImageHeaderParser$** {
    **[] $VALUES;
    public *;
}
-keep class org.jsoup.**
-keep public class com.bumptech.glide.load.resource.gif.GifDrawable**
-keepclassmembers class  com.bumptech.glide.load.resource.gif.GifDrawable** {*;}
-keep class  com.bumptech.glide.load.resource.gif.*{
*;}
-keep public  class com.bumptech.glide.load.resource.gif.GifFrameLoader
-keep public  class com.ameerhamza.animatedgiflivewallpapers.Extra.AnimatedGifEncoder
-dontwarn com.squareup.picasso.**
-keep class com.android.vending.billing.**
-keepclassmembers class * {
    @android.webkit.JavascriptInterface <methods>;
}

-dontwarn com.applovin.**
# For communication with AdColony's WebView
-keepclassmembers class * {
    @android.webkit.JavascriptInterface <methods>;
}

# Keep ADCNative class members unobfuscated
-keepclassmembers class com.adcolony.sdk.ADCNative** {
    *;
 }
-keep class com.applovin.** { *; }
-keep class com.google.android.gms.ads.identifier.** { *; }
-keep class com.applovin.** { *; }
-dontwarn com.applovin.**
-keep class com.microsoft.azure.storage.table.** { *; }
-dontwarn com.fasterxml.jackson.core**

-keep class com.microsoft.windowsazure.mobileservices.** { *; }
-dontwarn android.os.**
-dontwarn com.microsoft.windowsazure.mobileservices.RequestAsyncTask
-keep class com.firebase.** { *; }
-keep class org.apache.** { *; }
-keepnames class com.fasterxml.jackson.** { *; }
-keepnames class javax.servlet.** { *; }
-keepnames class org.ietf.jgss.** { *; }
-dontwarn org.w3c.dom.**
-dontwarn org.joda.time.**
-dontwarn org.shaded.apache.**
-dontwarn org.ietf.jgss.**
-keepattributes Signature

-keepattributes Annotation

-keep class okhttp3.** { *; }

-keep interface okhttp3.** { *; }

-dontwarn okhttp3.**


# Add any project specific keep options here:

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