package com.creatorsenginelabs.simplicalc

import android.app.DatePickerDialog
import android.content.ClipData
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.net.Uri
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.core.content.edit
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MenuDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.ArrowBack
import androidx.compose.material.icons.automirrored.outlined.KeyboardArrowRight
import androidx.compose.material.icons.outlined.KeyboardArrowDown
import androidx.compose.material.icons.outlined.Bolt
import androidx.compose.material.icons.outlined.Cake
import androidx.compose.material.icons.outlined.ContentCopy
import androidx.compose.material.icons.outlined.CurrencyRupee
import androidx.compose.material.icons.outlined.LocalOffer
import androidx.compose.material.icons.outlined.Favorite
import androidx.compose.material.icons.outlined.DataObject
import androidx.compose.material.icons.outlined.DeleteOutline
import androidx.compose.material.icons.outlined.DeviceThermostat
import androidx.compose.material.icons.outlined.GridView
import androidx.compose.material.icons.outlined.History
import androidx.compose.material.icons.outlined.Inventory2
import androidx.compose.material.icons.outlined.LocalGasStation
import androidx.compose.material.icons.outlined.MoreVert
import androidx.compose.material.icons.outlined.Scale
import androidx.compose.material.icons.outlined.Science
import androidx.compose.material.icons.outlined.Speed
import androidx.compose.material.icons.outlined.SquareFoot
import androidx.compose.material.icons.outlined.Straighten
import androidx.compose.material.icons.outlined.Timer
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
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
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import java.math.BigDecimal
import java.math.RoundingMode
import java.net.URL
import java.text.DecimalFormat
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale
import kotlinx.coroutines.launch
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.json.JSONArray
import kotlin.math.abs
import kotlin.math.E
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.ln
import kotlin.math.log10
import kotlin.math.pow
import kotlin.math.sin
import kotlin.math.sqrt
import kotlin.math.tan

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
                    val parts = encodedEntry.split(FIELD_SEPARATOR)
                    if (parts.size >= 5) {
                        CalculatorHistoryEntry(
                            expression = Uri.decode(parts[1]),
                            timestamp = parts[0].toLongOrNull() ?: 0L,
                            category = Uri.decode(parts[2]),
                            note = Uri.decode(parts[3]),
                            payload = Uri.decode(parts[4])
                        )
                    } else if (parts.size == 2) {
                        CalculatorHistoryEntry(
                            expression = Uri.decode(parts[1]),
                            timestamp = parts[0].toLongOrNull() ?: 0L
                        )
                    } else {
                        CalculatorHistoryEntry(
                            expression = Uri.decode(encodedEntry),
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
                        listOf(
                            entry.timestamp.toString(),
                            Uri.encode(entry.expression),
                            Uri.encode(entry.category),
                            Uri.encode(entry.note),
                            Uri.encode(entry.payload)
                        ).joinToString(FIELD_SEPARATOR)
                    }
                )
            }
    }
}

private val DefaultCurrencyRatesPerEuro = mapOf(
    "EUR" to 1.0,
    "INR" to 106.6290,
    "USD" to 1.1581,
    "GBP" to 0.86363,
    "JPY" to 183.63,
    "AUD" to 1.6195,
    "CAD" to 1.5742,
    "CHF" to 0.9031,
    "SGD" to 1.4757,
    "CNY" to 7.9518
)

private data class CurrencyRatesSnapshot(
    val ratesPerEuro: Map<String, Double>,
    val fetchedDate: String?
)

private object CurrencyRatesStore {
    private const val PREF_NAME = "currency_rates_store"
    private const val KEY_RATES = "rates"
    private const val KEY_FETCHED_DATE = "fetched_date"
    private const val KEY_LAST_SYNC_DAY = "last_sync_day"

    fun load(context: Context): CurrencyRatesSnapshot {
        val prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        val rawRates = prefs.getString(KEY_RATES, null)
        val fetchedDate = prefs.getString(KEY_FETCHED_DATE, null)
        if (rawRates.isNullOrBlank()) {
            return CurrencyRatesSnapshot(DefaultCurrencyRatesPerEuro, fetchedDate)
        }

        val parsedRates = rawRates
            .split("|")
            .mapNotNull { item ->
                val parts = item.split("=", limit = 2)
                val code = parts.getOrNull(0).orEmpty()
                val rate = parts.getOrNull(1)?.toDoubleOrNull()
                if (code.isBlank() || rate == null) null else code to rate
            }
            .toMap()

        return CurrencyRatesSnapshot(
            ratesPerEuro = DefaultCurrencyRatesPerEuro + parsedRates,
            fetchedDate = fetchedDate
        )
    }

    fun save(context: Context, snapshot: CurrencyRatesSnapshot) {
        val prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        val serializedRates = snapshot.ratesPerEuro.entries
            .sortedBy { it.key }
            .joinToString("|") { "${it.key}=${it.value}" }
        prefs.edit {
            putString(KEY_RATES, serializedRates)
            putString(KEY_FETCHED_DATE, snapshot.fetchedDate)
            putString(KEY_LAST_SYNC_DAY, todayKey())
        }
    }

    fun shouldRefreshToday(context: Context): Boolean {
        val prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        return prefs.getString(KEY_LAST_SYNC_DAY, null) != todayKey()
    }

    private fun todayKey(): String = SimpleDateFormat("yyyy-MM-dd", Locale.US).format(Date())
}

private suspend fun fetchFrankfurterCurrencySnapshot(
    currentRates: Map<String, Double> = DefaultCurrencyRatesPerEuro
): CurrencyRatesSnapshot? = withContext(Dispatchers.IO) {
    runCatching {
        val quotes = currentRates.keys
            .filterNot { it == "EUR" }
            .sorted()
            .joinToString(",")
        val endpoint = "https://api.frankfurter.dev/v2/rates?base=EUR&quotes=$quotes"
        val response = URL(endpoint).readText()
        val items = JSONArray(response)
        val updatedRates = currentRates.toMutableMap()
        var fetchedDate: String? = null

        for (index in 0 until items.length()) {
            val item = items.getJSONObject(index)
            val quote = item.optString("quote")
            val rate = item.optDouble("rate", Double.NaN)
            if (quote.isNotBlank() && rate.isFinite()) {
                updatedRates[quote] = rate
            }
            if (fetchedDate == null) {
                fetchedDate = item.optString("date").takeIf { it.isNotBlank() }
            }
        }

        CurrencyRatesSnapshot(
            ratesPerEuro = updatedRates,
            fetchedDate = fetchedDate
        )
    }.onFailure {
        Log.w("SimpliCalc", "Frankfurter rates refresh failed", it)
    }.getOrNull()
}

private fun formatFetchedCurrencyDate(rawDate: String?): String {
    if (rawDate.isNullOrBlank()) return "Unavailable"
    return runCatching {
        val parsed = SimpleDateFormat("yyyy-MM-dd", Locale.US).parse(rawDate)
        SimpleDateFormat("dd-MMM-yyyy", Locale.US).format(parsed ?: return rawDate)
    }.getOrDefault(rawDate)
}

data class CalculatorHistoryEntry(
    val expression: String,
    val timestamp: Long,
    val category: String = "Calculator",
    val note: String = "",
    val payload: String = ""
)

private fun encodeHistoryPayload(vararg values: String): String =
    values.joinToString("|") { Uri.encode(it) }

private fun decodeHistoryPayload(payload: String): List<String> =
    if (payload.isBlank()) emptyList() else payload.split('|').map(Uri::decode)

