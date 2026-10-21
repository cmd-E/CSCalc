package com.dbuster.cscalculator;

/** Grade rules independent of Android views and display rounding. */
public final class GradeCalculator {
    private static final double CURRENT_WEIGHT = 0.6;
    private static final double EXAM_WEIGHT = 0.4;
    private static final double EPSILON = 1e-9;
    private GradeCalculator() {}
    public static double parseMark(String text) {
        if (text == null || text.trim().isEmpty())
            throw new IllegalArgumentException("Поле не должно быть пустым");
        final double value;
        try { value = Double.parseDouble(text.trim().replace(',', '.')); }
        catch (NumberFormatException error) { throw new IllegalArgumentException("Введите число"); }
        validate(value);
        return value;
    }
    private static void validate(double value) {
        if (Double.isNaN(value) || Double.isInfinite(value) || value < 0 || value > 100)
            throw new IllegalArgumentException("Введите значение от 0 до 100");
    }
    public static double finalMark(double current, double exam) {
        validate(current); validate(exam);
        return current * CURRENT_WEIGHT + exam * EXAM_WEIGHT;
    }
    public static Requirement requiredExam(double current, double target) {
        validate(current); validate(target);
        double required = (target - CURRENT_WEIGHT * current) / EXAM_WEIGHT;
        return new Requirement(required <= 100 + EPSILON,
                Math.max(0, Math.min(100, required)), finalMark(current, 100), required < -EPSILON);
    }
    public static final class Requirement {
        public final boolean reachable, alreadyMet;
        public final double score, maximumFinal;
        private Requirement(boolean reachable, double score, double maximumFinal, boolean alreadyMet) {
            this.reachable = reachable; this.score = score;
            this.maximumFinal = maximumFinal; this.alreadyMet = alreadyMet;
        }
    }
}
