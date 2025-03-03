package com.avdei.spring1app.util;

public class DurationConverter {
    public static String formatDuration(long durationInMillis) {
        long seconds = durationInMillis / 1000;
        long minutes = seconds / 60;
        long hours = minutes / 60;
        long days = hours / 24;

        hours = hours % 24;
        minutes = minutes % 60;
        seconds = seconds % 60;

        StringBuilder formattedDuration = new StringBuilder();

        if (days > 0) {
            formattedDuration.append(days).append(" дн. ");
        }
        if (hours > 0) {
            formattedDuration.append(hours).append(" ч. ");
        }
        if (minutes > 0) {
            formattedDuration.append(minutes).append(" мин. ");
        }
        if (seconds > 0) {
            formattedDuration.append(seconds).append(" сек.");
        }

        return formattedDuration.toString().trim();
    }
}
