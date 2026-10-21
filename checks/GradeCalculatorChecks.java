import com.dbuster.cscalculator.GradeCalculator;
public class GradeCalculatorChecks {
    private static void check(boolean condition) { if (!condition) throw new AssertionError(); }
    public static void main(String[] args) {
        check(Math.abs(GradeCalculator.finalMark(80.9,90.9)-84.9)<1e-9);
        check(!GradeCalculator.requiredExam(0,100).reachable);
        check(GradeCalculator.requiredExam(0,100).maximumFinal==40);
        check(GradeCalculator.requiredExam(100,50).alreadyMet);
        check(Math.abs(GradeCalculator.parseMark("80,9")-80.9)<1e-9);
        for (String value:new String[]{"NaN","Infinity","-1","101","","."}) {
            boolean failed=false;
            try { GradeCalculator.parseMark(value); } catch(IllegalArgumentException expected) { failed=true; }
            check(failed);
        }
        for (int current=0;current<=100;current+=5)
            for (int exam=0;exam<=100;exam+=5) {
                double target=GradeCalculator.finalMark(current,exam);
                check(Math.abs(GradeCalculator.requiredExam(current,target).score-exam)<1e-8);
            }
        System.out.println("CSCalc formula, input validation and inverse checks passed");
    }
}
