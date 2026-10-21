package com.dbuster.cscalculator;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import java.text.DecimalFormat;

public class FinalMark extends Fragment {
    private EditText first, second;
    private TextView result;
    @Nullable @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle saved) {
        View view = inflater.inflate(R.layout.finalmarkfragment_layout, container, false);
        first = view.findViewById(R.id.currentMarkFM);
        second = view.findViewById(R.id.examMarkFM);
        result = view.findViewById(R.id.finalMarkTV);
        if (saved != null) {
            first.setText(saved.getString("first", ""));
            second.setText(saved.getString("second", ""));
            result.setText(saved.getString("result", ""));
        }
        view.findViewById(R.id.fmbutton).setOnClickListener(v -> {
            first.setError(null); second.setError(null);
            final double a, b;
            try { a = GradeCalculator.parseMark(first.getText().toString()); }
            catch (IllegalArgumentException error) { first.setError(error.getMessage()); return; }
            try { b = GradeCalculator.parseMark(second.getText().toString()); }
            catch (IllegalArgumentException error) { second.setError(error.getMessage()); return; }
            result.setText(format(GradeCalculator.finalMark(a, b)));
        });
        return view;
    }
    private String format(double value) { return new DecimalFormat("0.##").format(value); }
    @Override public void onSaveInstanceState(@NonNull Bundle state) {
        super.onSaveInstanceState(state);
        if (first != null) {
            state.putString("first", first.getText().toString());
            state.putString("second", second.getText().toString());
            state.putString("result", result.getText().toString());
        }
    }
    @Override public void onDestroyView() {
        super.onDestroyView(); first = null; second = null; result = null;
    }
}
