package shahin.textcounter;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
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
import android.widget.ImageView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

import butterknife.BindView;
import butterknife.ButterKnife;

public class CompareTwo extends AppCompatActivity {

    //Views Declaration (Using ButterKnife Library)
    @BindView(R.id.txtTopString)
    EditText txtTopString;
    @BindView(R.id.btnTopStringCount)
    Button btnTopStringCount;
    private final TextWatcher textWatcherTopString = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            btnTopStringCount.setText(getString(R.string.str_count) + String.valueOf(charSequence.length()) + getString(R.string.str_char));
        }

        @Override
        public void afterTextChanged(Editable editable) {

        }
    };
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
    private final TextWatcher textWatcherBottomString = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            btnBottomStringCount.setText(getString(R.string.str_count) + String.valueOf(charSequence.length()) + getString(R.string.str_char));
        }

        @Override
        public void afterTextChanged(Editable editable) {

        }
    };
    @BindView(R.id.imgBottomStringCopy)
    ImageView imgBottomStringCopy;
    @BindView(R.id.imgBottomStringClear)
    ImageView imgBottomStringClear;
    @BindView(R.id.chkBottomStringAutoCount)
    CheckBox chkBottomStringAutoCount;
    @BindView(R.id.btnTakeTheDiff)
    Button btnTakeTheDiff;
    @BindView(R.id.txtDifferentString)
    EditText txtDifferentString;
    @BindView(R.id.btnCountDiff)
    Button btnCountDiff;
    @BindView(R.id.imgCopyDiff)
    ImageView imgCopyDiff;
    @BindView(R.id.imgClearDiff)
    ImageView imgClearDiff;
    //Global variables
    private String copiedText;
    private int topStringCharacters;
    private int bottomStringCharacters;
    private int diffStringCharacters;

    //Show the difference between two strings
    private static String difference(String str1, String str2) {
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

    private static int indexOfDifference(String str1, String str2) {
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

        btnTakeTheDiff.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (txtTopString.getText().toString().isEmpty() || txtBottomString.getText().toString().isEmpty()) {
                    Toast.makeText(getApplicationContext(), R.string.str_enter_strings_first, Toast.LENGTH_SHORT).show();
                }

                if (txtTopString.getText().toString().length() > txtBottomString.getText().toString().length()) {
                    Toast.makeText(getApplicationContext(), R.string.cannot_compare_length, Toast.LENGTH_SHORT).show();
                }

                String difference = difference(txtTopString.getText().toString(), txtBottomString.getText().toString());
                int length = difference.length();
                txtDifferentString.setText(difference);
                btnCountDiff.setText(getString(R.string.str_count) + String.valueOf(length) + getString(R.string.str_char));
            }
        });

        btnCountDiff.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                countDiff();
            }
        });

        imgCopyDiff.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                copyDiff();
            }
        });

        imgClearDiff.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                clearDiff();
            }
        });


    }

    /**
     * Count characters in a string
     */
    private void countTopString() {
        topStringCharacters = txtTopString.getText().toString().trim().length();
        if (topStringCharacters < 1) {
            Toast.makeText(getApplicationContext(), R.string.str_pls_enter_txt_first, Toast.LENGTH_SHORT).show();
        } else {
            btnTopStringCount.setText(getString(R.string.str_count) + String.valueOf(topStringCharacters) + getString(R.string.str_char));
        }
    }

    /**
     * Copy the string text
     */
    private void copyTopString() {
        copiedText = txtTopString.getText().toString();
        if (copiedText.isEmpty()) {
            Toast.makeText(getApplicationContext(), R.string.str_pls_enter_txt_first, Toast.LENGTH_SHORT).show();
        } else {
            ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
            ClipData clip = ClipData.newPlainText(getString(R.string.str_topstring), copiedText);
            clipboard.setPrimaryClip(clip);
            Toast.makeText(getApplicationContext(), R.string.str_toast_text_copied, Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * Clear the EditText view
     */
    private void clearTopString() {
        if (txtTopString.getText().toString().isEmpty()) {
            Toast.makeText(getApplicationContext(), R.string.str_pls_enter_txt_first, Toast.LENGTH_SHORT).show();
        } else {
            txtTopString.setText("");
        }
    }

    /**
     * Perform auto counting characters
     */
    private void autoCountTopString() {
        if (chkTopStringAutoCount.isChecked()) {
            Toast.makeText(getApplicationContext(), R.string.str_toast_auto_text_on, Toast.LENGTH_SHORT).show();
            txtTopString.addTextChangedListener(textWatcherTopString);
        } else {
            Toast.makeText(getApplicationContext(), R.string.str_toast_auto_text_off, Toast.LENGTH_SHORT).show();
            txtTopString.removeTextChangedListener(textWatcherTopString);
        }
    }

    /**
     * Count characters in a string
     */
    private void countBottomString() {
        bottomStringCharacters = txtBottomString.getText().toString().trim().length();
        if (bottomStringCharacters < 1) {
            Toast.makeText(getApplicationContext(), R.string.str_pls_enter_txt_first, Toast.LENGTH_SHORT).show();
        } else {
            btnBottomStringCount.setText(getString(R.string.str_count) + String.valueOf(bottomStringCharacters) + getString(R.string.str_char));
        }
    }

    /**
     * Copy the string text
     */
    private void copyBottomString() {
        copiedText = txtBottomString.getText().toString();
        if (copiedText.isEmpty()) {
            Toast.makeText(getApplicationContext(), R.string.str_pls_enter_txt_first, Toast.LENGTH_SHORT).show();
        } else {
            ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
            ClipData clip = ClipData.newPlainText(getString(R.string.str_bottomstring), copiedText);
            clipboard.setPrimaryClip(clip);
            Toast.makeText(getApplicationContext(), R.string.str_toast_text_copied, Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * Clear the EditText view
     */
    private void clearBottomString() {
        txtBottomString.setText("");
    }

    /**
     * Perform auto counting characters
     */
    private void autoCountBottomString() {
        if (chkBottomStringAutoCount.isChecked()) {
            Toast.makeText(getApplicationContext(), R.string.str_toast_auto_text_on, Toast.LENGTH_SHORT).show();
            txtBottomString.addTextChangedListener(textWatcherBottomString);
        } else {
            Toast.makeText(getApplicationContext(), R.string.str_toast_auto_text_off, Toast.LENGTH_SHORT).show();
            txtBottomString.removeTextChangedListener(textWatcherBottomString);
        }
    }

    /**
     * Count characters in a string
     */
    private void countDiff() {
        diffStringCharacters = txtDifferentString.getText().toString().trim().length();
        if (diffStringCharacters < 1) {
            Toast.makeText(getApplicationContext(), R.string.str_compare_strings_first, Toast.LENGTH_SHORT).show();
        } else {
            btnCountDiff.setText(getString(R.string.str_count) + String.valueOf(diffStringCharacters) + getString(R.string.str_char));
        }
    }

    /**
     * Copy the string text
     */
    private void copyDiff() {
        copiedText = txtDifferentString.getText().toString();
        if (copiedText.isEmpty()) {
            Toast.makeText(getApplicationContext(), R.string.str_compare_strings_first, Toast.LENGTH_SHORT).show();
        } else {
            ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
            ClipData clip = ClipData.newPlainText(getString(R.string.str_diffstring), copiedText);
            clipboard.setPrimaryClip(clip);
            Toast.makeText(getApplicationContext(), R.string.str_toast_text_copied, Toast.LENGTH_SHORT).show();
        }

    }

    /**
     * Clear the EditText view
     */
    private void clearDiff() {
        txtDifferentString.setText("");
    }

    /**
     * Methods relate to the Menu option
     *
     * @param menu
     * @return
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu_options items for use in the action bar
        getMenuInflater().inflate(R.menu.menu_options, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.option_how_to_use:
                startActivity(new Intent(this, HowToUseActivity.class));
                return true;
            case R.id.btnSuggest:
                sendEmail("Suggest or Report");
                return true;
            case R.id.btnExit:
                finish();
                System.exit(0);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * Sending email to the developer based on the user
     *
     * @param text
     */
    private void sendEmail(String text) {
        Intent i = new Intent(Intent.ACTION_SEND);
        i.setType("message/rfc822");
        i.putExtra(Intent.EXTRA_EMAIL, new String[]{"M7edshin@gmail.com"});
        i.putExtra(Intent.EXTRA_SUBJECT, text);
        i.putExtra(Intent.EXTRA_TEXT, "Your Message");
        try {
            startActivity(Intent.createChooser(i, "Send mail..."));
        } catch (android.content.ActivityNotFoundException ex) {
            Toast.makeText(CompareTwo.this, "There are no email clients or apps installed on your device.", Toast.LENGTH_SHORT).show();
        }
    }
}
