package ro.pub.cs.systems.eim.practicaltest02;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.util.Currency;



public class PracticalTest02MainActivity extends AppCompatActivity {
    EditText serverPortEditText;
    Button connectButton;
    EditText currencyEditText;
    Button getCurrencyButton;
    TextView currencyDisplayTextView;


    



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_practical_test02_main_);

        serverPortEditText = (EditText)findViewById(R.id.server_port_edit_text);
        currencyEditText = (EditText)findViewById(R.id.currency_edit_text);
        connectButton = (Button)findViewById(R.id.connect_button);
        getCurrencyButton = (Button)findViewById(R.id.get_currency_button);
        currencyDisplayTextView = (TextView)findViewById(R.id.currencyDisplayTextView);
    }
}
