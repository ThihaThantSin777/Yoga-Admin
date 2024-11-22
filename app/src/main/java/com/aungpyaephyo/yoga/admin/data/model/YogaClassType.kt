package com.aungpyaephyo.yoga.admin.data.model

enum class YogaClassType(val displayName: String) {
    DYNAMIC_FLOW_YOGA("Dynamic Flow Yoga"),
  ELEVATE_AERIAL_YOGA("Elevate Aerial Yoga"),
    HARMONY_FAMILY_YOGA("Harmony Family Yoga"),
   EMPOWER_HATHA_YOGA("Empower Hatha Yoga"),
    TRANQUIL_YIN_YOGA("Tranquil Yin Yoga");

    companion object {
        fun getDisplayNames(): List<String> = entries.map { it.displayName }

        fun fromDisplayName(name: String): YogaClassType? = entries.find { it.displayName.equals(name, ignoreCase = true) }
    }
}