package com.a0.ztransport2.robinwilde.ztransport2;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.view.ContextThemeWrapper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.NumberPicker;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.a0.ztransport2.robinwilde.ztransport2.Objects.PalletBalanceUpdater;
import com.a0.ztransport2.robinwilde.ztransport2.Objects.PalletReport;
import com.a0.ztransport2.robinwilde.ztransport2.Objects.TimeReport;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

import static com.a0.ztransport2.robinwilde.ztransport2.HelpMethods.vibrate;

public class PalletReportFragment extends Fragment {
    FragmentCommunicator mCallback;

    CheckBox cbStartPalletReport;
    Spinner spGetPalletsFromPicker, spLeavePalletsToPicker;
    TextView tvGetPalletsFrom, tvLeavePalletsTo, tvNoOfPallets, tvPalletBalanceJBL, tvPalletBalanceHede, tvPalletBalanceFashionService;
    Button bPickNoOfPallets, bConfirmAndSendPalletReport;

    ProgressDialog mProgressDialog;
    //TODO ta bort vid senare tillfälle.
    String palletReportUrl = "https://prod-14.northeurope.logic.azure.com:443/workflows/23eb5ee9035d42dba41a6eaa58c14734/triggers/manual/paths/invoke?api-version=2016-10-01&sp=%2Ftriggers%2Fmanual%2Frun&sv=1.0&sig=1_phvvdrr-NVB8pS1EWzoThkZyvg1MpcVzu9KtISod4";
    String getPalletBalanceUrl = "https://prod-15.northeurope.logic.azure.com:443/workflows/6533211b3671489e993edb348f13c3c4/triggers/request/paths/invoke?api-version=2016-10-01&sp=%2Ftriggers%2Frequest%2Frun&sv=1.0&sig=F24liE1532lDaexqEG8neoK_QVQIo4fMfctwg80B5pg";
    String palletBalanceUpdaterUrl = "https://prod-01.northeurope.logic.azure.com:443/workflows/67e40f7e1daf4eeb8f111cbb37f4c9a7/triggers/request/paths/invoke?api-version=2016-10-01&sp=%2Ftriggers%2Frequest%2Frun&sv=1.0&sig=jHvh0qp4-tfcTS81bxO5hDqbPwCcYYZZMD4nTXDAZcg";

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
        mCallback = null;
        super.onDetach();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        return inflater.inflate(R.layout.fragment_pallet_report, container, false);
    }
    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        cbStartPalletReport = (CheckBox) view.findViewById(R.id.cbStartPalletReport);
        spGetPalletsFromPicker = (Spinner) view.findViewById(R.id.spGetPalletsFromPicker);
        spLeavePalletsToPicker = (Spinner) view.findViewById(R.id.spLeavePalletsToPicker);
        bPickNoOfPallets = (Button) view.findViewById(R.id.bPickNoOfPallets);
        bConfirmAndSendPalletReport = (Button) view.findViewById(R.id.bConfirmAndSendPalletReport);
        tvGetPalletsFrom = (TextView) view.findViewById(R.id.tvGetPalletFrom);
        tvLeavePalletsTo = (TextView) view.findViewById(R.id.tvLeavePalletTo);
        tvNoOfPallets = (TextView) view.findViewById(R.id.tvNoOfPallets);
        tvPalletBalanceJBL = (TextView) view.findViewById(R.id.tvPalletBalanceJBL);
        tvPalletBalanceHede = (TextView) view.findViewById(R.id.tvPalletBalanceHede);
        tvPalletBalanceFashionService = (TextView) view.findViewById(R.id.tvPalletBalanceFashionService);


        setDefaultState();

        cbStartPalletReport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(cbStartPalletReport.isChecked()){
                    spGetPalletsFromPicker.setEnabled(true);
                    spLeavePalletsToPicker.setEnabled(true);
                    bConfirmAndSendPalletReport.setEnabled(true);
                    bPickNoOfPallets.setEnabled(true);
                    setSpinnerValues();

                    mProgressDialog = new ProgressDialog(getActivity());
                    mProgressDialog.setTitle(getString(R.string.wait));
                    mProgressDialog.setMessage(getString(R.string.getting_pallet_balance));
                    mProgressDialog.show();

                    DbHelperMethods.getRequester(getActivity(), getPalletBalanceUrl, null, new VolleyCallback() {
                        @Override
                        public void onSuccess(JSONObject responseObject) {
                            mProgressDialog.dismiss();
                            JSONObject lastRow = getLastRowFromResult(responseObject);
                            if(lastRow==null){
                                Toast.makeText(getActivity(), getString(R.string.error_getting_last_row), Toast.LENGTH_SHORT).show();
                            }else{
                                palletBalance(lastRow);
                            }
                        }

                        @Override
                        public void onError(String message) {
                            mProgressDialog.dismiss();
                            Toast.makeText(getActivity(), getString(R.string.error_message)+" "+ message, Toast.LENGTH_SHORT).show();
                        }
                    });
                }
                else{
                    setDefaultState();
                }
            }
        });

        bPickNoOfPallets.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                palletPickerDialog();
            }
        });
        bConfirmAndSendPalletReport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(isInputCorrect()){
                    showConfirmInputDialog();
                }
            }
        });
    }

    private JSONObject getLastRowFromResult(JSONObject responseObject) {
        JSONObject lastRow = null;
        JSONArray palletArray = null;
        try {
            palletArray = responseObject.getJSONArray("palletArray");
            lastRow = palletArray.getJSONObject(palletArray.length()-1);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return lastRow;
    }

    public void palletBalance(JSONObject lastRow){
        try {
            String timeStamp = (String) lastRow.get("InputTimeStamp");
            String jblBalance = (String) lastRow.get("JBLBalance");
            String hedeBalance = (String) lastRow.get("HedeBalance");
            String fashionServiceBalance = (String) lastRow.get("FashionServiceBalance");

            tvPalletBalanceJBL.setText(jblBalance);
            tvPalletBalanceHede.setText(hedeBalance);
            tvPalletBalanceFashionService.setText(fashionServiceBalance);

        }catch(Exception e){
            e.printStackTrace();
        }
    }

    private void showConfirmInputDialog() {
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View alertCustomLayout = inflater.inflate(R.layout.custom_user_confirm_input_pallets_dialog, null);

        final TextView tvPalletsFromPlace = (TextView) alertCustomLayout.findViewById(R.id.tvPalletsFromPlace);
        final TextView tvPalletsToPlace = (TextView) alertCustomLayout.findViewById(R.id.tvPalletsToPlace);
        final TextView tvNoOfPickedPallets = (TextView) alertCustomLayout.findViewById(R.id.tvNoOfPickedPallets);

        tvPalletsFromPlace.setText(spGetPalletsFromPicker.getSelectedItem().toString());
        tvPalletsToPlace.setText(spLeavePalletsToPicker.getSelectedItem().toString());
        tvNoOfPickedPallets.setText(tvNoOfPallets.getText().toString());

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

                final String fromPlace = tvPalletsFromPlace.getText().toString();
                final String toPlace = tvPalletsToPlace.getText().toString();
                final String noOfPallets = tvNoOfPickedPallets.getText().toString();

                mProgressDialog = new ProgressDialog(getActivity());
                mProgressDialog.setTitle(getString(R.string.wait));
                mProgressDialog.setMessage(getString(R.string.sending_pallet_report));
                mProgressDialog.show();

                JSONObject data = (HelpMethods.prepareReportDataObject(createPalletReportFromUserInput(fromPlace, toPlace, noOfPallets)));
                DbHelperMethods.postRequester(getActivity(), data, palletReportUrl, new VolleyCallback() {
                    @Override
                    public void onSuccess(JSONObject result) {
                        mProgressDialog.dismiss();
                        Toast.makeText(getActivity(),getString(R.string.report_success), Toast.LENGTH_SHORT).show();
                        updatePalletBalance(fromPlace, toPlace, noOfPallets);
                    }
                    @Override
                    public void onError(String message) {
                        mProgressDialog.dismiss();
                        Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
                    }
                });
                setDefaultState();
                dialog.dismiss();
            }
        });
        dialog.getButton(AlertDialog.BUTTON_NEGATIVE).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
    }

    private void updatePalletBalance(String fromPlace, String toPlace, String noOfPallets) {
        int newFromBalance = 0;
        int newToBalance = 0;
        int jblBalance = Integer.parseInt(tvPalletBalanceJBL.getText().toString());
        int hedeBalance = Integer.parseInt(tvPalletBalanceHede.getText().toString());
        int fsBalance = Integer.parseInt(tvPalletBalanceFashionService.getText().toString());
        int noOfPalletsInt = Integer.parseInt(noOfPallets);

        if(fromPlace.equals(getString(R.string.palletBalanceJBL))){
            jblBalance = jblBalance - noOfPalletsInt;
        }
        if(fromPlace.equals(getString(R.string.palletBalanceHede))){
            hedeBalance = hedeBalance - noOfPalletsInt;
        }
        if(fromPlace.equals(getString(R.string.palletBalanceFashionService))){
            fsBalance = fsBalance - noOfPalletsInt;
        }
        if(toPlace.equals(getString(R.string.palletBalanceJBL))){
            jblBalance = jblBalance + noOfPalletsInt;
        }
        if(toPlace.equals(getString(R.string.palletBalanceHede))){
            hedeBalance = hedeBalance + noOfPalletsInt;
        }
        if(toPlace.equals(getString(R.string.palletBalanceFashionService))){
            fsBalance = fsBalance + noOfPalletsInt;
        }
        sendBalanceUpdate(String.valueOf(jblBalance), String.valueOf(hedeBalance), String.valueOf(fsBalance));
    }

    private void sendBalanceUpdate(final String jblBalance, final String hedeBalance, final String fsBalance) {
        String timeStamp = HelpMethods.getTimeStamp();
        PalletBalanceUpdater palletBalanceUpdater = new PalletBalanceUpdater(timeStamp, jblBalance,
                                                                             hedeBalance, fsBalance, false);

        mProgressDialog = new ProgressDialog(getActivity());
        mProgressDialog.setTitle(getString(R.string.wait));
        mProgressDialog.setMessage(getString(R.string.updating_pallet_balance_on_server));
        mProgressDialog.show();

        DbHelperMethods.postRequester(getActivity(), HelpMethods.prepareReportDataObject(palletBalanceUpdater),
                palletBalanceUpdaterUrl, new VolleyCallback() {
                    @Override
                    public void onSuccess(JSONObject result) {
                        mProgressDialog.dismiss();

                        tvPalletBalanceJBL.setText(String.valueOf(jblBalance));
                        tvPalletBalanceHede.setText(String.valueOf(hedeBalance));
                        tvPalletBalanceFashionService.setText(String.valueOf(fsBalance));

                        Toast.makeText(getActivity(), getString(R.string.pallet_balance_update_success), Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onError(String message) {
                        mProgressDialog.dismiss();
                        Toast.makeText(getActivity(), getString(R.string.pallet_balance_update_failed), Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private PalletReport createPalletReportFromUserInput(String fromPlace, String toPlace, String noOfPallets) {

        SharedPreferences sharedPreferences = this.getActivity().getSharedPreferences(
                getString(R.string.shared_preference_name), Context.MODE_PRIVATE);

        String driverName = sharedPreferences.getString(getString(R.string.shared_prefs_user_name), null);
        String driverId = sharedPreferences.getString(getString(R.string.shared_prefs_user_id), null);

        PalletReport palletReport = new PalletReport(HelpMethods.getTimeStamp(), driverName, driverId,
                                                     fromPlace, toPlace, noOfPallets, driverName);

        return palletReport;
    }

    private void setDefaultState() {
        cbStartPalletReport.setChecked(false);
        spGetPalletsFromPicker.setEnabled(false);
        spLeavePalletsToPicker.setEnabled(false);
        bConfirmAndSendPalletReport.setEnabled(false);
        bPickNoOfPallets.setEnabled(false);
        spGetPalletsFromPicker.setSelection(0);
        spLeavePalletsToPicker.setSelection(0);
        tvNoOfPallets.setText("");
    }

    private boolean isInputCorrect() {
        if(spGetPalletsFromPicker.getSelectedItem().toString().equals(spLeavePalletsToPicker.getSelectedItem().toString())){
            vibrate(getActivity(), getString(R.string.error_vibrate));
            Toast.makeText(getActivity(), getString(R.string.error_same_pickup_and_leave_place), Toast.LENGTH_SHORT).show();
            return false;
        }
        else if(tvNoOfPallets.getText().equals("")){
            vibrate(getActivity(), getString(R.string.error_vibrate));
            Toast.makeText(getActivity(), getString(R.string.error_not_picked_pallets), Toast.LENGTH_SHORT).show();
            return false;
        }
        else{
            return true;
        }
    }
    private void palletPickerDialog() {
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View alertCustomLayout = inflater.inflate(R.layout.custom_pallet_picker_pialog, null);

        final NumberPicker npPallets = (NumberPicker) alertCustomLayout.findViewById(R.id.npPallets);

        npPallets.setMinValue(1);
        npPallets.setMaxValue(100);
        npPallets.setWrapSelectorWheel(true);
        npPallets.setValue(10);

        AlertDialog.Builder alert = new AlertDialog.Builder(new ContextThemeWrapper(getActivity(), R.style.AlertDialogCustom));
        alert.setTitle(getString(R.string.set_no_of_pallets));
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
                tvNoOfPallets.setText(String.valueOf(npPallets.getValue()));
                dialog.dismiss();
            }
        });
        dialog.getButton(AlertDialog.BUTTON_NEGATIVE).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tvNoOfPallets.setText("");
                dialog.dismiss();
            }
        });
    }
    private void setSpinnerValues() {
        ArrayAdapter<String> adapterPalletsFrom = new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_item, getActivity().getResources().getStringArray(R.array.pallet_places_array));
        adapterPalletsFrom.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spGetPalletsFromPicker.setAdapter(adapterPalletsFrom);
        spLeavePalletsToPicker.setAdapter(adapterPalletsFrom);
    }
    public void onRefresh() {
        Toast.makeText(getActivity(), "Fragment Pallets: Refresh called.",
                Toast.LENGTH_SHORT).show();
    }
}
