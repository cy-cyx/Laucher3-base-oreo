package com.theme.lambda.launcher.appwidget

enum class WidgetType(var type: String) {
    None(""),
    XPanel("xpanel"),
    Weather("weather"),
    Clocks("clock"),
    Calendar("calendar"),
}

fun String.toWidgetType(): WidgetType {
    return when (this) {
        "xpanel" -> WidgetType.XPanel
        "weather" -> WidgetType.Weather
        "clock" -> WidgetType.Clocks
        "calendar" -> WidgetType.Calendar
        else -> WidgetType.None
    }
}