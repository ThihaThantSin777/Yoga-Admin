package com.aungpyaephyo.yoga.admin.utils

import com.aungpyaephyo.yoga.admin.data.model.DifficultyLevel
import com.aungpyaephyo.yoga.admin.data.model.YogaCourse

//import com.aungpyaephyo.yoga.admin.data.model.CancellationPolicy
//import com.aungpyaephyo.yoga.admin.data.model.TargetAudience
import com.aungpyaephyo.yoga.admin.data.model.YogaClass
import com.aungpyaephyo.yoga.admin.data.model.YogaClassType
import com.aungpyaephyo.yoga.admin.data.model.YogaEventType
import java.time.DayOfWeek
import java.util.Random
import java.util.UUID

/**
 * A data com.aungpyaephyo.yoga.admin.ui.theme.getProvider that generates dummy yoga course data for development and testing purposes.
 */
object DummyDataProvider {

    val dummyYogaCourses = generateSampleYogaCourses()

    /**
     * Generates a list of sample yoga courses.
     *
     * @return A list of [YogaCourse] objects representing sample yoga courses.
     */
    private fun generateSampleYogaCourses(): List<YogaCourse> {
        val courses = mutableListOf<YogaCourse>()
        val random = Random()

        for (i in 1..10) {
            val id = UUID.randomUUID().toString()
            val dayOfWeek = DayOfWeek.entries[random.nextInt(DayOfWeek.entries.size)]
            val time = "${
                (9 + random.nextInt(6)).toString().padStart(2, '0')
            }:00" // Random time between 09:00 and 15:00
            val capacity = 10 + random.nextInt(15)
            val duration = 60 + random.nextInt(30) // Duration in minutes (60 to 90)
            val pricePerClass = 15.242323
            val typeOfClass = YogaClassType.entries[random.nextInt(YogaClassType.entries.size)]
            val description = "This yoga class is designed to nurture your mind, body, and spirit. We'll explore a variety of poses and breathing techniques to increase flexibility, strength, and balance. You'll learn to connect with your breath, quiet your mind, and cultivate a sense of inner peace. This class is suitable for all levels, from beginners to experienced practitioners. Join us on the mat and discover the transformative power of yoga."
            val difficultyLevel =
                DifficultyLevel.entries[random.nextInt(DifficultyLevel.entries.size)]
//            val cancellationPolicy =
//                CancellationPolicy.entries[random.nextInt(CancellationPolicy.entries.size)]
//            val targetAudience = TargetAudience.entries[random.nextInt(TargetAudience.entries.size)]

            val classes = generateSampleYogaClasses()

            courses.add(
                YogaCourse(
                    id = id,
                    dayOfWeek = dayOfWeek,
                    time = time,
                    capacity = capacity,
                    duration = duration.toString(),
                    pricePerClass = pricePerClass,
                    typeOfClass = typeOfClass,
                    description = description,
                    difficultyLevel = difficultyLevel,
//                    cancellationPolicy = cancellationPolicy,
//                    targetAudience = targetAudience,
                    classes = classes,
//                    images = listOf(
//                        YogaImage(
//                            id = UUID.randomUUID().toString(),
//                            bitmap = null,
//                            courseId = "",
//                            base64 = ""
//                        ),
//                        YogaImage(
//                            id = UUID.randomUUID().toString(),
//                            bitmap = null,
//                            courseId = "",
//                            base64 = ""
//                        ),
//                        YogaImage(
//                            id = UUID.randomUUID().toString(),
//                            bitmap = null,
//                            courseId = "",
//                            base64 = ""
//                        ),
//                        YogaImage(
//                            id = UUID.randomUUID().toString(),
//                            bitmap = null,
//                            courseId = "",
//                            base64 = ""
//                        )
//                    ),
                    eventType = YogaEventType.ONLINE,
                    onlineUrl = "",
                    latitude = 0.0,
                    longitude = 0.0
                )
            )
        }

        return courses
    }
}

/**
 * Generates a list of sample yoga classes.
 *
 * @return A list of [YogaClass] objects representing sample yoga classes.
 */
fun generateSampleYogaClasses(): List<YogaClass> {
    val classes = mutableListOf<YogaClass>()
    val random = Random()
    val teachers = listOf("Amy", "John", "Sarah", "David")

    for (i in 1..4) {
        val id = UUID.randomUUID().toString()
        val date = "19/11/2023"
        val comment = listOf(
            "This class is designed for relaxation and stress relief. " +
                    "It incorporates gentle movements, breathing exercises, and guided meditation. " +
                    "Wear comfortable clothing and bring a blanket for extra warmth during the final relaxation.",
            "This class is physically challenging and is recommended for students with some yoga experience. " +
                    "It will involve advanced poses and variations. " +
                    "Listen to your body and take breaks when needed. " +
                    "Please inform the instructor of any injuries or limitations before class.",
            "This class emphasizes the flow and connection between poses. " +
                    "It is a dynamic and invigorating practice that will build strength, flexibility, and stamina. " +
                    "Be prepared to move and sweat! " +
                    "Hydrate well before and after class."
        )[random.nextInt(3)]

        classes.add(
            YogaClass(
                id = id,
                date = date,
                courseId = "",
                comment = comment,
                teachers = teachers,
            )
        )
    }

    return classes
}