package com.dbuster.cscalculator;
import org.junit.Test;
import static org.junit.Assert.*;
public class GradeCalculatorTest {
    @Test public void preservesDecimals() { assertEquals(84.9, GradeCalculator.finalMark(80.9,90.9),1e-9); }
    @Test public void unreachableTarget() {
        GradeCalculator.Requirement r=GradeCalculator.requiredExam(0,100);
        assertFalse(r.reachable); assertEquals(40,r.maximumFinal,1e-9);
    }
    @Test public void inverseFormula() {
        assertEquals(90.9,GradeCalculator.requiredExam(80.9,84.9).score,1e-9);
        assertTrue(GradeCalculator.requiredExam(100,50).alreadyMet);
    }
    @Test(expected=IllegalArgumentException.class) public void rejectsNaN() { GradeCalculator.parseMark("NaN"); }

    @Test public void parsesTrimmedDecimalsAndBothDecimalSeparators() {
        assertEquals(80.9, GradeCalculator.parseMark(" 80,9 "), 1e-9);
        assertEquals(80.9, GradeCalculator.parseMark("80.9"), 1e-9);
        assertEquals(0, GradeCalculator.parseMark("0"), 0);
        assertEquals(100, GradeCalculator.parseMark("100"), 0);
    }

    @Test public void rejectsMissingMalformedAndOutOfRangeInput() {
        for (String value : new String[]{null, "", "  ", "abc", "1,2,3", "Infinity", "-Infinity", "-0.1", "100.1"}) {
            assertThrows(IllegalArgumentException.class, () -> GradeCalculator.parseMark(value));
        }
    }

    @Test public void bothCalculationArgumentsAreValidated() {
        for (double invalid : new double[]{Double.NaN, Double.POSITIVE_INFINITY, Double.NEGATIVE_INFINITY, -1, 101}) {
            assertThrows(IllegalArgumentException.class, () -> GradeCalculator.finalMark(invalid, 50));
            assertThrows(IllegalArgumentException.class, () -> GradeCalculator.finalMark(50, invalid));
            assertThrows(IllegalArgumentException.class, () -> GradeCalculator.requiredExam(invalid, 50));
            assertThrows(IllegalArgumentException.class, () -> GradeCalculator.requiredExam(50, invalid));
        }
    }

    @Test public void exactAndAlreadyMetTargetsClampToValidExamRange() {
        GradeCalculator.Requirement exact = GradeCalculator.requiredExam(100, 60);
        assertTrue(exact.reachable); assertFalse(exact.alreadyMet); assertEquals(0, exact.score, 0);
        GradeCalculator.Requirement lower = GradeCalculator.requiredExam(100, 50);
        assertTrue(lower.reachable); assertTrue(lower.alreadyMet); assertEquals(0, lower.score, 0);
        GradeCalculator.Requirement maximum = GradeCalculator.requiredExam(100, 100);
        assertTrue(maximum.reachable); assertFalse(maximum.alreadyMet); assertEquals(100, maximum.score, 1e-9);
        GradeCalculator.Requirement impossible = GradeCalculator.requiredExam(0, 100);
        assertFalse(impossible.reachable); assertEquals(100, impossible.score, 0);
    }

    @Test public void inverseFormulaRoundTripsAcrossTheMarkRange() {
        for (int current = 0; current <= 100; current += 5) {
            for (int exam = 0; exam <= 100; exam += 5) {
                double target = GradeCalculator.finalMark(current, exam);
                GradeCalculator.Requirement requirement = GradeCalculator.requiredExam(current, target);
                assertTrue(requirement.reachable);
                assertEquals(exam, requirement.score, 1e-8);
                assertEquals(GradeCalculator.finalMark(current, 100), requirement.maximumFinal, 1e-9);
            }
        }
    }
}
