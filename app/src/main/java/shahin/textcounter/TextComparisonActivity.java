/*
 * Copyright © 2018 Mohamed Shahin.  All rights reserved.
 */

package shahin.textcounter;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

import butterknife.BindView;
import butterknife.ButterKnife;

public class TextComparisonActivity extends AppCompatActivity {

    @BindView(R.id.et_original_text)
    EditText et_original_text;

    @BindView(R.id.et_changed_text)
    EditText et_changed_text;

    @BindView(R.id.tv_difference_text)
    TextView tv_difference_text;

    @BindView(R.id.btn_copy_original)
    Button btn_copy_original;

    @BindView(R.id.btn_copy_changed)
    Button btn_copy_changed;

    @BindView(R.id.btn_copy_difference)
    Button btn_copy_difference;

    @BindView(R.id.tv_characters)
    TextView tv_characters;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_text_comparison);
        ButterKnife.bind(this);

        MobileAds.initialize(this, "ca-app-pub-1885749404874590~8434977462");

        AdView mAdView = findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);

        btn_copy_original.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String original = et_original_text.getText().toString().trim();
                copy(original);
            }
        });

        btn_copy_changed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String changed = et_changed_text.getText().toString().trim();
                copy(changed);
            }
        });

        btn_copy_difference.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String difference = tv_difference_text.getText().toString().trim();
                copy(difference);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_options_text_comparison, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.home:
                NavUtils.navigateUpFromSameTask(this);
                return true;
            case R.id.action_compare:
                findDifference();
                return true;
            case R.id.action_reset:
                reset();
                return true;
            case R.id.action_count:
                totalCharacters();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }


    private String difference(String str1, String str2) {
        if (str1 == null) {
            return str2;
        }
        if (str2 == null) {
            return str1;
        }
        int at = indexOfDifference(str1, str2);
        if (at == -1) {
            return "";
        }
        return str2.substring(at);
    }

    private int indexOfDifference(String str1, String str2) {
        if (str1 == str2) {
            return -1;
        }
        if (str1 == null || str2 == null) {
            return 0;
        }
        int i;
        for (i = 0; i < str1.length() && i < str2.length(); ++i) {
            if (str1.charAt(i) != str2.charAt(i)) {
                break;
            }
        }
        if (i < str2.length() || i < str1.length()) {
            return i;
        }
        return -1;
    }

    private void findDifference() {

        String original = et_original_text.getText().toString().trim().toLowerCase();
        String changed = et_changed_text.getText().toString().trim().toLowerCase();

        if (original.isEmpty() || changed.isEmpty()) {
            Toast.makeText(getApplicationContext(), R.string.message_text_empty, Toast.LENGTH_SHORT).show();
        } else {
            if (original.equals(changed)) {
                Toast.makeText(getApplicationContext(), R.string.message_no_differences, Toast.LENGTH_SHORT).show();
            } else {
                String textDifferences = difference(original, changed);
                tv_difference_text.setText(textDifferences);
            }
        }

    }

    private void totalCharacters() {
        int original = et_original_text.getText().toString().trim().length();
        int changed = et_changed_text.getText().toString().trim().length();
        int difference = tv_difference_text.getText().toString().trim().length();

        if (original < 1 || changed < 1 || difference < 1) {
            Toast.makeText(getApplicationContext(), R.string.message_text_empty, Toast.LENGTH_SHORT).show();
        } else {
            String report = getString(R.string.report_part_one) + getString(R.string.tab) +
                    String.valueOf(original) + getString(R.string.report_part_two) +
                    getString(R.string.tab) + String.valueOf(changed) +
                    getString(R.string.report_part_three) + getString(R.string.tab) +
                    String.valueOf(difference);
            tv_characters.setText(report);
            Toast.makeText(getApplicationContext(), R.string.char_calculated, Toast.LENGTH_SHORT).show();
        }
    }

    private void copy(String text) {
        if (text.isEmpty()) {
            Toast.makeText(getApplicationContext(), R.string.message_text_empty, Toast.LENGTH_SHORT).show();
        } else {
            ClipboardManager clipboard = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
            ClipData clip = ClipData.newPlainText("Text", text);
            clipboard.setPrimaryClip(clip);
            Toast.makeText(getApplicationContext(), R.string.message_copied, Toast.LENGTH_SHORT).show();
        }
    }

    private void reset() {
        et_original_text.setText("");
        et_changed_text.setText("");
        tv_difference_text.setText("");
        tv_characters.setText(getString(R.string.total_characters));
        Toast.makeText(getApplicationContext(), R.string.clear, Toast.LENGTH_SHORT).show();
    }

}
