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
}