@Composable
fun CalculatorScreen() {
    val context = LocalContext.current
    val clipboard = LocalClipboard.current
    val hapticFeedback = LocalHapticFeedback.current
    val coroutineScope = rememberCoroutineScope()
    val calculatorTypography = rememberCalculatorTypography()
    var currencySnapshot by remember { mutableStateOf(CurrencyRatesStore.load(context)) }
    val converterCategories = remember(currencySnapshot.ratesPerEuro) {
        unitConversionCategories(currencySnapshot.ratesPerEuro)
    }
    var currentValue by remember { mutableStateOf("0") }
    var previousValue by remember { mutableStateOf("") }
    var operator by remember { mutableStateOf<String?>(null) }
    var shouldResetDisplay by remember { mutableStateOf(false) }
    var expression by remember { mutableStateOf(" ") }
    var currentScreen by remember { mutableStateOf(CalculatorScreenMode.CALCULATOR) }
    var selectedConverterCategoryKey by remember { mutableStateOf<String?>(null) }
    var historyEntries by remember { mutableStateOf(CalculatorHistoryStore.load(context)) }
    var activeHistoryEntry by remember { mutableStateOf("") }
    var activeHistoryTimestamp by remember { mutableStateOf<Long?>(null) }
    var activeExpressionChain by remember { mutableStateOf("") }
    var recalledConverterPayload by remember { mutableStateOf<String?>(null) }
    val scientificFormatter = remember { DecimalFormat("0.##E0") }

    LaunchedEffect(Unit) {
        if (CurrencyRatesStore.shouldRefreshToday(context)) {
            fetchFrankfurterCurrencySnapshot(currencySnapshot.ratesPerEuro)?.let { snapshot ->
                currencySnapshot = snapshot
                CurrencyRatesStore.save(context, snapshot)
            }
        }
    }

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

    fun persistHistoryEntry(
        expressionText: String,
        category: String,
        note: String = "",
        payload: String = ""
    ) {
        val cleanedExpression = expressionText.trim()
        if (cleanedExpression.isEmpty()) return

        val existingTimestamp = activeHistoryTimestamp
        if (existingTimestamp == null) {
            val newTimestamp = System.currentTimeMillis()
            activeHistoryTimestamp = newTimestamp
            persistHistory(
                listOf(
                    CalculatorHistoryEntry(
                        expression = cleanedExpression,
                        timestamp = newTimestamp,
                        category = category,
                        note = note,
                        payload = payload
                    )
                ) + historyEntries
            )
        } else {
            persistHistory(
                historyEntries.map { savedEntry ->
                    if (savedEntry.timestamp == existingTimestamp) {
                        savedEntry.copy(
                            expression = cleanedExpression,
                            category = category,
                            note = note,
                            payload = payload
                        )
                    } else {
                        savedEntry
                    }
                }
            )
        }
    }

    fun withHaptics(action: () -> Unit): () -> Unit = {
        hapticFeedback.performHapticFeedback(HapticFeedbackType.TextHandleMove)
        action()
    }

    fun persistActiveHistory() {
        val entry = activeHistoryEntry.trim()
        if (entry.isNotEmpty()) {
            persistHistoryEntry(entry, category = "Calculator")
        }
    }

    fun resetHistorySession() {
        activeHistoryEntry = ""
        activeHistoryTimestamp = null
        activeExpressionChain = ""
        expression = " "
        recalledConverterPayload = null
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
        if (entry.payload.isNotBlank()) {
            val payloadParts = decodeHistoryPayload(entry.payload)
            val categoryKey = payloadParts.firstOrNull().orEmpty()
            if (categoryKey.isNotBlank()) {
                recalledConverterPayload = entry.payload
                activeHistoryTimestamp = entry.timestamp
                selectedConverterCategoryKey = categoryKey
                currentScreen = CalculatorScreenMode.UNIT_CONVERTER
                return
            }
        }

        val resultValue = entry.expression.substringAfterLast("=").trim()
        currentValue = resultValue.replace(",", "")
        previousValue = ""
        operator = null
        shouldResetDisplay = true
        expression = entry.expression
        activeExpressionChain = resultValue
        activeHistoryEntry = ""
        activeHistoryTimestamp = entry.timestamp
        recalledConverterPayload = null
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
            if (currentScreen == CalculatorScreenMode.UNIT_CONVERTER && selectedConverterCategoryKey != null) {
                selectedConverterCategoryKey = null
            } else {
                currentScreen = CalculatorScreenMode.CALCULATOR
            }
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
            onDeleteEntry = { entry ->
                hapticFeedback.performHapticFeedback(HapticFeedbackType.TextHandleMove)
                persistHistory(historyEntries.filterNot { it.timestamp == entry.timestamp })
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
    } else if (currentScreen == CalculatorScreenMode.UNIT_CONVERTER) {
        val selectedCategory = converterCategories.firstOrNull { it.key == selectedConverterCategoryKey }
        if (selectedCategory == null) {
            UnitConverterScreen(
                categories = converterCategories,
                onBack = withHaptics {
                    recalledConverterPayload = null
                    activeHistoryTimestamp = null
                    currentScreen = CalculatorScreenMode.CALCULATOR
                },
                onSelectCategory = { key ->
                    hapticFeedback.performHapticFeedback(HapticFeedbackType.TextHandleMove)
                    recalledConverterPayload = null
                    activeHistoryTimestamp = null
                    selectedConverterCategoryKey = key
                },
                calculatorFont = calculatorTypography.uiFont
            )
        } else {
            if (selectedCategory.key == "mileage") {
                val recalledParts = decodeHistoryPayload(recalledConverterPayload.orEmpty())
                MileageCalculatorScreen(
                    onBack = withHaptics {
                        recalledConverterPayload = null
                        activeHistoryTimestamp = null
                        selectedConverterCategoryKey = null
                    },
                    calculatorFont = calculatorTypography.uiFont,
                    displayFont = calculatorTypography.displayFont,
                    formatNumber = ::formatNumber,
                    initialDistance = recalledParts.getOrNull(1) ?: "0",
                    initialFuelQty = recalledParts.getOrNull(2) ?: "0",
                    initialFuelPrice = recalledParts.getOrNull(3) ?: "0",
                    initialField = recalledParts.getOrNull(4) ?: "distance",
                    onLogReset = {
                        activeHistoryTimestamp = null
                        recalledConverterPayload = null
                    },
                    onLogUpdate = { expressionText, note, payload ->
                        persistHistoryEntry(expressionText, "Mileage", note, payload)
                    }
                )
            } else if (selectedCategory.key == "age") {
                val recalledParts = decodeHistoryPayload(recalledConverterPayload.orEmpty())
                AgeCalculatorScreen(
                    onBack = withHaptics {
                        recalledConverterPayload = null
                        activeHistoryTimestamp = null
                        selectedConverterCategoryKey = null
                    },
                    calculatorFont = calculatorTypography.uiFont,
                    displayFont = calculatorTypography.displayFont,
                    initialDobMillis = recalledParts.getOrNull(1)?.toLongOrNull(),
                    initialCurrentMillis = recalledParts.getOrNull(2)?.toLongOrNull(),
                    onLogUpdate = { expressionText, note, payload ->
                        persistHistoryEntry(expressionText, "Age", note, payload)
                    }
                )
            } else if (selectedCategory.key == "bmi") {
                val recalledParts = decodeHistoryPayload(recalledConverterPayload.orEmpty())
                BMICalculatorScreen(
                    onBack = withHaptics {
                        recalledConverterPayload = null
                        activeHistoryTimestamp = null
                        selectedConverterCategoryKey = null
                    },
                    calculatorFont = calculatorTypography.uiFont,
                    displayFont = calculatorTypography.displayFont,
                    formatNumber = ::formatNumber,
                    initialWeightValue = recalledParts.getOrNull(1) ?: "0",
                    initialHeightValue = recalledParts.getOrNull(2) ?: "0",
                    initialWeightUnitIndex = recalledParts.getOrNull(3)?.toIntOrNull() ?: 0,
                    initialHeightUnitIndex = recalledParts.getOrNull(4)?.toIntOrNull() ?: 0,
                    initialActiveField = recalledParts.getOrNull(5) ?: "weight",
                    onLogReset = {
                        activeHistoryTimestamp = null
                        recalledConverterPayload = null
                    },
                    onLogUpdate = { expressionText, note, payload ->
                        persistHistoryEntry(expressionText, "BMI", note, payload)
                    }
                )
            } else if (selectedCategory.key == "scientific") {
                ScientificCalculatorScreenCleanV2(
                    onBack = withHaptics {
                        recalledConverterPayload = null
                        activeHistoryTimestamp = null
                        selectedConverterCategoryKey = null
                    },
                    calculatorFont = calculatorTypography.uiFont,
                    displayFont = calculatorTypography.displayFont,
                    formatNumber = ::formatNumber
                )
            } else if (selectedCategory.key == "discount") {
                val recalledParts = decodeHistoryPayload(recalledConverterPayload.orEmpty())
                DiscountCalculatorScreen(
                    onBack = withHaptics {
                        recalledConverterPayload = null
                        activeHistoryTimestamp = null
                        selectedConverterCategoryKey = null
                    },
                    calculatorFont = calculatorTypography.uiFont,
                    displayFont = calculatorTypography.displayFont,
                    formatNumber = ::formatNumber,
                    initialPrice = recalledParts.getOrNull(1) ?: "0",
                    initialDiscount = recalledParts.getOrNull(2) ?: "0",
                    initialField = recalledParts.getOrNull(3) ?: "price",
                    onLogReset = {
                        activeHistoryTimestamp = null
                        recalledConverterPayload = null
                    },
                    onLogUpdate = { expressionText, note, payload ->
                        persistHistoryEntry(expressionText, "Discount", note, payload)
                    }
                )
            } else {
                val recalledParts = decodeHistoryPayload(recalledConverterPayload.orEmpty())
                UnitConversionDetailScreen(
                    category = selectedCategory,
                    onBack = withHaptics {
                        recalledConverterPayload = null
                        activeHistoryTimestamp = null
                        selectedConverterCategoryKey = null
                    },
                    calculatorFont = calculatorTypography.uiFont,
                    displayFont = calculatorTypography.displayFont,
                    formatNumber = ::formatNumber,
                    initialInputValue = recalledParts.getOrNull(1) ?: "1",
                    initialFromIndex = recalledParts.getOrNull(2)?.toIntOrNull(),
                    initialToIndex = recalledParts.getOrNull(3)?.toIntOrNull(),
                    initialActiveInputSide = recalledParts.getOrNull(4) ?: "from",
                    currencyFetchedDate = currencySnapshot.fetchedDate,
                    onLogReset = {
                        activeHistoryTimestamp = null
                        recalledConverterPayload = null
                    },
                    onLogUpdate = { expressionText, note, payload ->
                        persistHistoryEntry(expressionText, selectedCategory.title, note, payload)
                    }
                )
            }
        }
    } else {
        CalculatorHomeScreen(
            currentValue = currentValue,
            expression = expression,
            onOpenUnitConverter = withHaptics {
                recalledConverterPayload = null
                activeHistoryTimestamp = null
                selectedConverterCategoryKey = null
                currentScreen = CalculatorScreenMode.UNIT_CONVERTER
            },
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
    CONTACT,
    UNIT_CONVERTER
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
        body = "Current version: 2.0"
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
    onOpenUnitConverter: () -> Unit,
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
    var menuExpanded by remember { mutableStateOf(false) }

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
                    UtilityIconButton(
                        onClick = { menuExpanded = true },
                        height = if (isSmallScreen) 28.dp else 32.dp,
                        width = if (isSmallScreen) 34.dp else 40.dp,
                        contentColor = WarmText.copy(alpha = 0.88f),
                        imageVector = Icons.Outlined.MoreVert,
                        contentDescription = "More"
                    )
                    DropdownMenu(
                        expanded = menuExpanded,
                        onDismissRequest = { menuExpanded = false },
                        containerColor = SurfaceColor,
                        shape = RoundedCornerShape(16.dp)
                    ) {
                        DropdownMenuItem(
                            text = { Text("Privacy policy", fontFamily = calculatorFont, color = WarmText) },
                            onClick = {
                                menuExpanded = false
                                onOpenPrivacy()
                            }
                        )
                        DropdownMenuItem(
                            text = { Text("About", fontFamily = calculatorFont, color = WarmText) },
                            onClick = {
                                menuExpanded = false
                                onOpenAbout()
                            }
                        )
                        DropdownMenuItem(
                            text = { Text("Contact", fontFamily = calculatorFont, color = WarmText) },
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
                        onClick = onOpenUnitConverter,
                        height = if (isSmallScreen) 28.dp else 32.dp,
                        width = if (isSmallScreen) 34.dp else 40.dp,
                        contentColor = WarmText.copy(alpha = 0.88f),
                        imageVector = Icons.Outlined.GridView,
                        contentDescription = "Unit converter"
                    )
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
    onDeleteEntry: (CalculatorHistoryEntry) -> Unit,
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
        val itemSize = if (compactSide < 360.dp) 16.sp else 18.sp
        val metaSize = if (compactSide < 360.dp) 12.sp else 14.sp
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
                    val rawCategoryMatch = entry.category.contains(query, ignoreCase = true)
                    val rawNoteMatch = entry.note.contains(query, ignoreCase = true)
                    val normalizedCategoryMatch = normalizeSearchText(entry.category).contains(normalizedQuery)
                    val normalizedNoteMatch = normalizeSearchText(entry.note).contains(normalizedQuery)
                    rawMatch || normalizedMatch || rawCategoryMatch || rawNoteMatch || normalizedCategoryMatch || normalizedNoteMatch
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
                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.SpaceBetween,
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Text(
                                        text = entry.category,
                                        color = AccentBorder,
                                        fontSize = metaSize,
                                        fontWeight = FontWeight.Medium,
                                        fontFamily = calculatorFont
                                    )
                                        Text(
                                            text = "Tap to Recall",
                                            color = AccentBorder,
                                            fontSize = metaSize,
                                            fontFamily = calculatorFont
                                        )
                                    }
                                    Spacer(modifier = Modifier.height(6.dp))
                                    Text(
                                        text = entry.expression,
                                        color = WarmText,
                                        fontSize = itemSize,
                                        fontWeight = FontWeight.Normal,
                                        fontFamily = calculatorFont
                                    )
                                    if (entry.note.isNotBlank()) {
                                        Spacer(modifier = Modifier.height(6.dp))
                                        Text(
                                            text = entry.note,
                                            color = SoftText.copy(alpha = 0.78f),
                                            fontSize = metaSize,
                                            fontFamily = calculatorFont
                                        )
                                    }
                                    Spacer(modifier = Modifier.height(8.dp))
                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.SpaceBetween,
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Text(
                                            text = historyTimeLabel(entry.timestamp),
                                            color = SoftText.copy(alpha = 0.52f),
                                            fontSize = metaSize,
                                            fontFamily = calculatorFont
                                        )
                                        Icon(
                                            imageVector = Icons.Outlined.DeleteOutline,
                                            contentDescription = "Delete entry",
                                            tint = DangerText.copy(alpha = 0.92f),
                                            modifier = Modifier
                                                .clickable(
                                                    interactionSource = remember { MutableInteractionSource() },
                                                    indication = null
                                                ) { onDeleteEntry(entry) }
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

private data class UnitConverterCategory(
    val title: String,
    val icon: ImageVector? = null,
    val iconLabel: String? = null
)

private data class ConversionUnit(
    val label: String,
    val symbol: String,
    val toBase: (Double) -> Double,
    val fromBase: (Double) -> Double
)

private data class UnitConversionCategory(
    val key: String,
    val title: String,
    val icon: ImageVector,
    val units: List<ConversionUnit>,
    val defaultFromIndex: Int = 0,
    val defaultToIndex: Int = 1
)

private fun linearUnit(label: String, symbol: String, factorToBase: Double): ConversionUnit {
    return ConversionUnit(
        label = label,
        symbol = symbol,
        toBase = { value -> value * factorToBase },
        fromBase = { value -> value / factorToBase }
    )
}

private fun mileageUnit(
    label: String,
    symbol: String,
    toKmPerLitre: (Double) -> Double,
    fromKmPerLitre: (Double) -> Double
): ConversionUnit {
    return ConversionUnit(
        label = label,
        symbol = symbol,
        toBase = toKmPerLitre,
        fromBase = fromKmPerLitre
    )
}

private fun unitConversionCategories(currencyRatesPerEuro: Map<String, Double> = DefaultCurrencyRatesPerEuro): List<UnitConversionCategory> {
    fun currencyUnit(label: String, symbol: String): ConversionUnit {
        val rate = currencyRatesPerEuro.getValue(symbol)
        return ConversionUnit(
            label = label,
            symbol = symbol,
            toBase = { value -> value / rate },
            fromBase = { value -> value * rate }
        )
    }

    return listOf(
        UnitConversionCategory(
            key = "currency",
            title = "Currency",
            icon = Icons.Outlined.CurrencyRupee,
            units = listOf(
                currencyUnit("Euro", "EUR"),
                currencyUnit("Indian rupee", "INR"),
                currencyUnit("US dollar", "USD"),
                currencyUnit("Pound sterling", "GBP"),
                currencyUnit("Japanese yen", "JPY"),
                currencyUnit("Australian dollar", "AUD"),
                currencyUnit("Canadian dollar", "CAD"),
                currencyUnit("Swiss franc", "CHF"),
                currencyUnit("Singapore dollar", "SGD"),
                currencyUnit("Chinese yuan", "CNY")
            ),
            defaultFromIndex = 1,
            defaultToIndex = 2
        ),
        UnitConversionCategory(
            key = "length",
            title = "Length",
            icon = Icons.Outlined.Straighten,
            units = listOf(
                linearUnit("Metre", "m", 1.0),
                linearUnit("Mile", "mi", 1609.344),
                linearUnit("Kilometre", "km", 1000.0),
                linearUnit("Centimetre", "cm", 0.01),
                linearUnit("Millimetre", "mm", 0.001),
                linearUnit("Inch", "in", 0.0254),
                linearUnit("Foot", "ft", 0.3048),
                linearUnit("Yard", "yd", 0.9144)
            ),
            defaultFromIndex = 0,
            defaultToIndex = 1
        ),
        UnitConversionCategory(
            key = "data",
            title = "Data",
            icon = Icons.Outlined.DataObject,
            units = listOf(
                linearUnit("Byte", "B", 1.0),
                linearUnit("Kilobyte", "KB", 1_000.0),
                linearUnit("Megabyte", "MB", 1_000_000.0),
                linearUnit("Gigabyte", "GB", 1_000_000_000.0),
                linearUnit("Terabyte", "TB", 1_000_000_000_000.0),
                linearUnit("Petabyte", "PB", 1_000_000_000_000_000.0),
                linearUnit("Kibibyte", "KiB", 1024.0),
                linearUnit("Mebibyte", "MiB", 1_048_576.0),
                linearUnit("Gibibyte", "GiB", 1_073_741_824.0),
                linearUnit("Tebibyte", "TiB", 1_099_511_627_776.0)
            ),
            defaultFromIndex = 2,
            defaultToIndex = 3
        ),
        UnitConversionCategory(
            key = "area",
            title = "Area",
            icon = Icons.Outlined.SquareFoot,
            units = listOf(
                linearUnit("Square metre", "m²", 1.0),
                linearUnit("Hectare", "ha", 10_000.0),
                linearUnit("Square kilometre", "km²", 1_000_000.0),
                linearUnit("Acre", "ac", 4046.8564224),
                linearUnit("Square foot", "ft²", 0.09290304),
                linearUnit("Square yard", "yd²", 0.83612736),
                linearUnit("Square mile", "mi²", 2_589_988.110336)
            ),
            defaultFromIndex = 0,
            defaultToIndex = 1
        ),
        UnitConversionCategory(
            key = "volume",
            title = "Volume",
            icon = Icons.Outlined.Inventory2,
            units = listOf(
                linearUnit("Litre", "L", 1.0),
                linearUnit("US gallon", "gal", 3.785411784),
                linearUnit("Millilitre", "mL", 0.001),
                linearUnit("Cubic metre", "m³", 1000.0),
                linearUnit("US pint", "pt", 0.473176473),
                linearUnit("Cup", "cup", 0.2365882365),
                linearUnit("Cubic foot", "ft³", 28.316846592)
            )
        ),
        UnitConversionCategory(
            key = "weight",
            title = "Weight",
            icon = Icons.Outlined.Scale,
            units = listOf(
                linearUnit("Kilogram", "kg", 1.0),
                linearUnit("Pound", "lb", 0.45359237),
                linearUnit("Gram", "g", 0.001),
                linearUnit("Milligram", "mg", 0.000001),
                linearUnit("Tonne", "t", 1000.0),
                linearUnit("Ounce", "oz", 0.028349523125),
                linearUnit("Stone", "st", 6.35029318)
            )
        ),
        UnitConversionCategory(
            key = "temperature",
            title = "Temperature",
            icon = Icons.Outlined.DeviceThermostat,
            units = listOf(
                ConversionUnit(
                    label = "Celsius",
                    symbol = "°C",
                    toBase = { it },
                    fromBase = { it }
                ),
                ConversionUnit(
                    label = "Fahrenheit",
                    symbol = "°F",
                    toBase = { (it - 32.0) * 5.0 / 9.0 },
                    fromBase = { (it * 9.0 / 5.0) + 32.0 }
                ),
                ConversionUnit(
                    label = "Kelvin",
                    symbol = "K",
                    toBase = { it - 273.15 },
                    fromBase = { it + 273.15 }
                )
            ),
            defaultFromIndex = 0,
            defaultToIndex = 1
        ),
        UnitConversionCategory(
            key = "speed",
            title = "Speed",
            icon = Icons.Outlined.Speed,
            units = listOf(
                linearUnit("Kilometre/hour", "km/h", 0.2777777778),
                linearUnit("Mile/hour", "mph", 0.44704),
                linearUnit("Metre/second", "m/s", 1.0),
                linearUnit("Knot", "kn", 0.5144444444),
                linearUnit("Foot/second", "ft/s", 0.3048),
                linearUnit("Centimetre/second", "cm/s", 0.01),
                linearUnit("Inch/second", "in/s", 0.0254),
                linearUnit("Mach", "Ma", 340.29)
            )
        ),
        UnitConversionCategory(
            key = "mileage",
            title = "Mileage",
            icon = Icons.Outlined.LocalGasStation,
            units = listOf(
                mileageUnit("Kilometre/litre", "km/L", { it }, { it }),
                mileageUnit(
                    "Litre/100 km",
                    "L/100km",
                    { value -> if (value == 0.0) 0.0 else 100.0 / value },
                    { value -> if (value == 0.0) 0.0 else 100.0 / value }
                ),
                mileageUnit("Miles/gallon (US)", "mpg US", { it * 0.4251437075 }, { it / 0.4251437075 }),
                mileageUnit("Miles/gallon (UK)", "mpg UK", { it * 0.3540061899 }, { it / 0.3540061899 })
            ),
            defaultFromIndex = 0,
            defaultToIndex = 2
        ),
        UnitConversionCategory(
            key = "age",
            title = "Age",
            icon = Icons.Outlined.Cake,
            units = emptyList(),
            defaultFromIndex = 0,
            defaultToIndex = 0
        ),
        UnitConversionCategory(
            key = "bmi",
            title = "BMI",
            icon = Icons.Outlined.Favorite,
            units = emptyList(),
            defaultFromIndex = 0,
            defaultToIndex = 0
        ),
        UnitConversionCategory(
            key = "scientific",
            title = "Scientific",
            icon = Icons.Outlined.Science,
            units = emptyList(),
            defaultFromIndex = 0,
            defaultToIndex = 0
        ),
        UnitConversionCategory(
            key = "discount",
            title = "Discount",
            icon = Icons.Outlined.LocalOffer,
            units = emptyList(),
            defaultFromIndex = 0,
            defaultToIndex = 0
        ),
        UnitConversionCategory(
            key = "pressure",
            title = "Pressure",
            icon = Icons.Outlined.Timer,
            units = listOf(
                linearUnit("Pascal", "Pa", 1.0),
                linearUnit("Kilopascal", "kPa", 1000.0),
                linearUnit("Bar", "bar", 100_000.0),
                linearUnit("PSI", "psi", 6894.757293),
                linearUnit("Atmosphere", "atm", 101_325.0),
                linearUnit("mmHg", "mmHg", 133.322387415)
            ),
            defaultFromIndex = 2,
            defaultToIndex = 3
        ),
        UnitConversionCategory(
            key = "power",
            title = "Power",
            icon = Icons.Outlined.Bolt,
            units = listOf(
                linearUnit("Watt", "W", 1.0),
                linearUnit("Kilowatt", "kW", 1000.0),
                linearUnit("Horsepower", "hp", 745.6998715823),
                linearUnit("Megawatt", "MW", 1_000_000.0)
            ),
            defaultFromIndex = 0,
            defaultToIndex = 2
        )
    )
}

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
private fun UnitConverterScreen(
    categories: List<UnitConversionCategory>,
    onBack: () -> Unit,
    onSelectCategory: (String) -> Unit,
    calculatorFont: FontFamily
) {
    val sortedCategories = remember(categories) { categories.sortedBy { it.title.lowercase(Locale.getDefault()) } }
    BoxWithConstraints(
        modifier = Modifier
            .fillMaxSize()
            .background(AppBackgroundBrush)
            .statusBarsPadding()
            .navigationBarsPadding()
    ) {
        val compactSide = if (maxWidth < maxHeight) maxWidth else maxHeight
        val outerPadding = (compactSide * 0.05f).coerceIn(16.dp, 28.dp)
        val gridGap = (compactSide * 0.024f).coerceIn(8.dp, 12.dp)
        val titleSize = if (compactSide < 360.dp) 19.sp else 23.sp
        val labelSize = if (compactSide < 360.dp) 11.sp else 13.sp
        val cardHeight = if (compactSide < 360.dp) 110.dp else 132.dp
        val iconSize = if (compactSide < 360.dp) 24.dp else 28.dp
        val badgeSize = if (compactSide < 360.dp) 36.dp else 42.dp

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(outerPadding)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                UtilityIconButton(
                    onClick = onBack,
                    height = if (compactSide < 360.dp) 34.dp else 38.dp,
                    width = if (compactSide < 360.dp) 40.dp else 44.dp,
                    contentColor = WarmText,
                    imageVector = Icons.AutoMirrored.Outlined.ArrowBack,
                    contentDescription = "Back"
                )
                Spacer(modifier = Modifier.width(14.dp))
                Text(
                    text = "Unit converter",
                    color = WarmText,
                    fontSize = titleSize,
                    fontWeight = FontWeight.Medium,
                    fontFamily = calculatorFont
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            LazyVerticalGrid(
                columns = GridCells.Fixed(3),
                verticalArrangement = Arrangement.spacedBy(gridGap),
                horizontalArrangement = Arrangement.spacedBy(gridGap),
                modifier = Modifier.fillMaxSize()
            ) {
                items(sortedCategories.size) { index ->
                    val category = sortedCategories[index]
                    Surface(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(cardHeight),
                        shape = RoundedCornerShape(24.dp),
                        color = Color.Transparent,
                        border = BorderStroke(1.dp, SubtleBorder.copy(alpha = 0.8f))
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxSize()
                                .background(PanelBrush, RoundedCornerShape(24.dp))
                                .combinedClickable(onClick = { onSelectCategory(category.key) })
                                .padding(vertical = 12.dp, horizontal = 8.dp),
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.Center
                        ) {
                            Box(
                                modifier = Modifier
                                    .width(badgeSize)
                                    .height(badgeSize)
                                    .background(
                                        brush = UtilityBrush,
                                        shape = RoundedCornerShape(badgeSize * 0.45f)
                                    ),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = category.icon,
                                    contentDescription = category.title,
                                    tint = WarmText.copy(alpha = 0.96f),
                                    modifier = Modifier
                                        .width(iconSize)
                                        .height(iconSize)
                                )
                            }
                            Spacer(modifier = Modifier.height(10.dp))
                            Text(
                                text = category.title,
                                color = WarmText,
                                fontSize = labelSize,
                                fontWeight = FontWeight.Normal,
                                fontFamily = calculatorFont,
                                textAlign = TextAlign.Center,
                                lineHeight = labelSize * 1.08f,
                                maxLines = 1
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun UnitConversionDetailScreen(
    category: UnitConversionCategory,
    onBack: () -> Unit,
    calculatorFont: FontFamily,
    displayFont: FontFamily,
    formatNumber: (String) -> String,
    initialInputValue: String = "1",
    initialFromIndex: Int? = null,
    initialToIndex: Int? = null,
    initialActiveInputSide: String = "from",
    currencyFetchedDate: String? = null,
    onLogReset: () -> Unit = {},
    onLogUpdate: (String, String, String) -> Unit = { _, _, _ -> }
) {
    var inputValue by remember(category.key, initialInputValue) { mutableStateOf(initialInputValue) }
    var fromIndex by remember(category.key, initialFromIndex) {
        mutableStateOf((initialFromIndex ?: category.defaultFromIndex).coerceIn(category.units.indices))
    }
    var toIndex by remember(category.key, initialToIndex) {
        mutableStateOf((initialToIndex ?: category.defaultToIndex).coerceIn(category.units.indices))
    }
    var activeInputSide by remember(category.key, initialActiveInputSide) { mutableStateOf(initialActiveInputSide) }
    var expandedMenu by remember(category.key) { mutableStateOf<Int?>(null) }
    var shouldReplaceOnNextInput by remember(category.key, initialActiveInputSide) { mutableStateOf(false) }

    fun parsedInput(): Double {
        val cleaned = inputValue.removePrefix("+").trim()
        val parseable = if (cleaned.endsWith(".")) cleaned.dropLast(1) else cleaned
        return parseable.toDoubleOrNull() ?: 0.0
    }

    fun formatConversionValue(value: Double): String {
        if (!value.isFinite()) return "Error"
        val absValue = abs(value)
        if (absValue != 0.0 && (absValue >= 1_000_000_000_000.0 || absValue < 0.000001)) {
            return DecimalFormat("0.######E0").format(value)
        }
        val rounded = BigDecimal.valueOf(value)
            .setScale(6, RoundingMode.HALF_UP)
            .stripTrailingZeros()
            .toPlainString()
        return formatNumber(rounded)
    }

    fun editableValueString(value: Double): String {
        if (!value.isFinite()) return "0"
        return BigDecimal.valueOf(value)
            .setScale(6, RoundingMode.HALF_UP)
            .stripTrailingZeros()
            .toPlainString()
    }

    fun recordHistory(
        updatedInput: String = inputValue,
        updatedFromIndex: Int = fromIndex,
        updatedToIndex: Int = toIndex,
        updatedActiveSide: String = activeInputSide
    ) {
        val safeFromUnit = category.units[updatedFromIndex]
        val safeToUnit = category.units[updatedToIndex]
        val cleaned = updatedInput.removePrefix("+").trim()
        val parseable = if (cleaned.endsWith(".")) cleaned.dropLast(1) else cleaned
        val parsed = parseable.toDoubleOrNull() ?: 0.0
        val fromText: String
        val toText: String
        if (updatedActiveSide == "from") {
            val converted = safeToUnit.fromBase(safeFromUnit.toBase(parsed))
            fromText = formatNumber(updatedInput)
            toText = formatConversionValue(converted)
        } else {
            val converted = safeFromUnit.fromBase(safeToUnit.toBase(parsed))
            fromText = formatConversionValue(converted)
            toText = formatNumber(updatedInput)
        }
        val summary = "$fromText ${safeFromUnit.symbol} = $toText ${safeToUnit.symbol}"
        val note = "1 ${safeFromUnit.symbol} = ${formatConversionValue(safeToUnit.fromBase(safeFromUnit.toBase(1.0)))} ${safeToUnit.symbol}"
        val payload = encodeHistoryPayload(
            category.key,
            updatedInput,
            updatedFromIndex.toString(),
            updatedToIndex.toString(),
            updatedActiveSide
        )
        onLogUpdate(summary, note, payload)
    }

    fun appendToken(token: String) {
        inputValue = when {
            shouldReplaceOnNextInput && token == "00" -> "0"
            shouldReplaceOnNextInput -> token
            inputValue == "0" && token == "00" -> "0"
            inputValue == "0" -> token
            else -> inputValue + token
        }
        shouldReplaceOnNextInput = false
        recordHistory(updatedInput = inputValue)
    }

    fun appendDecimal() {
        if (shouldReplaceOnNextInput) {
            inputValue = "0."
            shouldReplaceOnNextInput = false
            recordHistory(updatedInput = inputValue)
        } else if (!inputValue.contains(".")) {
            inputValue += "."
            recordHistory(updatedInput = inputValue)
        }
    }

    fun deleteLast() {
        inputValue = when {
            inputValue.length <= 1 -> "0"
            else -> inputValue.dropLast(1)
        }
        recordHistory(updatedInput = inputValue)
    }

    fun clearValue() {
        inputValue = "0"
        shouldReplaceOnNextInput = false
        onLogReset()
    }

    val fromUnit = category.units[fromIndex]
    val toUnit = category.units[toIndex]
    val parsedValue = parsedInput()
    val fromDisplayValue: String
    val toDisplayValue: String
    val oneFromToTo = toUnit.fromBase(fromUnit.toBase(1.0))
    val oneToToFrom = fromUnit.fromBase(toUnit.toBase(1.0))

    if (activeInputSide == "from") {
        val baseValue = fromUnit.toBase(parsedValue)
        val convertedValue = toUnit.fromBase(baseValue)
        fromDisplayValue = formatNumber(inputValue)
        toDisplayValue = formatConversionValue(convertedValue)
    } else {
        val baseValue = toUnit.toBase(parsedValue)
        val convertedValue = fromUnit.fromBase(baseValue)
        fromDisplayValue = formatConversionValue(convertedValue)
        toDisplayValue = formatNumber(inputValue)
    }

    BoxWithConstraints(
        modifier = Modifier
            .fillMaxSize()
            .background(AppBackgroundBrush)
            .statusBarsPadding()
            .navigationBarsPadding()
    ) {
        val compactSide = if (maxWidth < maxHeight) maxWidth else maxHeight
        val outerPadding = (compactSide * 0.05f).coerceIn(16.dp, 24.dp)
        val topGap = (compactSide * 0.04f).coerceIn(12.dp, 20.dp)
        val keypadGap = (compactSide * 0.022f).coerceIn(8.dp, 12.dp)
        val numberButtonSize = if (compactSide < 360.dp) 74.dp else 86.dp
        val sideButtonWidth = numberButtonSize
        val titleSize = if (compactSide < 360.dp) 18.sp else 21.sp
        val unitLabelSize = if (compactSide < 360.dp) 12.sp else 14.sp
        val valueSize = if (compactSide < 360.dp) 22.sp else 26.sp
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(outerPadding)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                UtilityIconButton(
                    onClick = onBack,
                    height = 34.dp,
                    width = 40.dp,
                    contentColor = WarmText,
                    imageVector = Icons.AutoMirrored.Outlined.ArrowBack,
                    contentDescription = "Back"
                )
                Spacer(modifier = Modifier.width(14.dp))
                Text(
                    text = "${category.title} conversion",
                    color = WarmText,
                    fontSize = titleSize,
                    fontWeight = FontWeight.Normal,
                    fontFamily = calculatorFont
                )
            }

            Spacer(modifier = Modifier.height(topGap))

            UnitSelectionCard(
                unit = fromUnit,
                value = fromDisplayValue,
                calculatorFont = calculatorFont,
                unitLabelSize = unitLabelSize,
                valueSize = valueSize,
                valueColor = if (activeInputSide == "from") AccentBorder else WarmText,
                isActive = activeInputSide == "from",
                onActivate = {
                    if (activeInputSide != "from") {
                        inputValue = editableValueString(fromUnit.fromBase(toUnit.toBase(parsedValue)))
                        activeInputSide = "from"
                        shouldReplaceOnNextInput = true
                        recordHistory(updatedInput = inputValue, updatedActiveSide = "from")
                    }
                },
                onOpenMenu = { expandedMenu = 0 },
                expanded = expandedMenu == 0,
                units = category.units,
                onDismiss = { expandedMenu = null },
                onSelectUnit = {
                    val newInput = if (activeInputSide == "from") {
                        inputValue
                    } else {
                        editableValueString(category.units[it].fromBase(toUnit.toBase(parsedValue)))
                    }
                    fromIndex = it
                    inputValue = newInput
                    activeInputSide = "from"
                    shouldReplaceOnNextInput = true
                    expandedMenu = null
                    recordHistory(
                        updatedInput = newInput,
                        updatedFromIndex = it,
                        updatedActiveSide = "from"
                    )
                }
            )

            Spacer(modifier = Modifier.height(14.dp))

            UnitSelectionCard(
                unit = toUnit,
                value = toDisplayValue,
                calculatorFont = calculatorFont,
                unitLabelSize = unitLabelSize,
                valueSize = valueSize,
                valueColor = if (activeInputSide == "to") AccentBorder else WarmText,
                isActive = activeInputSide == "to",
                onActivate = {
                    if (activeInputSide != "to") {
                        inputValue = editableValueString(toUnit.fromBase(fromUnit.toBase(parsedValue)))
                        activeInputSide = "to"
                        shouldReplaceOnNextInput = true
                        recordHistory(updatedInput = inputValue, updatedActiveSide = "to")
                    }
                },
                onOpenMenu = { expandedMenu = 1 },
                expanded = expandedMenu == 1,
                units = category.units,
                onDismiss = { expandedMenu = null },
                onSelectUnit = {
                    val newInput = if (activeInputSide == "to") {
                        inputValue
                    } else {
                        editableValueString(category.units[it].fromBase(fromUnit.toBase(parsedValue)))
                    }
                    toIndex = it
                    inputValue = newInput
                    activeInputSide = "to"
                    shouldReplaceOnNextInput = true
                    expandedMenu = null
                    recordHistory(
                        updatedInput = newInput,
                        updatedToIndex = it,
                        updatedActiveSide = "to"
                    )
                }
            )

            if (category.key == "currency") {
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "Latest fetched date: ${formatFetchedCurrencyDate(currencyFetchedDate)}",
                    color = SoftText.copy(alpha = 0.58f),
                    fontSize = 11.sp,
                    fontFamily = calculatorFont,
                    modifier = Modifier.padding(start = 4.dp)
                )
            }

            Spacer(modifier = Modifier.height(10.dp))
            Text(
                text = if (activeInputSide == "from") {
                    "Formula: 1 ${fromUnit.symbol} = ${formatConversionValue(oneFromToTo)} ${toUnit.symbol}"
                } else {
                    "Formula: 1 ${toUnit.symbol} = ${formatConversionValue(oneToToFrom)} ${fromUnit.symbol}"
                },
                color = SoftText.copy(alpha = 0.72f),
                fontSize = 12.sp,
                fontFamily = calculatorFont,
                modifier = Modifier.padding(start = 4.dp)
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = if (activeInputSide == "from") {
                    "Math: ${formatNumber(inputValue)} ${fromUnit.symbol} = $toDisplayValue ${toUnit.symbol}"
                } else {
                    "Math: ${formatNumber(inputValue)} ${toUnit.symbol} = $fromDisplayValue ${fromUnit.symbol}"
                },
                color = WarmText,
                fontSize = 13.sp,
                fontFamily = calculatorFont,
                modifier = Modifier.padding(start = 4.dp)
            )

            Spacer(modifier = Modifier.weight(1f))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(keypadGap)
            ) {
                Column(
                    modifier = Modifier.weight(1f),
                    verticalArrangement = Arrangement.spacedBy(keypadGap)
                ) {
                    ConversionKeypadRow(
                        labels = listOf("7", "8", "9"),
                        buttonWidth = numberButtonSize,
                        buttonHeight = numberButtonSize,
                        buttonGap = keypadGap,
                        calculatorFont = calculatorFont,
                        onTap = ::appendToken
                    )
                    ConversionKeypadRow(
                        labels = listOf("4", "5", "6"),
                        buttonWidth = numberButtonSize,
                        buttonHeight = numberButtonSize,
                        buttonGap = keypadGap,
                        calculatorFont = calculatorFont,
                        onTap = ::appendToken
                    )
                    ConversionKeypadRow(
                        labels = listOf("1", "2", "3"),
                        buttonWidth = numberButtonSize,
                        buttonHeight = numberButtonSize,
                        buttonGap = keypadGap,
                        calculatorFont = calculatorFont,
                        onTap = ::appendToken
                    )
                    ConversionKeypadRow(
                        labels = listOf("00", "0", "."),
                        buttonWidth = numberButtonSize,
                        buttonHeight = numberButtonSize,
                        buttonGap = keypadGap,
                        calculatorFont = calculatorFont,
                        onTap = { token ->
                            if (token == ".") appendDecimal() else appendToken(token)
                        }
                    )
                }

                Column(
                    modifier = Modifier.width(sideButtonWidth),
                    verticalArrangement = Arrangement.spacedBy(keypadGap)
                ) {
                    ConversionTallButton(
                        label = "AC",
                        onClick = ::clearValue,
                        height = numberButtonSize * 2 + keypadGap,
                        width = sideButtonWidth,
                        calculatorFont = calculatorFont,
                        textColor = DangerText,
                        brush = SpecialBrush,
                        borderColor = DangerBorder
                    )
                    ConversionTallButton(
                        label = "\u232B",
                        onClick = ::deleteLast,
                        height = numberButtonSize * 2 + keypadGap,
                        width = sideButtonWidth,
                        calculatorFont = calculatorFont,
                        textColor = WarmText,
                        brush = UtilityBrush,
                        borderColor = SubtleBorder
                    )
                }
            }
        }
    }
}

@Composable
private fun MileageCalculatorScreen(
    onBack: () -> Unit,
    calculatorFont: FontFamily,
    displayFont: FontFamily,
    formatNumber: (String) -> String,
    initialDistance: String = "0",
    initialFuelQty: String = "0",
    initialFuelPrice: String = "0",
    initialField: String = "distance",
    onLogReset: () -> Unit = {},
    onLogUpdate: (String, String, String) -> Unit = { _, _, _ -> }
) {
    var distanceValue by remember(initialDistance) { mutableStateOf(initialDistance) }
    var fuelQtyValue by remember(initialFuelQty) { mutableStateOf(initialFuelQty) }
    var fuelPriceValue by remember(initialFuelPrice) { mutableStateOf(initialFuelPrice) }
    var activeField by remember(initialField) { mutableStateOf(initialField) }
    var shouldReplaceOnNextInput by remember(initialField) { mutableStateOf(false) }

    fun parseValue(raw: String): Double {
        val cleaned = raw.removePrefix("+").trim()
        val parseable = if (cleaned.endsWith(".")) cleaned.dropLast(1) else cleaned
        return parseable.toDoubleOrNull() ?: 0.0
    }

    fun activeText(): String {
        return when (activeField) {
            "distance" -> distanceValue
            "fuel_qty" -> fuelQtyValue
            else -> fuelPriceValue
        }
    }

    fun setActiveText(value: String) {
        when (activeField) {
            "distance" -> distanceValue = value
            "fuel_qty" -> fuelQtyValue = value
            else -> fuelPriceValue = value
        }
    }

    fun formatComputed(value: Double): String {
        if (!value.isFinite()) return "Error"
        val rounded = BigDecimal.valueOf(value)
            .setScale(4, RoundingMode.HALF_UP)
            .stripTrailingZeros()
            .toPlainString()
        return formatNumber(rounded)
    }

    fun recordHistory(
        updatedDistance: String = distanceValue,
        updatedFuelQty: String = fuelQtyValue,
        updatedFuelPrice: String = fuelPriceValue,
        updatedField: String = activeField
    ) {
        val distanceNum = parseValue(updatedDistance)
        val fuelQtyNum = parseValue(updatedFuelQty)
        val fuelPriceNum = parseValue(updatedFuelPrice)
        val totalFuel = fuelQtyNum * fuelPriceNum
        val mileageValue = if (fuelQtyNum == 0.0) 0.0 else distanceNum / fuelQtyNum
        val summary = "${formatNumber(updatedDistance)} km, ${formatNumber(updatedFuelQty)} L @ ${formatNumber(updatedFuelPrice)} = ${formatComputed(mileageValue)} km/L"
        val note = "Fuel amount ${formatComputed(totalFuel)}"
        val payload = encodeHistoryPayload(
            "mileage",
            updatedDistance,
            updatedFuelQty,
            updatedFuelPrice,
            updatedField
        )
        onLogUpdate(summary, note, payload)
    }

    fun appendToken(token: String) {
        val current = activeText()
        val updated = when {
                shouldReplaceOnNextInput && token == "00" -> "0"
                shouldReplaceOnNextInput -> token
                current == "0" && token == "00" -> "0"
                current == "0" -> token
                else -> current + token
            }
        setActiveText(updated)
        shouldReplaceOnNextInput = false
        when (activeField) {
            "distance" -> recordHistory(updatedDistance = updated)
            "fuel_qty" -> recordHistory(updatedFuelQty = updated)
            else -> recordHistory(updatedFuelPrice = updated)
        }
    }

    fun appendDecimal() {
        val current = activeText()
        if (shouldReplaceOnNextInput) {
            val updated = "0."
            setActiveText(updated)
            shouldReplaceOnNextInput = false
            when (activeField) {
                "distance" -> recordHistory(updatedDistance = updated)
                "fuel_qty" -> recordHistory(updatedFuelQty = updated)
                else -> recordHistory(updatedFuelPrice = updated)
            }
        } else if (!current.contains(".")) {
            val updated = "$current."
            setActiveText(updated)
            when (activeField) {
                "distance" -> recordHistory(updatedDistance = updated)
                "fuel_qty" -> recordHistory(updatedFuelQty = updated)
                else -> recordHistory(updatedFuelPrice = updated)
            }
        }
    }

    fun deleteLast() {
        val current = activeText()
        val updated = when {
                current.length <= 1 -> "0"
                else -> current.dropLast(1)
            }
        setActiveText(updated)
        when (activeField) {
            "distance" -> recordHistory(updatedDistance = updated)
            "fuel_qty" -> recordHistory(updatedFuelQty = updated)
            else -> recordHistory(updatedFuelPrice = updated)
        }
    }

    fun clearAll() {
        distanceValue = "0"
        fuelQtyValue = "0"
        fuelPriceValue = "0"
        shouldReplaceOnNextInput = false
        onLogReset()
    }

    val distance = parseValue(distanceValue)
    val fuelQty = parseValue(fuelQtyValue)
    val fuelPrice = parseValue(fuelPriceValue)
    val totalFuelAmount = fuelQty * fuelPrice
    val mileage = if (fuelQty == 0.0) 0.0 else distance / fuelQty

    BoxWithConstraints(
        modifier = Modifier
            .fillMaxSize()
            .background(AppBackgroundBrush)
            .statusBarsPadding()
            .navigationBarsPadding()
    ) {
        val compactSide = if (maxWidth < maxHeight) maxWidth else maxHeight
        val outerPadding = (compactSide * 0.05f).coerceIn(16.dp, 24.dp)
        val topGap = (compactSide * 0.04f).coerceIn(12.dp, 20.dp)
        val keypadGap = (compactSide * 0.022f).coerceIn(8.dp, 12.dp)
        val numberButtonSize = if (compactSide < 360.dp) 74.dp else 86.dp
        val sideButtonWidth = numberButtonSize
        val titleSize = if (compactSide < 360.dp) 18.sp else 21.sp
        val unitLabelSize = if (compactSide < 360.dp) 12.sp else 14.sp
        val valueSize = if (compactSide < 360.dp) 22.sp else 26.sp
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(outerPadding)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                UtilityIconButton(
                    onClick = onBack,
                    height = 34.dp,
                    width = 40.dp,
                    contentColor = WarmText,
                    imageVector = Icons.AutoMirrored.Outlined.ArrowBack,
                    contentDescription = "Back"
                )
                Spacer(modifier = Modifier.width(14.dp))
                Text(
                    text = "Mileage calculator",
                    color = WarmText,
                    fontSize = titleSize,
                    fontWeight = FontWeight.Normal,
                    fontFamily = calculatorFont
                )
            }

            Spacer(modifier = Modifier.height(topGap))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                MileageInputCard(
                    title = "Distance driven",
                    value = formatNumber(distanceValue),
                    isActive = activeField == "distance",
                    onClick = {
                        activeField = "distance"
                        shouldReplaceOnNextInput = true
                    },
                    calculatorFont = calculatorFont,
                    displayFont = displayFont,
                    unitLabelSize = unitLabelSize,
                    valueSize = valueSize,
                    height = 106.dp,
                    modifier = Modifier.weight(1f)
                )
                MileageInputCard(
                    title = "Fuel qty (L)",
                    value = formatNumber(fuelQtyValue),
                    isActive = activeField == "fuel_qty",
                    onClick = {
                        activeField = "fuel_qty"
                        shouldReplaceOnNextInput = true
                    },
                    calculatorFont = calculatorFont,
                    displayFont = displayFont,
                    unitLabelSize = unitLabelSize,
                    valueSize = valueSize,
                    height = 106.dp,
                    modifier = Modifier.weight(1f)
                )
            }
            Spacer(modifier = Modifier.height(10.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                MileageInputCard(
                    title = "Fuel price (/L)",
                    value = formatNumber(fuelPriceValue),
                    isActive = activeField == "fuel_price",
                    onClick = {
                        activeField = "fuel_price"
                        shouldReplaceOnNextInput = true
                    },
                    calculatorFont = calculatorFont,
                    displayFont = displayFont,
                    unitLabelSize = unitLabelSize,
                    valueSize = valueSize,
                    height = 106.dp,
                    modifier = Modifier.weight(1f)
                )
                MileageOutputCard(
                    title = "Total fuel amount",
                    value = formatComputed(totalFuelAmount),
                    calculatorFont = calculatorFont,
                    displayFont = displayFont,
                    unitLabelSize = unitLabelSize,
                    valueSize = valueSize,
                    height = 106.dp,
                    modifier = Modifier.weight(1f)
                )
            }
            Spacer(modifier = Modifier.height(10.dp))
            MileageOutputCard(
                title = "Total mileage (km/L)",
                value = formatComputed(mileage),
                calculatorFont = calculatorFont,
                displayFont = displayFont,
                unitLabelSize = unitLabelSize,
                valueSize = valueSize,
                height = 110.dp,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.weight(1f))

            Spacer(modifier = Modifier.height(18.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(keypadGap)
            ) {
                Column(
                    modifier = Modifier.weight(1f),
                    verticalArrangement = Arrangement.spacedBy(keypadGap)
                ) {
                    ConversionKeypadRow(
                        labels = listOf("7", "8", "9"),
                        buttonWidth = numberButtonSize,
                        buttonHeight = numberButtonSize,
                        buttonGap = keypadGap,
                        calculatorFont = calculatorFont,
                        onTap = ::appendToken
                    )
                    ConversionKeypadRow(
                        labels = listOf("4", "5", "6"),
                        buttonWidth = numberButtonSize,
                        buttonHeight = numberButtonSize,
                        buttonGap = keypadGap,
                        calculatorFont = calculatorFont,
                        onTap = ::appendToken
                    )
                    ConversionKeypadRow(
                        labels = listOf("1", "2", "3"),
                        buttonWidth = numberButtonSize,
                        buttonHeight = numberButtonSize,
                        buttonGap = keypadGap,
                        calculatorFont = calculatorFont,
                        onTap = ::appendToken
                    )
                    ConversionKeypadRow(
                        labels = listOf("00", "0", "."),
                        buttonWidth = numberButtonSize,
                        buttonHeight = numberButtonSize,
                        buttonGap = keypadGap,
                        calculatorFont = calculatorFont,
                        onTap = { token ->
                            if (token == ".") appendDecimal() else appendToken(token)
                        }
                    )
                }

                Column(
                    modifier = Modifier.width(sideButtonWidth),
                    verticalArrangement = Arrangement.spacedBy(keypadGap)
                ) {
                    ConversionTallButton(
                        label = "AC",
                        onClick = ::clearAll,
                        height = numberButtonSize * 2 + keypadGap,
                        width = sideButtonWidth,
                        calculatorFont = calculatorFont,
                        textColor = DangerText,
                        brush = SpecialBrush,
                        borderColor = DangerBorder
                    )
                    ConversionTallButton(
                        label = "\u232B",
                        onClick = ::deleteLast,
                        height = numberButtonSize * 2 + keypadGap,
                        width = sideButtonWidth,
                        calculatorFont = calculatorFont,
                        textColor = WarmText,
                        brush = UtilityBrush,
                        borderColor = SubtleBorder
                    )
                }
            }
            Spacer(modifier = Modifier.height(8.dp))
        }
    }
}

@Composable
private fun MileageInputCard(
    modifier: Modifier = Modifier,
    height: Dp,
    title: String,
    value: String,
    isActive: Boolean,
    onClick: () -> Unit,
    calculatorFont: FontFamily,
    displayFont: FontFamily,
    unitLabelSize: TextUnit,
    valueSize: TextUnit
) {
    Surface(
        modifier = modifier
            .fillMaxWidth()
            .height(height)
            .combinedClickable(onClick = onClick),
        shape = RoundedCornerShape(24.dp),
        color = Color.Transparent,
        border = BorderStroke(1.dp, if (isActive) AccentBorder else SubtleBorder.copy(alpha = 0.72f))
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(PanelBrush, RoundedCornerShape(24.dp))
                .padding(horizontal = 16.dp, vertical = 16.dp)
        ) {
            Text(
                text = title,
                color = SoftText.copy(alpha = 0.72f),
                fontSize = unitLabelSize,
                fontWeight = FontWeight.Medium,
                fontFamily = calculatorFont
            )
            Spacer(modifier = Modifier.height(10.dp))
                Text(
                    text = value,
                    color = if (isActive) AccentBorder else WarmText,
                    fontSize = valueSize,
                    lineHeight = valueSize * 1.15f,
                    fontWeight = FontWeight.Normal,
                    fontFamily = calculatorFont,
                    maxLines = 1
                )
        }
    }
}

@Composable
private fun MileageOutputCard(
    modifier: Modifier = Modifier,
    height: Dp,
    title: String,
    value: String,
    calculatorFont: FontFamily,
    displayFont: FontFamily,
    unitLabelSize: TextUnit,
    valueSize: TextUnit
) {
    Surface(
        modifier = modifier
            .fillMaxWidth()
            .height(height),
        shape = RoundedCornerShape(24.dp),
        color = Color.Transparent,
        border = BorderStroke(1.dp, SubtleBorder.copy(alpha = 0.72f))
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(PanelBrush, RoundedCornerShape(24.dp))
                .padding(horizontal = 16.dp, vertical = 16.dp)
        ) {
            Text(
                text = title,
                color = SoftText.copy(alpha = 0.72f),
                fontSize = unitLabelSize,
                fontWeight = FontWeight.Medium,
                fontFamily = calculatorFont
            )
            Spacer(modifier = Modifier.height(10.dp))
            Text(
                text = value,
                color = WarmText,
                fontSize = valueSize,
                lineHeight = valueSize * 1.15f,
                fontWeight = FontWeight.Normal,
                fontFamily = calculatorFont,
                maxLines = 1
            )
        }
    }
}

@Composable
private fun DiscountCalculatorScreen(
    onBack: () -> Unit,
    calculatorFont: FontFamily,
    displayFont: FontFamily,
    formatNumber: (String) -> String,
    initialPrice: String = "0",
    initialDiscount: String = "0",
    initialField: String = "price",
    onLogReset: () -> Unit = {},
    onLogUpdate: (String, String, String) -> Unit = { _, _, _ -> }
) {
    var priceValue by remember(initialPrice) { mutableStateOf(initialPrice) }
    var discountValue by remember(initialDiscount) { mutableStateOf(initialDiscount) }
    var activeField by remember(initialField) { mutableStateOf(initialField) }
    var shouldReplaceOnNextInput by remember(initialField) { mutableStateOf(false) }

    fun parseValue(raw: String): Double {
        val cleaned = raw.removePrefix("+").trim()
        val parseable = if (cleaned.endsWith(".")) cleaned.dropLast(1) else cleaned
        return parseable.toDoubleOrNull() ?: 0.0
    }

    fun activeText(): String {
        return when (activeField) {
            "price" -> priceValue
            else -> discountValue
        }
    }

    fun setActiveText(value: String) {
        when (activeField) {
            "price" -> priceValue = value
            else -> discountValue = value
        }
    }

    fun formatComputed(value: Double): String {
        if (!value.isFinite()) return "Error"
        val rounded = BigDecimal.valueOf(value)
            .setScale(4, RoundingMode.HALF_UP)
            .stripTrailingZeros()
            .toPlainString()
        return formatNumber(rounded)
    }

    fun recordHistory(
        updatedPrice: String = priceValue,
        updatedDiscount: String = discountValue,
        updatedField: String = activeField
    ) {
        val priceNum = parseValue(updatedPrice)
        val discountNum = parseValue(updatedDiscount)
        val finalNum = priceNum - (priceNum * discountNum / 100.0)
        val summary = "${formatNumber(updatedPrice)} at ${formatNumber(updatedDiscount)}% = ${formatComputed(finalNum)}"
        val note = "Final price = Price - (Price × Discount ÷ 100)"
        val payload = encodeHistoryPayload(
            "discount",
            updatedPrice,
            updatedDiscount,
            updatedField
        )
        onLogUpdate(summary, note, payload)
    }

    fun appendToken(token: String) {
        val current = activeText()
        val updated = when {
                shouldReplaceOnNextInput && token == "00" -> "0"
                shouldReplaceOnNextInput -> token
                current == "0" && token == "00" -> "0"
                current == "0" -> token
                else -> current + token
            }
        setActiveText(updated)
        shouldReplaceOnNextInput = false
        if (activeField == "price") recordHistory(updatedPrice = updated) else recordHistory(updatedDiscount = updated)
    }

    fun appendDecimal() {
        val current = activeText()
        if (shouldReplaceOnNextInput) {
            val updated = "0."
            setActiveText(updated)
            shouldReplaceOnNextInput = false
            if (activeField == "price") recordHistory(updatedPrice = updated) else recordHistory(updatedDiscount = updated)
        } else if (!current.contains(".")) {
            val updated = "$current."
            setActiveText(updated)
            if (activeField == "price") recordHistory(updatedPrice = updated) else recordHistory(updatedDiscount = updated)
        }
    }

    fun deleteLast() {
        val current = activeText()
        val updated = when {
                current.length <= 1 -> "0"
                else -> current.dropLast(1)
            }
        setActiveText(updated)
        if (activeField == "price") recordHistory(updatedPrice = updated) else recordHistory(updatedDiscount = updated)
    }

    fun clearAll() {
        priceValue = "0"
        discountValue = "0"
        shouldReplaceOnNextInput = false
        onLogReset()
    }

    val price = parseValue(priceValue)
    val discountPercent = parseValue(discountValue)
    val discountAmount = price * discountPercent / 100.0
    val finalPrice = price - discountAmount
    val explanation = "${formatComputed(price)} - (${formatComputed(price)} × ${formatComputed(discountPercent)}%) = ${formatComputed(finalPrice)}"

    BoxWithConstraints(
        modifier = Modifier
            .fillMaxSize()
            .background(AppBackgroundBrush)
            .statusBarsPadding()
            .navigationBarsPadding()
    ) {
        val compactSide = if (maxWidth < maxHeight) maxWidth else maxHeight
        val outerPadding = (compactSide * 0.05f).coerceIn(16.dp, 24.dp)
        val topGap = (compactSide * 0.04f).coerceIn(12.dp, 20.dp)
        val keypadGap = (compactSide * 0.022f).coerceIn(8.dp, 12.dp)
        val numberButtonSize = if (compactSide < 360.dp) 74.dp else 86.dp
        val sideButtonWidth = numberButtonSize
        val titleSize = if (compactSide < 360.dp) 18.sp else 21.sp
        val unitLabelSize = if (compactSide < 360.dp) 12.sp else 14.sp
        val valueSize = if (compactSide < 360.dp) 22.sp else 26.sp

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(outerPadding)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                UtilityIconButton(
                    onClick = onBack,
                    height = 34.dp,
                    width = 40.dp,
                    contentColor = WarmText,
                    imageVector = Icons.AutoMirrored.Outlined.ArrowBack,
                    contentDescription = "Back"
                )
                Spacer(modifier = Modifier.width(14.dp))
                Text(
                    text = "Discount calculator",
                    color = WarmText,
                    fontSize = titleSize,
                    fontWeight = FontWeight.Normal,
                    fontFamily = calculatorFont
                )
            }

            Spacer(modifier = Modifier.height(topGap))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                MileageInputCard(
                    title = "Price",
                    value = formatNumber(priceValue),
                    isActive = activeField == "price",
                    onClick = {
                        activeField = "price"
                        shouldReplaceOnNextInput = true
                    },
                    calculatorFont = calculatorFont,
                    displayFont = displayFont,
                    unitLabelSize = unitLabelSize,
                    valueSize = valueSize,
                    height = 106.dp,
                    modifier = Modifier.weight(1f)
                )
                MileageInputCard(
                    title = "Discount (%)",
                    value = formatNumber(discountValue),
                    isActive = activeField == "discount",
                    onClick = {
                        activeField = "discount"
                        shouldReplaceOnNextInput = true
                    },
                    calculatorFont = calculatorFont,
                    displayFont = displayFont,
                    unitLabelSize = unitLabelSize,
                    valueSize = valueSize,
                    height = 106.dp,
                    modifier = Modifier.weight(1f)
                )
            }

            Spacer(modifier = Modifier.height(10.dp))

            MileageOutputCard(
                title = "Final price",
                value = formatComputed(finalPrice),
                calculatorFont = calculatorFont,
                displayFont = displayFont,
                unitLabelSize = unitLabelSize,
                valueSize = valueSize,
                height = 110.dp,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(12.dp))

            Text(
                text = "Final price = Price - (Price × Discount ÷ 100)",
                color = SoftText.copy(alpha = 0.72f),
                fontSize = if (compactSide < 360.dp) 12.sp else 14.sp,
                fontWeight = FontWeight.Medium,
                fontFamily = calculatorFont
            )
            Spacer(modifier = Modifier.height(6.dp))
            Text(
                text = explanation,
                color = WarmText,
                fontSize = if (compactSide < 360.dp) 13.sp else 15.sp,
                fontWeight = FontWeight.Normal,
                fontFamily = calculatorFont
            )

            Spacer(modifier = Modifier.weight(1f))

            Spacer(modifier = Modifier.height(18.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(keypadGap)
            ) {
                Column(
                    modifier = Modifier.weight(1f),
                    verticalArrangement = Arrangement.spacedBy(keypadGap)
                ) {
                    ConversionKeypadRow(
                        labels = listOf("7", "8", "9"),
                        buttonWidth = numberButtonSize,
                        buttonHeight = numberButtonSize,
                        buttonGap = keypadGap,
                        calculatorFont = calculatorFont,
                        onTap = ::appendToken
                    )
                    ConversionKeypadRow(
                        labels = listOf("4", "5", "6"),
                        buttonWidth = numberButtonSize,
                        buttonHeight = numberButtonSize,
                        buttonGap = keypadGap,
                        calculatorFont = calculatorFont,
                        onTap = ::appendToken
                    )
                    ConversionKeypadRow(
                        labels = listOf("1", "2", "3"),
                        buttonWidth = numberButtonSize,
                        buttonHeight = numberButtonSize,
                        buttonGap = keypadGap,
                        calculatorFont = calculatorFont,
                        onTap = ::appendToken
                    )
                    ConversionKeypadRow(
                        labels = listOf("00", "0", "."),
                        buttonWidth = numberButtonSize,
                        buttonHeight = numberButtonSize,
                        buttonGap = keypadGap,
                        calculatorFont = calculatorFont,
                        onTap = { token ->
                            if (token == ".") appendDecimal() else appendToken(token)
                        }
                    )
                }

                Column(
                    modifier = Modifier.width(sideButtonWidth),
                    verticalArrangement = Arrangement.spacedBy(keypadGap)
                ) {
                    ConversionTallButton(
                        label = "AC",
                        onClick = ::clearAll,
                        height = numberButtonSize * 2 + keypadGap,
                        width = sideButtonWidth,
                        calculatorFont = calculatorFont,
                        textColor = DangerText,
                        brush = SpecialBrush,
                        borderColor = DangerBorder
                    )
                    ConversionTallButton(
                        label = "\u232B",
                        onClick = ::deleteLast,
                        height = numberButtonSize * 2 + keypadGap,
                        width = sideButtonWidth,
                        calculatorFont = calculatorFont,
                        textColor = WarmText,
                        brush = UtilityBrush,
                        borderColor = SubtleBorder
                    )
                }
            }
            Spacer(modifier = Modifier.height(8.dp))
        }
    }
}

@Composable
private fun AgeCalculatorScreen(
    onBack: () -> Unit,
    calculatorFont: FontFamily,
    displayFont: FontFamily,
    initialDobMillis: Long? = null,
    initialCurrentMillis: Long? = null,
    onLogUpdate: (String, String, String) -> Unit = { _, _, _ -> }
) {
    val context = LocalContext.current
    val dateFormatter = remember { SimpleDateFormat("dd MMM yyyy", Locale.getDefault()) }
    val today = remember {
        Calendar.getInstance().apply {
            set(Calendar.HOUR_OF_DAY, 0)
            set(Calendar.MINUTE, 0)
            set(Calendar.SECOND, 0)
            set(Calendar.MILLISECOND, 0)
        }
    }
    var dobCalendar by remember(initialDobMillis) {
        mutableStateOf(
            (initialDobMillis?.let {
                Calendar.getInstance().apply {
                    timeInMillis = it
                    set(Calendar.HOUR_OF_DAY, 0)
                    set(Calendar.MINUTE, 0)
                    set(Calendar.SECOND, 0)
                    set(Calendar.MILLISECOND, 0)
                }
            } ?: Calendar.getInstance().apply {
                add(Calendar.YEAR, -25)
                set(Calendar.HOUR_OF_DAY, 0)
                set(Calendar.MINUTE, 0)
                set(Calendar.SECOND, 0)
                set(Calendar.MILLISECOND, 0)
            })
        )
    }
    var currentCalendar by remember(initialCurrentMillis) {
        mutableStateOf(
            initialCurrentMillis?.let {
                Calendar.getInstance().apply {
                    timeInMillis = it
                    set(Calendar.HOUR_OF_DAY, 0)
                    set(Calendar.MINUTE, 0)
                    set(Calendar.SECOND, 0)
                    set(Calendar.MILLISECOND, 0)
                }
            } ?: (today.clone() as Calendar)
        )
    }

    fun openDatePicker(
        current: Calendar,
        onSelect: (Calendar) -> Unit
    ) {
        DatePickerDialog(
            context,
            { _, year, month, dayOfMonth ->
                val updated = Calendar.getInstance().apply {
                    set(year, month, dayOfMonth, 0, 0, 0)
                    set(Calendar.MILLISECOND, 0)
                }
                onSelect(updated)
            },
            current.get(Calendar.YEAR),
            current.get(Calendar.MONTH),
            current.get(Calendar.DAY_OF_MONTH)
        ).show()
    }

    fun calculateAgeSummary(dob: Calendar, current: Calendar): String {
        if (dob.after(current)) return "Invalid date range"

        val start = dob.clone() as Calendar
        val end = current.clone() as Calendar

        var years = end.get(Calendar.YEAR) - start.get(Calendar.YEAR)
        var months = end.get(Calendar.MONTH) - start.get(Calendar.MONTH)
        var days = end.get(Calendar.DAY_OF_MONTH) - start.get(Calendar.DAY_OF_MONTH)

        if (days < 0) {
            end.add(Calendar.MONTH, -1)
            days += end.getActualMaximum(Calendar.DAY_OF_MONTH)
            end.add(Calendar.MONTH, 1)
            months -= 1
        }

        if (months < 0) {
            months += 12
            years -= 1
        }

        return "$years years, $months months, $days days"
    }

    fun recordHistory(updatedDob: Calendar = dobCalendar, updatedCurrent: Calendar = currentCalendar) {
        val summary = "${dateFormatter.format(updatedDob.time)} → ${dateFormatter.format(updatedCurrent.time)} = ${calculateAgeSummary(updatedDob, updatedCurrent)}"
        val note = "Age • date of birth to current date"
        val payload = encodeHistoryPayload("age", updatedDob.timeInMillis.toString(), updatedCurrent.timeInMillis.toString())
        onLogUpdate(summary, note, payload)
    }

    BoxWithConstraints(
        modifier = Modifier
            .fillMaxSize()
            .background(AppBackgroundBrush)
            .statusBarsPadding()
            .navigationBarsPadding()
    ) {
        val compactSide = if (maxWidth < maxHeight) maxWidth else maxHeight
        val outerPadding = (compactSide * 0.05f).coerceIn(16.dp, 24.dp)
        val topGap = (compactSide * 0.04f).coerceIn(12.dp, 20.dp)
        val titleSize = if (compactSide < 360.dp) 18.sp else 21.sp
        val unitLabelSize = if (compactSide < 360.dp) 12.sp else 14.sp
        val valueSize = if (compactSide < 360.dp) 18.sp else 21.sp
        val cardHeight = if (compactSide < 360.dp) 106.dp else 116.dp
        val ageSummary = calculateAgeSummary(dobCalendar, currentCalendar)

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(outerPadding)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                UtilityIconButton(
                    onClick = onBack,
                    height = 34.dp,
                    width = 40.dp,
                    contentColor = WarmText,
                    imageVector = Icons.AutoMirrored.Outlined.ArrowBack,
                    contentDescription = "Back"
                )
                Spacer(modifier = Modifier.width(14.dp))
                Text(
                    text = "Age calculator",
                    color = WarmText,
                    fontSize = titleSize,
                    fontWeight = FontWeight.Normal,
                    fontFamily = calculatorFont
                )
            }

            Spacer(modifier = Modifier.height(topGap))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                AgeInputCard(
                    title = "Date of birth",
                    value = dateFormatter.format(dobCalendar.time),
                    onClick = {
                        openDatePicker(dobCalendar) {
                            dobCalendar = it
                            recordHistory(updatedDob = it)
                        }
                    },
                    calculatorFont = calculatorFont,
                    displayFont = displayFont,
                    unitLabelSize = unitLabelSize,
                    valueSize = valueSize,
                    height = cardHeight,
                    modifier = Modifier.weight(1f)
                )
                AgeInputCard(
                    title = "Current date",
                    value = dateFormatter.format(currentCalendar.time),
                    onClick = {
                        openDatePicker(currentCalendar) {
                            currentCalendar = it
                            recordHistory(updatedCurrent = it)
                        }
                    },
                    calculatorFont = calculatorFont,
                    displayFont = displayFont,
                    unitLabelSize = unitLabelSize,
                    valueSize = valueSize,
                    height = cardHeight,
                    modifier = Modifier.weight(1f)
                )
            }

            Spacer(modifier = Modifier.height(10.dp))

            AgeOutputCard(
                title = "Age",
                value = ageSummary,
                calculatorFont = calculatorFont,
                displayFont = displayFont,
                unitLabelSize = unitLabelSize,
                valueSize = valueSize,
                height = cardHeight,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.weight(1f))
        }
    }
}

