package com.example.simplecalculator

import android.content.ClipData
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.net.Uri
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.core.content.edit
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.ContentCopy
import androidx.compose.material.icons.outlined.History
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.ClipEntry
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalClipboard
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import java.math.BigDecimal
import java.math.RoundingMode
import java.text.DecimalFormat
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale
import kotlinx.coroutines.launch
import kotlin.math.abs

private val AppBackgroundBrush = Brush.linearGradient(
    listOf(Color(0xFF101011), Color(0xFF060607), Color(0xFF121214))
)
private val PanelBrush = Brush.verticalGradient(
    listOf(Color(0xFF2A2B2F), Color(0xFF18191D))
)
private val NumberBrush = Brush.verticalGradient(
    listOf(Color(0xFF2F3136), Color(0xFF1B1D22))
)
private val UtilityBrush = Brush.verticalGradient(
    listOf(Color(0xFF303238), Color(0xFF1A1C21))
)
private val SpecialBrush = Brush.verticalGradient(
    listOf(Color(0xFF6A4348), Color(0xFF4C2E33))
)
private val AccentBrush = Brush.verticalGradient(
    listOf(Color(0xFF34BC72), Color(0xFF23975A))
)
private val AccentActiveBrush = Brush.verticalGradient(
    listOf(Color(0xFF4DCC87), Color(0xFF2BAE66))
)
private val AccentBorder = Color(0xFF2BAE66)
private val AccentBorderActive = Color(0xFF75D8A0)
private val WarmText = Color(0xFFF5F1E8)
private val SoftText = Color(0xFFE7DED0)
private val SubtleBorder = Color(0xFF3A3C42)
private val DangerText = Color(0xFFFF8C8C)
private val DangerBorder = Color(0xFF82535A)
private val DangerSoft = Color(0xFF96666E)
private val SurfaceColor = Color(0xFF17181B)

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MaterialTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = Color(0xFF0B0B0C)
                ) {
                    CalculatorScreen()
                }
            }
        }
    }
}

private object CalculatorHistoryStore {
    private const val PREFS_NAME = "calculator_history"
    private const val KEY_ENTRIES = "entries"
    private const val ENTRY_SEPARATOR = "\u001E"
    private const val FIELD_SEPARATOR = "\u001F"

    fun load(context: Context): List<CalculatorHistoryEntry> {
        val saved = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
            .getString(KEY_ENTRIES, "")
            .orEmpty()

        return if (saved.isBlank()) {
            emptyList()
        } else {
            saved.split(ENTRY_SEPARATOR)
                .filter { it.isNotBlank() }
                .map { encodedEntry ->
                    val parts = encodedEntry.split(FIELD_SEPARATOR, limit = 2)
                    if (parts.size == 2) {
                        CalculatorHistoryEntry(
                            expression = parts[1],
                            timestamp = parts[0].toLongOrNull() ?: 0L
                        )
                    } else {
                        CalculatorHistoryEntry(
                            expression = encodedEntry,
                            timestamp = 0L
                        )
                    }
                }
        }
    }

    fun save(context: Context, entries: List<CalculatorHistoryEntry>) {
        context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
            .edit {
                putString(
                    KEY_ENTRIES,
                    entries.joinToString(ENTRY_SEPARATOR) { entry ->
                        "${entry.timestamp}$FIELD_SEPARATOR${entry.expression}"
                    }
                )
            }
    }
}

data class CalculatorHistoryEntry(
    val expression: String,
    val timestamp: Long
)

