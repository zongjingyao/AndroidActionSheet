package cn.zjy.actionsheetexample;

import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import cn.zjy.actionsheet.ActionSheet;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button btnActionSheet = (Button) findViewById(R.id.btn_action_sheet);
        btnActionSheet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showActionSheet();
            }
        });
    }

    private void showActionSheet() {
        ActionSheet actionSheet = new ActionSheet.Builder()
                .setTitle("Title", Color.BLUE)
                .setOtherBtn(new String[]{"Btn0", "Btn1", "Btn2"}, new int[]{Color.BLACK, Color.GREEN, Color.GREEN})
                .setCancelBtn("Cancel", Color.RED)
                .setCancelableOnTouchOutside(true)
                .setActionSheetListener(new ActionSheet.ActionSheetListener() {
                    @Override
                    public void onDismiss(ActionSheet actionSheet, boolean isByBtn) {
                        Toast.makeText(MainActivity.this, "onDismiss: " + isByBtn, Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onButtonClicked(ActionSheet actionSheet, int index) {
                        Toast.makeText(MainActivity.this, "onButtonClicked: " + index, Toast.LENGTH_SHORT).show();
                    }
                }).build();

        actionSheet.show(getFragmentManager());
    }
}