@Composable
private fun AgeInputCard(
    modifier: Modifier = Modifier,
    height: Dp,
    title: String,
    value: String,
    onClick: () -> Unit,
    calculatorFont: FontFamily,
    displayFont: FontFamily,
    unitLabelSize: TextUnit,
    valueSize: TextUnit
) {
    Surface(
        modifier = modifier
            .fillMaxWidth()
            .height(height)
            .combinedClickable(onClick = onClick),
        shape = RoundedCornerShape(24.dp),
        color = Color.Transparent,
        border = BorderStroke(1.dp, SubtleBorder.copy(alpha = 0.72f))
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(PanelBrush, RoundedCornerShape(24.dp))
                .padding(horizontal = 16.dp, vertical = 16.dp)
        ) {
            Text(
                text = title,
                color = SoftText.copy(alpha = 0.72f),
                fontSize = unitLabelSize,
                fontWeight = FontWeight.Medium,
                fontFamily = calculatorFont
            )
            Spacer(modifier = Modifier.height(10.dp))
            Text(
                text = value,
                modifier = Modifier.fillMaxWidth(),
                color = WarmText,
                fontSize = valueSize,
                lineHeight = valueSize * 1.15f,
                fontWeight = FontWeight.Normal,
                fontFamily = calculatorFont,
                maxLines = 2,
                textAlign = TextAlign.Center
            )
        }
    }
}