@Composable
fun CalculatorScreen() {
    val context = LocalContext.current
    val clipboard = LocalClipboard.current
    val hapticFeedback = LocalHapticFeedback.current
    val coroutineScope = rememberCoroutineScope()
    val calculatorTypography = rememberCalculatorTypography()
    var currentValue by remember { mutableStateOf("0") }
    var previousValue by remember { mutableStateOf("") }
    var operator by remember { mutableStateOf<String?>(null) }
    var shouldResetDisplay by remember { mutableStateOf(false) }
    var expression by remember { mutableStateOf(" ") }
    var currentScreen by remember { mutableStateOf(CalculatorScreenMode.CALCULATOR) }
    var historyEntries by remember { mutableStateOf(CalculatorHistoryStore.load(context)) }
    var activeHistoryEntry by remember { mutableStateOf("") }
    var activeHistoryTimestamp by remember { mutableStateOf<Long?>(null) }
    var activeExpressionChain by remember { mutableStateOf("") }
    val scientificFormatter = remember { DecimalFormat("0.##E0") }

    fun formatNumber(num: String): String {
        if (num == "Error") return num
        if (num.isBlank()) return "0"

        val hasTrailingDecimal = num.endsWith(".")
        val numericPortion = if (hasTrailingDecimal) num.dropLast(1) else num
        if (numericPortion.isBlank() || numericPortion == "-") return num

        val parsed = numericPortion.toBigDecimalOrNull() ?: return num
        val rounded = parsed.setScale(2, RoundingMode.HALF_UP)
        val normalized = rounded.stripTrailingZeros()
        val absValue = normalized.abs()

        if (absValue != BigDecimal.ZERO &&
            (absValue >= BigDecimal("1000000000000") || absValue < BigDecimal("0.000001"))
        ) {
            return scientificFormatter.format(parsed.toDouble())
        }

        val plain = normalized.toPlainString()
        val negative = plain.startsWith("-")
        val unsigned = if (negative) plain.drop(1) else plain
        val parts = unsigned.split(".")
        val integerPart = parts[0]
        val groupedInteger = integerPart
            .reversed()
            .chunked(3)
            .joinToString(",")
            .reversed()
        val decimalPart = parts.getOrNull(1)
        val formatted = buildString {
            if (negative) append("-")
            append(groupedInteger)
            if (decimalPart != null) {
                append(".")
                append(decimalPart)
            } else if (hasTrailingDecimal) {
                append(".")
            }
        }

        return formatted
    }

    fun persistHistory(entries: List<CalculatorHistoryEntry>) {
        historyEntries = entries
        CalculatorHistoryStore.save(context, entries)
    }

    fun withHaptics(action: () -> Unit): () -> Unit = {
        hapticFeedback.performHapticFeedback(HapticFeedbackType.TextHandleMove)
        action()
    }

    fun persistActiveHistory() {
        val entry = activeHistoryEntry.trim()
        if (entry.isNotEmpty()) {
            val existingTimestamp = activeHistoryTimestamp
            if (existingTimestamp == null) {
                val newTimestamp = System.currentTimeMillis()
                activeHistoryTimestamp = newTimestamp
                persistHistory(
                    listOf(
                        CalculatorHistoryEntry(
                            expression = entry,
                            timestamp = newTimestamp
                        )
                    ) + historyEntries
                )
            } else {
                persistHistory(
                    historyEntries.map { savedEntry ->
                        if (savedEntry.timestamp == existingTimestamp) {
                            savedEntry.copy(expression = entry)
                        } else {
                            savedEntry
                        }
                    }
                )
            }
        }
    }

    fun resetHistorySession() {
        activeHistoryEntry = ""
        activeHistoryTimestamp = null
        activeExpressionChain = ""
        expression = " "
    }

    fun appendNumber(num: String) {
        if (shouldResetDisplay && operator == null) {
            resetHistorySession()
        }
        currentValue = if (shouldResetDisplay || currentValue == "Error") {
            shouldResetDisplay = false
            if (num == "00") "0" else num
        } else {
            when {
                currentValue == "0" && num == "00" -> "0"
                currentValue == "0" -> num
                else -> currentValue + num
            }
        }
    }

    fun copyableCalculation(): String {
        val trimmedExpression = expression.trim()
        val formattedCurrent = formatNumber(currentValue)
        return if (trimmedExpression.isNotEmpty()) {
            if (trimmedExpression.endsWith("=")) {
                "$trimmedExpression $formattedCurrent"
            } else {
                "$trimmedExpression $formattedCurrent"
            }
        } else {
            formattedCurrent
        }
    }

    fun appendDecimal() {
        if (shouldResetDisplay && operator == null) {
            resetHistorySession()
        }
        currentValue = if (shouldResetDisplay || currentValue == "Error") {
            shouldResetDisplay = false
            "0."
        } else {
            if (!currentValue.contains(".")) "$currentValue." else currentValue
        }
    }

    fun deleteLastDigit() {
        if (currentValue == "Error") {
            currentValue = "0"
            shouldResetDisplay = false
            return
        }

        if (shouldResetDisplay) {
            currentValue = "0"
            shouldResetDisplay = false
            return
        }

        currentValue = when {
            currentValue.length <= 1 -> "0"
            currentValue.startsWith("-") && currentValue.length == 2 -> "0"
            else -> currentValue.dropLast(1)
        }
    }

    fun clearCurrentNumber() {
        currentValue = "0"
        shouldResetDisplay = false
    }

    fun clearAll() {
        currentValue = "0"
        previousValue = ""
        operator = null
        shouldResetDisplay = false
        resetHistorySession()
    }

    fun clearCurrentEntry() {
        currentValue = "0"
        shouldResetDisplay = false
        if (operator == null && previousValue.isBlank()) {
            expression = " "
            activeExpressionChain = ""
        }
    }

    fun smartClear() {
        val hasVisibleEntry = currentValue != "0" || currentValue == "Error"
        if (hasVisibleEntry) {
            clearCurrentEntry()
        } else {
            clearAll()
        }
    }

    fun toggleSign() {
        if (currentValue != "0" && currentValue != "Error") {
            currentValue = if (currentValue.startsWith("-")) {
                currentValue.drop(1)
            } else {
                "-$currentValue"
            }
        }
    }

    fun operatorSymbol(op: String?): String {
        return when (op) {
            "*" -> "x"
            "/" -> "/"
            "-" -> "-"
            "mod" -> "%"
            else -> op.orEmpty()
        }
    }

    fun calculate() {
        if (operator == null || shouldResetDisplay) return

        val prev = previousValue.toDoubleOrNull() ?: return
        val current = currentValue.toDoubleOrNull() ?: return

        val result = when (operator) {
            "+" -> (prev + current).toString()
            "-" -> (prev - current).toString()
            "*" -> (prev * current).toString()
            "/" -> if (current == 0.0) "Error" else (prev / current).toString()
            "mod" -> ((prev / 100.0) * current).toString()
            else -> return
        }

        val opSymbol = operatorSymbol(operator)

        val formattedPrev = formatNumber(previousValue)
        val formattedCurrent = formatNumber(currentValue)
        val formattedResult = formatNumber(result)
        val chainPrefix = if (activeExpressionChain.isBlank()) {
            "$formattedPrev $opSymbol $formattedCurrent"
        } else {
            "$activeExpressionChain $opSymbol $formattedCurrent"
        }
        val completedChain = "$chainPrefix = $formattedResult"

        expression = "$chainPrefix ="
        currentValue = result
        operator = null
        shouldResetDisplay = true
        activeExpressionChain = completedChain
        activeHistoryEntry = completedChain
        persistActiveHistory()
    }

    fun setOperator(op: String) {
        if (currentValue == "Error") return

        if (operator != null && !shouldResetDisplay) {
            calculate()
        }

        previousValue = currentValue
        operator = op
        shouldResetDisplay = true

        val opSymbol = operatorSymbol(op)

        val formattedPrevious = formatNumber(previousValue)
        expression = if (activeExpressionChain.isBlank()) {
            "$formattedPrevious $opSymbol"
        } else {
            "$activeExpressionChain $opSymbol"
        }
    }

    fun reuseHistoryEntry(entry: CalculatorHistoryEntry) {
        val resultValue = entry.expression.substringAfterLast("=").trim()
        currentValue = resultValue.replace(",", "")
        previousValue = ""
        operator = null
        shouldResetDisplay = true
        expression = entry.expression
        activeExpressionChain = resultValue
        activeHistoryEntry = ""
        activeHistoryTimestamp = null
        currentScreen = CalculatorScreenMode.CALCULATOR
    }

    fun openContactEmail() {
        val intent = Intent(Intent.ACTION_SENDTO).apply {
            data = Uri.parse("mailto:creators.engine@gmail.com")
            putExtra(Intent.EXTRA_EMAIL, arrayOf("creators.engine@gmail.com"))
            putExtra(Intent.EXTRA_SUBJECT, "SimpliCalc support")
        }
        context.startActivity(Intent.createChooser(intent, "Contact SimpliCalc"))
    }

    if (currentScreen != CalculatorScreenMode.CALCULATOR) {
        BackHandler {
            currentScreen = CalculatorScreenMode.CALCULATOR
        }
    }

    if (currentScreen == CalculatorScreenMode.HISTORY) {
        HistoryScreen(
            historyEntries = historyEntries,
            onBack = withHaptics { currentScreen = CalculatorScreenMode.CALCULATOR },
            onClear = withHaptics { persistHistory(emptyList()) },
            onSelectEntry = { entry ->
                hapticFeedback.performHapticFeedback(HapticFeedbackType.TextHandleMove)
                reuseHistoryEntry(entry)
            },
            calculatorFont = calculatorTypography.uiFont
        )
    } else if (currentScreen == CalculatorScreenMode.PRIVACY) {
        InfoScreen(
            title = "Privacy Policy",
            sections = privacyPolicySections(),
            onBack = withHaptics { currentScreen = CalculatorScreenMode.CALCULATOR },
            calculatorFont = calculatorTypography.uiFont
        )
    } else if (currentScreen == CalculatorScreenMode.ABOUT) {
        InfoScreen(
            title = "About",
            sections = aboutSections(),
            onBack = withHaptics { currentScreen = CalculatorScreenMode.CALCULATOR },
            calculatorFont = calculatorTypography.uiFont
        )
    } else if (currentScreen == CalculatorScreenMode.CONTACT) {
        ContactScreen(
            onBack = withHaptics { currentScreen = CalculatorScreenMode.CALCULATOR },
            onOpenMail = withHaptics(::openContactEmail),
            calculatorFont = calculatorTypography.uiFont
        )
    } else {
        CalculatorHomeScreen(
            currentValue = currentValue,
            expression = expression,
            activeOperator = operator,
            onOpenPrivacy = withHaptics { currentScreen = CalculatorScreenMode.PRIVACY },
            onOpenAbout = withHaptics { currentScreen = CalculatorScreenMode.ABOUT },
            onContact = withHaptics { currentScreen = CalculatorScreenMode.CONTACT },
            onOpenHistory = withHaptics { currentScreen = CalculatorScreenMode.HISTORY },
            onClearAll = withHaptics(::smartClear),
            onDeleteLastDigit = withHaptics(::deleteLastDigit),
            onDeleteAllDigits = withHaptics(::clearCurrentNumber),
            onToggleSign = withHaptics(::toggleSign),
            onOperator = { value ->
                hapticFeedback.performHapticFeedback(HapticFeedbackType.TextHandleMove)
                setOperator(value)
            },
            onAppendNumber = { value ->
                hapticFeedback.performHapticFeedback(HapticFeedbackType.TextHandleMove)
                appendNumber(value)
            },
            onAppendDecimal = withHaptics(::appendDecimal),
            onCalculate = withHaptics(::calculate),
            onCopyResult = withHaptics {
                coroutineScope.launch {
                    clipboard.setClipEntry(
                        ClipEntry(
                            ClipData.newPlainText("calculator_calculation", copyableCalculation())
                        )
                    )
                }
            },
            formatNumber = ::formatNumber,
            calculatorFont = calculatorTypography.uiFont,
            displayFont = calculatorTypography.displayFont
        )
    }
}

