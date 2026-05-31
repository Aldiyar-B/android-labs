package com.example.rgz;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

// Ядро РГР: подбор инструмента методом взвешенной суммы с жёсткими фильтрами.
public class Scorer {

    public static final String PROFILE_NOVICE = "novice";
    public static final String PROFILE_TEAM = "team";
    public static final String PROFILE_EXPERT = "expert";

    public String profile = PROFILE_NOVICE;
    public int maxBudget = 30;
    public boolean requirePrivacy = false;
    public boolean requireIde = false;

    public List<Tool> rank(List<Tool> all) {
        double[] w = weights(profile); // {quality, ease, privacy, price, ide}
        List<Tool> result = new ArrayList<>();

        for (Tool t : all) {
            // жёсткие фильтры
            if (t.price > maxBudget) continue;
            if (requirePrivacy && t.privacy < 4) continue;
            if (requireIde && t.ide == 0) continue;

            // нормализация 0..1
            double quality = t.quality / 5.0;
            double ease = t.ease / 5.0;
            double privacy = t.privacy / 5.0;
            double price = 1.0 - Math.min(t.price, 30) / 30.0; // дешевле = лучше
            double ide = t.ide;

            double s = w[0] * quality + w[1] * ease + w[2] * privacy
                    + w[3] * price + w[4] * ide;
            t.score = Math.round(s * 1000) / 10.0; // 0..100
            result.add(t);
        }

        Collections.sort(result, (a, b) -> Double.compare(b.score, a.score));
        return result;
    }

    private double[] weights(String profile) {
        switch (profile) {
            case PROFILE_TEAM:   return new double[]{0.25, 0.10, 0.35, 0.10, 0.20};
            case PROFILE_EXPERT: return new double[]{0.45, 0.10, 0.15, 0.10, 0.20};
            default:             return new double[]{0.20, 0.40, 0.05, 0.30, 0.05};
        }
    }

    public static String profileTitle(String profile) {
        switch (profile) {
            case PROFILE_TEAM: return "Команда";
            case PROFILE_EXPERT: return "Эксперт";
            default: return "Новичок";
        }
    }
}
