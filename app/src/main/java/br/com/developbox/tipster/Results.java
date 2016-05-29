package br.com.developbox.tipster;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class Results extends AppCompatActivity {
    private String tipAmount;
    private String totalToPay;
    private String perPersonPays;

    private TextView resultTipAmount;
    private TextView resultTotalToPay;
    private TextView resultTipPerPerson;

    private Button okButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_results);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        this.resultTipAmount = (TextView) findViewById(R.id.resultTipAmount);
        this.resultTotalToPay = (TextView) findViewById(R.id.resultTotalToPay);
        this.resultTipPerPerson = (TextView) findViewById(R.id.resultTipPerPerson);

        this.okButton = (Button) findViewById(R.id.okButton);

        this.okButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        Intent it = getIntent();
        if(it != null){
            Bundle params  = it.getExtras();
            if(params != null){
                this.tipAmount = params.getString("tipAmount");
                this.totalToPay = params.getString("totalToPay");
                this.perPersonPays = params.getString("perPersonPays");

                this.resultTipAmount.setText(this.tipAmount);
                this.resultTotalToPay.setText(this.totalToPay);
                this.resultTipPerPerson.setText(this.perPersonPays);
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.main, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        switch (item.getItemId()){
            case android.R.id.home:
                finish();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }


}