private enum class CalculatorScreenMode {
    CALCULATOR,
    HISTORY,
    PRIVACY,
    ABOUT,
    CONTACT
}

private fun historyGroupLabel(timestamp: Long): String {
    if (timestamp <= 0L) return "Older"

    val now = Calendar.getInstance()
    val entryDate = Calendar.getInstance().apply { timeInMillis = timestamp }
    val yesterday = Calendar.getInstance().apply { add(Calendar.DAY_OF_YEAR, -1) }

    return when {
        isSameDay(entryDate, now) -> "Today"
        isSameDay(entryDate, yesterday) -> "Yesterday"
        else -> SimpleDateFormat("MMM d", Locale.getDefault()).format(Date(timestamp))
    }
}

private fun historyTimeLabel(timestamp: Long): String {
    if (timestamp <= 0L) return "Saved earlier"
    return SimpleDateFormat("h:mm a", Locale.getDefault()).format(Date(timestamp))
}

private fun normalizeSearchText(value: String): String {
    return value.replace(",", "").lowercase(Locale.getDefault())
}

private fun privacyPolicySections(): List<InfoSection> = listOf(
    InfoSection(
        heading = "Overview",
        body = "SimpliCalc is designed as an offline calculator for everyday math. It works without account sign-in, cloud sync, or ads."
    ),
    InfoSection(
        heading = "Data collection",
        body = "SimpliCalc does not collect personal information, analytics data, location data, contacts, or payment information."
    ),
    InfoSection(
        heading = "On-device storage",
        body = "Your calculation log is stored locally on your device so previous calculations remain available after you close the app. You can remove this stored history at any time by using Clear all in the log screen."
    ),
    InfoSection(
        heading = "Clipboard use",
        body = "When you tap copy result, SimpliCalc places the visible result on your device clipboard. This happens only when you choose to use that action."
    ),
    InfoSection(
        heading = "Sharing and retention",
        body = "SimpliCalc does not share your data with third parties. Stored history remains on your device until you clear it manually or uninstall the app."
    )
)

