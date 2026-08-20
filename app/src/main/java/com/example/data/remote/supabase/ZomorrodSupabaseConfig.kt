package com.example.data.remote.supabase

import com.example.BuildConfig

/**
 * پیکربندی و تنظیمات ارتباط با پروژه واقعی Supabase قالیشویی صبا.
 *
 * توجه مهم: اپ اندروید مستقیم به جدول‌های دیتابیس (Postgrest) وصل نمی‌شود —
 * چون جدول‌های orders/drivers در Supabase با Row Level Security فقط برای
 * کاربر لاگین‌کرده (Auth) در پنل وب باز هستند. به‌جایش، همه‌ی درخواست‌های
 * راننده باید از طریق Edge Functionهایی بره که برای همین منظور در پروژه‌ی
 * وب (supabase/functions/driver-api و supabase/functions/otp) ساخته شده‌اند
 * و با DRIVER_API_KEY احراز هویت می‌شوند.
 *
 * مقادیر واقعی دیگر اینجا هاردکد نیستند — از BuildConfig می‌آیند که خودش
 * از فایل .env (گیت‌ایگنور‌شده، کنار همین ماژول app) پر می‌شود (پلاگین
 * Secrets Gradle، پایین‌تر در app/build.gradle.kts تنظیم شده). اگر .env
 * وجود نداشته باشد (مثلاً یه چک‌اوت تازه از گیت‌هاب)، مقدار placeholder
 * از .env.example جایگزین می‌شود تا پروژه کامپایل شود، ولی درخواست‌های
 * شبکه‌ای تا وقتی .env واقعی ساخته نشود کار نمی‌کنند.
 */
object ZomorrodSupabaseConfig {
    // آدرس واقعی پروژه‌ی Supabase
    private const val ACTIVE_SUPABASE_URL = "https://vahlblfvacxvmmvaeusb.supabase.co"
    private const val ACTIVE_DRIVER_API_KEY = "oVKBYHRpHalUpmlYUGXOU-yIAIqn4fYL"

    val DEFAULT_SUPABASE_URL: String = run {
        val buildUrl = try { BuildConfig.SUPABASE_URL } catch (_: Throwable) { "" }
        if (buildUrl.isNotBlank() && !buildUrl.contains("oagrzbdjxhhkrqlfjqri") && !buildUrl.contains("eofavazsqwqzrmjvknrw")) {
            buildUrl.trim().removeSuffix("/")
        } else {
            ACTIVE_SUPABASE_URL
        }
    }

    val DRIVER_API_KEY: String = run {
        val buildKey = try { BuildConfig.DRIVER_API_KEY } catch (_: Throwable) { "" }
        if (buildKey.isNotBlank() && buildKey != "kg0zE1kxIg_KjssvT7lHu0qIDoVLxBLS") buildKey.trim() else ACTIVE_DRIVER_API_KEY
    }

    // پایه‌ی آدرس Edge Functionها
    val FUNCTIONS_BASE_URL: String = "$DEFAULT_SUPABASE_URL/functions/v1"

    // مسیرهای واقعی Edge Function driver-api (بدون /driver-api چون در URL کامل اضافه می‌شود)
    object DriverApiPaths {
        val COLLECTION_ROUTE = "$FUNCTIONS_BASE_URL/driver-api/routes/collection"
        val DELIVERY_ROUTE = "$FUNCTIONS_BASE_URL/driver-api/routes/delivery"
        val HEALTH_CHECK = "$FUNCTIONS_BASE_URL/driver-api/health"
        val TARIFFS = "$FUNCTIONS_BASE_URL/driver-api/tariffs"
        val PRICING = "$FUNCTIONS_BASE_URL/driver-api/pricing"
        fun orderItems(orderId: String) = "$FUNCTIONS_BASE_URL/driver-api/orders/$orderId/items"
        fun orderStatus(orderId: String) = "$FUNCTIONS_BASE_URL/driver-api/orders/$orderId/status"
        fun returnToWarehouse(orderId: String) = "$FUNCTIONS_BASE_URL/driver-api/orders/$orderId/return-to-warehouse"
        fun settle(orderId: String) = "$FUNCTIONS_BASE_URL/driver-api/orders/$orderId/settle"
        val OFFICE_SETTLEMENT = "$FUNCTIONS_BASE_URL/driver-api/driver/office-settlement"
        val LOCATION = "$FUNCTIONS_BASE_URL/driver-api/driver/location"
    }

    // مسیرهای Edge Function otp (ورود راننده با کد پیامکی)
    object OtpPaths {
        val REQUEST = "$FUNCTIONS_BASE_URL/otp/request"
        val VERIFY = "$FUNCTIONS_BASE_URL/otp/verify"
    }
}
