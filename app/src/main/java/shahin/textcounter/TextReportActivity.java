package shahin.textcounter;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;

import static shahin.textcounter.Constant.MARKET_APP_ID;
import static shahin.textcounter.Constant.PRIVACY_POLICY;
import static shahin.textcounter.Constant.setDialog;

public class TextReportActivity extends AppCompatActivity {

    //View declaration
    @BindView(R.id.et_text)
    EditText et_text;
    @BindView(R.id.chbAutoCount)
    CheckBox chbAutoCount;
    @BindView(R.id.btn_count)
    Button btn_count;
    @BindView(R.id.btn_word_freq)
    Button btn_word_freq;
    @BindView(R.id.btn_lowercase)
    Button btn_lowercase;
    @BindView(R.id.btn_uppercase)
    Button btn_uppercase;
    @BindView(R.id.btn_report)
    Button btn_report;

    //Automatically update the characters count while text is written
    TextWatcher textWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            btn_count.setText(getString(R.string.text_characters) + ": " + s.length());
        }

        @Override
        public void afterTextChanged(Editable s) {
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_text_report);
        ButterKnife.bind(this);

        //Initialize Ad
        MobileAds.initialize(this, getString(R.string.admob_app_id));
        AdView mAdView = findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);

        //Initialize Views
        chbAutoCount.setChecked(false);

        //Set buttons action
        chbAutoCount.setOnClickListener(v -> autoCountTheText());
        btn_count.setOnClickListener(v -> countTheText());
        btn_word_freq.setOnClickListener(v -> wordFrequency());
        btn_lowercase.setOnClickListener(v -> lowercase());
        btn_uppercase.setOnClickListener(v -> uppercase());
        btn_report.setOnClickListener(v -> getTextReport());

        //Hide the keyboard from displaying at the beginning
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_text_report, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_copy:
                copyTheText();
                return true;
            case R.id.action_reset:
                resetAll();
                return true;
            case R.id.action_share:
                share();
                return true;
            case R.id.action_comparison_activity:
                startActivity(new Intent(this, TextComparisonActivity.class));
                return true;
            case R.id.action_rate:
                startActivity(new Intent(Intent.ACTION_VIEW,
                        Uri.parse(MARKET_APP_ID)));
                return true;
            case R.id.action_policy:
                openPrivacyPolicy();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    //Auto count the text
    private void autoCountTheText() {
        if (chbAutoCount.isChecked()) {
            Toast.makeText(this, R.string.message_auto_count_on, Toast.LENGTH_SHORT).show();
            et_text.addTextChangedListener(textWatcher);
        } else {
            Toast.makeText(this, R.string.message_auto_count_off, Toast.LENGTH_SHORT).show();
            et_text.removeTextChangedListener(textWatcher);
        }
    }

    //Counting the text
    private void countTheText() {
        int total = et_text.getText().toString().trim().length();

        if (total < 1) {
            Toast.makeText(this, R.string.message_text_empty, Toast.LENGTH_SHORT).show();
        } else {
            btn_count.setText(getString(R.string.text_characters) + ": " + total);
        }
    }

    //Copy the text
    private void copyTheText() {
        String text = et_text.getText().toString().trim();
        if (text.isEmpty()) {
            Toast.makeText(this, R.string.message_text_empty, Toast.LENGTH_SHORT).show();
        } else {
            setCopy(text);
        }
    }

    //Reset all field to default state
    private void resetAll() {
        if (et_text.getText().toString().isEmpty()) {
            Toast.makeText(this, R.string.message_text_empty, Toast.LENGTH_SHORT).show();
        } else {
            chbAutoCount.setChecked(false);
            et_text.setText("");
            btn_count.setText(getString(R.string.text_characters));
            et_text.removeTextChangedListener(textWatcher);
        }
    }


    //Print text report
    private void getTextReport() {
        String text = et_text.getText().toString().trim();
        int numberOfWords = text.split("\\s+").length;
        int numberOfSpaces = text.split(" ").length - 1;
        int numberOfCharacters = text.length();
        if (text.isEmpty()) {
            Toast.makeText(this, R.string.message_text_empty, Toast.LENGTH_SHORT).show();
        } else {
            setDialog(this, "Text Report", "Number of words: " + numberOfWords + "\n" +
                    "Number of spaces: " + numberOfSpaces + "\n" +
                    "Number of characters: " + (numberOfCharacters - numberOfSpaces));
        }
    }

    //Print word frequency
    private void wordFrequency() {
        String text = et_text.getText().toString().trim().toLowerCase();
        if (text.isEmpty()) {
            Toast.makeText(this, R.string.message_text_empty, Toast.LENGTH_SHORT).show();
        } else {
            String[] words = text.replaceAll("[^a-zA-Z\']", " ").split(" ");
            String wordFreq = "";
            Map<String, Integer> map = new HashMap<>();
            for (String w : words) {
                Integer n = map.get(w);
                n = (n == null) ? 1 : ++n;
                map.put(w, n);
            }
            for (Map.Entry<String, Integer> entry : map.entrySet()) {
                String key = entry.getKey();
                int value = entry.getValue();

                wordFreq += "\n" + key + getString(R.string.tab) + value;
            }
            setDialog(this, getString(R.string.word_frequency), wordFreq);
        }
    }

    //Set the letters to uppercase
    private void uppercase() {
        String text = et_text.getText().toString().toUpperCase();
        if (text.isEmpty()) {
            Toast.makeText(this, R.string.message_text_empty, Toast.LENGTH_SHORT).show();
        } else {
            setDialog(this, getString(R.string.text_uppercase), text);
            setCopy(text);
        }
    }

    //Set the letters to lowercase
    private void lowercase() {
        String text = et_text.getText().toString().toLowerCase();
        if (text.isEmpty()) {
            Toast.makeText(this, R.string.message_text_empty, Toast.LENGTH_SHORT).show();
        } else {
            setDialog(this, getString(R.string.text_lowercase), text);
            setCopy(text);
        }
    }

    //Share the text with other Apps
    private void share() {
        String text = et_text.getText().toString();
        if (text.isEmpty()) {
            Toast.makeText(this, R.string.message_text_empty, Toast.LENGTH_SHORT).show();
        } else {
            Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
            sharingIntent.setType("text/plain");
            sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, getString(R.string.share_subject));
            sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, text);
            startActivity(Intent.createChooser(sharingIntent, getString(R.string.share_text)));
        }
    }

    //Copy the text
    private void setCopy(String str) {
        ClipboardManager clipboardManager = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
        ClipData clipData = ClipData.newPlainText("Text", str);
        if (clipboardManager != null) {
            clipboardManager.setPrimaryClip(clipData);
        }
        Toast.makeText(this, R.string.message_copied, Toast.LENGTH_SHORT).show();
    }

    //Navigate to privacy policy terms and conditions
    private void openPrivacyPolicy() {
        Intent i = new Intent(Intent.ACTION_VIEW);
        i.setData(Uri.parse(PRIVACY_POLICY));
        startActivity(i);
    }
}
