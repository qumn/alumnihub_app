package xyz.qumn.alumnihub_app.util

import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

val FORMATTER = DateTimeFormatter.ISO_LOCAL_DATE_TIME

/**
 * Convert LocalDateTime to view format
 * in same day, show time
 * in same year, show month and day
 * else, show year
 */
fun LocalDateTime.toViewFormat(): String {
    val now = LocalDateTime.now()
    return if (this.year == now.year) {
        if (this.dayOfYear == now.dayOfYear) {
            this.format(DateTimeFormatter.ofPattern("HH:mm"))
        } else {
            this.format(DateTimeFormatter.ofPattern("MM-dd HH:mm"))
        }
    } else {
        this.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"))
    }
}
