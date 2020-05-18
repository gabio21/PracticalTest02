package ro.pub.cs.systems.eim.practicaltest02;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Currency;



public class PracticalTest02MainActivity extends AppCompatActivity {
    EditText serverPortEditText;
    Button connectButton;
    EditText currencyEditText;
    Button getCurrencyButton;
    TextView currencyDisplayTextView;
    ServerThread serverThread;
    ClientThread clientThread;


    ConnectButtonClickListener connectButtonClickListener = new ConnectButtonClickListener();

    private class ConnectButtonClickListener implements Button.OnClickListener{

        @Override
        public void onClick(View v) {
            String serverPort = serverPortEditText.getText().toString();
            if(serverPort == null || serverPort.isEmpty()) {
                Toast.makeText(getApplicationContext(), "[MAIN ACTIVITY] Server port should be filled!", Toast.LENGTH_SHORT).show();
                return;
            }
            serverThread = new ServerThread(Integer.valueOf(serverPortEditText.getText().toString()));
            if(serverThread.getServerSocket() == null){
                Log.e("Main", "[MAIN ACTIVITY] Could not create server thread!");
                return;
            }
            serverThread.start();
        }
    }


    GetCurrencyClickListener getCurrencyClickListener = new GetCurrencyClickListener();

    private class GetCurrencyClickListener implements Button.OnClickListener{

        @Override
        public void onClick(View v) {
            String currency = currencyEditText.getText().toString();

            clientThread = new ClientThread("localhost", Integer.parseInt(serverPortEditText.getText().toString()), currency, currencyDisplayTextView);
            clientThread.start();
        }
    }



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_practical_test02_main_);

        serverPortEditText = (EditText)findViewById(R.id.server_port_edit_text);
        currencyEditText = (EditText)findViewById(R.id.currency_edit_text);
        connectButton = (Button)findViewById(R.id.connect_button);
        getCurrencyButton = (Button)findViewById(R.id.get_currency_button);
        currencyDisplayTextView = (TextView)findViewById(R.id.currencyDisplayTextView);

        connectButton.setOnClickListener(connectButtonClickListener);
        getCurrencyButton.setOnClickListener(getCurrencyClickListener);
    }
}
