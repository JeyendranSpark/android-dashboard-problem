package zuper.dev.android.dashboard.AppUtils

import android.content.Context
import androidx.compose.ui.graphics.Color
import zuper.dev.android.dashboard.R
import zuper.dev.android.dashboard.data.model.InvoiceApiModel
import zuper.dev.android.dashboard.data.model.InvoiceStatus
import zuper.dev.android.dashboard.data.model.JobApiModel
import zuper.dev.android.dashboard.data.model.JobStatus
import zuper.dev.android.dashboard.userInterface.theme.LightBlue
import zuper.dev.android.dashboard.userInterface.theme.LightCyan
import zuper.dev.android.dashboard.userInterface.theme.LightGreen
import zuper.dev.android.dashboard.userInterface.theme.LightRed
import zuper.dev.android.dashboard.userInterface.theme.LightYellow
import java.text.NumberFormat
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.format.TextStyle
import java.util.Calendar
import java.util.Date
import java.util.Locale

object Utility {

    private val dateFormat get() = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
    private val timeFormat get() = SimpleDateFormat("HH:mm a", Locale.getDefault())
    private val dateTimeFormat
        get() = SimpleDateFormat(
            "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'",
            Locale.getDefault()
        )

    fun formatDateTime(context: Context, startTime: String, endTime: String): String {
        val startDate = dateTimeFormat.parse(startTime) ?: Date()
        val endDate = dateTimeFormat.parse(endTime) ?: Date()
        return when {
            isToday(startDate) && isToday(endDate) -> context.getString(
                R.string.today, timeFormat.format(startDate), timeFormat.format(
                    endDate
                )
            )

            isYesterday(startDate) && isToday(endDate)
            -> context.getString(
                R.string.yesterday_today,
                timeFormat.format(startDate),
                timeFormat.format(endDate)
            )

            isYesterday(startDate) -> context.getString(
                R.string.yesterday,
                timeFormat.format(startDate),
                formatDate(endDate),
                timeFormat.format(endDate)
            )

            isYesterday(endDate) -> context.getString(
                R.string.yesterday_end_date,
                formatDate(startDate),
                timeFormat.format(startDate),
                timeFormat.format(endDate)
            )

            dateFormat.format(startDate) == dateFormat.format(endDate) -> context.getString(
                R.string.start_date_time_time,
                formatDate(startDate),
                timeFormat.format(startDate),
                timeFormat.format(endDate)
            )

            else -> context.getString(
                R.string.start_date_time_end_date_time,
                formatDate(startDate),
                timeFormat.format(startDate),
                formatDate(endDate),
                timeFormat.format(endDate)
            )
        }
    }

    private fun formatDate(date: Date): String {
        return dateFormat.format(date)
    }

    private fun isToday(date: Date): Boolean {
        val calendar = Calendar.getInstance()
        val today = calendar.time
        return dateFormat.format(date) == dateFormat.format(today)
    }

    private fun isYesterday(date: Date): Boolean {
        val calendar = Calendar.getInstance()
        calendar.add(Calendar.DATE, -1)
        val yesterday = calendar.time
        return dateFormat.format(date) == dateFormat.format(yesterday)
    }

    fun calculateJobPercentagesWithColors(jobs: List<JobApiModel>?): Map<Color, Double> {
        val totalJobs = jobs?.size ?: 0
        val completedJobs = jobs?.count { it.status == JobStatus.Completed } ?: 0
        val cancelledJobs = jobs?.count { it.status == JobStatus.Canceled } ?: 0
        val inProgressJobs = jobs?.count { it.status == JobStatus.InProgress } ?: 0
        val yetToStartJobs = jobs?.count { it.status == JobStatus.YetToStart } ?: 0
        val inCompleteJobs = jobs?.count { it.status == JobStatus.Incomplete } ?: 0

        val percentagesMap = mapOf(
            LightBlue to (yetToStartJobs.toDouble() / totalJobs) * 100,
            LightCyan to (inProgressJobs.toDouble() / totalJobs) * 100,
            LightYellow to (cancelledJobs.toDouble() / totalJobs) * 100,
            LightGreen to (completedJobs.toDouble() / totalJobs) * 100,
            LightRed to (inCompleteJobs.toDouble() / totalJobs) * 100
        )

        return percentagesMap.toList()
            .sortedByDescending { (_, value) -> value }
            .toMap()
    }

    fun calculateInvoicePercentagesWithColors(invoices: List<InvoiceApiModel>?):  Map<Color, Double> {
        val totalInvoices = invoices?.size ?: 0
        val totalDraft = invoices?.count { it.status == InvoiceStatus.Draft } ?: 0
        val totalPending = invoices?.count { it.status == InvoiceStatus.Pending } ?: 0
        val totalPaid = invoices?.count { it.status == InvoiceStatus.Paid } ?: 0
        val totalBadDebt = invoices?.count { it.status == InvoiceStatus.BadDebt } ?: 0

        val percentagesMap = mapOf(
            LightYellow to (totalDraft.toDouble() / totalInvoices) * 100,
            LightCyan to (totalPending.toDouble() / totalInvoices) * 100,
            LightGreen to (totalPaid.toDouble() / totalInvoices) * 100,
            LightRed to (totalBadDebt.toDouble() / totalInvoices) * 100
        )

        return percentagesMap.toList()
            .sortedByDescending { (_, value) -> value }
            .toMap()
    }

    fun getWelcomeMessage(context: Context, name: String): String {
        return context.getString(R.string.hello, name)
    }

    fun getCurrentDate(context: Context): String {
        val currentDate = LocalDate.now()
        val dayOfWeek = currentDate.dayOfWeek.getDisplayName(TextStyle.FULL, Locale.ENGLISH)
        val month = currentDate.month.getDisplayName(TextStyle.FULL, Locale.ENGLISH)
        val dayOfMonth = currentDate.dayOfMonth
        val year = currentDate.year
        val daySuffix = when (dayOfMonth) {
            1, 21, 31 -> context.getString(R.string.st)
            2, 22 ->  context.getString(R.string.nd)
            3, 23  -> context.getString(R.string.rd)
            else  -> context.getString(R.string.th)
        }
        return "$dayOfWeek, $month $dayOfMonth$daySuffix $year"
    }

    fun formatTotalCollected(invoices: List<InvoiceApiModel>?): String {
        val totalCollected =
            invoices?.filter { it.status == InvoiceStatus.Paid }?.sumOf { it.total } ?: 0
        return NumberFormat.getNumberInstance(Locale.US).format(totalCollected)
    }

    fun formatTotalValue(invoices: List<InvoiceApiModel>?): String {
        val totalValue = invoices?.sumOf { it.total } ?: 0
        return NumberFormat.getNumberInstance(Locale.US).format(totalValue)
    }
}