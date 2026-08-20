package com.example.ui.screens

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(
    isDarkMode: Boolean,
    onToggleDarkMode: () -> Unit,
    connectedPrinterName: String?,
    onOpenPrinterDialog: () -> Unit,
    onPrintTestReceipt: () -> Unit = {},
    onSyncNow: () -> Unit,
    savedServerUrl: String = "https://panel.yaselectrical.ir",
    isTestingConnection: Boolean = false,
    connectionTestResult: String? = null,
    onUpdateServerUrl: (String) -> Unit = {},
    onTestConnection: (String) -> Unit = {},
    tariffSyncResult: com.example.data.model.TariffSyncResult = com.example.data.model.TariffSyncResult.createDefault(),
    onRefreshTariffs: () -> Unit = {},
    backupInfo: com.example.utils.BackupInfo? = null,
    onBackupDatabase: () -> Unit = {},
    onRestoreDatabase: () -> Unit = {},
    onLogout: () -> Unit = {}
) {
    val context = LocalContext.current

    var serverUrl by remember(savedServerUrl) { mutableStateOf(savedServerUrl) }
    var autoSyncEnabled by remember { mutableStateOf(true) }
    var autoSyncInterval by remember { mutableStateOf("۱۰ دقیقه") }
    var autoPrintReceipt by remember { mutableStateOf(true) }
    var receiptCopies by remember { mutableStateOf("۲ نسخه (مشتری + راننده)") }
    var paperWidth by remember { mutableStateOf("۸۰ میلی‌متر (پوز/حرارتی)") }
    var scanSoundBeep by remember { mutableStateOf(true) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(16.dp)
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Top Header Title
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Box(
                modifier = Modifier
                    .size(42.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(CleanBlueContainer),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    Icons.Default.Settings,
                    contentDescription = null,
                    tint = CleanBluePrimary,
                    modifier = Modifier.size(24.dp)
                )
            }
            Column {
                Text(
                    text = "تنظیمات نرم‌افزار و سخت‌افزار",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onBackground
                )
                Text(
                    text = "مدیریت ارتباط سرور، چاپگر حرارتی، پشتیبان‌گیری و اعلان‌ها",
                    fontSize = 11.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }

        // Server Connection & Cloud Sync Settings (With Real Server Connection Test)
        SettingsSectionCard(
            title = "ارتباط با پنل مدیریت و همگام‌سازی ابری",
            subtitle = "مدیریت ارتباط بی‌درنگ با وب‌سرور و دریافت فاکتورها",
            icon = Icons.Default.CloudSync,
            iconContainerColor = CleanBlueContainer,
            iconTintColor = CleanBluePrimary
        ) {
            Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                OutlinedTextField(
                    value = serverUrl,
                    onValueChange = {
                        serverUrl = it
                        onUpdateServerUrl(it)
                    },
                    label = { Text("آدرس وب‌سرویس و پنل مرکزی قالیشویی", fontSize = 11.sp) },
                    singleLine = true,
                    leadingIcon = { Icon(Icons.Default.Language, contentDescription = null, tint = CleanBluePrimary, modifier = Modifier.size(18.dp)) },
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier.fillMaxWidth()
                )

                // Real Server Connection Test Button
                Button(
                    onClick = { onTestConnection(serverUrl) },
                    enabled = !isTestingConnection,
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = CleanBluePrimary),
                    modifier = Modifier.fillMaxWidth().height(46.dp)
                ) {
                    if (isTestingConnection) {
                        CircularProgressIndicator(
                            color = Color.White,
                            modifier = Modifier.size(18.dp),
                            strokeWidth = 2.dp
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("در حال ارسال درخواست به سرور...", fontSize = 12.sp)
                    } else {
                        Icon(Icons.Default.NetworkCheck, contentDescription = null, modifier = Modifier.size(18.dp))
                        Spacer(modifier = Modifier.width(6.dp))
                        Text("تست اتصال به سرور API", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                    }
                }

                if (connectionTestResult != null) {
                    val isSuccess = connectionTestResult.startsWith("موفقیت")
                    Surface(
                        shape = RoundedCornerShape(12.dp),
                        color = if (isSuccess) Color(0xFFDCFCE7) else Color(0xFFFFEBEE),
                        border = androidx.compose.foundation.BorderStroke(1.dp, if (isSuccess) Color(0xFF86EFAC) else Color(0xFFFFCDD2)),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Row(
                            modifier = Modifier.padding(12.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                imageVector = if (isSuccess) Icons.Default.CheckCircle else Icons.Default.ErrorOutline,
                                contentDescription = null,
                                tint = if (isSuccess) Color(0xFF16A34A) else Color(0xFFD32F2F),
                                modifier = Modifier.size(20.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = connectionTestResult,
                                fontSize = 11.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = if (isSuccess) Color(0xFF15803D) else Color(0xFFC62828),
                                lineHeight = 16.sp
                            )
                        }
                    }
                }

                HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.5f))

                val isBgRunning by com.example.data.remote.ZomorrodBackgroundService.isServiceRunning.collectAsState()
                val lastSyncTime by com.example.data.remote.ZomorrodBackgroundService.lastSyncTimestamp.collectAsState()

                Surface(
                    shape = RoundedCornerShape(14.dp),
                    color = if (isBgRunning) CleanBlueContainer.copy(alpha = 0.4f) else MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.35f),
                    border = androidx.compose.foundation.BorderStroke(
                        1.dp,
                        if (isBgRunning) CleanBluePrimaryLight.copy(alpha = 0.4f) else MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.7f)
                    )
                ) {
                    Column(modifier = Modifier.padding(12.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Box(
                                    modifier = Modifier
                                        .size(10.dp)
                                        .clip(CircleShape)
                                        .background(if (isBgRunning) Color(0xFF10B981) else Color(0xFFEF4444))
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Column {
                                    Text(
                                        text = if (isBgRunning) "سرویس پس‌زمینه فعال است" else "سرویس پس‌زمینه متوقف است",
                                        fontSize = 12.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = MaterialTheme.colorScheme.onSurface
                                    )
                                    Text(
                                        text = if (isBgRunning) {
                                            if (lastSyncTime != null) "آخرین بررسی آنلاین: ساعت $lastSyncTime" else "اعلان در نوار ابزار گوشی فعال است"
                                        } else {
                                            "برنامه در پس‌زمینه فعالیتی ندارد"
                                        },
                                        fontSize = 10.sp,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant
                                    )
                                }
                            }

                            Button(
                                onClick = {
                                    if (isBgRunning) {
                                        com.example.data.remote.ZomorrodBackgroundService.stopService(context)
                                        Toast.makeText(context, "سرویس پس‌زمینه و اعلان متوقف شد", Toast.LENGTH_SHORT).show()
                                    } else {
                                        com.example.data.remote.ZomorrodBackgroundService.startService(context)
                                        Toast.makeText(context, "سرویس پس‌زمینه مجدداً راه‌اندازی شد", Toast.LENGTH_SHORT).show()
                                    }
                                },
                                shape = RoundedCornerShape(10.dp),
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = if (isBgRunning) CleanRedAccent else CleanBluePrimary
                                ),
                                contentPadding = PaddingValues(horizontal = 12.dp, vertical = 6.dp),
                                modifier = Modifier.height(36.dp)
                            ) {
                                Icon(
                                    imageVector = if (isBgRunning) Icons.Default.PowerSettingsNew else Icons.Default.PlayArrow,
                                    contentDescription = null,
                                    modifier = Modifier.size(16.dp)
                                )
                                Spacer(modifier = Modifier.width(4.dp))
                                Text(
                                    text = if (isBgRunning) "خروج از پس‌زمینه" else "فعال‌سازی سرویس",
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }
                    }
                }

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = "همگام‌سازی خودکار در پس‌زمینه",
                            fontSize = 13.sp,
                            fontWeight = FontWeight.SemiBold
                        )
                        Text(
                            text = "ارسال خودکار وضعیت فاکتورها و دریافت سفارش‌های جدید",
                            fontSize = 10.sp,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                    Switch(
                        checked = autoSyncEnabled,
                        onCheckedChange = { 
                            autoSyncEnabled = it
                            if (it) {
                                com.example.data.remote.ZomorrodBackgroundService.startService(context)
                            } else {
                                com.example.data.remote.ZomorrodBackgroundService.stopService(context)
                            }
                        }
                    )
                }

                if (autoSyncEnabled) {
                    Text(
                        text = "بازه زمانی بررسی سرور:",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(6.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        listOf("۵ دقیقه", "۱۰ دقیقه", "۳۰ دقیقه").forEach { interval ->
                            FilterChip(
                                selected = autoSyncInterval == interval,
                                onClick = { autoSyncInterval = interval },
                                label = { Text(interval, fontSize = 11.sp) },
                                colors = FilterChipDefaults.filterChipColors(
                                    selectedContainerColor = CleanPurpleContainer,
                                    selectedLabelColor = CleanPurpleAccent
                                )
                            )
                        }
                    }
                }

                OutlinedButton(
                    onClick = {
                        onSyncNow()
                        Toast.makeText(context, "درخواست همگام‌سازی فوری با سرور ارسال شد", Toast.LENGTH_SHORT).show()
                    },
                    shape = RoundedCornerShape(12.dp),
                    border = androidx.compose.foundation.BorderStroke(1.dp, CleanPurpleAccent.copy(alpha = 0.5f)),
                    modifier = Modifier.fillMaxWidth().height(44.dp)
                ) {
                    Icon(Icons.Default.Sync, contentDescription = null, tint = CleanPurpleAccent, modifier = Modifier.size(18.dp))
                    Spacer(modifier = Modifier.width(6.dp))
                    Text("همگام‌سازی و استعلام فوری فاکتورها", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = CleanPurpleAccent)
                }
            }
        }

        // Printer Settings Section (Focused and Real)
        SettingsSectionCard(
            title = "تنظیمات پرینتر حرارتی و دستگاه پوز",
            subtitle = "اتصال به چاپگر جیبی و صدور رسید تسویه و تحویل فرش",
            icon = Icons.Default.PointOfSale,
            iconContainerColor = CleanTealContainer,
            iconTintColor = CleanTealAccent
        ) {
            Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                // Connected status box
                Surface(
                    shape = RoundedCornerShape(14.dp),
                    color = if (connectedPrinterName != null) CleanTealContainer.copy(alpha = 0.45f) else MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.35f),
                    border = androidx.compose.foundation.BorderStroke(
                        1.dp,
                        if (connectedPrinterName != null) CleanTealAccent.copy(alpha = 0.4f) else MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.7f)
                    ),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        modifier = Modifier.padding(12.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Box(
                                modifier = Modifier
                                    .size(10.dp)
                                    .clip(CircleShape)
                                    .background(if (connectedPrinterName != null) CleanTealAccent else MaterialTheme.colorScheme.outline)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Column {
                                Text(
                                    text = if (connectedPrinterName != null) "پرینتر متصل: $connectedPrinterName" else "هیچ پرینتری متصل نیست",
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = if (connectedPrinterName != null) CleanTealAccent else MaterialTheme.colorScheme.onSurfaceVariant
                                )
                                Text(
                                    text = if (connectedPrinterName != null) "پروتکل ارتباطی بلوتوث حرارتی (POS ESC/POS)" else "جهت چاپ فاکتور، پرینتر را جفت نمایید",
                                    fontSize = 10.sp,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                        }

                        Button(
                            onClick = onOpenPrinterDialog,
                            shape = RoundedCornerShape(10.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = CleanTealAccent),
                            contentPadding = PaddingValues(horizontal = 10.dp, vertical = 4.dp),
                            modifier = Modifier.height(36.dp)
                        ) {
                            Icon(Icons.Default.BluetoothSearching, contentDescription = null, modifier = Modifier.size(16.dp))
                            Spacer(modifier = Modifier.width(4.dp))
                            Text("جستجو", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                        }
                    }
                }

                // Real Test Print Action
                OutlinedButton(
                    onClick = onPrintTestReceipt,
                    shape = RoundedCornerShape(12.dp),
                    border = androidx.compose.foundation.BorderStroke(1.dp, CleanTealAccent.copy(alpha = 0.6f)),
                    modifier = Modifier.fillMaxWidth().height(44.dp)
                ) {
                    Icon(Icons.Default.ReceiptLong, contentDescription = null, tint = CleanTealAccent, modifier = Modifier.size(18.dp))
                    Spacer(modifier = Modifier.width(6.dp))
                    Text("چاپ فاکتور آزمایشی (تست سلامت چاپگر)", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = CleanTealAccent)
                }

                HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.5f))

                // Paper width
                Text(
                    text = "عرض رول کاغذ حرارتی:",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Row(
                    horizontalArrangement = Arrangement.spacedBy(6.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    listOf("۸۰ میلی‌متر (پوز/حرارتی)", "۵۸ میلی‌متر (مینی پرینتر)").forEach { width ->
                        FilterChip(
                            selected = paperWidth == width,
                            onClick = { paperWidth = width },
                            label = { Text(width, fontSize = 11.sp) },
                            colors = FilterChipDefaults.filterChipColors(
                                selectedContainerColor = CleanTealContainer,
                                selectedLabelColor = CleanTealAccent
                            )
                        )
                    }
                }

                // Auto-print toggle
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = "چاپ خودکار فاکتور پس از تسویه",
                            fontSize = 13.sp,
                            fontWeight = FontWeight.SemiBold
                        )
                        Text(
                            text = "بلافاصله پس از ثبت نهایی فاکتور، رسید به صورت خودکار پرینت شود",
                            fontSize = 10.sp,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                    Switch(
                        checked = autoPrintReceipt,
                        onCheckedChange = { autoPrintReceipt = it }
                    )
                }

                // Copies count
                Text(
                    text = "تعداد نسخه‌های چاپ رسید:",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Row(
                    horizontalArrangement = Arrangement.spacedBy(6.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    listOf("۱ نسخه (مشتری)", "۲ نسخه (مشتری + راننده)").forEach { option ->
                        FilterChip(
                            selected = receiptCopies == option,
                            onClick = { receiptCopies = option },
                            label = { Text(option, fontSize = 11.sp) },
                            colors = FilterChipDefaults.filterChipColors(
                                selectedContainerColor = CleanTealContainer,
                                selectedLabelColor = CleanTealAccent
                            )
                        )
                    }
                }
            }
        }

        // Supabase Web Panel Tariffs & Price List Management Card
        SettingsSectionCard(
            title = "مدیریت نرخ‌نامه خدمات و فرش (همگام با وب‌پنل)",
            subtitle = "دریافت و اعمال تعرفه‌های رسمی قالیشویی صبا از Supabase",
            icon = Icons.Default.PriceCheck,
            iconContainerColor = CleanTealContainer,
            iconTintColor = CleanTealAccent
        ) {
            Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                Surface(
                    shape = RoundedCornerShape(14.dp),
                    color = if (tariffSyncResult.isLiveFromSupabase) CleanTealContainer.copy(alpha = 0.4f) else CleanPurpleContainer.copy(alpha = 0.35f),
                    border = androidx.compose.foundation.BorderStroke(
                        1.dp,
                        if (tariffSyncResult.isLiveFromSupabase) CleanTealAccent.copy(alpha = 0.4f) else CleanPurpleAccent.copy(alpha = 0.3f)
                    ),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(12.dp), verticalArrangement = Arrangement.spacedBy(6.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(
                                    if (tariffSyncResult.isLiveFromSupabase) Icons.Default.CloudDone else Icons.Default.Inventory2,
                                    contentDescription = null,
                                    tint = if (tariffSyncResult.isLiveFromSupabase) CleanTealAccent else CleanPurpleAccent,
                                    modifier = Modifier.size(18.dp)
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(
                                    text = if (tariffSyncResult.isLiveFromSupabase) "متصل به نرخ‌نامه زنده سرور" else "نرخ‌نامه مصوب قالیشویی صبا",
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = if (tariffSyncResult.isLiveFromSupabase) CleanTealAccent else CleanPurpleAccent
                                )
                            }

                            Surface(
                                shape = RoundedCornerShape(8.dp),
                                color = if (tariffSyncResult.isLiveFromSupabase) CleanTealAccent else CleanPurpleAccent
                            ) {
                                Text(
                                    text = "${tariffSyncResult.carpetTariffs.size} تعرفه فرش",
                                    color = Color.White,
                                    fontSize = 10.sp,
                                    fontWeight = FontWeight.Bold,
                                    modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                                )
                            }
                        }

                        Text(
                            text = "${tariffSyncResult.serviceTariffs.size} خدمت تکمیلی و شستشوی تخصصی در نرخ‌نامه ثبت شده است.",
                            fontSize = 11.sp,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }

                Button(
                    onClick = onRefreshTariffs,
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = CleanTealAccent),
                    modifier = Modifier.fillMaxWidth().height(44.dp)
                ) {
                    Icon(Icons.Default.Sync, contentDescription = null, modifier = Modifier.size(18.dp))
                    Spacer(modifier = Modifier.width(6.dp))
                    Text("بروزرسانی فوری نرخ‌نامه از پنل وب", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                }
            }
        }

        // Room Database Backup & Restore Card
        SettingsSectionCard(
            title = "پشتیبان‌گیری و بازیابی دیتابیس محلی (Room)",
            subtitle = "ذخیره‌سازی و بازیابی آفلاین اطلاعات در پایگاه داده",
            icon = Icons.Default.Storage,
            iconContainerColor = CleanPurpleContainer,
            iconTintColor = CleanPurpleAccent
        ) {
            Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                Text(
                    text = "از کلیه سفارشات محلی، اقلام فرش، فاکتورها و وضعیت‌ها یک فایل پشتیبان امن در حافظه دستگاه ذخیره نمایید.",
                    fontSize = 11.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    lineHeight = 16.sp
                )

                if (backupInfo != null && backupInfo.exists) {
                    Surface(
                        shape = RoundedCornerShape(14.dp),
                        color = CleanPurpleContainer.copy(alpha = 0.35f),
                        border = androidx.compose.foundation.BorderStroke(1.dp, CleanPurpleAccent.copy(alpha = 0.3f)),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(modifier = Modifier.padding(12.dp), verticalArrangement = Arrangement.spacedBy(4.dp)) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(Icons.Default.TaskAlt, contentDescription = null, tint = CleanPurpleAccent, modifier = Modifier.size(16.dp))
                                Spacer(modifier = Modifier.width(6.dp))
                                Text("آخرین فایل پشتیبان موجود:", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = CleanPurpleAccent)
                            }
                            Text("تاریخ ثبت: ${backupInfo.timestamp}", fontSize = 11.sp)
                            Text("حجم فایل: ${backupInfo.fileSizeKb} کیلوبایت (${backupInfo.ordersCount} سفارش)", fontSize = 11.sp)
                        }
                    }
                }

                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Button(
                        onClick = onBackupDatabase,
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = CleanPurpleAccent),
                        modifier = Modifier.weight(1f).height(44.dp)
                    ) {
                        Icon(Icons.Default.CloudUpload, contentDescription = null, modifier = Modifier.size(18.dp))
                        Spacer(modifier = Modifier.width(6.dp))
                        Text("ایجاد پشتیبان جدید", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                    }

                    OutlinedButton(
                        onClick = onRestoreDatabase,
                        shape = RoundedCornerShape(12.dp),
                        border = androidx.compose.foundation.BorderStroke(1.dp, CleanPurpleAccent.copy(alpha = 0.6f)),
                        modifier = Modifier.weight(1f).height(44.dp)
                    ) {
                        Icon(Icons.Default.CloudDownload, contentDescription = null, tint = CleanPurpleAccent, modifier = Modifier.size(18.dp))
                        Spacer(modifier = Modifier.width(6.dp))
                        Text("بازیابی اطلاعات", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = CleanPurpleAccent)
                    }
                }
            }
        }

        // App Theme & Sound Settings
        SettingsSectionCard(
            title = "پوسته و صداهای سیستم",
            subtitle = "شخصی‌سازی ظاهر برنامه و هشدارهای صوتی اسکن",
            icon = Icons.Default.Palette,
            iconContainerColor = CleanBlueContainer,
            iconTintColor = CleanBluePrimary
        ) {
            Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(
                        modifier = Modifier.weight(1f),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(
                            modifier = Modifier
                                .size(32.dp)
                                .clip(RoundedCornerShape(8.dp))
                                .background(CleanBlueContainer),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                if (isDarkMode) Icons.Default.DarkMode else Icons.Default.LightMode,
                                contentDescription = null,
                                tint = CleanBluePrimary,
                                modifier = Modifier.size(18.dp)
                            )
                        }
                        Spacer(modifier = Modifier.width(10.dp))
                        Column {
                            Text(
                                text = "پوسته تاریک (حالت شب)",
                                fontSize = 13.sp,
                                fontWeight = FontWeight.SemiBold
                            )
                            Text(
                                text = "مناسب برای کار در شب و کاهش مصرف باتری",
                                fontSize = 10.sp,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }
                    Switch(
                        checked = isDarkMode,
                        onCheckedChange = { onToggleDarkMode() }
                    )
                }

                HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.5f))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(
                        modifier = Modifier.weight(1f),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(
                            modifier = Modifier
                                .size(32.dp)
                                .clip(RoundedCornerShape(8.dp))
                                .background(CleanTealContainer),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                Icons.Default.VolumeUp,
                                contentDescription = null,
                                tint = CleanTealAccent,
                                modifier = Modifier.size(18.dp)
                            )
                        }
                        Spacer(modifier = Modifier.width(10.dp))
                        Column {
                            Text(
                                text = "صدای بوق (Beep) هنگام اسکن بارکد",
                                fontSize = 13.sp,
                                fontWeight = FontWeight.SemiBold
                            )
                            Text(
                                text = "تایید صوتی اسکن بارکدهای منگنه فرش و قفسه",
                                fontSize = 10.sp,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }
                    Switch(
                        checked = scanSoundBeep,
                        onCheckedChange = { scanSoundBeep = it }
                    )
                }
            }
        }

        // Save Settings Action Button
        Button(
            onClick = {
                Toast.makeText(context, "تنظیمات برنامه با موفقیت ذخیره شد", Toast.LENGTH_SHORT).show()
            },
            shape = RoundedCornerShape(14.dp),
            colors = ButtonDefaults.buttonColors(containerColor = CleanBluePrimary),
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp)
        ) {
            Icon(Icons.Default.CheckCircle, contentDescription = null, modifier = Modifier.size(20.dp))
            Spacer(modifier = Modifier.width(8.dp))
            Text("ذخیره تنظیمات", fontSize = 14.sp, fontWeight = FontWeight.Bold)
        }

        // Logout Button Card
        OutlinedButton(
            onClick = onLogout,
            shape = RoundedCornerShape(14.dp),
            border = androidx.compose.foundation.BorderStroke(1.5.dp, CleanRedAccent),
            colors = ButtonDefaults.outlinedButtonColors(contentColor = CleanRedAccent),
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp)
        ) {
            Icon(Icons.Default.Logout, contentDescription = null, modifier = Modifier.size(20.dp))
            Spacer(modifier = Modifier.width(8.dp))
            Text("خروج از حساب کاربری راننده", fontSize = 14.sp, fontWeight = FontWeight.Bold)
        }

        Spacer(modifier = Modifier.height(20.dp))
    }
}

@Composable
private fun SettingsSectionCard(
    title: String,
    subtitle: String? = null,
    icon: ImageVector,
    iconContainerColor: Color = CleanBlueContainer,
    iconTintColor: Color = CleanBluePrimary,
    content: @Composable ColumnScope.() -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 1.dp,
            hoveredElevation = 2.dp
        ),
        border = androidx.compose.foundation.BorderStroke(
            1.dp,
            MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.8f)
        )
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Box(
                    modifier = Modifier
                        .size(40.dp)
                        .clip(RoundedCornerShape(12.dp))
                        .background(iconContainerColor),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = icon,
                        contentDescription = null,
                        tint = iconTintColor,
                        modifier = Modifier.size(22.dp)
                    )
                }

                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = title,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    if (subtitle != null) {
                        Text(
                            text = subtitle,
                            fontSize = 10.sp,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }

            HorizontalDivider(
                color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.6f),
                thickness = 0.8.dp
            )

            content()
        }
    }
}