private fun aboutSections(): List<InfoSection> = listOf(
    InfoSection(
        heading = "SimpliCalc",
        body = "SimpliCalc is a lightweight Android calculator focused on fast everyday use, clear visual feedback, and a persistent reusable log."
    ),
    InfoSection(
        heading = "Features",
        body = "The app supports chained calculations, local log storage, grouped and searchable log entries, copy result, haptic feedback, and tap-to-reuse from the log."
    ),
    InfoSection(
        heading = "Design",
        body = "The interface is built around a compact bottom keypad, large readable results, and dark high-contrast styling for quick use on mobile screens."
    ),
    InfoSection(
        heading = "Version",
        body = "Current version: 1.0"
    )
)

private fun isSameDay(first: Calendar, second: Calendar): Boolean {
    return first.get(Calendar.YEAR) == second.get(Calendar.YEAR) &&
        first.get(Calendar.DAY_OF_YEAR) == second.get(Calendar.DAY_OF_YEAR)
}

@Composable
private fun CalculatorHomeScreen(
    currentValue: String,
    expression: String,
    activeOperator: String?,
    onOpenPrivacy: () -> Unit,
    onOpenAbout: () -> Unit,
    onContact: () -> Unit,
    onOpenHistory: () -> Unit,
    onClearAll: () -> Unit,
    onDeleteLastDigit: () -> Unit,
    onDeleteAllDigits: () -> Unit,
    onToggleSign: () -> Unit,
    onOperator: (String) -> Unit,
    onAppendNumber: (String) -> Unit,
    onAppendDecimal: () -> Unit,
    onCalculate: () -> Unit,
    onCopyResult: () -> Unit,
    formatNumber: (String) -> String,
    calculatorFont: FontFamily,
    displayFont: FontFamily
) {
    val formattedResult = formatNumber(currentValue)
    BoxWithConstraints(
        modifier = Modifier
            .fillMaxSize()
            .background(AppBackgroundBrush)
            .statusBarsPadding()
            .navigationBarsPadding()
    ) {
        val compactSide = if (maxWidth < maxHeight) maxWidth else maxHeight
        val isVeryShortWindow = maxHeight < 620.dp
        val isNarrowWindow = maxWidth < 320.dp
        val outerPadding = (compactSide * 0.045f).coerceIn(12.dp, 24.dp)
        val displayTopPadding = if (isVeryShortWindow) {
            16.dp
        } else {
            (maxHeight * 0.1f).coerceIn(32.dp, 84.dp)
        }
        val displayBottomGap = if (isVeryShortWindow) {
            6.dp
        } else {
            (compactSide * 0.055f).coerceIn(10.dp, 24.dp)
        }
        val buttonGap = if (isVeryShortWindow || isNarrowWindow) {
            3.dp
        } else {
            (compactSide * 0.02f).coerceIn(4.dp, 10.dp)
        }
        val buttonHeight = if (isVeryShortWindow) {
            ((maxHeight - outerPadding * 2) * 0.085f).coerceIn(44.dp, 68.dp)
        } else {
            ((maxHeight - outerPadding * 2) * 0.1f).coerceIn(58.dp, 94.dp)
        }
        val isSmallScreen = compactSide < 360.dp
        val isLargeScreen = compactSide >= 420.dp
        val expressionSize = when {
            isSmallScreen -> 14.sp
            isLargeScreen -> 20.sp
            else -> 16.sp
        }
        val resultSize = when {
            isSmallScreen -> 58.sp
            isLargeScreen -> 92.sp
            else -> 74.sp
        }
        val buttonTextSize = when {
            isSmallScreen -> 18.sp
            isLargeScreen -> 28.sp
            else -> 22.sp
        }
        val adaptiveExpressionSize = when {
            expression.length > 32 -> expressionSize * 0.8f
            expression.length > 24 -> expressionSize * 0.9f
            else -> expressionSize
        }
        val adaptiveResultSize = when {
            formattedResult.length > 16 -> resultSize * 0.42f
            formattedResult.length > 14 -> resultSize * 0.5f
            formattedResult.length > 12 -> resultSize * 0.58f
            formattedResult.length > 10 -> resultSize * 0.7f
            formattedResult.length > 8 -> resultSize * 0.82f
            else -> resultSize
        }
        val buttonCorner = (buttonHeight * 0.26f).coerceIn(16.dp, 28.dp)
        var menuExpanded by remember { mutableStateOf(false) }
        val hasCalculatedValue = expression.trim().isNotEmpty() && currentValue != "Error"
        val contentScrollState = rememberScrollState()

        Column(
            modifier = Modifier
                .fillMaxSize()
                .then(
                    if (isVeryShortWindow) {
                        Modifier.verticalScroll(contentScrollState)
                    } else {
                        Modifier
                    }
                )
                .padding(horizontal = outerPadding, vertical = outerPadding.coerceAtLeast(10.dp))
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box {
                    UtilityTextButton(
                        label = "\u22EE",
                        onClick = { menuExpanded = true },
                        textSize = if (isSmallScreen) 23.sp else 27.sp,
                        height = if (isSmallScreen) 28.dp else 32.dp,
                        width = if (isSmallScreen) 34.dp else 40.dp,
                        calculatorFont = calculatorFont,
                        contentColor = WarmText.copy(alpha = 0.88f),
                        blendWithBackground = true,
                        removeBackground = true
                    )
                    DropdownMenu(
                        expanded = menuExpanded,
                        onDismissRequest = { menuExpanded = false },
                        containerColor = SurfaceColor
                    ) {
                        DropdownMenuItem(
                            text = { Text("Privacy policy", color = WarmText, fontFamily = calculatorFont) },
                            onClick = {
                                menuExpanded = false
                                onOpenPrivacy()
                            }
                        )
                        DropdownMenuItem(
                            text = { Text("About", color = WarmText, fontFamily = calculatorFont) },
                            onClick = {
                                menuExpanded = false
                                onOpenAbout()
                            }
                        )
                        DropdownMenuItem(
                            text = { Text("Contact", color = WarmText, fontFamily = calculatorFont) },
                            onClick = {
                                menuExpanded = false
                                onContact()
                            }
                        )
                    }
                }

                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    UtilityIconButton(
                        onClick = onOpenHistory,
                        height = if (isSmallScreen) 28.dp else 32.dp,
                        width = if (isSmallScreen) 34.dp else 40.dp,
                        contentColor = SoftText.copy(alpha = 0.82f),
                        imageVector = Icons.Outlined.History,
                        contentDescription = "History"
                    )
                    UtilityIconButton(
                        onClick = onCopyResult,
                        height = if (isSmallScreen) 28.dp else 32.dp,
                        width = if (isSmallScreen) 34.dp else 40.dp,
                        contentColor = if (hasCalculatedValue) AccentBorder else SoftText.copy(alpha = 0.82f),
                        imageVector = Icons.Outlined.ContentCopy,
                        contentDescription = "Copy"
                    )
                }
            }

            Spacer(modifier = Modifier.height(displayTopPadding))

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = buttonGap),
                horizontalAlignment = Alignment.End
            ) {
                if (expression.trim().isNotEmpty()) {
                    Text(
                        text = expression,
                        color = SoftText.copy(alpha = 0.52f),
                        fontSize = adaptiveExpressionSize,
                        maxLines = 3,
                        fontFamily = displayFont,
                        textAlign = TextAlign.End,
                        modifier = Modifier.fillMaxWidth()
                    )
                }

                Text(
                    text = formattedResult,
                    color = WarmText,
                    fontSize = adaptiveResultSize,
                    fontWeight = FontWeight.Light,
                    lineHeight = adaptiveResultSize * 1.08f,
                    maxLines = 2,
                    fontFamily = displayFont,
                    textAlign = TextAlign.End,
                    overflow = TextOverflow.Clip,
                    modifier = Modifier.fillMaxWidth()
                )
            }

            if (!isVeryShortWindow) {
                Spacer(modifier = Modifier.weight(1f))
            } else {
                Spacer(modifier = Modifier.height(10.dp))
            }

            Spacer(modifier = Modifier.height(displayBottomGap))

            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.Bottom
            ) {
                CalculatorRow(
                    buttons = listOf(
                        CalcButtonData("C", ButtonType.SPECIAL) { onClearAll() },
                        CalcButtonData("%", ButtonType.UTILITY) { onOperator("mod") },
                        CalcButtonData("\u232B", ButtonType.UTILITY, onLongClick = onDeleteAllDigits) { onDeleteLastDigit() },
                        CalcButtonData("\u00F7", ButtonType.OPERATOR, isActive = activeOperator == "/") {
                            onOperator("/")
                        }
                    ),
                    buttonHeight = buttonHeight,
                    buttonGap = buttonGap,
                    buttonTextSize = buttonTextSize,
                    buttonCorner = buttonCorner,
                    calculatorFont = calculatorFont
                )

                CalculatorRow(
                    buttons = listOf(
                        CalcButtonData("7", ButtonType.NUMBER) { onAppendNumber("7") },
                        CalcButtonData("8", ButtonType.NUMBER) { onAppendNumber("8") },
                        CalcButtonData("9", ButtonType.NUMBER) { onAppendNumber("9") },
                        CalcButtonData("\u00D7", ButtonType.OPERATOR, isActive = activeOperator == "*") {
                            onOperator("*")
                        }
                    ),
                    buttonHeight = buttonHeight,
                    buttonGap = buttonGap,
                    buttonTextSize = buttonTextSize,
                    buttonCorner = buttonCorner,
                    calculatorFont = calculatorFont
                )

                CalculatorRow(
                    buttons = listOf(
                        CalcButtonData("4", ButtonType.NUMBER) { onAppendNumber("4") },
                        CalcButtonData("5", ButtonType.NUMBER) { onAppendNumber("5") },
                        CalcButtonData("6", ButtonType.NUMBER) { onAppendNumber("6") },
                        CalcButtonData("-", ButtonType.OPERATOR, isActive = activeOperator == "-") {
                            onOperator("-")
                        }
                    ),
                    buttonHeight = buttonHeight,
                    buttonGap = buttonGap,
                    buttonTextSize = buttonTextSize,
                    buttonCorner = buttonCorner,
                    calculatorFont = calculatorFont
                )

                CalculatorRow(
                    buttons = listOf(
                        CalcButtonData("1", ButtonType.NUMBER) { onAppendNumber("1") },
                        CalcButtonData("2", ButtonType.NUMBER) { onAppendNumber("2") },
                        CalcButtonData("3", ButtonType.NUMBER) { onAppendNumber("3") },
                        CalcButtonData("+", ButtonType.OPERATOR, isActive = activeOperator == "+") {
                            onOperator("+")
                        }
                    ),
                    buttonHeight = buttonHeight,
                    buttonGap = buttonGap,
                    buttonTextSize = buttonTextSize,
                    buttonCorner = buttonCorner,
                    calculatorFont = calculatorFont
                )

                Row(modifier = Modifier.fillMaxWidth()) {
                    CalculatorButton(
                        text = ".",
                        type = ButtonType.NUMBER,
                        modifier = Modifier
                            .weight(1f)
                            .padding(buttonGap),
                        onClick = onAppendDecimal,
                        buttonHeight = buttonHeight,
                        textSize = buttonTextSize,
                        cornerRadius = buttonCorner,
                        fontFamily = calculatorFont
                    )

                    CalculatorButton(
                        text = "0",
                        type = ButtonType.NUMBER,
                        modifier = Modifier
                            .weight(1f)
                            .padding(buttonGap),
                        onClick = { onAppendNumber("0") },
                        buttonHeight = buttonHeight,
                        textSize = buttonTextSize,
                        cornerRadius = buttonCorner,
                        fontFamily = calculatorFont
                    )

                    CalculatorButton(
                        text = "00",
                        type = ButtonType.NUMBER,
                        modifier = Modifier
                            .weight(1f)
                            .padding(buttonGap),
                        onClick = { onAppendNumber("00") },
                        buttonHeight = buttonHeight,
                        textSize = buttonTextSize,
                        cornerRadius = buttonCorner,
                        fontFamily = calculatorFont
                    )

                    CalculatorButton(
                        text = "=",
                        type = ButtonType.OPERATOR,
                        modifier = Modifier
                            .weight(1f)
                            .padding(buttonGap),
                        onClick = onCalculate,
                        buttonHeight = buttonHeight,
                        textSize = buttonTextSize,
                        cornerRadius = buttonCorner,
                        fontFamily = calculatorFont
                    )
                }
            }
        }
    }
}

