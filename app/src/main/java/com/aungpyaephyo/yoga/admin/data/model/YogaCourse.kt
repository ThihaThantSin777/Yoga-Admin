package com.aungpyaephyo.yoga.admin.data.model

import kotlinx.datetime.DayOfWeek
import java.text.DecimalFormat

data class YogaCourse(
    val id: String,

    val dayOfWeek: DayOfWeek,
    val time: String,
    val duration: String,

    val capacity: Int,
    val pricePerClass: Double,

    val typeOfClass: YogaClassType,
    val description: String,
    val difficultyLevel: DifficultyLevel,
   // val targetAudience: TargetAudience,

//    val cancellationPolicy: CancellationPolicy,

    val classes: List<YogaClass>,

//    val images: List<YogaImage>,

    val eventType: YogaEventType,
    val latitude: Double,
    val longitude: Double,
    val onlineUrl: String
) {
    val displayPrice: String = let {
        val decimalFormat = DecimalFormat("#.##")
        val formattedValue = decimalFormat.format(pricePerClass)
        if (formattedValue.endsWith(".00")) {
            formattedValue.substring(0, formattedValue.length - 2)
        } else {
            formattedValue
        }
    }
}
