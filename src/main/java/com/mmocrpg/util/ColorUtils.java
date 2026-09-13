package com.mmocrpg.util;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public final class ColorUtils {

    private static final Pattern HEX_PATTERN = Pattern.compile("&#([A-Fa-f0-9]{6})");
    private static final Pattern GRADIENT_PATTERN = Pattern.compile("<gradient:(#[A-Fa-f0-9]{6}):(#[A-Fa-f0-9]{6})>(.*?)</gradient>");
    private static final Pattern RAINBOW_PATTERN = Pattern.compile("<rainbow>(.*?)</rainbow>");

    private ColorUtils() {
    }

    public static String colorize(String text) {
        if (text == null) return "";
        text = applyGradient(text);
        text = applyRainbow(text);
        text = applyHexColors(text);
        text = text.replace("&0", "§0").replace("&1", "§1").replace("&2", "§2")
                .replace("&3", "§3").replace("&4", "§4").replace("&5", "§5")
                .replace("&6", "§6").replace("&7", "§7").replace("&8", "§8")
                .replace("&9", "§9").replace("&a", "§a").replace("&b", "§b")
                .replace("&c", "§c").replace("&d", "§d").replace("&e", "§e")
                .replace("&f", "§f").replace("&k", "§k").replace("&l", "§l")
                .replace("&m", "§m").replace("&n", "§n").replace("&o", "§o")
                .replace("&r", "§r");
        return text;
    }

    public static Component component(String text) {
        return LegacyComponentSerializer.legacySection().deserialize(colorize(text));
    }

    public static String stripColor(String text) {
        return text.replaceAll("§[0-9a-fk-or]", "").replaceAll("&[0-9a-fk-or]", "")
                .replaceAll("&#([A-Fa-f0-9]{6})", "");
    }

    private static String applyHexColors(String text) {
        Matcher matcher = HEX_PATTERN.matcher(text);
        StringBuilder sb = new StringBuilder();
        while (matcher.find()) {
            String hex = matcher.group(1);
            matcher.appendReplacement(sb, "§x§" +
                    hex.charAt(0) + "§" + hex.charAt(1) +
                    hex.charAt(2) + "§" + hex.charAt(3) +
                    hex.charAt(4) + "§" + hex.charAt(5));
        }
        matcher.appendTail(sb);
        return sb.toString();
    }

    private static String applyGradient(String text) {
        Matcher matcher = GRADIENT_PATTERN.matcher(text);
        StringBuilder sb = new StringBuilder();
        while (matcher.find()) {
            String color1 = matcher.group(1);
            String color2 = matcher.group(2);
            String content = matcher.group(3);
            matcher.appendReplacement(sb, Matcher.quoteReplacement(applyGradientToText(content, color1, color2)));
        }
        matcher.appendTail(sb);
        return sb.toString();
    }

    private static String applyGradientToText(String text, String hex1, String hex2) {
        String stripped = stripColor(text);
        if (stripped.isEmpty()) return text;
        int r1 = Integer.parseInt(hex1.substring(1, 3), 16);
        int g1 = Integer.parseInt(hex1.substring(3, 5), 16);
        int b1 = Integer.parseInt(hex1.substring(5, 7), 16);
        int r2 = Integer.parseInt(hex2.substring(1, 3), 16);
        int g2 = Integer.parseInt(hex2.substring(3, 5), 16);
        int b2 = Integer.parseInt(hex2.substring(5, 7), 16);
        StringBuilder result = new StringBuilder();
        for (int i = 0; i < stripped.length(); i++) {
            float ratio = (float) i / (stripped.length() - 1);
            int r = (int) (r1 + (r2 - r1) * ratio);
            int g = (int) (g1 + (g2 - g1) * ratio);
            int b = (int) (b1 + (b2 - b1) * ratio);
            String hex = String.format("§x§%s§%s§%s§%s§%s§%s",
                    Integer.toHexString(r).charAt(0), Integer.toHexString(g).charAt(0),
                    Integer.toHexString(b).charAt(0),
                    r > 15 ? "" + Integer.toHexString(r).charAt(1) : "" + Integer.toHexString(r).charAt(0),
                    g > 15 ? "" + Integer.toHexString(g).charAt(1) : "" + Integer.toHexString(g).charAt(0),
                    b > 15 ? "" + Integer.toHexString(b).charAt(1) : "" + Integer.toHexString(b).charAt(0));
            result.append(hex).append(stripped.charAt(i));
        }
        return result.toString();
    }

    private static String applyRainbow(String text) {
        Matcher matcher = RAINBOW_PATTERN.matcher(text);
        StringBuilder sb = new StringBuilder();
        while (matcher.find()) {
            String content = matcher.group(1);
            String stripped = stripColor(content);
            StringBuilder rainbow = new StringBuilder();
            float hueStep = 1.0f / Math.max(stripped.length(), 1);
            for (int i = 0; i < stripped.length(); i++) {
                float hue = i * hueStep;
                int rgb = java.awt.Color.HSBtoRGB(hue, 1.0f, 1.0f);
                String hex = String.format("%06x", rgb & 0xFFFFFF);
                rainbow.append("&#").append(hex).append(stripped.charAt(i));
            }
            matcher.appendReplacement(sb, Matcher.quoteReplacement(rainbow.toString()));
        }
        matcher.appendTail(sb);
        return sb.toString();
    }

    public static String formatTime(long millis) {
        long seconds = millis / 1000;
        long minutes = seconds / 60;
        long hours = minutes / 60;
        if (hours > 0) return hours + "h " + (minutes % 60) + "m";
        if (minutes > 0) return minutes + "m " + (seconds % 60) + "s";
        return seconds + "s";
    }

    public static String formatNumber(long number) {
        if (number >= 1_000_000) return String.format("%.1fM", number / 1_000_000.0);
        if (number >= 1_000) return String.format("%.1fK", number / 1_000.0);
        return String.valueOf(number);
    }

    public static String translate(String text) {
        return colorize(text);
    }
}
