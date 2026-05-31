package com.example.rgz;

// Модель ИИ-инструмента (строка таблицы tools).
public class Tool {
    public int id;
    public String name;
    public String vendor;
    public String category;
    public int price;     // $/мес (0 = бесплатно)
    public int quality;   // качество результата 1..5
    public int privacy;   // приватность данных 1..5 (5 = локальный/закрытый)
    public int ease;      // простота освоения 1..5
    public int ide;       // интеграция с IDE 0/1
    public String siteUrl;
    public String risks;
    public double score;  // балл соответствия (после подбора)
}
