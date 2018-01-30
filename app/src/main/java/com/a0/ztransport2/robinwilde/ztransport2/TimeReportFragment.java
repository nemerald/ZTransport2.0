package com.a0.ztransport2.robinwilde.ztransport2;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.view.ContextThemeWrapper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.NumberPicker;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.a0.ztransport2.robinwilde.ztransport2.Objects.TimeReport;

import org.json.JSONObject;

import java.util.ArrayList;

import static com.a0.ztransport2.robinwilde.ztransport2.HelpMethods.vibrate;

public class TimeReportFragment extends Fragment {
    FragmentCommunicator mCallback;
    String driverName;
    String driverId;

    Button bDatePicker, bConfirmAndSendTimeReport;
    TextView tvPickedDate, tvWorkedHours;
    EditText etOtherCostumer, etOtherArea, etWorkDescription;
    Spinner spPickCostumer, spPickArea;
    RadioGroup rgHourData;
    RadioButton rbHour, rbRoute;

    String costumer, area, workDescription, hours;
    int route;

    //TODO ta bort vid senare tillfälle.
    String timeReportUrl = "https://prod-13.northeurope.logic.azure.com:443/workflows/d4b4fdd85821419aa59715ff02484a71/triggers/manual/paths/invoke?api-version=2016-10-01&sp=%2Ftriggers%2Fmanual%2Frun&sv=1.0&sig=u7zvS6QsLtB7z0J7pW65E-vbFbNhHeY6rfMeMPtJqTo";

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try{
            mCallback = (FragmentCommunicator) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString()
                    + " must implement FragmentCommunicator");
        }
    }

    @Override
    public void onDetach() {
        mCallback=null;
        super.onDetach();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        return inflater.inflate(R.layout.fragment_time_report, container, false);
    }
    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {

        tvPickedDate = (TextView) view.findViewById(R.id.tvPickedDate);
        tvWorkedHours = (TextView) view.findViewById(R.id.tvWorkedHours);
        bDatePicker = (Button) view.findViewById(R.id.bShowDateDialog);
        bConfirmAndSendTimeReport = (Button) view.findViewById(R.id.bConfirmAndSendTimeReport);
        spPickCostumer = (Spinner) view.findViewById(R.id.spCostumerPicker);
        spPickArea = (Spinner) view.findViewById(R.id.spAreaPicker);
        etOtherCostumer = (EditText) view.findViewById(R.id.etOtherCustomer);
        etOtherArea = (EditText) view.findViewById(R.id.etOtherArea);
        etWorkDescription = (EditText) view.findViewById(R.id.etWorkDescription);
        rgHourData = (RadioGroup) view.findViewById(R.id.rgHourPicker);
        rbHour = (RadioButton) view.findViewById(R.id.rbHours);
        rbRoute = (RadioButton) view.findViewById(R.id.rbRoute);

        if(!ifDateIsSet()){
            setDateFirstTime();
        }
        setSpinnerValues();
        setDefaultState();
        setSharedPref();

        bDatePicker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DialogFragment picker = new DatePickerFragment();
                picker.show(getFragmentManager(), "datePicker");

            }
        });

        spPickCostumer.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                switch (i){
                    case 0:
                        costumer=getString(R.string.DHL);
                        etOtherCostumer.setEnabled(false);
                        etOtherCostumer.setText("");
                        rbRoute.setEnabled(true);
                        spPickArea.setEnabled(true);
                        area=spPickArea.getSelectedItem().toString();
                        break;
                    case 1:
                        costumer= getString(R.string.DGF);
                        etOtherCostumer.setEnabled(false);
                        etOtherCostumer.setText("");
                        rbRoute.setEnabled(true);
                        spPickArea.setEnabled(true);
                        area=spPickArea.getSelectedItem().toString();
                        break;
                    case 2:
                        costumer=getString(R.string.ALLUM);
                        etOtherCostumer.setEnabled(false);
                        etOtherCostumer.setText("");
                        rbRoute.setEnabled(true);
                        spPickArea.setEnabled(true);
                        area=spPickArea.getSelectedItem().toString();
                        break;
                    case 3:
                        costumer=getString(R.string.CASSELS);
                        etOtherCostumer.setEnabled(false);
                        etOtherCostumer.setText("");
                        spPickArea.setEnabled(false);
                        rgHourData.clearCheck();
                        rbRoute.setEnabled(false);
                        area=getString(R.string.not_applicable);
                        break;
                    case 4:
                        costumer=getString(R.string.OTHER);
                        etOtherCostumer.setEnabled(true);
                        etOtherCostumer.requestFocus();
                        rbRoute.setEnabled(true);
                        spPickArea.setEnabled(true);
                        area=spPickArea.getSelectedItem().toString();
                        InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                        imm.showSoftInput(etOtherCostumer, InputMethodManager.SHOW_IMPLICIT);
                        break;
                    }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        spPickArea.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                switch (i){
                    case 0:
                        area=getString(R.string.BOHUSLAN);
                        etOtherArea.setEnabled(false);
                        etOtherArea.setText("");
                        break;
                    case 1:
                        area=getString(R.string.BORAS);
                        etOtherArea.setEnabled(false);
                        etOtherArea.setText("");
                        break;
                    case 2:
                        area=getString(R.string.HALLAND);
                        etOtherArea.setEnabled(false);
                        etOtherArea.setText("");
                        break;
                    case 3:
                        area=getString(R.string.GOTEBORG);
                        etOtherArea.setEnabled(false);
                        etOtherArea.setText("");
                        break;
                    case 4:
                        area=getString(R.string.OTHER);
                        etOtherArea.setEnabled(true);
                        etOtherArea.requestFocus();
                        InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                        imm.showSoftInput(etOtherArea, InputMethodManager.SHOW_IMPLICIT);
                        break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        rgHourData.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, @IdRes int i) {

                switch (radioGroup.getCheckedRadioButtonId()){
                    case R.id.rbRoute:
                        if(rbRoute.isChecked()){
                            route = 1;
                            hours = "8";
                            tvWorkedHours.setText(hours);
                        }
                        break;
                    case R.id.rbHours:
                        if(rbHour.isChecked()){
                            hourPickerDialog();
                            route = 0;
                            tvWorkedHours.setText(hours);
                        }
                        break;
                }
            }
        });
        bConfirmAndSendTimeReport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(isInputFromMandatoryFields()){
                    showConfirmInputDialog();
                }
            }
        });
    }

    private void hourPickerDialog() {
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View alertCustomLayout = inflater.inflate(R.layout.custom_hour_picker_pialog, null);

        final NumberPicker npHours = (NumberPicker) alertCustomLayout.findViewById(R.id.npHours);
        final NumberPicker npHalfHours = (NumberPicker) alertCustomLayout.findViewById(R.id.npHalfHours);

        npHours.setMinValue(1);
        npHours.setMaxValue(10);
        npHours.setWrapSelectorWheel(true);
        npHours.setValue(8);

        npHalfHours.setMinValue(0);
        npHalfHours.setMaxValue(1);
        npHalfHours.setDisplayedValues( new String[] { "0", "5"} );

        AlertDialog.Builder alert = new AlertDialog.Builder(new ContextThemeWrapper(getActivity(), R.style.AlertDialogCustom));
        alert.setTitle(getString(R.string.set_hours));
        alert.setView(alertCustomLayout);
        alert.setCancelable(false);
        alert.setPositiveButton(getString(R.string.confirm), new DialogInterface.OnClickListener(){

            @Override
            public void onClick(DialogInterface dialog, int which){

            }
        });
        alert.setNegativeButton(getString(R.string.cancel), new DialogInterface.OnClickListener(){

            @Override
            public void onClick(DialogInterface dialog, int which){

            }
        });
        final AlertDialog dialog = alert.create();
        dialog.show();

        int titleDividerId = getResources().getIdentifier("titleDivider", "id", "android");
        View titleDivider = dialog.findViewById(titleDividerId);
        if (titleDivider != null)
            titleDivider.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.white));
        dialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String halfHour;
                if(npHalfHours.getValue()>0){
                    halfHour="5";
                    tvWorkedHours.setText(String.valueOf(npHours.getValue())+"."+halfHour);
                    hours=tvWorkedHours.getText().toString();
                }
                else{
                    tvWorkedHours.setText(String.valueOf(npHours.getValue()));
                    hours=tvWorkedHours.getText().toString();
                }
                dialog.dismiss();
            }
        });
        dialog.getButton(AlertDialog.BUTTON_NEGATIVE).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                rgHourData.clearCheck();
                tvWorkedHours.setText("");
                dialog.dismiss();
            }
        });
    }

    private void showConfirmInputDialog() {
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View alertCustomLayout = inflater.inflate(R.layout.custom_user_confirm_input_data_dialog, null);

        final TextView tvInputDate = (TextView) alertCustomLayout.findViewById(R.id.tvInputDate);
        final TextView tvInputDriverName = (TextView) alertCustomLayout.findViewById(R.id.tvInputDriverName);
        final TextView tvInputCostumer = (TextView) alertCustomLayout.findViewById(R.id.tvInputCostumer);
        final TextView tvInputArea = (TextView) alertCustomLayout.findViewById(R.id.tvInputArea);
        final TextView tvInputIsRoute = (TextView) alertCustomLayout.findViewById(R.id.tvInputIsRoute);
        final TextView tvInputHours = (TextView) alertCustomLayout.findViewById(R.id.tvInputHours);
        final TextView tvInputWorkDescription = (TextView) alertCustomLayout.findViewById(R.id.tvInputWorkDescription);

        if(costumer.equals(getString(R.string.OTHER))){
            costumer = etOtherCostumer.getText().toString();
            costumer=HelpMethods.setFirstCharacterToUpperCase(costumer);
        }
        if(area.equals(getString(R.string.OTHER))){
            area = etOtherArea.getText().toString();
            area=HelpMethods.setFirstCharacterToUpperCase(area);
        }

        tvInputDate.setText(tvPickedDate.getText().toString());
        tvInputDriverName.setText(driverName);
        tvInputCostumer.setText(costumer);
        tvInputArea.setText(area);
        if(route==0){
            tvInputIsRoute.setText(getString(R.string.no));
        }else{
            tvInputIsRoute.setText(getString(R.string.yes));
        }
        tvInputHours.setText(hours);
        tvInputWorkDescription.setText(workDescription);

        AlertDialog.Builder alert = new AlertDialog.Builder(new ContextThemeWrapper(getActivity(), R.style.AlertDialogCustom));
        alert.setTitle(getString(R.string.confirm_input));
        alert.setView(alertCustomLayout);
        alert.setCancelable(false);
        alert.setPositiveButton(getString(R.string.confirm), new DialogInterface.OnClickListener(){

            @Override
            public void onClick(DialogInterface dialog, int which){

            }
        });
        alert.setNegativeButton(getString(R.string.cancel), new DialogInterface.OnClickListener(){

            @Override
            public void onClick(DialogInterface dialog, int which){

            }
        });
        final AlertDialog dialog = alert.create();
        dialog.show();

        int titleDividerId = getResources().getIdentifier("titleDivider", "id", "android");
        View titleDivider = dialog.findViewById(titleDividerId);
        if (titleDivider != null)
            titleDivider.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.white));
        dialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                JSONObject data = (HelpMethods.prepareReportDataObject(createTimeReportFromUserInput()));
                //TODO Control return message and give feedback to user

                DbHelperMethods.postRequester(getContext(), data, timeReportUrl, new VolleyCallback() {
                    @Override
                    public void onSuccess(JSONObject result) {
                        Toast.makeText(getActivity(), getString(R.string.report_success), Toast.LENGTH_SHORT).show();
                        setDefaultState();
                        dialog.dismiss();
                    }

                    @Override
                    public void onError(String message) {
                        Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
                        setDefaultState();
                        dialog.dismiss();
                    }
                });
            }
        });
        dialog.getButton(AlertDialog.BUTTON_NEGATIVE).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
    }
    private TimeReport createTimeReportFromUserInput(){
        ArrayList<String> yearMonthDayWeek = HelpMethods.splitYearMonthDay(tvPickedDate.getText().toString());
        String year = yearMonthDayWeek.get(0);
        String month = yearMonthDayWeek.get(1);
        String day = yearMonthDayWeek.get(2);
        //String day = HelpMethods.trimDayStringIfStartWithZero(yearMonthDayWeek.get(2));
        String week = yearMonthDayWeek.get(3);
        boolean isRoute;
        if(route==0){
            isRoute=false;
        }
        else{
            isRoute=true;
        }

        TimeReport timeReport = new TimeReport(driverId, driverName, HelpMethods.getTimeStamp(), driverId, driverName, year, month, day, week,
                                                costumer, area, hours, isRoute, workDescription,false);
        return timeReport;
    }
    private boolean isInputFromMandatoryFields() {
        if((costumer.equals(getString(R.string.OTHER)) && HelpMethods.checkIfStringIsEmptyOrBlankOrNull(etOtherCostumer.getText().toString())) |
                (area.equals(getString(R.string.OTHER)) && HelpMethods.checkIfStringIsEmptyOrBlankOrNull(etOtherArea.getText().toString()))
                || rgHourData.getCheckedRadioButtonId()==-1){
            Toast.makeText(getActivity(), getString(R.string.error_no_input_from_mandatory_fields), Toast.LENGTH_LONG).show();
            vibrate(getActivity(), getString(R.string.error_vibrate));
            return false;
        }
        else if(HelpMethods.checkIfStringIsEmptyOrBlankOrNull(etWorkDescription.getText().toString())){
            Toast.makeText(getActivity(), getString(R.string.error_no_input_from_mandatory_fields), Toast.LENGTH_LONG).show();
            vibrate(getActivity(), getString(R.string.error_vibrate));
            return false;
        }
        else{
            workDescription = etWorkDescription.getText().toString();
            return true;
        }
    }

    private void setDefaultState() {
        etOtherCostumer.setText("");
        etOtherArea.setText("");
        etWorkDescription.setText("");
        spPickCostumer.setSelection(0);
        spPickArea.setSelection(0);
        rgHourData.clearCheck();
        etOtherArea.setEnabled(false);
        etOtherCostumer.setEnabled(false);
        tvWorkedHours.setText("");
    }

    private void setSpinnerValues() {
        ArrayAdapter<String> adapterCostumer = new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_item, getActivity().getResources().getStringArray(R.array.customer_array));
        adapterCostumer.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spPickCostumer.setAdapter(adapterCostumer);

        ArrayAdapter<String> adapterArea = new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_item, getActivity().getResources().getStringArray(R.array.area_array));
        adapterArea.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spPickArea.setAdapter(adapterArea);
    }

    private boolean ifDateIsSet() {
        if(tvPickedDate.getText().toString().equals("")){
            return false;
        }
        else{
            return true;
        }
    }
    public void setDateFirstTime(){
        tvPickedDate.setText(HelpMethods.getTodaysDate());
    }
    public void setDateFromFragment(String date) {
        tvPickedDate.setText(date);
    }

    public void setSharedPref(){
        SharedPreferences sharedPreferences = this.getActivity().getSharedPreferences(
                getString(R.string.shared_preference_name), Context.MODE_PRIVATE);

        driverName = sharedPreferences.getString(getString(R.string.shared_prefs_user_name), null);
        driverId = sharedPreferences.getString(getString(R.string.shared_prefs_user_id), null);
    }

    public void onRefresh() {
        Toast.makeText(getActivity(), "Fragment Time Report: Refresh called.",
                Toast.LENGTH_SHORT).show();
    }
}
