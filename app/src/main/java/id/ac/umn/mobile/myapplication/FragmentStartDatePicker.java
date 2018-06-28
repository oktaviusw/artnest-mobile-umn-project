package id.ac.umn.mobile.myapplication;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.widget.DatePicker;

import java.util.Calendar;

public class FragmentStartDatePicker extends DialogFragment implements DatePickerDialog.OnDateSetListener{


    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState){
        // Use the current date as the default date in the picker.
        final Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);
        return new DatePickerDialog(getActivity(), this, year, month, day);
    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int day) {
        // Set the activity to the Main Activity.
        ActivityCreateCommission commissionOrderActivity = (ActivityCreateCommission) getActivity();
        // Invoke Main Activity's processDatePickerResult() method.
        commissionOrderActivity.ProcessStartDatePickerResult(year, month, day);
    }
}
