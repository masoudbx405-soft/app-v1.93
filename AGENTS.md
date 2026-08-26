# قوانین ثابت معماری اپ اندروید «راننده» — هرگز نقض نشوند

این قوانین قراردادهای ثابت بین اپ اندروید و پنل وب/دیتابیس Supabase هستند و باید در تمام تغییرات و بازسازی‌ها دقیقاً حفظ شوند.

## قانون ۱: هیچوقت چند آدرس حدسی برای یک درخواست امتحان نکن

هر عملیات شبکه باید **فقط یک** endpoint واقعی و مشخص را صدا بزند — نه لیستی از چند آدرس حدسی.

آدرس‌های واقعی (پایه: `${functionsBase()}`):
- **درخواست کد OTP**: `POST /otp/request`
- **تایید کد OTP**: `POST /otp/verify`
- **گرفتن سفارش‌های جمع‌آوری**: `GET /driver-api/routes/collection?driverId={id}`
- **گرفتن سفارش‌های تحویل**: `GET /driver-api/routes/delivery?driverId={id}`
- **نرخ‌نامه خدمات**: `GET /driver-api/tariffs`
- **اطلاعات کارگاه (نام/آدرس/موقعیت)**: `GET /driver-api/workshop`
- **ارسال پیام چت**: `POST /driver-api/chat/send`
- **گرفتن پیام‌های چت**: `GET /driver-api/chat/messages`
- **آپلود امضای دیجیتال**: `POST /driver-api/signature/upload`
- **ارسال موقعیت زنده‌ی راننده**: `POST /driver-api/driver/location`
- **آپدیت وضعیت سفارش**: `PUT /driver-api/orders/{orderId}/status`
- **بررسی سلامت اتصال**: `GET /driver-api/health`

## قانون ۲: هیچ برند/اسم ثابتی هاردکد نشود

هیچ‌جای اپ (متن UI، نوتیفیکیشن، پیام چت خودکار، رسید چاپی، toast) نباید یک اسم شرکت/کارگاه ثابت داشته باشد.
- `object WorkshopNameHolder` در پکیج `com.example.data` (`var current: String = "کارگاه"`) مقدارش را از `/driver-api/workshop` می‌گیرد.
- `DriverViewModel` دارای `StateFlow<String> workshopName` است که با `SharedPreferences` کش می‌شود (`workshop_name`) و در `init{}` با `refreshWorkshopInfo()` به‌روزرسانی می‌شود.
- لایه‌های عمیق‌تر از `WorkshopNameHolder.current` می‌خوانند.
- هیچ نام اختصاصی هاردکد نشود.

## قانون ۳: نوع سرویس پیش‌زمینه برای موقعیت زنده

سرویس پس‌زمینه GPS:
- در `AndroidManifest.xml`: `android:foregroundServiceType="dataSync|location"`
- در کد، هر دو جای `startForeground(...)`: `ServiceInfo.FOREGROUND_SERVICE_TYPE_DATA_SYNC or ServiceInfo.FOREGROUND_SERVICE_TYPE_LOCATION`
- مجوز `<uses-permission android:name="android.permission.FOREGROUND_SERVICE_LOCATION" />` در مانیفست.

## قانون ۴: قوانین تکمیلی و مهم

- **فقط نشان برای مسیریابی** — هیچ دکمه/تابعی برای Balad یا Google Maps اضافه نشود.
- **اپ باید خام شروع شود** — هیچ سفارش/مشتری/فرش/پیام چت ساختگی در دیتابیس ساخته نشود.
- **صدای هشدار** — استفاده از `RingtoneManager.TYPE_NOTIFICATION` با رفرنس `activeRingtone` و `stop()` قبل از پخش جدید + تایمر ایمنی توقف خودکار.
- **سازگاری Gradle و AGP**
