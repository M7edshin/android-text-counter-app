package shahin.textcounter;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import butterknife.BindView;
import butterknife.ButterKnife;

public class CompareTwo extends AppCompatActivity {

    //Views Declaration (Using ButterKnife Library)
    @BindView(R.id.txtTopString)
    EditText txtTopString;
    @BindView(R.id.btnTopStringCount)
    Button btnTopStringCount;
    @BindView(R.id.imgTopStringCopy)
    ImageView imgTopStringCopy;
    @BindView(R.id.imgTopStringClear)
    ImageView imgTopStringClear;
    @BindView(R.id.chkTopStringAutoCount)
    CheckBox chkTopStringAutoCount;

    @BindView(R.id.txtBottomString)
    EditText txtBottomString;
    @BindView(R.id.btnBottomStringCount)
    Button btnBottomStringCount;
    @BindView(R.id.imgBottomStringCopy)
    ImageView imgBottomStringCopy;
    @BindView(R.id.imgBottomStringClear)
    ImageView imgBottomStringClear;
    @BindView(R.id.chkBottomStringAutoCount)
    CheckBox chkBottomStringAutoCount;

    //Global variables
    private String copiedText;
    private int topStringCharacters;
    private int BottomStringCharacters;

    private final TextWatcher textWatcherTopString = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            btnTopStringCount.setText("Count: " + String.valueOf(charSequence.length()) + " Char.");
        }

        @Override
        public void afterTextChanged(Editable editable) {

        }
    };

    private final TextWatcher textWatcherBottomString = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            btnBottomStringCount.setText("Count: " + String.valueOf(charSequence.length()) + " Char.");
        }

        @Override
        public void afterTextChanged(Editable editable) {

        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_compare_two);
        ButterKnife.bind(this);

        btnTopStringCount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                countTopString();
            }
        });

        imgTopStringCopy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                copyTopString();
            }
        });

        imgTopStringClear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                clearTopString();
            }
        });

        chkTopStringAutoCount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                autoCountTopString();
            }
        });

        btnBottomStringCount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                countBottomString();
            }
        });

        imgBottomStringCopy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                copyBottomString();
            }
        });

        imgBottomStringClear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                clearBottomString();
            }
        });

        chkBottomStringAutoCount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                autoCountBottomString();
            }
        });

    }

    /**
     * Count characters in a string
     */
    private void countTopString(){
        topStringCharacters = txtTopString.getText().toString().trim().length();
        btnTopStringCount.setText("Count: " + String.valueOf(topStringCharacters) + " Char.");

    }

    /**
     * Copy the string text
     */
    private void copyTopString(){
        Toast.makeText(getApplicationContext(), R.string.str_toast_text_copied, Toast.LENGTH_SHORT).show();
        copiedText = txtTopString.getText().toString();
        ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
        ClipData clip = ClipData.newPlainText("TopString", copiedText);
        clipboard.setPrimaryClip(clip);
    }

    /**
     * Clear the EditText view
     */
    private void clearTopString(){
        txtTopString.setText("");
    }

    /**
     * Perform auto counting characters
     */
    private void autoCountTopString(){
        if(chkTopStringAutoCount.isChecked()){
            Toast.makeText(getApplicationContext(), R.string.str_toast_auto_text_on, Toast.LENGTH_SHORT).show();
            txtTopString.addTextChangedListener(textWatcherTopString);
        }else{
            Toast.makeText(getApplicationContext(), R.string.str_toast_auto_text_off, Toast.LENGTH_SHORT).show();
            txtTopString.removeTextChangedListener(textWatcherTopString);
        }
    }

    /**
     * Count characters in a string
     */
    private void countBottomString(){
        BottomStringCharacters = txtBottomString.getText().toString().trim().length();
        btnBottomStringCount.setText("Count: " + String.valueOf(BottomStringCharacters) + " Char.");
    }

    /**
     * Copy the string text
     */
    private void copyBottomString(){
        Toast.makeText(getApplicationContext(), R.string.str_toast_text_copied, Toast.LENGTH_SHORT).show();
        copiedText = txtBottomString.getText().toString();
        ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
        ClipData clip = ClipData.newPlainText("BottomString", copiedText);
        clipboard.setPrimaryClip(clip);
    }

    /**
     * Clear the EditText view
     */
    private void clearBottomString(){
        txtBottomString.setText("");
    }

    /**
     * Perform auto counting characters
     */
    private void autoCountBottomString(){
        if(chkBottomStringAutoCount.isChecked()){
            Toast.makeText(getApplicationContext(), R.string.str_toast_auto_text_on, Toast.LENGTH_SHORT).show();
            txtBottomString.addTextChangedListener(textWatcherBottomString);
        }else{
            Toast.makeText(getApplicationContext(), R.string.str_toast_auto_text_off, Toast.LENGTH_SHORT).show();
            txtBottomString.removeTextChangedListener(textWatcherBottomString);
        }
    }
}