@Composable
private fun AgeOutputCard(
    modifier: Modifier = Modifier,
    height: Dp,
    title: String,
    value: String,
    calculatorFont: FontFamily,
    displayFont: FontFamily,
    unitLabelSize: TextUnit,
    valueSize: TextUnit
) {
    Surface(
        modifier = modifier
            .fillMaxWidth()
            .height(height),
        shape = RoundedCornerShape(24.dp),
        color = Color.Transparent,
        border = BorderStroke(1.dp, SubtleBorder.copy(alpha = 0.72f))
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(PanelBrush, RoundedCornerShape(24.dp))
                .padding(horizontal = 16.dp, vertical = 16.dp)
        ) {
            Text(
                text = title,
                color = SoftText.copy(alpha = 0.72f),
                fontSize = unitLabelSize,
                fontWeight = FontWeight.Medium,
                fontFamily = calculatorFont
            )
            Spacer(modifier = Modifier.height(10.dp))
            Text(
                text = value,
                color = WarmText,
                fontSize = valueSize,
                lineHeight = valueSize * 1.15f,
                fontWeight = FontWeight.Normal,
                fontFamily = calculatorFont,
                maxLines = 2
            )
        }
    }
}

@Composable
private fun BMICalculatorScreen(
    onBack: () -> Unit,
    calculatorFont: FontFamily,
    displayFont: FontFamily,
    formatNumber: (String) -> String,
    initialWeightValue: String = "0",
    initialHeightValue: String = "0",
    initialWeightUnitIndex: Int = 0,
    initialHeightUnitIndex: Int = 0,
    initialActiveField: String = "weight",
    onLogReset: () -> Unit = {},
    onLogUpdate: (String, String, String) -> Unit = { _, _, _ -> }
) {
    val weightUnits = listOf("kg", "lb")
    val heightUnits = listOf("cm", "ft.in")
    var weightValue by remember(initialWeightValue) { mutableStateOf(initialWeightValue) }
    var heightValue by remember(initialHeightValue) { mutableStateOf(initialHeightValue) }
    var weightUnitIndex by remember(initialWeightUnitIndex) { mutableStateOf(initialWeightUnitIndex) }
    var heightUnitIndex by remember(initialHeightUnitIndex) { mutableStateOf(initialHeightUnitIndex) }
    var activeField by remember(initialActiveField) { mutableStateOf(initialActiveField) }
    var expandedMenu by remember { mutableStateOf<String?>(null) }
    var shouldReplaceOnNextInput by remember(initialActiveField) { mutableStateOf(false) }

    fun activeText(): String = if (activeField == "weight") weightValue else heightValue

    fun setActiveText(value: String) {
        if (activeField == "weight") {
            weightValue = value
        } else {
            heightValue = value
        }
    }

    fun parseDecimal(raw: String): Double {
        val cleaned = raw.removePrefix("+").trim()
        val parseable = if (cleaned.endsWith(".")) cleaned.dropLast(1) else cleaned
        return parseable.toDoubleOrNull() ?: 0.0
    }

    fun weightInKg(): Double {
        val value = parseDecimal(weightValue)
        return if (weightUnitIndex == 0) value else value * 0.45359237
    }

    fun heightInMetres(): Double {
        val raw = heightValue.trim()
        if (heightUnitIndex == 0) {
            return parseDecimal(raw) / 100.0
        }

        if (raw.isBlank()) return 0.0
        val parts = raw.split(".", limit = 2)
        val feet = parts.getOrNull(0)?.toIntOrNull() ?: 0
        val inches = parts.getOrNull(1)?.toIntOrNull() ?: 0
        val totalInches = feet * 12 + inches.coerceIn(0, 11)
        return totalInches * 0.0254
    }

    fun formatComputed(value: Double): String {
        if (!value.isFinite()) return "0"
        val rounded = BigDecimal.valueOf(value)
            .setScale(2, RoundingMode.HALF_UP)
            .stripTrailingZeros()
            .toPlainString()
        return formatNumber(rounded)
    }

    fun bmiCategory(value: Double): String {
        return when {
            value == 0.0 -> "Add values to calculate"
            value < 18.5 -> "Underweight"
            value < 25.0 -> "Healthy"
            value < 30.0 -> "Overweight"
            else -> "Obese"
        }
    }

    val bmiValue = run {
        val kg = weightInKg()
        val metres = heightInMetres()
        if (kg <= 0.0 || metres <= 0.0) 0.0 else kg / (metres * metres)
    }

    fun recordHistory(
        updatedWeight: String = weightValue,
        updatedHeight: String = heightValue,
        updatedWeightUnit: Int = weightUnitIndex,
        updatedHeightUnit: Int = heightUnitIndex,
        updatedField: String = activeField
    ) {
        val summary = "${formatNumber(updatedWeight)} ${weightUnits[updatedWeightUnit]}, ${formatNumber(updatedHeight)} ${heightUnits[updatedHeightUnit]} = BMI ${formatComputed(bmiValue)}"
        val note = bmiCategory(bmiValue)
        val payload = encodeHistoryPayload(
            "bmi",
            updatedWeight,
            updatedHeight,
            updatedWeightUnit.toString(),
            updatedHeightUnit.toString(),
            updatedField
        )
        onLogUpdate(summary, note, payload)
    }

    fun appendToken(token: String) {
        val current = activeText()
        val updated = when {
                shouldReplaceOnNextInput && token == "00" -> "0"
                shouldReplaceOnNextInput -> token
                current == "0" && token == "00" -> "0"
                current == "0" -> token
                else -> current + token
            }
        setActiveText(updated)
        shouldReplaceOnNextInput = false
        if (activeField == "weight") recordHistory(updatedWeight = updated) else recordHistory(updatedHeight = updated)
    }

    fun appendDecimal() {
        val current = activeText()
        if (shouldReplaceOnNextInput) {
            val updated = "0."
            setActiveText(updated)
            shouldReplaceOnNextInput = false
            if (activeField == "weight") recordHistory(updatedWeight = updated) else recordHistory(updatedHeight = updated)
        } else if (!current.contains(".")) {
            val updated = "$current."
            setActiveText(updated)
            if (activeField == "weight") recordHistory(updatedWeight = updated) else recordHistory(updatedHeight = updated)
        }
    }

    fun deleteLast() {
        val current = activeText()
        val updated = when {
                current.length <= 1 -> "0"
                else -> current.dropLast(1)
            }
        setActiveText(updated)
        if (activeField == "weight") recordHistory(updatedWeight = updated) else recordHistory(updatedHeight = updated)
    }

    fun clearAll() {
        weightValue = "0"
        heightValue = "0"
        shouldReplaceOnNextInput = false
        onLogReset()
    }

    BoxWithConstraints(
        modifier = Modifier
            .fillMaxSize()
            .background(AppBackgroundBrush)
            .statusBarsPadding()
            .navigationBarsPadding()
    ) {
        val compactSide = if (maxWidth < maxHeight) maxWidth else maxHeight
        val outerPadding = (compactSide * 0.05f).coerceIn(16.dp, 24.dp)
        val topGap = (compactSide * 0.04f).coerceIn(12.dp, 20.dp)
        val keypadGap = (compactSide * 0.022f).coerceIn(8.dp, 12.dp)
        val numberButtonSize = if (compactSide < 360.dp) 74.dp else 86.dp
        val sideButtonWidth = numberButtonSize
        val titleSize = if (compactSide < 360.dp) 18.sp else 21.sp
        val unitLabelSize = if (compactSide < 360.dp) 12.sp else 14.sp
        val valueSize = if (compactSide < 360.dp) 22.sp else 26.sp

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(outerPadding)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                UtilityIconButton(
                    onClick = onBack,
                    height = 34.dp,
                    width = 40.dp,
                    contentColor = WarmText,
                    imageVector = Icons.AutoMirrored.Outlined.ArrowBack,
                    contentDescription = "Back"
                )
                Spacer(modifier = Modifier.width(14.dp))
                Text(
                    text = "BMI calculator",
                    color = WarmText,
                    fontSize = titleSize,
                    fontWeight = FontWeight.Normal,
                    fontFamily = calculatorFont
                )
            }

            Spacer(modifier = Modifier.height(topGap))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                UnitEditCard(
                    title = "Weight",
                    unitLabel = weightUnits[weightUnitIndex],
                    value = formatNumber(weightValue),
                    isActive = activeField == "weight",
                    onActivate = {
                        activeField = "weight"
                        shouldReplaceOnNextInput = true
                    },
                    onOpenMenu = { expandedMenu = "weight" },
                    options = weightUnits,
                    expanded = expandedMenu == "weight",
                    onDismiss = { expandedMenu = null },
                    onSelectOption = {
                        weightUnitIndex = it
                        activeField = "weight"
                        shouldReplaceOnNextInput = true
                        expandedMenu = null
                        recordHistory(updatedWeightUnit = it)
                    },
                    calculatorFont = calculatorFont,
                    unitLabelSize = unitLabelSize,
                    valueSize = valueSize,
                    modifier = Modifier.weight(1f)
                )
                UnitEditCard(
                    title = "Height",
                    unitLabel = heightUnits[heightUnitIndex],
                    value = formatNumber(heightValue),
                    isActive = activeField == "height",
                    onActivate = {
                        activeField = "height"
                        shouldReplaceOnNextInput = true
                    },
                    onOpenMenu = { expandedMenu = "height" },
                    options = heightUnits,
                    expanded = expandedMenu == "height",
                    onDismiss = { expandedMenu = null },
                    onSelectOption = {
                        heightUnitIndex = it
                        activeField = "height"
                        shouldReplaceOnNextInput = true
                        expandedMenu = null
                        recordHistory(updatedHeightUnit = it)
                    },
                    calculatorFont = calculatorFont,
                    unitLabelSize = unitLabelSize,
                    valueSize = valueSize,
                    modifier = Modifier.weight(1f)
                )
            }

            Spacer(modifier = Modifier.height(10.dp))

            BMIOutputCard(
                title = "BMI",
                value = formatComputed(bmiValue),
                subtitle = bmiCategory(bmiValue),
                calculatorFont = calculatorFont,
                unitLabelSize = unitLabelSize,
                valueSize = valueSize,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(10.dp))

            BMIPlainTextNote(
                calculatorFont = calculatorFont,
                unitLabelSize = unitLabelSize
            )

            Spacer(modifier = Modifier.weight(1f))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(keypadGap)
            ) {
                Column(
                    modifier = Modifier.weight(1f),
                    verticalArrangement = Arrangement.spacedBy(keypadGap)
                ) {
                    ConversionKeypadRow(
                        labels = listOf("7", "8", "9"),
                        buttonWidth = numberButtonSize,
                        buttonHeight = numberButtonSize,
                        buttonGap = keypadGap,
                        calculatorFont = calculatorFont,
                        onTap = ::appendToken
                    )
                    ConversionKeypadRow(
                        labels = listOf("4", "5", "6"),
                        buttonWidth = numberButtonSize,
                        buttonHeight = numberButtonSize,
                        buttonGap = keypadGap,
                        calculatorFont = calculatorFont,
                        onTap = ::appendToken
                    )
                    ConversionKeypadRow(
                        labels = listOf("1", "2", "3"),
                        buttonWidth = numberButtonSize,
                        buttonHeight = numberButtonSize,
                        buttonGap = keypadGap,
                        calculatorFont = calculatorFont,
                        onTap = ::appendToken
                    )
                    ConversionKeypadRow(
                        labels = listOf("00", "0", "."),
                        buttonWidth = numberButtonSize,
                        buttonHeight = numberButtonSize,
                        buttonGap = keypadGap,
                        calculatorFont = calculatorFont,
                        onTap = { token ->
                            if (token == ".") appendDecimal() else appendToken(token)
                        }
                    )
                }

                Column(
                    modifier = Modifier.width(sideButtonWidth),
                    verticalArrangement = Arrangement.spacedBy(keypadGap)
                ) {
                    ConversionTallButton(
                        label = "AC",
                        onClick = ::clearAll,
                        height = numberButtonSize * 2 + keypadGap,
                        width = sideButtonWidth,
                        calculatorFont = calculatorFont,
                        textColor = DangerText,
                        brush = SpecialBrush,
                        borderColor = DangerBorder
                    )
                    ConversionTallButton(
                        label = "\u232B",
                        onClick = ::deleteLast,
                        height = numberButtonSize * 2 + keypadGap,
                        width = sideButtonWidth,
                        calculatorFont = calculatorFont,
                        textColor = WarmText,
                        brush = UtilityBrush,
                        borderColor = SubtleBorder
                    )
                }
            }
        }
    }
}

