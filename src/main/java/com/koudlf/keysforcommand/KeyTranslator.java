package com.koudlf.keysforcommand;

import org.lwjgl.glfw.GLFW;

import java.lang.reflect.Modifier;
import java.util.Arrays;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public class KeyTranslator {

    public static final Set<Map.Entry<Integer, String>> KEYS = Arrays.stream(GLFW.class.getDeclaredFields())
            .filter(field -> Modifier.isPublic(field.getModifiers()) && Modifier.isStatic(field.getModifiers()) && field.getName().startsWith("GLFW_KEY_"))
            .collect(Collectors.toMap(
                    field -> {
                        try {
                            return field.getInt(null);
                        } catch (IllegalAccessException e) {
                            throw new RuntimeException(e);
                        }
                    },
                    field -> field.getName().substring(9).toLowerCase(),
                    (oldValue, newValue) -> oldValue
            )).entrySet();

    public static String translateKeyCode(int keyCode) {
            for (Map.Entry<Integer, String> e : KEYS) {
                if (e.getKey() == keyCode) {
                    return e.getValue();
                }
            }
        return Integer.toString(keyCode);
    }

    public static int translateKeyCode(String key) {
        key = key.toLowerCase();
        for (Map.Entry<Integer, String> e : KEYS) {
            if (e.getValue().equals(key)) {
                return e.getKey();
            }
        }
        return -2;
    }

    public static String translateMouseCode(int mouseCode) {
        return switch (mouseCode) {
            case GLFW.GLFW_MOUSE_BUTTON_LEFT -> "left";
            case GLFW.GLFW_MOUSE_BUTTON_RIGHT -> "right";
            case GLFW.GLFW_MOUSE_BUTTON_MIDDLE -> "middle";
            default -> Integer.toString(mouseCode);
        };
    }

    public static int translateMouseCode(String mouse) {
        mouse = mouse.toLowerCase();
        return switch (mouse) {
            case "left" -> GLFW.GLFW_MOUSE_BUTTON_LEFT;
            case "right" -> GLFW.GLFW_MOUSE_BUTTON_RIGHT;
            case "middle" -> GLFW.GLFW_MOUSE_BUTTON_MIDDLE;
            default -> mouse.matches("\\d+") ? Integer.parseInt(mouse) : -2;
        };
    }

    public static String translateOtherCode(int opCode) {
        return switch (opCode) {
            case 1 -> "mouse_scroll";
            case 2 -> "send_message";
            default -> "unknown";
        };
    }

    public static int translateOtherCode(String other) {
        other = other.toLowerCase();
        return switch (other) {
            case "mouse_scroll" -> 1;
            case "send_message" -> 2;
            default -> -2;
        };
    }
}