@Composable
private fun HistoryScreen(
    historyEntries: List<CalculatorHistoryEntry>,
    onBack: () -> Unit,
    onClear: () -> Unit,
    onSelectEntry: (CalculatorHistoryEntry) -> Unit,
    calculatorFont: FontFamily
) {
    BoxWithConstraints(
        modifier = Modifier
            .fillMaxSize()
            .background(AppBackgroundBrush)
            .statusBarsPadding()
            .navigationBarsPadding()
    ) {
        val compactSide = if (maxWidth < maxHeight) maxWidth else maxHeight
        val outerPadding = (compactSide * 0.05f).coerceIn(16.dp, 28.dp)
        val titleSize = if (compactSide < 360.dp) 24.sp else 28.sp
        val itemSize = if (compactSide < 360.dp) 18.sp else 20.sp
        val metaSize = if (compactSide < 360.dp) 14.sp else 16.sp
        val actionHeight = if (compactSide < 360.dp) 28.dp else 31.dp
        val actionWidth = if (compactSide < 360.dp) 110.dp else 124.dp
        var searchQuery by remember { mutableStateOf("") }
        val filteredEntries = remember(historyEntries, searchQuery) {
            val query = searchQuery.trim()
            if (query.isBlank()) {
                historyEntries
            } else {
                val normalizedQuery = normalizeSearchText(query)
                historyEntries.filter { entry ->
                    val rawMatch = entry.expression.contains(query, ignoreCase = true)
                    val normalizedMatch = normalizeSearchText(entry.expression).contains(normalizedQuery)
                    rawMatch || normalizedMatch
                }
            }
        }
        val groupedEntries = remember(filteredEntries) {
            filteredEntries.groupBy { historyGroupLabel(it.timestamp) }
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(outerPadding)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                UtilityTextButton(
                    label = "Back",
                    onClick = onBack,
                    textSize = metaSize,
                    height = actionHeight,
                    width = actionWidth,
                    calculatorFont = calculatorFont,
                    contentColor = WarmText
                )

                UtilityTextButton(
                    label = "Clear all",
                    onClick = onClear,
                    textSize = metaSize,
                    height = actionHeight,
                    width = actionWidth,
                    calculatorFont = calculatorFont,
                    contentColor = DangerText
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            Text(
                text = "Log",
                color = WarmText,
                fontSize = titleSize,
                fontWeight = FontWeight.Bold,
                fontFamily = calculatorFont
            )

            Spacer(modifier = Modifier.height(20.dp))

            TextField(
                value = searchQuery,
                onValueChange = { searchQuery = it },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                placeholder = {
                    Text(
                        text = "Search history",
                        color = SoftText.copy(alpha = 0.42f),
                        fontSize = metaSize,
                        fontFamily = calculatorFont
                    )
                },
                textStyle = androidx.compose.ui.text.TextStyle(
                    color = Color.White,
                    fontSize = metaSize,
                    fontFamily = calculatorFont
                ),
                shape = RoundedCornerShape(18.dp),
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = SurfaceColor,
                    unfocusedContainerColor = SurfaceColor,
                    disabledContainerColor = SurfaceColor,
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent,
                    disabledIndicatorColor = Color.Transparent,
                    cursorColor = AccentBorder
                )
            )

            Spacer(modifier = Modifier.height(18.dp))

            if (filteredEntries.isEmpty()) {
                Text(
                    text = if (historyEntries.isEmpty()) "No calculations yet" else "No matching calculations",
                    color = SoftText.copy(alpha = 0.72f),
                    fontSize = itemSize,
                    fontFamily = calculatorFont
                )
            } else {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    groupedEntries.forEach { (groupLabel, entriesInGroup) ->
                        item {
                            Text(
                                text = groupLabel,
                                color = AccentBorder,
                                fontSize = metaSize,
                                fontWeight = FontWeight.SemiBold,
                                fontFamily = calculatorFont
                            )
                        }

                        items(entriesInGroup) { entry ->
                            val interactionSource = remember { MutableInteractionSource() }
                            val isPressed by interactionSource.collectIsPressedAsState()
                            val scale by animateFloatAsState(
                                targetValue = if (isPressed) 0.98f else 1f,
                                animationSpec = spring(stiffness = 720f, dampingRatio = 0.74f),
                                label = "history_item_scale"
                            )

                            Button(
                                onClick = { onSelectEntry(entry) },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .graphicsLayer {
                                        scaleX = scale
                                        scaleY = scale
                                    }
                                    .background(
                                        brush = Brush.verticalGradient(
                                            listOf(Color(0xFF303238), Color(0xFF1A1C21))
                                        ),
                                        shape = RoundedCornerShape(18.dp)
                                    ),
                                shape = RoundedCornerShape(18.dp),
                                border = BorderStroke(1.dp, SubtleBorder),
                                contentPadding = PaddingValues(18.dp, 16.dp),
                                interactionSource = interactionSource,
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = Color.Transparent,
                                    contentColor = Color.White
                                )
                            ) {
                                Column(modifier = Modifier.fillMaxWidth()) {
                                    Text(
                                        text = entry.expression,
                                    color = WarmText,
                                        fontSize = itemSize,
                                        fontWeight = FontWeight.Medium,
                                        fontFamily = calculatorFont
                                    )
                                    Spacer(modifier = Modifier.height(8.dp))
                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.SpaceBetween,
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Text(
                                            text = "tap to recall",
                                            color = AccentBorder,
                                            fontSize = metaSize,
                                            fontFamily = calculatorFont
                                        )
                                        Text(
                                            text = historyTimeLabel(entry.timestamp),
                                            color = SoftText.copy(alpha = 0.52f),
                                            fontSize = metaSize,
                                            fontFamily = calculatorFont
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

private data class InfoSection(
    val heading: String,
    val body: String
)

private data class CalculatorTypography(
    val displayFont: FontFamily,
    val uiFont: FontFamily
)

@Composable
private fun InfoScreen(
    title: String,
    sections: List<InfoSection>,
    onBack: () -> Unit,
    calculatorFont: FontFamily,
    footer: @Composable (() -> Unit)? = null
) {
    BoxWithConstraints(
        modifier = Modifier
            .fillMaxSize()
            .background(AppBackgroundBrush)
            .statusBarsPadding()
            .navigationBarsPadding()
    ) {
        val compactSide = if (maxWidth < maxHeight) maxWidth else maxHeight
        val outerPadding = (compactSide * 0.05f).coerceIn(16.dp, 28.dp)
        val titleSize = if (compactSide < 360.dp) 24.sp else 28.sp
        val headingSize = if (compactSide < 360.dp) 16.sp else 18.sp
        val bodySize = if (compactSide < 360.dp) 14.sp else 16.sp
        val actionHeight = if (compactSide < 360.dp) 28.dp else 31.dp
        val actionWidth = if (compactSide < 360.dp) 110.dp else 124.dp

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(outerPadding)
        ) {
            UtilityTextButton(
                label = "Back",
                onClick = onBack,
                textSize = bodySize,
                height = actionHeight,
                width = actionWidth,
                calculatorFont = calculatorFont,
                contentColor = WarmText
            )

            Spacer(modifier = Modifier.height(24.dp))

            Text(
                text = title,
                color = WarmText,
                fontSize = titleSize,
                fontWeight = FontWeight.Bold,
                fontFamily = calculatorFont
            )

            Spacer(modifier = Modifier.height(20.dp))

            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(sections) { section ->
                    Surface(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(18.dp),
                        color = Color.Transparent,
                        border = BorderStroke(1.dp, SubtleBorder)
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .background(
                                    brush = PanelBrush,
                                    shape = RoundedCornerShape(18.dp)
                                )
                                .padding(18.dp)
                        ) {
                            Text(
                                text = section.heading,
                                color = AccentBorder,
                                fontSize = headingSize,
                                fontWeight = FontWeight.SemiBold,
                                fontFamily = calculatorFont
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                text = section.body,
                                color = SoftText.copy(alpha = 0.9f),
                                fontSize = bodySize,
                                fontFamily = calculatorFont
                            )
                        }
                    }
                }

                if (footer != null) {
                    item {
                        Spacer(modifier = Modifier.height(4.dp))
                        footer()
                    }
                }
            }
        }
    }
}

@Composable
private fun ContactScreen(
    onBack: () -> Unit,
    onOpenMail: () -> Unit,
    calculatorFont: FontFamily
) {
    InfoScreen(
        title = "Contact",
        sections = listOf(
            InfoSection(
                heading = "Support",
                body = "For support, feedback, bug reports, or publishing questions, use the email action below."
            ),
            InfoSection(
                heading = "Email",
                body = "creators.engine@gmail.com"
            )
        ),
        onBack = onBack,
        calculatorFont = calculatorFont,
        footer = {
            UtilityTextButton(
                label = "open mail",
                onClick = onOpenMail,
                textSize = 16.sp,
                height = 34.dp,
                width = 120.dp,
                calculatorFont = calculatorFont,
                contentColor = AccentBorder
            )
        }
    )
}

@Composable
private fun UtilityActionButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    height: Dp,
    width: Dp,
    textSize: TextUnit,
    symbol: String = "\u21BA",
    calculatorFont: FontFamily = FontFamily.Serif
) {
    Button(
        onClick = onClick,
        modifier = modifier
            .height(height)
            .width(width)
            .background(
                brush = PanelBrush,
                shape = RoundedCornerShape(height * 0.42f)
            ),
        shape = RoundedCornerShape(height * 0.42f),
        contentPadding = PaddingValues(0.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = Color.Transparent,
            contentColor = AccentBorder
        )
    ) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = symbol,
                fontSize = textSize,
                fontWeight = FontWeight.Medium,
                fontFamily = calculatorFont
            )
        }
    }
}

@Composable
private fun UtilityTextButton(
    label: String,
    onClick: () -> Unit,
    textSize: TextUnit,
    height: Dp,
    width: Dp,
    calculatorFont: FontFamily,
    contentColor: Color = AccentBorder,
    blendWithBackground: Boolean = false,
    removeBackground: Boolean = false
) {
    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()
    val scale by animateFloatAsState(
        targetValue = if (isPressed) 0.95f else 1f,
        animationSpec = spring(stiffness = 700f, dampingRatio = 0.72f),
        label = "utility_button_scale"
    )

    Button(
        onClick = onClick,
        modifier = Modifier
            .height(height)
            .width(width)
            .graphicsLayer {
                scaleX = scale
                scaleY = scale
            }
            .then(
                if (removeBackground) {
                    Modifier
                } else {
                    Modifier.background(
                        brush = if (blendWithBackground) {
                            Brush.verticalGradient(
                                listOf(Color(0xFF101011), Color(0xFF060607))
                            )
                        } else {
                            PanelBrush
                        },
                        shape = RoundedCornerShape(height * 0.42f)
                    )
                }
            ),
        shape = RoundedCornerShape(height * 0.42f),
        contentPadding = PaddingValues(0.dp),
        interactionSource = interactionSource,
        colors = ButtonDefaults.buttonColors(
            containerColor = Color.Transparent,
            contentColor = contentColor
        )
    ) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = label,
                fontSize = textSize,
                fontWeight = FontWeight.Medium,
                fontFamily = calculatorFont
            )
        }
    }
}

