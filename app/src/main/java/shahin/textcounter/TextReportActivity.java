/*
 * Copyright © 2018 Mohamed Shahin.  All rights reserved.
 */

package shahin.textcounter;

import android.app.AlertDialog;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;

public class TextReportActivity extends AppCompatActivity {

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

    @BindView(R.id.btn_reset)
    Button btn_reset;

    TextWatcher textWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            btn_count.setText(getString(R.string.total_characters) + ": " + s.length());
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

        MobileAds.initialize(this, "ca-app-pub-1885749404874590~8434977462");

        AdView mAdView = findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);

        chbAutoCount.setChecked(false);

        //Auto count the text
        chbAutoCount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                autoCountTheText();
            }
        });

        //Counting the text
        btn_count.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                countTheText();
            }
        });

        btn_word_freq.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                wordFrequency();
            }
        });

        btn_lowercase.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                lowercase();
            }
        });

        btn_uppercase.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                uppercase();
            }
        });
        btn_reset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resetAll();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_options_text_report, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_copy:
                copyTheText();
                return true;
            case R.id.action_report:
                report();
                return true;
            case R.id.action_share:
                share();
                return true;
            case R.id.action_comparison_activity:
                startActivity(new Intent(this, TextComparisonActivity.class));
                return true;
            case R.id.action_email_us:
                sendEmail();
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
            btn_count.setText(getString(R.string.total_characters) + ": " + String.valueOf(total));
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

    private void resetAll() {

        if (et_text.getText().toString().isEmpty()) {
            Toast.makeText(this, R.string.message_text_empty, Toast.LENGTH_SHORT).show();
        } else {
            chbAutoCount.setChecked(false);
            et_text.setText("");
            btn_count.setText(getString(R.string.total_characters));
            et_text.removeTextChangedListener(textWatcher);
        }

    }

    private void report() {

        String text = et_text.getText().toString().trim();
        int numberOfWords = text.split("\\s+").length;
        int numberOfSpaces = text.split(" ").length - 1;
        int numberOfCharacters = text.length();


        if (text.isEmpty()) {
            Toast.makeText(this, R.string.message_text_empty, Toast.LENGTH_SHORT).show();
        } else {
            setDialog("Text Report", "Number of words: " + String.valueOf(numberOfWords) + "\n" +
                    "Number of spaces: " + String.valueOf(numberOfSpaces) + "\n" +
                    "Number of characters: " + String.valueOf(numberOfCharacters - numberOfSpaces));
        }
    }

    private void wordFrequency() {
        String text = et_text.getText().toString().trim().toLowerCase();

        if (text.isEmpty()) {
            Toast.makeText(this, R.string.message_text_empty, Toast.LENGTH_SHORT).show();
        } else {
            String[] words = text.replaceAll("[^a-zA-Z ]", "").split(" ");
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

            setDialog(getString(R.string.word_frequency), wordFreq);
        }

    }

    private void uppercase() {
        String text = et_text.getText().toString().toUpperCase();

        if (text.isEmpty()) {
            Toast.makeText(this, R.string.message_text_empty, Toast.LENGTH_SHORT).show();
        } else {
            setDialog(getString(R.string.text_uppercase), text);
            setCopy(text);
        }
    }

    private void lowercase() {
        String text = et_text.getText().toString().toLowerCase();

        if (text.isEmpty()) {
            Toast.makeText(this, R.string.message_text_empty, Toast.LENGTH_SHORT).show();
        } else {
            setDialog(getString(R.string.text_lowercase), text);
            setCopy(text);
        }
    }

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

    private void setDialog(String title, String str) {
        AlertDialog.Builder builder;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            builder = new AlertDialog.Builder(this, android.R.style.Theme_Material_Dialog_Alert);
        } else {
            builder = new AlertDialog.Builder(this);
        }
        builder.setTitle(title)
                .setMessage(str)
                .setNegativeButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // do nothing
                    }
                })
                .setIcon(android.R.drawable.ic_dialog_info)
                .show();
    }

    private void setCopy(String str) {
        ClipboardManager clipboardManager = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
        ClipData clipData = ClipData.newPlainText("Text", str);
        if (clipboardManager != null) {
            clipboardManager.setPrimaryClip(clipData);
        }
        Toast.makeText(this, R.string.message_copied, Toast.LENGTH_SHORT).show();
    }

    private void sendEmail() {
        Intent i = new Intent(Intent.ACTION_SEND);
        i.setType("message/rfc822");
        i.putExtra(Intent.EXTRA_EMAIL, new String[]{"M7edshin@gmail.com"});
        i.putExtra(Intent.EXTRA_SUBJECT, getString(R.string.suggest_report));
        i.putExtra(Intent.EXTRA_TEXT, getString(R.string.your_message));
        try {
            startActivity(Intent.createChooser(i, "Send mail..."));
        } catch (android.content.ActivityNotFoundException ex) {
            Toast.makeText(this, R.string.error_message_no_apps, Toast.LENGTH_SHORT).show();
        }
    }
}