@Composable
private fun BMIPlainTextNote(
    calculatorFont: FontFamily,
    unitLabelSize: TextUnit
) {
    val bmiNotes =
        "Below 18.5 - Underweight, 18.5 - 24.9 - Healthy / Normal Weight, " +
            "25.0 - 29.9 - Overweight, 30.0 - 34.9 - Obesity Class I, " +
            "35.0 - 39.9 - Obesity Class II, 40.0 or higher - Obesity Class III (Severe/Morbid)"

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 4.dp)
    ) {
        Text(
            text = "BMI categories",
            color = SoftText.copy(alpha = 0.72f),
            fontSize = unitLabelSize,
            fontWeight = FontWeight.Medium,
            fontFamily = calculatorFont
        )
        Spacer(modifier = Modifier.height(6.dp))
        Text(
            text = bmiNotes,
            color = SoftText.copy(alpha = 0.92f),
            fontSize = (unitLabelSize.value - 1f).sp,
            fontWeight = FontWeight.Normal,
            fontFamily = calculatorFont,
            lineHeight = ((unitLabelSize.value - 1f).sp) * 1.2f
        )
    }
}

private fun scientificNormalizeOperators(expression: String): String {
    return expression
        .replace("×", "*")
        .replace("÷", "/")
        .replace("π", PI.toString())
        .replace("e", E.toString())
        .replace(" ", "")
}