@Composable
private fun UtilityIconButton(
    onClick: () -> Unit,
    height: Dp,
    width: Dp,
    contentColor: Color,
    imageVector: ImageVector,
    contentDescription: String
) {
    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()
    val scale by animateFloatAsState(
        targetValue = if (isPressed) 0.95f else 1f,
        animationSpec = spring(stiffness = 700f, dampingRatio = 0.72f),
        label = "utility_icon_button_scale"
    )

    Button(
        onClick = onClick,
        modifier = Modifier
            .height(height)
            .width(width)
            .graphicsLayer {
                scaleX = scale
                scaleY = scale
            },
        shape = RoundedCornerShape(height * 0.42f),
        contentPadding = PaddingValues(0.dp),
        interactionSource = interactionSource,
        colors = ButtonDefaults.buttonColors(
            containerColor = Color.Transparent,
            contentColor = contentColor
        )
    ) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = imageVector,
                contentDescription = contentDescription,
                tint = contentColor
            )
        }
    }
}

data class CalcButtonData(
    val text: String,
    val type: ButtonType,
    val isActive: Boolean = false,
    val onLongClick: (() -> Unit)? = null,
    val onClick: () -> Unit
)

@Composable
private fun rememberCalculatorTypography(): CalculatorTypography {
    // These are lighter built-in fallbacks approximating:
    // display/result -> Cormorant Garamond
    // buttons/menu/log UI -> Lora
    // Replace with bundled font resources later for the exact look.
    return CalculatorTypography(
        displayFont = FontFamily.Serif,
        uiFont = FontFamily.SansSerif
    )
}

