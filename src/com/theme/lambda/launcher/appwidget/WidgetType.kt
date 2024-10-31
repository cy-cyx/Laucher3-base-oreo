package com.theme.lambda.launcher.appwidget

enum class WidgetType(var type: String) {
    None(""),
    XPanel("xpanel"),
    Battery1("battery_1"),
    Battery2("battery_2"),
    Weather("weather"),
    Clocks("clock"),
    Calendar("calendar"),
    WidgetDiy("widget_diy");
}

fun String.toWidgetType(): WidgetType {
    return when (this) {
        "xpanel" -> WidgetType.XPanel
        "battery_1" -> WidgetType.Battery1
        "battery_2" -> WidgetType.Battery2
        "weather" -> WidgetType.Weather
        "clock" -> WidgetType.Clocks
        "calendar" -> WidgetType.Calendar
        "widget_diy" -> WidgetType.WidgetDiy
        else -> WidgetType.None
    }
}