private fun scientificFactorial(value: Double): Double {
    if (value < 0 || value % 1.0 != 0.0) error("Factorial requires a whole number")
    var result = 1.0
    var current = value.toInt()
    while (current > 1) {
        result *= current.toDouble()
        current--
    }
    return result
}

private class ScientificParser(private val input: String) {
    private var position = 0

    fun parse(): Double {
        val result = parseExpression()
        if (position < input.length) error("Unexpected token")
        return result
    }

    private fun parseExpression(): Double {
        var result = parseTerm()
        while (true) {
            result = when {
                match('+') -> result + parseTerm()
                match('-') -> result - parseTerm()
                else -> return result
            }
        }
    }

    private fun parseTerm(): Double {
        var result = parsePower()
        while (true) {
            result = when {
                match('*') -> result * parsePower()
                match('/') -> result / parsePower()
                match('%') -> result % parsePower()
                else -> return result
            }
        }
    }

    private fun parsePower(): Double {
        var result = parseUnary()
        if (match('^')) {
            result = result.pow(parsePower())
        }
        return result
    }

    private fun parseUnary(): Double {
        return when {
            match('+') -> parseUnary()
            match('-') -> -parseUnary()
            else -> parsePostfix()
        }
    }

    private fun parsePostfix(): Double {
        var result = parsePrimary()
        while (true) {
            result = when {
                match('!') -> scientificFactorial(result)
                else -> return result
            }
        }
    }

    private fun parsePrimary(): Double {
        if (match('(')) {
            val result = parseExpression()
            require(match(')')) { "Missing closing bracket" }
            return result
        }

        val start = position
        while (position < input.length && (input[position].isDigit() || input[position] == '.')) {
            position++
        }
        if (start == position) error("Expected number")
        return input.substring(start, position).toDouble()
    }

    private fun match(expected: Char): Boolean {
        if (position < input.length && input[position] == expected) {
            position++
            return true
        }
        return false
    }
}

private fun evaluateScientificExpression(expression: String): Double {
    val normalized = scientificNormalizeOperators(expression)
    require(normalized.isNotBlank()) { "Empty expression" }
    return ScientificParser(normalized).parse()
}

