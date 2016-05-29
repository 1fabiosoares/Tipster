package br.com.developbox.tipster;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnKeyListener;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;

public class MainActivity extends AppCompatActivity {
    // Definindo Variaáveis das widgets a Activity
    private EditText txtAmount;
    private EditText txtPeople;
    private EditText txtTipOther;

    private RadioGroup rdoGroupTips;
    private int radioCheckedId = -1;

    private Button btnCalculate;
    private Button btnReset;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Colhendo widgets da Activity
        txtAmount = (EditText) findViewById(R.id.txtAmount);
        txtAmount.setOnKeyListener(mKeyListener);
        txtPeople = (EditText) findViewById(R.id.txtPeople);
        txtPeople.setOnKeyListener(mKeyListener);
        txtTipOther = (EditText) findViewById(R.id.txtTipOther);
        txtTipOther.setOnKeyListener(mKeyListener);
        txtTipOther.setEnabled(false);

        rdoGroupTips = (RadioGroup) findViewById(R.id.radioGroup);

        btnCalculate = (Button) findViewById(R.id.btnCalculate);
        btnCalculate.setOnClickListener(mClickListener);
        btnCalculate.setEnabled(false);
        btnReset = (Button) findViewById(R.id.btnReset);
        btnReset.setOnClickListener(mClickListener);

        rdoGroupTips.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if(checkedId == R.id.op1 || checkedId == R.id.op2 || checkedId == R.id.op3){
                    txtTipOther.setEnabled(false);

                    btnCalculate.setEnabled(txtAmount.getText().length() > 0 && txtAmount.getText().length() > 0);

                }else{
                    txtTipOther.setEnabled(true);
                    txtTipOther.requestFocus();

                    btnCalculate.setEnabled(txtAmount.getText().length() > 0 && txtAmount.getText().length() > 0 && txtTipOther.getText().length() > 0);
                }
                radioCheckedId = checkedId;
            }
        });

        InputMethodManager teclado = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        teclado.showSoftInput(txtAmount, InputMethodManager.SHOW_FORCED);
    }

    // Key Listener
    private OnKeyListener mKeyListener = new OnKeyListener() {
        @Override
        public boolean onKey(View v, int keyCode, KeyEvent event) {
            switch (v.getId()){
                case R.id.txtAmount:
                case R.id.txtPeople:
                    btnCalculate.setEnabled(txtAmount.getText().length() > 0 && txtPeople.getText().length() > 0);
                    break;
                case R.id.txtTipOther:
                    btnCalculate.setEnabled(txtAmount.getText().length() > 0 && txtPeople.getText().length() > 0 && txtTipOther.getText().length() >= 0);
                    break;
            }
            return false;
        }
    };
    // Click Listener
    private OnClickListener mClickListener = new OnClickListener() {
        @Override
        public void onClick(View v) {
            if(v.getId() == R.id.btnCalculate){
                calculate();
            }else{
                reset();
            }
        }
    };



    // Reset
    private void reset(){
        txtAmount.setText("");
        txtPeople.setText("");
        txtTipOther.setText("");

        rdoGroupTips.check(R.id.op1);

        txtAmount.requestFocus();
        InputMethodManager teclado = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        teclado.showSoftInput(txtAmount, InputMethodManager.SHOW_IMPLICIT);

        radioCheckedId = -1;
    }

    // Calculate
    private void calculate(){
        Double billAmount;
        Double totalPeople;
        Double percentage = null;
        boolean isError = false;

        if(!txtAmount.getText().toString().isEmpty()){
            billAmount = Double.parseDouble(txtAmount.getText().toString());
        }else{
            billAmount = null;
        }
        if(!txtPeople.getText().toString().isEmpty()){
            totalPeople = Double.parseDouble(txtPeople.getText().toString());
        }else{
            totalPeople = null;
        }

        if(billAmount < 1.0 || billAmount == null){
            showErrorAlert("Digite um valor válido em \"Valor Total\"", txtAmount.getId());
            isError = true;
        }
        if(totalPeople < 1.0 || totalPeople == null){
            showErrorAlert("Digite um valor válido em \"Pessoas\"", txtPeople.getId());
            isError = true;
        }

        if(radioCheckedId == -1){
            radioCheckedId = rdoGroupTips.getCheckedRadioButtonId();
        }
        if(radioCheckedId == R.id.op1){
            percentage = 10.0;
        }else if(radioCheckedId == R.id.op2){
            percentage = 15.0;
        }else if(radioCheckedId == R.id.op3){
            percentage = 20.0;
        }else if(radioCheckedId == R.id.op4){
            percentage = Double.parseDouble(txtTipOther.getText().toString());
            if(percentage < 0){
                showErrorAlert("Digite um valor válido em \"Outro\"", txtTipOther.getId());
                isError = true;
            }
        }

        if(!isError){
            Double tipAmount = ((billAmount * percentage)/100);
            Double totalToPay = billAmount + tipAmount;
            Double perPersonPays = totalToPay / totalPeople;

            Intent resultActivity = new Intent(this, Results.class);
            Bundle params = new Bundle();

            params.putString("tipAmount", String.format("R$ %.2f", tipAmount));
            params.putString("totalToPay", String.format("R$ %.2f", totalToPay));
            params.putString("perPersonPays", String.format("R$ %.2f", perPersonPays));

            resultActivity.putExtras(params);

            startActivity(resultActivity);

            //resultTipAmount.setText(String.format("R$ %.2f", tipAmount));
            //resultTotalToPay.setText(String.format("R$ %.2f", totalToPay));
            //resultTipPerPerson.setText(String.format("R$ %.2f", perPersonPays));
        }
    }

    // Error Alert
    private void showErrorAlert(String errorMessage, final int fieldId){
        AlertDialog.Builder alertError = new AlertDialog.Builder(this);
        alertError.setTitle("Ops! Aconteceu um erro");
        alertError.setMessage(errorMessage);
        alertError.setPositiveButton("Ok!", new AlertDialog.OnClickListener(){
            public void onClick(DialogInterface dialog, int which){
                findViewById(fieldId).requestFocus();
            }
        });
        alertError.create();
        alertError.show();

        //Toast.makeText(this, errorMessage, Toast.LENGTH_LONG).show();
    }

}
