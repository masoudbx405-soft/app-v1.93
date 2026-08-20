package com.example.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.graphics.Brush
import coil.compose.SubcomposeAsyncImage
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.ui.theme.*
import com.example.utils.FarsiUtils

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DriverLoginScreen(
    onSendOtp: (String) -> Unit,
    onVerifyOtp: (String, String) -> Unit,
    onResetOtp: () -> Unit,
    otpSent: Boolean,
    isLoading: Boolean,
    errorMessage: String?,
    generatedOtpHint: String? = null
) {
    var phoneNumber by remember { mutableStateOf("") }
    var otpCode by remember { mutableStateOf("") }

    // Auto-fill OTP if test code provided in hint
    LaunchedEffect(otpSent, generatedOtpHint) {
        if (otpSent && !generatedOtpHint.isNullOrBlank()) {
            val digits = generatedOtpHint.filter { it.isDigit() }
            if (digits.length in 4..6 && otpCode.isBlank()) {
                otpCode = digits
            }
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(CleanLightBackground)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .imePadding()
                .verticalScroll(rememberScrollState())
                .padding(20.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Spacer(modifier = Modifier.height(20.dp))

            // Main Brand Luxury Banner Image
            Card(
                shape = RoundedCornerShape(20.dp),
                elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                border = androidx.compose.foundation.BorderStroke(1.dp, CleanBluePrimary.copy(alpha = 0.15f)),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 2.dp)
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(20.dp))
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.img_saba_auth_banner_1787262473410),
                        contentDescription = "بنر قالیشویی صبا",
                        modifier = Modifier
                            .fillMaxWidth()
                            .aspectRatio(16f / 9f),
                        contentScale = ContentScale.Crop
                    )

                    // Gradient overlay with Brand Title
                    Box(
                        modifier = Modifier
                            .matchParentSize()
                            .background(
                                Brush.verticalGradient(
                                    colors = listOf(
                                        Color.Transparent,
                                        Color.Black.copy(alpha = 0.65f)
                                    ),
                                    startY = 100f
                                )
                            )
                    )

                    Column(
                        modifier = Modifier
                            .align(Alignment.BottomStart)
                            .padding(16.dp)
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            Surface(
                                shape = RoundedCornerShape(6.dp),
                                color = CleanPurpleAccent
                            ) {
                                Text(
                                    text = "صبا",
                                    color = Color.White,
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.ExtraBold,
                                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp)
                                )
                            }
                            Text(
                                text = "قالیشویی تخصصی صبا",
                                color = Color.White,
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                        Text(
                            text = "سامانه هوشمند مدیریت ناوگان و رانندگان",
                            color = Color(0xFFE0E7FF),
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Medium
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Login Card
            Card(
                shape = RoundedCornerShape(24.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    // Title and subtitle
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Surface(
                            shape = CircleShape,
                            color = CleanBlueContainer,
                            modifier = Modifier.size(36.dp)
                        ) {
                            Box(contentAlignment = Alignment.Center) {
                                Icon(
                                    Icons.Default.VerifiedUser,
                                    contentDescription = null,
                                    tint = CleanBluePrimary,
                                    modifier = Modifier.size(20.dp)
                                )
                            }
                        }
                        Text(
                            text = "ورود سفیران و رانندگان صبا",
                            fontSize = 17.sp,
                            fontWeight = FontWeight.Bold,
                            color = CleanLightOnSurface
                        )
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    Text(
                        text = "احراز هویت و ورود به پنل عملیاتی رانندگان",
                        fontSize = 12.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        textAlign = TextAlign.Center
                    )

                    Spacer(modifier = Modifier.height(20.dp))

                    // Error Alert Banner if any
                    if (!errorMessage.isNullOrEmpty()) {
                        Surface(
                            shape = RoundedCornerShape(12.dp),
                            color = CleanRedContainer,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(bottom = 16.dp)
                        ) {
                            Row(
                                modifier = Modifier.padding(12.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(
                                    Icons.Default.ErrorOutline,
                                    contentDescription = null,
                                    tint = CleanRedAccent,
                                    modifier = Modifier.size(20.dp)
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(
                                    text = errorMessage,
                                    fontSize = 12.sp,
                                    color = CleanRedAccent,
                                    fontWeight = FontWeight.Medium
                                )
                            }
                        }
                    }

                    if (!otpSent) {
                        // Step 1: Phone Number Input
                        OutlinedTextField(
                            value = phoneNumber,
                            onValueChange = { phoneNumber = it },
                            label = { Text("شماره همراه راننده (مثال: ۰۹۱۲۳۴۵۶۷۸۹)", fontSize = 12.sp) },
                            singleLine = true,
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
                            leadingIcon = {
                                Icon(
                                    Icons.Default.PhoneIphone,
                                    contentDescription = null,
                                    tint = CleanBluePrimary
                                )
                            },
                            shape = RoundedCornerShape(14.dp),
                            modifier = Modifier.fillMaxWidth(),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = CleanBluePrimary,
                                unfocusedBorderColor = Color.LightGray
                            )
                        )

                        Spacer(modifier = Modifier.height(12.dp))

                        Surface(
                            shape = RoundedCornerShape(12.dp),
                            color = CleanBlueContainer.copy(alpha = 0.5f),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Row(
                                modifier = Modifier.padding(12.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(
                                    Icons.Default.Info,
                                    contentDescription = null,
                                    tint = CleanBluePrimary,
                                    modifier = Modifier.size(18.dp)
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(
                                    text = "شماره همراه سفیر در پنل مرکزی صبا استعلام شده و کد یکبارمصرف ارسال می‌شود.",
                                    fontSize = 11.sp,
                                    color = CleanBlueOnContainer,
                                    lineHeight = 16.sp
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(18.dp))

                        Button(
                            onClick = { onSendOtp(phoneNumber) },
                            enabled = !isLoading && phoneNumber.isNotBlank(),
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(50.dp),
                            shape = RoundedCornerShape(14.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = CleanBluePrimary)
                        ) {
                            if (isLoading) {
                                CircularProgressIndicator(
                                    color = Color.White,
                                    modifier = Modifier.size(24.dp),
                                    strokeWidth = 2.5.dp
                                )
                                Spacer(modifier = Modifier.width(10.dp))
                                Text("در حال استعلام از پنل...", fontSize = 14.sp)
                            } else {
                                Icon(Icons.Default.Send, contentDescription = null, modifier = Modifier.size(18.dp))
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(
                                    text = "استعلام شماره و دریافت کد تایید",
                                    fontSize = 14.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }
                    } else {
                        // Step 2: OTP Code Input
                        Surface(
                            shape = RoundedCornerShape(14.dp),
                            color = CleanTealContainer.copy(alpha = 0.7f),
                            border = androidx.compose.foundation.BorderStroke(1.dp, CleanTealAccent.copy(alpha = 0.3f)),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Row(
                                modifier = Modifier.padding(12.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Icon(
                                        Icons.Default.MarkEmailRead,
                                        contentDescription = null,
                                        tint = CleanTealAccent,
                                        modifier = Modifier.size(22.dp)
                                    )
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Column {
                                        Text(
                                            text = "کد تایید یکبارمصرف ارسال شد به:",
                                            fontSize = 11.sp,
                                            color = CleanTealAccent
                                        )
                                        Text(
                                            text = FarsiUtils.toFarsiDigits(phoneNumber),
                                            fontSize = 14.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = CleanTealAccent
                                        )
                                    }
                                }
                                TextButton(
                                    onClick = { onResetOtp() },
                                    colors = ButtonDefaults.textButtonColors(contentColor = CleanPurpleAccent)
                                ) {
                                    Icon(Icons.Default.Edit, contentDescription = null, modifier = Modifier.size(14.dp))
                                    Spacer(modifier = Modifier.width(4.dp))
                                    Text("تغییر شماره", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                                }
                            }
                        }

                        if (!generatedOtpHint.isNullOrBlank()) {
                            Spacer(modifier = Modifier.height(10.dp))
                            Surface(
                                shape = RoundedCornerShape(10.dp),
                                color = CleanPurpleContainer.copy(alpha = 0.5f),
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Row(
                                    modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Icon(
                                        Icons.Default.Info,
                                        contentDescription = null,
                                        tint = CleanPurpleAccent,
                                        modifier = Modifier.size(16.dp)
                                    )
                                    Spacer(modifier = Modifier.width(6.dp))
                                    Text(
                                        text = generatedOtpHint,
                                        fontSize = 11.sp,
                                        color = CleanPurpleAccent,
                                        fontWeight = FontWeight.Medium
                                    )
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(14.dp))

                        OutlinedTextField(
                            value = otpCode,
                            onValueChange = { if (it.length <= 6) otpCode = it },
                            label = { Text("کد تایید پیامک‌شده (۵ رقم)", fontSize = 12.sp) },
                            placeholder = { Text("مثال: ۱۲۳۴۵", fontSize = 12.sp, color = Color.Gray) },
                            singleLine = true,
                            textStyle = androidx.compose.ui.text.TextStyle(
                                fontSize = 20.sp,
                                fontWeight = FontWeight.Bold,
                                textAlign = TextAlign.Center,
                                letterSpacing = 6.sp
                            ),
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                            leadingIcon = {
                                Icon(
                                    Icons.Default.Pin,
                                    contentDescription = null,
                                    tint = CleanPurpleAccent
                                )
                            },
                            trailingIcon = {
                                if (otpCode.isNotEmpty()) {
                                    IconButton(onClick = { otpCode = "" }) {
                                        Icon(Icons.Default.Clear, contentDescription = "پاک کردن", modifier = Modifier.size(18.dp))
                                    }
                                }
                            },
                            shape = RoundedCornerShape(14.dp),
                            modifier = Modifier.fillMaxWidth(),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = CleanPurpleAccent,
                                unfocusedBorderColor = Color.LightGray
                            )
                        )

                        Spacer(modifier = Modifier.height(18.dp))

                        Button(
                            onClick = { onVerifyOtp(phoneNumber, otpCode) },
                            enabled = !isLoading && otpCode.isNotBlank(),
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(52.dp),
                            shape = RoundedCornerShape(14.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = CleanPurpleAccent)
                        ) {
                            if (isLoading) {
                                CircularProgressIndicator(
                                    color = Color.White,
                                    modifier = Modifier.size(24.dp),
                                    strokeWidth = 2.5.dp
                                )
                                Spacer(modifier = Modifier.width(10.dp))
                                Text("در حال تایید ورود...", fontSize = 14.sp)
                            } else {
                                Icon(Icons.Default.CheckCircle, contentDescription = null, modifier = Modifier.size(20.dp))
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(
                                    text = "تایید و ورود به پنل رانندگان",
                                    fontSize = 14.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(10.dp))

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.Center,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            TextButton(
                                onClick = { onSendOtp(phoneNumber) },
                                enabled = !isLoading
                            ) {
                                Icon(Icons.Default.Refresh, contentDescription = null, modifier = Modifier.size(16.dp), tint = CleanPurpleAccent)
                                Spacer(modifier = Modifier.width(4.dp))
                                Text("ارسال مجدد کد پیامکی", fontSize = 12.sp, color = CleanPurpleAccent)
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            Text(
                text = "سامانه هوشمند مدیریت ناوگان و قالیشویی صبا © ۱۴۰۵",
                fontSize = 11.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(20.dp))
        }
    }
}