@Composable
private fun ScientificCalculatorScreen(
    onBack: () -> Unit,
    calculatorFont: FontFamily,
    displayFont: FontFamily,
    formatNumber: (String) -> String
) {
    var expression by remember { mutableStateOf("") }
    var resultText by remember { mutableStateOf("0") }
    var justEvaluated by remember { mutableStateOf(false) }

    fun updatePreview() {
        resultText = try {
            formatNumber(
                BigDecimal.valueOf(evaluateScientificExpression(expression))
                    .setScale(10, RoundingMode.HALF_UP)
                    .stripTrailingZeros()
                    .toPlainString()
            )
        } catch (_: Exception) {
            if (expression.isBlank()) "0" else resultText
        }
    }

    fun clearAll() {
        expression = ""
        resultText = "0"
        justEvaluated = false
    }

    fun appendToken(token: String) {
        val operators = setOf("+", "-", "×", "÷", "^", "%")
        expression = when {
            justEvaluated && token.firstOrNull()?.isDigit() == true -> token
            justEvaluated && token == "." -> "0."
            justEvaluated && token in operators -> resultText.replace(",", "") + token
            justEvaluated -> expression
            expression.isEmpty() && token in operators && token != "-" -> expression
            else -> expression + token
        }
        justEvaluated = false
        updatePreview()
    }

    fun deleteLast() {
        expression = expression.dropLast(1)
        justEvaluated = false
        updatePreview()
    }

    fun applyUnary(transform: (Double) -> Double) {
        val source = if (expression.isBlank()) resultText else expression
        val evaluated = runCatching { evaluateScientificExpression(source.replace(",", "")) }.getOrNull() ?: return
        val transformed = runCatching { transform(evaluated) }.getOrNull() ?: return
        val plain = BigDecimal.valueOf(transformed)
            .setScale(10, RoundingMode.HALF_UP)
            .stripTrailingZeros()
            .toPlainString()
        expression = plain
        resultText = formatNumber(plain)
        justEvaluated = true
    }

    fun evaluateNow() {
        if (expression.isBlank()) return
        val evaluated = runCatching { evaluateScientificExpression(expression) }.getOrNull() ?: run {
            resultText = "Error"
            return
        }
        val plain = BigDecimal.valueOf(evaluated)
            .setScale(10, RoundingMode.HALF_UP)
            .stripTrailingZeros()
            .toPlainString()
        resultText = formatNumber(plain)
        expression = plain
        justEvaluated = true
    }

    BoxWithConstraints(
        modifier = Modifier
            .fillMaxSize()
            .background(AppBackgroundBrush)
            .statusBarsPadding()
            .navigationBarsPadding()
    ) {
        val compactSide = if (maxWidth < maxHeight) maxWidth else maxHeight
        val outerPadding = (compactSide * 0.05f).coerceIn(16.dp, 24.dp)
        val keypadGap = (compactSide * 0.018f).coerceIn(6.dp, 10.dp)
        val buttonHeight = if (compactSide < 360.dp) 54.dp else 62.dp
        val buttonTextSize = if (compactSide < 360.dp) 15.sp else 18.sp

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(outerPadding)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                UtilityIconButton(
                    onClick = onBack,
                    height = 34.dp,
                    width = 40.dp,
                    contentColor = WarmText,
                    imageVector = Icons.AutoMirrored.Outlined.ArrowBack,
                    contentDescription = "Back"
                )
                Spacer(modifier = Modifier.width(14.dp))
                Text(
                    text = "Scientific calculator",
                    color = WarmText,
                    fontSize = if (compactSide < 360.dp) 20.sp else 24.sp,
                    fontWeight = FontWeight.Normal,
                    fontFamily = calculatorFont
                )
            }

            Spacer(modifier = Modifier.height(18.dp))

            Surface(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(24.dp),
                color = Color.Transparent,
                border = BorderStroke(1.dp, SubtleBorder.copy(alpha = 0.72f))
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(PanelBrush, RoundedCornerShape(24.dp))
                        .padding(horizontal = 18.dp, vertical = 18.dp),
                    horizontalAlignment = Alignment.End
                ) {
                    Text(
                        text = if (expression.isBlank()) "0" else expression,
                        color = SoftText.copy(alpha = 0.72f),
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Normal,
                        fontFamily = calculatorFont,
                        textAlign = TextAlign.End,
                        modifier = Modifier.fillMaxWidth(),
                        maxLines = 2
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = resultText,
                        color = WarmText,
                        fontSize = 32.sp,
                        fontWeight = FontWeight.Light,
                        fontFamily = displayFont,
                        textAlign = TextAlign.End,
                        modifier = Modifier.fillMaxWidth(),
                        maxLines = 2
                    )
                }
            }

            Spacer(modifier = Modifier.weight(1f))

            val rows = listOf(
                listOf("sin", "cos", "tan", "ln", "log"),
                listOf("(", ")", "π", "e", "C"),
                listOf("x²", "√", "xʸ", "1/x", "DEL"),
                listOf("7", "8", "9", "÷", "%"),
                listOf("4", "5", "6", "×", "!"),
                listOf("1", "2", "3", "-", "+"),
                listOf("+/-", "0", ".", "=", "")
            )

            rows.forEach { row ->
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(keypadGap)
                ) {
                    row.forEach { label ->
                        if (label.isEmpty()) {
                            Spacer(modifier = Modifier.weight(1f))
                        } else {
                            val type = when (label) {
                                "C" -> ButtonType.SPECIAL
                                "=", -> ButtonType.EQUALS
                                "÷", "×", "-", "+", "%", "xʸ" -> ButtonType.OPERATOR
                                else -> ButtonType.UTILITY
                            }
                            CalculatorButton(
                                text = label,
                                type = type,
                                modifier = Modifier.weight(1f),
                                onClick = {
                                    when (label) {
                                        "C" -> clearAll()
                                        "DEL" -> deleteLast()
                                        "=" -> evaluateNow()
                                        "π" -> appendToken("π")
                                        "e" -> appendToken("e")
                                        "÷" -> appendToken("÷")
                                        "×" -> appendToken("×")
                                        "xʸ" -> appendToken("^")
                                        "+", "-", "%", "(", ")", ".", "0", "1", "2", "3", "4", "5", "6", "7", "8", "9" -> appendToken(label)
                                        "sin" -> applyUnary { sin(Math.toRadians(it)) }
                                        "cos" -> applyUnary { cos(Math.toRadians(it)) }
                                        "tan" -> applyUnary { tan(Math.toRadians(it)) }
                                        "ln" -> applyUnary { ln(it) }
                                        "log" -> applyUnary { log10(it) }
                                        "√" -> applyUnary { sqrt(it) }
                                        "x²" -> applyUnary { it * it }
                                        "1/x" -> applyUnary { 1.0 / it }
                                        "!" -> applyUnary { scientificFactorial(it) }
                                        "+/-" -> applyUnary { -it }
                                    }
                                },
                                buttonHeight = buttonHeight,
                                textSize = if (label.length > 2) (buttonTextSize.value * 0.78f).sp else buttonTextSize,
                                cornerRadius = (buttonHeight * 0.26f).coerceIn(16.dp, 24.dp),
                                fontFamily = calculatorFont
                            )
                        }
                    }
                }
                Spacer(modifier = Modifier.height(keypadGap))
            }
        }
    }
}

@Composable
private fun ScientificCalculatorScreenClean(
    onBack: () -> Unit,
    calculatorFont: FontFamily,
    displayFont: FontFamily,
    formatNumber: (String) -> String
) {
    var expression by remember { mutableStateOf("") }
    var resultText by remember { mutableStateOf("0") }
    var justEvaluated by remember { mutableStateOf(false) }

    fun updatePreview() {
        resultText = try {
            formatNumber(
                BigDecimal.valueOf(evaluateScientificExpression(expression))
                    .setScale(10, RoundingMode.HALF_UP)
                    .stripTrailingZeros()
                    .toPlainString()
            )
        } catch (_: Exception) {
            if (expression.isBlank()) "0" else resultText
        }
    }

    fun clearAll() {
        expression = ""
        resultText = "0"
        justEvaluated = false
    }

    fun appendToken(token: String) {
        val operators = setOf("+", "-", "×", "÷", "^", "%")
        expression = when {
            justEvaluated && token.firstOrNull()?.isDigit() == true -> token
            justEvaluated && token == "." -> "0."
            justEvaluated && token in operators -> resultText.replace(",", "") + token
            justEvaluated -> expression
            expression.isEmpty() && token in operators && token != "-" -> expression
            else -> expression + token
        }
        justEvaluated = false
        updatePreview()
    }

    fun deleteLast() {
        expression = expression.dropLast(1)
        justEvaluated = false
        updatePreview()
    }

    fun applyUnary(transform: (Double) -> Double) {
        val source = if (expression.isBlank()) resultText else expression
        val evaluated = runCatching { evaluateScientificExpression(source.replace(",", "")) }.getOrNull() ?: return
        val transformed = runCatching { transform(evaluated) }.getOrNull() ?: return
        val plain = BigDecimal.valueOf(transformed)
            .setScale(10, RoundingMode.HALF_UP)
            .stripTrailingZeros()
            .toPlainString()
        expression = plain
        resultText = formatNumber(plain)
        justEvaluated = true
    }

    fun evaluateNow() {
        if (expression.isBlank()) return
        val evaluated = runCatching { evaluateScientificExpression(expression) }.getOrNull() ?: run {
            resultText = "Error"
            return
        }
        val plain = BigDecimal.valueOf(evaluated)
            .setScale(10, RoundingMode.HALF_UP)
            .stripTrailingZeros()
            .toPlainString()
        resultText = formatNumber(plain)
        expression = plain
        justEvaluated = true
    }

    BoxWithConstraints(
        modifier = Modifier
            .fillMaxSize()
            .background(AppBackgroundBrush)
            .statusBarsPadding()
            .navigationBarsPadding()
    ) {
        val compactSide = if (maxWidth < maxHeight) maxWidth else maxHeight
        val outerPadding = (compactSide * 0.05f).coerceIn(16.dp, 24.dp)
        val keypadGap = (compactSide * 0.018f).coerceIn(6.dp, 10.dp)
        val buttonHeight = if (compactSide < 360.dp) 54.dp else 62.dp
        val buttonTextSize = if (compactSide < 360.dp) 15.sp else 18.sp
        val cornerRadius = (buttonHeight * 0.26f).coerceIn(16.dp, 24.dp)
        val scientificKeypadWidth = maxWidth - outerPadding * 2
        val baseScientificButtonWidth = (scientificKeypadWidth - keypadGap * 4) / 5
        val wideScientificButtonWidth = baseScientificButtonWidth * 2 + keypadGap
        val rows = listOf(
            listOf("sin", "cos", "tan", "ln", "log"),
            listOf("(", ")", "π", "e", "!"),
            listOf("x²", "√", "xʸ", "1/x", "%"),
            listOf("7", "8", "9", "÷", "C"),
            listOf("4", "5", "6", "×", "DEL"),
            listOf("1", "2", "3", "-", "+"),
            listOf("+/-", "0", ".", "=", "")
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(outerPadding)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                UtilityIconButton(
                    onClick = onBack,
                    height = 34.dp,
                    width = 40.dp,
                    contentColor = WarmText,
                    imageVector = Icons.AutoMirrored.Outlined.ArrowBack,
                    contentDescription = "Back"
                )
                Spacer(modifier = Modifier.width(14.dp))
                Text(
                    text = "Scientific calculator",
                    color = WarmText,
                    fontSize = if (compactSide < 360.dp) 20.sp else 24.sp,
                    fontWeight = FontWeight.Normal,
                    fontFamily = calculatorFont
                )
            }

            Spacer(modifier = Modifier.height(18.dp))

            Surface(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(24.dp),
                color = Color.Transparent,
                border = BorderStroke(1.dp, SubtleBorder.copy(alpha = 0.72f))
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(PanelBrush, RoundedCornerShape(24.dp))
                        .padding(horizontal = 18.dp, vertical = 18.dp),
                    horizontalAlignment = Alignment.End
                ) {
                    Text(
                        text = if (expression.isBlank()) "0" else expression,
                        color = SoftText.copy(alpha = 0.72f),
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Normal,
                        fontFamily = calculatorFont,
                        textAlign = TextAlign.End,
                        modifier = Modifier.fillMaxWidth(),
                        maxLines = 2
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = resultText,
                        color = WarmText,
                        fontSize = 32.sp,
                        fontWeight = FontWeight.Light,
                        fontFamily = displayFont,
                        textAlign = TextAlign.End,
                        modifier = Modifier.fillMaxWidth(),
                        maxLines = 2
                    )
                }
            }

            Spacer(modifier = Modifier.weight(1f))

            rows.forEach { row ->
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(keypadGap)
                ) {
                    row.forEach { label ->
                        if (label.isEmpty()) {
                            Spacer(modifier = Modifier.weight(1f))
                        } else {
                            val type = when (label) {
                                "C" -> ButtonType.SPECIAL
                                "=" -> ButtonType.EQUALS
                                "÷", "×", "-", "+", "xʸ" -> ButtonType.OPERATOR
                                else -> ButtonType.UTILITY
                            }
                            CalculatorButton(
                                text = label,
                                type = type,
                                modifier = Modifier.weight(1f),
                                onClick = {
                                    when (label) {
                                        "C" -> clearAll()
                                        "DEL" -> deleteLast()
                                        "=" -> evaluateNow()
                                        "π" -> appendToken("π")
                                        "e" -> appendToken("e")
                                        "÷" -> appendToken("÷")
                                        "×" -> appendToken("×")
                                        "xʸ" -> appendToken("^")
                                        "+", "-", "%", "(", ")", ".", "0", "1", "2", "3", "4", "5", "6", "7", "8", "9" -> appendToken(label)
                                        "sin" -> applyUnary { sin(Math.toRadians(it)) }
                                        "cos" -> applyUnary { cos(Math.toRadians(it)) }
                                        "tan" -> applyUnary { tan(Math.toRadians(it)) }
                                        "ln" -> applyUnary { ln(it) }
                                        "log" -> applyUnary { log10(it) }
                                        "√" -> applyUnary { sqrt(it) }
                                        "x²" -> applyUnary { it * it }
                                        "1/x" -> applyUnary { 1.0 / it }
                                        "!" -> applyUnary { scientificFactorial(it) }
                                        "+/-" -> applyUnary { -it }
                                    }
                                },
                                buttonHeight = buttonHeight,
                                textSize = if (label.length > 2) (buttonTextSize.value * 0.78f).sp else buttonTextSize,
                                cornerRadius = cornerRadius,
                                fontFamily = calculatorFont
                            )
                        }
                    }
                }
                Spacer(modifier = Modifier.height(keypadGap))
            }
        }
    }
}