enum class ButtonType {
    NUMBER,
    OPERATOR,
    UTILITY,
    SPECIAL,
    EQUALS
}

@Composable
fun CalculatorRow(
    buttons: List<CalcButtonData>,
    buttonHeight: Dp,
    buttonGap: Dp,
    buttonTextSize: TextUnit,
    buttonCorner: Dp,
    calculatorFont: FontFamily
) {
    Row(modifier = Modifier.fillMaxWidth()) {
        buttons.forEach { button ->
            val resolvedTextSize = if (button.type == ButtonType.UTILITY && (button.text == "DEL" || button.text == "log")) {
                (buttonTextSize.value * 0.62f).sp
            } else {
                buttonTextSize
            }
            CalculatorButton(
                text = button.text,
                type = button.type,
                modifier = Modifier
                    .weight(1f)
                    .padding(buttonGap),
                onClick = button.onClick,
                buttonHeight = buttonHeight,
                textSize = resolvedTextSize,
                cornerRadius = buttonCorner,
                isActive = button.isActive,
                onLongClick = button.onLongClick,
                fontFamily = calculatorFont
            )
        }
    }
}

@Composable
fun CalculatorButton(
    text: String,
    type: ButtonType,
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
    buttonHeight: Dp,
    textSize: TextUnit,
    cornerRadius: Dp,
    isActive: Boolean = false,
    onLongClick: (() -> Unit)? = null,
    fontFamily: FontFamily = FontFamily.Serif
) {
    val backgroundBrush = when (type) {
        ButtonType.NUMBER -> NumberBrush
        ButtonType.OPERATOR -> if (isActive) AccentActiveBrush else AccentBrush
        ButtonType.UTILITY -> UtilityBrush
        ButtonType.SPECIAL -> SpecialBrush
        ButtonType.EQUALS -> AccentBrush
    }

    val contentColor = when (type) {
        ButtonType.SPECIAL -> DangerText
        ButtonType.OPERATOR, ButtonType.EQUALS -> Color.White
        else -> WarmText
    }

    val borderColor = when (type) {
        ButtonType.NUMBER -> SubtleBorder
        ButtonType.OPERATOR -> if (isActive) AccentBorderActive else AccentBorder
        ButtonType.UTILITY -> SubtleBorder
        ButtonType.SPECIAL -> DangerBorder
        ButtonType.EQUALS -> AccentBorder
    }

    val shape = RoundedCornerShape(cornerRadius)
    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()
    val scale by animateFloatAsState(
        targetValue = if (isPressed) 0.94f else 1f,
        animationSpec = spring(stiffness = 720f, dampingRatio = 0.72f),
        label = "calculator_button_scale"
    )

    Surface(
        modifier = modifier
            .height(buttonHeight)
            .graphicsLayer {
                scaleX = scale
                scaleY = scale
            }
            .combinedClickable(
                interactionSource = interactionSource,
                indication = null,
                onClick = onClick,
                onLongClick = onLongClick
            ),
        shape = shape,
        color = Color.Transparent,
        border = BorderStroke(1.dp, borderColor)
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    brush = backgroundBrush,
                    shape = shape
                ),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = text,
                color = contentColor,
                fontSize = textSize,
                fontWeight = FontWeight.Medium,
                fontFamily = fontFamily,
                maxLines = 1
            )
        }
    }
}