@Composable
private fun ScientificCalculatorScreenCleanV2(
    onBack: () -> Unit,
    calculatorFont: FontFamily,
    displayFont: FontFamily,
    formatNumber: (String) -> String
) {
    data class ScientificKey(val label: String, val type: ButtonType, val weight: Float = 1f)

    var expression by remember { mutableStateOf("") }
    var resultText by remember { mutableStateOf("0") }
    var justEvaluated by remember { mutableStateOf(false) }

    fun updatePreview() {
        resultText = try {
            formatNumber(
                BigDecimal.valueOf(evaluateScientificExpression(expression))
                    .setScale(10, RoundingMode.HALF_UP)
                    .stripTrailingZeros()
                    .toPlainString()
            )
        } catch (_: Exception) {
            if (expression.isBlank()) "0" else resultText
        }
    }

    fun clearAll() {
        expression = ""
        resultText = "0"
        justEvaluated = false
    }

    fun appendToken(token: String) {
        val operators = setOf("+", "-", "×", "÷", "^", "%")
        expression = when {
            justEvaluated && token.firstOrNull()?.isDigit() == true -> token
            justEvaluated && token == "." -> "0."
            justEvaluated && token in operators -> resultText.replace(",", "") + token
            justEvaluated -> expression
            expression.isEmpty() && token in operators && token != "-" -> expression
            else -> expression + token
        }
        justEvaluated = false
        updatePreview()
    }

    fun deleteLast() {
        expression = expression.dropLast(1)
        justEvaluated = false
        updatePreview()
    }

    fun applyUnary(transform: (Double) -> Double) {
        val source = if (expression.isBlank()) resultText else expression
        val evaluated = runCatching { evaluateScientificExpression(source.replace(",", "")) }.getOrNull() ?: return
        val transformed = runCatching { transform(evaluated) }.getOrNull() ?: return
        val plain = BigDecimal.valueOf(transformed)
            .setScale(10, RoundingMode.HALF_UP)
            .stripTrailingZeros()
            .toPlainString()
        expression = plain
        resultText = formatNumber(plain)
        justEvaluated = true
    }

    fun evaluateNow() {
        if (expression.isBlank()) return
        val evaluated = runCatching { evaluateScientificExpression(expression) }.getOrNull() ?: run {
            resultText = "Error"
            return
        }
        val plain = BigDecimal.valueOf(evaluated)
            .setScale(10, RoundingMode.HALF_UP)
            .stripTrailingZeros()
            .toPlainString()
        resultText = formatNumber(plain)
        expression = plain
        justEvaluated = true
    }

    BoxWithConstraints(
        modifier = Modifier
            .fillMaxSize()
            .background(AppBackgroundBrush)
            .statusBarsPadding()
            .navigationBarsPadding()
    ) {
        val compactSide = if (maxWidth < maxHeight) maxWidth else maxHeight
        val outerPadding = (compactSide * 0.05f).coerceIn(16.dp, 24.dp)
        val keypadGap = (compactSide * 0.018f).coerceIn(6.dp, 10.dp)
        val buttonHeight = if (compactSide < 360.dp) 54.dp else 62.dp
        val buttonTextSize = if (compactSide < 360.dp) 15.sp else 18.sp
        val cornerRadius = (buttonHeight * 0.26f).coerceIn(16.dp, 24.dp)
        val scientificKeypadWidth = maxWidth - outerPadding * 2
        val baseScientificButtonWidth = (scientificKeypadWidth - keypadGap * 4) / 5
        val wideScientificButtonWidth = baseScientificButtonWidth * 2 + keypadGap
        val rows = listOf(
            listOf(
                ScientificKey("sin", ButtonType.UTILITY),
                ScientificKey("cos", ButtonType.UTILITY),
                ScientificKey("tan", ButtonType.UTILITY),
                ScientificKey("C", ButtonType.SPECIAL),
                ScientificKey("\u232B", ButtonType.UTILITY)
            ),
            listOf(
                ScientificKey("ln", ButtonType.UTILITY),
                ScientificKey("log", ButtonType.UTILITY),
                ScientificKey("(", ButtonType.UTILITY),
                ScientificKey(")", ButtonType.UTILITY),
                ScientificKey("!", ButtonType.UTILITY)
            ),
            listOf(
                ScientificKey("π", ButtonType.UTILITY),
                ScientificKey("e", ButtonType.UTILITY),
                ScientificKey("x²", ButtonType.UTILITY),
                ScientificKey("√", ButtonType.UTILITY),
                ScientificKey("1/x", ButtonType.UTILITY)
            ),
            listOf(
                ScientificKey("7", ButtonType.UTILITY),
                ScientificKey("8", ButtonType.UTILITY),
                ScientificKey("9", ButtonType.UTILITY),
                ScientificKey("+/-", ButtonType.OPERATOR),
                ScientificKey("%", ButtonType.OPERATOR)
            ),
            listOf(
                ScientificKey("4", ButtonType.UTILITY),
                ScientificKey("5", ButtonType.UTILITY),
                ScientificKey("6", ButtonType.UTILITY),
                ScientificKey("÷", ButtonType.OPERATOR),
                ScientificKey("×", ButtonType.OPERATOR)
            ),
            listOf(
                ScientificKey("1", ButtonType.UTILITY),
                ScientificKey("2", ButtonType.UTILITY),
                ScientificKey("3", ButtonType.UTILITY),
                ScientificKey("-", ButtonType.OPERATOR),
                ScientificKey("+", ButtonType.OPERATOR)
            ),
            listOf(
                ScientificKey("00", ButtonType.UTILITY),
                ScientificKey("0", ButtonType.UTILITY),
                ScientificKey(".", ButtonType.UTILITY),
                ScientificKey("=", ButtonType.EQUALS, 2f)
            )
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(outerPadding)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                UtilityIconButton(
                    onClick = onBack,
                    height = 34.dp,
                    width = 40.dp,
                    contentColor = WarmText,
                    imageVector = Icons.AutoMirrored.Outlined.ArrowBack,
                    contentDescription = "Back"
                )
                Spacer(modifier = Modifier.width(14.dp))
                Text(
                    text = "Scientific calculator",
                    color = WarmText,
                    fontSize = if (compactSide < 360.dp) 20.sp else 24.sp,
                    fontWeight = FontWeight.Normal,
                    fontFamily = calculatorFont
                )
            }

            Spacer(modifier = Modifier.height(18.dp))

            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
                    .heightIn(min = 190.dp),
                shape = RoundedCornerShape(24.dp),
                color = Color.Transparent,
                border = BorderStroke(1.dp, SubtleBorder.copy(alpha = 0.72f))
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(PanelBrush, RoundedCornerShape(24.dp))
                        .padding(horizontal = 18.dp, vertical = 18.dp),
                    horizontalAlignment = Alignment.End,
                    verticalArrangement = Arrangement.Bottom
                ) {
                    Text(
                        text = if (expression.isBlank()) "0" else expression,
                        color = SoftText.copy(alpha = 0.72f),
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Normal,
                        fontFamily = calculatorFont,
                        textAlign = TextAlign.End,
                        modifier = Modifier.fillMaxWidth(),
                        maxLines = 10
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = resultText,
                        color = WarmText,
                        fontSize = 32.sp,
                        fontWeight = FontWeight.Light,
                        fontFamily = displayFont,
                        textAlign = TextAlign.End,
                        modifier = Modifier.fillMaxWidth(),
                        maxLines = 4
                    )
                }
            }

            Spacer(modifier = Modifier.height(18.dp))

            rows.forEachIndexed { rowIndex, row ->
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(keypadGap)
                ) {
                    val isBottomRow = rowIndex == rows.lastIndex
                    row.forEach { key ->
                        if (key.label.isEmpty()) {
                            Spacer(
                                modifier = if (isBottomRow) {
                                    Modifier.width(baseScientificButtonWidth)
                                } else {
                                    Modifier.weight(key.weight)
                                }
                            )
                        } else {
                            CalculatorButton(
                                text = key.label,
                                type = key.type,
                                modifier = if (isBottomRow) {
                                    Modifier.width(
                                        if (key.label == "=") wideScientificButtonWidth
                                        else baseScientificButtonWidth
                                    )
                                } else {
                                    Modifier.weight(key.weight)
                                },
                                onClick = {
                                    when (key.label) {
                                        "C" -> clearAll()
                                        "\u232B" -> deleteLast()
                                        "=" -> evaluateNow()
                                        "π" -> appendToken("π")
                                        "e" -> appendToken("e")
                                        "÷" -> appendToken("÷")
                                        "×" -> appendToken("×")
                                        "xʸ" -> appendToken("^")
                                        "+", "-", "%", "(", ")", ".", "0", "1", "2", "3", "4", "5", "6", "7", "8", "9" -> appendToken(key.label)
                                        "sin" -> applyUnary { sin(Math.toRadians(it)) }
                                        "cos" -> applyUnary { cos(Math.toRadians(it)) }
                                        "tan" -> applyUnary { tan(Math.toRadians(it)) }
                                        "ln" -> applyUnary { ln(it) }
                                        "log" -> applyUnary { log10(it) }
                                        "√" -> applyUnary { sqrt(it) }
                                        "x²" -> applyUnary { it * it }
                                        "1/x" -> applyUnary { 1.0 / it }
                                        "!" -> applyUnary { scientificFactorial(it) }
                                        "+/-" -> applyUnary { -it }
                                    }
                                },
                                buttonHeight = buttonHeight,
                                textSize = if (key.label.length > 2) (buttonTextSize.value * 0.78f).sp else buttonTextSize,
                                cornerRadius = cornerRadius,
                                fontFamily = calculatorFont
                            )
                        }
                    }
                }
                Spacer(modifier = Modifier.height(keypadGap))
            }
        }
    }
}

@Composable
private fun UnitEditCard(
    modifier: Modifier = Modifier,
    title: String,
    unitLabel: String,
    value: String,
    isActive: Boolean,
    onActivate: () -> Unit,
    onOpenMenu: () -> Unit,
    options: List<String>,
    expanded: Boolean,
    onDismiss: () -> Unit,
    onSelectOption: (Int) -> Unit,
    calculatorFont: FontFamily,
    unitLabelSize: TextUnit,
    valueSize: TextUnit
) {
    Box(modifier = modifier) {
        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .height(106.dp)
                .combinedClickable(onClick = onActivate),
            shape = RoundedCornerShape(24.dp),
            color = Color.Transparent,
            border = BorderStroke(1.dp, if (isActive) AccentBorder else SubtleBorder.copy(alpha = 0.72f))
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(PanelBrush, RoundedCornerShape(24.dp))
                    .padding(horizontal = 16.dp, vertical = 16.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = "$title ($unitLabel)",
                        color = SoftText.copy(alpha = 0.72f),
                        fontSize = unitLabelSize,
                        fontWeight = FontWeight.Medium,
                        fontFamily = calculatorFont
                    )
                    Box(modifier = Modifier.wrapContentSize(Alignment.TopEnd)) {
                        Icon(
                            imageVector = Icons.Outlined.KeyboardArrowDown,
                            contentDescription = "Select unit",
                            tint = if (expanded) AccentBorder else SoftText.copy(alpha = 0.72f),
                            modifier = Modifier.clickable(onClick = onOpenMenu)
                        )

                        DropdownMenu(
                            expanded = expanded,
                            onDismissRequest = onDismiss,
                            offset = DpOffset(x = 0.dp, y = 6.dp),
                            containerColor = Color(0xFF181A1F),
                            shape = RoundedCornerShape(12.dp),
                            border = BorderStroke(1.dp, SubtleBorder.copy(alpha = 0.9f)),
                            shadowElevation = 10.dp,
                            tonalElevation = 0.dp
                        ) {
                            options.forEachIndexed { index, option ->
                                DropdownMenuItem(
                                    text = {
                                        Text(
                                            text = option,
                                            color = WarmText,
                                            fontFamily = calculatorFont,
                                            fontSize = unitLabelSize,
                                            fontWeight = FontWeight.Normal
                                        )
                                    },
                                    colors = MenuDefaults.itemColors(
                                        textColor = WarmText,
                                        leadingIconColor = WarmText,
                                        trailingIconColor = WarmText,
                                        disabledTextColor = SoftText,
                                        disabledLeadingIconColor = SoftText,
                                        disabledTrailingIconColor = SoftText
                                    ),
                                    onClick = { onSelectOption(index) }
                                )
                            }
                        }
                    }
                }
                Spacer(modifier = Modifier.height(10.dp))
                Text(
                    text = value,
                    color = if (isActive) AccentBorder else WarmText,
                    fontSize = valueSize,
                    lineHeight = valueSize * 1.15f,
                    fontWeight = FontWeight.Normal,
                    fontFamily = calculatorFont,
                    maxLines = 1
                )
            }
        }

    }
}

@Composable
private fun BMIOutputCard(
    modifier: Modifier = Modifier,
    title: String,
    value: String,
    subtitle: String,
    calculatorFont: FontFamily,
    unitLabelSize: TextUnit,
    valueSize: TextUnit
) {
        Surface(
            modifier = modifier
                .fillMaxWidth()
                .height(112.dp),
        shape = RoundedCornerShape(24.dp),
        color = Color.Transparent,
        border = BorderStroke(1.dp, SubtleBorder.copy(alpha = 0.72f))
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(PanelBrush, RoundedCornerShape(24.dp))
                .padding(horizontal = 16.dp, vertical = 16.dp)
        ) {
            Text(
                text = title,
                color = SoftText.copy(alpha = 0.72f),
                fontSize = unitLabelSize,
                fontWeight = FontWeight.Medium,
                fontFamily = calculatorFont
            )
            Spacer(modifier = Modifier.height(10.dp))
            Text(
                text = value,
                color = WarmText,
                fontSize = valueSize,
                lineHeight = valueSize * 1.15f,
                fontWeight = FontWeight.Normal,
                fontFamily = calculatorFont,
                maxLines = 1
            )
            Spacer(modifier = Modifier.height(3.dp))
            Text(
                text = subtitle,
                color = AccentBorder,
                fontSize = unitLabelSize,
                fontWeight = FontWeight.Normal,
                fontFamily = calculatorFont,
                maxLines = 1
            )
        }
    }
}

@Composable
private fun UnitSelectionCard(
    unit: ConversionUnit,
    value: String,
    calculatorFont: FontFamily,
    unitLabelSize: TextUnit,
    valueSize: TextUnit,
    valueColor: Color,
    isActive: Boolean,
    onActivate: () -> Unit,
    onOpenMenu: () -> Unit,
    expanded: Boolean,
    units: List<ConversionUnit>,
    onDismiss: () -> Unit,
    onSelectUnit: (Int) -> Unit
) {
    Box {
        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .combinedClickable(onClick = onActivate),
            shape = RoundedCornerShape(24.dp),
            color = Color.Transparent,
            border = BorderStroke(1.dp, if (isActive) AccentBorder else SubtleBorder.copy(alpha = 0.72f))
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(PanelBrush, RoundedCornerShape(24.dp))
                    .padding(horizontal = 16.dp, vertical = 16.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = "${unit.label} (${unit.symbol})",
                        color = SoftText.copy(alpha = 0.72f),
                        fontSize = unitLabelSize,
                        fontWeight = FontWeight.Medium,
                        fontFamily = calculatorFont
                    )
                    Box(modifier = Modifier.wrapContentSize(Alignment.TopEnd)) {
                        Icon(
                            imageVector = Icons.Outlined.KeyboardArrowDown,
                            contentDescription = "Select unit",
                            tint = if (expanded) AccentBorder else SoftText.copy(alpha = 0.72f),
                            modifier = Modifier.clickable(onClick = onOpenMenu)
                        )

                        DropdownMenu(
                            expanded = expanded,
                            onDismissRequest = onDismiss,
                            offset = DpOffset(x = 0.dp, y = 6.dp),
                            containerColor = Color(0xFF181A1F),
                            shape = RoundedCornerShape(12.dp),
                            border = BorderStroke(1.dp, SubtleBorder.copy(alpha = 0.9f)),
                            shadowElevation = 10.dp,
                            tonalElevation = 0.dp
                        ) {
                            units.forEachIndexed { index, candidate ->
                                DropdownMenuItem(
                                    text = {
                                        Text(
                                            text = "${candidate.label} (${candidate.symbol})",
                                            color = WarmText,
                                            fontFamily = calculatorFont,
                                            fontSize = unitLabelSize,
                                            fontWeight = FontWeight.Normal
                                        )
                                    },
                                    colors = MenuDefaults.itemColors(
                                        textColor = WarmText,
                                        leadingIconColor = WarmText,
                                        trailingIconColor = WarmText,
                                        disabledTextColor = SoftText,
                                        disabledLeadingIconColor = SoftText,
                                        disabledTrailingIconColor = SoftText
                                    ),
                                    onClick = { onSelectUnit(index) }
                                )
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                Text(
                    text = value,
                    color = valueColor,
                    fontSize = valueSize,
                    lineHeight = valueSize * 1.15f,
                    fontWeight = FontWeight.Normal,
                    fontFamily = calculatorFont,
                    maxLines = 2
                )
            }
        }

    }
}

@Composable
private fun ConversionKeypadRow(
    labels: List<String>,
    buttonWidth: Dp,
    buttonHeight: Dp,
    buttonGap: Dp,
    calculatorFont: FontFamily,
    onTap: (String) -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(buttonGap)
    ) {
        labels.forEach { label ->
            ConversionRectButton(
                label = label,
                onClick = { onTap(label) },
                width = buttonWidth,
                height = buttonHeight,
                calculatorFont = calculatorFont
            )
        }
    }
}

@Composable
private fun ConversionRectButton(
    label: String,
    onClick: () -> Unit,
    width: Dp,
    height: Dp,
    calculatorFont: FontFamily
) {
    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()
    val scale by animateFloatAsState(
        targetValue = if (isPressed) 0.95f else 1f,
        animationSpec = spring(stiffness = 700f, dampingRatio = 0.72f),
        label = "conversion_rect_button_scale"
    )
    val cornerRadius = RoundedCornerShape((height * 0.28f).coerceIn(18.dp, 24.dp))

    Button(
        onClick = onClick,
        modifier = Modifier
            .width(width)
            .height(height)
            .graphicsLayer {
                scaleX = scale
                scaleY = scale
            },
        shape = cornerRadius,
        contentPadding = PaddingValues(0.dp),
        interactionSource = interactionSource,
        colors = ButtonDefaults.buttonColors(
            containerColor = Color.Transparent,
            contentColor = WarmText
        )
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(NumberBrush, cornerRadius),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = label,
                color = WarmText,
                fontSize = if (label == "00") 19.sp else 23.sp,
                fontWeight = FontWeight.Medium,
                fontFamily = calculatorFont
            )
        }
    }
}

@Composable
private fun ConversionTallButton(
    label: String,
    onClick: () -> Unit,
    height: Dp,
    width: Dp,
    calculatorFont: FontFamily,
    textColor: Color,
    brush: Brush,
    borderColor: Color
) {
    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()
    val scale by animateFloatAsState(
        targetValue = if (isPressed) 0.97f else 1f,
        animationSpec = spring(stiffness = 700f, dampingRatio = 0.72f),
        label = "conversion_tall_button_scale"
    )
    val resolvedCorner = RoundedCornerShape((width * 0.28f).coerceIn(18.dp, 24.dp))

    Button(
        onClick = onClick,
        modifier = Modifier
            .height(height)
            .width(width)
            .graphicsLayer {
                scaleX = scale
                scaleY = scale
            },
        shape = resolvedCorner,
        contentPadding = PaddingValues(0.dp),
        interactionSource = interactionSource,
        colors = ButtonDefaults.buttonColors(
            containerColor = Color.Transparent,
            contentColor = textColor
        )
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(brush, resolvedCorner)
                .padding(1.dp),
            contentAlignment = Alignment.Center
        ) {
            Surface(
                modifier = Modifier.fillMaxSize(),
                shape = resolvedCorner,
                color = Color.Transparent,
                border = BorderStroke(1.dp, borderColor)
            ) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = label,
                        color = textColor,
                        fontSize = if (label == "AC") 24.sp else 28.sp,
                        fontWeight = FontWeight.Medium,
                        fontFamily = calculatorFont
                    )
                }
            }
        }
    }
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
